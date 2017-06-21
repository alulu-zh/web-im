package com.easemob.webim.webim_test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.google.common.base.Preconditions;

public class WebIMTestBase {
	public static String PROPERTY_BASE_URL = "BASE_URL";
	public static String PROPERTY_INTERNAL_BASE_URL = "INTERNAL_BASE_URL";
	public static String PROPERTY_USER_NAME = "USER_NAME";
	public static String PROPERTY_INTERNAL_USER_NAME = "INTERNAL_USER_NAME";
	public static String PROPERTY_USER_PASSWORD = "USER_PASSWORD";
	public static String PROPERTY_INTERNAL_USER_PASSWORD = "INTERNAL_USER_PASSWORD";
	private static final String LOGIN_BUTTON_XPATH = "//div[@class='webim-sign']/button[@class='webim-button bg-color']";
    private static final String SETTING_BUTTON_XPATH = "//div[@class='webim-chat']/div[@class='webim-leftbar']/div/i[@class='webim-operations-icon font xsmaller']";
    private static final String RIGHT_SETTING_BUTTON_XPATH = "//div[@class='webim-chatwindow ']/div[2]/div/i[@class='webim-operations-icon font xsmaller']";
	private static final Logger logger = LoggerFactory.getLogger(WebIMTestBase.class);
	private static final long DEFAULT_TIMEOUT = 5L;
	protected WebDriver driver;
	protected String baseUrl;
	protected String username;
	protected String password;
	protected String screenshotPath = "target";
	protected String screenshotSuffix = "png";

	protected boolean isGetBaseUrl = true;

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
		}
		driver.manage().window().maximize();
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
		sleep(10);

		logger.info("check if login webim successfully");
		// xpath = "//a[@id='accordion1']";
		// WebElement ele = findElementByXpath(driver, xpath);
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
					"find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + msg);
		} catch (Exception e) {
			logger.error("Failed to find chat log: user1: {}, user2: {}, message: {}", username1, username2, msg, e);
			Assert.assertTrue(false,
					"find chat log: user1: " + username1 + ", user2: " + username2 + ", message: " + msg);
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
        xpath="//div/ul[@class='webim-operations']/li[4]";
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

	public String getRandomStr(int count) {
		return RandomStringUtils.randomAlphanumeric(count).toLowerCase();
	}
	public  WebElement findSpecialGroupName(WebDriver driver, String groupName,String path){
    	Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(groupName), "friend name was missing!");
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
}
