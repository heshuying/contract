package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.haier.hailian.contract.config.ChainConfig;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.util.chian.ChainContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 19012964 on 2019/12/24.
 */
@Slf4j
@Service
public class ChainCommonServiceImpl implements ChainCommonService{
    @Autowired
    private ChainConfig chainConfig;
    @Autowired
    private ZContractsService contractsService;
    @Autowired
    private ZContractsFactorService contractsFactorService;

    private Gson gson=new Gson();
    @Override
    public String uploadJsonData(String json) {
        log.info("========json upload chain start");
        log.info(json);
        // defaults to http://localhost:8545/
        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 设置需要的矿工费
        BigInteger GAS_PRICE = BigInteger.valueOf(0);
        BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
        // 转账人账户地址
        String ownAddress = "0xef678007d18427e6022059dbc264f27507cd1ffc";
        // 被转人账户地址
        // System.out.println("to：");
        // Scanner t = new Scanner(System.in);
        String toAddress = "";// t.next();
        // 私钥
        String prikey = chainConfig.getContractPrivateKey();
        // 公钥
        String pubkey = chainConfig.getContractPublicKeyHead()
                + chainConfig.getContractPublicKeyTail();
        // getNonce
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(ownAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            // 创建交易，转0个ether
            BigInteger value = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, value,
                    json);
            // 签名Transaction，这里要对交易做签名
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Credentials.create(prikey, pubkey));
            String hexValue = Numeric.toHexString(signedMessage);
            // 发送交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("========json upload chain end");
            log.info(transactionHash);
            return transactionHash;
        }catch (Exception e){
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public void doChainAfterGrab(String contractId, String status, String dataHash) {
        if(dataHash.startsWith("0x")){
            dataHash=dataHash.substring(1);
        }
        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 私钥
        String prikey = chainConfig.getContractPrivateKey();
        // 公钥
        String pubkey = chainConfig.getContractPublicKeyHead()
                + chainConfig.getContractPublicKeyTail();
        ECKeyPair KEY_PAIR = new ECKeyPair(Numeric.toBigInt(prikey), Numeric.toBigInt(pubkey));
        Credentials CREDENTIALS = Credentials.create(KEY_PAIR);
        //System.out.println(CREDENTIALS.getAddress());
        String address = "0xef678007d18427e6022059dbc264f27507cd1ffc";
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

            if(ethGetBalance!=null){
                // 打印账户余额
                System.out.println(ethGetBalance.getBalance());
                // 将单位转为以太，方便查看
                System.out.println(Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER));
            }

            //ChainContract cc = ChainContract.deploy(web3, CREDENTIALS,BigInteger.valueOf(0), BigInteger.valueOf(50000000)).send();
            //System.out.println(cc.getContractAddress());
            //cc.Create("c001", "0", "0xc8d6229e0d348b885eea9599271ad4883c0f24e76450ecbcded047906c69e8f8")
            //合约地址可配0x9307adc58cb4d47d0f43a3f30d8a1bd33797debe
            ChainContract cc = ChainContract.load("0x9307adc58cb4d47d0f43a3f30d8a1bd33797debe", web3j, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
            if(cc.isValid())
            {
                TransactionReceipt result = cc.Create(ToBytes32(contractId), BigInteger.valueOf(Integer.valueOf(status)),
                        Numeric.hexStringToByteArray(dataHash)).send();

                String ret=getTxByHash(web3j,"0x76d0e284f2d374b513b6abebf0963f8efb48ae4b43b0efdebfe51ad8d37c240c");
                log.info("合约id：{}，状态：{}，数据：{}，返回:{}",
                        contractId,status,dataHash,ret);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     * 构建合约链群
     * @param contractId
     */
    @Override
    public void buildContractChain(Integer contractId) {
        try{
            ZContracts contracts =contractsService.getById(contractId);
            List<ZContractsFactor> factors=contractsFactorService.list(
                    new QueryWrapper<ZContractsFactor>().eq("contract_id",contractId)
            );
            Map<String , Object> data=new HashedMap();
            data.put("contract",contracts);
            data.put("contractFactor", factors);
            String jsonData=gson.toJson(data);

            String jsonHash=uploadJsonData(jsonData);

            doChainAfterGrab(contracts.getId().toString(),contracts.getStatus(),jsonHash);
        }catch (Exception e){

        }

    }

    public static byte[] ToBytes32(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        String hexstr = hex.toString() + String.join("", Collections.nCopies(32 - (hex.length() / 2), "00"));
        byte[] myStringInByte = Numeric.hexStringToByteArray(hexstr);
        return myStringInByte;
    }

    public static String getTxByHash(Web3j web3j, String txHash) throws IOException {
        EthTransaction tx = web3j.ethGetTransactionByHash(txHash).send();
        String str =tx.getTransaction().get().getInput();
        return str;
    }
}
