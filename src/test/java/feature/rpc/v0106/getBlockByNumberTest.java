package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.*;
import org.junit.Assert;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class getBlockByNumberTest extends BeforeSuite {
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockNumber")
    @Description("with_cycles:null/false/true")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetBlockByNumber(long blockNumber)throws Exception{
        Block block = apiService.getBlockByNumber(blockNumber);
        Assert.assertNotNull(block.transactions);
    }

    @DataProvider(name = "getBlockNumber")
    public Object[][] getBlockNumber() throws Exception{
        return new Object[][]{
                {1},
//                {1, null},
//                {1, true},
//                {1, false}
        };
    }
}
