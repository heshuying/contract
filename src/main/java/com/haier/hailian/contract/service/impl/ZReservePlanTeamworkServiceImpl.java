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
import java.util.*;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        获取本月第一天：
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 0);
        Date tempDate = calendar.getTime();
        String startTime = dateFormat.format(tempDate);
//        获取本月最后一天：
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        tempDate = calendar.getTime();
        String endTime = dateFormat.format(tempDate) + " 23:59:59";

        ZContracts zContracts = zContractsDao.selectByTime(startTime,endTime,zReservePlanTeamworkDto.getGroupId());
        if (zContracts == null){
            //上个月第一天
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            tempDate = calendar.getTime();
            startTime = dateFormat.format(tempDate);
            //上个月最后一天
            int month=calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            tempDate = calendar.getTime();
            endTime = dateFormat.format(tempDate) + " 23:59:59";
            zContracts = zContractsDao.selectByTime(startTime,endTime,zReservePlanTeamworkDto.getGroupId());
            if (zContracts == null ){
                return "当前月和上个月都没有抢单！";
            }
        }
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        zHrChainInfo.setGroupId(zReservePlanTeamworkDto.getGroupId());
        List<ZHrChainInfo> zHrChainInfos = zHrChainInfoDao.queryAll(zHrChainInfo);
        if (zHrChainInfos.size() == 0) {
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
        Date date = sdf.parse(zReservePlanTeamworkDto.getEndTime() + " 23:59:59");

        // 调用ihaier的接口进行任务创建
        IhaierTask ihaierTask = new IhaierTask();
        if (!StringUtils.isBlank(zReservePlanTeamworkDto.getExecuter())) {
            ihaierTask.setExecutors(zReservePlanTeamworkDto.getExecuter().split(","));
        }
        if (!StringUtils.isBlank(zReservePlanTeamworkDto.getTeamworker())) {
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
        String[] systemSource = zReservePlanTeamworkDto.getProblemChannel().split(",,,");
        String[] problemSource = zReservePlanTeamworkDto.getProblemType().split(",,,");
        String[] problemId = zReservePlanTeamworkDto.getProblemCode().split(",,,");
        String[] problem = zReservePlanTeamworkDto.getProblemContent().split(",,,");
        String[] problemChannelId = zReservePlanTeamworkDto.getProblemChannelId().split(",,,");
        String[] problemTypeId = zReservePlanTeamworkDto.getProblemTypeId().split(",,,");
        JsonObject[] jsonData = new JsonObject[problemId.length];
        for (int i = 0; i < problemId.length; i++) {
            JsonParser parse = new JsonParser();  //创建json解析器
            jsonData[i] = (JsonObject) parse.parse("{" +
                    "\"systemSource\": \"" + systemSource[i] + "\"," +
                    "\"problemSource\": \"" + problemSource[i] + "\"," +
                    "\"linkId\": \"" + zHrChainInfos.get(0).getChainCode() + "\"," +
                    "\"problemId\": \"" + problemId[i] + "\"," +
                    "\"contractsId\": \"" + zContracts.getId() + "\"," +
                    "\"proSrcCode\": \"" + problemChannelId[i] + "\"," +
                    "\"proTypeCode\": \"" + problemTypeId[i] + "\"," +
                    "\"problem\": \"" + problem[i] + "\"" +
                    "}");
        }
        String jsonDateStr = new Gson().toJson(jsonData);
        String extData = "{" +
                "        \"searchKey\": \"" + zHrChainInfos.get(0).getChainCode() + "\"," +
                "        \"jsonData\": {\"dataList\": " + jsonDateStr +
                " }   }";
        JsonParser parse = new JsonParser();  //创建json解析器
        JsonObject json = (JsonObject) parse.parse(extData);
        ihaierTask.setExtData(json);
        String jsonObject = new Gson().toJson(ihaierTask);
        String taskId = IHaierUtil.createTask(jsonObject);
        if (taskId == null || "系统错误".equals(taskId)) {
            return "保存失败,调用任务中心出错了！";
        }else if (taskId.contains("接口错误")){
            return taskId;
        }
        zReservePlanTeamworkDto.setTaskCode(taskId);
//        更新taskID
        zReservePlanTeamworkDao.updateByDto(zReservePlanTeamworkDto);
        return "保存成功";
    }

    @Override
    public void createGroup() {
        List<ZContracts> list = zContractsDao.selectAllContracts("1");
        for (ZContracts zContracts : list) {
           ZHrChainInfo zHrChainInfo = zHrChainInfoDao.queryByCode(zContracts.getChainCode());
           if (zHrChainInfo != null){
               ZContracts zContractTemp = new ZContracts();
               zContractTemp.setId(zContracts.getId());
               zContractTemp.setGroupId(zHrChainInfo.getGroupId());
               if (zHrChainInfo.getGroupId()!=null){
                   zContractsDao.updateById(zContractTemp);
               }
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
