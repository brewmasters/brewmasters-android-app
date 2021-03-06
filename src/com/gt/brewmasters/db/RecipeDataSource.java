package com.gt.brewmasters.db;

import java.util.ArrayList;
import java.util.List;

import com.gt.brewmasters.structures.Ingredient;
import com.gt.brewmasters.structures.Recipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecipeDataSource {

	// Database fields
	private SQLiteDatabase database;
	private BrewmasterDB db;
	private String[] recipeColumns = { BrewmasterDB.RECIPE_ID,
			BrewmasterDB.RECIPE_NAME, BrewmasterDB.RECIPE_DESCRIPTION,
			BrewmasterDB.RECIPE_BEER_TYPE, BrewmasterDB.RECIPE_WATER_GRAIN_RATIO,
			BrewmasterDB.RECIPE_MASH_TEMP, BrewmasterDB.RECIPE_MASH_DURATION,
			BrewmasterDB.RECIPE_BOIL_DURATION };

	public RecipeDataSource(Context context) {
		db = new BrewmasterDB(context);
		db.onCreate(db.getWritableDatabase());
	}

	public void open() throws SQLException {
		database = db.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	public Recipe createRecipe(String name, String type, String description, String ratio, String mashTemp, String mashDur, String boilDur) {
		ContentValues values = new ContentValues();
		values.put(BrewmasterDB.RECIPE_NAME, name);
		values.put(BrewmasterDB.RECIPE_BEER_TYPE, type);
		values.put(BrewmasterDB.RECIPE_DESCRIPTION, description);
		values.put(BrewmasterDB.RECIPE_WATER_GRAIN_RATIO, ratio);
		values.put(BrewmasterDB.RECIPE_MASH_TEMP, mashTemp);
		values.put(BrewmasterDB.RECIPE_MASH_DURATION, mashDur);
		values.put(BrewmasterDB.RECIPE_BOIL_DURATION, boilDur);
		
		long insertId = database.insert(BrewmasterDB.TABLE_RECIPE, null, values);
		// To show how to query
		Cursor cursor = database.query(BrewmasterDB.TABLE_RECIPE,
				recipeColumns, BrewmasterDB.RECIPE_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		return cursorToRecipe(cursor);

	}

	public void deleteRecipe(Recipe recipe) {
		long id = recipe.getId();
		System.out.println("Recipe deleted with id: " + id);
		database.delete(BrewmasterDB.TABLE_RECIPE, BrewmasterDB.RECIPE_ID
				+ " = '" + id+"'", null);
	}
	
	public void updateRecipe(Recipe recipe) {
		long id = recipe.getId();
		Log.v("Brewmaster", "Recipe updated with name: " + recipe.getName() +"sd");
		ContentValues args = new ContentValues();
	    args.put("name", recipe.getName());
	    args.put("beer_type", recipe.getBeerType());
	    args.put("description", recipe.getDescription());
	    args.put("water_grain_ratio", String.valueOf(recipe.getWaterGrainRatio()));
	    args.put("mash_temp", String.valueOf(recipe.getMashTemp()));
	    args.put("mash_duration", String.valueOf(recipe.getMashDuration()));
	    args.put("boil_duration", String.valueOf(recipe.getBoilDuration()));
	    
//	    String sql = "UPDATE "+ BrewmasterDB.TABLE_RECIPE + 
//	    		" SET name="+recipe.getName()
//	    		+", beer_type="+recipe.getBeerType()
//	    		+", description="+recipe.getDescription()
//	    		+", water_grain_ratio="+recipe.getWaterGrainRatio()
//	    		+", mash_temp="+recipe.getMashTemp()
//	    		+", mash_duration="+recipe.getMashDuration()
//	    		+", boil_duration="+recipe.getBoilDuration()
//	    		+" WHERE id='"+id+"'";
//	    
//	    Log.v("Brewmaster", sql);
//	    
//	    Cursor cursor = database.rawQuery(sql, null);

		database.update(BrewmasterDB.TABLE_RECIPE, args, BrewmasterDB.RECIPE_ID
				+ " = '" + id+"'" , null);
		
	}
	
	public ArrayList<Recipe> getAllRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Cursor cursor = database.query(BrewmasterDB.TABLE_RECIPE,
				recipeColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Recipe recipe = cursorToRecipe(cursor);
			recipes.add(recipe);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return recipes;
	}
	
	private Recipe cursorToRecipe(Cursor cursor) {
		Recipe recipe = new Recipe();
		recipe.setId(cursor.getLong(0));
		recipe.setName(cursor.getString(1));
		recipe.setDescription(cursor.getString(2));
		recipe.setBeerType(cursor.getString(3));
		recipe.setWaterGrainRatio(Float.valueOf(cursor.getString(4)));
		recipe.setMashTemp(Integer.valueOf(cursor.getString(5)));
		recipe.setMashDuration(Integer.valueOf(cursor.getString(6)));
		recipe.setBoilDuration(Integer.valueOf(cursor.getString(7)));
		recipe.ingredients = new ArrayList<Ingredient>();
		return recipe;
	}

}
