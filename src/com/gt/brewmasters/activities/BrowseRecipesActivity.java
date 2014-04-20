package com.gt.brewmasters.activities;

import com.gt.brewmasters.R;
import com.gt.brewmasters.db.BrewmasterDB;
import com.gt.brewmasters.db.IngredientDataSource;
import com.gt.brewmasters.db.RecipeDataSource;
import com.gt.brewmasters.structures.Ingredient;
import com.gt.brewmasters.structures.Recipe;
import com.gt.brewmasters.utils.RecipeListAdapter;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BrowseRecipesActivity extends Activity{

	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
    
    public static final int EDIT_RECIPE = 1;
    
	Context mContext;
	SharedPreferences mPref;
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	private static Brewmasters appContext;
	
	private ListView recipeLv;
	private Boolean selecting = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initUi();
        
		recipes = getRecipesFromDb();
		
		for(int i=0; i<recipes.size(); i++) {
			if(D) Log.v(TAG, "Recipe name: " + recipes.get(i).getName());
		}
		
		Bundle bundle = getIntent().getExtras();
        
    	if (bundle != null) {
			String action = bundle.getString("action");
			if(action.equals("select"))
			{
				selecting=true;
			}
		}
		
		//adapter
		recipeLv = (ListView)findViewById(R.id.data_result_list);
		View footer = getLayoutInflater().inflate(R.layout.activity_browse_recipes_footer, null);
		recipeLv.addFooterView(footer);
		RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, R.layout.recipe_row, recipes);
		recipeLv.setAdapter(recipeAdapter);
		
		if(selecting) {
			
			OnItemClickListener selectListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Bundle bundle = new Bundle();
					bundle.putInt("pos", position);
					Intent resultIntent = new Intent();
		    		resultIntent.putExtra("RecipePos", bundle);
	            	BrowseRecipesActivity.this.setResult(Activity.RESULT_OK, resultIntent);
	            	finish();	
				}		
			};
			
			recipeLv.setOnItemClickListener(selectListener);
		}
		else {
			recipeLv.setOnItemClickListener(recipeAdapter);	
		}
		

	}
    
    public ArrayList<Recipe> getRecipesFromDb() {
    	//db
        RecipeDataSource recipeDatasource = new RecipeDataSource(this);
    	recipeDatasource.open();
    		
		ArrayList<Recipe> recipes = recipeDatasource.getAllRecipes();
		recipeDatasource.close();	
		return recipes;
    }
    
    public void initUi(){
    	setContentView(R.layout.activity_browse_recipes);
    }
    
    public void onBackClick(View v) {
    	finish();
    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
			return;

		switch(requestCode)
		{
			case EDIT_RECIPE:
				//adapter
				recipes = getRecipesFromDb();
				
				recipeLv = (ListView)findViewById(R.id.data_result_list);
				RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, R.layout.recipe_row, recipes);
				recipeLv.setAdapter(recipeAdapter);
				recipeLv.setOnItemClickListener(recipeAdapter);
				break;
		}
	}

}
