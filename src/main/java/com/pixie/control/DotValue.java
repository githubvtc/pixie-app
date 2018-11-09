package com.pixie.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author voont
 * Check against the dot value if it is confirmed, whether there is deterministic value based on this information.
 * 
 */
public class DotValue extends RulesPrototype {

	private char[] data;
	private List<Integer> dotValueLst;
	private List<Integer> oriConcatDotValues;
	private List<Integer> revConcatDotValues;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Results proc(char[] data, T dataInput) {
//		This can check the validity of data
		try {
			this.data = data;
			dotValueLst.addAll((ArrayList<Integer>)dataInput);
			
		} catch (ClassCastException e) {
			clean();
			return Results.FAIL;
		}
		super.setLen(data.length);
		
		for (Integer dot : dotValueLst) {
			revConcatDotValues.add(new Integer(dot.intValue()));
		}
		
		Collections.reverse(revConcatDotValues);
		
		return runOnce();
	}
	
	private Results runOnce() {
		if (0 == 0/40 ) {
			
		}
		return null;
	}

	private void clean() {
		dotValueLst = null;
	}

}
