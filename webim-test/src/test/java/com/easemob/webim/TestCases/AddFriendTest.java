package com.easemob.webim.TestCases;


import java.net.MalformedURLException;
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
public class AddFriendTest extends WebIMTestBase {
	private static final Logger logger = LoggerFactory.getLogger(AddFriendTest.class);
	private String username;
	private String password;
	private String friend;
	private WebDriver driverAddFriend;
	private WebDriver driver2;
	private DataReader dr2;
	
	    
	    @BeforeClass(alwaysRun=true)
		public void setup() throws Exception{

			init_noPublicUsername();
		     //设置数据源
		    resgisteUserWithRest("AddFriendData");
		    init("src/test/java/com/easemob/webim/TestData/AddFriendTest.xml");
		   
		    
		  } 

	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void addFriend(Document params) throws Exception{
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		dr2=new DataReader();
		
		System.out.println("the param is"+params);
		if(null==params){
    		System.out.println("document is null");
    	}else{
    		System.out.println("document is not null");
    	}
		logger.info("dr2 object is {}",dr2);
		username=dr2.readnodevalue(params, "addFriend", "username");
		logger.info("params is {}",params);
		password=dr2.readnodevalue(params, "addFriend", "password");
		friend=dr2.readnodevalue(params, "addFriend", "friend");
		
		
		logger.info("click add friend button");
		driverAddFriend=new ChromeDriver();
		super.login(driverAddFriend, username, password, path, true);
		// if there is addfriend dialog box,reject first
		String xpath="//button[@class='webim-button bg-color error webim-subscribe-button'][text()='拒绝']";
		if(null!=findElementByXpath(driverAddFriend,xpath )){
			findElement(driverAddFriend, xpath, path).click();
		}
		xpath = SETTING_BUTTON_XPATH;
		WebElement ele = findElement(driverAddFriend, xpath, path);
		ele.click();
		sleep(1);
		xpath = "//ul[@class='webim-operations']/li/span[text()='添加好友']";
		ele = findElement(driverAddFriend, xpath, path);
		ele.click();
		sleep(3);
		//addSpecialFriend(friendToAdd);
		logger.info("input friend id: {}", friend);
		xpath = "//div[@class='webim-dialog']/div/input[@class='webim-input'][@type='text'][@placeholder='用户名']";
		ele = findElement(driverAddFriend, xpath, path);
		ele.clear();
		ele.sendKeys(friend);
		sleep(1);
		logger.info("click add button");
		xpath = "//div[@class='webim-dialog']/button[@class='webim-button bg-color webim-dialog-button'][text()='添加']";
		ele = findElement(driverAddFriend, xpath, path);
		ele.click();
		sleep(3);		
	}
	
	@Test(enabled = true,dependsOnMethods = { "addFriend" })
	public void loginWebIMWithNewUser() throws MalformedURLException {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		
        driver2=new ChromeDriver();
		super.login(driver2, friend, password, path, true);
	}
	
	@Test(enabled = true,dependsOnMethods = { "loginWebIMWithNewUser" })
	public void receiveAddFriendConfirmMsg() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		String xpath = "//*[@id='"+username+"']/button[1]";
		WebElement ele = findElement(driver2, xpath, path);
		ele.click();
		sleep(3);
		logger.info("find new friend: {}", username);
		xpath = "//*[@id='"+username+"']";
		ele = findElement(driver2, xpath, path);
				
	}
	
	@Test(enabled = true, groups = { "add_friend" },dependsOnMethods = { "receiveAddFriendConfirmMsg" })
	public void deleteFriend() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driverAddFriend, username, password, path, isGetBaseUrl);
		findSpecialFriend(driverAddFriend,friend,path);
		logger.info("click set button");
		 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        WebElement element = findElement(driverAddFriend, xpath,path);
        element.click();
        // click deleteFriend button
        xpath = "//ul[@class='webim-operations ']/li/span[text()='删除好友']";
        element = findElement(driverAddFriend, xpath,path);
        element.click();
        try{
        	findSpecialFriend(driverAddFriend,friend,path);
        }catch(Exception e){
        	logger.info("friend {} has been deleted！",friend);
        	
        }
       
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driverAddFriend);
		quitDriver(driver2);
		deleteFriendWithRest(username,friend,token);
        
	}
	

}
