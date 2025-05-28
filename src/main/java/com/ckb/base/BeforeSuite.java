package com.ckb.base;

import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.service.RpcService;

import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.ckb.utils.RandomUtils.getRandom;

public class BeforeSuite {
    /**
     * 环境地址
     */
    public static String INDEXER_URL;
    public static String MERCURY_URL;
    public static String CKB_URL;
    public static String CKB_MAINNET;
    public static String CKB_TESTNET;

    public static String CKB_DEVNET;

    public static String GW_DEVNET_V1;

    public static String GW_ALPHANET_V1;

    public static String LOCAL_ETH;

    public static String GW_TESTNET_V1;


    public static String CKB_RPC_MOCK;

    //获取配置文件内的参数
    static {
        ResourceBundle resource = ResourceBundle.getBundle("service_node");
        CKB_MAINNET = resource.getString("CKB_MAINNET");
        CKB_TESTNET = resource.getString("CKB_TESTNET");
        GW_DEVNET_V1 = resource.getString("GW_DEVNET_V1");
        GW_ALPHANET_V1 = resource.getString("GW_ALPHANET_V1");
        LOCAL_ETH = resource.getString("LOCAL_ETH");
        GW_TESTNET_V1 = resource.getString("GW_TESTNET_V1");
        CKB_URL = resource.getString("CKB_URL");
        INDEXER_URL = resource.getString("INDEXER_URL");
        MERCURY_URL = resource.getString("MERCURY_URL");
        CKB_DEVNET = resource.getString("CKB_DEVNET");
        CKB_RPC_MOCK = resource.getString("CKB_RPC_MOCK");
    }
    public static Api getApi(String rpcUrl, boolean isDebug){
        return new Api(new RpcService(rpcUrl, isDebug));
    }
    public Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<>(6);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public Map<String, Object> getBodyForJsonRpc() {
        Map<String, Object> body = new HashMap<>(6);
        body.put("jsonrpc", "2.0");
        body.put("id", getRandom());
        return body;
    }

    /**
     * 通过JsonPath路径，获取接口返回的特定值
     *
     * @param response 接口返回提体
     * @param jsonPath 目标参数的jsonPath路径
     * @return 目标参数（String）
     */
    public static String getValueByJsonPath(Response response, String jsonPath) {
        if (response.getBody().path(jsonPath) != null){
            return response.getBody().path(jsonPath).toString();
        }else return null;

    }

    /**
     *
     * @param response REST-assured json响应
     * @param bodyKey 子层body的key值
     * @param goalKey 目标value对应的key值
     * @return 目标value
     */
    public static String getValueByJsonKey(Response response, String bodyKey, String goalKey) {
        JSONObject resp = JSONObject.parseObject(response.asString());
        return resp.getJSONObject(bodyKey).getString(goalKey);
    }
}
