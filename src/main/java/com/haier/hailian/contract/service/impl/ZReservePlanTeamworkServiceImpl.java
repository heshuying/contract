package com.haier.hailian.contract.service.impl;

import com.google.gson.Gson;
import com.haier.hailian.contract.dao.ZReservePlanTeamworkDao;
import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.entity.ZReservePlanTeamworkDetail;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
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
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Date date = new Date();
        long timestamp = date.getTime();
        RequestBody body = RequestBody.create(mediaType, "eid=102&secret=TUW0n1TAW8FYkALRHBS7OfYFQP9GezvB&timestamp="+timestamp+"&scope=resGroupSecret");
        Request request = new Request.Builder()
                .url("https://i.haier.net/gateway/oauth2/token/getAccessToken")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "PostmanRuntime/7.15.2")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "5af5b09c-ec7f-4723-b3c0-f54f0b600477,52c65978-76ce-4e8a-951c-d8c0601d42b7")
                .addHeader("Host", "i.haier.net")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "92")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "保存成功";
    }
}