package com.gt.brewmasters.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Represents an asynchronous http task
 * 
 * This is setup to be a skeleton for general http requests, but currently only POST is implemented
 */
public class HttpTask extends AsyncTask<Void, Void, String> {
	
    // Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	private String url;
	private List<NameValuePair> nvp;
	private JSONObject json;
	private Handler handler;
	private int type;
	
	private String[] httpResult;
	private String response;
	private String serverResponsePhrase;
	private int serverStatusCode = -1;
	
	private StringEntity strEntity;
	
	public static final int GET    = 1;
	public static final int POST   = 2;
	public static final int DELETE = 3;
	
	public static final int BAD_RESPONSE = -1;
	public static final int RESPONSE     =  1;
	
	HttpGetter getter;
	HttpPoster poster;
	
	//For use with jsons
	public HttpTask (String url, JSONObject json, Handler handler, int type) {
		this.url=url;
		this.json=json;
		this.handler=handler;
		this.type=type;

		strEntity = null;
		
		try {
			strEntity = new StringEntity(json.toString(), HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		strEntity.setContentType("application/json");
		
	}
	
	//For use with get
	public HttpTask(String url, Handler handler, int type) {
		this.url=url;
		this.handler=handler;
		this.type=type;
	}
	
	@Override
	protected String doInBackground(Void... params) {

		if(this.type==HttpTask.POST) {
			if (strEntity!=null) {
					poster = new HttpPoster(url, strEntity);
					if(D) Log.e(TAG, poster.toString());
					if(D) Log.e(TAG, "*******POSTING JSON*******");
					httpResult = poster.post();
					response = httpResult[0];
					serverResponsePhrase = httpResult[1];
					try {
						serverStatusCode = Integer.valueOf(httpResult[2]);
					}
					catch(Exception e) {
						serverStatusCode = -1;
					}
			}
		}
		if(this.type==HttpTask.GET) {
			getter = new HttpGetter(url);
			if(D) Log.e(TAG, getter.toString());
			if(D) Log.e(TAG, "*******GETTING WEB REQUEST*******");
			httpResult = getter.get();
			response = httpResult[0];
			serverResponsePhrase = httpResult[1];
			try {
				serverStatusCode = Integer.valueOf(httpResult[2]);
			}
			catch(Exception e) {
				serverStatusCode = -1;
			}
		}
		
		
		if(D) Log.d(TAG, "returning server response: " + response);
		return response;
	}

	@Override 
	protected void onPostExecute(String result) {
		Message msg = new Message();
		
		if(result == null || result.startsWith("-1") || result.startsWith("null"))
		{
			if(D) Log.d(TAG, "result: " + result);
	        msg.what = this.BAD_RESPONSE;
	        handler.sendMessage(msg);
		}
		
		else
		{
			if(D) Log.d(TAG, "result: " + result);
	        msg.what = this.RESPONSE;
	        Bundle data = new Bundle();
	        data.putString("server_response", this.serverResponsePhrase);
	        data.putInt("server_status_code", this.serverStatusCode);
	        data.putString("ResponseObject", result);
	        msg.setData(data);
	        handler.sendMessage(msg);
		}				
	}
	
	@Override
	protected void onCancelled() {
		if(this.type == HttpTask.GET) {
			if(D) Log.v(TAG, "canceled get");
			getter.httpclient.getConnectionManager().shutdown();
		}
		if(this.type == HttpTask.POST) {
			if(D) Log.v(TAG, "canceled post");
			poster.httpclient.getConnectionManager().shutdown();
		}
	}
}