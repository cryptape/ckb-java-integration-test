package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.FeeRateStatics;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class getFeeRateStaticsTest extends BeforeSuite{
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getTarget")
    @Description("[],should return FeeRateResponse")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetTransaction(Integer target) throws Exception {
        FeeRateStatics feeRateStatics = apiService.getFeeRateStatics(target);
        if (feeRateStatics == null){
            //block中如果没有tx，则为null
        }else {
            Assert.assertNotNull(feeRateStatics);
            System.out.println("mean:" + feeRateStatics.mean);
            System.out.println("median:" + feeRateStatics.median);
        }
    }

    @DataProvider(name = "getTarget")
    public Object[][] getTarget(){
        return new Object[][]{
                {null},
                {0},
                {1},
                {101},
                {102}
        };
    }
}
