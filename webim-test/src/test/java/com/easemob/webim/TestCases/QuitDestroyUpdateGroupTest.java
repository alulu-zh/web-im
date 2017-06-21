package com.easemob.webim.TestCases;

import java.io.FileReader;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Listeners({ WebIMBaseListener.class })
public class QuitDestroyUpdateGroupTest extends WebIMTestBase{
	
	private static final Logger logger = LoggerFactory.getLogger(QuitDestroyUpdateGroupTest.class);
	private String username;
	private String password;
	private String groupName;
	private String friend;
	private WebDriver driver2;
	private String groupID;
	private WebDriver driver;
	public DataReader dr;
	
	@BeforeClass(alwaysRun=true)
	public void setup() throws Exception{

		init_noPublicUsername();
	     //设置数据源
		dr=new DataReader();
	    init("src/test/java/com/easemob/webim/TestData/QuitDestroyUpdateGroupTest.xml");
	    logger.info("Begin initing test data!");
	    JsonParser parser=new JsonParser();
	    try {
	    	String file="src/test/java/com/easemob/webim/TestData/RestApiData.json";
	    	JsonObject json=(JsonObject)parser.parse(new FileReader(file));
	    	JsonArray jsonArray=(JsonArray)json.get("QuitDestroyUpdateData").getAsJsonArray();
	    	if (jsonArray.size()>0){
	    		//register group owner
	    		for(int i=0;i<2;i++){
	    			JsonObject register_json=jsonArray.get(i).getAsJsonObject();
		    		String register_url=resturl+appkey+"/users";
		    		String user=register_json.get("username").getAsString();
		    		System.out.println(user);
		    		if(IsUserExist(user)){
		    			logger.info("user exist!");	
		    		}else{
		    			RestAPI.requestByPostMethod(register_url, register_json, token);
			    		logger.info("Finish register!");
		    		}
	    		}
	    		
	    		//create group
	    		JsonObject create_group_json=jsonArray.get(2).getAsJsonObject();
	    		groupID=createGroupWithRest(create_group_json,token);
	    		logger.info("Finish creating group!");
	    		
	    		
	    	}
			
		} catch (JsonIOException e) {
			e.printStackTrace();
		}
	  } 
	@Test(enabled = true,dataProvider="Test_xml_dataprovider")
	public void quitGroup(Document params){
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    sleep(2);
	    username=dr.readnodevalue(params, "QuitDestroyUpdate", "username");
		password=dr.readnodevalue(params, "QuitDestroyUpdate", "password");
		friend=dr.readnodevalue(params, "QuitDestroyUpdate", "friend");
		groupName=dr.readnodevalue(params, "QuitDestroyUpdate", "groupname");
	    driver2=new ChromeDriver();
	    driver=new ChromeDriver();
	    super.login(driver2, friend, password, path, true);
	    findSpecialGroupName(driver2, groupName, path);
    	 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        WebElement ele = findElement(driver2, xpath,path);
        ele.click();
        sleep(5);
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span/span[2]";
        ele=findElement(driver2, xpath,path);
        ele.click();
        sleep(5);
        try{
    	findSpecialGroup(driver2,groupID,path);
        }
        catch (Exception e){
    	logger.info("quit group successfully!");
        } 
        quitDriver(driver2);    	
    }
	
	@Test(enabled = true, groups = { "group_operation" },dependsOnMethods = { "quitGroup" })
	 public void updateGroupInfo() throws Exception{
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    super.login(driver, username, password, path, true);
	    WebElement ele = findSpecialGroupName(driver, groupName, path);
	    //ele = findSpecialGroup(driver, groupid, path);
		//String groupID = ele.getAttribute("id");
   	 // click setting button
	    String xpath = RIGHT_SETTING_BUTTON_XPATH;
	    ele = findElement(driver, xpath,path);
	    ele.click();
       // click manage group button
        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
        WebElement element = findElement(driver, xpath,path);       
        logger.info("if hide?{}",element.getAttribute("class"));
        while(element.getAttribute("class")=="webim-operations hide"){
        	ele.click();
        }
        
       logger.info("if hide?{}",element.getAttribute("class"));
       xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li[2]/span";
       ele=findElement(driver, xpath,path);
       ele.click();        
       // check group list
       xpath="//div[@class='webim-dialog-body']/input[@placeholder='群组名']";
       String groupName_change="changed_group_name";
       element=findElement(driver,xpath,path);
       element.clear();
       element.sendKeys(groupName_change);
       xpath="//div[@class='webim-dialog-body']/input[@placeholder='群组简介']";
       element=findElement(driver,xpath,path);
       String groupDesc="changed_group_desc";
       element.clear();
       element.sendKeys(groupDesc);
       //save changes
       xpath="//button[@class='webim-button bg-color webim-dialog-button'][text()='确定']";
       element = findElement(driver, xpath,path);
       element.click();
       //check if have add members
   	   sleep(3);
   	   findSpecialGroupName(driver,groupName_change,path);
   	   logger.info("update group name successfully!");   	
   }
	
	@Test(enabled = true, groups = { "group_operation" }, dependsOnMethods = { "updateGroupInfo" })
	public void destroyGroup(){
	    String path = getScreenshotPath(Thread.currentThread().getStackTrace()[1].getMethodName());
	    sleep(5);
	    WebElement ele = findSpecialGroup(driver, groupID, path);
    	 // click setting button
        String xpath = RIGHT_SETTING_BUTTON_XPATH;
        ele = findElement(driver, xpath,path);
        ele.click();
        // click manage group button
        xpath = "//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']";
        WebElement element = findElement(driver, xpath,path);      
        logger.info("if hide?{}",element.getAttribute("class"));
        while(element.getAttribute("class")=="webim-operations hide"){
        	ele.click();
        }
        logger.info("if hide?{}",element.getAttribute("class"));
        xpath="//div[@class='webim-chatwindow ']/div[2]/div/ul[@tabindex='-1']/li/span/span[2]";
        ele=findElement(driver, xpath,path);
        ele.click();
        sleep(5);
        try{
    	findSpecialGroup(driver,groupID,path);
        }
        catch (Exception e){
    	logger.info("destroy group name successfully!");
        }     	
    }
	
	@AfterClass(alwaysRun=true)
	public void teardown(){
		deleteGroupWithRest(groupID,token);
		quitDriver(driver);
		quitDriver(driver2);
	    
	}

}
