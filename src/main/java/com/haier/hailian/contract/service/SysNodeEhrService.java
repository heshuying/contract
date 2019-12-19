package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.SysNodeEhr;
import java.util.List;

/**
 * (SysNodeEhr)表服务接口
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
public interface SysNodeEhrService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysNodeEhr queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysNodeEhr> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysNodeEhr 实例对象
     * @return 实例对象
     */
    SysNodeEhr insert(SysNodeEhr sysNodeEhr);

    /**
     * 修改数据
     *
     * @param sysNodeEhr 实例对象
     * @return 实例对象
     */
    SysNodeEhr update(SysNodeEhr sysNodeEhr);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}