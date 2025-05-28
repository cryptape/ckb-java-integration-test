package feature.rpc.mocking;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ckb.base.BeforeSuite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.GsonFactory;
import org.nervos.ckb.type.Cycles;
import org.nervos.ckb.type.Transaction;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.ckb.utils.HttpUtils.getByQueryParams;


public class MockingTest extends BeforeSuite {
    @Test(dataProvider= "getBody", enabled=false)
    public void TestDemo(Map<String, Object> body) throws IOException {
        String MockUrl = CKB_RPC_MOCK + "/" + body.get("method") + "/" + body.get("filename");
        Api apiService = getApi(MockUrl, true);
        Response response = getByQueryParams(MockUrl, getHeaders(), null);
        JsonPath jsonPath = response.jsonPath();
        Map<String, Object> params = jsonPath.get("request.params[0]");
        Map<String, Object> params2 = jsonPath.get("response.result");
        Gson gson = GsonFactory.create();
        Object cyclesValue = params2.get("cycles");
        if (cyclesValue != null) {
            String cyclesString = cyclesValue.toString();
            System.out.println("Cycles: " + cyclesString);
            long cyclesLong = Long.parseLong(cyclesString.substring(2), 16);
            String jsonString = gson.toJson(params);
            System.out.println("[tx debug]:\n" + jsonString);
            Transaction tx = gson.fromJson(jsonString, Transaction.class);
            Cycles cycles = apiService.estimateCycles(tx);
            Assert.assertEquals(cyclesLong, cycles.cycles);
        } else {
            System.out.println("Cycles not found in params2");
        }
    }

    @DataProvider(name = "getBody")
    public Object[][] getBody() {
        Map<String, Object> requestBody = getBodyForJsonRpc();
        requestBody.put("method", "estimate_cycles");
        requestBody.put("filename", "[tx]");
        return new Object[][]{
                {requestBody},
        };
    }
}

