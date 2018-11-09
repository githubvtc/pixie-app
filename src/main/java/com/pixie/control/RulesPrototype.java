package com.pixie.control;

import com.pixie.util.PixieUtils;

public abstract class RulesPrototype implements ProcRule {
//	static Logger LOG = Logger.getLogger(RulesPrototype.class);

	protected int len;
	protected PixieUtils util;

	public RulesPrototype() {
		this.util = PixieUtils.getInstance();
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getRuleName() {
		return this.getClass().getName();
	}

//	protected abstract Results checkYLine();

	@Override
	public String toString() {
//		Removing unnecessary string
		return this.getRuleName().replaceAll("com.pixie.control.", "");
	}
}
