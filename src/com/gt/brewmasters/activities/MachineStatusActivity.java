package com.gt.brewmasters.activities;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.brewmasters.R;
import com.gt.brewmasters.structures.ResponseStatus;
import com.gt.brewmasters.utils.HttpTask;
import com.gt.brewmasters.utils.MySound;

public class MachineStatusActivity extends Activity {
	
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    
	public static final int PREMASH = 0;
	public static final int MASH    = 2;
	public static final int SPARGE  = 3;
	public static final int BOIL    = 4;
	public static final int COOL    = 5;
	
	//response codes from server
	
	public static final int MASHWATERCONFIRM        = 1;
	public static final int OPENVALVECONFIRM        = 2;
	public static final int CLOSEVALVECONFIRM       = 3;
	public static final int SPARGEWATERCONFIRM      = 4;
	public static final int SPARGEVALVEOPENCONFIRM  = 5;
	public static final int SPARGEVALVECLOSECONFIRM = 6;
	public static final int HOPSADDCONFIRM          = 7;
	public static final int ICEADDCONFIRM           = 8;
    
    private Brewmasters appContext;
    
    private ProgressBar    progressSpinner;
    private LinearLayout   brewStatus;
    private RelativeLayout confirmationStatus;
    
    private TextView timeLeftTv;
    private TextView curStepTv;
    private TextView stepNameTv;
    private TextView boilTempTv;
    private TextView mashTempTv;
    private TextView messageTv;
    
    TimerTask doAsynchronousTask;
    
    private Boolean isQuerying = false;
    
    private static HttpTask getTask;
    private String responseUrl;
    
    private Boolean waitingForConfirmationAck = false;
    
    private Boolean showingAlert = false;
    
    private Boolean resetting = false;
    
    private MySound beeper = new MySound();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        Bundle bundle = getIntent().getExtras();
        
        parseBundle(bundle);
        
        initUi();
        
        //AlertBox("User Interaction Required", "Please add water at this time");
        queryDevice(statusHandler);
        
    }
    
    public int parseBundle(Bundle bundle) {
    	int retVal = -1;
    	if (bundle != null) {
			String status = bundle.getString("status");
			if(Boolean.valueOf(status)==false)
			{
				retVal = bundle.getInt("recipePos");
			}
		}
    	if(D) Log.v(TAG,"pos: " + String.valueOf(retVal));
    	return retVal;
    }
    
    public void initUi(){
    	setContentView(R.layout.activity_machine_status);
    	
    	brewStatus         = (LinearLayout)findViewById(R.id.brew_status_form);
    	confirmationStatus = (RelativeLayout)findViewById(R.id.confirmation_status);
    	
    	timeLeftTv = (TextView)findViewById(R.id.time_left);
    	curStepTv  = (TextView)findViewById(R.id.current_step);
    	stepNameTv = (TextView)findViewById(R.id.current_step_name);
    	boilTempTv = (TextView)findViewById(R.id.current_boil_temp);
    	mashTempTv = (TextView)findViewById(R.id.current_mash_temp);
    	messageTv  = (TextView)findViewById(R.id.current_message);
    	
    	progressSpinner = (ProgressBar)findViewById(R.id.status_progress);
    }
    
  //Handler that receives messages from post execute to tell us when http post has been completed
  	Handler statusHandler = new Handler() {
  	    @Override
  	    public void handleMessage(Message msg) 
  	    {
  	    	super.handleMessage(msg);
  	    	
  	    	if(D) Log.e(TAG, "=============== pagerHandler == received");

  	    	if (msg.what == HttpTask.BAD_RESPONSE)
  	    	{
  	    		isQuerying = true;
  	    		if (doAsynchronousTask != null) {
  	    			doAsynchronousTask.cancel();
  	    		}
      			Log.v(TAG, "bad response");
      			
      			if(resetting) {
      				Log.v(TAG, "failed to reset");
      				resetDevice();
      			}
      			
      			else {
      				Log.v(TAG, "failed to query");
      				queryDevice(this);
      			}
      			
      			
  	    	}
  	    	
  	    	else if (msg.what == HttpTask.RESPONSE)
  	    	{
  	    		//We recently sent a confirmation to the server. Ack is processed here 
  	    		if (waitingForConfirmationAck) {
  	    			showProgress(false);
  	    			waitingForConfirmationAck = false;
  	    			Log.v(TAG, "Server recieved confirmation");
  	    			queryDevice();
  	    		}
  	    		
  	    		//All other responses here
  	    		else {
  	    			showProgress(false);
  	    			
  	    			if (resetting) {
  	    				resetting = false;
  	    				appContext.makeToast("Device has been reset");
  	    				finish();
  	    				return;
  	    			}
  	    			
  	    			else {
  	    				Bundle data = msg.getData();
  	  	  	    		Log.v(TAG, "data: " + data.toString());
  	  	  	    		
  	  	  	    		JSONObject response = null;
  	  					try {
  	  						response = new JSONObject(data.getString("ResponseObject"));
  	  						
  	  						Log.v(TAG, "response json : " + response);
  	  						
  	  						ResponseStatus status = parseResponseJSON(response);
  	  						
  	  						Log.v(TAG, "response : " + status.toString());
  	  						
  	  						updateUi(status);
  	  						
  	  						if(status.requireUser) {
  	  							if(!showingAlert) {
  	  								showingAlert = true;
  	  								isQuerying=false;
  	  								Log.v(TAG, "showing alert");
  	  								beeper.playSound();
  	  								AlertBox("User Interaction Required", status.message);
  	  							}
  	  						}
  	  		  	    		
  	  					}
  	  	  	    		catch (JSONException e) {
  	  	  	    			Log.v(TAG, "error parsing json");
  	  						e.printStackTrace();
  	  					}	
  	    			}
  	    			
  	  	    		
  	    		}
  	    	}
  	    }
  	};
  	
  	public void updateUi(ResponseStatus status) {
  		String url = Brewmasters.getDeviceAddress(appContext);
  		switch(status.confirmationNumber) {
  		
  			case MASHWATERCONFIRM:
  				responseUrl = url+"/mashwaterconfirm"; 
  				break;
  			case OPENVALVECONFIRM:
  				responseUrl = url+"/openvalveconfirm";
  				//showPumpProgress(true);
  				break;
  			case CLOSEVALVECONFIRM:
  				//showPumpProgress(false);
  				responseUrl = url+"/closevalveconfirm";
  				break;
  			case SPARGEWATERCONFIRM:
  				responseUrl = url+"/spargewaterconfirm";
  				break;
  			case SPARGEVALVEOPENCONFIRM:
  				responseUrl = url+"/spargevalveopenconfirm";
  				break;
  			case SPARGEVALVECLOSECONFIRM:
  				responseUrl = url+"/spargevalvecloseconfirm";
  				break;
  			case HOPSADDCONFIRM:
  				responseUrl = url+"/hopsaddconfirm";
  				break;
  			case ICEADDCONFIRM:
  				responseUrl = url+"/iceaddconfirm";
  				break;
  		}
  		
  		String step = String.valueOf(status.step);
  		Log.v(TAG, "step:" + step);
  		
  		//if we are mashing, we want value of mash temp. Otherwise we want boil temp
  		String boilTemp = String.valueOf(round(status.boilTemp, 2));
  		String mashTemp = String.valueOf(round(status.mashTemp, 2));
  		
  		String time = String.valueOf(status.timeLeft);
  		
  		String stepName=null;
  		//Depending on what step number we are in, adjust the title accordingly
  		switch(status.step) {
  		
  			case PREMASH:
  				stepName = "Premash";
  				Log.v(TAG, "stepname:" + stepName);
  				break;
  			case MASH:
  				stepName = "Mash";
  				Log.v(TAG, "stepname:" + stepName);
  				break;
  			case SPARGE:
  				stepName = "Sparge";
  				Log.v(TAG, "stepname:" + stepName);
  				break;
  			case BOIL:
  				stepName = "Boil";
  				Log.v(TAG, "stepname:" + stepName);
  				break;
  			case COOL:
  				stepName = "Cool";
  				Log.v(TAG, "stepname:" + stepName);
  			default:
  				stepName = "Premash";
  				Log.v(TAG, "default stepname:" + stepName);
  		}
  		
  		Log.v(TAG, "stepName: " + stepName);
  		Log.v(TAG, "boiltemp: " + boilTemp);
  		Log.v(TAG, "mashtemp: " + mashTemp);
  		
  		//NA if not in step 2 or 4 (mash/boil)
  		if(Integer.valueOf(time)==0 && Integer.valueOf(step) != 2 && Integer.valueOf(step) !=4 ) {
  			time = "N/A";
  		}
  		
    	timeLeftTv.setText(time);
    	curStepTv.setText(step+"/5"); 
    	if(Integer.valueOf(step) == 3) {
    		stepNameTv.setText(stepName + " " + status.spargeNumber+"/5");
    	}
    	else {
    		stepNameTv.setText(stepName);
    	}
    	//(char) 0x00B0 = degree symbol
    	boilTempTv.setText(boilTemp + " " + (char) 0x00B0 + "F");
    	mashTempTv.setText(mashTemp + " " + (char) 0x00B0 + "F");
    	messageTv.setText(status.message);
  	}
  	
  	public ResponseStatus parseResponseJSON(JSONObject json) {
  		
  		Log.v(TAG, "first " + json.toString());
  		
  		ResponseStatus resp = new ResponseStatus();
  		try {
  			json = json.getJSONObject("ResponseObject");
  			Log.v(TAG, "second " + json.toString());
			resp.status             = json.getBoolean("status");
			resp.step               = json.getInt("step");
			resp.timeLeft           = json.getInt("timeLeft");
			resp.mashTemp           = json.getDouble("MashTemp");
			resp.boilTemp           = json.getDouble("BoilTemp");
			resp.spargeNumber       = json.getInt("SpargeNumber");
			resp.requireUser        = json.getBoolean("DoesRequireUser");
			resp.confirmationNumber = json.getInt("ConfirmationNumber");
			resp.message            = json.getString("Message");
			
		} 
  		
  		catch (JSONException e) {
			Log.v(TAG, "error parsing json");
			e.printStackTrace();
		}
		return resp;
  	}
  	
  	private void queryDevice(final Handler handler) {
  		Timer timer = new Timer();

  		doAsynchronousTask = new TimerTask() {

  		    @Override
  		    public void run() {

  		        handler.post(new Runnable() {
  		            public void run() {
  		            showProgress(true);
  		      		String url = Brewmasters.getDeviceAddress(MachineStatusActivity.this)+"/status";
  		    		
  		    		HttpTask getTask = new HttpTask(url, handler, HttpTask.GET);
  		    		getTask.execute((Void) null);
  		            }
  		        });

  		    }

  		};

  		timer.schedule(doAsynchronousTask, 0, 5000);// execute every 30s
  	}
  	
  	public void queryDevice() {
  		isQuerying=true;
  		showProgress(true);
		String url = Brewmasters.getDeviceAddress(MachineStatusActivity.this)+"/status";
		
		getTask = new HttpTask(url, statusHandler, HttpTask.GET);
		getTask.execute((Void) null);
  	}
  	
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//	private void showPumpProgress(final boolean show) {
//		 //connectStatusView = findViewById(R.id.connect_status);
//		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//		// for very easy animations. If available, use these APIs to fade-in
//		// the progress spinner.
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//			int shortAnimTime = getResources().getInteger(
//					android.R.integer.config_shortAnimTime);
//			if(show) {
//				if(D) Log.e(TAG, "pump visible");
//				confirmationStatus.setVisibility(View.VISIBLE);
//				brewStatus.setVisibility(View.GONE);
//				
//			}
//			else {
//				confirmationStatus.setVisibility(View.GONE);	
//				brewStatus.setVisibility(View.VISIBLE);
//			}
//
//		} 
//	}
	
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
				if(D) Log.e(TAG, "visible");
				
				//if we are waiting for confirmation, this is a different view we show/hide
				if(waitingForConfirmationAck) {
					confirmationStatus.setVisibility(View.VISIBLE);
					brewStatus.setVisibility(View.GONE);
				}
				else {
					progressSpinner.setVisibility(View.VISIBLE);	
				}
				
			}
			else {
				if(waitingForConfirmationAck) {
					confirmationStatus.setVisibility(View.GONE);
					brewStatus.setVisibility(View.VISIBLE);
				}
				else {
					progressSpinner.setVisibility(View.GONE);	
				}
				
			}

		} 
//		else {
//			// The ViewPropertyAnimator APIs are not available, so simply show
//			// and hide the relevant UI components.
//			if(show) {
//				if(D) Log.e(TAG, "visible");
//				progressSpinner.setVisibility(View.VISIBLE);
//			}
//			else {
//				progressSpinner.setVisibility(View.GONE);
//			}
//		}
	}
	
	@Override
	public void onBackPressed() {
		
		if(isQuerying) {
			Log.v(TAG, "stopping Query");
			this.stopQuery();
		}
		
		else {
			if(doAsynchronousTask != null) {
				this.doAsynchronousTask.cancel();
			}
			if(getTask != null) {
				getTask.cancel(true);
			}
			finish();
			return;
		}

	}
	
	public void stopQuery() {
		if(getTask != null) {
			getTask.cancel(true);
		}
		this.doAsynchronousTask.cancel();
		appContext.makeToast("canceled thread");
		isQuerying=false;
		showProgress(false);
	}
	
	public void AlertBox(String title, String message ){
	    new AlertDialog.Builder(this)
	    .setTitle( title )
	    .setMessage( message + " Press OK when ready to proceed." )
	    .setPositiveButton("OK", new OnClickListener() {
	        public void onClick(DialogInterface arg0, int arg1) {
	        	waitingForConfirmationAck=true;
	        	showingAlert=false;
	        	HttpTask getTask = new HttpTask(responseUrl, statusHandler, HttpTask.GET);
	        	getTask.execute((Void) null);
	        }
	    }).show();
	}
	
	public void ResetDevice(String title, String message ){
	    new AlertDialog.Builder(this)
	    .setTitle( title )
	    .setMessage( message )
	    .setPositiveButton("Yes", new OnClickListener() {
	        public void onClick(DialogInterface arg0, int arg1) {
                resetDevice();
	        }
	    })
	    .setNegativeButton("No", new OnClickListener() {
	        public void onClick(DialogInterface arg0, int arg1) {
                //do nothing
	        }
	    }).show();
	}
	
	public void resetDevice() {
  		showProgress(true);
  		resetting=true;
  		
		String url = Brewmasters.getDeviceAddress(MachineStatusActivity.this)+"/reset";
		
		getTask = new HttpTask(url, statusHandler, HttpTask.GET);
		getTask.execute((Void) null);
	}
	
	public void onResetClick(View v) {
		ResetDevice("Are you sure?", "Do you really want to reset the brewery?");
	}
	
    public static Double round(Double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
	
}
