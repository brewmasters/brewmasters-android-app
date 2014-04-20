package com.gt.brewmasters.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class HttpGetter {
	
    // Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	String serverResponsePhrase;
	int serverStatusCode = -1;
	String bytesSent     = "null";
	String url;
	
	StringEntity strEntity;
	
	HttpGet httpGet;
	DefaultHttpClient httpclient;
	BasicHttpContext httpContext;
	
	
	public HttpGetter(String url) {
		
		this.url=url;
		
		httpGet = new HttpGet(url);
		httpclient = new DefaultHttpClient();
		httpContext = new BasicHttpContext();
	}
	
	// Send GET message  with given parameters to the HTTP server.	
	public String[] get() {
		
		String [] retVal = new String[] {"null", "null", "null"};
		
		try {                    
			httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
			
			HttpResponse response = httpclient.execute(httpGet, httpContext);
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
			if(D) Log.e(TAG, "An error occured while getting");
			
		}
		
		return retVal;
	}
}
