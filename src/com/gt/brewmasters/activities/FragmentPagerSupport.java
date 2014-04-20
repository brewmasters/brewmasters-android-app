package com.gt.brewmasters.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.gt.brewmasters.R;
import com.gt.brewmasters.utils.HttpTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPagerSupport extends FragmentActivity implements OnClickListener {
	
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
	
    static final int NUM_ITEMS = 3;
    
    
    static final int CONNECT_SCREEN   = 0;
    static final int RECIPE_SCREEN    = 1;
    static final int COMMUNITY_SCREEN = 2;

    MyAdapter mAdapter;

    ViewPager mPager;
    public static Brewmasters appContext;
    static FragmentPagerSupport context;
    
	//private View connectFormView;
	private View connectStatusView;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        
        context = this;
        
        appContext = Brewmasters.getAppContext();
        
        connectStatusView = findViewById(R.id.connect_status);
        
        mAdapter = new MyAdapter(getSupportFragmentManager());
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

    }
    
  //Handler that receives messages from post execute to tell us when http post has been completed
  	Handler pagerHandler = new Handler() {
  	    @Override
  	    public void handleMessage(Message msg) 
  	    {
  	    	super.handleMessage(msg);
  	    	showProgress(false);
  	    	
  	    	if(D) Log.e(TAG, "=============== pagerHandler == received");

  	    	if (msg.what == HttpTask.BAD_RESPONSE)
  	    	{
  				Toast.makeText(FragmentPagerSupport.this, "Failed to communicate with device. Check power/connectivity",
      					Toast.LENGTH_LONG).show();
      			Log.v(TAG, "bad response");
  	    	}
  	    	
  	    	else if (msg.what == HttpTask.RESPONSE)
  	    	{
  	    		Bundle data = msg.getData();
  	    		Log.v(TAG, "data: " + data.toString());
  	    		
  	    		JSONObject response;
				try {
					response = new JSONObject(data.getString("token"));
					
	  	    		Log.v(TAG, "from json: " + response.getString("Status"));
	  	    		Boolean serverStatus = Boolean.valueOf(response.getString("Status"));
	  	    		if(serverStatus==false) {
	  	    			gotoBeerReport();
	  	    		}
	  	    		else {
	  	    			
	  	    		}
	  	    		
				}
  	    		catch (JSONException e) {
					e.printStackTrace();
				}
  	    		
  	    	}
  	    }
  	};

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends Fragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
        	
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	
        	View v = null;
    		View tv;
    		
        	switch(mNum) {
        		
        		case CONNECT_SCREEN:
        			v = inflater.inflate(R.layout.fragment_connect_screen, container, false);
                    tv = v.findViewById(R.id.text);
                    ((TextView)tv).setText("Device Management");
                    
//            		btnImage = (Button)this.findViewById(R.id.btn_images);
//            		btnImage.setBackgroundResource(R.drawable.enroll_topbar);
//            		
//            		Button btnGeneralInfo = (Button)this.findViewById(R.id.btn_general_info);
//            		btnGeneralInfo.setBackgroundResource(R.drawable.enroll_top_btn);
//            		
//            		Button btnFingerPrints = (Button)this.findViewById(R.id.btn_finger_prints);
//            		btnFingerPrints.setBackgroundResource(R.drawable.enroll_top_btn);
//            		
//            		Button btnIris = (Button) findViewById(R.id.btn_iris);
//            		btnIris.setBackgroundResource(R.drawable.enroll_top_btn);
                    
        			break;
        			
        		case RECIPE_SCREEN:
        			v = inflater.inflate(R.layout.fragment_recipe_screen, container, false);
                    tv = v.findViewById(R.id.text);
                    ((TextView)tv).setText("Recipe Management");
                    
                    Button createRecipe = (Button) v.findViewById(R.id.btn_new_recipe);
                    
                    createRecipe.setOnClickListener(context);
                    
        			break;
        			
        		case COMMUNITY_SCREEN:
        			break;
        		
        	}
        	
        	return v;
        	
        }

    }
    
    //Connect button on device screen. Attempts to get machine state
    public void onConnectClick(View v) {
    	this.showProgress(true);
    	this.connect();
    	//this.gotoBeerReport();
    }
    
    public void connect() {
    	String deviceUrl = "http://192.168.0.100/status";
    	
    	HttpTask getTask = new HttpTask(deviceUrl, this.pagerHandler, HttpTask.GET);
    	getTask.execute((Void) null);
    }
    
    public void gotoBeerReport() {
		Intent myIntent = new Intent(this, BeerReportActivity.class);
		startActivity(myIntent);
    }
    
    public void onBrowseRecipeClick(View v) {
		Intent myIntent = new Intent(this, BrowseRecipesActivity.class);
		startActivity(myIntent);
    }

	@Override
	public void onClick(View v) {
		
		Intent myIntent;
		
		switch(v.getId()) {
			case R.id.btn_new_recipe:
				//this.setContentView(R.layout.activity_create_recipe);
				myIntent = new Intent(this, CreateRecipeActivity.class);
				startActivity(myIntent);
				break;
			case R.id.btn_browse_recipes:
				myIntent = new Intent(this, BrowseRecipesActivity.class);
				startActivity(myIntent);
				break;
		}
		
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		 connectStatusView = findViewById(R.id.connect_status);
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