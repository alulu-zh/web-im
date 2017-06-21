
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
public class MuteGroupMemberTest extends WebIMTestBase{
	
	private static final Logger logger = LoggerFactory.getLogger(MuteGroupMemberTest.class);
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
	    init("src/test/java/com/easemob/webim/TestData/MuteGroupMember.xml");
	    resgisteUserWithRest("MuteGroupMember");
	    groupID=createGroupWithIndex("MuteGroupMember",2);
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void mutegroupmanager(Document params) throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    username=dr.readnodevalue(params, "mutegroupmember", "username");
		password=dr.readnodevalue(params, "mutegroupmember", "password");
		friend=dr.readnodevalue(params, "mutegroupmember", "friend");
		groupName=dr.readnodevalue(params, "mutegroupmember", "groupname");
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
	    			element.findElement(By.xpath("//li["+j+"]/div/i[@title='禁言']")).click();	
	    			break;
	    		}
	    		
	    	}
		 }
	    sleep(5);
	    if(getMuteGroupMemberWithRest(groupID,token,friend)){
		    logger.info("has mute group member {} ",friend);
		    logout(driver, xpath);
		 }else{
		    throw new Exception("mute group member failed");
		 }
	    
	   
	}
	@Test(enabled = true,dependsOnMethods = { "mutegroupmanager" })
	public void demutegroupmanager() throws Exception{
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
	    			element.findElement(By.xpath("//li["+j+"]/div/i[@title='解除禁言']")).click();	
	    			break;
	    		}
	    		
	    	}
		 }
	    sleep(5);
	    if(!getMuteGroupMemberWithRest(groupID,token,friend)){
		    logger.info("has demute group member {} ",friend);
		 }else{
		    throw new Exception("demute group member failed");
		 }
	    
	   
	}
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driver);
		deleteGroupWithRest(groupID, token);
        
	}
}
	    	
    

