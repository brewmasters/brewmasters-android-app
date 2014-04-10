package com.gt.brewmasters.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class HttpPoster {
	
    // Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	String serverResponsePhrase;
	int serverStatusCode = -1;
	String bytesSent     = "null";
	String url;
	
	List<NameValuePair> nvp;
	MultipartEntity multEntity;
	StringEntity strEntity;
	
	HttpPost httppost;
	DefaultHttpClient httpclient;
	BasicHttpContext httpContext;
	
	boolean usingNvp       = false;
	boolean usingMultiPart = false;
	boolean usingStrEnt    = false;
	
	//Entity with nvp's (not used in this project)
	public HttpPoster(String url, List<NameValuePair> nvp) {
		
		this.url=url;
		this.nvp=nvp;
		
		usingNvp = true;
		
		httppost = new HttpPost(url);
		httpclient = new DefaultHttpClient();
		httpContext = new BasicHttpContext();
	}
	
	//Entity with multiple parts (not used in this project)
	public HttpPoster(String url, MultipartEntity entity) {
		
		this.url=url;
		this.multEntity=entity;
		usingMultiPart = true;
		
		httppost = new HttpPost(url);
		httpclient = new DefaultHttpClient();
		httpContext = new BasicHttpContext();
	}
	
	//String entity
	public HttpPoster(String url, StringEntity entity) {
		
		this.url=url;
		this.strEntity=entity;
		usingStrEnt = true;
		
		httppost = new HttpPost(url);
		httpclient = new DefaultHttpClient();
		httpContext = new BasicHttpContext();
	}
	
	// Send POST message  with given parameters to the HTTP server.	
	public String[] post() {
		
		String [] retVal = new String[] {"null", "null", "null"};
		
		try {                    
			httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
			
			if (usingNvp) httppost.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8 ));
			if (usingMultiPart) httppost.setEntity(multEntity);
			if (usingStrEnt) httppost.setEntity(strEntity);
			
			HttpResponse response = httpclient.execute(httppost, httpContext);
			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
	
			int current = 0;  
			while((current = bis.read()) != -1) {  
				baf.append((byte)current);  
			}  
	
			bytesSent = new String(baf.toByteArray());
	
			// Response from the server          
			serverResponsePhrase = response.getStatusLine().getReasonPhrase();
			serverStatusCode = response.getStatusLine().getStatusCode();
	
			if (D) Log.v(TAG, serverResponsePhrase);
			if (D) Log.v(TAG, String.valueOf(serverStatusCode));
			if (D) Log.v(TAG, String.valueOf(bytesSent));
			if(D) Log.e(TAG, "here: " +  bytesSent);
			
			retVal[0] = bytesSent;
			retVal[1] = serverResponsePhrase;
			retVal[2] = String.valueOf(serverStatusCode);
			
			return retVal;
	
		}
		catch (Exception e) {
			bytesSent += e.toString();
			retVal[0] = bytesSent;
			if(D) Log.e(TAG, "An error occured while posting");		
		}
		return retVal;
	}
}
