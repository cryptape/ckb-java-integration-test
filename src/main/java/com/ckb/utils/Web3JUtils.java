package com.ckb.utils;

import com.ckb.base.BeforeSuite;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
public class Web3JUtils extends BeforeSuite {

    private static Web3j web3j = null;

    private static synchronized Web3j init(String rpcUrl){
        if (!ObjectUtils.isEmpty(web3j)) {
            return web3j;
        }
        // 实例化web3j
        web3j = Web3j.build(new HttpService(rpcUrl));
        return web3j;
    }

    public static Web3j getWeb3j(String rpcUrl) {
        //获取web3
        if(!ObjectUtils.isEmpty(web3j)) {
            return web3j;
        }
        return init(rpcUrl);
    }
}
