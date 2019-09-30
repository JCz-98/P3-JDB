import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class Refactor_Autores 
{
	private static final String URL = "jdbc:mysql://localhost/books";
	private static final String USERNAME = "deitel";
	private static final String PASSWORD = "deitel";

	public static Connection connection = null; // manages connection
	public static PreparedStatement insertNewPerson = null; 
	public static PreparedStatement DeletePerson = null; 
	public static PreparedStatement Def_Update = null;
	
	public static JFrame Autors_frame = new JFrame();
	public static ResultSetTableModel table_autores = null;
	public static JTable resultTable_A = null;
	   
	public static void Build_frame()
	{
		// create new window
		 System.out.println("Editar Autores");
		 
		 Autors_frame.setTitle("Registro Autores");
		 Autors_frame.setSize(600, 250);
		 Autors_frame.setVisible(true);
		 
		 //create elements
		 JButton agregar = new JButton( "Nuevo..."); 
		 JButton borrar = new JButton( "Eliminar!");
		 JButton modificar = new JButton( "Modificar?");
		 JButton actualizar = new JButton( "Actualizar>>");
		 
		 
		
		 try 
		 {
			String DEFAULT_QUERYA = "SELECT * FROM authors";
			table_autores = new ResultSetTableModel( URL,
					            USERNAME, PASSWORD, DEFAULT_QUERYA );
			
			//table to modify
			resultTable_A = new JTable( table_autores );
			Autors_frame.add( new JScrollPane( resultTable_A ), BorderLayout.CENTER );
			
			 //add elements in window
			Box boxN = Box.createHorizontalBox();
	        boxN.add(agregar);
	        boxN.add(borrar);
	        boxN.add(modificar);
	        boxN.add(actualizar);
	        Autors_frame.add( boxN, BorderLayout.NORTH );
	        
	        //event handlers
	        
	        agregar.addActionListener
	        (
	       		 new ActionListener()
	       		 {
	       			 public void actionPerformed( ActionEvent event )
	       			 {
	       				 Refactor_Autores.nuevo_Autor();
	       			 
	       			 }
	       		 }
	        );
	        
	        borrar.addActionListener
	        (
	        		new ActionListener()
		       		 {
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 eliminar_Autor();
		       				 
		       				 
		       			 }
		       		 }
	        		
	        
	        );
	        
			actualizar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 actualizar_A();
		       			 }
		       		 }
	        );
			
			modificar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 modif_A();
		       			 }
		       		 }
	        );
		 } 
		 catch ( SQLException sqlException ) 
	     {
	         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
	            "Database error", JOptionPane.ERROR_MESSAGE );
	               
	         // ensure database connection is closed
	         table_autores.disconnectFromDatabase();
	         
	         System.exit( 1 ); // terminate application
	     }
		 
		 
		
	}
	
	   public static void insertButtonActionPerformed( ActionEvent evt , JTextField fname, JTextField lname, JFrame agregar) 
	   {
	      int result = addPerson( fname.getText(), lname.getText());
	      
	      if ( result == 1 )
	      {
	 
	    	  JOptionPane.showMessageDialog( agregar, "Autor ingresado!",
	  	            "Autor Ingresado", JOptionPane.PLAIN_MESSAGE ); 
	      }
	         
	      else
	         JOptionPane.showMessageDialog( agregar, "Autor no ingresado!",
	            "Error", JOptionPane.PLAIN_MESSAGE );
	   } // end method insertButtonActionPerformed
	   
	   public static int addPerson(String fname, String lname)
	   {
	      int result = 0;
	      
	      // set parameters, then execute insertNewPerson
	      try 
	      {
	    	  connection = 
	    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
	    	  insertNewPerson = connection.prepareStatement( 
			            "INSERT INTO Authors " + 
			            "(FirstName, LastName) " + 
			            "VALUES ( ?, ?)" );
	    	  
	         insertNewPerson.setString( 1, fname );
	         insertNewPerson.setString( 2, lname );
	
	         // insert the new entry; returns # of rows updated
	         result = insertNewPerson.executeUpdate(); 
	      } // end try
	      catch ( SQLException sqlException )
	      {
	         sqlException.printStackTrace();
	         //close();
	      } // end catch
	      
	      return result;
	   } // end method addPerson
	   
	   
	   public static void nuevo_Autor()
	   {
		     JFrame agregar = new JFrame("Nuevo Autor");
			 JPanel navigatePanel = new JPanel();
			 JPanel displayPanel = new JPanel();
			
			 JLabel firstNameLabel = new JLabel();
			 JTextField firstNameTextField = new JTextField( 10 );
			 JLabel lastNameLabel = new JLabel();
			 JTextField lastNameTextField = new JTextField( 10 );
			 JButton insertButton = new JButton();
	
			 agregar.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10 ) );
			 agregar.setSize( 300, 300 );
			 agregar.setResizable(true);
	
			 navigatePanel.setLayout(
	         new BoxLayout( navigatePanel, BoxLayout.X_AXIS ) );
	
			 displayPanel.setLayout( new GridLayout( 10, 1, 4, 4 ) );

 		      firstNameLabel.setText( "First Name:" );
 		      displayPanel.add( firstNameLabel );
 		      displayPanel.add( firstNameTextField );

 		      lastNameLabel.setText( "Last Name:" );
 		      displayPanel.add( lastNameLabel );
 		      displayPanel.add( lastNameTextField );
 		      
 		      displayPanel.add(insertButton);
 		      
 		      
 		      agregar.add( displayPanel );

 		      insertButton.setText( "Insert New Entry" );
 		      insertButton.addActionListener(
 		         new ActionListener()
 		         {
 		            public void actionPerformed( ActionEvent evt )
 		            {
 		            	Refactor_Autores.insertButtonActionPerformed( evt, firstNameTextField, lastNameTextField, agregar);
 		            	System.out.println("Agregar Autor");
 		            } // end method actionPerformed
 		         } // end anonymous inner class
 		      ); // end call to addActionListener
 		      
 		      agregar.setVisible(true);
	   }
	   
	   public static void eliminar_Autor()
	   {
		   System.out.println("Boorrado");
				
				 
				 //remove from gui and db
				 try
				 {
					 
					 int curindex = resultTable_A.getSelectedRow();
					 if(curindex < 0)
					 {
						JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );

					 }
					 String cellid = resultTable_A.getModel().getValueAt(curindex, 0).toString();
					 String del_query = "DELETE FROM Authors WHERE AuthorID = " + cellid;
					 
					 System.out.println(del_query);
					 
					connection = 
				            DriverManager.getConnection( URL, USERNAME, PASSWORD );
					DeletePerson = connection.prepareStatement(del_query);
					DeletePerson.execute();
					
					JOptionPane.showMessageDialog( null, "Deleted Succesfully!" );
					
					DeletePerson.close();
					connection.close();
					
					 
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );
				 }
		   
	   }

	   public static void actualizar_A()
	   {
		   System.out.println("Actualiza!");
				
				 
				 //remove from gui and db
				 try
				 {
					String DEFAULT_QUERYA = "SELECT * FROM authors";
					table_autores = new ResultSetTableModel( URL,
							            USERNAME, PASSWORD, DEFAULT_QUERYA ); 
					resultTable_A.setModel(table_autores);
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "No actualizado" );
				 }
	   }

	   public static void modif_A()
	   {
		   System.out.println("Modificar");
		   
		   int curindex = resultTable_A.getSelectedRow();
		   if(curindex < 0)
			 {
				JOptionPane.showMessageDialog( null, "No item selected" );
				
			 }
		   String cellid = resultTable_A.getModel().getValueAt(curindex, 0).toString();
		   String aname = resultTable_A.getModel().getValueAt(curindex, 1).toString();
		   String alast = resultTable_A.getModel().getValueAt(curindex, 2).toString();
		   
		   
		   
		 //crear GUI
		   
		   	 JFrame agregar = new JFrame("Modificar Autor");
			 JPanel navigatePanel = new JPanel();
			 JPanel displayPanel = new JPanel();
			
			 JLabel aid = new JLabel();
			 JTextField aidField = new JTextField(cellid, 10);
			 aidField.setEditable(false);
			 
			 JLabel firstNameLabel = new JLabel();
			 JTextField firstNameTextField = new JTextField(aname, 10);
			 JLabel lastNameLabel = new JLabel();
			 JTextField lastNameTextField = new JTextField(alast, 10);
			 JButton updatebut = new JButton();
	
			 agregar.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10 ) );
			 agregar.setSize( 300, 300 );
			 agregar.setResizable(true);
	
			 navigatePanel.setLayout(
	         new BoxLayout( navigatePanel, BoxLayout.X_AXIS ) );
	
			 displayPanel.setLayout( new GridLayout( 10, 1, 4, 4 ) );
	
			 aid.setText( "Author ID:" );
		      displayPanel.add( aid );
		      displayPanel.add( aidField );
		      
		      firstNameLabel.setText( "First Name:" );
		      displayPanel.add( firstNameLabel );
		      displayPanel.add( firstNameTextField );
	
		      lastNameLabel.setText( "Last Name:" );
		      displayPanel.add( lastNameLabel );
		      displayPanel.add( lastNameTextField );
		      
		      updatebut.setText( "Guardar" );
		      displayPanel.add(updatebut);
		      
		      agregar.add( displayPanel );
		      agregar.setVisible(true);
		      
		      //event handler to update
		      
		      updatebut.addActionListener
		        (
		        		new ActionListener()
			       		 {
		        			
			       			 public void actionPerformed(ActionEvent event)
			       			 {
			       				try 
			       		      {
			       		    	  connection = 
			       		    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
			       		    	  String query =   
			       		    			  	"UPDATE Authors " + 
			       				            "SET FirstName = '" + firstNameTextField.getText() + "',"
			       				            + " LastName = '" + lastNameTextField.getText() + "'"
			       				            + "WHERE AuthorID = " + cellid;
			       		    	  
			       		    	  insertNewPerson = connection.prepareStatement(query);
			       		    	  
			       		    	  insertNewPerson.executeUpdate(); 
			       		    	  
			       		    	  JOptionPane.showMessageDialog(agregar,"Autor Modificado Correctamente", "Info" , JOptionPane.INFORMATION_MESSAGE );
			       		    	  insertNewPerson.close();
			       		    	  connection.close();
			       		      } // end try
			       		      catch ( SQLException sqlException )
			       		      {
			       		         sqlException.printStackTrace();
			       		         JOptionPane.showMessageDialog(agregar,"No Modificado", "Info" , JOptionPane.ERROR_MESSAGE );
			       		         //close();
			       		      } // end catch
			       			 }
			       		 }
		        );
			 
		   
	   }
}
