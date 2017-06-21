package com.easemob.webim.TestCases;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
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
public class FriendBlacklistTest extends WebIMTestBase {
	private static final Logger logger = LoggerFactory.getLogger(FriendBlacklistTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driver;
	public DataReader dr;
	
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{
		
		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/FriendBlacklistTest.xml");
	    resgisteUserWithRest("FriendBlacklistData");
	    
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void addToBlacklist(Document params){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		username=dr.readnodevalue(params, "friendblacklist", "username");
		password=dr.readnodevalue(params, "friendblacklist", "password");
		friend=dr.readnodevalue(params, "friendblacklist", "friend");
		addFriendWithRest(username,friend,token);
		driver=new ChromeDriver();
		super.login(driver, username, password, path, true);
		findSpecialFriend(driver,friend,path);
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
    		xpath = "//div[@id='" + friend + "']";
        	findElementByXpath(driver, xpath);
        	throw new Exception("add to blacklist failed");      	
        }catch(Exception e){
        	logger.info("has add {} to blacklist",friend);
        }        
	}
	@Test(enabled = true, groups = { "friendblacklist" }, dependsOnMethods = { "addToBlacklist" })
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
	
	@Test(enabled = true, groups = { "friendblacklist" }, dependsOnMethods = { "getFriendBlacklist" })
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
    		if(StringUtils.contains(text,friend)){
    			int j=i+1;
    			elements.get(i).findElement(By.xpath("//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']/li["+j+"]/i[@class='webim-leftbar-icon font smaller']")).click();
    			logger.info("has remove {} frome blacklist",friend);
    		}   		
    	}
    	//click OK button
    	xpath = "//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
    	element = findElement(driver, xpath,path);
    	element.click();
        findSpecialFriend(driver,friend,path);
	}
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		delFriendBlacklist(username,friend,token);
		
        
	}
	

}
