
package com.easemob.webim.TestCases;

import java.util.ArrayList;
import java.util.List;

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
public class ApplyJoinGroupTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(ApplyJoinGroupTest.class);
	private String username;
	private String password;
	private String friend;
	private String groupID;
	private WebDriver driver;
	private DataReader dr;
	@BeforeClass(alwaysRun = true)
	public void setup() throws Exception{

		init_noPublicUsername();
	     //设置数据源
		
	    init("src/test/java/com/easemob/webim/TestData/ApplyJoinGroup.xml");
	    resgisteUserWithRest("JoinPublicGroupData");
	    groupID=createGroupWithIndex("JoinPublicGroupData",2);
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	 public void joinPublicGroup(Document params) throws Exception{
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		dr=new DataReader();
		if(null!=dr){
			username=dr.readnodevalue(params, "joingroup", "username");
			password=dr.readnodevalue(params, "joingroup", "password");
			friend=dr.readnodevalue(params, "joingroup", "friend");
		}
		
		driver=new ChromeDriver();
		super.login(driver, friend, password, path, true);
		 //click join public group button
   	    String xpath = SETTING_BUTTON_XPATH;
        WebElement element = findElement(driver, xpath,path);
        element.click();
        xpath = "//ul[@class='webim-operations']/li/span[text()='申请加入公开群']";
        element = findElement(driver, xpath,path);
        element.click();
       // input groupID
        xpath = "//div[@class='webim-friend-options']/div[2]/div/input[@class='webim-input'][@type='text']";
        
        inputText(driver, xpath, groupID,path);
       	//click search group button
        xpath = "//div[@class='webim-friend-options']/div[2]/div/button[@class='webim-button bg-color webim-dialog-button-search']";
        element = findElement(driver, xpath,path);
        element.click();
        // click join group button
        sleep(3);
        xpath="//div[@class='webim-dialog']/div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
        element = findElement(driver,xpath,path);
        element.click();
        sleep(5);
        //click close window button
        xpath="//div[@class='webim-dialog']/span[@class='font']";
		WebElement ele=findElement(driver,xpath,path);
		ele.click();
		logout(driver, xpath);
		login(driver, username, password, path, false);
		xpath="//div[@class='webim-dialog-footer']/button[2]";
		findElement(driver,xpath,path).click();
		List<String> members=new ArrayList<String>();
		members.add(friend);
		boolean result=findGroupMembers(driver, groupID, path,members);
		if(result){
			logger.info("join group successfully");
		}else{
			logger.info("join group failed");
			throw new Exception("join group failed");
		}
        
	}
	@Test(enabled = true,dependsOnMethods={"joinPublicGroup"})
	 public void getPublicGroup() throws Exception{
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, friend, password, path, false);
		 //click join public group button
  	    String xpath = SETTING_BUTTON_XPATH;
       WebElement element = findElement(driver, xpath,path);
       element.click();
       xpath = "//ul[@class='webim-operations']/li/span[text()='申请加入公开群']";
       element = findElement(driver, xpath,path);
       element.click();
       xpath="//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']";
       element = findElement(driver, xpath,path);
       List list=element.findElements(By.xpath("//li"));
  
		if(list.size()>0){
			logger.info("get public group successfully");
		}else{
			logger.info("get group failed");
			throw new Exception("get group failed");
		}
       
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		deleteGroupWithRest(groupID, token);
		
        
	}
}
	