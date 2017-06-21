package com.easemob.webim.TestCases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import com.easemob.webim.CommonFunction.DataReader;
import com.easemob.webim.CommonFunction.WebIMBaseListener;
import com.easemob.webim.CommonFunction.WebIMTestBase;

@Listeners({ WebIMBaseListener.class })
public class LoginTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);
	private String username;
	private String password;
	private WebDriver driver;
	public DataReader dr;
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		logger.info("Start to webim auto test on chrome...");

		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/LoginTest.xml");
	    resgisteUserWithRest("LoginData");
	  } 
	
	@Test(dataProvider="Test_xml_dataprovider")
	public void loginWebIM(Document params) {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		username=dr.readnodevalue(params, "login", "username");
		password=dr.readnodevalue(params, "login", "password");
		driver=new ChromeDriver();
		super.login(driver, username, password, path, true);
		super.logout(driver, path);
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
        
	}
	
}
