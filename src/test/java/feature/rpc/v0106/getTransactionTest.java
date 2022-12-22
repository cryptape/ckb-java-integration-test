package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.log4j.Logger;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.TransactionWithStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.lang.Long.toHexString;

public class getTransactionTest extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getTransactionTest.class);
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getExistTx")
    @Description("get tx ,should return include cycles")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetTransaction(byte[] hash) throws Exception {
        TransactionWithStatus transactionWithStatus = apiService.getTransaction(hash);
        System.out.println("0x" + toHexString(transactionWithStatus.cycles));
        Assert.assertNotNull(transactionWithStatus.cycles);
    }

    @DataProvider(name = "getExistTx")
    public Object[][] getTx() throws Exception {
        Header header = apiService.getTipHeader();
        Block block = apiService.getBlockByNumber(header.number);
        byte[] existTxHash = block.transactions.get(0).hash;
        return new Object[][]{
                {existTxHash},
        };
    }

}
