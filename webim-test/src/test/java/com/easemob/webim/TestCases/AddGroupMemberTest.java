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
public class AddGroupMemberTest extends WebIMTestBase {
	
	private static final Logger logger = LoggerFactory.getLogger(AddGroupMemberTest.class);
	private String username;
	private String password;
	private String groupName;
	private String add_groupmember;
	private String groupID;
	private WebDriver driverAddGroupMember;
	public DataReader dr;
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{

		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/AddGroupMemberTest.xml");
	    resgisteUserWithRest("AddGroupMemberData");
	    groupID=createGroupWithIndex("AddGroupMemberData",3);
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	 public void addgroupmembers(Document params) throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    username=dr.readnodevalue(params, "addgroupmember", "username");
		password=dr.readnodevalue(params, "addgroupmember", "password");
		add_groupmember=dr.readnodevalue(params, "addgroupmember", "friend");
		groupName=dr.readnodevalue(params, "addgroupmember", "groupname");
		addFriendWithRest(username,add_groupmember,token);
		driverAddGroupMember=new ChromeDriver();
	    super.login(driverAddGroupMember, username, password, path, true);
	    List<String> members=new ArrayList<>();
	    members.add(add_groupmember);	
	    findSpecialGroupName(driverAddGroupMember, groupName, path);
		String xpath="//div/div[@class='webim-contact-item selected']";
		WebElement ele=findElement(driverAddGroupMember,xpath,path);
		groupID=ele.getAttribute("id");
	    findSpecialGroup(driverAddGroupMember, groupID, path);
		//String groupID = ele.getAttribute("id");
    	 // click setting button
        xpath = RIGHT_SETTING_BUTTON_XPATH;
        ele = findElement(driverAddGroupMember, xpath,path);
        ele.click();
        // click manage group button
        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
        WebElement element = findElement(driverAddGroupMember, xpath,path);	       
        logger.info("if hide?{}",element.getAttribute("class"));
        while(element.getAttribute("class")=="webim-operations hide"){
        	ele.click();
        }
        logger.info("if hide?{}",element.getAttribute("class"));
       // ele=driverAddGroupMember.findElement(By.xpath("//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span[text()='管理群成员']"));
       // ele.click();
        element.findElement(By.xpath("//li/span[text()='管理群成员']")).click();
        //element.click();
        sleep(5);	        
        // check group list
        xpath="//div[@class='react-multi-select-box-container']/button[@class='react-multi-select-box']";
        //xpath="//div[@class='react-multi-select-box-container react-multi-select-box-empty']/button[@class='react-multi-select-box']";
        element=findElement(driverAddGroupMember,xpath,path);
        element.click();
        //add group member
        for(int i=0;i<members.size();i++){
            //add group members
     		 String str=members.get(i);
     		 xpath="//div[@class='react-multi-select-area'][@tabindex='0']";
     		 ele=findElement(driverAddGroupMember,xpath,path);
     		 if (ele.getAttribute("class")=="react-multi-select-area react-multi-select-box-hidden"){
     			 element.click();
     		 }
     		 xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
     		 element = findElement(driverAddGroupMember, xpath,path);
             element.click();
             
             if (element.getAttribute("class")=="react-multi-select-box-option react-multi-select-box-option"){
            	 logger.info("the {}st member {} is added",i+1,str);
             }	            
            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
            element = findElement(driverAddGroupMember, xpath,path);
            element.click();
             sleep(1);
     		}	
        xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='确定']";
        element = findElement(driverAddGroupMember, xpath,path);
        element.click();
        //check if have add members
    	sleep(3);
    	findGroupMembers(driverAddGroupMember,groupID,path,members);
    }
	@Test(enabled = true, groups = { "add_groupmember" }, dependsOnMethods = { "addgroupmembers" })
	 public void delgroupmembers() throws Exception{		
		 String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
		    //String addGroupMember="webimebs002";
		    List<String> members=new ArrayList<>();
		    members.add(add_groupmember);
		    logger.info("select first group");		    
		    WebElement ele = findSpecialGroup(driverAddGroupMember, groupID, path);
			//String groupID = ele.getAttribute("id");
	    	 // click setting button
	        String xpath = RIGHT_SETTING_BUTTON_XPATH;
	        ele = findElement(driverAddGroupMember, xpath,path);
	        ele.click();
	        // click manage group button
	        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
	        WebElement element = findElement(driverAddGroupMember, xpath,path);	       
	        logger.info("if hide?{}",element.getAttribute("class"));
	        while(element.getAttribute("class")=="webim-operations hide"){
	        	ele.click();
	        }
	        logger.info("if hide?{}",element.getAttribute("class"));
	        element.findElement(By.xpath("//li/span[text()='管理群成员']")).click();
	        sleep(2);	        
	        // check group list
	        xpath="//div[@class='react-multi-select-box-container']/button[@class='react-multi-select-box']";
	        element=findElement(driverAddGroupMember,xpath,path);
	        element.click();
	        //add group member
	        for(int i=0;i<members.size();i++){
	            //add group members
	     		 String str=members.get(i);
	     		 xpath="//div[@class='react-multi-select-area'][@tabindex='0']";
	     		 ele=findElement(driverAddGroupMember,xpath,path);
	     		 if (ele.getAttribute("class")=="react-multi-select-area react-multi-select-box-hidden"){
	     			 element.click();
	     		 }
	     		 xpath="//div[@class='react-multi-select-area']/div[@class='react-multi-select-panel']/ul[@class='react-multi-select-col']/li[@class='react-multi-select-list-option']/div[@label='"+str+"']";
	     		 element = findElement(driverAddGroupMember, xpath,path);
	             element.click();
	             if (element.getAttribute("class")=="react-multi-select-box-option"){
	            	 logger.info("the {}st member {} has deleted",i+1,str);
	             }
	            xpath="//div[@class='react-multi-select-area-btn']/button[@class='react-multi-select-btn'][text()='ok']";
	            element = findElement(driverAddGroupMember, xpath,path);
	            element.click();
	             sleep(1);
	     		}	
	        xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='确定']";
	        element = findElement(driverAddGroupMember, xpath,path);
	        element.click();
	        //check if have add members
	    	sleep(3);
	    	if (!findGroupMembers(driverAddGroupMember,groupID,path,members)){
	    		logger.info("delete group member successfully!");
	    	}else{
	    		throw new Exception("delete group member failed!");
	    	}    	     
	    }
	
	@AfterClass(alwaysRun=true)
		public void teardown(){
			quitDriver(driverAddGroupMember);
			
			deleteGroupWithRest(groupID, token);
			
		    
		}


}
