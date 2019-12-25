package com.haier.hailian.contract.service.impl;

import com.alibaba.dubbo.common.json.JSONObject;
import com.google.gson.Gson;
import com.haier.hailian.contract.config.shiro.HacLoginToken;
import com.haier.hailian.contract.dto.HacLoginRespDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.dao.ZReservePlanTeamworkDao;
import com.haier.hailian.contract.entity.ZReservePlanTeamworkDetail;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import com.haier.hailian.contract.util.Constant;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * (ZReservePlanTeamwork)表服务实现类
 *
 * @author makejava
 * @since 2019-12-24 17:32:41
 */
@Service("zReservePlanTeamworkService")
public class ZReservePlanTeamworkServiceImpl implements ZReservePlanTeamworkService {
    @Resource
    private ZReservePlanTeamworkDao zReservePlanTeamworkDao;
    private Gson gson=new Gson();
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZReservePlanTeamwork queryById(Integer id) {
        return this.zReservePlanTeamworkDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ZReservePlanTeamwork> queryAllByLimit(int offset, int limit) {
        return this.zReservePlanTeamworkDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 实例对象
     */
    @Override
    public ZReservePlanTeamwork insert(ZReservePlanTeamwork zReservePlanTeamwork) {
        this.zReservePlanTeamworkDao.insert(zReservePlanTeamwork);
        return zReservePlanTeamwork;
    }

    /**
     * 修改数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 实例对象
     */
    @Override
    public ZReservePlanTeamwork update(ZReservePlanTeamwork zReservePlanTeamwork) {
        this.zReservePlanTeamworkDao.update(zReservePlanTeamwork);
        return this.queryById(zReservePlanTeamwork.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.zReservePlanTeamworkDao.deleteById(id) > 0;
    }

    @Override
    public int queryCountByParentId(Integer parentId) {
        return this.zReservePlanTeamworkDao.selectCountByParentId(parentId);
    }

    @Override
    public List<ZReservePlanTeamworkDto> queryAllByKey(ZReservePlanTeamwork zReservePlanTeamwork) {
        return this.zReservePlanTeamworkDao.queryAll(zReservePlanTeamwork);
    }

    @Override
    public String saveAllInfo(List<ZReservePlanTeamworkDto> zReservePlanTeamworkDtoList) {
        for (ZReservePlanTeamworkDto zReservePlanTeamworkDto : zReservePlanTeamworkDtoList) {
            zReservePlanTeamworkDao.save(zReservePlanTeamworkDto);
            List<ZReservePlanTeamworkDetail> details = zReservePlanTeamworkDto.getDetails();
            for (ZReservePlanTeamworkDetail zReservePlanTeamworkDetail : details) {
                zReservePlanTeamworkDetail.setParentId(zReservePlanTeamworkDto.getId());
                zReservePlanTeamworkDao.insertDetail(zReservePlanTeamworkDetail);
            }
        }
        // 调用ihaier的接口进行任务创建
        /**
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String accessToken="";
//        headers.set("Authorization", "Basic "+ Base64Utils.encodeToString(basic.getBytes()));
        String str = "{" +
                "\"groupName\": \"讨论组\"," +
                "\"currentUid\":\"5b503ee9e4b02abb5318b23e\"," +
                "\"userIds\":[" +
                "\"5b6cea82e4b05df05b3efc2b\"," +
                "\"5bee6706e4b073e587c144ae\"," +
                "\"5b50428ce4b091389c3b3777\"" +
                "]" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(str), headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://i.haier.net/gateway/xtinterface/group/createGroup?accessToken="+accessToken,
                    entity, String.class);

            String value = responseEntity.getBody();
//
        }catch (Exception e){
            throw new RException("异常");
        }
         **/
        return "保存成功";
    }
}