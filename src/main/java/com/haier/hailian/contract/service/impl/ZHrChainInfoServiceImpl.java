package com.haier.hailian.contract.service.impl;


import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ValidateChainNameDTO;
import com.haier.hailian.contract.dto.ZHrChainInfoDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ZHrChainInfo queryById(Integer id) {
        return this.zHrChainInfoDao.queryById(id);
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
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
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
//        zHrChainInfo.setCdMasterEmpsn(sysUser.getEmpSn());
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
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("001");
        targetBasic.setTargetPtCode(currentUser.getPtcode());
        targetBasic.setTargetXwCategoryCode(nodeCodeStr);
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    public ZHrChainInfoDto saveChainInfo(ZHrChainInfoDto zHrChainInfoDto) {
        //1.保存链群信息
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
//        List<SysXiaoweiEhr> list = sysUser.getXiaoweiEhrList();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
//        SysXiaoweiEhr xiaoweiEhr = list.get(0);
        //链群编码生成
        String maxOne = zHrChainInfoDao.queryMaxOne();
        String chainCode = frontCompWithZore(maxOne, 5, "H");
        //判断是否存在链群关键字，不存在则添加
        String name = zHrChainInfoDto.getChainName();
        if (!name.contains("链群")) {
            name = name + "链群";
        }
        zHrChainInfo.setChainCode(chainCode);
        zHrChainInfo.setChainPtCode(currentUser.getPtcode());
        zHrChainInfo.setMasterCode(currentUser.getEmpsn());
        zHrChainInfo.setMasterName(currentUser.getEmpname());
        zHrChainInfo.setXwCode(currentUser.getOrgNum());
        zHrChainInfo.setXwName(currentUser.getOrgName());
        zHrChainInfo.setChainName(name);
        zHrChainInfoDao.insert(zHrChainInfo);
        //2.保存链群的目标信息
        for (ZNodeTargetPercentInfo z:zHrChainInfoDto.getZNodeTargetPercentInfos()) {
            z.setLqCode(chainCode);
            z.setLqName(name);
            zNodeTargetPercentInfoDao.insert(z);
        }
        List<TOdsMinbu> getIsTY = tOdsMinbuDao.getListByIsTY(currentUser.getPtcode());
        for (TOdsMinbu tOdsMinbu:getIsTY){
            ZNodeTargetPercentInfo zNodeTargetPercentInfo =new ZNodeTargetPercentInfo();
            zNodeTargetPercentInfo.setLqCode(chainCode);
            zNodeTargetPercentInfo.setLqName(name);
            zNodeTargetPercentInfo.setNodeCode(tOdsMinbu.getLittleXwCode());
            zNodeTargetPercentInfo.setNodeName(tOdsMinbu.getLittleXwName());
            zNodeTargetPercentInfo.setXwCode(tOdsMinbu.getXwCode());
            zNodeTargetPercentInfo.setXwName(tOdsMinbu.getXwName());
        }
        zHrChainInfoDto.setId(zHrChainInfo.getId());
        zHrChainInfoDto.setChainCode(chainCode);
        zHrChainInfoDto.setMasterCode(currentUser.getEmpsn());
        zHrChainInfoDto.setMasterName(currentUser.getEmpname());
        zHrChainInfoDto.setChainPtCode(currentUser.getPtcode());
        zHrChainInfoDto.setXwCode(currentUser.getOrgNum());
        zHrChainInfoDto.setXwName(currentUser.getOrgName());
        zHrChainInfoDto.setChainName(name);
        //3.保存数据到链上
        //接口调用的时候会用到这个dto的实体类
        return zHrChainInfoDto;
    }

    @Override
    public List<TOdsMinbu> getMinbuList() {
        //1获取当前登陆人的平台信息
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        if (currentUser == null || currentUser.getXwCode() == null){
            return null;
        }
        //2获取数据库中这个平台的所有最小单元

        return tOdsMinbuDao.getListByPtCode(currentUser.getPtcode());
    }


    /**
     * 　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     * 　　* @param sourceDate  编码在当前数据库中的最大值
     * 　　* @param formatLength 编码的数字长度
     * 　　* @param key  编码开始字母
     * 　　* @return 重组后的数据
     */

    private static String frontCompWithZore(String sourceDate, int formatLength, String key) {

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

    public static void main(String[] args) {
        System.out.println(frontCompWithZore("ACC00003", 10, "ACC"));
    }

}