package com.haier.hailian.contract.util.chian;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * 单志刚提供上链代码
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class ChainContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6103638061001e6000396000f30060606040526004361061008d5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663071d78e68114610092578063288330e8146100ba578063950d8a72146100d5578063b1a4da18146100f7578063b7a6cc8d14610110578063da5cb8691461012f578063eaeaa66c14610171578063ec0366851461019f575b600080fd5b341561009d57600080fd5b6100a86004356101b8565b60405190815260200160405180910390f35b34156100c557600080fd5b6100d36004356024356101ca565b005b34156100e057600080fd5b6100d36004356024356044356064356084356101e0565b341561010257600080fd5b6100d360043560243561025c565b341561011b57600080fd5b6100d360043560243560443560643561026b565b341561013a57600080fd5b6101456004356102c1565b604051938452602084019290925260408084019190915290151560608301526080909101905180910390f35b341561017c57600080fd5b6101876004356102eb565b60405191825260208201526040908101905180910390f35b34156101aa57600080fd5b6100d3600435602435610308565b60026020526000908152604090205481565b60008281526002602052604090208190555b5050565b60008581526001602081905260409182902086815590810185905560038101849055600201829055849086907fa7fe58257baf82e8734af54b55b06c49af10bc6f6871f159a429a372d0dc1e21908690869086905192835260208301919091526040808301919091526060909101905180910390a35050505050565b600082905260026020526101dc565b60008381526020819052604090206003015460ff161561028a576102bb565b60008381526020819052604090208481556001808201849055600282018390556003909101805460ff191690911790555b50505050565b60006020819052908152604090208054600182015460028301546003909301549192909160ff1684565b600090815260208190526040902060018101546002909101549091565b60008281526020819052604090206003015460ff16156101dc57600091825260208290526040909120600101555600a165627a7a72305820c936659c35ca2cb3648b27681b32af01c69b34d9d0e54f2f65cbc1b5266d23b10029";

    protected ChainContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChainContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TargetLogEventResponse> getTargetLogEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TargetLog",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TargetLogEventResponse> responses = new ArrayList<TargetLogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TargetLogEventResponse typedResponse = new TargetLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ccode = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.partCode = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tcode = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ttype = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tval = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TargetLogEventResponse> targetLogEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TargetLog",
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TargetLogEventResponse>() {
            @Override
            public TargetLogEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TargetLogEventResponse typedResponse = new TargetLogEventResponse();
                typedResponse.log = log;
                typedResponse.ccode = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.partCode = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tcode = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ttype = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tval = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<byte[]> ChainInfo(byte[] param0) {
        final Function function = new Function("ChainInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> UpdataInfo(byte[] chaiID, byte[] chainInfo) {
        final Function function = new Function(
                "UpdataInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chaiID),
                        new org.web3j.abi.datatypes.generated.Bytes32(chainInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> InsertTatget(byte[] ccode, byte[] partCode, byte[] tcode, byte[] ttype, byte[] tval) {
        final Function function = new Function(
                "InsertTatget",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(ccode),
                        new org.web3j.abi.datatypes.generated.Bytes32(partCode),
                        new org.web3j.abi.datatypes.generated.Bytes32(tcode),
                        new org.web3j.abi.datatypes.generated.Bytes32(ttype),
                        new org.web3j.abi.datatypes.generated.Bytes32(tval)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> SetInfo(byte[] chaiID, byte[] chainInfo) {
        final Function function = new Function(
                "SetInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(chaiID),
                        new org.web3j.abi.datatypes.generated.Bytes32(chainInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Create(byte[] parentid, byte[] ccode, BigInteger cstate, byte[] hashcode) {
        final Function function = new Function(
                "Create",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(parentid),
                        new org.web3j.abi.datatypes.generated.Bytes32(ccode),
                        new org.web3j.abi.datatypes.generated.Uint256(cstate),
                        new org.web3j.abi.datatypes.generated.Bytes32(hashcode)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple4<byte[], BigInteger, byte[], Boolean>> COf(byte[] param0) {
        final Function function = new Function("COf",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {},
                        new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple4<byte[], BigInteger, byte[], Boolean>>(
                new Callable<Tuple4<byte[], BigInteger, byte[], Boolean>>() {
                    @Override
                    public Tuple4<byte[], BigInteger, byte[], Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<byte[], BigInteger, byte[], Boolean>(
                                (byte[]) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (byte[]) results.get(2).getValue(),
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<Tuple2<BigInteger, byte[]>> GetInfo(byte[] ccode) {
        final Function function = new Function("GetInfo",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(ccode)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple2<BigInteger, byte[]>>(
                new Callable<Tuple2<BigInteger, byte[]>>() {
                    @Override
                    public Tuple2<BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(),
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> UpdateState(byte[] ccode, BigInteger cstate) {
        final Function function = new Function(
                "UpdateState",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(ccode),
                        new org.web3j.abi.datatypes.generated.Uint256(cstate)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ChainContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChainContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ChainContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChainContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ChainContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChainContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ChainContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChainContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class TargetLogEventResponse {
        public Log log;

        public byte[] ccode;

        public byte[] partCode;

        public byte[] tcode;

        public byte[] ttype;

        public byte[] tval;
    }
}