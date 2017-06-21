package com.easemob.webim.CommonFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class WebIMBaseListener implements ITestListener {
	
	private static final Logger logger = LoggerFactory.getLogger(WebIMBaseListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("**************Start Test: " + result.getMethod().getMethodName() + " **************");
 	}

	@Override
	public void onTestSuccess(ITestResult result) {
		logger.info("**************Success Test: " + result.getMethod().getMethodName() + " **************");		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		logger.info("**************Fail Test: " + result.getMethod().getMethodName() + " **************");		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		logger.info("**************Skip Test: " + result.getMethod().getMethodName() + " **************");		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		
		
	}

}
