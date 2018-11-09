package com.pixie.main;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class TestMainApp {

	final static Logger LOG = Logger.getLogger(TestMainApp.class);
	
	private MainApp app = null;
	
	@Before
	public void init() throws FileNotFoundException, IOException {
		app = new MainApp();
		InputStream inStream = getClass().getResourceAsStream("/pixie-log4j.properties");
		
		// Because properties file is custom name, it needs to be configured.
		PropertyConfigurator.configure(inStream);
		
	}
	@Test
	public void testRun() {
		if (app != null ) {
			app.runApp();
			LOG.info("Running log4j... ");
			assertTrue(true);
		} else {
			assertTrue(false);
			LOG.fatal("Running log4j... ");
		}
		
	}

}
