package com.gt.brewmasters.fragments;

import com.gt.brewmasters.R;
import com.gt.brewmasters.activities.Brewmasters;
import com.gt.brewmasters.activities.BrowseRecipesActivity;
import com.gt.brewmasters.activities.CreateRecipeActivity;
import com.gt.brewmasters.activities.FragmentPagerSupport;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class RecipeManager extends Fragment{
	
	// Debugging
    private static final String TAG = "Brewmasters";
    private static final boolean D  = true;
    
    Button newRecipe;
    Button browseRecipe;
    
    public static Brewmasters appContext;
    static FragmentPagerSupport context;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recipe_screen, container, false);
			
			appContext = Brewmasters.getAppContext();
	        
	        rootView.findViewById(R.id.btn_new_recipe).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
					Intent recipeIntent = new Intent(RecipeManager.this.getActivity(), CreateRecipeActivity.class);
					startActivity(recipeIntent);
	            }
	        });
	        
	        rootView.findViewById(R.id.btn_browse_recipes).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
					Intent recipeIntent = new Intent(RecipeManager.this.getActivity(), BrowseRecipesActivity.class);
					startActivity(recipeIntent);
	            }
	        });
			
			return rootView;
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(D) Log.d(TAG, "Fragment created");
		
		Brewmasters appContext = (Brewmasters) getActivity().getApplicationContext();
	}
		
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(D) Log.d(TAG, "Fragment destroyed");
	}
	
}