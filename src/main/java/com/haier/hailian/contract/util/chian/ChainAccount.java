package com.haier.hailian.contract.util.chian;

import org.web3j.crypto.*;
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
import java.util.*;
import static org.web3j.crypto.Hash.sha256;

/**
 * BlockChain ChainAccount Manager
 *
 */
public class ChainAccount {
    public static void main(String[] args) throws Exception {
        
        // Random r = new Random(1);
        // for(int i=0;i<10;i++)
        // {
           
        //     int ran1 = r.nextInt(1000000000);
        //     Generate(ran1);
        // }
        Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8501"));  // defaults to http://localhost:8545/
        String PRIVATE_KEY = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        String PUBLIC_KEY = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aab"
                + "a645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
        ECKeyPair KEY_PAIR = new ECKeyPair(Numeric.toBigInt(PRIVATE_KEY), Numeric.toBigInt(PUBLIC_KEY));
        Credentials CREDENTIALS = Credentials.create(KEY_PAIR);
        //System.out.println(CREDENTIALS.getAddress());
        String address = "0xef678007d18427e6022059dbc264f27507cd1ffc";
        EthGetBalance ethGetBalance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

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
        ChainContract cc = ChainContract.load("0x9307adc58cb4d47d0f43a3f30d8a1bd33797debe", web3, CREDENTIALS, BigInteger.valueOf(0), BigInteger.valueOf(200000));
        System.out.println(cc.isValid());
        //String cid = "c001";
        //byte[] scp_cid = Numeric.hexStringToByteArray(asciiToHex(cid));

        TransactionReceipt  result = cc.Create(ToBytes32("c001"), BigInteger.valueOf(0), Numeric.hexStringToByteArray("8c5bfb49ce767d7bb910d676850bbea5c4ef71ddb14c86473e1b97d5f69f6168")).send();
        System.out.println(result);
        getTxByHash(web3,"0x76d0e284f2d374b513b6abebf0963f8efb48ae4b43b0efdebfe51ad8d37c240c");
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

    // 上传json
    public static String SendTrans(Web3j web3j) throws Exception {
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
        String prikey = "0xa392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";
        // 公钥
        String pubkey = "0x506bc1dc099358e5137292f4efdd57e400f29ba5132aa5d12b18dac1c1f6aab"
                + "a645c0b7b58158babbfa6c6cd5a48aa7340a8749176b120e8516216787a13dc76";
        // getNonce
        EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount(ownAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        // 创建交易，转0个ether
        BigInteger value = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();
        String data = "要上传的json数据";
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, value,
                data);
        // 签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Credentials.create(prikey, pubkey));
        String hexValue = Numeric.toHexString(signedMessage);
        // 发送交易
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        // 打印transactionHash
        return transactionHash;
    }

    public static void getTxByHash(Web3j web3j, String txHash) throws IOException {
        EthTransaction tx = web3j.ethGetTransactionByHash(txHash).send();
        String str =tx.getTransaction().get().getInput();
        System.out.println(str);
    }

    //参数可以根据需要添加用户id信息。
    public static void Generate(int ran)
    {
        String seedstr ="海链-baas-" + ran;
        byte[] seed = seedstr.getBytes();
        ECKeyPair keypair = ECKeyPair.create(sha256(seed));
        System.out.println("private:"+keypair.getPrivateKey().toString(16));
        System.out.println("public:"+keypair.getPublicKey().toString(16));
        System.out.println("0x" + Keys.getAddress(keypair));
        
    }

    public static Web3j createWeb3j(){
        Web3j web3 = Web3j.build(new HttpService("http://10.138.93.29:8102"));
        return web3;
    }

}
