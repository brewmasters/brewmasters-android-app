package com.gt.brewmasters.activities;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.gt.brewmasters.R;
import com.gt.brewmasters.utils.HttpTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Shows create new account screen
 */
public class RegisterActivity extends Activity implements OnClickListener {
  
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
	
	private View registerFormView;
	private View registerStatusView;
	
	private EditText regPasswordEt;
	private EditText regEmailEt;
	private EditText regPasswordConfEt;
	
	private HttpTask regTask = null;
	
	private static Brewmasters appContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = Brewmasters.getAppContext();

        initUi();
    }
	
	public void initUi(){
        setContentView(R.layout.activity_register);
        
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        Button registerButton = (Button) findViewById(R.id.btn_register_accept);
        
		registerFormView = findViewById(R.id.register_form);
		registerStatusView = findViewById(R.id.register_status);
		
		regPasswordEt = (EditText) findViewById(R.id.reg_password);
		regEmailEt = (EditText) findViewById(R.id.reg_email);
		regPasswordConfEt = (EditText) findViewById(R.id.reg_password_confirmation);
		
		loginScreen.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
	}

	//Handler that receives messages from post execute to tell us when http post has been completed
	Handler registerHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) 
	    {
	    	super.handleMessage(msg);
	    	showProgress(false);
	    	
	    	if(D) Log.e(TAG, "=============== registerHandler == received");

	    	if (msg.what == HttpTask.BAD_RESPONSE)
	    	{
				Toast.makeText(RegisterActivity.this, "Failed to communicate with server",
    					Toast.LENGTH_LONG).show();
    			Log.v(TAG, "bad response");
	    	}
	    	
	    	else if (msg.what == HttpTask.RESPONSE)
	    	{	
	    		Bundle data = msg.getData();
	    		String authToken = (String) data.get("token");
	    		Log.v(TAG, "token: " + authToken);
	    		//TODO better way of checking this?
	    		//added information to data bundle that will help check this
	    		if (authToken.contains("has already been taken")) {
	    			Toast.makeText(RegisterActivity.this, "Account creation failed - Email taken",
					Toast.LENGTH_LONG).show();
	    			Log.v(TAG, "bad response");
	    		}
	    		else {
	    			Toast.makeText(RegisterActivity.this, "Account has been created",
	    					Toast.LENGTH_LONG).show();
		    		//TODO what do i need to pass this?
		    		//How do i use token? store in app context?
		    		Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
					startActivity(myIntent);
	    		}
	    	}
	    }
	};

	public void attemptRegister() {
		if (regTask != null) {
			return;
		}

		// Reset errors.
		regEmailEt.setError(null);
		regPasswordEt.setError(null);
		regPasswordConfEt.setError(null);
		
		String email = regEmailEt.getText().toString();
		String password = regPasswordEt.getText().toString();
		String passwordConf = regPasswordConfEt.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			regPasswordEt.setError(getString(R.string.error_field_required));
			focusView = regPasswordEt;
			cancel = true;
		} else if (password.length() < 8) {
			regPasswordEt.setError(getString(R.string.error_invalid_password));
			focusView = regPasswordEt;
			cancel = true;
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			regEmailEt.setError(getString(R.string.error_field_required));
			focusView = regEmailEt;
			cancel = true;
		} else if (!email.contains("@")) {
			regEmailEt.setError(getString(R.string.error_invalid_email));
			focusView = regEmailEt;
			cancel = true;
		}
		
		//Ensure passwords match
		if (TextUtils.isEmpty(passwordConf)) {
			regPasswordConfEt.setError(getString(R.string.error_field_required));
			focusView = regPasswordConfEt;
			cancel = true;
		}
		else if(!password.contentEquals(passwordConf)){
			if(D) Log.v(TAG, password + " " + passwordConf);
			regPasswordConfEt.setError(getString(R.string.error_password_mismatch));
			focusView = regPasswordConfEt;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} 

		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		else {
			showProgress(true);
			
    		JSONObject registerJson = createRegisterJSON(email, password, passwordConf);
			createUser(registerJson);
			
		}
	}
	
	//{"user": {"email": "user@email.com", "password": "mypassword123", "password_confirmation": "mypassword123"}}
	public JSONObject createRegisterJSON(String email, String password, String passwordConf) {
		Map data=new LinkedHashMap();
		data.put("email", email);
		data.put("password", password);
		data.put("password_confirmation", passwordConf);
		
		JSONObject userData = appContext.createJSON(data);
		
		data.clear();
		data.put("user", userData);
		JSONObject registerJson = appContext.createJSON(data);
		return registerJson;
	}
	
	/*
	 * REGISTER - Accepts POST of type JSON
	 * URL - brewmasters.herokuapp.com/api/v1/register
	 * i.e.
	 * {"user": {"email": "user@email.com", "password": "mypassword123", "password_confirmation": "mypassword123"}}
	 * returns auth_token
	*/
    public void createUser(JSONObject json){
    	String registerUrl = appContext.REGISTER_URL;
    	
    	HttpTask postTask = new HttpTask(registerUrl, json, this.registerHandler, HttpTask.POST);
    	postTask.execute((Void) null);
    }
    
    /**
	 * Shows the progress UI and hides the register form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			registerStatusView.setVisibility(View.VISIBLE);
			registerStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							registerStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			registerFormView.setVisibility(View.VISIBLE);
			registerFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							registerFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			registerStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btn_register_accept:
				attemptRegister();
				break;
			case R.id.link_to_login:
				finish();
				break;
		}
	}
    
} 