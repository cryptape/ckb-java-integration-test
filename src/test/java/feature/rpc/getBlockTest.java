package feature.rpc;

import com.ckb.base.BeforeSuite;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static com.ckb.utils.HttpUtils.post;


public class getBlockTest extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getBlockTest.class);
    public static final String url = CKB_MAINNET;
    @Test(dataProvider = "get_block")
    @Description("get_block(block_hash, verbosity, with_cycles)")
    public void TestGetBlock(Map<String, Object> body){
        logger.info("begin...");
        Response response = post(url, getHeaders(), body);
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("result:" + getValueByJsonPath(response, "result"));
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

}
