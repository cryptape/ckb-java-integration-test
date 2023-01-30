package feature.rpc.v0107;

import com.ckb.base.BeforeSuite;
import com.ckb.listener.Retry;
import feature.rpc.mock.v107Impl;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.nervos.ckb.service.Api;
import org.nervos.ckb.type.Block;
import org.nervos.ckb.type.Header;
import org.nervos.ckb.type.Transaction;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Collections;

import static com.ckb.utils.HttpUtils.post;

public class getTransactionAndWitnessProof extends BeforeSuite {
    public static org.apache.log4j.Logger logger = Logger.getLogger(getTransactionAndWitnessProof.class);
    public static final String url = CKB_DEVNET;
    public static final Api apiService = getApi(url, false);

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("a list of transactions in different orders，return same of proof")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashesWithDiffOrders() throws Exception {
        Block block = apiService.getBlock(Numeric.hexStringToByteArray("0xadaa049a601126abb71b08b4d7d522bd26ce50fe68ac75d7ebedd65b41ad8c1d"));
        ArrayList<String> arrayList = new ArrayList<>();
        for (Transaction tx: block.transactions) {
            arrayList.add(Numeric.toHexString(tx.hash));
        }
        Response sortProof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Collections.shuffle(arrayList);
        Response randomSortProof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonPath(sortProof , "block_hash"), getValueByJsonPath(randomSortProof , "block_hash"));
        Assert.assertEquals(getValueByJsonPath(sortProof , "transactions_proof"), getValueByJsonPath(randomSortProof , "transactions_proof"));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("tx hashes is empty,should return error:Empty transaction hashes")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashIsEmpty(){
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(new ArrayList<>(), null));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Empty transaction hashes");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("not exist txHashes,should return error: not yet in block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashIsNotExist(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0xc3cfa08597c5a381e271dc6a307017b8c37fda75b0a8434fd776d10a1a99f651");
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Transaction 0xc3cfa08597c5a381e271dc6a307017b8c37fda75b0a8434fd776d10a1a99f651 not yet in block");
    }

    //Todo verify proof is right
    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("not exist txHashes,should return error: not yet in block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashIsExist() throws Exception{
        Block block = apiService.getBlockByNumber(0);
        byte[] txHash = block.transactions.get(0).hash;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(txHash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonKey(proof , "result", "block_hash"), Numeric.toHexString(block.header.hash));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("exist hashes ,but not in same block,should return Not all transactions found in retrieved block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestHashIsExistAndNotInSameBlock() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        Block block2 = apiService.getBlockByNumber(2);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        arrayList.add(Numeric.toHexString(block2.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Not all transactions found in retrieved block");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("some exist hashes,some not exist hashes,should return ot yet in block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestHashIsExistNotAtAll() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        String randNotExistHash = "0x1f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f";
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        arrayList.add(randNotExistHash);
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Transaction 0x1f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c9f not yet in block");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("tx_hashes")
    @Description("dup hashes")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestHashIsDup() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertTrue(getValueByJsonKey(proof , "error", "message").contains("InvalidParams: Duplicated tx_hash"));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash")
    @Description("block_hash is null,should successfull")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestBlockHashIsNull() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, null));
        Assert.assertEquals(getValueByJsonKey(proof , "result", "block_hash"), Numeric.toHexString(block1.header.hash));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash")
    @Description("block is genesis,should successfull")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestBlockIsGenesis() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block1.header.hash)));
        Assert.assertEquals(getValueByJsonKey(proof , "result", "block_hash"), Numeric.toHexString(block1.header.hash));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash")
    @Description("block is tip number")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestBlockIsTipNumber() throws Exception{
        Header header = apiService.getTipHeader();
        Block block1 = apiService.getBlock(header.hash);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block1.header.hash)));
        Assert.assertEquals(getValueByJsonKey(proof , "result", "block_hash"), Numeric.toHexString(block1.header.hash));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash")
    @Description("block is not exist hash,should return Not all transactions found in specified block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestBlockIsNotExisit() throws Exception{
        Block block1 = apiService.getBlockByNumber(0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, "0x8f8c79eb6671709633fe6a46de93c0fedc9c1b8a6527a18d3983879542635c91"));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Not all transactions found in specified block");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash")
    @Description("only have cellbase tx")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestBlockIsOnlyExisitCellBase() throws Exception{
        Block block1 = apiService.getBlockByNumber(1);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block1.header.hash)));
        Assert.assertEquals(getValueByJsonKey(proof , "result", "block_hash"), Numeric.toHexString(block1.header.hash));
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash, tx_hashes")
    @Description("exist tx_hash exist block but tx_hashes not in block  should failed")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashIsNotInBlock() throws Exception{
        Block block1 = apiService.getBlockByNumber(1);
        Block block2 = apiService.getBlockByNumber(2);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Numeric.toHexString(block1.transactions.get(0).hash));
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block2.header.hash)));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Not all transactions found in specified block");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash, tx_hashes")
    @Description("not exist txHash ,exist block,should return not yet in block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashIsNotExistInBlock() throws Exception{
        Block block1 = apiService.getBlockByNumber(1);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0xfa0072347417d8f9cd328ad52ed71f993abff8923ee19cd50fc56782c7aedc41");
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, Numeric.toHexString(block1.header.hash)));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Transaction 0xfa0072347417d8f9cd328ad52ed71f993abff8923ee19cd50fc56782c7aedc41 not yet in block");
    }

    @Test(retryAnalyzer = Retry.class)
    @Story("block_hash, tx_hashes")
    @Description("not exist txHash and block,should return not yet in block")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("rpc")
    public void TestTxHashAndBlockIsNotExist() throws Exception{
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0xfa0072347417d8f9cd328ad52ed71f993abff8923ee19cd50fc56782c7aedc41");
        Response proof = post(url, getHeaders(), new v107Impl().get_transaction_and_witness_proof(arrayList, "0xfa0072347417d8f9cd328ad52ed71f993abff8923ee19cd50fc56782c7aedc42"));
        Assert.assertEquals(getValueByJsonKey(proof , "error", "message"), "InvalidParams: Transaction 0xfa0072347417d8f9cd328ad52ed71f993abff8923ee19cd50fc56782c7aedc41 not yet in block");
    }
}
