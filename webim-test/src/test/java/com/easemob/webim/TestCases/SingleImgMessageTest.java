
package com.easemob.webim.TestCases;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import com.easemob.webim.CommonFunction.DataReader;
import com.easemob.webim.CommonFunction.WebIMBaseListener;
import com.easemob.webim.CommonFunction.WebIMTestBase;
@Listeners({ WebIMBaseListener.class })
public class SingleImgMessageTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(SingleImgMessageTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driver2;
	private String img="dog.jpg";
	private WebDriver driver;
	public DataReader dr;
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/SingleImgMessageTest.xml");
	    resgisteUserWithRest("SingleImgMessageData");
	  } 
	
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void sendOnLineImg(Document params) {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver=new ChromeDriver();
		driver2=new ChromeDriver();
		username=dr.readnodevalue(params, "sendimgmessage", "username");
		password=dr.readnodevalue(params, "sendimgmessage", "password");
		friend=dr.readnodevalue(params, "sendimgmessage", "friend");
		addFriendWithRest(username,friend,token);
		super.login(driver2, friend, password, path, true);
		super.login(driver, username, password, path, true);
		logger.info("find special friend: {}", friend);
		WebElement ele = findSpecialFriend(driver, friend, path);
		cleanSingleMessage(driver,username,friend,path);
		sleep(3);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		getExeFile("img.exe");
			
		logger.info("talk to friend: {} with img message: {}", friend);
		sleep(3);
		checkChatImg(driver, username, friend, img, path);
			
	}
	@Test(enabled = true, dependsOnMethods = { "sendOnLineImg" })
	public void receiveOnLineImg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatImg(driver2, friend, username, img, path);
		isReadMsg(driver, username, friend, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}
	
	@Test(enabled = true,dependsOnMethods = { "receiveOnLineImg" })
	public void sendOffLineImg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);		
		logger.info("find special friend: {}", friend);
		WebElement ele = findSpecialFriend(driver, friend, path);
		sleep(3);
		cleanSingleMessage(driver,username,friend,path);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		getExeFile("img.exe");
		logger.info("talk to friend: {} with img message: {}", friend);
		sleep(3);
		checkChatImg(driver, username, friend, img, path);
			
	}
	@Test(enabled = true, dependsOnMethods = { "sendOffLineImg" })
	public void receiveOffLineImg() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver2=new ChromeDriver();
		super.login(driver2, friend, password, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatImg(driver2, friend, username, img, path);
		//begin clean chatmessage
		isReadMsg(driver, username, friend, path);
		
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		quitDriver(driver2);
        
	}

}