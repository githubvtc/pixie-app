package com.pixie.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Dot values that are added together, are large enough to check against the max
 * length of each row/col, whether it gives any deterministic result.
 * </p>
 * <p>
 * This rule is only runned once at the beginning for each row&col. it depends on the dot values.
 * </p>
 * @author pixie
 */
public class MaxMinDots extends RulesPrototype {

	private char[] data = null;
	private List<Integer> dotValuesLst = null;

	/**
	 * This is the min dot value formed with min count of possible blanks
	 */
	private Integer minDataLen;

	/**
	 * This is the max dot value formed with max count of possible blanks
	 */
	private Integer maxDataLen;

	@SuppressWarnings("unchecked")
	@Override
	public <T> Results proc(char[] data, T dataInput) {
		this.data = data;
		super.setLen(data.length);

		dotValuesLst = new ArrayList<>();

		try {
			dotValuesLst.addAll((ArrayList<Integer>) dataInput);

			int estDataLen = 0;
			for (int dot : dotValuesLst) {
				estDataLen += dot;
			}
			minDataLen = new Integer(estDataLen + dotValuesLst.size() - 1);
			maxDataLen = new Integer(estDataLen + dotValuesLst.size() + 1);

			return checkMax();

		} catch (ClassCastException ex) {
			ex.printStackTrace();
			return Results.FAIL;
		} finally {
			clean();
		}
	}

	/**
	 * This is to clean the object before it is used by another process
	 */
	private void clean() {
		data = null;
		dotValuesLst = null;
		minDataLen = null;
		maxDataLen = null;
	}

	/**
	 * <p>Find the min possible combined dot values with min possible number of blanks and check 
	 * against max length. If checking returns true , whole row/col is determined</p>
	 * <p>Find the max possible combined dot values with max possible number of blanks and check 
	 * against max length. If checking returns true , whole row/col is determined</p>
	 * <p>Continue checking with another method after this</p> 
	 * @return Results
	 */
	private Results checkMax() {
		/* I1+I2+ ... + In + n-1 = len , then confirm all cells */
		if (len == minDataLen) {
			int index = 0;
			for (Integer dot : dotValuesLst) {
				for (int i = 0; i < dot; i++) {
					data[index++] = 'X';
				}
				if (index < len) {
					data[index++] = 'O';
				}
			}
			return Results.SUCCESS;
		}
		/* I1+I2+ ... + In + n+1 = len , then confirm all cells too */
		else if (len == maxDataLen) {
			int index = 0;
			data[index++] = 'O';
			for (Integer dot : dotValuesLst) {
				for (int i = 0; i < dot; i++) {
					data[index++] = 'X';
				}
				data[index++] = 'O';
			}
			return Results.SUCCESS;
		}

		else
			/* Continue with second checking */
			return checkMax2();

	}

	/**
	 * <p>Get the overlapping dots that are determined by looping through the left most 
	 * possible min dot values and check against right most possible min dot values. If 
	 * the same dot overlaps each during the checking , means the dots are determined.</p>
	 * @return Results
	 */
	private Results checkMax2() {
		int mid = len / 2;

		/* If min data length does not even reach the mid,definitely no overlap */
		if (maxDataLen.intValue() < mid) {
			return Results.HOLD;
		} else {
			int startingMinDotValue = 0;
			Results res = null;

			/* To obtain the reverse of dot values list to obtain the current min dot values
			 * Need to make sure deep copy of this list is obtained before reversing*/
			List<Integer> reverseLst = new ArrayList<Integer>();
			for (Integer itm : dotValuesLst) {
				reverseLst.add(new Integer(itm.intValue()));
			}

			Collections.reverse(reverseLst);
			
			/* Check against each min dot values from the beginning until mid dot values reaches mid
			 * Once obtained left possible min dot values, check against the reverse of it.*/
			for (int i = 0; i < dotValuesLst.size(); i++) {
				if (i == 0) {
					startingMinDotValue = dotValuesLst.get(i);
					res = checkWithReverseLst(startingMinDotValue, i, reverseLst);
				} else {
					startingMinDotValue = startingMinDotValue + 1 + dotValuesLst.get(i);
					res = checkWithReverseLst(startingMinDotValue, i, reverseLst);
				}
			}

			return res;
		}
	}

	/**
	 * <p>Check against its reverse dot values <em>(right most possible min dot values)</em></p>
	 * 
	 * @param startingMinDotValue the left most min dot values
	 * @param index the index of current dot, to calculate its position in reverse list.
	 * @param reverseLst the reverse list obtained from main function flow
	 * @return Results
	 */
	private Results checkWithReverseLst(int startingMinDotValue, int index, List<Integer> reverseLst) {
		int reversedMinDotValue = 0;

		/*
		 * The index of the current dot in reversed list is calculated by length - (index + 1) +1 
		 * I.E. the second dot(index 1 in an array) in a list with length 4 will be at index 3 at reversed list
		 */
		for (int j = 0; j < dotValuesLst.size() - (index + 1) + 1; j++) {
			if (j == 0)
				reversedMinDotValue = reverseLst.get(j);
			else
				reversedMinDotValue = reversedMinDotValue + 1 + reverseLst.get(j);

		}

		/*
		 * if left minDotValue + right mindDotValue for current dot is more than length,
		 * means that there is overlap, there are dots determined. The difference determines
		 * the number of dots overlapped, 
		 * direction is important too! there is only backward! 
		 */
		if (startingMinDotValue + reversedMinDotValue > len) {
			int diff = startingMinDotValue + reversedMinDotValue - len;
			for (int k = 0; k < diff; k++) {
				data[startingMinDotValue - k -1] = 'X';
			}
			return Results.SUCCESS;
		}
		return Results.HOLD;
	}
}
