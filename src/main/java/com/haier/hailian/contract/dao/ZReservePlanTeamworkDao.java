package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.entity.ZReservePlanTeamworkDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ZReservePlanTeamwork)表数据库访问层
 *
 * @author makejava
 * @since 2019-12-24 17:32:41
 */
public interface ZReservePlanTeamworkDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZReservePlanTeamwork queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZReservePlanTeamwork> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 对象列表
     */
    List<ZReservePlanTeamworkDto> queryAll(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 新增数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 影响行数
     */
    int insert(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 修改数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 影响行数
     */
    int update(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    int selectCountByParentId(Integer parentId);

    /**
     * 保存数据
     * @param zReservePlanTeamworkDto
     * @return
     */
    int save(ZReservePlanTeamworkDto zReservePlanTeamworkDto);


    int insertDetail(ZReservePlanTeamworkDetail zReservePlanTeamworkDetail);



    int updateByDto(ZReservePlanTeamworkDto zReservePlanTeamworkDto);

}