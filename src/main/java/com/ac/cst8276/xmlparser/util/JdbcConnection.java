package com.ac.cst8276.xmlparser.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class to manage Jdbc Connections.
 * @author wangs
 *
 */
public final class JdbcConnection {
	
	private static JdbcConnection instance = null;
	
	static {
		try {
			Class.forName(new ParserProperty().getValue("jdbcdriver"));			
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static JdbcConnection getInstance() {
		if(instance == null) {
			synchronized(JdbcConnection.class) {
				if(instance == null) {
					instance =new JdbcConnection();
				}
			}
		}
		return instance;
	}
	
	/**
	 * get the DBConnection using DriverManager.
	 * @return Database connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{
		ParserProperty pp = new ParserProperty();
	        String jdbcConn 		= pp.getValue("jdbcconn");
	        String jdbcHost 		= pp.getValue("jdbchost");
	        String jdbcConnString 	= jdbcConn + jdbcHost;
	        String userName 		= pp.getValue("username");
	        String password 		= pp.getValue("passwd");
		return DriverManager.getConnection(jdbcConnString, userName, password);
	}
	
	/**
	 * free all the database connection resource if it is not null.
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void freeResource(ResultSet rs,Statement stmt,Connection conn) {
		try{
			if(rs != null){rs.close();}
		}catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(stmt != null) {stmt.close();}
			}catch(SQLException ex){
				ex.printStackTrace();
			}finally{
				try{
					if(conn != null) {conn.close();}
				}catch(SQLException em){
					em.printStackTrace();
				}
			}
		}
	}
}
