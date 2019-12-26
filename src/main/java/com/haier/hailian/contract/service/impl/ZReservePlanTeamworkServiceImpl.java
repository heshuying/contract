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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public String saveAllInfo(ZReservePlanTeamworkDto zReservePlanTeamworkDto) throws ParseException {
        //查询对应的合约ID
        ZContracts zContracts = zContractsDao.selectByGID(zReservePlanTeamworkDto.getGroupId());
        if (zContracts ==null){
            return null;
        }
        zReservePlanTeamworkDto.setParentId(zContracts.getId());
        zReservePlanTeamworkDao.save(zReservePlanTeamworkDto);
        List<ZReservePlanTeamworkDetail> details = zReservePlanTeamworkDto.getDetails();
        for (ZReservePlanTeamworkDetail zReservePlanTeamworkDetail : details) {
            zReservePlanTeamworkDetail.setParentId(zReservePlanTeamworkDto.getId());
            zReservePlanTeamworkDao.insertDetail(zReservePlanTeamworkDetail);
        }
        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(zReservePlanTeamworkDto.getEndTime());
        // 调用ihaier的接口进行任务创建
        IhaierTask ihaierTask = new IhaierTask();
        String executors = IHaierUtil.getUserOpenId(zReservePlanTeamworkDto.getExecuter().split(","));
        ihaierTask.setExecutors(executors.split(","));
        String ccs = IHaierUtil.getUserOpenId(zReservePlanTeamworkDto.getTeamworker().split(","));
        ihaierTask.setCcs(ccs.split(","));
        String oid = IHaierUtil.getUserOpenId(zReservePlanTeamworkDto.getCreateUserCode().split(","));
        ihaierTask.setOpenId(oid);
        ihaierTask.setContent(zReservePlanTeamworkDto.getDetails().get(0).getContent());
        ihaierTask.setEndDate(date.getTime());
        ihaierTask.setImportant(Integer.parseInt(zReservePlanTeamworkDto.getIsImportant()));
        ihaierTask.setNoticeTime(15);
        ihaierTask.setTimingNoticeTime(Integer.parseInt(zReservePlanTeamworkDto.getRemindTime()));
        ihaierTask.setCallBackUrl("http://zzfx.hoptest.haier.net");
        String taskId = IHaierUtil.getTaskId(new Gson().toJson(ihaierTask));
        zReservePlanTeamworkDto.setTaskCode(taskId);
        //更新taskID
        zReservePlanTeamworkDao.updateByDto(zReservePlanTeamworkDto);


        return "保存成功";
    }

    @Override
    public void createGroup() {
        List<ZContracts> list = zContractsDao.selectAllContracts();
        List<String> userList = new ArrayList<>();
        for (ZContracts zContracts : list) {
            List<ZContracts> contracts= zContractsDao.selectUserList(zContracts.getId());
            for (ZContracts z: contracts){
                userList.add(z.getCreateCode());
            }
            String[] toBeStored = new String[userList.size()];
            userList.toArray(toBeStored);
            String groupId = IHaierUtil.getGroupId(toBeStored);
            ZContracts zContractTemp = new ZContracts();
            zContractTemp.setId(zContracts.getId());
            zContractTemp.setGroupId(groupId);
            zContractsDao.updateById(zContractTemp);
        }

    }
}