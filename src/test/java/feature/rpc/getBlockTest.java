package feature.rpc;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.nervos.ckb.type.Block;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Map;

import static com.ckb.utils.HttpUtils.post;


public class getBlockTest extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getBlockTest.class);
    public static final String url = CKB_MAINNET;
    @Test(dataProvider = "get_block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestGetBlock(Map<String, Object> body){
        logger.info("get_block(block_hash, verbosity, with_cycles)");
        Response response = post(url, getHeaders(), body);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertNull(getValueByJsonPath(response, "result"));
    }

    @DataProvider(name = "get_block")
    public Object[][] body(){
        Map<String, Object> requestBody = getBodyForJsonRpc();
        requestBody.put("method", "get_block");
        ArrayList<String> params = new ArrayList<>();
        params.add("0xa5f5c85987a15de25661e5a214f2c1449cd803f071acc7999820f25246471f40");
        requestBody.put("params", params);
        return new Object[][]{
                {requestBody},
        };
    }

    @Test(retryAnalyzer = Retry.class, enabled = false)
    public void TestGetBlockBySdk() throws Exception{
        byte[] blockHash = Numeric.hexStringToByteArray("0xa5f5c85987a15de25661e5a214f2c1449cd803f071acc7999820f25246471f40");
        Block block = getApi(url, false).getBlock(blockHash);
        Assert.assertNull(block);
    }
}
