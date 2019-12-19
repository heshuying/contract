package com.haier.hailian.contract.service.impl;


import com.haier.hailian.contract.dao.SysNodeEhrDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dao.ZNodeTargetPercentInfoDao;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ValidateChainNameDTO;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//        CurrentUser currentUser = sysUser.getCurrentUser();
        //校验链群名称是否已经存在
        ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
        zHrChainInfo.setChainName(validateChainNameDTO.getChainName());
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
    public List<ZNodeTargetPercentInfo> getNodeTargetList(String empCodeStr) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateNowStr = sdf.format(d);
        System.out.println("格式化后的日期：" + dateNowStr);
        String[] strings = empCodeStr.split(",");
        List<String> list= Arrays.asList(strings);
        return zNodeTargetPercentInfoDao.queryByKeyWorld(list,dateNowStr);
    }
}