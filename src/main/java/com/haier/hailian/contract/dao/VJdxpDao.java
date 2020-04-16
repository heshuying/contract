package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;
import com.haier.hailian.contract.entity.VJdxp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2020-03-02
 */
public interface VJdxpDao extends BaseMapper<VJdxp> {
    List<StarDTO> getStarList(Map<String,Object> paraMap);

    VJdxp getOrgStar(ExpectAndActualDiffDto expectAndActualDiffDto);
}
