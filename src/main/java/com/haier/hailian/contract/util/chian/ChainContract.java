package com.haier.hailian.contract.util.chian;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
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
    private static final String BINARY = "608060405234801561001057600080fd5b50610537806100206000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063b1a4da181161005b578063b1a4da181461015c578063e9a03a0114610194578063eaeaa66c146101d6578063ec0366851461021f5761007d565b8063071d78e614610082578063288330e8146100c457806371a0d74a146100fc575b600080fd5b6100ae6004803603602081101561009857600080fd5b8101908080359060200190929190505050610257565b6040518082815260200191505060405180910390f35b6100fa600480360360408110156100da57600080fd5b81019080803590602001909291908035906020019092919050505061026f565b005b61015a600480360360c081101561011257600080fd5b810190808035906020019092919080359060200190929190803590602001909291908035906020019092919080359060200190929190803590602001909291905050506102ad565b005b6101926004803603604081101561017257600080fd5b8101908080359060200190929190803590602001909291905050506103af565b005b6101d4600480360360608110156101aa57600080fd5b810190808035906020019092919080359060200190929190803590602001909291905050506103ed565b005b610202600480360360208110156101ec57600080fd5b8101908080359060200190929190505050610482565b604051808381526020018281526020019250505060405180910390f35b6102556004803603604081101561023557600080fd5b8101908080359060200190929190803590602001909291905050506104ba565b005b60026020528060005260406000206000915090505481565b60006002600084815260200190815260200160002050602060ff1611156102a9578060026000848152602001908152602001600020819055505b5050565b83600160008881526020019081526020016000206000878152602001908152602001600020600001819055508260016000888152602001908152602001600020600087815260200190815260200160002060010181905550816001600088815260200190815260200160002060008781526020019081526020016000206003018190555080600160008881526020019081526020016000206000878152602001908152602001600020600201819055508385877f56bb19b7dff93d71338f2f81d05f931f884f341c6106a3be4dcc11f574fc75c686868660405180848152602001838152602001828152602001935050505060405180910390a4505050505050565b60006002600084815260200190815260200160002050602060ff1614156103e9578060026000848152602001908152602001600020819055505b5050565b60008084815260200190815260200160002060020160009054906101000a900460ff161561041a5761047d565b81600080858152602001908152602001600020600001819055508060008085815260200190815260200160002060010181905550600160008085815260200190815260200160002060020160006101000a81548160ff0219169083151502179055505b505050565b600080600080848152602001908152602001600020600001546000808581526020019081526020016000206001015491509150915091565b60008083815260200190815260200160002060020160009054906101000a900460ff16156104fd5780600080848152602001908152602001600020600001819055505b505056fea2646970667358221220668938044fb298ae8aed35743470742a854dfadc2d02711e956a22d83038279b64736f6c63430006000033";

    protected ChainContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChainContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<TargetLogEventResponse> getTargetLogEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TargetLog", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TargetLogEventResponse> responses = new ArrayList<TargetLogEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TargetLogEventResponse typedResponse = new TargetLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.parentId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ccode = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.partCode = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.tcode = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.ttype = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tval = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TargetLogEventResponse> targetLogEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TargetLog", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TargetLogEventResponse>() {
            @Override
            public TargetLogEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TargetLogEventResponse typedResponse = new TargetLogEventResponse();
                typedResponse.log = log;
                typedResponse.parentId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ccode = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.partCode = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.tcode = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.ttype = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tval = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> ChainInfo(byte[] param0) {
        final Function function = new Function(
                "ChainInfo", 
                Arrays.<Type>asList(new Bytes32(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Create(byte[] ccode, BigInteger cstate, byte[] hashcode) {
        final Function function = new Function(
                "Create", 
                Arrays.<Type>asList(new Bytes32(ccode),
                new Uint256(cstate),
                new Bytes32(hashcode)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> GetInfo(byte[] ccode) {
        final Function function = new Function(
                "GetInfo", 
                Arrays.<Type>asList(new Bytes32(ccode)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> InsertTatget(byte[] parentId, byte[] ccode, byte[] partCode, byte[] tcode, byte[] ttype, BigInteger tval) {
        final Function function = new Function(
                "InsertTatget", 
                Arrays.<Type>asList(new Bytes32(parentId),
                new Bytes32(ccode),
                new Bytes32(partCode),
                new Bytes32(tcode),
                new Bytes32(ttype),
                new Uint256(tval)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> SetInfo(byte[] chaiID, byte[] chainInfo) {
        final Function function = new Function(
                "SetInfo", 
                Arrays.<Type>asList(new Bytes32(chaiID),
                new Bytes32(chainInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> UpdataInfo(byte[] chaiID, byte[] chainInfo) {
        final Function function = new Function(
                "UpdataInfo", 
                Arrays.<Type>asList(new Bytes32(chaiID),
                new Bytes32(chainInfo)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> UpdateState(byte[] ccode, BigInteger cstate) {
        final Function function = new Function(
                "UpdateState", 
                Arrays.<Type>asList(new Bytes32(ccode),
                new Uint256(cstate)),
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

        public byte[] parentId;

        public byte[] ccode;

        public byte[] partCode;

        public byte[] tcode;

        public byte[] ttype;

        public BigInteger tval;
    }
}