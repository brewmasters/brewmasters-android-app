package com.gt.brewmasters.activities;

import com.gt.brewmasters.R;

import android.content.Intent;
import android.os.Bundle;
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

public class FragmentPagerSupport extends FragmentActivity implements OnClickListener {
	
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
	
    static final int NUM_ITEMS = 3;
    
    static final int COMMUNITY_SCREEN = 0;
    static final int CONNECT_SCREEN   = 1;
    static final int RECIPE_SCREEN    = 2;
    

    MyAdapter mAdapter;

    ViewPager mPager;
    public static Brewmasters appContext;
    static FragmentPagerSupport context;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);
        
        context = this;
        
        appContext = Brewmasters.getAppContext();

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        // Watch for button clicks.
//        Button button = (Button)findViewById(R.id.goto_first);
//        button.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(0);
//            }
//        });
//        button = (Button)findViewById(R.id.goto_last);
//        button.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                mPager.setCurrentItem(NUM_ITEMS-1);
//            }
//        });
    }

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
        			break;
        			
        		case RECIPE_SCREEN:
        			v = inflater.inflate(R.layout.fragment_recipe_screen, container, false);
                    tv = v.findViewById(R.id.text);
                    ((TextView)tv).setText("Recipe Management");
                    
                    Button createRecipe = (Button) v.findViewById(R.id.btn_new_recipe);
                    
//                    OnClickListener recipeListener = new OnClickListener() {
//                    	Intent myIntent;
//						@Override
//						public void onClick(View v) {
//							switch(v.getId()) {
//								case R.id.btn_new_recipe:
//									Log.v(TAG, "here");
//				        			v = appContext.getInflater().inflate(R.layout.activity_create_recipe, appContext.getViewGroup(), false);
////				                    tv = v.findViewById(R.id.text);
////				                    ((TextView)tv).setText("Device Management");
////									myIntent = new Intent(getActivity(), CreateRecipeActivity.class);
////									startActivity(myIntent);
//							}
//						}
//                    };
                    
                    createRecipe.setOnClickListener(context);
                    
                    
        			break;
        			
        		case COMMUNITY_SCREEN:
        			break;
        			
        		
        	}
        	
        	return v;
        	
//            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
//            View tv = v.findViewById(R.id.text);
//            ((TextView)tv).setText("Fragment #" + mNum);
//            return v;
        }

//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//            String[] Cheeses= new String[] {"sd", "sdd"};
//            //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Cheeses));
//        }

//        @Override
//        public void onListItemClick(ListView l, View v, int position, long id) {
//            Log.i("FragmentList", "Item clicked: " + id);
//        }
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
}