package com.gt.brewmasters.activities;

import com.gt.brewmasters.R;
import com.gt.brewmasters.db.BrewmasterDB;
import com.gt.brewmasters.db.RecipeDataSource;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BrowseRecipesActivity extends Activity{

	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
    
	Context mContext;
	SharedPreferences mPref;
	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	
	private static Brewmasters appContext;
	
	private ListView recipeLv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        initUi();
        
		//db
        RecipeDataSource recipeDatasource = new RecipeDataSource(this);
    	recipeDatasource.open();
    		
		recipes = recipeDatasource.getAllRecipes();
		recipeDatasource.close();	
		
		for(int i=0; i<recipes.size(); i++) {
			if(D) Log.v(TAG, "Recipe name: " + recipes.get(i).getName());
		}
		
		//adapter
		recipeLv = (ListView)findViewById(R.id.data_result_list);
		RecipeListAdapter recipeAdapter = new RecipeListAdapter(this, R.layout.recipe_row, recipes);
		recipeLv.setAdapter(recipeAdapter);
		recipeLv.setCacheColorHint(0);

		recipeLv.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
                // todo implement deletion of db records if this functionailty is desired

            	if (D) Log.i(TAG, "pos: " + pos);

                return true;
            }
        }); 
	}
    
    public void initUi(){
    	setContentView(R.layout.activity_browse_recipes);
    }

    	
	//click row
    public void alertstate(int npos) {
    	if(npos == 0)
    	{
    		if (D) Log.i(TAG, "dataactivity==========npos == 0");
    		return;
    	}
    	
	}
   
    private void showRecipe(int npos)
    {
       	Intent intent = new Intent(this, CreateRecipeActivity.class);
    	
//    	intent.putextra("retry", "retry");
//    	intent.putextra("pos", npos);
//    	intent.putextra("ro", "1");
    	startActivity(intent);
    }

}
