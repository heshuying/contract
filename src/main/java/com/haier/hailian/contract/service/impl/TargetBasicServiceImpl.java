package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.dao.SysXiaoweiEhrDao;
import com.haier.hailian.contract.dao.TOdsDictionaryDao;
import com.haier.hailian.contract.dao.TargetBasicDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.TargetBasicInfo;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.TargetBasicService;
import com.haier.hailian.contract.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
@Service
public class TargetBasicServiceImpl extends ServiceImpl<TargetBasicDao, TargetBasic> implements TargetBasicService {

    @Autowired
    private TargetBasicDao targetBasicDao;
    @Autowired
    private ZHrChainInfoDao zHrChainInfoDao;
    @Autowired
    private SysXiaoweiEhrDao sysXiaoweiEhrDao;
    @Autowired
    private TOdsDictionaryDao tOdsDictionaryDao;

    @Override
    public List<TargetBasic> selectBottom(QueryBottomDTO dto){
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setChainCode(dto.getChainCode());
        targetBasic.setTargetDiffType("000");
        targetBasic.setPeriodCode(dto.getMonth());
        List<TargetBasic> list = targetBasicDao.selectTarget(targetBasic);
        for(TargetBasic targetBasic1:list){
            if(null != targetBasic1.getTargetBottomLine() && !"".equals(targetBasic1.getTargetBottomLine())){
                String bottom = targetBasic1.getTargetBottomLine();
                int position = bottom.length() - bottom.indexOf(".") - 1;
                if(position==1) bottom = bottom.replaceAll("\\.0","");
                targetBasic1.setTargetBottomLine(bottom);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
            if(null != targetBasic1.getTargetJdLine() && !"".equals(targetBasic1.getTargetJdLine())){
                String e2e = targetBasic1.getTargetJdLine();
                int position = e2e.length() - e2e.indexOf(".") - 1;
                if(position==1) e2e = e2e.replaceAll("\\.0","");
                targetBasic1.setTargetJdLine(e2e);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
        }
        return list;
    }

    @Override
    public List<TargetBasic> selectContractsFirstTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("003");
        targetBasic.setChainCode(dto.getChainCode());
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    public List<TargetBasic> selectContractsSecondTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("004");
        targetBasic.setChainCode(dto.getChainCode());
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    public int updateContractsTarget(List<TargetBasic> targetBasicList) {
        int num = 0;
        for(TargetBasic targetBasic : targetBasicList){
            targetBasicDao.updateById(targetBasic);
            num++;
        }
        return num;
    }

    @Override
    public int insertContractsTarget(List<TargetBasic> targetBasicList) {
        int num = 0;
        for(TargetBasic targetBasic : targetBasicList){
            if(StringUtils.isNotBlank(targetBasic.getRoleCode())){ // 2级单
                String[] codes = targetBasic.getTargetXwCategoryCode().split("|");
                String roleCode = "|";
                for(String code : codes){
                    Map minbuMap = new HashMap();
                    minbuMap.put("xwStyleCode" , "|" + code + "|");
                    List<SysXiaoweiEhr> mins = sysXiaoweiEhrDao.getListByXwStyleCode(minbuMap);
                    for(SysXiaoweiEhr xw : mins){
                        roleCode = roleCode + xw.getXwcode() + "|";
                    }
                }
                targetBasic.setRoleCode(roleCode);
            }
            targetBasic.setChainCode("LQ" + getNum());
            targetBasicDao.insert(targetBasic);
            num++;
        }
        return num;
    }


    @Override
    public int saveContractsTarget(List<TargetBasicInfo> targetBasicInfos) {
        int num = 0;
        for(TargetBasicInfo targetBasicInfo : targetBasicInfos){

            // 一级单数据
            TargetBasic targetBasicFirst = new TargetBasic();
            BeanUtils.copyProperties(targetBasicInfo , targetBasicFirst);

            if(targetBasicInfo.getId() == null){ // 一级单id为空插入
                targetBasicFirst.setChainCode("LQ" + getNum());
                targetBasicDao.insert(targetBasicFirst);
                // 一级单id为空，则二级单全部插入
                for(TargetBasic targetBasicSecond : targetBasicInfo.getChildTargetBasicList()){
                    targetBasicSecond = getXwInfo(targetBasicSecond);
                    targetBasicDao.insert(targetBasicSecond);
                }
            } else { //一级单id 不为空 更新
                targetBasicDao.updateById(targetBasicFirst);
                for(TargetBasic targetBasicSecond : targetBasicInfo.getChildTargetBasicList()){
                    targetBasicSecond = getXwInfo(targetBasicSecond);
                    if(targetBasicSecond.getId() == null){ //二级单id为空则插入
                        targetBasicDao.insert(targetBasicSecond);
                    } else {
                        targetBasicDao.updateById(targetBasicSecond);
                    }

                }
            }
        }
        return num;
    }


    public TargetBasic  getXwInfo(TargetBasic targetBasicSecond){
        String[] codes = targetBasicSecond.getTargetXwCategoryCode().split("|");
        String roleCode = "|";
        String roleName = "|";
        for(String code : codes){
            Map minbuMap = new HashMap();
            minbuMap.put("xwStyleCode" , "|" + code + "|");
            List<SysXiaoweiEhr> mins = sysXiaoweiEhrDao.getListByXwStyleCode(minbuMap);
            for(SysXiaoweiEhr xw : mins){
                roleCode = roleCode + xw.getXwcode() + "|";
                roleName = roleName + xw.getXwname() + "|";
            }
        }
        targetBasicSecond.setRoleCode(roleCode);
        targetBasicSecond.setRoleName(roleName);

        return targetBasicSecond;
    }

    @Override
    public List<TargetBasicInfo> selectContractsTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("003");
        targetBasic.setChainCode(dto.getChainCode());
        List<TargetBasic> targetList = targetBasicDao.selectTarget(targetBasic);
        List<TargetBasicInfo> infos = new ArrayList<>();
        for(TargetBasic basic : targetList){
            targetBasic.setTargetDiffType("004");
            targetBasic.setJudanFlag(String.valueOf(basic.getId()));
            List<TargetBasic> childTargetList = targetBasicDao.selectTarget(targetBasic);

            TargetBasicInfo info = new TargetBasicInfo();
            BeanUtils.copyProperties(basic , info);
            info.setChildTargetBasicList(childTargetList);
            infos.add(info);

        }
        return infos;
    }

    @Override
    public int deleteContractsTarget(Integer id) {
        TargetBasic targetBasic = targetBasicDao.selectById(id);
        if("0".equals(targetBasic.getJudanFlag())){ // 是一级单
            targetBasicDao.delete(new QueryWrapper<TargetBasic>()
                    .eq("judan_flag" , String.valueOf(id)));

        }
        int num = targetBasicDao.deleteById(id);
        return num;
    }

    @Override
    public List<SysXiaoweiEhr> selectXwAll(XiaoweiEhr xiaoweiEhr) {
        List<SysXiaoweiEhr> list = sysXiaoweiEhrDao.queryAll(xiaoweiEhr);
        return list;
    }

    @Override
    public int getNum() {
        // 获取计数器
        int num = zHrChainInfoDao.getNum();
        // 更新计数器表
        zHrChainInfoDao.updateNum();
        return num;
    }



    @Override
    public List<TOdsDictionary> getXwTypeList() {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        List<TOdsDictionary> list = tOdsDictionaryDao.selectList(new QueryWrapper<TOdsDictionary>()
                .eq("status" , "1")
                .eq("Type " , "XWstyle"));

        List<TOdsDictionary> realList = new ArrayList<>();
        for(TOdsDictionary ods : list){
            Map minbuMap = new HashMap();
            minbuMap.put("xwStyleCode" , "|" + ods.getCode() + "|");
            List<SysXiaoweiEhr> mins = sysXiaoweiEhrDao.getListByXwStyleCode(minbuMap);
            if(mins.size()>0){
                realList.add(ods);
            }else{
                continue;
            }
        }
        return realList;
    }


}
