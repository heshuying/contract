package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.ZWaringPeriodConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 链群抢单举单预警配置表(ZWaringPeriodConfig)表数据库访问层
 *
 * @author makejava
 * @since 2020-01-17 09:22:04
 */
public interface ZWaringPeriodConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZWaringPeriodConfig queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZWaringPeriodConfig> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zWaringPeriodConfig 实例对象
     * @return 对象列表
     */
    List<ZWaringPeriodConfig> queryAll(ZWaringPeriodConfig zWaringPeriodConfig);

    /**
     * 新增数据
     *
     * @param zWaringPeriodConfig 实例对象
     * @return 影响行数
     */
    int insert(ZWaringPeriodConfig zWaringPeriodConfig);

    /**
     * 修改数据
     *
     * @param zWaringPeriodConfig 实例对象
     * @return 影响行数
     */
    int update(ZWaringPeriodConfig zWaringPeriodConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}