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

public class IngredientDataSource {

	// Database fields
	private SQLiteDatabase database;
	private BrewmasterDB db;
	private String[] ingredientColumns = { BrewmasterDB.INGREDIENT_ID,
			BrewmasterDB.INGREDIENT_RECIPE_ID, BrewmasterDB.INGREDIENT_NAME,
			BrewmasterDB.INGREDIENT_TYPE, BrewmasterDB.INGREDIENT_DESCRIPTION,
			BrewmasterDB.INGREDIENT_AMOUNT, BrewmasterDB.INGREDIENT_UNIT,
			BrewmasterDB.INGREDIENT_ADD_TIME };

	public IngredientDataSource(Context context) {
		db = new BrewmasterDB(context);
	}

	public void open() throws SQLException {
		database = db.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	public Ingredient createIngredient(long recipeId, String name, String type, String description, String amount, String unit, String addTime) {
		ContentValues values = new ContentValues();
		values.put(BrewmasterDB.INGREDIENT_RECIPE_ID, recipeId);
		values.put(BrewmasterDB.INGREDIENT_NAME, name);
		values.put(BrewmasterDB.INGREDIENT_TYPE, type);
		values.put(BrewmasterDB.INGREDIENT_DESCRIPTION, description);
		values.put(BrewmasterDB.INGREDIENT_AMOUNT, amount);
		values.put(BrewmasterDB.INGREDIENT_UNIT, unit);
		values.put(BrewmasterDB.INGREDIENT_ADD_TIME, addTime);
		
		long insertId = database.insert(BrewmasterDB.TABLE_INGREDIENT, null, values);
		// To show how to query
		Cursor cursor = database.query(BrewmasterDB.TABLE_INGREDIENT,
				ingredientColumns, BrewmasterDB.RECIPE_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		return cursorToIngredient(cursor);

	}

	public void deleteIngredient(Ingredient ingredient) {
		long id = ingredient.getId();
		//System.out.println("Recipe deleted with id: " + id);
		database.delete(BrewmasterDB.TABLE_INGREDIENT, BrewmasterDB.INGREDIENT_ID
				+ " = " + id, null);
	}
	
	public List<Ingredient> getAllIngredients() {
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		Cursor cursor = database.query(BrewmasterDB.TABLE_INGREDIENT,
				ingredientColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Ingredient ingredient = cursorToIngredient(cursor);
			ingredients.add(ingredient);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return ingredients;
	}
	
	//Gets all ingredients with given recipe ID
	public ArrayList<Ingredient> getIngredients(long id) {
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		String sql = "SELECT * FROM " + BrewmasterDB.TABLE_INGREDIENT + " WHERE recipe_id = '"+String.valueOf(id)+"'";
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Ingredient ingredient = cursorToIngredient(cursor);
			ingredients.add(ingredient);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return ingredients;
	}
	
	private Ingredient cursorToIngredient(Cursor cursor) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId(cursor.getLong(0));
		ingredient.setRecipeId(cursor.getLong(1));
		ingredient.setName(cursor.getString(2));
		ingredient.setType(cursor.getString(3));
		ingredient.setDescription(cursor.getString(4));
		ingredient.setAmount(Integer.valueOf(cursor.getString(5)));
		ingredient.setUnit(cursor.getString(6));
		ingredient.setAddTime(Integer.valueOf(cursor.getString(7)));
		return ingredient;
	}

}
