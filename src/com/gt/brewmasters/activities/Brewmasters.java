package com.gt.brewmasters.activities;

import java.util.Map;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;


@ReportsCrashes(
        formKey = "",
        formUri = "https://lucidic.cloudant.com/acra-brewmasters/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin="ioniontereaceenteratuthe",
        formUriBasicAuthPassword="eMYEVXqxFig1aedueAAtuuXm")
public class Brewmasters extends Application {
	
	private static Context context;
	public JSONArray array;
	public String user;
	
	public LayoutInflater inflater;
	public ViewGroup viewGroup;
	
	public final String WEBSITE_URL = "http://brewmasters.herokuapp.com/";
	public final String REGISTER_URL = "http://brewmasters.herokuapp.com/api/v1/register";
	public final String LOGIN_URL = "http://brewmasters.herokuapp.com/api/v1/login";
	public final String LOGOUT_URL = "http://brewmasters.herokuapp.com/api/v1/logout";
	public static final String DEVICE_ADDRESS = "http://192.168.0.100";
	
	public static String getDeviceAddress(Context oContext) {
		SharedPreferences oSP = null;
		oSP = oContext.getSharedPreferences("settings_save", 0);
		String strDeviceAddress = oSP.getString("save_ip", null);
		
		String strMabisServerUrl = null;
		if(strDeviceAddress == null || strDeviceAddress.length() == 0)
			strMabisServerUrl = DEVICE_ADDRESS;
		else
			strMabisServerUrl = strDeviceAddress;

		return strMabisServerUrl;
	}
	
    public void onCreate(){
        super.onCreate();
        Brewmasters.context = (Brewmasters) getApplicationContext();
        ACRA.init(this);
    }
    
    public static Brewmasters getAppContext() {
        return (Brewmasters) Brewmasters.context;
    }
	
	public String getUser() {
		return this.user;
	}
	
	public void setUser(String user) {
		this.user=user;
	}
	
	public JSONArray getJsonArray () {
		return this.array;
	}
	
	public void setJsonArray(JSONArray array) {
		this.array = array;
	}
	
	
	public JSONObject createJSON(Map hashMap) {
		 JSONObject jObj = new JSONObject(hashMap);

		// return JSON String
		return jObj;
	}
	
	public LayoutInflater getInflater() {
		return this.inflater;
	}
	
	public void setInflater(LayoutInflater inflater) {
		this.inflater=inflater;
	}
	
	public ViewGroup getViewGroup() {
		return this.viewGroup;
	}
	
	public void setViewGroup(ViewGroup viewGroup) {
		this.viewGroup=viewGroup;
	}
	
	public void makeToast(String toastText) {
		Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
	}
	
}
