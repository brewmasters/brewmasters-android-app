package com.gt.brewmasters.fragments;

import com.gt.brewmasters.R;
import com.gt.brewmasters.activities.Brewmasters;
import com.gt.brewmasters.activities.BrowseRecipesActivity;
import com.gt.brewmasters.activities.CreateRecipeActivity;

import android.app.Fragment;
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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_recipe_screen, container, false);
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

//	@Override
//	public void onClick(View v) {
//		
//		Intent myIntent;
//		
//		switch(v.getId()) {
//			
//		case R.id.btn_new_recipe:
//			myIntent = new Intent(getActivity(), CreateRecipeActivity.class);
//			startActivity(myIntent);
//			break;
//			
//		case R.id.btn_browse_recipes:
//			myIntent = new Intent(getActivity(), BrowseRecipesActivity.class);
//			startActivity(myIntent);
//			break;
//			
//		}
//	}
	
}