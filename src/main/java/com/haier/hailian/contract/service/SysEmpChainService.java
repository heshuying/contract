package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.SysEmpChain;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-23
 */
public interface SysEmpChainService extends IService<SysEmpChain> {
    /**
     * @param empSn
     * @return
     */
    String buildChainCode(String empSn);

    void batchCreateChainForEmp();
}
