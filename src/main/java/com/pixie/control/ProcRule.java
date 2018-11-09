package com.pixie.control;

public interface ProcRule {
	public <T> Results proc(char[] data, T dataInput);
}
