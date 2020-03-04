package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.entity.TOdsDictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19033715
 * @since 2020-03-03
 */
public interface TOdsDictionaryDao extends BaseMapper<TOdsDictionary> {

    List<TOdsDictionary> getOtherOdsXwType3List(Map map);

}
