package com.gt.brewmasters.structures;

public class Ingredient {
	
	private Long id;
	private Long recipeId;
	private String name;
	private String type;
	private String description;
	private int amount;
	private String unit;
	private int addTime;
	
	public Ingredient() {
		
	}
	
	public Ingredient(String name, String type, int amount, String unit, String description, int addTime) {
		this.name = name;
		this.type=type;
		this.amount=amount;
		this.unit=unit;
		this.description=description;
		this.addTime=addTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Override
	public String toString() {
		return this.name+" "+ this.type+" "+this.amount+ " " + this.unit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAddTime() {
		return addTime;
	}

	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}
	
}
