package com.ckb.common;

import com.ckb.base.BeforeSuite;
import com.ckb.utils.Web3JUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;

public class Web3JComm extends BeforeSuite {
    public static Web3j client = Web3JUtils.getWeb3j(GW_TESTNET_V1);

    public static BigDecimal ConverBigDecimal(String value) {
        return new BigDecimal(value).divide(new BigDecimal(10).pow(18), 18,  RoundingMode.HALF_UP);
    }

    public static BigDecimal getChain_Balance(String address) throws Exception {
            EthGetBalance ethGetBalance = client.ethGetBalance(
                    address,
                    DefaultBlockParameterName.fromString(DefaultBlockParameterName.LATEST.name())
            ).sendAsync().get();
            BigDecimal bigDecimal = new BigDecimal(ethGetBalance.getBalance());
            return ConverBigDecimal(bigDecimal.toString());
    }

    public static String signSendTxForkEth(String from, String to, String value, String priKey) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = client
                .ethGetTransactionCount(from, DefaultBlockParameterName.PENDING)
                .sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction etherTransaction = RawTransaction.createEtherTransaction(
                nonce,
                client.ethGasPrice().sendAsync().get().getGasPrice(),
                DefaultGasProvider.GAS_LIMIT,
                to,
                Convert.toWei(value, Convert.Unit.ETHER).toBigInteger()
        );
        Credentials credentials = Credentials.create(priKey);
        long chainId = Long.parseLong(client.netVersion().send().getNetVersion());
        System.out.println("chainId:" + chainId);
        byte[] signature = TransactionEncoder.signMessage(etherTransaction, chainId, credentials);
        String signatureHexValue = Numeric.toHexString(signature);
        System.out.println("本次交易结构体生成的Hex内容:" + signatureHexValue);
        EthSendTransaction result = client.ethSendRawTransaction(signatureHexValue).send();
        System.out.println("本次生成的交易hash:" + result.getResult());
        if (result.getResult() != null) {
            System.out.println("交易成功");
        } else {
            System.out.println("交易失败");
        }
        return result.getResult();
    }

    public BigInteger getTxAmountByHash(String hash) throws Exception {
        Optional<Transaction> transactions = client.ethGetTransactionByHash(hash).sendAsync().get().getTransaction();
        for (int i = 0; i < 10; i++) {
            if (transactions.isPresent()){
                return transactions.get().getValue();
            }
        }
        return null;
    }

    public BigInteger getBlockHeightByHash(String hash) throws Exception {
        Optional<Transaction> transactions = client.ethGetTransactionByHash(hash).sendAsync().get().getTransaction();
        for (int i = 0; i < 10; i++) {
            if (transactions.isPresent()){
                return transactions.get().getBlockNumber();
            }
        }
        return null;
    }

    public BigInteger getGasFeeByHash(String hash) throws Exception {
        Optional<Transaction> transactions = client.ethGetTransactionByHash(hash).sendAsync().get().getTransaction();
        for (int i = 0; i < 10; i++) {
            if (transactions.isPresent()){
                return transactions.get().getGas();
            }
        }
        return null;

    }

    public static void main(String[] args) throws Exception {
        //获取链上实际余额
        System.out.println(getChain_Balance("0xaA453eA6AeAb66B5798Af85312e4a9Dd5D957e15"));
    }

}
