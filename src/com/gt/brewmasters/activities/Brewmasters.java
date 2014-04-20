package com.gt.brewmasters.activities;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

public class Brewmasters extends Application {
	
	private static Context context;
	public JSONArray array;
	public String user;
	
	public LayoutInflater inflater;
	public ViewGroup viewGroup;
	
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
