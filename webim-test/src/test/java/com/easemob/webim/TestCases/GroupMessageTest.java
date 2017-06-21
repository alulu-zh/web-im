package com.easemob.webim.TestCases;


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
public class GroupMessageTest extends WebIMTestBase {
	
	private static final Logger logger = LoggerFactory.getLogger(GroupMessageTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driver2;
	private String msg;
	private String groupName;
	private String groupID;
	private WebDriver driver;
	public DataReader dr;
	private String img="dog.jpg";
	private String audio="audio.amr";
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/GroupMessageTest.xml");
	    resgisteUserWithRest("GroupMessageData");
	    groupID=createGroupWithIndex("GroupMessageData",2);
	  } 
	
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void sendOnlineGroupMessage(Document params) {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver=new ChromeDriver();
		driver2=new ChromeDriver();
		username=dr.readnodevalue(params, "groupmessage", "username");
		password=dr.readnodevalue(params, "groupmessage", "password");
		friend=dr.readnodevalue(params, "groupmessage", "friend");
		groupName=dr.readnodevalue(params, "groupmessage", "groupname");
		super.login(driver, username, password, path, true);
		super.login(driver2, friend, password, path, true);
		logger.info("select the group to send message");
		WebElement ele = findSpecialGroupName(driver, groupName, path);
	    logger.info("The groupID is {}",groupID);
	    cleanGroupMessage(driver, username, groupID, path);
		logger.info("find text area for send message");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
 		findElement(driver, xpath,path);
 		//send text message
 		msg="group online message";
 		inputText(driver, xpath,msg,path);
		//xpath = "//div[@class='webim-chatwindow ']/div[2][@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		xpath ="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		logger.info("click send button");
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("Check group message: {} has been send", msg);
		checkChatMsg(driver, username, groupID, msg, path);
		//send img message
		logger.info("begin send image message");
		xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		
		getExeFile("img.exe");
		sleep(5);
		checkChatImg(driver, username, groupID, img, path);
		//send audio message
		sleep(3);
		logger.info("begin send audio message");
		xpath = "//div[@class='webim-chatwindow-options']/span[3][@class='webim-audio-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		getExeFile("audio.exe");
		sleep(5);
		checkChatAudio(driver, username, groupID, audio, path);
		
	}
	
	
	@Test(enabled = true, groups = { "group_message" },dependsOnMethods = { "sendOnlineGroupMessage" })
	public void receiveOnlineGroupMessage() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver2, friend, password, path, true);
		logger.info("select first group to send message");
		msg="group online message";
		WebElement ele = findSpecialGroupName(driver2, groupName, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		ele=findElement(driver2,xpath,path);
		sleep(3);
		logger.info("Check group message: {} has been received", msg);
		checkChatMsg(driver2, friend, groupID, msg, path);
		checkChatImg(driver2, friend, groupID, img, path);
		checkChatAudio(driver2, friend, groupID, audio, path);
		cleanGroupMessage(driver2, friend, groupID, path);
		super.logout(driver2, path);
	}
	
	@Test(enabled = true, groups = { "group_message" },dependsOnMethods = { "receiveOnlineGroupMessage" })
	public void sendOfflineGroupMessage() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, true);
		msg="group offline message";
		logger.info("select first group to send message");
		WebElement ele = findSpecialGroupName(driver, groupName, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		ele=findElement(driver,xpath,path);
		cleanGroupMessage(driver, username, groupID, path);
		logger.info("find text area for send message");
		xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
 		findElement(driver, xpath,path);
 		inputText(driver, xpath,msg,path);
		//xpath = "//div[@class='webim-chatwindow ']/div[2][@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		xpath ="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		logger.info("click send button");
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("Check group message: {} has been send", msg);
		checkChatMsg(driver, username, groupID, msg, path);
		logger.info("begin send image message");
		xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		getExeFile("img.exe");
		sleep(5);
		checkChatImg(driver, username, groupID, img, path);
		//send audio message
		sleep(3);
		logger.info("begin send audio message");
		xpath = "//div[@class='webim-chatwindow-options']/span[3][@class='webim-audio-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		getExeFile("audio.exe");
		sleep(5);
		checkChatAudio(driver, username, groupID, audio, path);
		
	}
	
	
	@Test(enabled = true, groups = { "group_message" },dependsOnMethods = { "sendOfflineGroupMessage" })
	public void receiveOfflineGroupMessage() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver2, friend, password, path, true);
		msg="group offline message";
		logger.info("select first group to send message");
		WebElement ele = findSpecialGroupName(driver2, groupName, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		ele=findElement(driver,xpath,path);
		sleep(3);
		logger.info("Check group message: {} has been received", msg);
		checkChatMsg(driver2, friend, groupID, msg, path);
		checkChatImg(driver2, friend, groupID, img, path);
		checkChatAudio(driver2, friend, groupID, audio, path);
		cleanGroupMessage(driver2, friend, groupID, path);

	}
	
	@Test(enabled = true, groups = { "group_message" },dependsOnMethods = { "receiveOfflineGroupMessage" })
	public void sendRestMessage(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver2, friend, password, path, true);
		logger.info("begin send rest group message");
		sendGroupMessageWithRest(groupID, token);
		sleep(3);
		logger.info("begin receive rest group message");
		WebElement ele = findSpecialGroupName(driver2, groupName, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		ele=findElement(driver,xpath,path);
		msg="message from rest";
		sleep(3);
		logger.info("Check group message: {} has been received", msg);
		checkChatMsg(driver2, friend, groupID, msg, path);
		cleanGroupMessage(driver2, friend, groupID, path);
		
	}
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		quitDriver(driver2);
		deleteGroupWithRest(groupID, token);
        
	}

}
