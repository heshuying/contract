package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * (ZReservePlanTeamwork)表服务接口
 *
 * @author makejava
 * @since 2019-12-24 17:32:41
 */
public interface ZReservePlanTeamworkService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZReservePlanTeamwork queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZReservePlanTeamwork> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 实例对象
     */
    ZReservePlanTeamwork insert(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 修改数据
     *
     * @param zReservePlanTeamwork 实例对象
     * @return 实例对象
     */
    ZReservePlanTeamwork update(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


    /**
     * 查询链群合约下的所有的问题
     * @param parentId
     * @return
     */
    int queryCountByParentId(Integer parentId);

    /**
     * 根据条件查询数据
     * @param zReservePlanTeamwork
     * @return
     */
    List<ZReservePlanTeamworkDto> queryAllByKey(ZReservePlanTeamwork zReservePlanTeamwork);

    /**
     * 保存并联协同数据
     * @param zReservePlanTeamworkDto
     * @return
     */
    String saveAllInfo(ZReservePlanTeamworkDto zReservePlanTeamworkDto);


    String createGroup(int id);

}