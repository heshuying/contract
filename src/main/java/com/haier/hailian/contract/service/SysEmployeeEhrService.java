package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-16
 */
public interface SysEmployeeEhrService extends IService<SysEmployeeEhr> {
    SysEmployeeEhr getEmployeeEhr(String empSn);
}
