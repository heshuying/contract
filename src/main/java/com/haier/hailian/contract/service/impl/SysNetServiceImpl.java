package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.entity.SysNet;
import com.haier.hailian.contract.dao.SysNetDao;
import com.haier.hailian.contract.service.SysNetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-16
 */
@Service
public class SysNetServiceImpl extends ServiceImpl<SysNetDao, SysNet> implements SysNetService {
    @Override
    public List<SysNet> queryByXwcode(String xwcode) {
        List<SysNet> list=baseMapper.selectList(
                new QueryWrapper<SysNet>().eq("xw_code",xwcode)
        );
        return list;
    }
}
