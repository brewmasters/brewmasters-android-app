package com.gt.brewmasters.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.gt.brewmasters.R;
import com.gt.brewmasters.activities.BeerReportActivity;
import com.gt.brewmasters.activities.Brewmasters;
import com.gt.brewmasters.activities.FragmentPagerSupport;
import com.gt.brewmasters.activities.MachineStatusActivity;
import com.gt.brewmasters.utils.HttpTask;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommunityFragment extends Fragment{

	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
	//private View connectFormView;
	private RelativeLayout webStatusView;
	
    public static Brewmasters appContext;
    static FragmentPagerSupport context;
    
    private WebView wv;
    
    private Boolean show;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_screen, container, false);

        appContext = Brewmasters.getAppContext();
        
        webStatusView = (RelativeLayout) rootView.findViewById(R.id.webview_status);
        wv 			  = (WebView) rootView.findViewById(R.id.community_webview);
        
        getWebPage();

        return rootView;
    }
    
    public void getWebPage() {
    	showProgress(true);
    	wv.loadUrl(appContext.WEBSITE_URL);
        wv.getSettings().setJavaScriptEnabled(true);
        
        
        wv.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                appContext.makeToast("Failed to retrieve webpage");
                showProgress(false);
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
            	try {
            		showProgress(false);
                	super.onPageFinished(view, url);	
            	}
            	catch (IllegalStateException e) {
            		appContext.makeToast("Webpage not found");
            	}
            	
            }
        });
    }
    
    //Handler that receives messages from post execute to tell us when http post has been completed
//  	Handler pagerHandler = new Handler() {
//  	    @Override
//  	    public void handleMessage(Message msg) 
//  	    {
//  	    	super.handleMessage(msg);
//  	    	
//  	    	if(D) Log.e(TAG, "=============== pagerHandler == received");
//
//  	    	if (msg.what == HttpTask.BAD_RESPONSE)
//  	    	{
//  				//appContext.makeToast("Failed to communicate with device. Check power/connectivity");
//      			Log.v(TAG, "bad response");
//      			//connect();
//  	    	}
//  	    	
//  	    	else if (msg.what == HttpTask.RESPONSE)
//  	    	{
//  	    		
//  	    	}
//  	    }
//  	};
  	
//    public void connect() {
//    	Log.v(TAG, "connecting");
//    	String deviceUrl = Brewmasters.getDeviceAddress(this.getActivity())+"/status";
//    	getTask = new HttpTask(deviceUrl, this.pagerHandler, HttpTask.GET);
//    	getTask.execute((Void) null);
//    }
    
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
				webStatusView.setVisibility(View.VISIBLE);
				wv.setVisibility(View.GONE);
			}
			else {
				webStatusView.setVisibility(View.GONE);
				wv.setVisibility(View.VISIBLE);
			}

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			if(show) {
				webStatusView.setVisibility(View.VISIBLE);
				wv.setVisibility(View.GONE);
			}
			else {
				webStatusView.setVisibility(View.GONE);
				wv.setVisibility(View.VISIBLE);
			}
		}
	}
	
}
