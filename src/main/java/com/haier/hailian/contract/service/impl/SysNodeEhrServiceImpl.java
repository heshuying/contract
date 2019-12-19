package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.dao.SysNodeEhrDao;
import com.haier.hailian.contract.service.SysNodeEhrService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SysNodeEhr)表服务实现类
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
@Service("sysNodeEhrService")
public class SysNodeEhrServiceImpl implements SysNodeEhrService {
    @Resource
    private SysNodeEhrDao sysNodeEhrDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysNodeEhr queryById(Integer id) {
        return this.sysNodeEhrDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<SysNodeEhr> queryAllByLimit(int offset, int limit) {
        return this.sysNodeEhrDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param sysNodeEhr 实例对象
     * @return 实例对象
     */
    @Override
    public SysNodeEhr insert(SysNodeEhr sysNodeEhr) {
        this.sysNodeEhrDao.insert(sysNodeEhr);
        return sysNodeEhr;
    }

    /**
     * 修改数据
     *
     * @param sysNodeEhr 实例对象
     * @return 实例对象
     */
    @Override
    public SysNodeEhr update(SysNodeEhr sysNodeEhr) {
        this.sysNodeEhrDao.update(sysNodeEhr);
        return this.queryById(sysNodeEhr.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sysNodeEhrDao.deleteById(id) > 0;
    }
}