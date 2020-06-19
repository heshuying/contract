package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ZHrChainInfo)表数据库访问层
 *
 * @author makejava
 * @since 2019-12-17 14:52:17
 */
public interface ZHrChainInfoDao extends BaseMapper<ZHrChainInfo> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZHrChainInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZHrChainInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zHrChainInfo 实例对象
     * @return 对象列表
     */
    List<ZHrChainInfo> queryAll(ZHrChainInfo zHrChainInfo);


    /**
     * 修改数据
     *
     * @param zHrChainInfo 实例对象
     * @return 影响行数
     */
    int update(ZHrChainInfo zHrChainInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * id最大的那条数据的链群编码
     * @return
     */
    String queryMaxOne();

    String getDepVCode(String userCode);

    /**
     * 通过ID查询单条数据
     *
     * @param lqCode 主键
     * @return 实例对象
     */
    ZHrChainInfo queryByCode(String lqCode);

    /**
     * 查询用户举单和抢单的链群列表
     * @param userCode
     * @return
     */
    List<ZHrChainInfo> searchChainListByUser(String userCode);

    int getNum();

    void updateNum();

    String getChainAttr(@Param("chainCode") String chainCode);
}