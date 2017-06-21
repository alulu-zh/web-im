package com.easemob.webim.TestCases;

import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.easemob.webim.CommonFunction.WebIMTestBase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import junit.framework.AssertionFailedError;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class testVideo extends WebIMTestBase{
	private String username3="line002";
	private String password3="asd";
	private WebDriver driver2;
	protected String baseUrl="https://webim.easemob.com/index.html";
	protected String username="line001";
	protected String password="asd";
	private String groupID;
	private String msg;
	private static final String SETTING_BUTTON_XPATH = "//div[@class='webim-chat']/div[@class='webim-leftbar']/div/i[@class='webim-operations-icon font xsmaller']";
    private static final String RIGHT_SETTING_BUTTON_XPATH = "//div[@class='webim-chatwindow ']/div[2]/div/i[@class='webim-operations-icon font xsmaller']";

	private static final Logger logger = LoggerFactory.getLogger(testVideo.class);

	public void loginWebIM(){
		logger.info("Start to webim auto test on firefox...");
	
		//System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		driver = new ChromeDriver();
		//driver=new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver.get(baseUrl);
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
		
	}
	public void loginWebIMWithNewUser() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		driver2 = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		driver2.get(baseUrl);
		driver2.manage().window().maximize();
		sleep(5);
		logger.info("find username box and input username: {}", username);
		String xpath = "//input[@type='text']";
		WebElement usernameInput = findElementByXpath(driver2, xpath);
		if (null == usernameInput) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(usernameInput);
		usernameInput.clear();
		usernameInput.sendKeys(username3);

		logger.info("find password box and input password: {}", password3);
		xpath = "//input[@type='password']";
		WebElement passwordInput = findElementByXpath(driver2, xpath);
		if (null == passwordInput) {
			screenshot(driver2, getPath(path));
		}
		Assert.assertNotNull(passwordInput);
		passwordInput.clear();
		passwordInput.sendKeys(password3);

		logger.info("click login button");
		xpath = "//button[@class='webim-button bg-color']";
		WebElement login = findElementByXpath(driver2, xpath);
		if (null == login) {
			screenshot(driver2, getPath(path));
		}
		Assert.assertNotNull(login);
		login.click();
		
	}
	public void sendGroupMessage(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driver=new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
		//driver=new ChromeDriver();
		//super.login(driver, username, password, path, true);
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
	public void addgroupmembers(){
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    String addGroupMember="webimebs002";
	    List<String> members=new ArrayList<>();
	    members.add(addGroupMember);
	    logger.info("select first group");
	    sleep(5);
	    groupID="1481284627926";
	    String xpath = "//div[@class='webim-leftbar']/div[@id='groups']/i[@title='群组']";
		WebElement ele = findElement(driver, xpath, path);
		ele.click();
		xpath = "//div[@class='webim-contact-wrapper']/div[2]/div[@id='"+groupID+"']";
		ele = findElement(driver, xpath, path);
		if (StringUtils.isNotBlank(ele.getAttribute("id"))) {
			ele.click();
			sleep(3);
	   // WebElement ele = findSpecialGroup(driver, groupID, path);
		//String groupID = ele.getAttribute("id");
    	 // click setting button
         xpath = RIGHT_SETTING_BUTTON_XPATH;
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
        xpath="//div[@class='react-multi-select-box-container']/button[@class='react-multi-select-box']";
        element=findElement(driver,xpath,path);
        element.click();
        //add group member
        for(int i=0;i<members.size();i++){
            //add group members
     		 String str=members.get(i);
     		 xpath="//div[@class='react-multi-select-area'][@tabindex='0']";
     		 ele=findElement(driver,xpath,path);
     		 
     		 if(ele.getAttribute("class")=="react-multi-select-area react-multi-select-box-hidden"){
     			 element.click();
     		 }
     		 xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
    		 element = findElement(driver,xpath,path);
    		 sleep(3);
     		 element.click();
    		     		// element.click();
     		 /*WebDriverWait wait=new WebDriverWait(driver, 5);
    		 ExpectedCondition<Boolean> con1=new ExpectedCondition<Boolean>(){
    			 public Boolean apply(WebDriver d) {
    				 String xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
    	     		 WebElement element = findElement(driver,xpath,path);
    	     		 if (element.getAttribute("class")=="react-multi-select-box-option"){
    	            	 element.click();
    	     		 }
    	    		return (element.getAttribute("class")=="react-multi-select-box-option react-multi-select-box-option-selected");	       
    		 }
    		 };
    		 wait.until(con1);*/
    		 
            /* if (element.getAttribute("class")=="react-multi-select-box-option"){
            	 element.click();
             }
     		 if(element.getAttribute("class")=="react-multi-select-box-option react-multi-select-box-option-selected"){
     			logger.info("the {}st member {} is added",i+1,str); 
     		 }*/
            
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
    	findGroupMembers(driver,groupID,path,members);
    }
	}
	public boolean findGroupMembers(WebDriver driver,String groupID,String path,List<String> members){
    	logger.info("find special group!");
    	findSpecialGroup(driver,groupID,path);
    	String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-down-icon']";
    	WebElement element=findElement(driver,xpath,path);
    	element.click();
    	sleep(5);
    	for (int i=0;i<members.size();i++){
    		String member=members.get(i);
    		xpath="//div[@class='webim-chatwindow ']/ul[@class='webim-group-memeber']/li/span[text()='"+member+"']";
    		element=findElement(driver,xpath,path);
    	}
    	logger.info("add group member success!");
    	return true;
	}
	public void singleVideo(){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, isGetBaseUrl);
		logger.info("find special friend: {}", username3);
		WebElement ele = findSpecialFriend(driver, username3, path);
		sleep(3);
		logger.info("click video sent button");
		String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-send-wrapper']/div[@class='webim-chatwindow-options']/span[5]";

		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(10);
		logger.info("check if the window of video is opened");
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
		super.login(driver2, username3, password3, path, isGetBaseUrl);
		
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
		findElement(driver2, xpath, path).click();
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
	public void destroyGroup(){
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    sleep(5);
	    groupID="1481286461495";
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
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span[text()='解散群组']";
        ele=findElement(driver, xpath,path);
        ele.click();
        sleep(5);
        try{
    	findSpecialGroup(driver,groupID,path);
        }
        catch (AssertionError e){
    	logger.info("destroy group name successfully!");
        } 
    	
    }
	public void quitGroup(){
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	   sleep(5);
	    //System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
	    //String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	   // driver2=new ChromeDriver();
	    //driver2.get(baseUrl);
		//driver2.manage().window().maximize();
	    WebElement ele = findSpecialGroup(driver, "1482389651919", path);
		//String groupID = ele.getAttribute("id");
    	 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        ele = findElement(driver, xpath,path);
        ele.click();
        sleep(3);
        // click manage group button
        //xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
       // WebElement element = findElement(driver, xpath,path);
       
        /*logger.info("if hide?{}",element.getAttribute("class"));
        if(element.getAttribute("class")=="webim-operations hide"){
        	ele.click();
        }*/
       // logger.info("if hide?{}",element.getAttribute("class"));
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span[text()='退出群组']";
        ele=findElement(driver, xpath,path);
        ele.click();
        sleep(5);
        try{
    	findSpecialGroup(driver,"1482389651919",path);
        }
        catch (Exception e){
    	logger.info("quit group successfully!");
        } 
    	
    }
	public void sendfile(){
		sleep(3);
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		String xpath = "//div[@id='friends']";
		WebElement ele = findElement(driver, xpath,path);
		ele.click();
		sleep(3);
		xpath = "//div[@id='" + username3 + "']";
		ele = findElement(driver, xpath, path);
		if (StringUtils.isNotBlank(ele.getAttribute("class"))) {
			ele.click();
		}
		xpath="//div[@class='webim-chatwindow-options']/span[4][@class='webim-file-icon font smaller']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(3);
		Runtime rn = Runtime.getRuntime();
		try{
		String str = "C://Users/gaozhq/Documents/test.exe" ;
		rn.exec(str);
		} catch (Exception e){
		System.out.println("Error to run the exe");
		}
		sleep(3);
		driver.quit();
		}
		
	
	 public static void main(String[] args){
		 Map map=new HashMap<>();
		 map.put("username", "gzq");
		 map.put("password", "value");
		 
		 JsonParser parser=new JsonParser();
		 //JsonObject jsonObject=parser.parse(map);
		 
	
	
		
		 
	 }
}
