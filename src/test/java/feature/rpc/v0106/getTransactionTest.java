package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.log4j.Logger;
import org.nervos.ckb.Network;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.DaoTransactionBuilder;
import org.nervos.ckb.transaction.InputIterator;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.transaction.handler.DaoScriptHandler;
import org.nervos.ckb.type.*;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Iterator;
public class getTransactionTest extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getTransactionTest.class);
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);
    Network network = Network.TESTNET;
    String sender = "ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsqtu9h6zuqvx2t0k5wk5fgm3vn4974c36qsckagd4";

    @Test(retryAnalyzer = Retry.class, dataProvider = "getCellBaseTxHash")
    @Description("get cellbase tx, should return include cycles and cycles == null")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetCellBaseTransaction(byte[] hash) throws Exception {
        TransactionWithStatus transactionWithStatus = apiService.getTransaction(hash);
        System.out.println(transactionWithStatus.cycles);
        Assert.assertNull(transactionWithStatus.cycles);
    }

    @Test(retryAnalyzer = Retry.class, dataProvider = "getDaoDepositTxHash")
    @Description("get DaoDepositTx, should return include cycles and cycles != null")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetDaoDepositTransaction(byte[] hash) throws Exception {
        TransactionWithStatus transactionWithStatus = apiService.getTransaction(hash);
        System.out.println(transactionWithStatus.cycles);
        Assert.assertNotNull(transactionWithStatus.cycles);
    }

    @Test(retryAnalyzer = Retry.class, dataProvider = "getDaoWithdrawTxHash")
    @Description("get DaoWithdrawTx, should return include cycles and cycles != null")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetDaoWithdrawTransaction(byte[] hash) throws Exception {
        TransactionWithStatus transactionWithStatus = apiService.getTransaction(hash);
        System.out.println(transactionWithStatus.cycles);
        Assert.assertNotNull(transactionWithStatus.cycles);
    }

    @Test(retryAnalyzer = Retry.class, dataProvider = "getNotCellBaseTxHash")
    @Description("send ckb tx, get not cellbase tx, should return include cycles and cycles != null")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetNotCellBaseTransaction(byte[] hash) throws Exception {
        TransactionWithStatus transactionWithStatus = apiService.getTransaction(hash);
        System.out.println(transactionWithStatus.cycles);
        Assert.assertNotNull(transactionWithStatus.cycles);
    }

    @DataProvider(name = "getCellBaseTxHash")
    public Object[][] getCellBaseTx() throws Exception {
        Header header = apiService.getTipHeader();
        Block block = apiService.getBlockByNumber(header.number);
        byte[] cellBaseTxHash = block.transactions.get(0).hash;
        return new Object[][]{
                {cellBaseTxHash}
        };
    }

    @DataProvider(name = "getDaoWithdrawTxHash")
    public Object[][] getDaoWithdrawTx() throws Exception {
        TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
        Iterator<TransactionInput> iterator = new InputIterator(sender);
        TransactionWithScriptGroups txWithGroupsDepositOutput = new CkbTransactionBuilder(configuration, iterator)
                .addDaoDepositOutput(sender, 51000000000L)
                .setChangeOutput(sender)
                .build();

        TransactionSigner.getInstance(network)
                .signTransaction(txWithGroupsDepositOutput, "0xf332e1f5bdf5d9d7ddcbde404bc2aa7a09e1f508fe17949ada2d43ee22bb3428");
        byte[] DaoDepositHash = apiService.sendTransaction(txWithGroupsDepositOutput.getTxView());
        Thread.sleep(60000);
        System.out.println("Transaction hash: " + Numeric.toHexString(DaoDepositHash));
        OutPoint depositOutPoint = new OutPoint(DaoDepositHash, 0);
        Iterator<TransactionInput> iterator2 = new InputIterator(sender);
        TransactionWithScriptGroups txWithGroupsWithdrawOutput = new DaoTransactionBuilder(configuration, iterator2, depositOutPoint, apiService)
                .addWithdrawOutput(sender)
                .setChangeOutput(sender)
                .build(new DaoScriptHandler.WithdrawInfo(apiService, depositOutPoint));
        TransactionSigner.getInstance(network)
                .signTransaction(txWithGroupsWithdrawOutput, "0xf332e1f5bdf5d9d7ddcbde404bc2aa7a09e1f508fe17949ada2d43ee22bb3428");

        // Send transaction
        byte[] DaoWithdrawTxHash =apiService.sendTransaction(txWithGroupsWithdrawOutput.getTxView());
        Thread.sleep(60000);
        System.out.println("Transaction hash: " + Numeric.toHexString(DaoWithdrawTxHash));
        return new Object[][]{
                {DaoWithdrawTxHash}
        };
    }

    @DataProvider(name = "getDaoDepositTxHash")
    public Object[][] getDaoDepositTx() throws Exception {
        TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
        Iterator<TransactionInput> iterator = new InputIterator(sender);
        TransactionWithScriptGroups txWithGroupsDepositOutput = new CkbTransactionBuilder(configuration, iterator)
                .addDaoDepositOutput(sender, 51000000000L)
                .setChangeOutput(sender)
                .build();

        TransactionSigner.getInstance(network)
                .signTransaction(txWithGroupsDepositOutput, "0xf332e1f5bdf5d9d7ddcbde404bc2aa7a09e1f508fe17949ada2d43ee22bb3428");
        byte[] DaoDepositTxHash = apiService.sendTransaction(txWithGroupsDepositOutput.getTxView());
        Thread.sleep(60000);
        System.out.println("Transaction hash: " + Numeric.toHexString(DaoDepositTxHash));
        return new Object[][]{
                {DaoDepositTxHash}
        };
    }

    @DataProvider(name = "getNotCellBaseTxHash")
    public Object[][] getNotCellBaseTxHashTx() throws Exception {
        TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
        Iterator<TransactionInput> iterator = new InputIterator(sender);
        TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
                .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                        50100000000L)
                .setChangeOutput(sender)
                .build();

        TransactionSigner.getInstance(network)
                .signTransaction(txWithGroups, "0xf332e1f5bdf5d9d7ddcbde404bc2aa7a09e1f508fe17949ada2d43ee22bb3428");
        byte[] sendCkbTxHash = apiService.sendTransaction(txWithGroups.getTxView());
        System.out.println("Transaction hash: " + Numeric.toHexString(sendCkbTxHash));
        return new Object[][]{
                {sendCkbTxHash}
        };
    }
}
