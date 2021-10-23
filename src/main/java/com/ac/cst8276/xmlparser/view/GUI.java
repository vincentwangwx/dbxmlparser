package com.ac.cst8276.xmlparser.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import com.ac.cst8276.xmlparser.controller.CourseService;
import com.ac.cst8276.xmlparser.entity.NodeObj;
import com.ac.cst8276.xmlparser.util.JdbcConnection;
import com.ac.cst8276.xmlparser.util.ParserProperty;
import com.ac.cst8276.xmlparser.util.XmlParserConstant;
public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JFrame f = new JFrame();
	JFrame editorFrame = new JFrame();
	JFrame connectionFrame = new JFrame();

	JTextArea textArea = new JTextArea();
	
	CourseService xserv = null;
	
	JScrollPane scroll = new JScrollPane (textArea, 
			   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
    JTextArea t;
    final Logger logger ;
    ParserProperty pp;
    
	public GUI() {
		xserv = new CourseService();
		logger = Logger.getLogger(GUI.class.getName());
		 pp = new ParserProperty();
        try {
            // Set metl look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            // Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }
        catch (Exception e) {
        	logger.info(getName());
        }
        
        initializeConnectionPanel();
        

	}
	
	public void initializeWelcomeFrame() {
		
        f = new JFrame(pp.getValue("frametitle"));
        
        t = new JTextArea();
        t.setEditable(false);
        
    	JScrollPane s = new JScrollPane (t, 
 			   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    	
    	t.setText("Welcome!\n\nThis application demostrate CRUD operations to an Oracle XMLTYPE object.\n"
    			+ "The operations performe with the help of XQUERY statements. \n\n"
    			+ "1. Please initialize the demo application from the File menu.\n"
    			+ "2. Refresh the view from the File Menu (fetch xml data from db).\n"
    			+ "3. Execute all CRUD operations under the Edit menu.\n"
    			+ "4. Save the Oracle database's XMLTYPE data through the file menu.");

    	JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
  
        // menu items
        JMenuItem mi1 = new JMenuItem(XmlParserConstant.CMD.Initialize.toString());
        JMenuItem mi2 = new JMenuItem(XmlParserConstant.CMD.Refresh.toString());
        JMenuItem mi3 = new JMenuItem(XmlParserConstant.CMD.Save.toString());
  
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
  
        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
  
        JMenu m2 = new JMenu("Edit");
  
        JMenuItem mi4 = new JMenuItem(XmlParserConstant.CMD.InsertNode.toString());
        JMenuItem mi5 = new JMenuItem(XmlParserConstant.CMD.UpdateNode.toString());
        JMenuItem mi6 = new JMenuItem(XmlParserConstant.CMD.DeleteNode.toString());
  
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);
  
        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);
          
        mb.add(m1);
        mb.add(m2);
  
        f.setJMenuBar(mb);
        f.add(s);
        f.setSize(500, 500);
        f.setLocation(500,300);
        f.setVisible(true);
	}



	public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        
        XmlParserConstant.CMD cmd = XmlParserConstant.CMD.valueOf(s);
        switch(cmd) {
	        case Initialize:
	        	doInitializeDatabase();
	        	break;
	        case InsertNode:
	        	initializeEditorPanel(0);
	        	break;
	        case UpdateNode:
	        	initializeEditorPanel(1);
	        	break;
	        case DeleteNode:
	        	initializeEditorPanel(2);
	        	break;
	        case Save:
	        	doSave();
	        	break;
	        case Refresh:
	        	doRefresh();
	        	break;
	        case Close:
	        	f.setVisible(false);
	        	System.exit(0);
	        	break;
	        default:
	        	logger.info("No action executed...");
	        	
        }

	}

	private void doRefresh() {
		t.setText("");
		String result = xserv.printXML();
		
		logger.info(result);
		if (result.isEmpty() || result.isBlank() || result == null)
		    JOptionPane.showMessageDialog(f, "Nothing was found. Please ensure DB has been initialized.");
		else {
		    JOptionPane.showMessageDialog(f, "XMLTYPE selected from database.");
			t.setText(result);
		}
	}

	private void doSave() {
		// File explorer
		JFileChooser j = new JFileChooser("f:");
		String dir = System.getProperty("user.dir");
		File target = new File(dir + "\\nodes.xml");

		j.setCurrentDirectory(target);
		j.setSelectedFile(target);
		j.addChoosableFileFilter(new FileNameExtensionFilter("*.xml", "xml"));

		int r = j.showSaveDialog(null);
  
		if (r == JFileChooser.APPROVE_OPTION) {
  
		    File fi = new File(j.getSelectedFile().getAbsolutePath());
  
		    try {
		        FileWriter wr = new FileWriter(fi, false);
		        BufferedWriter w = new BufferedWriter(wr);
  
		        // Write to file
		        w.write(t.getText());
  
		        w.flush();
		        w.close();
		    }
		    catch (Exception evt) {
		        JOptionPane.showMessageDialog(f, evt.getMessage());
		    }
		}
		else
		    JOptionPane.showMessageDialog(f, "Operation cancelled");
	}

	private void doInitializeDatabase() {
		logger.info("do initialize");
		int selectedOption = JOptionPane.showConfirmDialog(null, 
		        "You are about to repopulate the demo table.", 
		        "Message", 
		        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); 
		if (selectedOption == JOptionPane.YES_OPTION) {
			String dir = System.getProperty("user.dir");
			String filename = pp.getValue("filename");
			Boolean b = new File(dir + "\\"+filename).isFile();
			
			if (b == true) {
		        xserv.setupDatabase(dir,filename);
		        JOptionPane.showMessageDialog(f, "Table created and data inserted as XMLTYPE");
		        String result = xserv.printXML();
		        t.setText(result);
		    } else {
		    	logger.info("File not exists.");
		    }
		} else {
		    JOptionPane.showMessageDialog(f, "Operation cancelled", "Message", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * initialize the connection panel.
	 * set the host,port,user,psw and connect the database.
	 */
	public void initializeConnectionPanel() {
	    JLabel labelOracle = new JLabel("host:port:SID: ");
	    JLabel labelUsername = new JLabel("Username: ");
	    JLabel labelPassword = new JLabel("Password: ");
	    
	    final JTextField textOracle = new JTextField(18);
	    final JTextField textUsername = new JTextField(18);
	    final JTextField textPassword = new JPasswordField(18);
	    
	    String connDetails = xserv.getJDBC_STRING();
	    textOracle.setText(connDetails.substring(connDetails.lastIndexOf("@") + 1));
	    textUsername.setText(xserv.getUSER_NAME());
	    textPassword.setText(xserv.getPASSWD());
	    
		JButton confirmButton = new JButton("Connect");
		
		connectionFrame = new JFrame("Connection Settings");
		
		// create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());
         
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
         
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelOracle, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textOracle, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1; 
        newPanel.add(labelUsername, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textUsername, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        newPanel.add(labelPassword, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textPassword, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 13;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(confirmButton, constraints);
         
        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Connection Details"));
         
        // add the panel to this frame
        connectionFrame.add(newPanel);
         
        connectionFrame.pack();
        connectionFrame.setLocationRelativeTo(null);
        connectionFrame.show();
                
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	xserv.setJDBC_HOST(textOracle.getText());
            	xserv.setUSER_NAME(textUsername.getText());
            	xserv.setPASSWD(textPassword.getText());
            	//boolean connSuccess = xserv.connect();
            	Connection conn = null;
				try {
					conn = JdbcConnection.getInstance().getConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	if (conn == null) {
            		JOptionPane.showMessageDialog(connectionFrame, "Something went wrong with your connection. Please try again", "Message", JOptionPane.ERROR_MESSAGE);
            	} else  {
            		connectionFrame.dispose();
            		
    				String dir = System.getProperty("user.dir");
    				boolean b;
    				
    				b = new File(dir + "\\books.xml").isFile();
    				if (b == true) {
    					initializeWelcomeFrame();
    				} else {
    					initializeWelcomeFrame();
    				}
            	}
            }
        });
        
	}
	

	/**
	 * initialize the node's editor panel.
	 * @param mode 0 for insert 1 for edit
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initializeEditorPanel(final int mode) {
	    JLabel labelEdit = new JLabel("Select a Node to edit: ");
	    JLabel labelBookId = new JLabel("book id: ");
	    JLabel labelAuthor = new JLabel("author: ");
	    JLabel labelTitle = new JLabel("title: ");
	    JLabel labelGenre = new JLabel("genre: ");
	    JLabel labelPrice = new JLabel("price: ");
	    JLabel labelPublishDate = new JLabel("publish_date: ");
	    JLabel labelDescription = new JLabel("description: ");
	    
	    final JTextField textBookId = new JTextField(18);
	    final JTextField textAuthor = new JTextField(18);
	    final JTextField textTitle = new JTextField(18);
	    final JTextField textGenre= new JTextField(18);
	    final JTextField textPrice= new JTextField(18);
	    final JTextField textPublishDate = new JTextField(18);
	    final JTextArea textDescription = new JTextArea(6, 1);
	    
	    JScrollPane scrollPane = new JScrollPane( textDescription );
	    
	    final JTextField[] textboxes = {textBookId, textAuthor, textTitle, textGenre, textPrice, textPublishDate};

		JButton confirmButton = null;
		
		if (mode == 0) { // If mode is insert
			editorFrame = new JFrame("Node Details (INSERT)");
	    	confirmButton = new JButton("Insert Node");
		} else if (mode == 1 || mode == 2) { // If mode is update, or mode is delete
			if (mode == 1) {
				editorFrame = new JFrame("Node Details (EDIT)");
	    		confirmButton = new JButton("Edit Node");
			} else if (mode == 2) {
				editorFrame = new JFrame("Node Details (DELETE)");
	    		confirmButton = new JButton("Delete Node");
			}
	    	
	    	for(JTextField j : textboxes)
	    		j.setEnabled(false);
	    	
	    	textDescription.setEnabled(false);
		}
		
		final JComboBox idList = new JComboBox(xserv.getAllCourseIds().toArray());
		idList.setPrototypeDisplayValue("placeholder for default list width");
		
		// create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());
         
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
         
        // add to panel
        if(mode == 1 || mode == 2) { // If Edit or Delete mode, show drop-down of nodes
	        constraints.gridx = 0;
	        constraints.gridy = 0;
	        constraints.fill = GridBagConstraints.HORIZONTAL;
	        newPanel.add(labelEdit, constraints);
	
	        constraints.gridx = 1;
	        newPanel.add(idList, constraints);
        }
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labelBookId, constraints);
 
        constraints.gridx = 1;
        newPanel.add(textBookId, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 2;
        newPanel.add(labelAuthor, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textAuthor, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3; 
        newPanel.add(labelTitle, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textTitle, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 4;
        newPanel.add(labelGenre, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textGenre, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 5;
        newPanel.add(labelPrice, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textPrice, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 6;
        newPanel.add(labelPublishDate, constraints);
         
        constraints.gridx = 1;
        newPanel.add(textPublishDate, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 7;
        newPanel.add(labelDescription, constraints);         
        
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;
        constraints.gridx = 1;
        constraints.weightx = 1;
        newPanel.add(scrollPane, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 13;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(confirmButton, constraints);
         
        // set border
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Node Details"));
         
        // add panel to frame
        editorFrame.add(newPanel);
         
        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.show();
        
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NodeObj node = new NodeObj();
        	    node.setBookId(textBookId.getText());
        	    node.setAuthor(textAuthor.getText());
        	    node.setTitle(textTitle.getText());
        	    node.setGenre(textGenre.getText());
        	    node.setPrice(textPrice.getText());
        	    node.setPublishDate(textPublishDate.getText());
        	    node.setDescription(textDescription.getText());
        	    
        	    String confirmMessage = null;
				boolean isError = false;

            	if(mode == 0 ) { // Insert Mode
            		if(xserv.getAllCourseIds().contains(node.getBookId())) {
    		            JOptionPane.showMessageDialog(editorFrame, node.getBookId() + " already exists! Please try another id.", "Message", JOptionPane.ERROR_MESSAGE);
    		            isError = true;
            		} else {
		        	    xserv.insertNode(node);
		        	    editorFrame.setVisible(false);
		        	    confirmMessage = "Node inserted. Do you wish to refresh the view?";
            		}
				} else if(mode == 1) { // Edit Mode
	    	    	for(JTextField j : textboxes) {
	    	    		if(j.getText() == null || j.getText().isEmpty() || j.getText().isBlank()) {
	    		            JOptionPane.showMessageDialog(editorFrame, "Please select Node to edit, and ensure form is filled", "Message", JOptionPane.ERROR_MESSAGE);
	    		            isError = true;
	    		            break;
	    	    		}
	    	    	}
	    	    	
	    	    	if(isError == false) {
	    	    		xserv.updateNodeElements(node);
		        	    confirmMessage = "Node updated. Do you wish to refresh the view?";
	    	    	}
            	} else if(mode == 2) { //delete mode
            		String warningMessage = "Please select a Node to delete...";
            		
            		if (textBookId.getText() == null || textBookId.getText().isEmpty() || textBookId.getText().isBlank()) //If nothing was selected from dropdown list
            			isError = true;
            		
            		if (isError == false) {
	            	    int selectedOption = JOptionPane.showConfirmDialog(null, 
	                            "You are about delete this node from the table. Proceed?", 
	                            "Message", 
	                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); 
	        			if (selectedOption == JOptionPane.YES_OPTION) {
	            			xserv.deleteNode(node);
	    	        	    confirmMessage = "Node deleted. Do you wish to refresh the view?";
	        			} else {
	        				warningMessage = "Operation cancelled";
	    		            isError = true;
	        			}
            		}
            		
        			if (isError == true)
        	            JOptionPane.showMessageDialog(editorFrame, warningMessage, "Message", JOptionPane.ERROR_MESSAGE);
            	}
            	
            	if(isError == false) {
	        	    int selectedOption = JOptionPane.showConfirmDialog(null, 
	        	    		confirmMessage, 
	                        "Message", 
	                        JOptionPane.YES_NO_OPTION); 
					if (selectedOption == JOptionPane.YES_OPTION) {
						editorFrame.dispose();
						
		    	        String result = xserv.printXML();
		                t.setText(result);
					}
            	}
            }
        });
        
        idList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (mode == 1) { // If mode is edit 
	            	textBookId.setEnabled(true);
	            	textAuthor.setEnabled(true);
	            	textTitle.setEnabled(true);
	            	textGenre.setEnabled(true);
	            	textPrice.setEnabled(true);
	            	textPublishDate.setEnabled(true);

	            	textDescription.setEnabled(true);
            	}
            	
        	    String bookId = idList.getSelectedItem().toString();
        	    NodeObj node = new NodeObj();
        	    node.fromId(bookId);
        	    
        	    textBookId.setText(bookId);
        	    textAuthor.setText(node.getAuthor());
        	    textTitle.setText(node.getTitle());
        	    textGenre.setText(node.getGenre());
        	    textPrice.setText(node.getPrice());
        	    textPublishDate.setText(node.getPublishDate());
        	    textDescription.setText(node.getDescription());
            }
        });
    }
}
