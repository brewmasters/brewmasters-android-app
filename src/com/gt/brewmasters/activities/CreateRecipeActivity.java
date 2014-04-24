package com.gt.brewmasters.activities;

import java.io.File;
import java.util.ArrayList;

import com.gt.brewmasters.R;
import com.gt.brewmasters.db.IngredientDataSource;
import com.gt.brewmasters.db.RecipeDataSource;
import com.gt.brewmasters.structures.Ingredient;
import com.gt.brewmasters.structures.Recipe;
import com.gt.brewmasters.utils.IngredientListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class CreateRecipeActivity extends Activity {
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
    
    private static final int ADD_INGREDIENT=1;
	
	private static Brewmasters appContext;
	private Button addIngredient;
	private ListView ingredientListView;
	IngredientListAdapter adapter;
	
	//recipe Fields
	public EditText recipeNameEt;
	public EditText recipeBeerTypeEt;
	public EditText recipeDescriptionEt;
	public EditText recipeRatioEt;
	public EditText recipeMashTempEt;
	public EditText recipeMashDurEt;
	public EditText recipeBoilDurEt;
	
	//recipe field info
	public String recipeName;
	public String recipeBeerType;
	public String recipeDescription;
	public String recipeRatio;
	public String recipeMashTemp;
	public String recipeMashDur;
	public String recipeBoilDur;
	
	//ingredient field info
	public String ingredientName;
	public String ingredientType;
	public String ingredientDescription;
	public String ingredientAmount;
	public String ingredientUnit;
	public String ingredientAddTime;
	
	//Ingredient list
	ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
	private boolean editing;
	private long recipeId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editing = false;
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        Bundle bundle = getIntent().getExtras();
        int pos = parseBundle(bundle);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initUi();
        if(editing) {
        	Recipe recipe = getRecipe(pos);
        	loadRecipe(recipe);
        	recipeId=recipe.getId();
        }
        
    }
    
    public int parseBundle(Bundle bundle) {
    	int retVal = -1;
    	if (bundle != null) {
			String action = bundle.getString("action");
			if(action.equals("edit"))
			{
				editing=true;
				retVal = bundle.getInt("recipePos");
			}
		}
    	if(D) Log.v(TAG,"pos: " + String.valueOf(retVal));
    	return retVal;
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
    
    public void loadRecipe(Recipe recipe) {
    	
    	recipeNameEt.setText(recipe.getName());
    	recipeBeerTypeEt.setText(recipe.getBeerType());
    	recipeDescriptionEt.setText(recipe.getDescription());
    	recipeRatioEt.setText(String.valueOf(recipe.getWaterGrainRatio()));
    	recipeMashTempEt.setText(String.valueOf(recipe.getMashTemp()));
    	recipeMashDurEt.setText(String.valueOf(recipe.getMashDuration()));
    	recipeBoilDurEt.setText(String.valueOf(recipe.getBoilDuration()));

    	IngredientDataSource db = new IngredientDataSource(this);
    	db.open();
    	ArrayList<Ingredient> ingredients = db.getIngredients(recipe.id);
    	db.close();
    	
    	for(int i=0; i<ingredients.size(); i++) {
    		Ingredient ingredient = ingredients.get(i);
    		if(D) Log.v(TAG, ingredient.getName());
    		ingredientList.add(ingredient);
    	}
    }
    
    public void initUi(){
    	setContentView(R.layout.activity_create_recipe);
    	
    	View header = getLayoutInflater().inflate(R.layout.create_recipe_header, null); 
    	View footer = getLayoutInflater().inflate(R.layout.create_recipe_footer, null); 
    	
    	ingredientListView = (ListView) findViewById(R.id.recipe_ingredient_list);
    	ingredientListView.addHeaderView(header);
    	ingredientListView.addFooterView(footer);
    	
    	if(editing) {
        	Button addRecipe   = (Button) findViewById(R.id.recipe_create_button);
        	addRecipe.setText("Edit");    		
    	}
    	
    	addIngredient = (Button) this.findViewById(R.id.recipe_add_ingredient_button);

    	//recipe Fields
    	recipeNameEt 	    = (EditText) findViewById(R.id.recipe_name_et);
    	recipeBeerTypeEt    = (EditText) findViewById(R.id.recipe_type_et);
    	recipeDescriptionEt = (EditText) findViewById(R.id.recipe_description_et);
    	recipeRatioEt 	    = (EditText) findViewById(R.id.grain_to_water_et);
    	recipeMashTempEt 	= (EditText) findViewById(R.id.mash_temp_et);
    	recipeMashDurEt 	= (EditText) findViewById(R.id.mash_duration_et);
    	recipeBoilDurEt 	= (EditText) findViewById(R.id.boil_duration_et);
    	
    	createIngredientList();
    	
    }
    
    public void createIngredientList() {
    	//setup the listView
    	ListView ingredients = (ListView) this.findViewById(R.id.recipe_ingredient_list);
    	
		//ArrayAdapter<CharSequence> adapter = ArrayAdapter(R.layout.profile_info_row);
		adapter = new IngredientListAdapter(this, R.layout.ingredient_row, ingredientList);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ingredients.setAdapter(adapter);
		
    }
    
    public void onRemoveClick(View v) {
	    int pos = (Integer)v.getTag();
	    Ingredient ingredientRemove = adapter.getItem(pos);
	    IngredientDataSource db = new IngredientDataSource(this);
	    db.open();
	    db.deleteIngredient(ingredientRemove);
	    db.close();
	    adapter.remove(ingredientRemove);
	    //updateIngredientList();
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode != RESULT_OK)
			return;

		switch(requestCode)
		{

		case ADD_INGREDIENT:
			if (D) Log.v(TAG, "back from add ingredient");
        	if(data!=null) {
        		Bundle extras = data.getExtras();
        		Bundle ingredientInfo = extras.getBundle("ingredient");
        		//if (D) Log.v(TAG, ingredientInfo.toString());
        		
            	String name =  ingredientInfo.getString("name");
            	int amount = ingredientInfo.getInt("amount");
            	String unit = ingredientInfo.getString("unit");
            	String ingredientType = ingredientInfo.getString("ingredientType");
            	String description = ingredientInfo.getString("description");
            	int addTime = ingredientInfo.getInt("addTime");
            	
            	Ingredient ingredient = new Ingredient(name, ingredientType, amount, unit, description, addTime);
            	//Log.v(TAG, ingredient.toString());
            	//Log.v(TAG, "just created");
            	if(editing){
            		IngredientDataSource db = new IngredientDataSource(this);
                	db.open();
                	db.createIngredient(recipeId, name, ingredientType, description, String.valueOf(amount), unit, String.valueOf(addTime));
                	db.close();
            	}
            	
            	adapter.add(ingredient);
        		
        	}
			break;
			
		}		
	}   
    
    public void onIngredientAdd(View v) {
    	if(D) Log.v(TAG, "ingredient add");
    	Intent ingredientAddIntent = new Intent(this, AddIngredientActivity.class);
    	this.startActivityForResult(ingredientAddIntent, this.ADD_INGREDIENT);
    }
    
    public void onRecipeCreate(View v) {
    	if(D) Log.v(TAG, "recipe create");
    	
    	//get all recipe field info
    	recipeName 		  = recipeNameEt.getText().toString();
    	recipeBeerType 	  = recipeBeerTypeEt.getText().toString();
    	recipeDescription = recipeDescriptionEt.getText().toString();
    	recipeRatio 	  = recipeRatioEt.getText().toString();
    	recipeMashTemp 	  = recipeMashTempEt.getText().toString();
    	recipeMashDur 	  = recipeMashDurEt.getText().toString();
    	recipeBoilDur 	  = recipeBoilDurEt.getText().toString();
    	
    	if(isValidRecipe()) {
    		RecipeDataSource recipeDatasource = new RecipeDataSource(this);
    		if(editing) {
    			Log.v(TAG, "RECIPE NAME: " + recipeName +"SD");
    			Recipe recipe = new Recipe(recipeName, recipeBeerType, recipeDescription,
						  Float.valueOf(recipeRatio), Integer.valueOf(recipeMashTemp), Integer.valueOf(recipeMashDur), Integer.valueOf(recipeBoilDur));
    			recipe.setId(recipeId);
    			Log.v(TAG, "RECIPE id: " + recipeId +"SD");
    			recipeDatasource.open();
    			recipeDatasource.updateRecipe(recipe);
    			recipeDatasource.close();
    			
            	this.setResult(Activity.RESULT_OK);
            	finish();	
    		}
    		
    		else {
            	recipeDatasource.open();
            	
            	//create the recipe and add to database
            	Recipe recipe = recipeDatasource.createRecipe(recipeName, recipeBeerType, recipeDescription,
            								  recipeRatio, recipeMashTemp, recipeMashDur, recipeBoilDur);
            	
            	recipeDatasource.close();
            	
            	//get the id for the recipe to use as the foreign key in ingredient
            	long recipeId = recipe.getId();
            	
            	IngredientDataSource ingredientDatasource = new IngredientDataSource(this);
            	ingredientDatasource.open();
            	
            	//clear out the old list before we regenerate it
            	if(recipe.ingredients != null) {
            		recipe.ingredients.clear();
            	}
            	
            	//For each ingredient in the ingredient list, create it with the given recipeId
            	for(int i=0; i<this.ingredientList.size(); i++) {
            		
            		//Take partially filled out Ingredient object from list
            		Ingredient ingredient = ingredientList.get(i);
            		
            		//ingredient field info
            		ingredientName 		  = ingredient.getName();
            		ingredientType 		  = ingredient.getType();
            		ingredientDescription = ingredient.getDescription();
            		ingredientAmount 	  = String.valueOf(ingredient.getAmount());
            		ingredientUnit 		  = ingredient.getUnit();
            		ingredientAddTime 	  = String.valueOf(ingredient.getAddTime());
            		
            		//add ingredient to recipe
            		recipe.ingredients.add(ingredient);
            		
            		//Add to database and recreate to finalize
            		ingredientDatasource.createIngredient(recipeId, ingredientName, ingredientType, ingredientDescription, 
            											  ingredientAmount, ingredientUnit, ingredientAddTime);
            	}
            	ingredientDatasource.close();
            	appContext.makeToast("Recipe successfully created.");
            	finish();
        	}
    	}
    	else {
    		appContext.makeToast("Please fill out all fields and add atleast one ingredient");
    	}
    }
    
    private Boolean isValidRecipe() {
    	Boolean retVal = false;
    	
    	//if ingredient list has atleast one element and if no fields are blank
    	if(ingredientList.size()>0 && !recipeName.contentEquals("") && !recipeBeerType.contentEquals("")
    		&& !recipeDescription.contentEquals("") && !recipeRatio.contentEquals("") 
    		&& !recipeMashTemp.contentEquals("") && !recipeMashDur.contentEquals("") && !recipeBoilDur.contentEquals("")) {
    		retVal = true;
    	}
    	
    	return retVal;
    }
}
