package com.easemob.webim.CommonFunction;
import java.io.File;
import java.lang.reflect.Method;
//import org.testng.annotations.DataProvider;
import org.w3c.dom.*; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; 
import org.testng.annotations.DataProvider;

public class DataProvide {
	
	public static Document doc;
	
    public  static void init(String filename) throws Exception
    {
       File inputXml = new File(new File(filename).getAbsolutePath());
       // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
       DocumentBuilder db = null;
       DocumentBuilderFactory dbf = null;
       try {
           // 返回documentBuilderFactory对象
           dbf = DocumentBuilderFactory.newInstance();
           // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
           db = dbf.newDocumentBuilder();
           // 得到一个DOM并返回给document对象
           doc = (Document)db.parse(inputXml);
          
          
         }
         catch (Exception e) {
              e.printStackTrace();
          }
       
    }
    
    @DataProvider(name="Test_xml_dataprovider")
    public static Object[][] providerMethod(Method method){
    	
    	
       return new Object[][]{new Object[]{doc}};
    }

}
