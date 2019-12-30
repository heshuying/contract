package com.haier.hailian.contract.service.impl;

import com.google.gson.Gson;
import com.haier.hailian.contract.config.HacLoginConfig;
import com.haier.hailian.contract.config.shiro.HacLoginToken;
import com.haier.hailian.contract.dto.HacLoginDto;
import com.haier.hailian.contract.dto.HacLoginRespDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.service.HacLoginService;
import com.haier.hailian.contract.util.Constant;
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
            log.info("=====Hac 登录返回结果====>", value);
            HacLoginRespDto loginRespDto=gson.fromJson(value,HacLoginRespDto.class);
            if("0".equals(loginRespDto.getCode())){
                //登陆成功
                HacLoginToken token = new HacLoginToken(loginDto.getUserName());
                Subject subject = SecurityUtils.getSubject();
                subject.login(token);
                SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();

                return R.ok().put(Constant.JWT_AUTH_HEADER, subject.getSession().getId())
                        .put("data", sysUser);
            }else{
                return R.error(Constant.CODE_LOGINFAIL, loginRespDto.getMessage());
            }
        }catch (Exception e){
            log.info("=====Hac 登录异常====>", e.getMessage());
            throw new RException("登录异常");
        }
    }
}
