package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.dao.TOdsMinbuDao;
import com.haier.hailian.contract.entity.TOdsMinbuEmp;
import com.haier.hailian.contract.service.TOdsMinbuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    @Override
    public List<TOdsMinbu> queryMinbuByEmp(String empSn) {
        return baseMapper.queryMinbuByEmp(empSn);
    }

}