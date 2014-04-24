package com.gt.brewmasters.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.gt.brewmasters.R;
import com.gt.brewmasters.activities.BeerReportActivity;
import com.gt.brewmasters.activities.Brewmasters;
import com.gt.brewmasters.activities.FragmentPagerSupport;
import com.gt.brewmasters.activities.MachineStatusActivity;
import com.gt.brewmasters.utils.HttpTask;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

/**
 * A fragment that launches other parts of the demo application.
 */
public class DeviceConnectFragment extends Fragment {
	
	
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	//private View connectFormView;
	private View connectStatusView;
	
    public static Brewmasters appContext;
    static FragmentPagerSupport context;
    
    private Boolean show;
    HttpTask getTask;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect_screen, container, false);

        appContext = Brewmasters.getAppContext();
        
        rootView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Log.v(TAG, "on connect click");
            	//DeviceConnectFragment.this.gotoBeerReport();
            	show=true;
            	DeviceConnectFragment.this.showProgress(show);
            	DeviceConnectFragment.this.connect();
            }
        });
        
        rootView.findViewById(R.id.btn_cancel_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Log.v(TAG, "on cancel click");
            	show=false;
            	DeviceConnectFragment.this.showProgress(show);
            	getTask.cancel(true);
            }
        });
        
        connectStatusView = rootView.findViewById(R.id.connect_status);

        return rootView;
    }
    
    //Handler that receives messages from post execute to tell us when http post has been completed
  	Handler pagerHandler = new Handler() {
  	    @Override
  	    public void handleMessage(Message msg) 
  	    {
  	    	super.handleMessage(msg);
  	    	
  	    	if(D) Log.e(TAG, "=============== pagerHandler == received");

  	    	if (msg.what == HttpTask.BAD_RESPONSE)
  	    	{
  				//appContext.makeToast("Failed to communicate with device. Check power/connectivity");
      			Log.v(TAG, "bad response");
      			connect();
  	    	}
  	    	
  	    	else if (msg.what == HttpTask.RESPONSE)
  	    	{
  	    		show=false;
  	    		showProgress(show);
  	    		
  	    		Bundle data = msg.getData();
  	    		Log.v(TAG, "data: " + data.toString());
  	    		
  	    		JSONObject response;
				try {
					Log.v(TAG, data.getString("ResponseObject"));
					
					response = new JSONObject(data.getString("ResponseObject"));
					Log.v(TAG, response.toString());
					//token get response object then get status
					
					response = response.getJSONObject("ResponseObject");
					//String responseObject = response.getString("status");
	  	    		Log.v(TAG, "from json: " + response.getString("status"));
	  	    		Log.v(TAG, "resp" + response.toString());
	  	    		Boolean serverStatus = response.getBoolean("status");
	  	    		if(serverStatus==false) {
	  	    			gotoBeerReport();
	  	    		}
	  	    		else {

	  	    			gotoMachineStatus(response);
	  	    		}
	  	    		
				}
  	    		catch (JSONException e) {
  	    			Log.v(TAG, "error parsing json");
					e.printStackTrace();
				}
  	    		
  	    	}
  	    }
  	};
  	
    public void connect() {
    	Log.v(TAG, "connecting");
    	String deviceUrl = Brewmasters.getDeviceAddress(this.getActivity())+"/status";
    	getTask = new HttpTask(deviceUrl, this.pagerHandler, HttpTask.GET);
    	getTask.execute((Void) null);
    }
    
    public void gotoMachineStatus(JSONObject response) {
		Bundle jsonBundle = new Bundle();
		jsonBundle.putString("status", response.toString());
		Intent statusIntent = new Intent(this.getActivity(), MachineStatusActivity.class);
		statusIntent.putExtras(jsonBundle);
		startActivity(statusIntent);
    }
    
    public void gotoBeerReport() {
		Intent myIntent = new Intent(this.getActivity(), BeerReportActivity.class);
		startActivity(myIntent);
    }
    
    
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		 //connectStatusView = findViewById(R.id.connect_status);
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);
			if(show) {
				connectStatusView.setVisibility(View.VISIBLE);
			}
			else {
				connectStatusView.setVisibility(View.GONE);
			}

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			if(show) {
				connectStatusView.setVisibility(View.VISIBLE);
			}
			else {
				connectStatusView.setVisibility(View.GONE);
			}
		}
	}
	
}
