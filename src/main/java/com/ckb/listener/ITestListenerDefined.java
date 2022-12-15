package com.ckb.listener;

import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * @ClassNAME ITestListener
 * @Description TODO 测试方法监听
 * @Author tangyx
 * @Date 2022/7/25 7:29 PM
 * @Version 1.0
 */
public class ITestListenerDefined implements org.testng.ITestListener {
    @Override
    public void onTestStart(ITestResult iTestResult) {
        //测试方法开始 执行 方法test注解的方法
        System.out.println(iTestResult.getMethod().getMethodName());
        System.out.println(iTestResult.getTestName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
