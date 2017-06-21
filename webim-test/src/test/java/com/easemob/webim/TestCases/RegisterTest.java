package com.easemob.webim.TestCases;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.easemob.webim.CommonFunction.WebIMTestBase;
import com.google.common.base.Preconditions;
import com.easemob.webim.CommonFunction.DataReader;
import com.easemob.webim.CommonFunction.WebIMBaseListener;

@Listeners({ WebIMBaseListener.class })
public class RegisterTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(RegisterTest.class);
	private String register_name;
	private String password;
	private WebDriver driver;
	public DataReader dr;	
	

    
    
    @BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
		
		init("src/test/java/com/easemob/webim/TestData/RegisterTest.xml");
		
	    
	  } 

	
	@Test(dataProvider="Test_xml_dataprovider")
	public void register(Document params) throws Exception {
		
		driver=new ChromeDriver();
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		register_name=dr.readnodevalue(params, "register", "username")+getRandomStr(8);
		password=dr.readnodevalue(params, "register", "password");
		
		String nickname="nick_name";
		logger.info("generate random username: {}, password: {}, nickname: {}", register_name, password, nickname);
		driver.get(baseUrl);
		//driver.get("http://webim.easemob.com");
		
		//driver.manage().window().maximize();
		sleep(1);
		String xpath = "//*[@id='demo']/div/div/div[2]/p/i";
		WebElement reg = findElement(driver, xpath, path);
		reg.click();
		sleep(1);
		xpath = "//div[@class='webim-sign webim-signup']/input[@class='webim-input'][@type='text'][@placeholder='用户名']";
		WebElement ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(register_name);
		sleep(1);
		xpath = "//div[@class='webim-sign webim-signup']/input[@class='webim-input'][@type='password'][@placeholder='密码']";
		ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(password);
		sleep(1);
		xpath = "//div[@class='webim-sign webim-signup']/div/input[@class='webim-input'][@type='text'][@placeholder='昵称']";
		ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(nickname);
		sleep(1);
		logger.info("click ok button");
		xpath = "//div[@class='webim-sign webim-signup']/button[@class='webim-button bg-color']";
		ele = findElement(driver, xpath, path);
		ele.click();
		logger.info("check if register successfully!");
		super.login(driver, register_name, password, path, true);
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		 // RestAPI delete register user
        logger.info("Begin to delete register user!");
        deleteUserWithRest(register_name,token);
	}
	
	

}
