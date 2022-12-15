package com.ckb.listener;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import java.util.Arrays;

public class ITestResultListener {

    @AfterMethod
    public void afterMethod(ITestResult iTestResult){
        System.out.printf("getMethodName: %s%n",iTestResult.getMethod().getMethodName());
        System.out.printf("getStatus: %s%n",iTestResult.getStatus());
        System.out.printf("getTestClass: %s%n",iTestResult.getTestClass());
        System.out.printf("getEndMillis: %s%n",iTestResult.getEndMillis());
        System.out.printf("getHost: %s%n",iTestResult.getHost());
        System.out.printf("getInstanceName: %s%n",iTestResult.getInstanceName());
        System.out.printf("getInstance: %s%n",iTestResult.getInstance());
        System.out.printf("getTestName: %s%n",iTestResult.getTestName());
        System.out.printf("getTestContext: %s%n",iTestResult.getTestContext().getOutputDirectory());
        System.out.println(Arrays.toString(iTestResult.getParameters()));
        System.out.printf("getAttributeNames: %s%n",iTestResult.getAttributeNames());
        System.out.println(iTestResult.getThrowable().getMessage());
    }
}
