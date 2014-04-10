package com.gt.brewmasters.utils;

import java.util.ArrayList;

import com.gt.brewmasters.R;
import com.gt.brewmasters.structures.Ingredient;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> implements android.content.DialogInterface.OnClickListener{

	private ArrayList<Ingredient>ingredientList;
	private Activity activity;
	private int layoutResourceId;
	private Button delButton;
	
    // Debugging
    private static final String TAG = "Brewmasters";
    private static final boolean D = true;
	
	public IngredientListAdapter(Activity activity, int layoutResourceId, ArrayList<Ingredient> ingredientList) {
        super(activity, layoutResourceId, ingredientList);
        this.layoutResourceId=layoutResourceId;
        this.activity = activity;
        this.ingredientList=ingredientList;
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
        Activity activity = (Activity) getContext();
        LayoutInflater inflater = activity.getLayoutInflater();
        
        // Inflate the views from XML
        row = inflater.inflate(layoutResourceId, parent, false);
        Ingredient ingredient = getItem(position);

        // Set the text on the TextView
        TextView name   = (TextView) row.findViewById(R.id.ingredient_name);
        TextView amount = (TextView) row.findViewById(R.id.ingredient_amount);
        TextView unit   = (TextView) row.findViewById(R.id.ingredient_unit);
        
        name.setText(ingredient.getName());
        amount.setText(String.valueOf(ingredient.getAmount()));
        unit.setText(ingredient.getUnit());
        
        delButton = (Button)row.findViewById(R.id.remove_ingredient_button);
        delButton.setTag(position);
        row.setTag(position);
        
        return row;
    }
	
    public ArrayList<Ingredient> getIngredientList(){
    	return this.ingredientList;
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
	}
	
}
