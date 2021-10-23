package com.ac.cst8276.xmlparser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ac.cst8276.xmlparser.util.ParserProperty;
import com.ac.cst8276.xmlparser.view.GUI;


public class Demo {

	public static void main(String[] args) {
		final Logger logger = Logger.getLogger(Demo.class.getName());
		
    	logger.setLevel(Level.INFO);
    	logger.info("Initializing Demo");
    	
    	ParserProperty pp = new ParserProperty();
    	logger.info(pp.getValue("username"));
    	
		new GUI();
	}
}

