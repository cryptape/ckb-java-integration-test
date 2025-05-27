# ckb-java-integration-test

## 项目介绍

这是一个基于Maven + TestNG + Allure的CKB Java SDK集成测试项目，用于验证CKB Java SDK的功能和性能。

## 技术栈

- Maven：项目构建和依赖管理
- TestNG：测试框架
- Allure：测试报告生成工具
- CKB Java SDK：Nervos CKB的Java开发工具包

## 本地验证SDK临时分支步骤

### 准备工作

1. 克隆测试项目
   ```bash
   git clone https://github.com/15168316096/ckb-java-integration-test.git -b newmultisign
   cd ckb-java-integration-test
   ```

2. 在测试项目根目录克隆SDK项目作为依赖
   ```bash
   git clone https://github.com/eval-exec/ckb-sdk-java.git -b exec/fix-multisig
   ```

3. 打包ckb-sdk-java生成jar包
   ```bash
   cd ckb-sdk-java
   ./gradlew clean :ckb:shadowJar
   cd ..
   ```

4. 确认pom.xml中的依赖配置正确
   ```xml
   <dependency>
       <groupId>org.nervos.ckb</groupId>
       <artifactId>ckb</artifactId>
       <version>3.0.1</version>
       <scope>system</scope>
       <systemPath>${project.basedir}/ckb-sdk-java/ckb/build/libs/ckb-3.0.1-all.jar</systemPath>
   </dependency>
   ```

5. 编译测试项目
   ```bash
   mvn clean compile
   ```

6. 运行测试
   ```bash
   mvn test
   ```

## 项目结构

```
ckb-java-integration-test/
├── src/
│   ├── main/java/com/ckb/
│   │   ├── base/         # 基础类和配置
│   │   ├── cases/        # 测试用例
│   │   ├── utils/        # 工具类
│   ├── resources/        # 资源文件
├── testng/               # TestNG配置文件
├── allure-results/       # Allure测试结果
├── pom.xml               # Maven配置
└── README.md             # 项目说明
```

## 测试示例

```java
@Test(retryAnalyzer = Retry.class, dataProvider = "getBlockHash")
@Description("with_cycles:null/true/false")
@Severity(SeverityLevel.BLOCKER)
@Feature("rpc")
@Issue("https://github.com/nervosnetwork/ckb-sdk-java/issues/616")
public void TestGetBlock(byte[] blockHash) throws Exception {
    Block block = apiService.getBlock(blockHash);
    Assert.assertNotNull(block.transactions);
}

@DataProvider(name = "getBlockHash")
public Object[][] getBlockHash() throws Exception {
    byte[] blockHash = apiService.getBlockHash(1);
    //verbosity应该是一开始就没放进去的。verbosity取值不同，返回的类型就不一样，没法放在同一个方法里。
    return new Object[][] {
        {blockHash},
        //                {blockHash, null},
        //                {blockHash, true},
        //                {blockHash, false}
    };
}
```

## 生成测试报告

完成测试后，可以使用以下命令生成Allure测试报告：

```bash
mvn allure:report
```

报告将生成在`target/site/allure-maven-plugin`目录下。

## 常见问题

### 1. 依赖问题

如果遇到依赖问题，可以尝试以下命令重新加载依赖：

```bash
mvn clean install -U
```

### 2. 测试失败

如果测试失败，请检查：

- CKB节点是否正常运行
- 配置文件中的URL和参数是否正确
- SDK版本是否兼容

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 LICENSE 文件

## 联系方式

如有问题，请通过GitHub Issues提交。