package com.gt.brewmasters.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gt.brewmasters.R;
import com.gt.brewmasters.structures.Recipe;

public class RecipeListAdapter extends ArrayAdapter<Recipe> implements android.content.DialogInterface.OnClickListener{

	private ArrayList<Recipe>recipeList;
	private Activity activity;
	private int layoutResourceId;
	private Button delButton;
	
    // Debugging
    private static final String TAG = "Brewmasters";
    private static final boolean D = true;
	
	public RecipeListAdapter(Activity activity, int layoutResourceId, ArrayList<Recipe> recipeList) {
        super(activity, layoutResourceId, recipeList);
        this.layoutResourceId=layoutResourceId;
        this.activity = activity;
        this.recipeList=recipeList;
        //if(D) Log.v(TAG, "Created adapater")
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
        Activity activity = (Activity) getContext();
        LayoutInflater inflater = activity.getLayoutInflater();
        
        // Inflate the views from XML
        row = inflater.inflate(layoutResourceId, parent, false);
        Recipe recipe = getItem(position);

        // Set the text on the TextView
        TextView recipeNameTv   = (TextView) row.findViewById(R.id.recipe_name);
        TextView beerTypeTv = (TextView) row.findViewById(R.id.recipe_beer_type);
        
        recipeNameTv.setText(recipe.getName());
        beerTypeTv.setText(recipe.getBeerType());
        
        row.setTag(position);
        
        return row;
    }
	
    public ArrayList<Recipe> getRecipeList(){
    	return this.recipeList;
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	}
	
}