package com.haier.hailian.contract.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dao.ZReservePlanTeamworkDao;
import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import com.haier.hailian.contract.util.IHaierUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Resource
    private ZHrChainInfoDao zHrChainInfoDao;

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
    @Transactional
    public String saveAllInfo(ZReservePlanTeamworkDto zReservePlanTeamworkDto) throws ParseException {
        //查询对应的合约ID
//        ZContracts zContracts = zContractsDao.selectByGID(zReservePlanTeamworkDto.getGroupId(),zReservePlanTeamworkDto.getCreateUserCode());
//        if (zContracts ==null){
//            return null;
//        }
//        zReservePlanTeamworkDto.setParentId(zContracts.getId());
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        zHrChainInfo.setGroupId(zReservePlanTeamworkDto.getGroupId());
        List<ZHrChainInfo> zHrChainInfos = zHrChainInfoDao.queryAll(zHrChainInfo);
        if (zHrChainInfos.size()==0){
            return "保存失败，没有找到相应的链群！";
        }
        zReservePlanTeamworkDao.save(zReservePlanTeamworkDto);
        List<ZReservePlanTeamworkDetail> details = zReservePlanTeamworkDto.getDetails();
        for (ZReservePlanTeamworkDetail zReservePlanTeamworkDetail : details) {
            zReservePlanTeamworkDetail.setParentId(zReservePlanTeamworkDto.getId());
            zReservePlanTeamworkDao.insertDetail(zReservePlanTeamworkDetail);
        }
        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(zReservePlanTeamworkDto.getEndTime()+" 23:59:59");

        // 调用ihaier的接口进行任务创建
        IhaierTask ihaierTask = new IhaierTask();
        if (!StringUtils.isBlank(zReservePlanTeamworkDto.getExecuter())){
            ihaierTask.setExecutors(zReservePlanTeamworkDto.getExecuter().split(","));
        }
        if(!StringUtils.isBlank(zReservePlanTeamworkDto.getTeamworker())){
            ihaierTask.setCcs(zReservePlanTeamworkDto.getTeamworker().split(","));
        }
        ihaierTask.setCreateJobNo(zReservePlanTeamworkDto.getCreateUserCode());
        ihaierTask.setContent(zReservePlanTeamworkDto.getDetails().get(0).getContent());
        ihaierTask.setEndDate(date.getTime());
        ihaierTask.setImportant(Integer.parseInt(zReservePlanTeamworkDto.getIsImportant()));
        ihaierTask.setNoticeTime(15);
        ihaierTask.setChannel("690");
        ihaierTask.setCreateChannel(zReservePlanTeamworkDto.getGroupId());
        ihaierTask.setTimingNoticeTime(Integer.parseInt(zReservePlanTeamworkDto.getRemindTime()));
        ihaierTask.setCallBackUrl("http://jhzx.haier.net/api/v1/cloudworktask/callBack");
        String extData = "{" +
                "        \"searchKey\": \"并联协同预案\"," +
                "        \"jsonData\": {" +
                "            \"systemSource\": \""+zReservePlanTeamworkDto.getProblemChannel()+"\"," +
                "            \"problemSource\": \""+zReservePlanTeamworkDto.getProblemType()+"\"," +
                "            \"linkId\": \""+zHrChainInfos.get(0).getChainCode()+"\"," +
                "            \"problemId\": \""+zReservePlanTeamworkDto.getProblemCode()+"\"," +
                "            \"problem\": \""+zReservePlanTeamworkDto.getProblemContent()+"\"" +
                "        }" +
                "    }";
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject json = (JsonObject) parse.parse(extData);
        ihaierTask.setExtData(json);
        String taskId = IHaierUtil.createTask(new Gson().toJson(ihaierTask));
        if (taskId == null){
            return "保存失败,并联协同群组不存在！";
        }
        zReservePlanTeamworkDto.setTaskCode(taskId);
        //更新taskID
        zReservePlanTeamworkDao.updateByDto(zReservePlanTeamworkDto);
        return "保存成功";
    }

    @Override
    public void createGroup() {
        List<ZContracts> list = zContractsDao.selectAllContracts("1");
        List<String> userList = new ArrayList<>();
        for (ZContracts zContracts : list) {
            List<ZContracts> contracts= zContractsDao.selectUserList(zContracts.getId());
            for (ZContracts z: contracts){
                userList.add(z.getCreateCode());
            }
            String[] toBeStored = new String[userList.size()];
            userList.toArray(toBeStored);
            String user = IHaierUtil.getUserOpenId(toBeStored);
            String groupId = IHaierUtil.getGroupId(user.split(","),zContracts.getContractName());
            ZContracts zContractTemp = new ZContracts();
            zContractTemp.setId(zContracts.getId());
            zContractTemp.setGroupId(groupId);
            zContractsDao.updateById(zContractTemp);
            //循环父亲ID的数据
            List<ZContracts> pa = zContractsDao.selectAllContractsById(zContracts.getId());
            for (ZContracts z:pa){
                ZContracts zContractTemp2 = new ZContracts();
                zContractTemp2.setId(z.getId());
                zContractTemp2.setGroupId(groupId);
                zContractsDao.updateById(zContractTemp2);
            }
        }

    }

    @Override
    public void createContracts() {
        //查询所有的进行中的合约
        List<ZContracts> list = zContractsDao.selectAllContracts("0");
        for (ZContracts zContracts : list) {
            //判断是否达成和约
            long nowDate = (new Date()).getTime();
            if (zContracts.getJoinTime().getTime() <= nowDate) {
                //比对是否存在群组中
                List<String> userList = IHaierUtil.groupUsers(zContracts.getGroupId());
                if (userList == null){
                    System.out.println("出错了");
                    return;
                }
                //暂存合约的用户列表
                List<String> zCUserList = new ArrayList<>();
                List<String> tempList = new ArrayList<>();
                //循环父亲ID的数据
                List<ZContracts> pa = zContractsDao.selectAllContractsById(zContracts.getId());
                for (ZContracts z : pa) {
                    zCUserList.add(z.getCreateCode());
                }
                String[] toBeStored = new String[zCUserList.size()];
                userList.toArray(toBeStored);
                String user = IHaierUtil.getUserOpenId(toBeStored);
                String[] userOpenId = user.split(",");
                for (String st : userList) {
                    boolean is = Arrays.asList(userOpenId).contains(st);
                    if (!is) {
                        tempList.add(st);
                    }
                }
                System.out.println("群组中不包含以下的成员：" + tempList);
                ZContracts zContractTemp2 = new ZContracts();
                zContractTemp2.setId(zContracts.getId());
                zContractTemp2.setStatus("1");
                zContractsDao.updateById(zContractTemp2);
            }
        }
    }
}
