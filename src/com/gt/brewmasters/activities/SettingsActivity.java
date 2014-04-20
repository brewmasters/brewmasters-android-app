package com.gt.brewmasters.activities;

import com.gt.brewmasters.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//Manages application settings, nothing crazy going on here
public class SettingsActivity extends Activity {
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	private Brewmasters appContext;
	private SharedPreferences mPref;
	
	private static String DEVICE_IP = "http://192.168.0.100";
	
	@Override
	public void onCreate(Bundle bundle) {
		Log.i("SettingsManager", "==========onCreate");
		
		super.onCreate(bundle);
		
		appContext = Brewmasters.getAppContext();
		setContentView(R.layout.activity_settings);

		//AddURL, MabisURL
		SharedPreferences oSP = null;
		oSP = getApplicationContext().getSharedPreferences("settings_save", 0);
		
		String strDeviceAddress = oSP.getString("save_ip", null);
		
		
		if(strDeviceAddress == null || strDeviceAddress.length() == 0)
		{
			EditText etDeviceAddress = (EditText)this.findViewById(R.id.etDeviceAddress);
			etDeviceAddress.setText(DEVICE_IP);
		}
		
	}
    
	//save
    public void onSaveClick(View view) {
    	
    	//Checking RepositoryURL
    	EditText etDeviceAddress= (EditText)this.findViewById(R.id.etDeviceAddress);
		String strDeviceAddress = etDeviceAddress.getText().toString();
		
		if(strDeviceAddress == null || strDeviceAddress.length() == 0)
		{
			Toast.makeText(this, "Please enter a value for the device's address ...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SharedPreferences oSP = null;
		oSP = getApplicationContext().getSharedPreferences("settinga_save", 0);
		
		Editor editor = oSP.edit();
		editor.putString("save_ip", strDeviceAddress);
		editor.commit();
		
		Toast.makeText(this, "Settings have been updated", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("SettingsManager", "==========onResume");
	}
	
	@Override
	protected void onPause() {
		Log.i("SettingsManager", "==========onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("SettingsManager", "==========onDestroy");
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
