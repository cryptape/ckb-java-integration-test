package com.ckb.listener;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

/**
 * @ClassNAME ITestinvokeListener
 * @Description TODO  对类 对方法的事件监听 方法和类执行之前 之后都可以监听
 * @Author tangyx
 * @Date 2022/7/25 2:52 PM
 * @Version 1.0
 */
public class ITestinvokeListener implements IInvokedMethodListener {
    //控制 事件监听是否开启
    private final Boolean policy=false;
    public static boolean testResultFailed =false;
    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(policy){
            synchronized (this){
                if(testResultFailed){
                    throw  new SkipException("测试停止运行");
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if(policy){
            String methodName=iTestResult.getMethod().getMethodName();
            boolean targetMethod=methodName.contains("query_wallet")|| methodName.contains("order_")|| methodName.contains("cancel_");
//            targetMethod=true;
            if(iInvokedMethod.isTestMethod() && !iTestResult.isSuccess()&&targetMethod){
                synchronized (this){
                    testResultFailed=true;
                }
            }
        }
    }
}
