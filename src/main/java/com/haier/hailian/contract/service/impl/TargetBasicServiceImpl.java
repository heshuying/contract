package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.dao.TargetBasicDao;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.service.TargetBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
@Service
public class TargetBasicServiceImpl extends ServiceImpl<TargetBasicDao, TargetBasic> implements TargetBasicService {

    @Autowired
    private TargetBasicDao targetBasicDao;

    @Override
    public List<TargetBasic> selectBottom(QueryBottomDTO dto) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setChainCode(dto.getChainCode());
        String monthStr = month < 10 ? "0" + month : "" + month;
        targetBasic.setPeriodCode(year+monthStr);
        List<TargetBasic> list = targetBasicDao.selectTarget(targetBasic);
        return list;
    }
}
