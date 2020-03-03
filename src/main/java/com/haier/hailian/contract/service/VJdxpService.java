package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.entity.VJdxp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2020-03-02
 */
public interface VJdxpService extends IService<VJdxp> {

    List<StarDTO> getStarList(Map<String, Object> paraMap);
}
