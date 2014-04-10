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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
	
	private static Brewmasters appContext;
    
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private HttpTask loginTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	
	private Button btnLogin, btnRegister;
	Intent myIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        initUi();
        
    }
    
    public void initUi(){
        setContentView(R.layout.activity_login);
        
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
        
        //Add buttons and listeners
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        
        mEmailView = (EditText) findViewById(R.id.log_email);
        mPasswordView = (EditText) findViewById(R.id.log_password);
        
        //If email and password have been stored, automatically login
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("login_save", 0);;
		if (prefs.contains("password") && prefs.contains("email")) {
			mEmail    = prefs.getString("email", null);
			mPassword = prefs.getString("password", null);
			
			mEmailView.setText(mEmail);
			mPasswordView.setText(mPassword);
			
			btnLogin.performClick();
		}
        

    }
    
	//Handler that receives messages from post execute to tell us when http post has been completed
	Handler loginHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) 
	    {
	    	super.handleMessage(msg);
	    	showProgress(false);
	    	
	    	if(D) Log.e(TAG, "=============== registerHandler == received");

	    	if (msg.what == HttpTask.BAD_RESPONSE)
	    	{
				Toast.makeText(LoginActivity.this, "Account creation failed",
    					Toast.LENGTH_LONG).show();
    			Log.v(TAG, "bad response");
	    	}
	    	
	    	else if (msg.what == HttpTask.RESPONSE)
	    	{
	    		Bundle data = msg.getData();
	    		String authToken = (String) data.get("token");
	    		Log.v(TAG, "token: " + authToken);
	    		//TODO better way of checking this?
	    		if (authToken.contains("Error with your login or password")) {
	    			Toast.makeText(LoginActivity.this, "Invalid email/password",
					Toast.LENGTH_LONG).show();
	    			Log.v(TAG, "bad response");
	    		}
	    		else {
	    			Toast.makeText(LoginActivity.this, "Login Successful",
	    					Toast.LENGTH_SHORT).show();
	    			
	    			
	    			
		    		//TODO what do i need to pass this?
		    		//How do i use token? store in app context?
	    			
	    			SharedPreferences oSP = null;
	    			oSP = getApplicationContext().getSharedPreferences("login_save", 0);
	    			
	    			//Add values to shared preferences for automatic login next time
	    			Editor editor = oSP.edit();
	    			editor.putString("email", mEmail);
	    			editor.putString("password", mPassword);
	    			editor.putString("token", authToken);
	    			editor.commit();
	    			
	    			
		    		//Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
	    			Intent myIntent = new Intent(LoginActivity.this, FragmentPagerSupport.class);
					startActivity(myIntent);
	    		}
	    	}
	    }
	};
	
	public void onLoginClick(View v) {
		attemptLogin();
	}
	
	public void onRegisterClick(View v) {
		myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(myIntent);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (loginTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			
			showProgress(true);

			JSONObject loginJson = createLoginJSON(mEmail, mPassword);
			userLogin(loginJson);
			
		}
	}
	
	//{"user": {"email": "user@email.com", "password": "mypassword123"}}
	public JSONObject createLoginJSON(String email, String password) {
		Map data=new LinkedHashMap();
		data.put("email", email);
		data.put("password", password);
		
		JSONObject userData = appContext.createJSON(data);
		
		data.clear();
		data.put("user", userData);
		JSONObject loginJson = appContext.createJSON(data);
		return loginJson;
	}
	
	/*
	 * LOGIN - Accepts POST of type JSON
	 * URL - brewmasters.herokuapp.com/api/v1/login
	 * i.e.
	 * {"user": {"email": "user@email.com", "password": "mypassword123"}}
	 * returns auth_token
	*/
    public void userLogin(JSONObject json){
    	String loginUrl = appContext.LOGIN_URL;
    	
    	HttpTask postTask = new HttpTask(loginUrl, json, this.loginHandler, HttpTask.POST);
    	postTask.execute((Void) null);
    }
	
	private void showToastOnUiThread(final String message, final int duration)
	{
		runOnUiThread(new Runnable() 
		{ 
			@Override
			public void run()
			{
				Toast toast = Toast.makeText(appContext, message, duration);
				toast.show();				
			}
		});		
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
