package com.easemob.webim.TestCases;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import com.easemob.webim.CommonFunction.DataReader;
import com.easemob.webim.CommonFunction.WebIMBaseListener;
import com.easemob.webim.CommonFunction.WebIMTestBase;

@Listeners({ WebIMBaseListener.class })
public class CreateGroupTest extends WebIMTestBase{
	private static final Logger logger = LoggerFactory.getLogger(CreateGroupTest.class);
	private String username;
	private String password;
	private String groupName;
	private String friend;
	private String groupID;
	private WebDriver driverCreateGroup;
	private DataReader dr;
	@BeforeClass(alwaysRun = true)
	public void setup() throws Exception{

		init_noPublicUsername();
	     //设置数据源
		
	    init("src/test/java/com/easemob/webim/TestData/CreateGroupTest.xml");
	    resgisteUserWithRest("CreateGroupData");
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	 public void creatGroupswithmembers(Document params){
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		dr=new DataReader();
		if(null!=dr){
			username=dr.readnodevalue(params, "creategroup", "username");
			password=dr.readnodevalue(params, "creategroup", "password");
			friend=dr.readnodevalue(params, "creategroup", "friend");
		}
		
		addFriendWithRest(username,friend,token);
		driverCreateGroup=new ChromeDriver();
		super.login(driverCreateGroup, username, password, path, true);
		groupName="testGroup-"+getRandomStr(8);
		String style="joinOpen";//ownerInvite,memberInvite,joinApproval,joinOpen
		List<String> members=new ArrayList<String>();
		members.add(friend);
		 //create groups 
   	    String xpath = SETTING_BUTTON_XPATH;
        WebElement element = findElement(driverCreateGroup, xpath,path);
        element.click();
        // click create group button
        xpath = "//ul[@class='webim-operations']/li/span[text()='创建群组']";
        element = findElement(driverCreateGroup, xpath,path);
        element.click();
       // input group name
        xpath = "//div[@class='webim-dialog webim-dialog-2']/div/input[@class='webim-input'][@type='text'][@placeholder='群组名']";
        inputText(driverCreateGroup, xpath, groupName,path);
        //check group style
        checkGroupStyle(driverCreateGroup,style,path);
        // click add member button
        logger.info("Begin click add member button");
        for(int i=0;i<members.size();i++){
       	    xpath = "//div[@id='react-multi-select-box-1']/button[@class='react-multi-select-box']/div[@class='react-multi-select-box-label']";
            element = findElement(driverCreateGroup, xpath,path);
            element.click();
            //add group members
     		String str=members.get(i);   		
     		xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@class='react-multi-select-box-option'][@label='"+str+"']";
     		element = findElement(driverCreateGroup, xpath,path);
            element.click();
            logger.info("the {}st member {} is added",i+1,str);
            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
            element = findElement(driverCreateGroup, xpath,path);
            element.click();
            sleep(1);
     		}	
        // click create group button
        sleep(3);
        xpath="//button[@class='webim-button bg-color webim-dialog-button-left'][text()='添加']";
        element = findElement(driverCreateGroup,xpath,path);
        element.click();
        sleep(5);
        findSpecialGroupName(driverCreateGroup,groupName,path); 
        xpath="//div/div[@class='webim-contact-item selected']";
		WebElement ele=findElement(driverCreateGroup,xpath,path);
		groupID=ele.getAttribute("id");
        
	}
	
	@Test(enabled = true, groups = { "create_group" }, dependsOnMethods = { "creatGroupswithmembers"  })
	public void getGroupList() {
		String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		//driverCreateGroup=new ChromeDriver();
		super.login(driverCreateGroup, username, password, path, true);
		logger.info("get group list");
		String xpath = "//div[@class='webim-contact-wrapper']";
		WebElement ele = findElement(driverCreateGroup, xpath, path);
		List<WebElement> li = ele.findElements(By.xpath("//div[2]/div"));
		Assert.assertTrue(null != li && li.size() > 0, "hasn't found groups");
	}
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		quitDriver(driverCreateGroup);
		deleteGroupWithRest(groupID, token);
		
        
	}

}
