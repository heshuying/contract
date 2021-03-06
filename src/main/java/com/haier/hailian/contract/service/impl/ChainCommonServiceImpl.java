package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.haier.hailian.contract.config.ChainConfig;
import com.haier.hailian.contract.dao.ZContractsProductDao;
import com.haier.hailian.contract.dto.ChainResultNewDto;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZContractsProduct;
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.util.chian.ChainContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
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
    private RestTemplate nRestTemplate;
    @Autowired
    private ZContractsService contractsService;
    @Autowired
    private ZContractsFactorService contractsFactorService;
    @Autowired
    private ZContractsProductDao contractsProductDao;

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
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";//chainConfig.getContractPrivateKey();
        // 公钥
        String pubkey ="0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76"; //chainConfig.getContractPublicKeyHead()+ chainConfig.getContractPublicKeyTail();
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
            log.error("{},异常{}", "uploadJsonData", e.getMessage());
            return "";
        }
    }

    @Override
    public void doChainAfterGrab(ZContracts contract, String dataHash) {
        if(dataHash.startsWith("0x")){
            dataHash=dataHash.substring(2);
        }
        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 私钥
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        // 公钥
        String pubkey = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
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
            ChainContract cc = ChainContract.load("0xe8108ee3d27f3a7d6365c6db91358b28b23698af", web3j, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
            if(cc.isValid())
            {
                TransactionReceipt result = cc.Create(ToBytes32(contract.getParentId().toString()),
                        ToBytes32(contract.getId().toString()), BigInteger.valueOf(Integer.valueOf(contract.getStatus())),
                        Numeric.hexStringToByteArray(dataHash)).send();

                log.info("合约id：{}，数据Hash：{}，返回:{}",
                        contract.getId(),dataHash, result);
            }
        } catch (Exception e){
            log.error("{},异常{}", "doChainAfterGrab", e.getMessage());
        }
    }

    @Override
    public void doChain(String json) {
        new Thread(new Runnable(){
            public void run(){
                String jsonHash=doUploadChain(json);
                log.info("data：{}，返回:{}",
                        json,jsonHash);
            }
        }).start();
    }


    @Override
    public void doChain(Object object) {
        new Thread(new Runnable(){
            public void run(){
                String json = gson.toJson(object);
                String jsonHash=doUploadChain(json);
                log.info("data：{}，返回:{}",
                        json,jsonHash);
            }
        }).start();
    }

    @Override
    public void uploadBigContract(Integer contractId){
        Map<String,Object> bigData=new HashMap<>();
        ZContracts contracts =contractsService.getById(contractId);
        List<ZContractsFactor> factors=contractsFactorService.list(
                new QueryWrapper<ZContractsFactor>().eq("contract_id",contractId)
        );

        List<ZContractsProduct> products=contractsProductDao.selectList(
                new QueryWrapper<ZContractsProduct>().eq("contract_id",contractId)
        );
        bigData.put("main_"+contractId,contracts);
        bigData.put("factor_"+contractId,factors);
        bigData.put("product_"+contractId, products);
        String contractJsonHash=doUploadChain(gson.toJson(bigData));
        log.info("合约id：{}，返回:{}",
                contractId,contractJsonHash);
    }

    public void doChainAfterGrab(String contractId, List<ZContractsFactor> factors) {

        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 私钥
        // 私钥
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        // 公钥
        String pubkey = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
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
            ChainContract cc = ChainContract.load("0xe8108ee3d27f3a7d6365c6db91358b28b23698af", web3j, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
            if(cc.isValid())
            {
                for (ZContractsFactor fac:factors) {
                    String departCode = "";
                    if (StringUtils.isNoneBlank(fac.getMeshCode())) {
                        departCode = fac.getMeshCode();
                    } else if (StringUtils.isNoneBlank(fac.getRegionCode())) {
                        departCode = fac.getRegionCode();
                    } else {
                        departCode = contractId;
                    }
                    TransactionReceipt result = cc.InsertTatget(ToBytes32(contractId),
                            ToBytes32(departCode), ToBytes32(fac.getFactorCode()), ToBytes32(fac.getFactorType()),
                            ToBytes32(fac.getFactorValue())).send();

                    log.info("合约id：{}，factor数据：{}，返回:{}",
                            contractId, gson.toJson(fac), result);
                }

            }
        } catch (Exception e){
            log.error("{},异常{}", "doChainAfterGrab", e.getMessage());
        }
    }

    public void doChainAfterGrabProduct(String contractId, List<ZContractsProduct> products) {

        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 私钥
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        // 公钥
        String pubkey = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
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
            ChainContract cc = ChainContract.load("0xe8108ee3d27f3a7d6365c6db91358b28b23698af", web3j, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
            if(cc.isValid())
            {
                for (ZContractsProduct product:products) {
                    String departCode = "";
                    departCode = contractId;

                    TransactionReceipt result = cc.InsertTatget(ToBytes32(contractId),
                            ToBytes32(departCode), ToBytes32(product.getProductSeries()), ToBytes32(product.getQtyYear()+""),
                            ToBytes32(product.getQtyMonth()+"")).send();

                    log.info("合约id：{}，factor数据：{}，返回:{}",
                            contractId, gson.toJson(product), result);
                }

            }
        } catch (Exception e){
            log.error("{},异常{}", "doChainAfterGrab", e.getMessage());
        }
    }

    @Override
    public void getContractFromChain(Integer contractId) {
        ChainContract chain=getContracChain();
        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        if(chain!=null){
            try {
                byte[] parantid = chain.COf(ToBytes32(contractId.toString())).send().getValue1();
                byte[] hash = chain.COf(ToBytes32(contractId.toString())).send().getValue3();
                String hashCode="0x"+new String(hash);

                log.info(hashCode);
                //String hashCode1="0x"+ (String) hash;
                String json=  getTxByHash(web3j ,hashCode);
                log.info(json);

            }catch (Exception e){
                log.error("获取上链信息异常，入参{},异常{}",contractId, e.getMessage());
            }
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

            List<ZContractsProduct> products=contractsProductDao.selectList(
                    new QueryWrapper<ZContractsProduct>().eq("contract_id",contractId)
            );
            String contractJsonHash=uploadJsonData(gson.toJson(contracts));

            doChainAfterGrab(contracts,contractJsonHash);
            doChainAfterGrab(contracts.getId().toString(),factors);
            doChainAfterGrabProduct(contracts.getId().toString(),products);
        }catch (Exception e){
            log.error("合约：{}，上链产生异常",contractId);
            log.error(e.getMessage());
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

    private ChainContract getContracChain(){
        Web3j web3j = Web3j.build(new HttpService(chainConfig.getContractUri()));
        // 私钥
        // 私钥
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        // 公钥
        String pubkey = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aaba645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
        ECKeyPair KEY_PAIR = new ECKeyPair(Numeric.toBigInt(prikey), Numeric.toBigInt(pubkey));
        Credentials CREDENTIALS = Credentials.create(KEY_PAIR);
        //System.out.println(CREDENTIALS.getAddress());
        String address = "0xef678007d18427e6022059dbc264f27507cd1ffc";
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

            if (ethGetBalance != null) {
                // 打印账户余额
                System.out.println(ethGetBalance.getBalance());
                // 将单位转为以太，方便查看
                System.out.println(Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER));
            }

            //ChainContract cc = ChainContract.deploy(web3, CREDENTIALS,BigInteger.valueOf(0), BigInteger.valueOf(50000000)).send();
            //System.out.println(cc.getContractAddress());
            //cc.Create("c001", "0", "0xc8d6229e0d348b885eea9599271ad4883c0f24e76450ecbcded047906c69e8f8")
            //合约地址可配0x9307adc58cb4d47d0f43a3f30d8a1bd33797debe
            ChainContract cc = ChainContract.load("0xe8108ee3d27f3a7d6365c6db91358b28b23698af", web3j, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
            if (cc.isValid()) {
                return cc;
            }else{
                return null;
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }



    private String doUploadChain(String data){
        log.info("=====上链数据====>",data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api_gateway_auth_app_id",chainConfig.getApiGatewayAuthAppId());
        headers.set("api_gateway_auth_app_password",chainConfig.getApiGatewayAuthAppPassword());

        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        try {
            ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(chainConfig.getUploadUrl(),
                    entity, String.class);

            String value = responseEntity.getBody();
            log.info("=====上链返回结果:{}", value);
            ChainResultNewDto result = gson.fromJson(value, ChainResultNewDto.class);
            if (result.getResultCode().equals("10")) {
                return result.getData();
            } else {
                return "";
            }
        }catch (Exception e){
            log.info("=====上链发生异常:{}====>", e.getMessage());
            return "";
        }

    }

    private String doValidChain(String hashCode){
        log.info("=====校验数据====>",hashCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api_gateway_auth_app_id",chainConfig.getApiGatewayAuthAppId());
        headers.set("api_gateway_auth_app_password",chainConfig.getApiGatewayAuthAppPassword());

        HttpEntity<String> entity = new HttpEntity<String>(hashCode, headers);
        try {
            ResponseEntity<String> responseEntity = nRestTemplate.postForEntity(chainConfig.getValidUrl(),
                    entity, String.class);

            String value = responseEntity.getBody();
            log.info("=====校验数据结果====>");
            log.info(value);
            return value;
        }catch (Exception e){
            log.error("=====上链发生异常:{}====>", e.getMessage());
            return "";
        }

    }
}
