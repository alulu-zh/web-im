
package com.easemob.webim.TestCases;

import java.util.ArrayList;
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
public class SetGroupManagerTest extends WebIMTestBase{
	
	private static final Logger logger = LoggerFactory.getLogger(SetGroupManagerTest.class);
	private String username;
	private String password;
	private String groupName;
	private String friend;
	private String groupID;
	private WebDriver driver;
	public DataReader dr;
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{

		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/SetGroupManager.xml");
	    resgisteUserWithRest("GroupManagerData");
	    groupID=createGroupWithIndex("GroupManagerData",2);
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void setgroupmanager(Document params) throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    username=dr.readnodevalue(params, "groupmanager", "username");
		password=dr.readnodevalue(params, "groupmanager", "password");
		friend=dr.readnodevalue(params, "groupmanager", "friend");
		groupName=dr.readnodevalue(params, "groupmanager", "groupname");
		driver=new ChromeDriver();
		super.login(driver, username, password, path, true);
	    List<String> members=new ArrayList<>();
	    members.add(friend);
	    findSpecialGroupName(driver, groupName, path);
	    String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-down-icon']";
	    WebElement element=findElement(driver,xpath,path);
	    element.click();
        xpath="//div[@class='webim-chatwindow ']/ul[@class='webim-group-memeber']";
	    element=findElement(driver,xpath,path);
	    List<WebElement> elements = element.findElements(By.xpath("//ul[@class='webim-group-memeber']/li"));
	    logger.info("size of elements is {}",elements.size());	    
	    for(int k=0;k<members.size();k++){
		    for(int i=1;i<elements.size();i++){
		    	String member=members.get(k);
	    		String text=(String)elements.get(i).getText();
	    		logger.info(" friend value of getText {}",text);
	    		if(StringUtils.contains(text,member)){
	    			int j=i+1;
	    			element.findElement(By.xpath("//li["+j+"]/div/i[@title='设为管理员']")).click();	
	    			break;
	    		}
	    		
	    	}
		 }
	    sleep(5);
	    if(getGroupManagerWithRest(groupID,token,friend)){
		    logger.info("has set {} as manager ",friend);
		    logout(driver, xpath);
		 }else{
		    throw new Exception("set group manager failed");
		 }
	    
	   
	}
	@Test(enabled = true,dependsOnMethods = { "setgroupmanager" })
	public void delgroupmanager() throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		super.login(driver, username, password, path, true);
	    List<String> members=new ArrayList<>();
	    members.add(friend);
	    findSpecialGroupName(driver, groupName, path);
	    String xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-down-icon']";
	    WebElement element=findElement(driver,xpath,path);
	    element.click();
        xpath="//div[@class='webim-chatwindow ']/ul[@class='webim-group-memeber']";
	    element=findElement(driver,xpath,path);
	    List<WebElement> elements = element.findElements(By.xpath("//ul[@class='webim-group-memeber']/li"));
	    logger.info("size of elements is {}",elements.size());	    
	    for(int k=0;k<members.size();k++){
		    for(int i=1;i<elements.size();i++){
		    	String member=members.get(k);
	    		String text=(String)elements.get(i).getText();
	    		logger.info(" friend value of getText {}",text);
	    		if(StringUtils.contains(text,member)){
	    			int j=i+1;
	    			element.findElement(By.xpath("//li["+j+"]/div/i[@title='移除管理员']")).click();	
	    			break;
	    		}
	    		
	    	}
		 }
	    sleep(5);
	    if(!getGroupManagerWithRest(groupID,token,friend)){
		    logger.info("has delete manager {} ",friend);
		 }else{
		    throw new Exception("delete group manager failed");
		 }
	    
	   
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		deleteGroupWithRest(groupID, token);
        
	}
}
	    	
    
