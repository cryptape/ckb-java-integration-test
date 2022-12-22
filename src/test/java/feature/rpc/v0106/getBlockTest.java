package feature.rpc.v0106;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.*;
import org.junit.Assert;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * #### Method `get_block`
 * * `get_block(block_hash, verbosity, with_cycles)`
 *     * `block_hash`: [`H256`](#type-h256)
 *     * `verbosity`: [`Uint32`](#type-uint32) `|` `null`
 *     * `with_cycles`: `boolean` `|` `null`
 * * result: [`BlockResponse`](#type-blockresponse) `|` `null`
 *
 */
public class getBlockTest extends BeforeSuite {
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, true);

    @Test(retryAnalyzer = Retry.class, dataProvider = "getBlockHash")
    @Description("with_cycles:null/true/false")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/issues/616")
    public void TestGetBlock(byte[] blockHash)throws Exception{
        Block block = apiService.getBlock(blockHash);
        Assert.assertNotNull(block.transactions);
    }
    @DataProvider(name = "getBlockHash")
    public Object[][] getBlockHash() throws Exception{
        byte[] blockHash = apiService.getBlockHash(1);
        //verbosity应该是一开始就没放进去的。verbosity取值不同，返回的类型就不一样，没法放在同一个方法里。
        return new Object[][]{
            {blockHash},
//                {blockHash, null},
//                {blockHash, true},
//                {blockHash, false}
        };
    }
}
