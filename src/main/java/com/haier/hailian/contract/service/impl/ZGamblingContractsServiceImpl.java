package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.SysXiaoweiEhrDao;
import com.haier.hailian.contract.dto.GamblingContractDTO;
import com.haier.hailian.contract.entity.XiaoweiEhr;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-19
 */
@Service
public class ZGamblingContractsServiceImpl implements ZGamblingContractsService {

    @Autowired
    private SysXiaoweiEhrDao sysXiaoweiEhrDao;

    @Override
    public void saveGambling(GamblingContractDTO dto) {
        //保存合同主表
//        ZGamblingContracts contracts = new ZGamblingContracts();
//        contracts.setChainCode(dto.getChainCode());
//        contracts.setStartDate(dto.getStartDate());
//        contracts.setEndDate(dto.getEndDate());
//        Subject subject = SecurityUtils.getSubject();
//        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
//        contracts.setCreateCode(sysUser.getEmpSn());
//        contracts.setCreateDate(now());
//        contracts.setCreateName(sysUser.getEmpName());
//        zGamblingContractsDao.insert(contracts);
//        //保存目标表
//        int id = contracts.getId();
//        List<ZGamblingContractsProductIndex> list = dto.getIndexList();
//        for(ZGamblingContractsProductIndex index:list){
//            index.setContractsId(id);
//            indexDao.insert(index);
//        }
    }

    @Override
    public List<XiaoweiEhr> selectMarket() {

        List<XiaoweiEhr> list = sysXiaoweiEhrDao.selectMarket();
        return list;
    }
}
