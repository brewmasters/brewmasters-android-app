package com.gt.brewmasters.structures;

import java.util.ArrayList;


public class Recipe {
//consists of ingredients
	public String name;
	public String beerType;
	public String description;
	public Long id;
	
	public  float waterGrainRatio;
	private int mashTemp;
	
	private int boilDuration;
	private int mashDuration;
	
//	private Recipe(String name, ArrayList<Ingredient> ingredients) {
//		for(int i=0; i<ingredients.size(); i++) {
//			addIngredient(ingredients.get(i));
//		}
//	}
	
//	public Recipe(String name, String type, String description, float waterGrainRatio, int mashTemp, int boilDuration, int mashDuration) {
//		this.name=name;
//		this.type=type;
//		this.description=description;
//		this.waterGrainRatio=waterGrainRatio;
//		this.setMashTemp(mashTemp);
//		this.setBoilDuration(boilDuration);
//		this.setMashDuration(mashDuration);
//	}
	
	public void setId(Long id) {
		this.id=id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setBeerType(String type) {
		this.beerType=type;
	}
	
	public String getBeerType() {
		return this.beerType;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setWaterGrainRatio(float ratio) {
		this.waterGrainRatio=ratio;
	}
	
	public float getWaterGrainRatio(){
		return this.waterGrainRatio;
	}

	public int getMashDuration() {
		return mashDuration;
	}

	public void setMashDuration(int mashDuration) {
		this.mashDuration = mashDuration;
	}

	public int getBoilDuration() {
		return boilDuration;
	}

	public void setBoilDuration(int boilDuration) {
		this.boilDuration = boilDuration;
	}

	public int getMashTemp() {
		return mashTemp;
	}

	public void setMashTemp(int mashTemp) {
		this.mashTemp = mashTemp;
	}
	
}
