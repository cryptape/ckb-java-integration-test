package feature.rpc.v0108;

import com.ckb.listener.Retry;
import io.qameta.allure.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.BlockWithCycles;
import org.nervos.ckb.type.PackedBlockWithCycles;
import org.nervos.ckb.type.PackedHeader;
import org.nervos.ckb.type.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.ckb.base.BeforeSuite.CKB_DEVNET;
import static com.ckb.base.BeforeSuite.getApi;

public class packedRpcTest {
    public static final String url = CKB_DEVNET;
    public static final Api api = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockNumber")
    @Description("with_cycles:null/true/false")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void TestGetBlockByNumberWithCycles(long blockNumber) throws Exception {
        BlockWithCycles response = api.getBlockByNumber(blockNumber, true);
        Assert.assertEquals(response.cycles.size() + 1, response.block.transactions.size());
        Assert.assertTrue(response.cycles.size() > 0);

        BlockWithCycles response0 = api.getBlockByNumber(blockNumber, false);
        Assert.assertEquals(response0.block.pack().toByteArray(), response.block.pack().toByteArray());
        Assert.assertNull(response0.cycles);

        PackedBlockWithCycles packedResponse = api.getPackedBlockByNumber(blockNumber, true);
        Assert.assertEquals(Numeric.toHexString(packedResponse.getBlockBytes()), Numeric.toHexString(response.block.pack().toByteArray()));
        Assert.assertEquals(packedResponse.getBlockBytes(), response.block.pack().toByteArray());
        Assert.assertEquals(response.cycles, packedResponse.cycles);

        PackedBlockWithCycles packedResponse0 = api.getPackedBlockByNumber(blockNumber, false);
        Assert.assertEquals(packedResponse.getBlockBytes(), packedResponse0.getBlockBytes());
        Assert.assertNull(packedResponse0.cycles);
    }


    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockNumberNotExist")
    @Description("with_cycles:null/true/false")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void TestGetBlockByNumberWithCycles_NotExist(long blockNumber) throws Exception {
        BlockWithCycles response = api.getBlockByNumber(blockNumber, true);
        Assert.assertNull(response);
        BlockWithCycles response0 = api.getBlockByNumber(blockNumber, false);
        Assert.assertNull(response0);

        PackedBlockWithCycles packedResponse = api.getPackedBlockByNumber(blockNumber, true);
        Assert.assertNull(packedResponse);

        PackedBlockWithCycles packedResponse0 = api.getPackedBlockByNumber(blockNumber, false);
        Assert.assertNull(packedResponse0);
    }

    @DataProvider(name = "getBlockNumberNotExist")
    public Object[][] getBlockNumberNotExist() {
        return new Object[][]{
                {0xffffffffffffffffL}
        };
    }

    @DataProvider(name = "getBlockNumber")
    public Object[][] getBlockNumber() {
        return new Object[][]{
                {7981482}
        };
    }

    @Test(retryAnalyzer = Retry.class)
    @Description("packed rpc")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void testGetPackedTipHeader() throws IOException {
        PackedHeader tipHeader = api.getPackedTipHeader();
        byte[] headerHash = tipHeader.calculateHash();
        PackedHeader packedHeader = api.getPackedHeader(headerHash);
        Assert.assertEquals(tipHeader.header, packedHeader.header);
    }

    @Test(retryAnalyzer = Retry.class)
    @Description("packed rpc")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void testPackedTransaction() throws IOException {
        byte[] transactionHash =
                Numeric.hexStringToByteArray(
                        "0x8277d74d33850581f8d843613ded0c2a1722dec0e87e748f45c115dfb14210f1");
        byte[] transaction_bytes = api.getPackedTransaction(transactionHash).getTransactionBytes();

        Transaction transaction = api.getTransaction(transactionHash).transaction;
        byte[] bytes_from_json = transaction.pack().toByteArray();
        Assert.assertEquals(bytes_from_json, transaction_bytes);
    }

    @Test(retryAnalyzer = Retry.class)
    @Description("Numeric test")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void testIsIntegerValue2() {
        String littleEndian = Numeric.littleEndian(71);
        Assert.assertEquals("0x4700000000000000", littleEndian);

        String negLittleEndian = Numeric.littleEndian(-1);
        Assert.assertEquals("0xffffffffffffffff", negLittleEndian);
        String negLittleEndian2 = Numeric.littleEndian(-2);
        Assert.assertEquals("0xfeffffffffffffff", negLittleEndian2);

        long v = 0x7eadbeef;
        String negBeef = Numeric.littleEndian(-v);
        // 7e => 81, ad => 52, be => 41, ef=> 11
        Assert.assertEquals("0x11415281ffffffff", negBeef);
    }
}
