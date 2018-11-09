package com.pixie.main;

import java.util.concurrent.Callable;

public class PixieThread <T> extends Thread implements Callable<T> {
	private MainApp app;
	private T mapValue;

	public PixieThread(MainApp app, T mapValue) {
		this.app = app;
		this.mapValue = mapValue;
	}

	public MainApp getApp() {
		return app;
	}

	public void setApp(MainApp app) {
		this.app = app;
	}

	public synchronized T getMapValue() {
		return mapValue;
	}

	public void setMapValue(T mapValue) {
		this.mapValue = mapValue;
	}

	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
