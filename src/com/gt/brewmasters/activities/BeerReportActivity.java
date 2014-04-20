package com.gt.brewmasters.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gt.brewmasters.R;
import com.gt.brewmasters.db.IngredientDataSource;
import com.gt.brewmasters.db.RecipeDataSource;
import com.gt.brewmasters.structures.Ingredient;
import com.gt.brewmasters.structures.Recipe;
import com.gt.brewmasters.utils.HttpTask;

public class BeerReportActivity extends Activity {
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
	
    private static final int BROWSE_RECIPE=1;
    
	private Brewmasters appContext;
	private ListView reportListView;
	
	private Button selectRecipe;
	private Button brewButton;
	
	private LinearLayout llBrewDetails;
	
	//Recipe we want to send to machine to brew
	private Recipe brewRecipe;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        initUi();
        
    }
    
    public void initUi(){
    	setContentView(R.layout.activity_brew_report);

    	selectRecipe = (Button) findViewById(R.id.btn_select_recipe);
    	brewButton = (Button) findViewById(R.id.btn_brew);
    	
    	llBrewDetails = (LinearLayout) findViewById(R.id.brew_details);
    	
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
			return;

		switch(requestCode)
		{

		case BROWSE_RECIPE:
			if (D) Log.v(TAG, "back from select ingredient");
        	if(data!=null) {
        		Bundle extras = data.getExtras();
        		Bundle recipePos = extras.getBundle("RecipePos");
        		int pos = recipePos.getInt("pos");
        		Recipe recipe = this.getRecipe(pos);
        		
        		//initialize ingredient list variable
        		this.getIngredients(recipe);
        		
        		//generate textviews
        		brewRecipe = this.generateRecipeDetails(recipe);
        	}
			break;
		}		
	}
    
    public Recipe generateRecipeDetails(Recipe recipe) {
    	String name = recipe.getName();
    	
    	//clear any views that might be on the ll already
    	llBrewDetails.removeAllViews();
    	
    	TextView tv = new TextView(this);
    	tv.setText(" ");
    	llBrewDetails.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Recipe Name: " +name);
    	llBrewDetails.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText(" ");
    	llBrewDetails.addView(tv);
    	
    	tv = new TextView(this);
    	tv.setText("Ingredients");
    	llBrewDetails.addView(tv);
    	
    	//Iterate through recipe ingredients and generate a textview for each 
    	for(int i=0; i<recipe.ingredients.size(); i++) {
    		Ingredient ingredient = recipe.ingredients.get(i);
        	tv = new TextView(this);
        	String text = "Name: " + ingredient.getName()+" Type: "+ ingredient.getType()+" Amount: " +ingredient.getAmount() +" "+ingredient.getUnit();
        	Log.v(TAG, text);
        	tv.setText(text);
        	llBrewDetails.addView(tv);
    	}
    	
    	tv = new TextView(this);
    	tv.setText(" ");
    	llBrewDetails.addView(tv);
    	
    	return recipe;

    }
    
    //Handler that receives messages from post execute to tell us when http post has been completed
  	Handler brewHandler = new Handler() {
  	    @Override
  	    public void handleMessage(Message msg) 
  	    {
  	    	super.handleMessage(msg);
  	    	//showProgress(false);
  	    	
  	    	if(D) Log.e(TAG, "=============== pagerHandler == received");

  	    	if (msg.what == HttpTask.BAD_RESPONSE)
  	    	{
  				appContext.makeToast("Failed to communicate with device. Check power/connectivity");
      			Log.v(TAG, "bad response");
  	    	}
  	    	
  	    	else if (msg.what == HttpTask.RESPONSE)
  	    	{
  	    		Bundle data = msg.getData();
  	    		Log.v(TAG, "data: " + data.toString());

  	    	}
  	    }
  	};
    
    public void onBrewClick(View v) {
    	Log.v(TAG, "Brew");
    	if(brewRecipe != null) {
    		
    		JSONObject brewRequest = this.createBrewRequestJSON(brewRecipe);
    		
    		Log.v(TAG, brewRequest.toString());
    		
    		String loginUrl = Brewmasters.getDeviceAddress(this)+"/recipe";
    		
    		HttpTask postTask = new HttpTask(loginUrl, brewRequest, this.brewHandler, HttpTask.POST);
    		postTask.execute((Void) null);
    	}
    	
    	else {
    		appContext.makeToast("Please select a recipe to brew before continuing");
    	}
    }
    
    public void onSelectRecipeClick(View v) {
    	Log.v(TAG, "select recipe");
    	
    	Intent selectRecipeIntent = new Intent(this, BrowseRecipesActivity.class);
    	selectRecipeIntent.putExtra("action", "select");
    	startActivityForResult(selectRecipeIntent, this.BROWSE_RECIPE);
    }
    
    public Recipe getRecipe(int pos) {
    	Recipe recipe;
    	RecipeDataSource db = new RecipeDataSource(this);
    	db.open();
    	ArrayList<Recipe> recipes = db.getAllRecipes();
    	db.close();
    	recipe = recipes.get(pos);
    	return recipe;
    }
    
    public void getIngredients(Recipe recipe) {
    	
    	IngredientDataSource db = new IngredientDataSource(this);
    	db.open();
    	ArrayList<Ingredient> ingredients = db.getIngredients(recipe.getId());
    	db.close();
    	
    	Log.v(TAG, ingredients.toString());
    	recipe.setIngredients(ingredients);
    	
    }
    
	public JSONObject createBrewRequestJSON(Recipe recipe) {
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		JSONArray array = new JSONArray();
		Map recipeData=new LinkedHashMap();
		
		recipeData.put("id", String.valueOf(recipe.getId()));
		recipeData.put("name", String.valueOf(recipe.getName()));
		recipeData.put("description", recipe.getDescription());
		recipeData.put("water_grain_ratio", String.valueOf(recipe.getWaterGrainRatio()));
		recipeData.put("beer_type", recipe.getBeerType());
		recipeData.put("mash_temperature", String.valueOf(recipe.getMashTemp()));
		recipeData.put("boil_duration", String.valueOf(recipe.getBoilDuration()));
		recipeData.put("mash_duration", String.valueOf(recipe.getMashDuration()));
		array.put(gson.toJson(recipeData));
		ArrayList<Ingredient> ingredients = recipe.getIngredients();
		
		
		Map ingredientData=new LinkedHashMap();
		
		ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(int i=0; i<ingredients.size(); i++) {
			
			Ingredient ingredient = ingredients.get(i);
			ingredientData.put("id", String.valueOf(ingredient.getId()));
			ingredientData.put("recipe_id", String.valueOf(ingredient.getRecipeId()));
			ingredientData.put("name", ingredient.getName());
			ingredientData.put("type", ingredient.getType());
			ingredientData.put("description", ingredient.getDescription());
			ingredientData.put("amount", String.valueOf(ingredient.getAmount()));
			ingredientData.put("unit", ingredient.getUnit());
			ingredientData.put("add_time", String.valueOf(ingredient.getAddTime()));
			String json = gson.toJson(ingredient);
			
			
			Log.v(TAG, json);
			array.put(json);
		}
		
		//Gson gson = new Gson(); // Or use new GsonBuilder().create();
		//recipe.setIngredients(null);
		String json = gson.toJson(recipe);
		Log.v(TAG, array.toString());
		
		Map data=new LinkedHashMap();
		data.put("recipe", array);
		
		
		JSONObject brewDataJson = appContext.createJSON(data);
		
		return brewDataJson;
	}
}
