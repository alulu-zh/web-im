package com.easemob.webim.CommonFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import ch.qos.logback.classic.Logger;



public class RestAPI {
	private static CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }
     
    private static void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }
    
    public static List<String> requestByPostMethod(String url,JsonObject jsonParam,String token){
        CloseableHttpClient httpClient = getHttpClient();
        HttpResponse result;
        try {
        	HttpPost post = new HttpPost(url);
        	if(null!=jsonParam){
        		 StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                 entity.setContentEncoding("UTF-8");
                 entity.setContentType("application/json");
                 post.setEntity(entity);
        	}
            //url格式编码
            post.setHeader("Authorization", token);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            result = httpClient.execute(post); 
            List<String> response=new ArrayList<String>();
            String body=EntityUtils.toString(result.getEntity());
            String status=String.valueOf(result.getStatusLine().getStatusCode());
            response.add(status);
            response.add(body);
            System.out.println("响应状态码:"+status );
            System.out.println("-------------------------------------------------");
            System.out.println("响应内容:" + body);
            System.out.println("-------------------------------------------------");
            return response;
            
        }
        
	    catch (Exception e) {
	        e.printStackTrace();
	    }
        System.out.println("Exception occured!");
        return null;
           
        }
      
	public  static List<String> requestByGetMethod(String url,String token){
        //创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
		HttpResponse result;
		
        try {
            //用get方法发送http请求
            HttpGet get = new HttpGet(url);
            System.out.println("执行get请求:...."+get.getURI());
            
            get.setHeader("Authorization", token);
            //发送get请求
            result = httpClient.execute(get);
                //response实体
            List<String> response=new ArrayList<String>();
            String body=EntityUtils.toString(result.getEntity());
            String status=String.valueOf(result.getStatusLine().getStatusCode());
            response.add(status);
            response.add(body);
            System.out.println("响应状态码:"+status );
            System.out.println("-------------------------------------------------");
            System.out.println("响应内容:" + body);
            System.out.println("-------------------------------------------------");
            return response;
                 
 
    }
        catch(Exception e) {
        	e.printStackTrace();
            return null;
			
		}
	}
	
	public static void requestByDeleteMethod(String url,String token){
        //创建默认的httpClient实例
		CloseableHttpClient httpClient = getHttpClient();
        try {
            //用get方法发送http请求
        	HttpDelete delete=new HttpDelete(url);
            System.out.println("执行delete请求:...."+delete.getURI());
            CloseableHttpResponse httpResponse = null;
            delete.setHeader("Authorization", token);
            //发送get请求
            httpResponse = httpClient.execute(delete);
            try{
                //response实体
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("响应状态码:"+ httpResponse.getStatusLine());
                    System.out.println("-------------------------------------------------");
                    System.out.println("响应内容:" + EntityUtils.toString(entity));
                    System.out.println("-------------------------------------------------");                    
                }
            }
            finally{
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
 
    }
	public static void main(String[] args){
		JsonParser parser=new JsonParser();
		try {
			//带有body的post 实例
			String username="on003";
			String data="{groupname:'testGroupfsfsf','desc':'rest create group','public':true, 'maxusers':60,'members':['on002'],'owner':"+username+"}";
			JsonObject json=(JsonObject) parser.parse(new String(data));
			String addMember_url="http://a1.easemob.com/easemob-demo/chatdemoui/chatgroups";
	        RestAPI.requestByPostMethod(addMember_url, json, "Bearer YWMtxstBZht2Eee_8lVQg7NyqgAAAAAAAAAAAAAAAAAAAAGP-MBq3AgR45fkRZpPlqEwAQMAAAFbR8ziWgBPGgC53fdjolS_mPC-5s882Q2bmSQ5VsHjPS4xzpPKe3cdhg");
		}finally {
			System.out.println("created group");
		}
		
		
		
	}
	
	
	
	}
