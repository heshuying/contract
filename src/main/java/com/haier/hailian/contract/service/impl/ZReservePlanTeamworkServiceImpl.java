package com.haier.hailian.contract.service.impl;

import com.google.gson.Gson;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZReservePlanTeamworkDao;
import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.IhaierTask;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.entity.ZReservePlanTeamworkDetail;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import com.haier.hailian.contract.util.IHaierUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private ZContractsDao zContractsDao;

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
    public String saveAllInfo(ZReservePlanTeamworkDto zReservePlanTeamworkDto) {
        //查询对应的合约ID
        ZContracts zContracts = zContractsDao.selectByGID(zReservePlanTeamworkDto.getGroupId());
        zReservePlanTeamworkDto.setParentId(zContracts.getId());
        zReservePlanTeamworkDao.save(zReservePlanTeamworkDto);
        List<ZReservePlanTeamworkDetail> details = zReservePlanTeamworkDto.getDetails();
        for (ZReservePlanTeamworkDetail zReservePlanTeamworkDetail : details) {
            zReservePlanTeamworkDetail.setParentId(zReservePlanTeamworkDto.getId());
            zReservePlanTeamworkDao.insertDetail(zReservePlanTeamworkDetail);
        }
        // 调用ihaier的接口进行任务创建
        IhaierTask ihaierTask = new IhaierTask();
        String executors = IHaierUtil.getUserOpenId(zReservePlanTeamworkDto.getExecuter().split(","));
        ihaierTask.setExecutors(executors.split(","));
        String ccs = IHaierUtil.getUserOpenId(zReservePlanTeamworkDto.getTeamworker().split(","));
        ihaierTask.setCcs(ccs.split(","));
        ihaierTask.setOpenId(zReservePlanTeamworkDto.getCreateUserCode());
        ihaierTask.setContent(zReservePlanTeamworkDto.getDetails().get(0).getContent());
        ihaierTask.setEndDate(Long.parseLong(zReservePlanTeamworkDto.getEndTime()));
        ihaierTask.setImportant(Integer.parseInt(zReservePlanTeamworkDto.getIsImportant()));
        ihaierTask.setNoticeTime(12);
        ihaierTask.setTimingNoticeTime(1);
        ihaierTask.setCallBackUrl("");
        String taskId = IHaierUtil.getTaskId(new Gson().toJson(ihaierTask));
        zReservePlanTeamworkDto.setTaskCode(taskId);
        //更新taskID
        zReservePlanTeamworkDao.updateByDto(zReservePlanTeamworkDto);


        return "保存成功";
    }

    @Override
    public String createGroup(int id) {
        List<ZContracts> list = zContractsDao.selectUserList(id);
        List<String> userList = new ArrayList<>();
        for (ZContracts zContracts : list) {
            userList.add(zContracts.getCreateCode());
        }
        String[] toBeStored = new String[userList.size()];
        userList.toArray(toBeStored);
        String groupId = IHaierUtil.getGroupId(toBeStored);
        ZContracts zContracts = new ZContracts();
        zContracts.setId(id);
        zContracts.setGroupId(groupId);
        zContractsDao.updateById(zContracts);
        return null;
    }
}