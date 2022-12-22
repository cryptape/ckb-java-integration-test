package com.ckb.listener;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Iterator;

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
    public void onFinish(ITestContext context) {
        Iterator<ITestResult> listOfFailedTests=context.getFailedTests().getAllResults().iterator();
        while(listOfFailedTests.hasNext())
        {
            ITestResult failedTest=listOfFailedTests.next();
            ITestNGMethod method=failedTest.getMethod();
            if(context.getFailedTests().getResults(method).size()>1)
            {
                listOfFailedTests.remove();
            }else
            {
                if(context.getPassedTests().getResults(method).size()>0)
                {
                    listOfFailedTests.remove();
                }
            }
        }
    }

}
