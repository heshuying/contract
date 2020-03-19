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
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import com.haier.hailian.contract.util.IHaierUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
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
    @Resource
    private TOdsDictionaryDao tOdsDictionaryDao;
    @Resource
    private ChainCommonService chainCommonService;
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
        zHrChainInfoDto.setChainPtCode(zHrChainInfo.getChainPtCode());
        zHrChainInfoDto.setFixedPosition(zHrChainInfo.getFixedPosition());
        zHrChainInfoDto.setZzfxRate(zHrChainInfo.getZzfxRate());
        zHrChainInfoDto.setCdShareRate(zHrChainInfo.getCdShareRate());
        zHrChainInfoDto.setTyShareRate(zHrChainInfo.getTyShareRate());
        //ZNodeTargetPercentInfo zNodeTargetPercentInfo = new ZNodeTargetPercentInfo();
        //zNodeTargetPercentInfo.setLqCode(zHrChainInfo.getChainCode());
        Map parentMap = new HashMap();
        parentMap.put("lqCode" , zHrChainInfo.getChainCode());
        List<ZNodeTargetPercentInfo> parentNodes = zNodeTargetPercentInfoDao.selectListByXwType3Code(parentMap);
        zHrChainInfoDto.setZNodeTargetPercentInfos(parentNodes);
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
//            ZNodeTargetPercentInfo nodeChild = new ZNodeTargetPercentInfo();
//            nodeChild.setParentChainCode(zHrChainInfo.getChainCode());
//            nodeChild.setLqCode(fuck.getChainCode());
            Map childMap = new HashMap();
            childMap.put("lqCode" , fuck.getChainCode());
            childMap.put("parentChainCode" , zHrChainInfo.getChainCode());
            List<ZNodeTargetPercentInfo> childNodes = zNodeTargetPercentInfoDao.selectChildListByXwType3Code(childMap);
            dto.setZNodeTargetPercentInfos(childNodes);

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
        zHrChainInfo.setChainName(name);// 子链群检验
        List<ZHrChainInfo> childChainNames = zHrChainInfoDao.queryAll(zHrChainInfo);
        if (childChainNames.size() > 0) {
            return R.error("链群名称已经存在");
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
        zHrChainInfo.setChainPtCode(zHrChainInfoDto.getChainPtCode());
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
        //2.保存链群的目标信息 兼容XwType3版本
        if(!zHrChainInfoDto.getIsModel().equals("1")){
            SaveXwType3 saveXwType3Parent = zHrChainInfoDto.getSaveXwType3();
            saveXwType3Parent.setLqCode(chainCode);
            saveXwType3Parent.setLqName(name);
            saveXwType3Parent.setPtCode(zHrChainInfoDto.getChainPtCode());
            minbuList = saveXwType3(saveXwType3Parent);
        }
//        for (ZNodeTargetPercentInfo z:zHrChainInfoDto.getZNodeTargetPercentInfos()) {
//            z.setLqCode(chainCode);
//            z.setLqName(name);
//            minbuList.add(z.getNodeCode());
//            zNodeTargetPercentInfoDao.insert(z);
//        }
        //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
        List<TOdsMinbu> getIsTY = tOdsMinbuDao.getListByIsTY(zHrChainInfoDto.getChainPtCode());
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
        zHrChainInfoDto.setChainPtCode(zHrChainInfoDto.getChainPtCode());
        zHrChainInfoDto.setXwCode(currentUser.getXwCode());
        zHrChainInfoDto.setXwName(currentUser.getXwName());
        zHrChainInfoDto.setChainName(name);
        zHrChainInfoDto.setParentCode("0");
        //3.保存数据到链上（目前没有实现）
        //接口调用的时候会用到这个dto的实体类
        //4.新增创建群组，在创建链群的时候创建
        if(!zHrChainInfoDto.getIsModel().equals("1")){
            List<String> codeList = new ArrayList<>(); //只有插入了节点才创建群组
            if(minbuList.size()>0){
                codeList = tOdsMinbuDao.getListByCodeList(zHrChainInfoDto.getChainPtCode(),minbuList);
            }
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
//                if (!modelName.contains("链群")) {
//                    modelName = modelName + "链群";
//                }

                // 获取计数器
                int num = zHrChainInfoDao.getNum();
                String modelCode = chainCode + "-" + num;

                ZHrChainInfo fuck = new ZHrChainInfo();
                fuck.setChainCode(modelCode);
                fuck.setChainPtCode(zHrChainInfoDto.getChainPtCode());
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
                // 保存链群的目标信息  兼容XwType3版本
                SaveXwType3 saveXwType3Child = chain.getSaveXwType3();
                saveXwType3Child.setLqCode(modelCode);
                saveXwType3Child.setLqName(modelName);
                saveXwType3Child.setParentChainCode(chainCode);
                saveXwType3Child.setPtCode(zHrChainInfoDto.getChainPtCode());
                modelMinbuList = saveXwType3(saveXwType3Child);
//                for (ZNodeTargetPercentInfo z:chain.getZNodeTargetPercentInfos()) {
//                    z.setLqCode(modelCode);
//                    z.setLqName(modelName);
//                    z.setParentChainCode(chainCode);
//                    modelMinbuList.add(z.getNodeCode());
//                    zNodeTargetPercentInfoDao.insert(z);
//                }

                //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
                List<TOdsMinbu> getIsTYModel = tOdsMinbuDao.getListByIsTY(zHrChainInfoDto.getChainPtCode());
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
                List<String> modelCodeList = new ArrayList<>();
                if(modelMinbuList.size()>0){
                    modelCodeList = tOdsMinbuDao.getListByCodeList(zHrChainInfoDto.getChainPtCode(),modelMinbuList);
                }
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
                chain.setChainPtCode(zHrChainInfoDto.getChainPtCode());
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
        // 上链
        chainCommonService.doChain(zHrChainInfoDto);

        return zHrChainInfoDto;
    }

    @Override
    public List<TOdsMinbu> getMinbuList(String ptCode) {
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

        return tOdsMinbuDao.getListByPtCode(ptCode);
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
        ZHrChainInfo zHrChainInfo = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code" , chainCode));
        //2获取数据库中这个平台的所有最小单元
        Map map = new HashMap<>();
        map.put("ptCode" , zHrChainInfo.getChainPtCode());
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
//        if (!modelName.contains("链群")) {
//            modelName = modelName + "链群";
//        }

        // 获取计数器
        int num = zHrChainInfoDao.getNum();
        String modelCode = zHrChainInfoDto.getParentCode() + "-" + num;

        ZHrChainInfo fuck = new ZHrChainInfo();
        fuck.setChainCode(modelCode);
        fuck.setChainPtCode(zHrChainInfoDto.getChainPtCode());
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
        //2.保存链群的目标信息 兼容XwType3版本
        SaveXwType3 saveXwType3Parent = zHrChainInfoDto.getSaveXwType3();
        saveXwType3Parent.setLqCode(modelCode);
        saveXwType3Parent.setLqName(modelName);
        saveXwType3Parent.setPtCode(zHrChainInfoDto.getChainPtCode());
        modelMinbuList = saveXwType3(saveXwType3Parent);
//        for (ZNodeTargetPercentInfo z:zHrChainInfoDto.getZNodeTargetPercentInfos()) {
//            z.setLqCode(modelCode);
//            z.setLqName(zHrChainInfoDto.getChainName());
//            z.setParentChainCode(zHrChainInfoDto.getParentCode());
//            modelMinbuList.add(z.getNodeCode());
//            zNodeTargetPercentInfoDao.insert(z);
//        }

        //这个地方的逻辑是前端保存的时候只对创单进行设置百分比，后台处理的时候要将体验的同时也保存到数据库表中。
        List<TOdsMinbu> getIsTYModel = tOdsMinbuDao.getListByIsTY(zHrChainInfoDto.getChainPtCode());
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
        List<String> modelCodeList = new ArrayList<>();
        if(modelMinbuList.size()>0){
            modelCodeList = tOdsMinbuDao.getListByCodeList(zHrChainInfoDto.getChainPtCode(),modelMinbuList);
        }
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
        ZHrChainInfo zHrChainInfo = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code" , chainRepairInfo.getChildChainCode()));
        //2获取数据库中这个平台的所有最小单元
        Map map = new HashMap<>();
        map.put("ptCode" , zHrChainInfo.getChainPtCode());
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

    @Override
    public int updateChainTYInfo() {
        int num = 0;
        List<ZHrChainInfo> list = zHrChainInfoDao.selectList(
                new QueryWrapper<ZHrChainInfo>().eq("deleted" , "0"));

        for(ZHrChainInfo chainInfo : list){

            List<ZNodeTargetPercentInfo> nodes = zNodeTargetPercentInfoDao.selectList(
                    new QueryWrapper<ZNodeTargetPercentInfo>()
                            .eq("lq_code" , chainInfo.getChainCode())
                            .isNull("share_percent")
            );

            if(nodes != null && nodes.size() > 0){
                zNodeTargetPercentInfoDao.delete( new QueryWrapper<ZNodeTargetPercentInfo>()
                        .eq("lq_code" , chainInfo.getChainCode())
                        .isNull("share_percent"));
            }

            List<TOdsMinbu> getIsTY = tOdsMinbuDao.getListByIsTY(chainInfo.getChainPtCode());
            for (TOdsMinbu tOdsMinbu:getIsTY){
                ZNodeTargetPercentInfo zNodeTargetPercentInfo =new ZNodeTargetPercentInfo();
                zNodeTargetPercentInfo.setLqCode(chainInfo.getChainCode());
                zNodeTargetPercentInfo.setLqName(chainInfo.getChainName());
                zNodeTargetPercentInfo.setNodeCode(tOdsMinbu.getLittleXwCode());
                zNodeTargetPercentInfo.setNodeName(tOdsMinbu.getLittleXwName());
                zNodeTargetPercentInfo.setXwCode(tOdsMinbu.getXwCode());
                zNodeTargetPercentInfo.setXwName(tOdsMinbu.getXwName());
                zNodeTargetPercentInfoDao.insert(zNodeTargetPercentInfo);
            }
            if(getIsTY.size()>0){
                num++;
            }
        }
        return num;
    }


    @Override
    public int updateTargetNodesXwType3Code() {
        int num = 0;
        List<ZNodeTargetPercentInfo> list = zNodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>()
                .isNull("xwType3Code")
                .isNull("xwType3")
                .isNotNull("share_percent"));
        for(ZNodeTargetPercentInfo node : list){
            TOdsMinbu tOdsMinbu = tOdsMinbuDao.selectOne(new QueryWrapper<TOdsMinbu>()
                    .eq("littleXwCode" , node.getNodeCode()));
            String[] type3codes = tOdsMinbu.getXwType3Code().split("\\|");
            String[] typesNames = tOdsMinbu.getXwType3().split("\\|");
            if(type3codes.length == 2){
                node.setXwType3Code(type3codes[1]);
                node.setXwType3(typesNames[1]);
                zNodeTargetPercentInfoDao.update(node);
                num = num + 1;
            }else{
                for(int i = 0;i<type3codes.length;i++){
                    if(type3codes[i] == null || ("").equals(type3codes[i])){
                        continue;
                    }else{
                        if(node.getXwType3Code() == null || "".equals(node.getXwType3Code())){
                            node.setXwType3Code(type3codes[i]);
                            node.setXwType3(typesNames[i]);
                            zNodeTargetPercentInfoDao.update(node);
                            num = num + 1;
                        }else{
                            ZNodeTargetPercentInfo zNodeTargetPercentInfo = new ZNodeTargetPercentInfo();
                            BeanUtils.copyProperties(node,zNodeTargetPercentInfo);
                            zNodeTargetPercentInfo.setId(null);
                            zNodeTargetPercentInfo.setXwType3Code(type3codes[i]);
                            zNodeTargetPercentInfo.setXwType3(typesNames[i]);
                            zNodeTargetPercentInfoDao.insert(zNodeTargetPercentInfo);
                            num = num + 1;
                        }
                    }
                }
            }
        }
        return num;
    }

    @Override
    public List<TOdsDictionary> getOdsXwType3List() {
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
            minbuMap.put("XwType3Code" , "|" + ods.getCode() + "|");
            List<TOdsMinbu> mins = tOdsMinbuDao.getListByXwType3Code(minbuMap);
            if(mins.size()>0){
                realList.add(ods);
            }else{
                continue;
            }
        }

        return realList;
    }



    @Override
    public List<TOdsDictionary> getOtherOdsXwType3List(String chainCode) {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        Map map = new HashMap<>();
        map.put("chainCode" , chainCode.trim());
        List<TOdsDictionary> list = tOdsDictionaryDao.getOtherOdsXwType3List(map);
        List<TOdsDictionary> realList = new ArrayList<>();
        for(TOdsDictionary ods : list){
            Map minbuMap = new HashMap();
            minbuMap.put("XwType3Code" , "|" + ods.getCode() + "|");
            List<TOdsMinbu> mins = tOdsMinbuDao.getListByXwType3Code(minbuMap);
            if(mins.size()>0){
                realList.add(ods);
            }else{
                continue;
            }
        }
        return realList;
    }



    @Override
    public List<String> saveXwType3(SaveXwType3 saveXwType3) {
        List<String> minbuList = new ArrayList<>();
        List<ZNodeTargetPercentInfo> nodes = new ArrayList<>();
        for(XwType3Info XwType3Info : saveXwType3.getXwType3List()){
            Map map = new HashMap();
            map.put("XwType3Code" , "|" + XwType3Info.getXwType3Code() + "|");
            map.put("ptCode" , saveXwType3.getPtCode());
            List<TOdsMinbu> minbus = tOdsMinbuDao.getListByXwType3Code(map);
            for(TOdsMinbu min : minbus){
                ZNodeTargetPercentInfo zNodeTargetPercentInfo = new ZNodeTargetPercentInfo();
                zNodeTargetPercentInfo.setLqCode(saveXwType3.getLqCode());
                zNodeTargetPercentInfo.setLqName(saveXwType3.getLqName());
                zNodeTargetPercentInfo.setNodeCode(min.getLittleXwCode());
                zNodeTargetPercentInfo.setNodeName(min.getLittleXwName());
                zNodeTargetPercentInfo.setXwCode(min.getXwCode());
                zNodeTargetPercentInfo.setXwName(min.getXwName());
                zNodeTargetPercentInfo.setSharePercent(XwType3Info.getSharePercent());
                zNodeTargetPercentInfo.setParentChainCode(saveXwType3.getParentChainCode());
                zNodeTargetPercentInfo.setXwType3Code(XwType3Info.getXwType3Code());
                zNodeTargetPercentInfo.setTypeSharePercent(XwType3Info.getSharePercent());
                zNodeTargetPercentInfo.setXwType3(XwType3Info.getXwType3());
                nodes.add(zNodeTargetPercentInfo);
                minbuList.add(zNodeTargetPercentInfo.getNodeCode());
            }
        }
        if(nodes.size()>0){
            int num = zNodeTargetPercentInfoDao.insertBatch(nodes);
        }
        return minbuList;
    }


    @Override
    public int delXwType3Nodes(ZNodeTargetPercentInfo zNodeTargetPercentInfo) {
        int num = zNodeTargetPercentInfoDao.delete(new QueryWrapper<ZNodeTargetPercentInfo>()
                .eq("lq_code" , zNodeTargetPercentInfo.getLqCode())
                .eq("xwType3Code" , zNodeTargetPercentInfo.getXwType3Code())
                .isNotNull("share_percent"));
        return num;
    }


    @Override
    public int updateBatchXwType3Nodes(SaveXwType3 saveXwType3) {
        int num = 0;
        for(XwType3Info XwType3Info : saveXwType3.getXwType3List()){
            ZNodeTargetPercentInfo zNodeTargetPercentInfo = new ZNodeTargetPercentInfo();
            zNodeTargetPercentInfo.setLqCode(saveXwType3.getLqCode());
            zNodeTargetPercentInfo.setXwType3Code(XwType3Info.getXwType3Code());
            zNodeTargetPercentInfo.setSharePercent(XwType3Info.getSharePercent());
            zNodeTargetPercentInfo.setTypeSharePercent(XwType3Info.getSharePercent());
            zNodeTargetPercentInfoDao.updateBatchXwType3Nodes(zNodeTargetPercentInfo);
            num++ ;
        }
        return num;
    }


    @Override
    public int syncMinbuListByXwType3(SaveXwType3 saveXwType3) {
        int num = 0;

        // 删除
        for(XwType3Info XwType3Info : saveXwType3.getXwType3List()){
            Map map = new HashMap();
            map.put("lqCode" , saveXwType3.getLqCode());
            map.put("xwType3Code" , XwType3Info.getXwType3Code());
            zNodeTargetPercentInfoDao.deleteListByXwType3Code(map);
        }
        // 新增
        List<String> minbuList = saveXwType3(saveXwType3);
        return minbuList.size();
    }


}
