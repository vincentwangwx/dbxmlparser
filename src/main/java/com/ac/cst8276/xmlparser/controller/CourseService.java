
package com.ac.cst8276.xmlparser.controller;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ac.cst8276.xmlparser.entity.NodeObj;
import com.ac.cst8276.xmlparser.model.CourseDAO;
import com.ac.cst8276.xmlparser.util.ParserProperty;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;




public class CourseService {
	
	private String JdbcDriver ;
	private String JdbcConn;
	private String JdbcHost ;
	private String JdbcConnString;
	private String UserName;
	private String Password;
	
	private ParserProperty pp ;
	private Connection conn = null;
	private ResultSet rs = null;
	private Clob xmlClob = null;
	private String lastAction;
	
	private CourseDAO dao = null;
	private Logger logger = Logger.getLogger(CourseService.class.getName());

	public CourseService() {
		
    	logger.setLevel(Level.INFO);
    	logger.info("Initializing XML CourseService...");
       
        pp 				= new ParserProperty();
        JdbcDriver 		= pp.getValue("jdbcdriver");
        JdbcConn 		= pp.getValue("jdbcconn");
        JdbcHost 		= pp.getValue("jdbchost");
        JdbcConnString 	= JdbcConn + JdbcHost;
        UserName 		= pp.getValue("username");
        Password 		= pp.getValue("passwd");
        
        dao = new CourseDAO();
        //logConnectionDetails();
	}
	
//	public boolean connect() {
//		
//        try {
//    		Class.forName(JdbcDriver);
//			conn = DriverManager.getConnection(JdbcConnString, UserName, Password);
//	        logConnectionOpened(Thread.currentThread().getStackTrace()[1].getMethodName());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		}
//        
//        logConnectionDetails();
//		return true;
//	}
	
	public boolean insertNode(NodeObj n) {
		return dao.insertNode(n);
	}
	
	public String printXML() {
		
		return dao.printXML();
		
//		return true;
	}
	
	public boolean setupDatabase(String dir,String filename) {
		
		boolean b1 = dao.clearDatabase();
		boolean b2 = dao.setupDatabase(dir,filename);
		
       

		return b1 && b2;
	}
	
	public List<String> getAllCourseIds() {
		
		return dao.getAllRegNums();
       

	}
	
	
	
	protected String getJDBC_DRIVER() {
		return JdbcDriver;
	}

	protected void setJDBC_DRIVER(String jDBC_DRIVER) {
		JdbcDriver = jDBC_DRIVER;
	}

	public String getJDBC_STRING() {
		return JdbcConnString;
	}

	protected void setJDBC_STRING(String jDBC_STRING) {
		JdbcConnString = jDBC_STRING;
	}

	public String getUSER_NAME() {
		return UserName;
	}

	public void setUSER_NAME(String uSER_NAME) {
		UserName = uSER_NAME;
	}

	public String getPASSWD() {
		return Password;
	}

	public void setPASSWD(String pASSWD) {
		Password = pASSWD;
	}

	protected String getJDBC_CONN() {
		return JdbcConn;
	}

	protected void setJDBC_CONN(String jDBC_CONN) {
		JdbcConn = jDBC_CONN;
		JdbcConnString = JdbcConn + JdbcHost;
	}

	protected String getJDBC_HOST() {
		return JdbcHost;
	}

	public void setJDBC_HOST(String jDBC_HOST) {
		JdbcHost = jDBC_HOST;
		JdbcConnString = JdbcConn + JdbcHost;
	}

	public void updateNodeElements(NodeObj node) {
		dao.updateNodeElements(node);
		
	}

	public void deleteNode(NodeObj node) {
		dao.deleteNode(node);
		
	}
	
	
	
}

