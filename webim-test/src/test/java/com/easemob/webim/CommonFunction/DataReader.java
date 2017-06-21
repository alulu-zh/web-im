package com.easemob.webim.CommonFunction;
import org.w3c.dom.*; 
public class DataReader {
	 public   String  readnodevalue(Document params,String firsttag,String secondtag)
	    {
		 try{
			   String result="";
		       Element root=params.getDocumentElement();
		       NodeList childnode = root.getElementsByTagName(firsttag);
		       NodeList resnode=childnode.item(0).getChildNodes();
		       for(int i=0;i<resnode.getLength();i++)
		       {
		           if(resnode.item(i).getNodeName().equals(secondtag))
		           {
		              result=resnode.item(i).getTextContent();
		              break;
		           }
		       }         
		       return result;          
		      
		 }catch(NullPointerException e){
			 e.printStackTrace();
		 }
			return null; 
		 }
	      

}
