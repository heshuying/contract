package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.entity.SysEmpChain;
import com.haier.hailian.contract.dao.SysEmpChainDao;
import com.haier.hailian.contract.service.SysEmpChainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.util.Constant;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.List;

import static org.web3j.crypto.Hash.sha256;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-23
 */
@Service
public class SysEmpChainServiceImpl extends ServiceImpl<SysEmpChainDao, SysEmpChain> implements SysEmpChainService {
    @Override
    public String buildChainCode(String empSn) {

        String seedstr = Constant.CHAIN_SEED + empSn;
        byte[] seed = seedstr.getBytes();
        ECKeyPair keypair = ECKeyPair.create(sha256(seed));
        System.out.println("private:"+keypair.getPrivateKey().toString(16));
        System.out.println("public:"+keypair.getPublicKey().toString(16));
        System.out.println("0x" + Keys.getAddress(keypair));
        return seedstr;
    }

    @Override
    public void batchCreateChainForEmp() {
        while (true){
            //每组获取100个用户
            List<String> emps=baseMapper.selectNoChainEmp();
            if(emps==null||emps.size()==0){
                break;
            }

            List<SysEmpChain> empChains=new ArrayList<>();
            for (String empsn:emps) {
                String chain=buildChainCode(empsn);
                SysEmpChain empChain=new SysEmpChain();
                empChain.setEmpSn(empsn);
                empChain.setChainCode(chain);
            }
            this.saveBatch(empChains);
        }

    }


}
