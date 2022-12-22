package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Cycles;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Transaction;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class estimateCyclesTest extends BeforeSuite {
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getTx")
    @Description("estimateCycles")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestEstimateCycles(Transaction tx)throws Exception{
        Cycles cycles = apiService.estimateCycles(tx);
        System.out.println(cycles.cycles);
        Assert.assertEquals(0, cycles.cycles);
    }

    @DataProvider(name = "getTx")
    public Object[][] getTx() throws IOException {
        Header header = apiService.getTipHeader();
        Block block = apiService.getBlockByNumber(header.number);
        Transaction tx = block.transactions.get(0);
        return new Object[][]{
                {tx},
        };
    }
}
