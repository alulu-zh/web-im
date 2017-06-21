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
import com.easemob.webim.CommonFunction.RestAPI;
import com.easemob.webim.CommonFunction.WebIMBaseListener;
import com.easemob.webim.CommonFunction.WebIMTestBase;

@Listeners({ WebIMBaseListener.class })
public class GroupBlacklistTest extends WebIMTestBase{
	
	private static final Logger logger = LoggerFactory.getLogger(GroupBlacklistTest.class);
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
	    init("src/test/java/com/easemob/webim/TestData/GroupBlacklistTest.xml");
	    resgisteUserWithRest("GroupBlacklistData");
	    groupID=createGroupWithIndex("GroupBlacklistData",2);
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void addgroupblacklist(Document params) throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    username=dr.readnodevalue(params, "groupblacklist", "username");
		password=dr.readnodevalue(params, "groupblacklist", "password");
		friend=dr.readnodevalue(params, "groupblacklist", "friend");
		groupName=dr.readnodevalue(params, "groupblacklist", "groupname");
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
	    		logger.info("blacklist friend value of getText {}",text);
	    		if(StringUtils.contains(text,member)){
	    			int j=i+1;
	    			element.findElement(By.xpath("//li["+j+"]/div/i[@title='加入群黑名单']")).click();	
	    			break;
	    		}
	    		
	    	}
		 }
	    try{
	    	if(getgroupblacklist(groupID,friend,driver)){
		    	logger.info("has add {} to blacklist",friend);
		    }else{
		    	throw new Exception("add group blacklist failed");
		    }
	    }
	    finally {
	    	xpath="//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
	        findElement(driver, xpath,path).click();
		}
	    
        
	    	
    }
	
	@Test(enabled = true, groups = { "group_blacklist" }, dependsOnMethods = { "addgroupblacklist" })
	public void removegroupblacklist() throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    sleep(5);
	    List<String> members=new ArrayList<>();
	    members.add(friend);
    	 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        WebElement ele = findElement(driver, xpath,path);
        ele.click();
        // click manage group button
        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
        WebElement element = findElement(driver, xpath,path);       
        logger.info("if hide?{}",element.getAttribute("class"));
        while(element.getAttribute("class")=="webim-operations hide"){
        	ele.click();
        }
        logger.info("if hide?{}",element.getAttribute("class"));
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span[text()='群组黑名单']";
        ele=findElement(driver, xpath,path);
        ele.click();
        sleep(2);
        xpath="//div[@class='webim-dialog-body']/ul[@class='webim-blacklist-wrapper']";
        ele=findElement(driver, xpath,path);
        List<WebElement> elements=ele.findElements(By.xpath("//ul[@class='webim-blacklist-wrapper']/li"));
        for(int i=0;i<members.size();i++){
        	for(int k=0;k<elements.size();k++){
        		String member=members.get(i);
        		if(StringUtils.contains(elements.get(k).getText(), member)){
        			int j=k+1;
                	ele.findElement(By.xpath("//li["+j+"]/i[@class='webim-leftbar-icon font smaller']")).click();
                	break;
                }
        	}
        }
        xpath="//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
        findElement(driver, xpath,path).click();
        if(!getgroupblacklist(groupID,friend,driver)){
        	 logger.info("remove group blacklist member successfully!");
        	 xpath="//div[@class='webim-dialog-footer']/button[@class='webim-button bg-color webim-dialog-button']";
             findElement(driver, xpath,path).click();
             //xpath="//div[@class='webim-chatwindow ']/div[@class='webim-chatwindow-title']/i[@class='webim-down-icon font smallest  dib webim-up-icon']";
         	 //element=findElement(driver,xpath,path);
         	// element.click();
        	
        }else{
        	throw new Exception("remove group blacklist member failed!");
        }
        logger.info("readd groupmember {} to group {}",friend,groupName);
        String addMember_url=resturl+appkey+"/chatgroups/"+groupID+"/users/"+friend;
        RestAPI.requestByPostMethod(addMember_url, null, token);
        logger.info("readd {} to group  successfully",friend);
       
    }
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		deleteGroupWithRest(groupID, token);
        
	}
	

}
