package com.ac.cst8276.xmlparser.model;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.ac.cst8276.xmlparser.entity.NodeObj;
import com.ac.cst8276.xmlparser.util.JdbcConnection;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class CourseDAO {

	public List<String> getAllRegNums() {
		 OraclePreparedStatement stmt = null;
		 Connection conn = null;
		 ResultSet rset = null;
			try {
				conn = JdbcConnection.getInstance().getConnection();
	            
				stmt = (OraclePreparedStatement)
					 conn.prepareStatement("SELECT XMLQUERY( "
					 		+ "'for $i in /root/course "
					 		+ "return concat($i/reg_num/text(),\",\" )'"
					 		+ " PASSING xml RETURNING content).getStringVal() as ids FROM xml_tab");
			       rset = stmt.executeQuery(); 
			       OracleResultSet orset = (OracleResultSet) rset; 

			while(orset.next())
			        { 
				   String str = rset.getString(1);
				   
				   List<String> ids = Arrays.asList(str.split("\\s*,\\s*"));
				   System.out.println(ids.toString());
				   for(String s : ids) {
					   System.out.print(s);
				   }
				   return ids;
			        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				JdbcConnection.freeResource(rset, stmt, conn);
			}
			
			//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
			
			return null;

	}
	public String getElementById(String id, String elementName) {
        OraclePreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rset = null;
		try {
			
            
			stmt = (OraclePreparedStatement)
				 conn.prepareStatement("SELECT XMLQUERY( "
				 		+ "'(catalog/*[@id=\"" + id + "\"]/" + elementName + "/text())[1]' "
				 		+ "PASSING xml RETURNING content).getStringVal() as element FROM xml_tab ");
		       rset = stmt.executeQuery(); 
		       OracleResultSet orset = (OracleResultSet) rset; 

		while(orset.next())
		        { 
			   String str = rset.getString(1);
			   return str;
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			JdbcConnection.freeResource(rset, stmt, conn);
		}
		
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return null;

	}
	
	public void updateElementById(String id, String element, String value) {
        OraclePreparedStatement stmt = null;
        Connection conn = null;
        
		try {
			
            
			stmt = (OraclePreparedStatement)
				 conn.prepareStatement("UPDATE xml_tab SET xml = "
				 		+ "XMLQuery('copy $tmp := . modify "
				 		+ "replace value of node $tmp/catalog/book[@id=\"" + id + "\"]/" + element + " with \"" + value + "\" "
				 		+ "return $tmp' "
				 		+ "PASSING xml RETURNING CONTENT)");
		    stmt.executeQuery(); 
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			JdbcConnection.freeResource(null, stmt, conn);
		}
		
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void deleteNodeById(String id) {
        OraclePreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rset = null;
		try {
            
			stmt = (OraclePreparedStatement)
				 conn.prepareStatement("UPDATE xml_tab SET xml = "
				 		+ "XMLQuery('copy $tmp := . modify delete node "
				 		+ "$tmp/catalog/book[@id=\"" + id + "\"] return $tmp' "
				 		+ "PASSING xml RETURNING CONTENT)");
		    stmt.executeQuery(); 
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			JdbcConnection.freeResource(rset, stmt, conn);
		}
		
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void deleteNode(NodeObj node) {
		deleteNodeById(node.getBookId());
	}
	public boolean clearDatabase() {
		 OraclePreparedStatement setupStmt;
	        
		String dropTable = "DROP TABLE xml_tab";
	
		String dropSequence = "DROP SEQUENCE xml_tab_seq";
	
		String dropSQLs[] = {dropTable,dropSequence};
		
		 OraclePreparedStatement stmt = null;
	        Connection conn = null;
	        ResultSet rset = null;		
		try {
            
			conn = JdbcConnection.getInstance().getConnection();
        	for(String s : dropSQLs) {
            	System.out.println(s.toString());
            	setupStmt = (OraclePreparedStatement)conn.prepareStatement(s);
            	try {
            	setupStmt.executeQuery();
            	}catch (SQLException e) {
            		continue;
            	}
            	//logger.info("EXECUTED => " + s);
            }
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			JdbcConnection.freeResource(rset, stmt, conn);
		}
		
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
		return true;
	// TODO Auto-generated method stub
		
	}
	public boolean setupDatabase(String dir,String filename) {
		 OraclePreparedStatement setupStmt = null;
	     Connection conn = null;
			String createTable = "CREATE TABLE xml_tab ( "
					+ "id NUMBER(10), "
					+ "filename VARCHAR2(100), "
					+ "xml XMLTYPE, "
					+ "CONSTRAINT xml_tab_pk PRIMARY KEY (id) "
					+ ")";
			String createSequence = "CREATE SEQUENCE xml_tab_seq";
			String createDirectory = "CREATE OR REPLACE DIRECTORY xml_dir AS '" + dir + "'";
			String grantPermissions = "Grant all on directory xml_dir to public";
			String insertFromXML = "INSERT INTO xml_tab VALUES ( "
					+ "xml_tab_seq.NEXTVAL, "
					+ "'"+filename+"', "
					+ "XMLType(bfilename('XML_DIR', '"+filename+"') , "
					+ "nls_charset_id('UTF-8') )"
					+ ")";
			
			String setupProcedure[] = {
					createTable, 
					createSequence,
					createDirectory,
					grantPermissions,
					insertFromXML};
					
			
			
	        try {
	        	conn = JdbcConnection.getInstance().getConnection();

	            for(String s : setupProcedure) {
	            	System.out.println(s.toString());
	            	setupStmt = (OraclePreparedStatement)conn.prepareStatement(s);
	            	setupStmt.executeQuery();
	            	//logger.info("EXECUTED => " + s);
	            }
	            	

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally{
				JdbcConnection.freeResource(null, setupStmt, conn);
		}
		
			//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
		return true;
	}
	
	public boolean insertNode(NodeObj n) {
        OraclePreparedStatement updateStmt =null;
        ResultSet updateResults = null;
        Connection conn = null;
		try {
			conn = JdbcConnection.getInstance().getConnection();

			updateStmt = (OraclePreparedStatement)
			 		 conn.prepareStatement("UPDATE xml_tab "
			 		 		+ "SET xml = XMLQuery( "
			 		 		+ "'copy $tmp := . modify insert node "
			 		 		+ "<book id=\"" + n.getBookId() + "\"> "
			 		 		+ "<author>" + n.getAuthor() + "</author> "
			 		 		+ "<title>" + n.getTitle() + "</title> "
			 		 		+ "<genre> " + n.getGenre() + "</genre> "
			 		 		+ "<price> " + n.getPrice() + " </price> "
			 		 		+ "<description> " + n.getDescription() + "</description> "
			 		 		+ "</book> "
			 		 		+ "as last into $tmp/catalog "
			 		 		+ "return $tmp' "
			 		 		+ "PASSING xml RETURNING CONTENT)");
		       updateResults = updateStmt.executeQuery(); 
		       OracleResultSet updateOracleResults = (OracleResultSet) updateResults; 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}  finally{
			JdbcConnection.freeResource(updateResults, updateStmt, conn);
			
	}
	
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());

		return true;
	}
//	
//	private void logConnectionDetails() {
//    	logger.info("\t************************** Connection Details ***************************"
//    			+ "\n\t* JDBC_DRIVER \t = \t \"" + JdbcDriver +"\"\t\t*"
//    			+ "\n\t* JDBC_STRING \t = \t \"" + JdbcConnString +"\"\t*"
//    			+ "\n\t* USER_NAME \t = \t \"" + UserName +"\"\t\t\t\t*"
//    			+ "\n\t* PASSWD \t = \t \"" + Password +"\"\t\t\t\t\t*"
//    			+ "\n\t*************************************************************************");
//	}
//	
//	private void logConnectionOpened(String methodName) {
//	   	logger.info("\t************************** Connection Opened ****************************"
//    			+ "\n\t*\t\t\t    @ = \"" + methodName +"\"\t\t\t\t*"
//    			+ "\n\t*************************************************************************");
//	}
//	
//	private void logConnectionClosed(String methodName) {
//	   	logger.info("\t************************** Connection Closed ****************************"
//    			+ "\n\t*\t\t\t    @ = \"" + methodName +"\"\t\t\t\t*"
//    			+ "\n\t*************************************************************************");
//	}
	public String printXML() {

        OraclePreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rset = null;
		try {
			
			conn = JdbcConnection.getInstance().getConnection();
			stmt = (OraclePreparedStatement)
				 conn.prepareStatement("SELECT A.xml.getClobVal() xml FROM xml_tab A");
		        rset = stmt.executeQuery(); 
		       OracleResultSet orset = (OracleResultSet) rset; 
		Clob xmlClob = null;
		while(orset.next())
		        { 
		       
			   xmlClob = orset.getCLOB(1);
//			   System.out.print(xmlClob.getSubString(1,64000));
			   return xmlClob.getSubString(1,64000);
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JdbcConnection.freeResource(rset, stmt, conn);
		}
		
		//logConnectionClosed(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return null;
		
	}

	public void updateNodeElements(NodeObj node) {
//		String[] elements = {"author", "title", "genre", "price", "publish_date", "description"};
		HashMap<String, String> elements = new HashMap<String, String>();
		elements.put("author", node.getAuthor());
		elements.put("title", node.getTitle());
		elements.put("genre", node.getGenre());
		elements.put("price", node.getPrice());
		elements.put("publish_date", node.getPublishDate());
		elements.put("description", node.getDescription());

		for(String e : elements.keySet())
			updateElementById(node.getBookId(), e, elements.get(e));
	}
	
	
	

	
}
