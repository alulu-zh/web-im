package com.easemob.webim.TestCases;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.lang3.StringUtils;
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
public class SingleMessageTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(SingleMessageTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driver2;
	private String msg;
	private WebDriver driver;
	public DataReader dr;
	private String img="dog.jpg";
	private String audio="audio.amr";
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/SingleMessageTest.xml");
	    resgisteUserWithRest("SingleMessageData");
	  } 
	
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void sendOnLineMsg(Document params) {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver=new ChromeDriver();
		driver2=new ChromeDriver();
		username=dr.readnodevalue(params, "sendmessage", "username");
		password=dr.readnodevalue(params, "sendmessage", "password");
		friend=dr.readnodevalue(params, "sendmessage", "friend");
		addFriendWithRest(username,friend,token);
		super.login(driver2, friend, password, path, true);
		super.login(driver, username, password, path, true);
		logger.info("find special friend: {}", friend);
		WebElement ele = findSpecialFriend(driver, friend, path);
		sleep(3);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
		ele = findElement(driver, xpath, path);
		cleanSingleMessage(driver,username,friend,path);
		msg = getRandomStr(10);
		logger.info("talk to friend: {} with message: {}", friend, msg);
		ele.sendKeys(msg);
		//ele.sendKeys("C:\\Users\\gaozhq\\Pictures\\Camera Roll\\re.jpg");
		logger.info("send msg");
		xpath ="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		checkChatMsg(driver, username, friend, msg, path);
		sleep(3);
		logger.info("find img text area");
		xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		getExeFile("img.exe");
			
		logger.info("talk to friend: {} with img message: {}", friend);
		sleep(3);
		checkChatImg(driver, username, friend, img, path);
		sleep(3);
		logger.info("find message text area");
		xpath = "//div[@class='webim-chatwindow-options']/span[3][@class='webim-audio-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		Runtime runtime=Runtime.getRuntime();
		
		try{
			
			runtime.exec("C:\\users\\gaozhq\\Documents\\audio.exe c://");
		}
		catch(IOException e){
				e.printStackTrace();
		}
			
		logger.info("talk to friend: {} with audio message: {}", friend);
		sleep(3);
		checkChatAudio(driver, username, friend, audio, path);
	}
	@Test(enabled = true, groups = { "onlinemessage" }, dependsOnMethods = { "sendOnLineMsg" })
	public void receiveOnLineMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatMsg(driver2, friend, username, msg, path);
		isReadMsg(driver, username, friend, path);
		checkChatImg(driver2, friend, username, img, path);
		isReadMsg(driver, username, friend, path);
		checkChatAudio(driver2, friend, username, audio, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}

	@Test(enabled = true, groups = { "offlinemessage" },dependsOnMethods = { "receiveOnLineMsg" })
	public void sendOffLineMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);		
		logger.info("find special friend: {}", friend);
		WebElement ele = findSpecialFriend(driver, friend, path);
		sleep(3);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
		ele = findElement(driver, xpath, path);
		cleanSingleMessage(driver,username,friend,path);
		msg = getRandomStr(10);
		logger.info("talk to friend: {} with message: {}", friend, msg);
		ele.clear();
		ele.sendKeys(msg);
		logger.info("send msg");
		xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("has send mesg");
		checkChatMsg(driver, username, friend, msg, path);
		sleep(3);
		logger.info("find img text area");
		xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		getExeFile("img.exe");
			
		logger.info("talk to friend: {} with img message: {}", friend);
		sleep(3);
		checkChatImg(driver, username, friend, img, path);
		sleep(3);
		logger.info("find message text area");
		xpath = "//div[@class='webim-chatwindow-options']/span[3][@class='webim-audio-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(5);
		Runtime runtime=Runtime.getRuntime();
		
		try{
			
			runtime.exec("C:\\users\\gaozhq\\Documents\\audio.exe c://");
		}
		catch(IOException e){
				e.printStackTrace();
		}
			
		logger.info("talk to friend: {} with audio message: {}", friend);
		sleep(3);
		checkChatAudio(driver, username, friend, audio, path);
	}
	@Test(enabled = true, groups = { "offlinemessage" }, dependsOnMethods = { "sendOffLineMsg" })
	public void receiveOffLineMsg() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driver2 = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver2=new ChromeDriver();
		super.login(driver2, friend, password, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatMsg(driver2, friend, username, msg, path);
		isReadMsg(driver, username, friend, path);	
		isReadMsg(driver, username, friend, path);
		checkChatImg(driver2, friend, username, img, path);
		isReadMsg(driver, username, friend, path);
		checkChatAudio(driver2, friend, username, audio, path);
		
	}
	
	@Test(enabled = true,dependsOnMethods = { "receiveOffLineMsg" })
	public void sendRestMessage(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver2, friend, password, path, true);
		logger.info("begin send rest group message");
		sendSingleMessageWithRest(friend, token);
		sleep(3);
		logger.info("begin receive rest  message");
		String xpath = "//div[@id='strangers']";
		WebElement element = findElement(driver2, xpath,path);
		element.click();
		sleep(5);
		xpath = "//div[@id='rest']/div[2]/span";
		element = findElement(driver2, xpath, path);
		element.click();
		msg="message from rest";
		sleep(3);
		logger.info("Check  message: {} has been received", msg);
		//checkChatMsg(driver2, friend, "rest", msg, path);
		
	}
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		quitDriver(driver2);
        
	}

}
