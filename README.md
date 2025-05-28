# ckb-java-integration-test

## Project Introduction

This is a CKB Java SDK integration testing project based on Maven + TestNG + Allure, used to verify the functionality and performance of the CKB Java SDK.

## Technology Stack

- Maven: Project building and dependency management
- TestNG: Testing framework
- Allure: Test report generation tool
- CKB Java SDK: Java development toolkit for Nervos CKB

## Steps to Verify SDK Temporary Branch Locally

### Preparation

1. Clone the test project
   ```bash
   git clone https://github.com/15168316096/ckb-java-integration-test.git -b newmultisign
   cd ckb-java-integration-test
   ```

2. Clone the SDK project as a dependency in the test project root directory
   ```bash
   git clone https://github.com/eval-exec/ckb-sdk-java.git -b exec/fix-multisig
   ```

3. Package ckb-sdk-java to generate jar file
   ```bash
   cd ckb-sdk-java
   ./gradlew clean :ckb:shadowJar
   cd ..
   ```

4. Confirm the dependency configuration in pom.xml is correct
   ```xml
   <dependency>
       <groupId>org.nervos.ckb</groupId>
       <artifactId>ckb</artifactId>
       <version>3.0.1</version>
       <scope>system</scope>
       <systemPath>${project.basedir}/ckb-sdk-java/ckb/build/libs/ckb-3.0.1-all.jar</systemPath>
   </dependency>
   ```

5. Compile the test project
   ```bash
   mvn clean compile
   ```

6. Run the tests
   ```bash
   mvn test
   ```

## Project Structure

```
ckb-java-integration-test/
├── src/
│   ├── main/java/com/ckb/
│   │   ├── base/         # Base classes and configurations
│   │   ├── cases/        # Test cases
│   │   ├── utils/        # Utility classes
│   ├── resources/        # Resource files
├── testng/               # TestNG configuration files
├── allure-results/       # Allure test results
├── pom.xml               # Maven configuration
└── README.md             # Project description
```

## Test Example

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
    //verbosity should not have been included initially. Different verbosity values return different types, can't be placed in the same method.
    return new Object[][] {
        {blockHash},
        //                {blockHash, null},
        //                {blockHash, true},
        //                {blockHash, false}
    };
}
```

## Generate Test Report

After completing the tests, you can use the following command to generate an Allure test report:

```bash
mvn allure:report
```

The report will be generated in the `target/site/allure-maven-plugin` directory.

## Common Issues

### 1. Dependency Issues

If you encounter dependency issues, you can try the following command to reload dependencies:

```bash
mvn clean install -U
```

### 2. Test Failures

If tests fail, please check:

- Whether the CKB node is running normally
- Whether the URLs and parameters in the configuration file are correct
- Whether the SDK version is compatible

## Contribution Guidelines

1. Fork the project
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Contact

If you have any questions, please submit them through GitHub Issues.
        