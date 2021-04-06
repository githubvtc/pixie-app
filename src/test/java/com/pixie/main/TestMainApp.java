package com.pixie.main;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TestMainApp {

	final static Logger LOG = LogManager.getLogger(TestMainApp.class);
	
	private MainApp app = null;
	
	@Before
	public void init() throws FileNotFoundException, IOException {
		app = new MainApp();
//		InputStream inStream = getClass().getResourceAsStream("/pixie-log4j.properties");
		
		// Because properties file is custom name, it needs to be configured.
		
	}
	@Test
	public void testRun() {
		System.out.println("Check");
		if (app != null ) {
			app.runApp();
			LOG.info("Running log4j... ");
			LOG.info("just checking");
			assertTrue(true);
		} else {
			assertTrue(false);
			LOG.fatal("Running log4j... ");
		}
		
	}

}
