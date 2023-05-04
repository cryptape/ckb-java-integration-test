package feature.rpc.v0108;

import com.ckb.listener.Retry;
import io.qameta.allure.*;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.ScriptType;
import org.nervos.ckb.utils.Numeric;
import org.nervos.indexer.model.Order;
import org.nervos.indexer.model.ScriptSearchMode;
import org.nervos.indexer.model.SearchKeyBuilder;
import org.nervos.indexer.model.resp.TxsWithCell;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.ckb.base.BeforeSuite.CKB_DEVNET;
import static com.ckb.base.BeforeSuite.getApi;

public class indexerSearchModeTest {

    public static final String url = CKB_DEVNET;
    public static final Api api = getApi(url, true);

    @Test(retryAnalyzer = Retry.class)
    @Description("indexer SearchMode")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    @Issue("https://github.com/nervosnetwork/ckb-sdk-java/pull/624/files")
    public void testGetTransactions_prefix_partial() throws IOException {
        SearchKeyBuilder key = new SearchKeyBuilder();
        key.script(
                new Script(
                        Numeric.hexStringToByteArray(
                                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                        Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae".substring(0, 4)),
                        Script.HashType.TYPE));
        key.scriptType(ScriptType.LOCK);
        key.scriptSearchMode(ScriptSearchMode.Prefix);
        TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
        Assert.assertTrue(txs.objects.size() > 0);
    }

    @Test
    public void testGetTransactions_exact_partial() throws IOException {
        SearchKeyBuilder key = new SearchKeyBuilder();
        key.script(
                new Script(
                        Numeric.hexStringToByteArray(
                                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                        Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae".substring(0, 4)),
                        Script.HashType.TYPE));
        key.scriptType(ScriptType.LOCK).scriptSearchMode(ScriptSearchMode.Exact);
        TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
        Assert.assertEquals(0, txs.objects.size());
    }

    @Test
    public void testGetTransactions_exact_full() throws IOException {
        SearchKeyBuilder key = new SearchKeyBuilder();
        key.script(
                new Script(
                        Numeric.hexStringToByteArray(
                                "0x58c5f491aba6d61678b7cf7edf4910b1f5e00ec0cde2f42e0abb4fd9aff25a63"),
                        Numeric.hexStringToByteArray("0xe53f35ccf63bb37a3bb0ac3b7f89808077a78eae"),
                        Script.HashType.TYPE));
        key.scriptType(ScriptType.LOCK).scriptSearchMode(ScriptSearchMode.Exact);
        TxsWithCell txs = api.getTransactions(key.build(), Order.ASC, 10, null);
        Assert.assertTrue(txs.objects.size() > 0);
    }
}
