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
//Skeleton settings code taken from MABIS project. Havent reworked it for this project yet.
public class SettingsActivity extends Activity {
	private Brewmasters appContext;
	private SharedPreferences mPref;
	
	private static String REPOSITORY_URL = "http://10.10.10.97/UtilityMobService/FileUpload.aspx";
	private static String MABIS_URL 	 = "http://10.10.10.97/UtilityMobService/MobService.asmx/Process";
	
	@Override
	public void onCreate(Bundle bundle) {
		Log.i("SettingsManager", "==========onCreate");
		
		super.onCreate(bundle);
		
		appContext = Brewmasters.getAppContext();
		setContentView(R.layout.activity_settings);

		//AddURL, MabisURL
		SharedPreferences oSP = null;
		oSP = getApplicationContext().getSharedPreferences("img_mgr_setting_save", 0);
		
		String strRepositoryURL = oSP.getString("save_repositoryurl", null);
		String strMabisURL = oSP.getString("save_mabisurl", null);
		String strLocalDevice = oSP.getString("save_localdevice", null);
		
		
		if(strRepositoryURL == null || strRepositoryURL.length() == 0)
		{
			EditText etRepositoryURL = (EditText)this.findViewById(R.id.etRepositoryURL);
			etRepositoryURL.setText(REPOSITORY_URL);
		}
		
		if(strMabisURL == null || strMabisURL.length() == 0)
		{
			EditText etMabisURL = (EditText)this.findViewById(R.id.etMabisURL);
			etMabisURL.setText(MABIS_URL);
		}
		
		if(strLocalDevice == null || strLocalDevice.length() == 0)
		{
			CheckBox cbLocalDevice = (CheckBox)this.findViewById(R.id.cbLocalDevice);
			cbLocalDevice.setChecked(false);
		}
		
		if(strRepositoryURL != null && strRepositoryURL.length() != 0)
		{
			EditText etRepositoryURL = (EditText)this.findViewById(R.id.etRepositoryURL);
			etRepositoryURL.setText(strRepositoryURL);
		}
		
		if(strMabisURL != null && strMabisURL.length() != 0)
		{
			EditText etMabisURL = (EditText)this.findViewById(R.id.etMabisURL);
			etMabisURL.setText(strMabisURL);
		}
		
		if(strLocalDevice != null && strLocalDevice.length() != 0)
		{
			CheckBox cbLocalDevice = (CheckBox)this.findViewById(R.id.cbLocalDevice);
			if (strLocalDevice.compareTo("1")==0) {
				cbLocalDevice.setChecked(true);
			}
			else {
				cbLocalDevice.setChecked(false);
			}
			
		}
	}
    
	//save
    public void onSaveClick(View view) {
    	
    	//Checking RepositoryURL
    	EditText etRepositoryURL = (EditText)this.findViewById(R.id.etRepositoryURL);
		String strRepositoryURL = etRepositoryURL.getText().toString();
		
		if(strRepositoryURL == null || strRepositoryURL.length() == 0)
		{
			Toast.makeText(this, "Please enter a value for RepositoryURL ...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//Checking MabisURL
		EditText etMabisURL = (EditText)this.findViewById(R.id.etMabisURL);
		String strMabisURL = etMabisURL.getText().toString();
		
		if(strMabisURL == null || strMabisURL.length() == 0)
		{
			Toast.makeText(this, "Please enter a value for MabisURL ...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//Saving RepositoryURL, MabisURL, LocalDevice
		CheckBox cbLocalDevice = (CheckBox)this.findViewById(R.id.cbLocalDevice);
		String strLocalDevice;
		
		if(cbLocalDevice.isChecked()==true){
			strLocalDevice = "1";
			//appCotext.setUsingLocalDevice(true);
		}
		else {
			strLocalDevice = "0";
			//appContext.setUsingLocalDevice(false);
		}
		
		SharedPreferences oSP = null;
		oSP = getApplicationContext().getSharedPreferences("img_mgr_setting_save", 0);
		
		Editor editor = oSP.edit();
		editor.putString("save_repositoryurl", strRepositoryURL);
		editor.putString("save_mabisurl", strMabisURL);
		editor.putString("save_localdevice", strLocalDevice);
		editor.commit();
		
		Toast.makeText(this, "Save Success!", Toast.LENGTH_SHORT).show();
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
