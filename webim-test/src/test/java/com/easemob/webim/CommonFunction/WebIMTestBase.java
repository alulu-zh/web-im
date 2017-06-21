package com.easemob.webim.CommonFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.w3c.dom.Document;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class WebIMTestBase {
	public static String PROPERTY_BASE_URL = "BASE_URL";
	public static String PROPERTY_INTERNAL_BASE_URL = "INTERNAL_BASE_URL";
	public static String PROPERTY_USER_NAME = "USER_NAME";
	public static String PROPERTY_INTERNAL_USER_NAME = "INTERNAL_USER_NAME";
	public static String PROPERTY_USER_PASSWORD = "USER_PASSWORD";
	public static String PROPERTY_INTERNAL_USER_PASSWORD = "INTERNAL_USER_PASSWORD";
	private static final String LOGIN_BUTTON_XPATH = "//div[@class='webim-sign']/button[@class='webim-button bg-color']";
    public static final String SETTING_BUTTON_XPATH = "//div[@class='webim-chat']/div[@class='webim-leftbar']/div/i[@class='webim-operations-icon font xsmaller']";
    public static final String RIGHT_SETTING_BUTTON_XPATH = "//div[@class='webim-chatwindow ']/div[2]/div/i[@class='webim-operations-icon font xsmaller']";
	private static final Logger logger = LoggerFactory.getLogger(WebIMTestBase.class);
	private static final long DEFAULT_TIMEOUT = 10L;
	protected WebDriver driver;
	protected static String baseUrl;
	protected String username;
	protected String password;
	protected String screenshotPath = "target";
	protected String screenshotSuffix = "png";
	protected boolean isGetBaseUrl = true;
	protected static String token=System.getProperty("TOKEN");
	protected static String resturl=System.getProperty("REST_URL");
	protected static String appkey=System.getProperty("APPKEY");
	public Document doc;
	
	public void init() {
		if (StringUtils.isNotBlank(System.getProperty(PROPERTY_BASE_URL))) {
			baseUrl = System.getProperty(PROPERTY_BASE_URL);
		} else if (StringUtils.isNotBlank(System.getProperty(PROPERTY_INTERNAL_BASE_URL))) {
			String path = System.getProperty(PROPERTY_INTERNAL_BASE_URL);
			//find local index.html
			if (!path.contains("index.html")) {
				File file = new File(path);
				if (file.isDirectory()) {

					baseUrl = "file://" + file.getParentFile().getAbsolutePath() + System.getProperty("file.separator") + "index.html";
				}
			} else {
				baseUrl = System.getProperty(PROPERTY_INTERNAL_BASE_URL);
			}
		}
		logger.info("Initial base url: {}", baseUrl);
		if (StringUtils.isNotBlank(System.getProperty(PROPERTY_USER_NAME))) {
			username = System.getProperty(PROPERTY_USER_NAME);
		} else if (StringUtils.isNotBlank(System.getProperty(PROPERTY_INTERNAL_USER_NAME))) {
			username = System.getProperty(PROPERTY_INTERNAL_USER_NAME);
		}
		logger.info("Initial username: {}", username);
		if (StringUtils.isNotBlank(System.getProperty(PROPERTY_USER_PASSWORD))) {
			password = System.getProperty(PROPERTY_USER_PASSWORD);
		} else if (StringUtils.isNotBlank(System.getProperty(PROPERTY_INTERNAL_USER_PASSWORD))) {
			password = System.getProperty(PROPERTY_INTERNAL_USER_PASSWORD);
		}
		logger.info("Initial password: {}", password);
	}
	
	public static void init_noPublicUsername(){
		logger.info("Start to webim auto test on chrome...");
		if (StringUtils.isNotBlank(System.getProperty(PROPERTY_BASE_URL))) {
			baseUrl = System.getProperty(PROPERTY_BASE_URL);
		} else if (StringUtils.isNotBlank(System.getProperty(PROPERTY_INTERNAL_BASE_URL))) {
			String path = System.getProperty(PROPERTY_INTERNAL_BASE_URL);
			//find local index.html
			if (!path.contains("index.html")) {
				File file = new File(path);
				if (file.isDirectory()) {

					baseUrl = "file://" + file.getParentFile().getAbsolutePath() + System.getProperty("file.separator") + "index.html";
				}
			} else {
				baseUrl = System.getProperty(PROPERTY_INTERNAL_BASE_URL);
			}
		}
		logger.info("Initial base url: {}", baseUrl);
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");		
		logger.info("finish initing ...");
	           
		
	}

	public void login(WebDriver driver, String username, String password, String path, boolean isGetBaseUrl) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		
		Preconditions.checkArgument(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password),
				"username or password was missing!");
		WebElement we = checkLogin(driver);
		if (null != we && we.isDisplayed()) {
			return;
		}
		if (isGetBaseUrl) {
			driver.get(baseUrl);
			//driver.get("http://webim.easemob.com");
		}
		//driver.manage().window().maximize();
		sleep(5);
		logger.info("find username box and input username: {}", username);
		String xpath = "//input[@type='text']";
		WebElement usernameInput = findElementByXpath(driver, xpath);
		if (null == usernameInput) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(usernameInput);
		usernameInput.clear();
		usernameInput.sendKeys(username);

		logger.info("find password box and input password: {}", password);
		xpath = "//input[@type='password']";
		WebElement passwordInput = findElementByXpath(driver, xpath);
		if (null == passwordInput) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(passwordInput);
		passwordInput.clear();
		passwordInput.sendKeys(password);

		logger.info("click login button");
		xpath = "//button[@class='webim-button bg-color']";
		WebElement login = findElementByXpath(driver, xpath);
		if (null == login) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(login);
		login.click();
		sleep(5);

		logger.info("check if login webim successfully");
		WebElement ele = checkLogin(driver);
		if (null == ele) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(ele);
	}
	 public  WebElement inputText(WebDriver driver, String xpath, String text,String path) {
	        WebElement element = findElement(driver, xpath, path);
	        element.clear();
	        element.sendKeys(text);
	        return element;
	    }
	 public  WebElement checkGroupStyle(WebDriver driver,String style,String path){
		    //	boolean isPublic=true;
		    //	boolean needApprove=true;
		    	//boolean memberInvite=true;
		    	logger.info("here is checkgroupstyle {}!",style);
		    	if (StringUtils.equals("ownerInvite", style)){
		    		String xpath="//div/label[3]/span[text()='私有群']";
		    		WebElement element = findElement(driver, xpath,path);
		            element.click();
		            xpath="//div/label[2]/span[text()='不允许邀请']";
		    	    element = findElement(driver, xpath,path);
		            element.click();
		            logger.info("private group not allow member invite check success");
		            return element;
		            
		    	}
		    	else if(StringUtils.equals("memberInvite", style)){
		    		String xpath="//div/label[3]/span[text()='私有群']";
		    		WebElement element = findElement(driver, xpath,path);
		            element.click();
		            xpath="//div/label[3]/span[text()='允许']";
		    	    element = findElement(driver, xpath,path);
		            element.click();
		            logger.info("private group allow member invite check success");
		            return element;
		    		
		        }
		    	else if(StringUtils.equals("joinApproval", style)){
		    		String xpath="//div/label[2]/span[text()='公有群']";
		    		WebElement element = findElement(driver, xpath,path);
		            element.click();
		            xpath="//div/label[2]/span[text()='审批']";
		    	    element = findElement(driver, xpath,path);
		            element.click();
		            return element;
		    	}
		    	else if(StringUtils.equals("joinOpen", style)){
		    		String xpath="//div/label[2]/span[text()='公有群']";
		    		WebElement element = findElement(driver, xpath,path);
		            element.click();
		            xpath="//div/label[3]/span[text()='随便加']";
		    	    element = findElement(driver, xpath,path);
		            element.click();
		            return element;
		    	}
		    	
		    	else{
		    		
		    		logger.info("you provide illegal argument exception! ");
		    		throw new IllegalArgumentException();
		    	}
		    	
		    }

	public WebElement checkLogin(WebDriver driver) {
		String xpath = "//div[@id='friends']";
		WebElement ele = null;
		try {
			ele = findElementByXpath(driver, xpath);
		} catch (Exception e) {
			logger.error("Failed to check login page", e);
			ele = null;
		}
		return ele;
	}

	public WebElement findSpecialFriend(WebDriver driver, String username, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username), "friend name was missing!");
		String xpath = "//div[@id='friends']";
		WebElement ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		xpath = "//div[@id='" + username + "']";
		ele = findElement(driver, xpath, path);
		if (StringUtils.isNotBlank(ele.getAttribute("class"))) {
			ele.click();
		}
		return ele;
	}
	public boolean findGroupMembers(WebDriver driver,String groupID,String path,List<String> members){
		logger.info("find special group!");
    	findSpecialGroup(driver,groupID,path);
    	String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-down-icon']";
    	WebElement element=findElement(driver,xpath,path);
    	element.click();
    	sleep(5);
    	try{
    		for (int i=0;i<members.size();i++){
        		String member=members.get(i);
        		xpath="//div[@class='webim-chatwindow ']/ul[@class='webim-group-memeber']/li/span[text()='"+member+"']";
        		element=findElement(driver,xpath,path);
        		logger.info("find group member {} success!",member);
        	}
    	}
    	catch(Exception e){
    		logger.info("find group members failed!");
    		return false;
    	}
    	finally{
    		xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-up-icon']";
        	element=findElement(driver,xpath,path);
        	element.click();
    	}
    	return true;
    	}
    	
    public void checkChatImg(WebDriver driver, String username1, String username2, String img, String path){
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username1) && StringUtils.isNotBlank(username2),
				"username1 or username2 was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(img), "message was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath = "//div[@class='webim-chatwindow ']/div[@id='wrapper"+username2+"']";
		WebElement ele = findElement(driver, xpath, path);
		try {
			List<WebElement> eles = ele.findElements(By.xpath("//img[@class='webim-msg-img']"));
			if (eles.size()>0) {
				logger.info("find img: {}", img);
				return;		
			}
			
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + img);
		} catch (Exception e) {
			logger.error("Failed to find chat log: user1: {}, user2: {}, message: {}", username1, username2, img, e);
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + img);
		}
	}
    
    public void checkChatAudio(WebDriver driver, String username1, String username2, String audio, String path){
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username1) && StringUtils.isNotBlank(username2),
				"username1 or username2 was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(audio), "message was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath = "//div[@class='webim-chatwindow ']/div[@id='wrapper"+username2+"']";
		WebElement ele = findElement(driver, xpath, path);
		try {
			List<WebElement> eles = ele.findElements(By.xpath("//div[@class='webim-msg-value']/div[@class='webim-audio-slash']"));
			if (eles.size()>0) {
				logger.info("find audio: {}", audio);
				return;		
			}
			
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + audio);
		} catch (Exception e) {
			logger.error("Failed to find chat log: user1: {}, user2: {}, message: {}", username1, username2, audio, e);
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + audio);
		}
	}
    
	public void checkChatMsg(WebDriver driver, String username1, String username2, String msg, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username1) && StringUtils.isNotBlank(username2),
				"username1 or username2 was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(msg), "message was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath = "//div[@class='webim-chatwindow ']/div[@id='wrapper"+username2+"']";
		WebElement ele = findElement(driver, xpath, path);
		try {
			List<WebElement> eles = ele.findElements(By.xpath("//pre"));
			for (WebElement we : eles) {
				if (we.getText().contains(msg)) {
					logger.info("find message: {}", msg);
					return;
					
				}
			}
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + msg);
		} catch (Exception e) {
			logger.error("Failed to find chat log: user1: {}, user2: {}, message: {}", username1, username2, msg, e);
			Assert.assertTrue(false,
					"didn't find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + msg);
		}
	}
	
	public void isCleanMsg(WebDriver driver, String username1, String friend, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username1) && StringUtils.isNotBlank(friend),
				"username1 or username2 was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath = "//div[@class='webim-chatwindow ']/div[@id='wrapper"+friend+"']";
		WebElement ele = findElement(driver, xpath, path);
		List<WebElement> eles=ele.findElements(By.xpath("//pre"));
		if(eles.size()==0){
			logger.info("message list is blank");
		}else{
			Assert.assertTrue(false,"message list is not blank");
		}		
		
	}
	
	public void isReadMsg(WebDriver driver, String username1, String friend, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username1) && StringUtils.isNotBlank(friend),
				"username1 or username2 was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath = "//div[@class='webim-chatwindow ']/div[@id='wrapper"+friend+"']";
		WebElement ele = findElement(driver, xpath, path);
		List<WebElement> eles=ele.findElements(By.xpath("//div[@class='webim-msg-delivered '][text()='已读']"));
		if(eles.size()==0){
			Assert.assertTrue(false,"message has not been read");
			
		}else{
			logger.info("message has been read");
		}		
		
	}
	public void cleanSingleMessage(WebDriver driver, String username, String friend,String path){
		logger.info("begin clean chatmessage of {} and {}",username,friend);
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(friend),
				"username or friend was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath="//div[@class='webim-chatwindow-options']/span[7]";
		findElement(driver, xpath, path).click();
		isCleanMsg(driver,username,friend,path);
	}
	
	public void cleanGroupMessage(WebDriver driver, String username, String groupID,String path){
		logger.info("begin clean chatmessage of {} and {}",username,groupID);
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(groupID),
				"username or friend was missing");
		WebElement wet = checkLogin(driver);
		Assert.assertTrue(null != wet && wet.isDisplayed(), "check login web page");
		String xpath="//div[@class='webim-chatwindow-options']/span[5]";
		findElement(driver, xpath, path).click();
		isCleanMsg(driver,username,groupID,path);
	}

	public void getExeFile(String filename){
		Runtime runtime=Runtime.getRuntime();
		try{
			
			runtime.exec("C:\\users\\gaozhq\\Documents\\"+filename + " c://");
		}
		catch(IOException e){
				e.printStackTrace();
		}		
	}
	public void sendFile(WebDriver driver, String filePath, String data_type, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(filePath) && StringUtils.isNotBlank(data_type),
				"file path or data type was missing");
		logger.info("find file input");
		String xpath = "//input[@id='fileInput']";
		WebElement ele = findElement(driver, xpath, path);
		sleep(1);
		logger.info("reset file input property");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("$('#fileInput').show(); $('#fileInput').attr('data-type', '" + data_type + "');");
		sleep(3);
		File file = new File(filePath);
		String str = null;
		if (file.exists()) {
			logger.info("find resource file: {}", file.getAbsolutePath());
			str = file.getAbsolutePath();
		}
		Assert.assertNotNull(str, "resource file path");
		ele.sendKeys(str);
		sleep(10);
		logger.info("set back file input property");
		jse.executeScript("$('#fileInput').hide();");
	}

	public void logout(WebDriver driver, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		logger.info("click logout button");
		String xpath = SETTING_BUTTON_XPATH;
        WebElement ele = findElement(driver, xpath,path);
        ele.click();
        String ifhide="//div[@class='webim-chat']/div[@class='webim-leftbar']/div/ul";
        WebElement element = findElement(driver, ifhide,path);
        if (element.getAttribute("class")=="webim-operations  hide"){
        	ele.click();
        }
        //click logout button
        xpath="//div/ul[@class='webim-operations']/li/span/span[1]";
        element = findElement(driver, xpath,path);
        element.click();
        
        findElement(driver, LOGIN_BUTTON_XPATH, path);
	}

	public String getPath(String path) {
		return path + "_" + System.currentTimeMillis() + "." + screenshotSuffix;
	}

	@SuppressWarnings("static-access")
	public void sleep(int seconds) {
		logger.info("Start to sleep {} seconds...", seconds);
		try {
			Thread.currentThread().sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			logger.error("Failed to sleep {} seconds", seconds);
		}
	}

	public void screenshot(WebDriver driver, String path) {
		Preconditions.checkArgument(StringUtils.isNotBlank(path), "screenshot file path was missing!");
		try {
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			File srcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(path));
		} catch (Exception e) {
			logger.error("Failed to get screenshot: path[{}]", path, e);
		}
	}

	public WebElement findElementByXpath(WebDriver driver, String xpath) {
		WebElement element = null;
		try {
			element = driver.findElement(By.xpath(xpath));
		} catch (Exception e) {
			logger.error("Failed to find element: xpath[{}]", xpath, e);
			return null;
		}
		return element;
	}
	 public static WebElement findElement(WebDriver driver, String xpath, long timeout) {
	        if (Objects.isNull(driver) || StringUtils.isBlank(xpath)) {
	            logger.error("findElement | Illegal arguments: driver: {}, xpath: {}", driver, xpath);
	            throw new IllegalArgumentException("findElement | Illegal arguments");
	        }
	        ExpectedCondition<WebElement> ec = driv -> {
	            return driv.findElement(By.xpath(xpath));
	        };
	        return new WebDriverWait(driver, timeout).until(ec);
	    }
	 
	 public static WebElement findElement(WebDriver driver, String xpath, String path) {
	        return findElement(driver, xpath, DEFAULT_TIMEOUT);
	    }
	 
	/*public WebElement findElement(WebDriver driver, String xpath, String path) {
		WebElement element = findElementByXpath(driver, xpath);
		if (null == element) {
			logger.error("Find element is null: xpath[{}]", xpath);
			screenshot(driver, getPath(path));
			
		}
		Assert.assertNotNull(element, "Find element with xpath[" + xpath + "]");
		return element;
	}*/
	 public void getFriendList() {
			logger.info("get friend list");
			String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
			String xpath = "//*[@id='friends']/i[1]";
			WebElement ele = findElement(driver, xpath, path);
			List<WebElement> wl = ele.findElements(By.xpath("//li"));
			Assert.assertTrue(null != wl && wl.size() > 0, "have found friends");
		}
	 
	public String getRandomStr(int count) {
		return RandomStringUtils.randomAlphanumeric(count).toLowerCase();
	}
	public  WebElement findSpecialGroupName(WebDriver driver, String groupName,String path){
    	Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(groupName), "groupName name was missing!");
		String xpath = "//div[@id='groups']/i[@title='群组']";
		WebElement element = findElement(driver, xpath,path);
		element.click();
		xpath = "//div[@class='webim-contact-info']/span[text()='" + groupName + "']";
		element = findElement(driver, xpath,path);
		element.click();
		return element;
	}
	
	public WebElement findSpecialGroup(WebDriver driver, String groupId, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		String xpath = "//div[@class='webim-leftbar']/div[@id='groups']/i[@title='群组']";
		WebElement ele = findElement(driver, xpath, path);
		ele.click();
		if (ele.getAttribute("class").equals("webim-leftbar-icon font small")){
			ele.click();
			sleep(3);
		}
		if (StringUtils.isNotBlank(groupId)) {
			logger.info("select group: {}", groupId);
			xpath = "//div[@class='webim-contact-wrapper']/div[2]/div[@id='" + groupId + "']";
		} else {
			logger.info("select first group");
			xpath = "//div[@class='webim-contact-wrapper']/div[2]/div[@class='webim-contact-item']";
		}
		ele = findElement(driver, xpath, path);
		if (StringUtils.isNotBlank(ele.getAttribute("id"))) {
			ele.click();
			sleep(3);
		}
		return ele;
	}
	
	public boolean getgroupblacklist(String groupID,String friend,WebDriver driver) throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    sleep(5);
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
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span[text()='群组黑名单']";
        ele=findElement(driver, xpath,path);
        ele.click();
        sleep(2);
        try{
        	xpath="//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']/li[@class='webim-blacklist-item']";
            ele=findElement(driver, xpath,path);
        }
        catch(Exception e){
        	logger.info("blacklist is null!");
        	return false;
        }
        if(StringUtils.contains(ele.getText(), friend)){
        	return true;
        }
        else{
        	return false;
        } 
       
    }
	
	public static void deleteGroupWithRest(String groupID,String token){
		
		String url=resturl+appkey+"/chatgroups/"+groupID;		
		RestAPI.requestByDeleteMethod(url,token);
	}
	
	public static void deleteChatroomWithRest(String chatroomID,String token){
		
		String url=resturl+appkey+"/chatrooms/"+chatroomID;		
		RestAPI.requestByDeleteMethod(url,token);
	}
	
	public static void deleteFriendWithRest(String username,String friend,String token){
		String url=resturl+appkey+"/users/"+username+"/contacts/users/"+friend;
		RestAPI.requestByDeleteMethod(url,token);
	}
	
	public static void deleteUserWithRest(String username,String token){
		String url=resturl+appkey+"/users/"+username;
		RestAPI.requestByDeleteMethod(url,token);
	}
	
	public static boolean IsUserExist(String user){
		String get_user_url=resturl+appkey+"/users/"+user;
		//System.out.println( "respnse status is "+RestAPI.requestByGetMethod(get_user_url, token).get(0));
		List<String> result=RestAPI.requestByGetMethod(get_user_url, token);
		int status=Integer.parseInt(result.get(0).toString());
		if(status==200){
			return true;
		}
		return false;
		
	}
	
	public boolean IsGroupExist(String groupID){
		String get_group_url=resturl+appkey+"/users/"+groupID;
		int status=Integer.parseInt(RestAPI.requestByGetMethod(get_group_url, token).get(0).toString());
		if(status==200){
			return true;
		}
		return false;
		
	}
	public static void resgisteUserWithRest(String source_json){
		 JsonParser parser=new JsonParser();
		    try {
		    	String file="src/test/java/com/easemob/webim/TestData/RestApiData.json";
		    	JsonObject json;
				json = (JsonObject)parser.parse(new FileReader(file));		
		    	JsonArray jsonArray=(JsonArray)json.get(source_json).getAsJsonArray();
		    	if (jsonArray.size()>0){
		    		//register group owner
		    		for(int i=0;i<jsonArray.size();i++){
		    			JsonObject register_json=jsonArray.get(i).getAsJsonObject();
			    		String register_url=resturl+appkey+"/users";
			    		if(null!=register_json.get("username")){
			    			String user=register_json.get("username").getAsString();
				    		if(IsUserExist(user)){
				    			logger.info("user exist!");	
				    		}else{
				    			logger.info("user {} doesn't exist,begin register!",user);
				    			RestAPI.requestByPostMethod(register_url, register_json, token);
					    		logger.info("Finish register!");
				    		}
			    		}
			    		
		    		}	    		
		    		
		    	}
				
			} catch (JsonIOException e) {
				e.printStackTrace();
			}catch (JsonSyntaxException e) {				
				e.printStackTrace();
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
			}
		
	}
	
	public static String createGroupWithIndex(String source_json,int i){
		JsonParser parser=new JsonParser();
		 String file="src/test/java/com/easemob/webim/TestData/RestApiData.json";
	     JsonObject json = null;
		try {
			json = (JsonObject)parser.parse(new FileReader(file));
		} catch (JsonIOException e) {
			
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	     JsonArray jsonArray=(JsonArray)json.get(source_json).getAsJsonArray();
	     JsonObject create_group_json=jsonArray.get(i).getAsJsonObject();
		 String groupID=createGroupWithRest(create_group_json,token);
		 logger.info("Finish creating group!");
		 return groupID;
	}
	
	public static String createChatroomWithIndex(String source_json,int i){
		JsonParser parser=new JsonParser();
		 String file="src/test/java/com/easemob/webim/TestData/RestApiData.json";
	     JsonObject json = null;
		try {
			json = (JsonObject)parser.parse(new FileReader(file));
		} catch (JsonIOException e) {
			
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	     JsonArray jsonArray=(JsonArray)json.get(source_json).getAsJsonArray();
	     JsonObject create_chatroom_json=jsonArray.get(i).getAsJsonObject();
		 String chatroomID=createChatroomWithRest(create_chatroom_json,token);
		 logger.info("Finish creating chatroom!");
		 return chatroomID;
	}
	
	public static String createChatroomWithRest(JsonObject json,String token){
		
		String urlNewChatroom=resturl+appkey+"/chatrooms";
		
		try { 
				String body=RestAPI.requestByPostMethod(urlNewChatroom, json, token).get(1).toString();
				System.out.println(body);
				JsonParser parser=new JsonParser();
				JsonObject jsonid=(JsonObject) parser.parse(body);
				System.out.println("the chatroomID is"+jsonid.get("data").getAsJsonObject().get("id").getAsString());
				return jsonid.get("data").getAsJsonObject().get("id").getAsString();
				
			} catch (ParseException e) {
				
				e.printStackTrace();
			} 
						 
        return null;   	
		
	}
	public static String createGroupWithRest(JsonObject json,String token){
		
		String urlNewGroup=resturl+appkey+"/chatgroups";
		
		try { 
				String body=RestAPI.requestByPostMethod(urlNewGroup, json, token).get(1).toString();
				System.out.println(body);
				JsonParser parser=new JsonParser();
				JsonObject jsonid=(JsonObject) parser.parse(body);
				System.out.println("the groupID is"+jsonid.get("data").getAsJsonObject().get("groupid").getAsString());
				return jsonid.get("data").getAsJsonObject().get("groupid").getAsString();
				
			} catch (ParseException e) {
				
				e.printStackTrace();
			} 
						 
        return null;   		
		
	}
	
	public void sendGroupMessageWithRest(String groupID,String token){
		JsonParser parser=new JsonParser();
		String body="{ 'target_type':'chatgroups', 'target':["+groupID+"],'msg':{'type':'txt', 'msg':'message from rest' },'from':'rest'}";
		JsonObject json=(JsonObject)parser.parse(body);
		String sendGroupUrl=resturl+appkey+"/messages";
		try { 
			String result=RestAPI.requestByPostMethod(sendGroupUrl, json, token).get(1).toString();
			System.out.println(result);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
	}
	
	public void sendSingleMessageWithRest(String username,String token){
		JsonParser parser=new JsonParser();
		String body="{ 'target_type':'users', 'target':["+username+"],'msg':{'type':'txt', 'msg':'message from rest' },'from':'rest'}";
		JsonObject json=(JsonObject)parser.parse(body);
		String sendSingleMessageUrl=resturl+appkey+"/messages";
		try { 
			String result=RestAPI.requestByPostMethod(sendSingleMessageUrl, json, token).get(1).toString();
			System.out.println(result);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
	}
	public static boolean addFriendWithRest(String username,String friend,String token){
		String url=resturl+appkey+"/users/"+username+"/contacts/users/"+friend;
		List<String> result=RestAPI.requestByPostMethod(url, null, token);
		logger.info("Add {} as friend",friend);
		result.get(0).toString();
		if(result.get(0).equals("200")){
			return true;
		}
		return false;
	}
	
	public static boolean getGroupManagerWithRest(String groupID,String token,String friend){
		String url=resturl+appkey+"/chatgroups/"+groupID+"/admin";
		String  body=RestAPI.requestByGetMethod(url,token).get(1).toString();
		JsonParser parser=new JsonParser();
		JsonObject json=(JsonObject) parser.parse(body);
		if(json.get("data").getAsJsonArray().size()!=0){
			if(json.get("data").getAsJsonArray().get(0).getAsString().equals(friend)){
				return true;
			}
		}		
		return false;
	}
	
	public static boolean getMuteGroupMemberWithRest(String groupID,String token,String friend){
		String url=resturl+appkey+"/chatgroups/"+groupID+"/mute";
		String  body=RestAPI.requestByGetMethod(url,token).get(1).toString();
		JsonParser parser=new JsonParser();
		JsonObject json=(JsonObject) parser.parse(body);
		if(json.get("data").getAsJsonArray().size()>0){
			if(json.get("data").getAsJsonArray().get(0).getAsJsonObject().get("user").getAsString().equals(friend)){
				return true;
			}
		}		
		return false;
	}
	public static void delFriendBlacklist(String username,String friend,String token){
		String url=resturl+appkey+"/users/"+username+"/blocks/users/"+friend;
		RestAPI.requestByDeleteMethod(url,token);
		
	}
	public static String requestPostWithBody(String url,String file,String token){
		JsonParser parser=new JsonParser();
		try {
			JsonObject json=(JsonObject) parser.parse(new FileReader(file));
			String result=RestAPI.requestByPostMethod(url, json, token).get(0).toString();
			return result;
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {	
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public WebElement findSpecialChatroom(WebDriver driver, String chatroomId, String path) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		String xpath = "//*[@id='chatrooms']/i[1]";
		WebElement ele = findElement(driver, xpath, path);
		if (ele.getAttribute("class").equals("webim-leftbar-icon font small")) {
			ele.click();
			sleep(1);
		}
		if (StringUtils.isNotBlank(chatroomId)) {
			logger.info("select chatroom: {}", chatroomId);
			xpath = "//div[@class='webim-contact-wrapper']/div[3]/div[@id='" + chatroomId + "']";
		} else {
			logger.info("select first chatroom");
			xpath = "//div[@class='webim-contact-wrapper']/div[3]/div[1]";
		}
		ele = findElement(driver, xpath, path);
		if (StringUtils.isNotBlank(ele.getAttribute("class"))) {
			ele.click();
			sleep(5);
		}
		return ele;
	}
	public void quitDriver(WebDriver driver){
		logger.info("quit driver");
		if (null != driver) {
			try {
				driver.quit();
			} catch (Exception e) {
				logger.error("Failed to quit driver2:", e);
			}
	    }
	}
	
	public String getScreenshotPath(String name) {
		return screenshotPath + "/" + name;
	}
	
	public void init(String filename) throws Exception
    {
       File inputXml = new File(new File(filename).getAbsolutePath());
       // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
       DocumentBuilder db = null;
       DocumentBuilderFactory dbf = null;
       try {
           // 返回documentBuilderFactory对象
           dbf = DocumentBuilderFactory.newInstance();
           // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
           db = dbf.newDocumentBuilder();
           // 得到一个DOM并返回给document对象
           doc = (Document)db.parse(inputXml);
          
          
         }
         catch (Exception e) {
              e.printStackTrace();
          }
       
    }
    
    @DataProvider(name="Test_xml_dataprovider")
    public  Object[][] providerMethod(Method method){  	
    	
       return new Object[][]{new Object[]{doc}};
    }
}
