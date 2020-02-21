package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.dao.TOdsMinbuDao;
import com.haier.hailian.contract.entity.TOdsMinbuEmp;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.TOdsMinbuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-28
 */
@Service
public class TOdsMinbuServiceImpl extends ServiceImpl<TOdsMinbuDao, TOdsMinbu> implements TOdsMinbuService {
    @Autowired
    private ZHrChainInfoDao zHrChainInfoDao;

    @Override
    public List<TOdsMinbu> queryMinbuByEmp(String empSn) {
        return baseMapper.queryMinbuByEmp(empSn);
    }

    @Override
    public Boolean isChainMaster(String empSn) {
        if(StringUtils.isBlank(empSn)){
            return false;
        }
        Integer count =zHrChainInfoDao.selectCount(
                new QueryWrapper<ZHrChainInfo>()
                        .eq("master_code",empSn));
        return count>0;
    }
}
