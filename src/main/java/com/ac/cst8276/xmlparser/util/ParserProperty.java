package com.ac.cst8276.xmlparser.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ParserProperty {
	final Logger logger = Logger.getLogger(ParserProperty.class.getName());
	Properties prop;
	InputStream InputStream;

	public ParserProperty() {

		try {
			File f = new File("config.properties");
			System.out.println(f.getAbsolutePath());
			InputStream = new BufferedInputStream(new FileInputStream(new File("config.properties")));
			prop = new Properties();
			prop.load(InputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		String value = prop.getProperty(key);
		logger.info(value);
		return value;

	}
}
