package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.haier.hailian.contract.config.HacLoginConfig;
import com.haier.hailian.contract.config.shiro.HacLoginToken;
import com.haier.hailian.contract.dto.HacLoginDto;
import com.haier.hailian.contract.dto.HacLoginRespDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.RegisterDto;
import com.haier.hailian.contract.entity.AppStatistic;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysUser;
import com.haier.hailian.contract.service.AppStatisticService;
import com.haier.hailian.contract.service.HacLoginService;
import com.haier.hailian.contract.service.SysUserService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Created by 19012964 on 2019/12/17.
 */

@Service
public class HacLoginServiceImpl implements HacLoginService{
    private final Logger log =  LoggerFactory.getLogger(HacLoginServiceImpl.class);

    @Autowired
    private HacLoginConfig hacLoginConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppStatisticService appStatisticService;
    @Autowired
    private SysUserService sysUserService;

    private Gson gson=new Gson();

    @Override
    public R login(HacLoginDto loginDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String basic=hacLoginConfig.getAppKey()+":"+hacLoginConfig.getAppSecret();
        headers.set("Authorization", "Basic "+Base64Utils.encodeToString(basic.getBytes()));

        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(loginDto), headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(hacLoginConfig.getLoginUri(),
                    entity, String.class);

            String value = responseEntity.getBody();
            log.info("=====Hac返回结果{}", value);

            HacLoginRespDto loginRespDto=gson.fromJson(value,HacLoginRespDto.class);
            if("0".equals(loginRespDto.getCode())){
                //登陆成功
                HacLoginToken token = new HacLoginToken(loginDto.getUserName());
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();

                new Thread(new Runnable(){
                    public void run(){
                        AppStatistic appStatistic=new AppStatistic();
                        appStatistic.setCreateTime(new Date());
                        appStatistic.setEmpSn(sysUser.getEmpSn());
                        appStatistic.setPage("UV");
                        appStatistic.setSource("AppLogin");
                        appStatisticService.save(appStatistic);
                    }
                }).start();


                return R.ok().put(Constant.JWT_AUTH_HEADER, subject.getSession().getId())
                        .put("data", sysUser);
            }else{
                return R.error(Constant.CODE_LOGINFAIL, loginRespDto.getMessage());
            }
        }catch (Exception e){
            log.error("=====登录异常{}====>", e.getMessage());
            throw new RException("登录异常");
        }
    }

    @Override
    public boolean hasCellphone(String cellphone) {
        if(StringUtils.isBlank(cellphone)){
            return  false;
        }
        SysUser entity=sysUserService.getOne(new QueryWrapper<SysUser>()
        .eq("userphone",cellphone));
        return entity!=null;
    }

    @Override
    public R register(RegisterDto dto) {
        if(dto==null){
            return R.error();
        }
        if(hasCellphone(dto.getCellphone())){
            return R.error(Constant.CODE_DATA_FOUND,"手机号"+Constant.MSG_DATA_FOUND);
        }
        SysUser user=new SysUser();
        user.setUserphone(dto.getCellphone());
        user.setUsername(dto.getCellphone());
        user.setPassword(Md5Util.getMD5(dto.getPassword()));
        user.setCreateTime(new Date());
        sysUserService.save(user);
        return R.ok();
    }

    @Override
    public R loginVirtual(String empSn, String lqhy) {
        if(Constant.lqhyVirturl.equals(lqhy)){
            //登陆成功
            HacLoginToken token = new HacLoginToken(empSn);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
            new Thread(new Runnable(){
                public void run(){
                    AppStatistic appStatistic=new AppStatistic();
                    appStatistic.setCreateTime(new Date());
                    appStatistic.setEmpSn(sysUser.getEmpSn());
                    appStatistic.setPage("UV");
                    appStatistic.setSource("WebLogin");
                    appStatisticService.save(appStatistic);
                }
            }).start();
            return R.ok().put(Constant.JWT_AUTH_HEADER, subject.getSession().getId())
                    .put("data", sysUser);
        }else{
            throw new RException("非法请求");
        }
    }
}
