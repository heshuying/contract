package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.dao.TargetBasicDao;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.service.TargetBasicService;
import com.haier.hailian.contract.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public List<TargetBasic> selectBottom(QueryBottomDTO dto){
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setChainCode(dto.getChainCode());
        String date = dto.getStartDate();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMM");
        try {
            targetBasic.setPeriodCode(sf2.format(sf.parse(date)));
        } catch (ParseException e) {
            throw new RException("日期类型不正确", Constant.CODE_VALIDFAIL);
        }
        List<TargetBasic> list = targetBasicDao.selectTarget(targetBasic);
        for(TargetBasic targetBasic1:list){
            if(null != targetBasic1.getTargetBottomLine() && !"".equals(targetBasic1.getTargetBottomLine())){
                String bottom = targetBasic1.getTargetBottomLine();
                int position = bottom.length() - bottom.indexOf(".") - 1;
                if(position==1) bottom = bottom.replaceAll("\\.0","");
                targetBasic1.setTargetBottomLine(bottom);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
            if(null != targetBasic1.getTargetJdLine() && !"".equals(targetBasic1.getTargetJdLine())){
                String e2e = targetBasic1.getTargetJdLine();
                int position = e2e.length() - e2e.indexOf(".") - 1;
                if(position==1) e2e = e2e.replaceAll("\\.0","");
                targetBasic1.setTargetJdLine(e2e);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
        }
        return list;
    }
}
