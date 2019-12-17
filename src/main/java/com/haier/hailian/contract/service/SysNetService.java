package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.SysNet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-16
 */
public interface SysNetService extends IService<SysNet> {
    List<SysNet> queryByXwcode(String xwcode);
}
