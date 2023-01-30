package feature.rpc.v0107;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import feature.rpc.mock.v107Impl;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.TransactionProof;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.web3j.utils.Numeric;

import java.util.ArrayList;

import static com.ckb.utils.HttpUtils.post;

public class verifyTransactionAndWitnessProof extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getTransactionAndWitnessProof.class);
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, false);

    //Todo wait sdk adjust ckb 0107
    @Test(retryAnalyzer = Retry.class)
    @Story("tx_proof")
    @Description("exist proofs,should return tx ")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestExistProofs() throws Exception{
        Block block = apiService.getBlock(Numeric.hexStringToByteArray("0xadaa049a601126abb71b08b4d7d522bd26ce50fe68ac75d7ebedd65b41ad8c1d"));
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block.transactions.get(0).hash));
        Response response = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block.header.hash)));
        Assert.assertEquals(getValueByJsonKey(response , "result", "block_hash"), Numeric.toHexString(block.header.hash));
    }

}
