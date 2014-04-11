package com.gt.brewmasters.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import com.gt.brewmasters.structures.Recipe;
import com.gt.brewmasters.structures.RecipeInfo;

public class BrewmasterDB extends SQLiteOpenHelper
{
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "Brewmaster.db";
  public static final String TABLE_RECIPE = "tbl_recipe";
  public static final String TABLE_INGREDIENT = "tbl_ingredient";
  private static final String TABLE_RECIPE_INGREDIENT = "tbl_recipe_ingredient";

  private static final String ID = "id";
  
  public static final String RECIPE_ID 		      		= "id";
  public static final String RECIPE_NAME          		= "name";
  public static final String RECIPE_BEER_TYPE		  	= "beer_type";
  public static final String RECIPE_DESCRIPTION   		= "description";
  public static final String RECIPE_WATER_GRAIN_RATIO   = "water_grain_ratio";
  public static final String RECIPE_MASH_TEMP   		= "mash_temp";
  public static final String RECIPE_MASH_DURATION   	= "mash_duration";
  public static final String RECIPE_BOIL_DURATION    	= "boil_duration";
  
  public static final String INGREDIENT_ID 	      		= "id";
  public static final String INGREDIENT_RECIPE_ID 	    = "recipe_id";
  public static final String INGREDIENT_NAME      		= "name";
  public static final String INGREDIENT_TYPE      		= "type";
  public static final String INGREDIENT_DESCRIPTION   	= "description";
  public static final String INGREDIENT_AMOUNT    		= "amount";
  public static final String INGREDIENT_UNIT      		= "unit";
  public static final String INGREDIENT_ADD_TIME 		= "add_time";


  public BrewmasterDB(Context context)
  {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void DropTable(String tableName)
  {
    SQLiteDatabase oDB = getWritableDatabase();
    oDB.execSQL("DROP TABLE IF EXISTS " + tableName);
    onCreate(oDB);
  }

//  public void addRecipe(Recipe recipe)
//  {
//    SQLiteDatabase oDB = getWritableDatabase();
//    ContentValues oContentValues = new ContentValues();
//    
//    oContentValues.put(DEMO_UUID, oDemoInfo.getUUID());
//    
//    long nID = -1;
//    nID = oDB.insert(TABLE_RECIPE, null, oContentValues);
//    if(nID != -1)
//    	recipeInfo.setID((int)nID);
//    
//    oDB.close();
//  }

  public void deleteDemoInfo(RecipeInfo oDemoInfo)
  {
    SQLiteDatabase oDB = getWritableDatabase();
    String[] strID = new String[1];
    strID[0] = String.valueOf(oDemoInfo.getID());
    
    oDB.delete(TABLE_RECIPE, ID + " = ?", strID);
    oDB.close();
  }

	public ArrayList<Recipe> getAllRecipes()
	{
		ArrayList<Recipe> arrRecipeList = new ArrayList<Recipe>();
		Cursor cursor = null;
		
		try
		{
			String strQuery = "select * from " + TABLE_RECIPE + " ORDER BY " + "id" +" ASC";
			cursor = getWritableDatabase().rawQuery(
				//	strQuery, null);
				//"SELECT  * FROM tbl_demoinfo ORDER BY id", null);
				//"SELECT  * FROM " + TABLE_DEMOINFO + " ORDER BY " + "ID", null);
				"SELECT  * FROM tbl_recipes ORDER BY id ASC", null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

		boolean bFlag = cursor.moveToFirst();

		if (bFlag == false)
			return arrRecipeList;

		do {
//			Recipe recipe = new Recipe();
//			recipe.setID(Integer.parseInt(cursor.getString(DB_INDEX_ID)));
//			
//
//			arrRecipeList.add(recipe);
		} while (cursor.moveToNext());

		return arrRecipeList;
	}

  @Override
  public void onCreate(SQLiteDatabase oDB)
  {
	  String recipeCreate = "CREATE TABLE " + TABLE_RECIPE + "(" +
	  			RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				RECIPE_NAME + "UNIQUE TEXT," +
				RECIPE_DESCRIPTION + " TEXT," +
				RECIPE_BEER_TYPE + " TEXT," +
				RECIPE_WATER_GRAIN_RATIO + " TEXT," +
				RECIPE_MASH_TEMP + " TEXT," +
				RECIPE_MASH_DURATION + " TEXT," +
				RECIPE_BOIL_DURATION +" TEXT" + ");";

	  String ingredientCreate = "CREATE TABLE " + TABLE_INGREDIENT + "(" +
	  			INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				INGREDIENT_RECIPE_ID + " TEXT," +
				INGREDIENT_NAME + " TEXT," +
				INGREDIENT_TYPE + " TEXT," +
				INGREDIENT_DESCRIPTION + " TEXT," +
				INGREDIENT_AMOUNT + " TEXT," +
				INGREDIENT_UNIT + " TEXT," +
				INGREDIENT_ADD_TIME + " TEXT" + ");";
//	  String recipeIngredientCreate = "CREATE TABLE " + TABLE_RECIPE_INGREDIENT + "(" +
//	  			ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//				RECIPE_ID + " INT," +
//				INGREDIENT_ID + " INT," +
//				ADD_TIME + " TEXT" + ");";
	
	  try{
		  oDB.execSQL(recipeCreate);
		  oDB.execSQL(ingredientCreate);
		  //oDB.execSQL(recipeIngredientCreate);
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }

  @Override
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_demoinfo");
    onCreate(paramSQLiteDatabase);
  }
  

  public int updateRecord(RecipeInfo recipeInfo)
  {
    SQLiteDatabase oDB = getWritableDatabase();
    ContentValues oContentValues = new ContentValues();
    
//    oContentValues.put(DEMO_UUID, oDemoInfo.getUUID());
//    oContentValues.put(DEMO_DATE, oDemoInfo.getDate());
//    oContentValues.put(DEMO_NAME, oDemoInfo.getName());
//    oContentValues.put(DEMO_ADDRESS, oDemoInfo.getAddress());
//    oContentValues.put(DEMO_DOB, oDemoInfo.getDOB());
//    oContentValues.put(DEMO_IDNUMBER, oDemoInfo.getIDNumber());
//    oContentValues.put(DEMO_GENDER, oDemoInfo.getGender());
//    oContentValues.put(DEMO_REMARKS, oDemoInfo.getRemarks());
//    oContentValues.put(DEMO_STATE, oDemoInfo.getState());
//    
//    oContentValues.put(DEMO_IMG_PHOTO, oDemoInfo.getPhoto());
//    oContentValues.put(DEMO_IMG_DOC_1, oDemoInfo.getDoc1());
//    oContentValues.put(DEMO_IMG_DOC_2, oDemoInfo.getDoc2());
//    oContentValues.put(DEMO_IMG_DESC_1, oDemoInfo.getDesc1());
//    oContentValues.put(DEMO_IMG_DESC_2, oDemoInfo.getDesc2());
//    
//    oContentValues.put(DEMO_FP_RIM, oDemoInfo.getFpRIM());
//    oContentValues.put(DEMO_FP_LIM, oDemoInfo.getFpLIM());
//    oContentValues.put(DEMO_FP_RRL, oDemoInfo.getFpRRL());
//    oContentValues.put(DEMO_FP_LRL, oDemoInfo.getFpLRL());
//    oContentValues.put(DEMO_FP_BT, oDemoInfo.getFpBT());
//
//    oContentValues.put(DEMO_IRIS_LEFT, oDemoInfo.getIrisLeft());
//    oContentValues.put(DEMO_IRIS_RIGHT, oDemoInfo.getIrisRight());
//    
//    oContentValues.put(DEMO_ERR_MSG, oDemoInfo.getErrMsg());

    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(recipeInfo.getID());
    
    int nUpdate = oDB.update(TABLE_RECIPE, oContentValues, "id = ?", arrayOfString); 
    
    return nUpdate;
  }
}

