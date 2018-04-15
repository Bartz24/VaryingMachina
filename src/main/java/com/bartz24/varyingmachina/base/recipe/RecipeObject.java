package com.bartz24.varyingmachina.base.recipe;

public interface RecipeObject {
	public Object getRepresentativeObject();

	/**
	 * Checks if the checked item is equal to and at least has the same amount
	 */
	public boolean matches(RecipeObject check);

	/**
	 * Checks if the checked item is equal to and has the exact same amount
	 */
	public boolean matchesExact(RecipeObject check);

	public float getRatio(RecipeObject check);

	public boolean isValid();
	
	public int getCount();
	
	public RecipeObject setCount(int count);

	public RecipeObject copy();

}
