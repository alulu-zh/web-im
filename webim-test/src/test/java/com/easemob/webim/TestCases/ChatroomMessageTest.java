
package com.easemob.webim.TestCases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class ChatroomMessageTest extends WebIMTestBase {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatroomMessageTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driver2;
	private String msg;
	private String groupName;
	private String chatroomID;
	private WebDriver driver;
	public DataReader dr;
	private String img="dog.jpg";
	private String audio="audio.amr";
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/ChatroomMessageTest.xml");
	    resgisteUserWithRest("ChatroomMessageData");
	    chatroomID=createChatroomWithIndex("ChatroomMessageData",2);
	  } 
	
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void sendOnlineChatroomMessage(Document params) {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver=new ChromeDriver();
		driver2=new ChromeDriver();
		username=dr.readnodevalue(params, "chatroommessage", "username");
		password=dr.readnodevalue(params, "chatroommessage", "password");
		friend=dr.readnodevalue(params, "chatroommessage", "friend");
		groupName=dr.readnodevalue(params, "chatroommessage", "groupname");
		super.login(driver, username, password, path, true);
		super.login(driver2, friend, password, path, true);
		logger.info("select the group to send message");
		WebElement ele = findSpecialChatroom(driver, chatroomID, path);
	    logger.info("The chatroomID is {}",chatroomID);
	    cleanGroupMessage(driver, username, chatroomID, path);
		logger.info("find text area for send message");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
 		findElement(driver, xpath,path);
 		//send text message
 		msg="chatroom online message";
 		inputText(driver, xpath,msg,path);
		xpath ="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		logger.info("click send button");
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("Check chatroom message: {} has been send", msg);
		checkChatMsg(driver, username, chatroomID, msg, path);
		//send img message
//		logger.info("begin send image message");
//		xpath = "//div[@class='webim-chatwindow-options']/span[2][@class='webim-picture-icon font smaller']";
//		ele = findElement(driver, xpath, path);
//		ele.click();
//		
//		getExeFile("img.exe");
//		sleep(5);
//		checkChatImg(driver, username, chatroomID, img, path);
		//send audio message
//		sleep(3);
//		logger.info("begin send audio message");
//		xpath = "//div[@class='webim-chatwindow-options']/span[3][@class='webim-audio-icon font smaller']";
//		ele = findElement(driver, xpath, path);
//		ele.click();
//		getExeFile("audio.exe");
//		sleep(5);
//		checkChatAudio(driver, username, chatroomID, audio, path);
		
	}
	
	
	@Test(enabled = true,dependsOnMethods = { "sendOnlineChatroomMessage" })
	public void receiveOnlineChatMessage() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver2, friend, password, path, true);
		logger.info("select first group to send message");
		findSpecialChatroom(driver2, chatroomID, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		findElement(driver2,xpath,path);
		sleep(3);
		logger.info("Check group message: {} has been received", msg);
		checkChatMsg(driver2, friend, chatroomID, msg, path);
//		checkChatImg(driver2, friend, chatroomID, img, path);
//		checkChatAudio(driver2, friend, chatroomID, audio, path);
//		cleanGroupMessage(driver2, friend, chatroomID, path);
		super.logout(driver2, path);
	}
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		quitDriver(driver2);
		deleteChatroomWithRest(chatroomID, token);
        
	}

}

