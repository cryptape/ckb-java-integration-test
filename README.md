# ckb_rpc_testing
## maven + testng
**例子**
- 由于sdk通常要等正式版发布之后, 才会发新版本，所以先打包javasdk的develop分支，导入到该集成测试项目做本地测试

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
