
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


public class Refactor_Titles 
{
	private static final String URL = "jdbc:mysql://localhost/books";
	private static final String USERNAME = "deitel";
	private static final String PASSWORD = "deitel";

	public static Connection connection = null; // manages connection
	public static PreparedStatement insertNewTitle = null; 
	public static PreparedStatement DeleteTitle = null; 
	public static PreparedStatement Def_Update = null;
	
	public static JFrame Titles_frame = new JFrame();
	public static ResultSetTableModel table_titles = null;
	public static JTable resultTable_T = null;
	   
	public static void Build_frame()
	{
		// create new window
		 System.out.println("Editar Titulos");
		 
		 Titles_frame.setTitle("Registro Autores");
		 Titles_frame.setSize(600, 250);
		 Titles_frame.setVisible(true);
		 
		 //create elements
		 JButton agregar = new JButton( "Nuevo..."); 
		 JButton borrar = new JButton( "Eliminar!");
		 JButton modificar = new JButton( "Modificar?");
		 JButton actualizar = new JButton( "Actualizar>>");
		 
		 
		
		 try 
		 {
			String DEFAULT_QUERYA = "SELECT * FROM Titles";
			table_titles = new ResultSetTableModel( URL,
					            USERNAME, PASSWORD, DEFAULT_QUERYA );
			
			//table to modify
			resultTable_T = new JTable( table_titles );
			Titles_frame.add( new JScrollPane( resultTable_T ), BorderLayout.CENTER );
			
			 //add elements in window
			Box boxN = Box.createHorizontalBox();
	        boxN.add(agregar);
	        boxN.add(borrar);
	        boxN.add(modificar);
	        boxN.add(actualizar);
	        Titles_frame.add( boxN, BorderLayout.NORTH );
	        
	        //event handlers
	        
	        agregar.addActionListener
	        (
	       		 new ActionListener()
	       		 {
	       			 public void actionPerformed( ActionEvent event )
	       			 {
	       				 Refactor_Titles.nuevo_Title();
	       			 
	       			 }
	       		 }
	        );
	        
	        borrar.addActionListener
	        (
	        		new ActionListener()
		       		 {
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 eliminar_Title();
		       				 
		       				 
		       			 }
		       		 }
	        		
	        
	        );
	        
			actualizar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 actualizar_T();
		       			 }
		       		 }
	        );
			
			modificar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 modif_T();
		       			 }
		       		 }
	        );
		 } 
		 catch ( SQLException sqlException ) 
	     {
	         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
	            "Database error", JOptionPane.ERROR_MESSAGE );
	               
	         // ensure database connection is closed
	         table_titles.disconnectFromDatabase();
	         
	         System.exit( 1 ); // terminate application
	     }
		 
		 
		
	}
	
	   public static void insertButtonActionPerformed( ActionEvent evt , JTextField isbn, JTextField titlen, JTextField edit, JTextField crr, JFrame agregar) 
	   {
	      int result = addTitle( isbn.getText(), titlen.getText(), edit.getText(), crr.getText());
	      
	      if ( result == 1 )
	      {
	 
	    	  JOptionPane.showMessageDialog( agregar, "Titulo ingresado!",
	  	           "Nuevo Titulo", JOptionPane.PLAIN_MESSAGE ); 
	      }
	         
	      else
	         JOptionPane.showMessageDialog( agregar, "Titulo no ingresado! --> ISBN Duplicado o Nulo",
	            "Error", JOptionPane.PLAIN_MESSAGE );
	   } // end method insertButtonActionPerformed
	   
	   public static int addTitle(String isbn, String tname, String edn, String cr)
	   {
	      int result = 0;
	      
	      // set parameters, then execute insertNewTitle
	      try 
	      {
	    	  connection = 
	    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
	    	  insertNewTitle = connection.prepareStatement( 
			            "INSERT INTO Titles " + 
			            "(ISBN, Title, EditionNumber, Copyright) " + 
			            "VALUES ( ?, ?, ?, ?)" );
	    	  
	         insertNewTitle.setString( 1, isbn );
	         insertNewTitle.setString( 2, tname );
	         insertNewTitle.setString( 3, edn );
	         insertNewTitle.setString( 4, cr );
	
	         // insert the new entry; returns # of rows updated
	         result = insertNewTitle.executeUpdate(); 
	      } // end try
	      catch ( SQLException sqlException )
	      {
	         sqlException.printStackTrace();
	         //close();
	      } // end catch
	      
	      return result;
	   } // end method addPerson
	   	   
	   public static void nuevo_Title()
	   {
		     JFrame agregar = new JFrame("Nuevo Titulo");
			 JPanel navigatePanel = new JPanel();
			 JPanel displayPanel = new JPanel();
			
			 JLabel ISBN = new JLabel("ISBN:");
			 JTextField ISBNField = new JTextField( 10 );
			 JLabel Title = new JLabel("Titulo:");
			 JTextField TitleField = new JTextField( 10 );
			 JLabel ED = new JLabel("Edicion Num:");
			 JTextField EDField = new JTextField( 10 );
			 JLabel Copyr = new JLabel("CopyR:");
			 JTextField CopyrField = new JTextField( 10 );
			 JButton insertButton = new JButton();
	
			 agregar.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10 ) );
			 agregar.setSize( 300, 400 );
			 agregar.setResizable(true);
	
			 navigatePanel.setLayout(
	         new BoxLayout( navigatePanel, BoxLayout.X_AXIS ) );
	
			 displayPanel.setLayout( new GridLayout( 10, 1, 4, 4 ) );

 		      displayPanel.add( ISBN );
 		      displayPanel.add( ISBNField );

 		      displayPanel.add( Title );
 		      displayPanel.add( TitleField );
 		      
 		     displayPanel.add( ED );
		      displayPanel.add( EDField );
		      
		      displayPanel.add( Copyr );
 		      displayPanel.add( CopyrField );
 		     displayPanel.add(insertButton);
 		      
 		      
 		      agregar.add( displayPanel );

 		      insertButton.setText( "Insert New Title" );
 		      insertButton.addActionListener(
 		         new ActionListener()
 		         {
 		            public void actionPerformed( ActionEvent evt )
 		            {
 		            	Refactor_Titles.insertButtonActionPerformed( evt, ISBNField, TitleField, EDField, CopyrField, agregar);
 		            	System.out.println("Agregar Autor");
 		            } // end method actionPerformed
 		         } // end anonymous inner class
 		      ); // end call to addActionListener

 		      agregar.setVisible(true);
	   }
	   
	   public static void eliminar_Title()
	   {
		   System.out.println("Borrado T");
				
				 
				 //remove from gui and db
				 try
				 {
					 
					 int curindex = resultTable_T.getSelectedRow();
					 if(curindex < 0)
					 {
						JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );

					 }
					 String cellid = resultTable_T.getModel().getValueAt(curindex, 0).toString();
					 String del_query = "DELETE FROM Titles WHERE ISBN = '" + cellid + "'";
					 
					 System.out.println(del_query);
					 
					connection = 
				            DriverManager.getConnection( URL, USERNAME, PASSWORD );
					DeleteTitle = connection.prepareStatement(del_query);
					DeleteTitle.execute();
					
					JOptionPane.showMessageDialog(null, "Title Deleted Succesfully!" );
					
					DeleteTitle.close();
					connection.close();
					
					 
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "Can't delete title, DB not updated" );
				 }
		   
	   }

	   public static void actualizar_T()
	   {
		   System.out.println("Actualiza T!");
				
				 
				 //remove from gui and db
				 try
				 {
					String DEFAULT_QUERYA = "SELECT * FROM Titles";
					table_titles = new ResultSetTableModel( URL,
							            USERNAME, PASSWORD, DEFAULT_QUERYA ); 
					resultTable_T.setModel(table_titles);
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "No actualizado" );
				 }
	   }

	   public static void modif_T()
	   {
		   System.out.println("Modificar T");
		   
		   int curindex = resultTable_T.getSelectedRow();
		   if(curindex < 0)
			 {
				JOptionPane.showMessageDialog( null, "No item selected" );
				
			 }
		   String cellid = resultTable_T.getModel().getValueAt(curindex, 0).toString();
		   String tname = resultTable_T.getModel().getValueAt(curindex, 1).toString();
		   String tver = resultTable_T.getModel().getValueAt(curindex, 2).toString();
		   String tcr = resultTable_T.getModel().getValueAt(curindex, 3).toString();
		   
		   
		   
		 //crear GUI
		   
		   	 JFrame agregar = new JFrame("Modificar Titulo");
			 JPanel navigatePanel = new JPanel();
			 JPanel displayPanel = new JPanel();
			
			 JLabel tis = new JLabel();
			 JTextField tisField = new JTextField(cellid, 10);
			 JLabel tNameLabel = new JLabel();
			 JTextField tNameTextField = new JTextField(tname, 10);
			 JLabel tverNameLabel = new JLabel();
			 JTextField tverNameTextField = new JTextField(tver, 10);
			 JLabel tcrNameLabel = new JLabel();
			 JTextField tcrNameTextField = new JTextField(tcr, 10);
	
			 JButton updatebut = new JButton();
	
			 agregar.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10 ) );
			 agregar.setSize( 300, 300 );
			 agregar.setResizable(true);
	
			 navigatePanel.setLayout(
	         new BoxLayout( navigatePanel, BoxLayout.X_AXIS ) );
	
			 displayPanel.setLayout( new GridLayout( 10, 1, 4, 4 ) );
	
			 tis.setText( "Title ISBN:" );
		      displayPanel.add( tis );
		      displayPanel.add( tisField );
		      
		      tNameLabel.setText( "Nombre:" );
		      displayPanel.add( tNameLabel );
		      displayPanel.add( tNameTextField );
	
		      tverNameLabel.setText( "Edicion:" );
		      displayPanel.add( tverNameLabel );
		      displayPanel.add( tverNameTextField );
		      
		      tcrNameLabel.setText( "Copyright:" );
		      displayPanel.add( tcrNameLabel );
		      displayPanel.add( tcrNameTextField );
		      
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
			       		    			  	"UPDATE Titles " + 
			       				            "SET ISBN = '" + tisField.getText() + "',"
			       				            + " Title = '" + tNameTextField.getText() + "',"
			       				            + " EditionNumber = " + tverNameTextField.getText() + ","
			       				            + " Copyright = '" + tcrNameTextField.getText() + "'"
			       				            + "WHERE ISBN = '" + cellid + "'";
			       		    	  
			       		    	  System.out.println(query);
			       		    	  
			       		    	  insertNewTitle = connection.prepareStatement(query);
			       		    	  
			       		    	  insertNewTitle.executeUpdate(); 
			       		    	  
			       		    	  JOptionPane.showMessageDialog(agregar,"Modificado Correctamente", "Info" , JOptionPane.INFORMATION_MESSAGE );
			       		    	  insertNewTitle.close();
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
