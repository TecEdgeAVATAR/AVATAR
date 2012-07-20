package sate2012.avatar.android;


//import sate2012.avatar.android.GPSActivity.MyLocationListener;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log; 
import android.view.View; 
import android.widget.Button; 
import android.widget.Toast;
import android.widget.EditText;

import java.util.List;
import java.io.File;
import java.io.IOException;

import sate2012.avatar.android.BlueprintActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;

import org.apache.http.entity.*;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;


public  class UploadData  extends  Activity  { 
	
	//public static Context thisContext;
	
//	public String subj="";
//	public String body="Body.";
//	public String from="";
//	public String toList="";
//	public String ptName="";
//	public String ptDesc="";
//	public String ptType = "";
//	
//	public String ptLat = "39.7";
//	public String ptLng = "84.2";
//	public String item_sep="";
//	public String LatLong = "";
//	Context conn;

     /** Called when the activity is first created. */ 
    @ Override 
     public void onCreate (Bundle savedInstanceState )  { 
    	super .onCreate (savedInstanceState ) ; 
        Toast.makeText(getApplicationContext(), "Created UploadData Activity", Toast.LENGTH_LONG).show();
         //thisContext = getApplicationContext();
        
    }	// end of onCreate
    
    
	        
	       
	        
    public static void post(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue()), "image/jpeg"));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            
            Toast.makeText(BlueprintActivity.thisContext, "Response: '"+response+"'", Toast.LENGTH_LONG).show();
            // 4062ac00
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	        
	        
	        
	        
	        
      
}