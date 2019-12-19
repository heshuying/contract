package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.XChainInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (XChainInfo)表数据库访问层
 *
 * @author makejava
 * @since 2019-12-19 14:35:46
 */
public interface XChainInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    XChainInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<XChainInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param xChainInfo 实例对象
     * @return 对象列表
     */
    List<XChainInfo> queryAll(XChainInfo xChainInfo);

    /**
     * 新增数据
     *
     * @param xChainInfo 实例对象
     * @return 影响行数
     */
    int insert(XChainInfo xChainInfo);

    /**
     * 修改数据
     *
     * @param xChainInfo 实例对象
     * @return 影响行数
     */
    int update(XChainInfo xChainInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}