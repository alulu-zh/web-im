package com.easemob.webim.webim_test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.google.common.base.Preconditions;

@Listeners({ WebIMBaseListener.class })
@Test(suiteName = "WebIM_Base_Test", testName = "WebIM_Base_Test_on_Firefox", groups = { "Firefox_Group" })
public class FirefoxTest extends WebIMTestBase {
	private static final Logger logger = LoggerFactory.getLogger(FirefoxTest.class);

	private String username2;
	private String password2;
	private String username3="sand001";
	private String password3="asd";
	private String nickname2;
	private String msg;
	private WebDriver driver2;
	private String imgPath = "src/main/resources/test_img.png";
	private String audioPath = "src/main/resources/test_audio.mp3";
	private String filePath = "src/main/resources/test_file.txt";
	private String groupID;
	public static String IMG_TYPE = "img";
	public static String AUDIO_TYPE = "audio";
	public static String FILE_TYPE = "file";
	private static final String SETTING_BUTTON_XPATH = "//div[@class='webim-chat']/div[@class='webim-leftbar']/div/i[@class='webim-operations-icon font xsmaller']";
    private static final String RIGHT_SETTING_BUTTON_XPATH = "//div[@class='webim-chatwindow ']/div[2]/div/i[@class='webim-operations-icon font xsmaller']";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws MalformedURLException {
		logger.info("Start to webim auto test on firefox...");
		init();
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		//driver=new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver = new ChromeDriver();
		logger.info("finish initing ...");
	}
	@Test(enabled = true, groups = { "sanity_test" }, priority = -100)
	public void register() {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		username2 = "webimtest" + getRandomStr(8);
		password2 = "123456";
		nickname2 = "webimnick" + getRandomStr(8);
		logger.info("generate random username: {}, password: {}, nickname: {}", username2, password2, nickname2);
		driver.get(baseUrl);
		driver.manage().window().maximize();
		sleep(1);
		String xpath = "//*[@id='demo']/div/div/div[2]/p/i";
		WebElement reg = findElement(driver, xpath, path);
		reg.click();
		sleep(1);
		xpath = "//div[@class='webim-sign webim-signup']/input[@class='webim-input'][@type='text'][@placeholder='用户名']";
		WebElement ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(username2);
		sleep(1);

		xpath = "//div[@class='webim-sign webim-signup']/input[@class='webim-input'][@type='password'][@placeholder='密码']";
		ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(password2);
		sleep(1);

		xpath = "//div[@class='webim-sign webim-signup']/div/input[@class='webim-input'][@type='text'][@placeholder='昵称']";
		ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(nickname2);
		sleep(1);

		logger.info("click ok button");
		xpath = "//div[@class='webim-sign webim-signup']/button[@class='webim-button bg-color']";
		ele = findElement(driver, xpath, path);
		ele.click();
		

//		Alert alert = driver.switchTo().alert();
//		String text = alert.getText();
//		Assert.assertTrue(text.contains("注册成功"), "alert should indecate successful register");
//		alert.accept();
//		sleep(3);
		isGetBaseUrl = false;
	}

	@Test(enabled = true, groups = { "sanity_test" },dependsOnMethods = { "register" })
	public void loginWebIM() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "loginWebIM"})
	public void addFriend() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("click add friend button");
		sleep(5);
		String xpath = SETTING_BUTTON_XPATH;
		WebElement ele = findElement(driver, xpath, path);
		ele.click();
		sleep(1);
		xpath = "//ul[@class='webim-operations']/li/span[text()='添加好友']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("input friend id: {}", username2);
		xpath = "//div[@class='webim-dialog']/div/input[@class='webim-input'][@type='text'][@placeholder='用户名']";
		ele = findElement(driver, xpath, path);
		ele.clear();
		ele.sendKeys(username2);
		sleep(1);
		logger.info("click add button");
		xpath = "//div[@class='webim-dialog']/button[@class='webim-button bg-color webim-dialog-button'][text()='添加']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "addFriend" })
	public void getFriendList() {
		logger.info("get friend list");
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		String xpath = "//*[@id='friends']/i[1]";
		WebElement ele = findElement(driver, xpath, path);
		List<WebElement> wl = ele.findElements(By.xpath("//li"));
		Assert.assertTrue(null != wl && wl.size() > 0, "have found friends");
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "getFriendList" })
	public void loginWebIMWithNewUser() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driver2 = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
        driver2=new ChromeDriver();
		super.login(driver2, username2, password2, path, true);
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "loginWebIMWithNewUser" })
	public void receiveAddFriendConfirmMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		String xpath = "//*[@id='"+username+"']/button[1]";
		WebElement ele = findElement(driver2, xpath, path);
		ele.click();
		sleep(3);
		logger.info("find new friend: {}", username);
		xpath = "//*[@id='"+username+"']";
		ele = findElement(driver2, xpath, path);

		quitDriver(driver2);
		
	}
	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "receiveAddFriendConfirmMsg" })
	public void singleVideo(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		logger.info("find special friend: {}", username2);
		WebElement ele = findSpecialFriend(driver, username2, path);
		sleep(3);
		logger.info("click video sent button");
		String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/div[@class='webim-chatwindow-options']/span[5]";
		ele = findElement(driver, xpath, path);
		ele.click();
		logger.info("check if the window of video is opened");
		sleep(5);
		xpath="//div[@class='webim-chat']/div/div[@class='webim-rtc-video']/i[@id='webrtc_close']";
		ele = findElement(driver, xpath, path);
		sleep(3);
		if(ele.isDisplayed()){
			logger.info("the ele is disaplayed");
		}
		else{
			logger.info("the ele is not  disaplayed");
		}
		logger.info("accept video...");
		super.login(driver2, username2, password2, path, isGetBaseUrl);
		 WebDriverWait wait=new WebDriverWait(driver2, 5);
		 ExpectedCondition<Boolean> con1=new ExpectedCondition<Boolean>(){
			 public Boolean apply(WebDriver d) {
				 String xpath="//div[@class='webim-chat']/div/div[@class='webim-rtc-video']/i[@class='font small accept']";
	    			WebElement element = findElement(d, xpath, path);
	    			return element != null;
				 
			 }       
		 };
		 wait.until(con1);
		xpath="//div[@class='webim-chat']/div/div[@class='webim-rtc-video']/i[@class='font small accept']";
		ele = findElement(driver2, xpath, path);
		ele.click();
		logger.info("check if has connected ...");
		xpath="//div[@class='webim-chat']/div/div[@class='webim-rtc-video']/i[3][@class='font small toggle']";
		ele = findElement(driver2, xpath, path);
		logger.info("{} and {} has connected",username,username3);
		sleep(5);
		logger.info("Begin disconnect...");
		xpath="//i[@id='webrtc_close']";
		ele = findElement(driver2, xpath, path);
		ele.click();
		try{
			findElement(driver2, xpath, path);
		}catch(Exception e){
			logger.info("The video dialog has closed！");
		}
		
		
	}

	@Test(enabled = true, groups = { "sanity_test" },dependsOnMethods = { "receiveAddFriendConfirmMsg" })
	public void sendOffLineMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		
		logger.info("find special friend: {}", username2);
		WebElement ele = findSpecialFriend(driver, username2, path);
		sleep(3);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
		ele = findElement(driver, xpath, path);
		msg = getRandomStr(10);
		logger.info("talk to friend: {} with message: {}", username2, msg);
		ele.clear();
		ele.sendKeys(msg);
		logger.info("send msg");
		xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		logger.info("has send mesg");
		checkChatMsg(driver, username, username2, msg, path);
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "sendOffLineMsg" })
	public void receiveOffLineMsg() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driver2 = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver2=new ChromeDriver();
		super.login(driver2, username2, password2, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatMsg(driver2, username2, username, msg, path);
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "receiveOffLineMsg" })
	public void sendOnLineMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		logger.info("find special friend: {}", username2);
		WebElement ele = findSpecialFriend(driver, username2, path);
		sleep(3);
		logger.info("find message text area");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
		ele = findElement(driver, xpath, path);
		msg = getRandomStr(10);
		logger.info("talk to friend: {} with message: {}", username2, msg);
		ele.sendKeys(msg);
		logger.info("send msg");
		xpath ="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		checkChatMsg(driver, username, username2, msg, path);
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "sendOnLineMsg" })
	public void receiveOnLineMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		sleep(3);
		checkChatMsg(driver2, username2, username, msg, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "receiveOnLineMsg" })
	public void sendOffLineImg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("find special friend: {}", username2);
		findSpecialFriend(driver, username2, path);
		logger.info("send image file: {} to friend: {}", imgPath, username2);
		String[] sp = imgPath.split("/");
		String fp = sp[sp.length - 1];
		String data_type = "img";
		sendFile(driver, imgPath, data_type, path);
		sleep(3);
		logger.info("Check image file: {} has been send", imgPath);
		checkChatMsg(driver, username, username2, fp, path);
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "sendOffLineImg" })
	public void receiveOffLineImg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver2 = new FirefoxDriver();
		super.login(driver2, username2, password2, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		String[] sp = imgPath.split("/");
		String fp = sp[sp.length - 1];
		logger.info("Check image file: {} has been received", imgPath);
		checkChatMsg(driver2, username2, username, fp, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "receiveOffLineImg" })
	public void sendOffLineAudio() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("find special friend: {}", username2);
		findSpecialFriend(driver, username2, path);
		logger.info("send image file: {} to friend: {}", audioPath, username2);
		String[] sp = audioPath.split("/");
		String fp = sp[sp.length - 1];
		String data_type = "audio";
		sendFile(driver, audioPath, data_type, path);
		sleep(3);
		logger.info("Check audio file: {} has been send", audioPath);
		checkChatMsg(driver, username, username2, fp, path);
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "sendOffLineAudio" })
	public void receiveOffLineAudio() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver2 = new FirefoxDriver();
		super.login(driver2, username2, password2, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		String[] sp = audioPath.split("/");
		String fp = sp[sp.length - 1];
		logger.info("Check audio file: {} has been received", audioPath);
		checkChatMsg(driver2, username2, username, fp, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "receiveOffLineAudio" })
	public void sendOffLineFile() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("find special friend: {}", username2);
		findSpecialFriend(driver, username2, path);
		logger.info("send txt file: {} to friend: {}", filePath, username2);
		String[] sp = filePath.split("/");
		String fp = sp[sp.length - 1];
		String data_type = "file";
		sendFile(driver, filePath, data_type, path);
		sleep(3);
		logger.info("Check txt file: {} has been send", filePath);
		checkChatMsg(driver, username, username2, fp, path);
	}

	@Test(enabled = false, groups = { "sanity_test" }, dependsOnMethods = { "sendOffLineFile" })
	public void receiveOffLineFile() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver2 = new FirefoxDriver();
		super.login(driver2, username2, password2, path, true);
		logger.info("find special friend: {}", username);
		findSpecialFriend(driver2, username, path);
		String[] sp = filePath.split("/");
		String fp = sp[sp.length - 1];
		logger.info("Check txt file: {} has been received", filePath);
		checkChatMsg(driver2, username2, username, fp, path);
		logger.info("quit driver");
		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "receiveOnLineMsg"  }, priority = 100)
	public void getGroupList() {
		logger.info("get group list");
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		String xpath = "//div[@class='webim-contact-wrapper']";
		WebElement ele = findElement(driver, xpath, path);
		List<WebElement> li = ele.findElements(By.xpath("//div[2]/div"));
		Assert.assertTrue(null != li && li.size() > 0, "hasn't found groups");
	}
	
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "getGroupList" })
	   public  void creatGroups(){
		String groupName="testGroup";
		String style="joinOpen";//ownerInvite,memberInvite,joinApproval,joinOpen
		List<String> members=new ArrayList<String>();
		members.add(username2);
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		 //create groups 
   	    String xpath = SETTING_BUTTON_XPATH;
        WebElement element = findElement(driver, xpath,path);
        element.click();
        // click create group button
        xpath = "//ul[@class='webim-operations']/li[3]/span[text()='创建群组']";
        element = findElement(driver, xpath,path);
        element.click();
       // input group name
        xpath = "//div[@class='webim-dialog webim-dialog-2']/div/input[@class='webim-input'][@type='text'][@placeholder='群组名']";
        inputText(driver, xpath, groupName,path);
        //check group style
        checkGroupStyle(driver,style,path);
        // click add member button
        logger.info("Begin click add member button");
        for(int i=0;i<members.size();i++){
       	    xpath = "//div[@id='react-multi-select-box-1']/button[@class='react-multi-select-box']/div[@class='react-multi-select-box-label']";
            element = findElement(driver, xpath,path);
            element.click();
            //add group members
     		String str=members.get(i);
     		
     		xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@class='react-multi-select-box-option'][@label='"+str+"']";
     		element = findElement(driver, xpath,path);
            element.click();
            logger.info("the {}st member {} is added",i+1,str);
            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
            element = findElement(driver, xpath,path);
            element.click();
            sleep(1);
     		}	
        // click create group button
        sleep(3);
        xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='添加']";
        element = findElement(driver,xpath,path);
        element.click();
        sleep(10);
        quitDriver(driver);
        
	}
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "creatGroups" })
	public void sendGroupMessage() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driver=new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver=new ChromeDriver();
		super.login(driver, username, password, path, true);
		logger.info("select first group to send message");
		WebElement ele = findSpecialGroup(driver, null, path);
	    groupID = ele.getAttribute("id");
		logger.info("find text area for send message");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
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
	}
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "sendGroupMessage" })
	 public void addgroupmembers() throws Exception{
		    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		    String addGroupMember="sdb002";
		    List<String> members=new ArrayList<>();
		    members.add(addGroupMember);
		    logger.info("select first group");
		    
		    WebElement ele = findSpecialGroup(driver, groupID, path);
			//String groupID = ele.getAttribute("id");
	    	 // click setting button
	        String xpath = RIGHT_SETTING_BUTTON_XPATH;
	        ele = findElement(driver, xpath,path);
	        ele.click();

	        // click manage group button
	        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
	        WebElement element = findElement(driver, xpath,path);
	       
	        logger.info("if hide?{}",element.getAttribute("class"));
	        if(element.getAttribute("class")=="webim-operations hide"){
	        	ele.click();
	        }
	        logger.info("if hide?{}",element.getAttribute("class"));
	        element.findElement(By.xpath("//li/span[text()='管理群成员']")).click();
	        //element.click();
	        sleep(2);
	        
	        // check group list
	        xpath="//div[@id='react-multi-select-box-2']/button[@class='react-multi-select-box']";
	        element=findElement(driver,xpath,path);
	        element.click();
	        //add group member
	        for(int i=0;i<members.size();i++){
	            //add group members
	     		 String str=members.get(i);
	     		 xpath="//div[@id='react-multi-select-box-2']/div[tabindex='0']";
	     		 ele=findElement(driver,xpath,path);
	     		 if (ele.getAttribute("class")=="react-multi-select-area react-multi-select-box-hidden"){
	     			 element.click();
	     		 }
	     		 xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
	     		 element = findElement(driver, xpath,path);
	             if (element.getAttribute("class")=="react-multi-select-box-option"){
	            	 element.click();
	             }
	            logger.info("the {}st member {} is added",i+1,str);
	            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
	            element = findElement(driver, xpath,path);
	            element.click();
	             sleep(1);
	     		}	
	        xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='确定']";
	        element = findElement(driver, xpath,path);
	        element.click();
	        //check if have add members
	    	sleep(3);
	    	
			if (!getGroupMembers(driver,groupID,path).containsAll(members)){
	        	logger.error("add group Member failed");
	        	throw new Exception("add group Member failed");
	        }
	    }
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "addgroupmembers" })
	 public void delgroupmembers() throws Exception{
		    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		    String addGroupMember="sdb002";
		    List<String> members=new ArrayList<>();
		    members.add(addGroupMember);
		    logger.info("select first group");
			WebElement ele = findSpecialGroup(driver, null, path);
			String groupID = ele.getAttribute("id");
	    	 // click setting button
	        String xpath = RIGHT_SETTING_BUTTON_XPATH;
	        ele = findElement(driver, xpath,path);
	        ele.click();

	        // click manage group button
	        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
	        WebElement element = findElement(driver, xpath,path);
	        if(element.getAttribute("class")=="webim-operations hide"){
	        	ele.click();
	        }
	        logger.info("if hide?{}",element.getAttribute("class"));
	        element.findElement(By.xpath("//li/span[text()='管理群成员']"));
	        element.click();
	        sleep(2);
	        
	        // check group list
	        xpath="//div[@id='react-multi-select-box-4']/button[@class='react-multi-select-box']";
	        element=findElement(driver,xpath,path);
	        element.click();
	        //add group member
	        for(int i=0;i<members.size();i++){
	            //add group members
	     		 String str=members.get(i);
	     		 xpath="//div[@id='react-multi-select-box-4']/div[tabindex='0']";
	     		 ele=findElement(driver,xpath,path);
	     		 if (ele.getAttribute("class")=="react-multi-select-area react-multi-select-box-hidden"){
	     			 element.click();
	     		 }
	     		 xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
	     		 element = findElement(driver, xpath,path);
	             if (element.getAttribute("class")=="react-multi-select-box-option"){
	            	 element.click();
	             }
	            logger.info("the {}st member {} is deleted",i+1,str);
	            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
	            element = findElement(driver, xpath,path);
	            element.click();
	             sleep(1);
	     		}	
	        xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='确定']";
	        element = findElement(driver, xpath,path);
	        element.click();
	        //check if have add members
	    	sleep(3);
	    	
			if (getGroupMembers(driver,groupID,path).containsAll(members)){
	        	logger.error("delete group Member failed");
	        	throw new Exception("delete group Member failed");
	        }
	    }

	
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "delgroupmembers" })
	public void getChatroomList() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("get chatroom list");
		String xpath = "//div[@id='chatrooms']";
		WebElement ele = findElement(driver, xpath, path);
		ele.click();
		sleep(1);
		xpath = "//div[@class='webim-contact-wrapper']";
		ele = findElement(driver, xpath, path);
		List<WebElement> wes = ele.findElements(By.xpath("//div[3]/div"));
		Assert.assertTrue(null != wes && wes.size() > 0, "have found chatrooms");
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "getChatroomList" })
	public void sendchatmessage() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.info("select first chatroom to send message");
		WebElement ele = findSpecialChatroom(driver, null, path);
		//String chatroomId = ele.getAttribute("id");
		logger.info("find text area for send message");
		String xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/textarea";
		ele = findElement(driver, xpath, path);
		ele.click();
		msg = "webim_test_sendchatroommessage" + getRandomStr(10);
		ele.clear();
		ele.sendKeys(msg);
		xpath = "//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/button[@class='webim-button bg-color webim-send-btn base-bgcolor disabled']";
		logger.info("click send button");
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		// logger.info("Check chatroom message: {} has been send", msg);
		// checkChatMsg(driver, username, chatroomId, msg, path);
	}
	
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "sendchatmessage" })
	public void addToBlacklist(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		findSpecialFriend(driver,username2,path);
		logger.info("click set button");
		String xpath = RIGHT_SETTING_BUTTON_XPATH;
	    WebElement element = findElement(driver, xpath,path);
	    element.click();
	    // click add to blacklist button
        xpath = "//ul[@class='webim-operations ']/li/span[text()='加入黑名单']";
        element = findElement(driver, xpath,path);
        element.click();
        sleep(5);
        logger.info("check if has add to blacklist");
        try{
        	xpath = "//div[@id='friends']";
    		WebElement ele = findElement(driver, xpath, path);
    		ele.click();
    		sleep(3);
    		xpath = "//div[@id='" + username2 + "']";
        	findElementByXpath(driver, xpath);
        	logger.info("has not add {} to blacklist",username2);
        	
        }catch(Exception e){
        	logger.info("has add {} to blacklist",username2);
        }
        
	}
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "addToBlacklist" })
	public void getFriendBlacklist() throws Exception{
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//super.login(driver, username, password, path, isGetBaseUrl);
		logger.info("click set button");
		String xpath = SETTING_BUTTON_XPATH;
	    WebElement element = findElement(driver, xpath,path);
	    element.click();
	    // click get blacklist button
	    xpath = "//ul[@class='webim-operations']/li/span[text()='好友黑名单']";
    	element = findElement(driver, xpath,path);
    	element.click();
    	//check blacklist not empty
    	xpath = "//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']";
    	element = findElement(driver, xpath,path);    	
    	List<WebElement> elements = element.findElements(By.xpath("//li[@class='webim-blacklist-item']"));
    	logger.info("blacklist lentg is: {}", elements.size());
    	if(elements.size()==0){
    		logger.error("not found contacts");
    		throw new Exception("get blacklist | blacklist should not empty");
    	}    	
    	//click OK button
    	xpath = "//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
    	element = findElement(driver, xpath,path);
    	element.click();
        
	}
	
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "getFriendBlacklist" })
	public void removeFromBlacklist() throws Exception{
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//super.login(driver, username, password, path, isGetBaseUrl);
		logger.info("click set button");
		String xpath = SETTING_BUTTON_XPATH;
	    WebElement element = findElement(driver, xpath,path);
	    element.click();
	    // click get blacklist button
	    xpath = "//ul[@class='webim-operations']/li/span[text()='好友黑名单']";
    	element = findElement(driver, xpath,path);
    	element.click();
    	//check blacklist not empty
    	xpath = "//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']";
    	element = findElement(driver, xpath,path);    	
    	List<WebElement> elements = element.findElements(By.xpath("//li[@class='webim-blacklist-item']"));
    	logger.info("blacklist lentg is: {}", elements.size());
    	if(elements.size()==0){
    		logger.error("not found contacts");
    		throw new Exception("get blacklist | blacklist should not empty");
    	}    	
    	for(int i=0;i<elements.size();i++){
    		String text=elements.get(i).getText();
    		logger.info("blacklist friend value of getText {}",text);
    		if(StringUtils.contains(text,username2)){
    			int j=i+1;
    			elements.get(i).findElement(By.xpath("//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']/li["+j+"]/i[@class='webim-leftbar-icon font smaller']")).click();
    			logger.info("has remove {} frome blacklist",username2);
    		}
    		
    	}
    	//click OK button
    	xpath = "//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
    	element = findElement(driver, xpath,path);
    	element.click();
        
	}
	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "removeFromBlacklist" })
	public void deleteUser() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		findSpecialFriend(driver,username2,path);
		logger.info("click set button");
		 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        WebElement element = findElement(driver, xpath,path);
        element.click();

        // click deleteFriend button
        xpath = "//ul[@class='webim-operations ']/li/span[text()='删除好友']";
        element = findElement(driver, xpath,path);
        element.click();
	}

	@Test(enabled = true, groups = { "sanity_test" }, dependsOnMethods = { "deleteUser" }, priority = 100)
	public void logoutWebIM() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.logout(driver, path);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		logger.info("End to webim auto test on firefox... ");
		if (null != driver) {
			try {
				driver.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver:", e);
			}
		}

		if (null != driver2) {
			try {
				driver2.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
		}
	}
}
