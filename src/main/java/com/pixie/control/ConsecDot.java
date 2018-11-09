package com.pixie.control;

/**
 * @author pixie
 * Check against the arrays that show the order of dot values , see whether it can determine dots around to the dot
 * 
 */
public class ConsecDot extends RulesPrototype{

	private int dotValue;
	
	@Override
	public <T> Results proc(char[] data, T dataInput) {
		
		if(!(dataInput instanceof Integer)) {
			return Results.FAIL;
		}
		
		Integer initData = (Integer)dataInput;
		
		return Results.HOLD;
	}
	

}
