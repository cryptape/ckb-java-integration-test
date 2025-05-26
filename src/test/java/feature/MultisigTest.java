package feature;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.nervos.ckb.Network;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.sign.Context;
import org.nervos.ckb.sign.TransactionSigner;
import org.nervos.ckb.sign.TransactionWithScriptGroups;
import org.nervos.ckb.sign.signer.Secp256k1Blake160MultisigAllSigner;
import org.nervos.ckb.transaction.CkbTransactionBuilder;
import org.nervos.ckb.transaction.TransactionBuilderConfiguration;
import org.nervos.ckb.type.MultisigVersion;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.TransactionInput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.nervos.ckb.transaction.InputIterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Iterator;

public class MultisigTest extends BeforeSuite {
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, false);
    
    @Test(retryAnalyzer = Retry.class, dataProvider = "getMultisigVersions")
    @Description("测试多签交易 - 不同版本的多签脚本")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("multisig")
    public void testMultisigTransaction(MultisigVersion version, long amount) throws IOException {
        Network network = Network.TESTNET;
        Secp256k1Blake160MultisigAllSigner.MultisigScript multisigScript =
            new Secp256k1Blake160MultisigAllSigner.MultisigScript(0, 2,
                                                              "0x7336b0ba900684cb3cb00f0d46d4f64c0994a562",
                                                              "0x5724c1e3925a5206944d753a6f3edaedf977d77f");
        Script lock = new Script(
            version.codeHash(),
            multisigScript.computeHash(),
            version.hashType());
            
        // 生成发送地址
        String sender = new Address(lock, network).encode();

        TransactionBuilderConfiguration configuration = new TransactionBuilderConfiguration(network);
        Iterator<TransactionInput> iterator = new InputIterator(sender);
        TransactionWithScriptGroups txWithGroups = new CkbTransactionBuilder(configuration, iterator)
            .addOutput("ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r",
                       amount)
            .setChangeOutput(sender)
            .build(multisigScript);

        TransactionSigner signer = TransactionSigner.getInstance(network);
        signer.signTransaction(txWithGroups, new Context("0x4fd809631a6aa6e3bb378dd65eae5d71df895a82c91a615a1e8264741515c79c", multisigScript));
        signer.signTransaction(txWithGroups, new Context("0x7438f7b35c355e3d2fb9305167a31a72d22ddeafb80a21cc99ff6329d92e8087", multisigScript));

        byte[] txHash = apiService.testTxPoolAccept(txWithGroups.getTxView(), OutputsValidator.WELL_KNOWN_SCRIPTS_ONLY);
        String txHashHex = Numeric.toHexString(txHash);
        System.out.println("Transaction hash: " + txHashHex);
        
        // 验证交易哈希不为空
        Assert.assertNotNull("交易哈希不应为空", txHash);
        Assert.assertTrue("交易哈希长度应为66个字符", txHashHex.length() == 66);
    }

    @DataProvider(name = "getMultisigVersions")
    public Object[][] getMultisigVersions() {
        return new Object[][] {
            {MultisigVersion.Legacy, 50100000000L},
            {MultisigVersion.V2, 50100000001L}
        };
    }
}
