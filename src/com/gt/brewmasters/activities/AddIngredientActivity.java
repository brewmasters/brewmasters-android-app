package com.gt.brewmasters.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gt.brewmasters.R;

public class AddIngredientActivity extends Activity {
	// Debugging
    private static final String TAG = "Brewmaster";
    private static final boolean D  = true;
    //for example....
    //if(D) Log.d(TAG, "not connected");
	
	private static Brewmasters appContext;
	
	private EditText ingredientName;
	
	private EditText unitAmount;
	private Spinner unitSpinner;
	
	private EditText typeName;
	private Spinner typeSpinner;
	
	private EditText addTimeEt;
	private EditText descriptionEt;
	
	//Ingredient info
	String name;
	String ingredientType;
	int amount = -1;
	String unit;
	String description;
	int addTime;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get context for global application vars
        appContext = Brewmasters.getAppContext();
        
        initUi();
        
    }
    
    public void initUi(){
    	setContentView(R.layout.add_ingredient);
    	
    	ingredientName = (EditText) findViewById(R.id.ingredient_name_et);
    	
    	unitAmount     = (EditText) findViewById(R.id.ingredient_amount_et);
    	unitSpinner    = (Spinner)  findViewById(R.id.unit_spinner);
    	
    	typeName	   = (EditText) findViewById(R.id.ingredient_type_name_et);
    	typeSpinner	   = (Spinner)  findViewById(R.id.type_spinner);
    	
    	addTimeEt 	   = (EditText) findViewById(R.id.ingredient_add_time_et);
    	descriptionEt  = (EditText) findViewById(R.id.ingredient_description_et);
    	
    	
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
    		R.array.units_of_measure, android.R.layout.simple_spinner_item);
    	// Specify the layout to use when the list of choices appears
    	unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	unitSpinner.setAdapter(unitAdapter);
    	
    	ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
    	    R.array.ingredient_types, android.R.layout.simple_spinner_item);
    	typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	typeSpinner.setAdapter(typeAdapter);
    }
    
    //Returns a bundle that contains all necessary information to create an ingredient.
    //This activity will pass back to the parent activity when the result is set
    private Bundle getIngredientInfo() {
    	Bundle myBundle = new Bundle();
    	myBundle.putString("name", name);
    	myBundle.putInt("amount", amount);
    	myBundle.putString("unit", unit);
    	myBundle.putString("ingredientType", ingredientType);
    	myBundle.putInt("addTime", addTime);
    	myBundle.putString("description", description);
    	
    	return myBundle;
    }
    
    //Ensures that all information to create an ingredient has been entered before allowing the user to proceed
    private Boolean checkValidInput() {
    	Boolean retVal = false;

    	//required fields
    	name   = ingredientName.getText().toString();
    	
    	try {
    		amount = Integer.valueOf(unitAmount.getText().toString());
    	}
    	catch(Exception e) {
    		amount=0;
    	}
    	
    	try {
    		addTime = Integer.valueOf(addTimeEt.getText().toString());
    	}
    	catch(Exception e) {
    		addTime=0;
    	}
    	
    	unit = (String) unitSpinner.getSelectedItem();
    	ingredientType = (String) typeSpinner.getSelectedItem();
    	description = descriptionEt.getText().toString();
    	
    	
    	//Check that a value was entered for the name and amount/addTime fields
    	if(!name.contentEquals("") && amount>0 && addTime>0) {
    		retVal = true;
    	}
    	
    	return retVal;
    }
    
    public void onAddIngredientClick(View v) {
    	if(checkValidInput()) {
    		Bundle ingredientBundle = getIngredientInfo();
    		Log.v(TAG, "bundle created");
    		Log.v(TAG, ingredientBundle.toString());
    		Log.v(TAG, ingredientBundle.getString("name"));
    		Intent resultIntent = new Intent();
    		resultIntent.putExtra("ingredient", ingredientBundle);
        	this.setResult(Activity.RESULT_OK, resultIntent);
        	finish();	
    	}
    	else {
    		appContext.makeToast("Please enter a name/amount for the ingredient");
    	}
    	
    }
    	
}