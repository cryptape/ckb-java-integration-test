package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.*;
import org.junit.Assert;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.BlockWithCycles;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class getBlockByNumberTest extends BeforeSuite {
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockNumber")
    @Description("with_cycles:null/false/true")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetBlockByNumber(long blockNumber,  Object withCycles, Object cycles)throws Exception{
        Block block = apiService.getBlockByNumber(blockNumber);
        Assert.assertNotNull(block.transactions);
    }

    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockNumber")
    @Description("with_cycles:null/false/true")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetBlockByNumberWithCycles(long blockNumber, Object withCycles, Object cycles)throws Exception{
        BlockWithCycles block = apiService.getBlockByNumber(blockNumber, (Boolean) withCycles);
        Assert.assertNotNull(block.block.transactions);
        System.out.println("+++" + block.cycles);
        Assert.assertEquals(block.cycles, cycles);
    }

    @DataProvider(name = "getBlockNumber")
    public Object[][] getBlockNumber() throws Exception{
        List<Long> array = new ArrayList();
        array.add(13017235L);
        array.add(4740158L);
        array.add(1505345L);
        List<Long> array2 = new ArrayList();
        return new Object[][]{
                {9124927, true, array},
                {9124927, false, null},
                {1000, true, array2},
                {1000, false, null},
        };
    }
}
