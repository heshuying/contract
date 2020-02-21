package com.haier.hailian.contract.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.ehr.odssystem.chaingroup.service.ChainGroupClient;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.ExportChainUnitInfo;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ValidateChainNameDTO;
import com.haier.hailian.contract.dto.ZHrChainInfoDto;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import com.haier.hailian.contract.util.IHaierUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (ZHrChainInfo)表服务实现类
 *
 * @author makejava
 * @since 2019-12-17 14:52:20
 */
@Service("zHrChainInfoService")
public class ZHrChainInfoServiceImpl implements ZHrChainInfoService {
    @Resource
    private ZHrChainInfoDao zHrChainInfoDao;
    @Resource
    private SysNodeEhrDao sysNodeEhrDao;
    @Resource
    private TOdsMinbuDao tOdsMinbuDao;
    @Resource
    private TargetBasicDao targetBasicDao;
    @Resource
    private ZNodeTargetPercentInfoDao zNodeTargetPercentInfoDao;
    //hr发版后放开
    @Reference(version = "ehr2.0", registry = "registry2", check = false)
    //@Reference(version = "ehr2.0-test",registry = "registry2",check=false)
    private ChainGroupClient chainGroupClient;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZNodeTargetPercentInfo queryByNodeId(Integer id) {
        return this.zNodeTargetPercentInfoDao.queryById(id);
    }

    @Override
    public ZHrChainInfo queryById(Integer id) {
        return this.zHrChainInfoDao.queryById(id);
    }

    @Override
    public ZHrChainInfoDto queryAllById(Integer id) {
        ZHrChainInfoDto zHrChainInfoDto = new ZHrChainInfoDto();
        ZHrChainInfo zHrChainInfo = this.zHrChainInfoDao.queryById(id);
        zHrChainInfoDto.setId(zHrChainInfo.getId());
        zHrChainInfoDto.setChainName(zHrChainInfo.getChainName());
        zHrChainInfoDto.setChainCode(zHrChainInfo.getChainCode());
        zHrChainInfoDto.setFixedPosition(zHrChainInfo.getFixedPosition());
        zHrChainInfoDto.setZzfxRate(zHrChainInfo.getZzfxRate());
        zHrChainInfoDto.setCdShareRate(zHrChainInfo.getCdShareRate());
        zHrChainInfoDto.setTyShareRate(zHrChainInfo.getTyShareRate());
        ZNodeTargetPercentInfo zNodeTargetPercentInfo = new ZNodeTargetPercentInfo();
        zNodeTargetPercentInfo.setLqCode(zHrChainInfo.getChainCode());
        zHrChainInfoDto.setZNodeTargetPercentInfos(zNodeTargetPercentInfoDao.queryAll(zNodeTargetPercentInfo));
        // 获取子链群信息
        List<ZHrChainInfoDto> dtos = new ArrayList<>();
        ZHrChainInfo exp = new ZHrChainInfo();
        exp.setParentCode(zHrChainInfo.getChainCode());
        exp.setDeleted(0); //查询未删除的
        List<ZHrChainInfo> listChild = zHrChainInfoDao.queryAll(exp);
        for(ZHrChainInfo fuck : listChild){
            ZHrChainInfoDto dto = new ZHrChainInfoDto();
            dto.setId(fuck.getId());
            dto.setChainName(fuck.getChainName());
            dto.setChainCode(fuck.getChainCode());
            dto.setFixedPosition(fuck.getFixedPosition());
            dto.setZzfxRate(fuck.getZzfxRate());
            dto.setCdShareRate(fuck.getCdShareRate());
            dto.setTyShareRate(fuck.getTyShareRate());
            ZNodeTargetPercentInfo nodeChild = new ZNodeTargetPercentInfo();
            nodeChild.setParentChainCode(zHrChainInfo.getChainCode());
            nodeChild.setLqCode(fuck.getChainCode());
            dto.setZNodeTargetPercentInfos(zNodeTargetPercentInfoDao.queryAll(nodeChild));

            dtos.add(dto);
        }

        zHrChainInfoDto.setZHrChainInfoDtos(dtos);//包括主链群 和 子链群
        return zHrChainInfoDto;
    }


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ZHrChainInfo> queryAllByLimit(int offset, int limit) {
        return this.zHrChainInfoDao.queryAllByLimit(offset, limit);
    }

    @Override
    public List<ZHrChainInfo> queryAll(ZHrChainInfo zHrChainInfo) {
        return this.zHrChainInfoDao.queryAll(zHrChainInfo);
    }

    /**
     * 新增数据
     *
     * @param zHrChainInfo 实例对象
     * @return 实例对象
     */
    @Override
    public ZHrChainInfo insert(ZHrChainInfo zHrChainInfo) {
        this.zHrChainInfoDao.insert(zHrChainInfo);
        return zHrChainInfo;
    }

    /**
     * 修改数据
     *
     * @param zHrChainInfo 实例对象
     * @return 实例对象
     */
    @Override
    public ZHrChainInfo update(ZHrChainInfo zHrChainInfo) {
        this.zHrChainInfoDao.update(zHrChainInfo);
        return this.queryById(zHrChainInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.zHrChainInfoDao.deleteById(id) > 0;
    }
    

    @Override
    public R validateChainName(ValidateChainNameDTO validateChainNameDTO) {
        //校验链群名称是否已经存在
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        //判断是否存在链群关键字，不存在则添加
        String name = validateChainNameDTO.getChainName();
        if (StringUtils.isEmpty(name)){
            return R.error("链群名称不能为空，请输入链群名称！");
        }
        if (!name.contains("链群")) {
            name = name + "链群";
        }
        zHrChainInfo.setChainName(name);
        List<ZHrChainInfo> chainNames = zHrChainInfoDao.queryAll(zHrChainInfo);
        if (chainNames.size() > 0) {
            return R.error("链群名称已经存在");
        }
        return R.ok("链群名称可用");
    }

    @Override
    public List<SysNodeEhr> searchUsersByKeyWords(String keyWords) {
        return sysNodeEhrDao.searchUsersByKeyWords(keyWords);
    }

    @Override
    public List<TargetBasic> getNodeTargetList(String nodeCodeStr) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();

        /*
         * 根据当前登陆人的最小单元进行匹配平台以及登陆人对应的最小单元的角色进行竞争力目标的查询
         */
        TOdsMinbu tOdsMinbu = sysUser.getMinbu();
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("001");
        // 获取链群所在的平台 liuyq 2020年2月6日 11:47:05
        ZHrChainInfo zHrChainInfo = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code" , nodeCodeStr));
        targetBasic.setTargetPtCode(zHrChainInfo.getChainPtCode());
        targetBasic.setRoleCode(tOdsMinbu.getXwCode());
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    @Transactional
    public ZHrChainInfoDto saveChainInfo(ZHrChainInfoDto zHrChainInfoDto) {
        //1.保存链群信息
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //链群编码生成
        String maxOne = zHrChainInfoDao.queryMaxOne();
        //生成编码的方法
//        String chainCode = frontCompWithZore(maxOne, 5, "H");
        //判断是否存在链群关键字，不存在则添加
        String name = zHrChainInfoDto.getChainName();
        if (!name.contains("链群")) {
            name = name + "链群";
        }
        //hr接口获取编码
        String chainCode = chainGroupClient.getChainGroupCode(name);

        // 主链群
        zHrChainInfo.setChainCode(chainCode);
        zHrChainInfo.setChainPtCode(currentUser.getPtCode());
        zHrChainInfo.setMasterCode(sysUser.getEmpSn());
        zHrChainInfo.setMasterName(sysUser.getEmpName());
        zHrChainInfo.setXwCode(currentUser.getLittleXwCode());
        zHrChainInfo.setXwName(currentUser.getLittleXwName());
        zHrChainInfo.setFixedPosition(zHrChainInfoDto.getFixedPosition());
        zHrChainInfo.setChainName(name);
        zHrChainInfo.setZzfxRate(zHrChainInfoDto.getZzfxRate());
        zHrChainInfo.setCdShareRate(zHrChainInfoDto.getCdShareRate());
        zHrChainInfo.setTyShareRate(zHrChainInfoDto.getTyShareRate());
        zHrChainInfo.setParentCode("0");
        zHrChainInfoDao.insert(zHrChainInfo);

        List<String> minbuList = new ArrayList<>();
        //2.保存链群的目标信息
        for (ZNodeTargetPercentInfo z:zHrChainInfoDto.getZNodeTargetPercentInfos()) {
            z.setLqCode(chainCode);
            z.setLqName(name);
            minbuList.add(z.getNodeCode());
            zNodeTargetPercentInfoDao.insert(z);
        }
        //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
        List<TOdsMinbu> getIsTY = tOdsMinbuDao.getListByIsTY(currentUser.getPtCode());
        for (TOdsMinbu tOdsMinbu:getIsTY){
            ZNodeTargetPercentInfo zNodeTargetPercentInfo =new ZNodeTargetPercentInfo();
            zNodeTargetPercentInfo.setLqCode(chainCode);
            zNodeTargetPercentInfo.setLqName(name);
            zNodeTargetPercentInfo.setNodeCode(tOdsMinbu.getLittleXwCode());
            zNodeTargetPercentInfo.setNodeName(tOdsMinbu.getLittleXwName());
            zNodeTargetPercentInfo.setXwCode(tOdsMinbu.getXwCode());
            zNodeTargetPercentInfo.setXwName(tOdsMinbu.getXwName());
            zNodeTargetPercentInfoDao.insert(zNodeTargetPercentInfo);
        }
        zHrChainInfoDto.setId(zHrChainInfo.getId());
        zHrChainInfoDto.setChainCode(chainCode);
        zHrChainInfoDto.setMasterCode(sysUser.getEmpSn());
        zHrChainInfoDto.setMasterName(sysUser.getEmpName());
        zHrChainInfoDto.setChainPtCode(currentUser.getPtCode());
        zHrChainInfoDto.setXwCode(currentUser.getXwCode());
        zHrChainInfoDto.setXwName(currentUser.getXwName());
        zHrChainInfoDto.setChainName(name);
        zHrChainInfoDto.setParentCode("0");
        //3.保存数据到链上（目前没有实现）
        //接口调用的时候会用到这个dto的实体类
        //4.新增创建群组，在创建链群的时候创建
        if(!zHrChainInfoDto.getIsModel().equals("1")){
            List<String> codeList = tOdsMinbuDao.getListByCodeList(currentUser.getPtCode(),minbuList);
            codeList.add(sysUser.getEmpSn());
            String[] toBeStored = new String[codeList.size()];
            codeList.toArray(toBeStored);
            String groupId = IHaierUtil.createGroup(toBeStored,name,chainCode);
            //更新链群的群组ID字段
            ZHrChainInfo zHrChainInfo1 = new ZHrChainInfo();
            zHrChainInfo1.setId(zHrChainInfo.getId());
            zHrChainInfo1.setGroupId(groupId);
            zHrChainInfoDao.update(zHrChainInfo1 );
        }


        List<ZHrChainInfoDto> dtos = new ArrayList<>(); // 存放模块链群数据
        // 模块链群
        if(zHrChainInfoDto.getZHrChainInfoDtos()!=null && zHrChainInfoDto.getIsModel().equals("1")){
            for(ZHrChainInfoDto chain : zHrChainInfoDto.getZHrChainInfoDtos()){
                String modelName = chain.getChainName();
                if (!modelName.contains("链群")) {
                    modelName = modelName + "链群";
                }

                // 获取计数器
                int num = zHrChainInfoDao.getNum();
                String modelCode = chainCode + "-" + num;

                ZHrChainInfo fuck = new ZHrChainInfo();
                fuck.setChainCode(modelCode);
                fuck.setChainPtCode(currentUser.getPtCode());
                fuck.setMasterCode(sysUser.getEmpSn());
                fuck.setMasterName(sysUser.getEmpName());
                fuck.setXwCode(currentUser.getLittleXwCode());
                fuck.setXwName(currentUser.getLittleXwName());
                fuck.setFixedPosition(chain.getFixedPosition());
                fuck.setChainName(modelName);
                fuck.setZzfxRate(chain.getZzfxRate());
                fuck.setCdShareRate(chain.getCdShareRate());
                fuck.setTyShareRate(chain.getTyShareRate());
                fuck.setParentCode(chainCode);
                zHrChainInfoDao.insert(fuck);

                List<String> modelMinbuList = new ArrayList<>();
                // 保存链群的目标信息
                for (ZNodeTargetPercentInfo z:chain.getZNodeTargetPercentInfos()) {
                    z.setLqCode(modelCode);
                    z.setLqName(modelName);
                    z.setParentChainCode(chainCode);
                    modelMinbuList.add(z.getNodeCode());
                    zNodeTargetPercentInfoDao.insert(z);
                }

                //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
                List<TOdsMinbu> getIsTYModel = tOdsMinbuDao.getListByIsTY(currentUser.getPtCode());
                for (TOdsMinbu tOdsMinbu:getIsTYModel){
                    ZNodeTargetPercentInfo zNodeTargetPercentInfo =new ZNodeTargetPercentInfo();
                    zNodeTargetPercentInfo.setLqCode(modelCode);
                    zNodeTargetPercentInfo.setLqName(modelName);
                    zNodeTargetPercentInfo.setNodeCode(tOdsMinbu.getLittleXwCode());
                    zNodeTargetPercentInfo.setNodeName(tOdsMinbu.getLittleXwName());
                    zNodeTargetPercentInfo.setXwCode(tOdsMinbu.getXwCode());
                    zNodeTargetPercentInfo.setXwName(tOdsMinbu.getXwName());
                    zNodeTargetPercentInfo.setParentChainCode(chainCode);
                    zNodeTargetPercentInfoDao.insert(zNodeTargetPercentInfo);
                }

                //4.新增创建群组，在创建链群的时候创建
                List<String> modelCodeList = tOdsMinbuDao.getListByCodeList(currentUser.getPtCode(),modelMinbuList);
                modelCodeList.add(sysUser.getEmpSn());
                String[] modelToBeStored = new String[modelCodeList.size()];
                modelCodeList.toArray(modelToBeStored);
                String modelGroupId = IHaierUtil.createGroup(modelToBeStored,modelName,chainCode);
                //更新链群的群组ID字段
                ZHrChainInfo zHrChainInfoExp = new ZHrChainInfo();
                zHrChainInfoExp.setId(fuck.getId());
                zHrChainInfoExp.setGroupId(modelGroupId);
                zHrChainInfoDao.update(zHrChainInfoExp );

                // 构建返回对象
                chain.setId(fuck.getId());
                chain.setChainCode(modelCode);
                chain.setMasterCode(sysUser.getEmpSn());
                chain.setMasterName(sysUser.getEmpName());
                chain.setChainPtCode(currentUser.getPtCode());
                chain.setXwCode(currentUser.getXwCode());
                chain.setXwName(currentUser.getXwName());
                chain.setChainName(modelName);
                chain.setParentCode(chainCode);

                dtos.add(chain);

                // 更新计数器表
                zHrChainInfoDao.updateNum();
            }
        }
        zHrChainInfoDto.setZHrChainInfoDtos(dtos);

        return zHrChainInfoDto;
    }

    @Override
    public List<TOdsMinbu> getMinbuList() {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //2获取数据库中这个平台的所有最小单元

        return tOdsMinbuDao.getListByPtCode(currentUser.getPtCode());
    }

    @Override
    public int updateChainInfo(ZNodeTargetPercentInfo zNodeTargetPercentInfo) {
        return zNodeTargetPercentInfoDao.update(zNodeTargetPercentInfo);
    }

    @Override
    public int deleteChainInfo(Integer id) {
        return zNodeTargetPercentInfoDao.deleteById(id);
    }

    @Override
    public String getDepVCode(String userCode) {
        return zHrChainInfoDao.getDepVCode(userCode);
    }

    @Override
    public int updateBatch(List<ZNodeTargetPercentInfo> zNodeTargetPercentInfo) {
        return zNodeTargetPercentInfoDao.updateBatch(zNodeTargetPercentInfo);
    }


    /**
     * 　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     * 　　* @param sourceDate  编码在当前数据库中的最大值
     * 　　* @param formatLength 编码的数字长度
     * 　　* @param key  编码开始字母
     * 　　* @return 重组后的数据
     */

    private static String frontCompWithZore(String sourceDate, int formatLength, String key) {
        if (sourceDate==null){
            sourceDate="H00000";
        }
        String[] strings = sourceDate.split(key);
        int sourceNum = Integer.parseInt(strings[1]);
        /*
         * 0 指前面补充零
         * formatLength 字符总长度为 formatLength
         * d 代表为正数。
         */
        String num = String.format("%0" + formatLength + "d", sourceNum + 1);
        num = key + num;
        return num;

    }

    @Override
    public List<ExportChainUnitInfo> getPartMinbuList() {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //2获取数据库中这个平台的所有最小单元

        return tOdsMinbuDao.getPartListByPtCode(currentUser.getPtCode());
    }

    @Override
    public List<ZHrChainInfo> searchChainListByUser(String userCode) {
        return zHrChainInfoDao.searchChainListByUser(userCode);
    }


    @Override
    public List<TOdsMinbu> getOtherMinbuList(String chainCode) {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //2获取数据库中这个平台的所有最小单元
        Map map = new HashMap<>();
        map.put("ptCode" , currentUser.getPtCode());
        map.put("chainCode" , chainCode.trim());
        List<TOdsMinbu> list = tOdsMinbuDao.getOtherListByPtCode(map);
        return list;
    }

    @Override
    public int saveNewMinbu(List<ZNodeTargetPercentInfo> zNodeTargetPercentInfos) {
        int num = zNodeTargetPercentInfoDao.insertBatch(zNodeTargetPercentInfos);
        return num;
    }

    @Override
    public int saveModel(ZHrChainInfoDto zHrChainInfoDto) {

        //1.保存链群信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return 0;
        }

        String modelName = zHrChainInfoDto.getChainName();
        if (!modelName.contains("链群")) {
            modelName = modelName + "链群";
        }

        // 获取计数器
        int num = zHrChainInfoDao.getNum();
        String modelCode = zHrChainInfoDto.getParentCode() + "-" + num;

        ZHrChainInfo fuck = new ZHrChainInfo();
        fuck.setChainCode(modelCode);
        fuck.setChainPtCode(currentUser.getPtCode());
        fuck.setMasterCode(sysUser.getEmpSn());
        fuck.setMasterName(sysUser.getEmpName());
        fuck.setXwCode(currentUser.getLittleXwCode());
        fuck.setXwName(currentUser.getLittleXwName());
        fuck.setFixedPosition(zHrChainInfoDto.getFixedPosition());
        fuck.setChainName(modelName);
        fuck.setZzfxRate(zHrChainInfoDto.getZzfxRate());
        fuck.setCdShareRate(zHrChainInfoDto.getCdShareRate());
        fuck.setTyShareRate(zHrChainInfoDto.getTyShareRate());
        fuck.setParentCode(zHrChainInfoDto.getParentCode());
        int change = zHrChainInfoDao.insert(fuck);

        List<String> modelMinbuList = new ArrayList<>();
        // 保存链群的目标信息
        for (ZNodeTargetPercentInfo z:zHrChainInfoDto.getZNodeTargetPercentInfos()) {
            z.setLqCode(modelCode);
            z.setLqName(zHrChainInfoDto.getChainName());
            z.setParentChainCode(zHrChainInfoDto.getParentCode());
            modelMinbuList.add(z.getNodeCode());
            zNodeTargetPercentInfoDao.insert(z);
        }

        //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
        List<TOdsMinbu> getIsTYModel = tOdsMinbuDao.getListByIsTY(currentUser.getPtCode());
        for (TOdsMinbu tOdsMinbu:getIsTYModel){
            ZNodeTargetPercentInfo zNodeTargetPercentInfo =new ZNodeTargetPercentInfo();
            zNodeTargetPercentInfo.setLqCode(modelCode);
            zNodeTargetPercentInfo.setLqName(zHrChainInfoDto.getChainName());
            zNodeTargetPercentInfo.setNodeCode(tOdsMinbu.getLittleXwCode());
            zNodeTargetPercentInfo.setNodeName(tOdsMinbu.getLittleXwName());
            zNodeTargetPercentInfo.setXwCode(tOdsMinbu.getXwCode());
            zNodeTargetPercentInfo.setXwName(tOdsMinbu.getXwName());
            zNodeTargetPercentInfo.setParentChainCode(zHrChainInfoDto.getParentCode());
            zNodeTargetPercentInfoDao.insert(zNodeTargetPercentInfo);
        }

        //4.新增创建群组，在创建链群的时候创建
        List<String> modelCodeList = tOdsMinbuDao.getListByCodeList(currentUser.getPtCode(),modelMinbuList);
        modelCodeList.add(sysUser.getEmpSn());
        String[] modelToBeStored = new String[modelCodeList.size()];
        modelCodeList.toArray(modelToBeStored);
        String modelGroupId = IHaierUtil.createGroup(modelToBeStored,zHrChainInfoDto.getChainName(),zHrChainInfoDto.getParentCode());
        //更新链群的群组ID字段
        ZHrChainInfo zHrChainInfoExp = new ZHrChainInfo();
        zHrChainInfoExp.setId(fuck.getId());
        zHrChainInfoExp.setGroupId(modelGroupId);
        zHrChainInfoDao.update(zHrChainInfoExp );

        // 更新计数器表
        zHrChainInfoDao.updateNum();
        return change;
    }

    @Override
    public int updateModelInfo(ZHrChainInfo zHrChainInfo) {
        int num = zHrChainInfoDao.update(zHrChainInfo);
        return num;
    }



    @Override
    public List<TOdsMinbu> getChildChainOtherMinbuList(ChainRepairInfo chainRepairInfo) {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //2获取数据库中这个平台的所有最小单元
        Map map = new HashMap<>();
        map.put("ptCode" , currentUser.getPtCode());
        map.put("childchainCode" , chainRepairInfo.getChildChainCode());
        map.put("parentChainCode" , chainRepairInfo.getParentChainCode());
        List<TOdsMinbu> list = tOdsMinbuDao.getChildOtherListByPtCode(map);
        return list;
    }

    @Override
    public int updateAllGroupId() {
        int num = 0;
        List<ZHrChainInfo> list = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().isNull("group_id"));
        for(ZHrChainInfo chainInfo : list){
            String[] users = {chainInfo.getMasterCode(),"19037699"};
            String groupId = IHaierUtil.createGroup(users,chainInfo.getChainName(),chainInfo.getChainCode());
            ZHrChainInfo exp = new ZHrChainInfo();
            exp.setId(chainInfo.getId());
            exp.setGroupId(groupId);
            zHrChainInfoDao.update(exp);
            num++;
        }
        return num;
    }


}
