
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


public class Refactor_AuISBN 
{
	private static final String URL = "jdbc:mysql://localhost/books";
	private static final String USERNAME = "deitel";
	private static final String PASSWORD = "deitel";

	public static Connection connection = null; // manages connection
	public static PreparedStatement insertNew = null; 
	public static PreparedStatement Delete = null; 
	public static PreparedStatement Def_Update = null;
	
	public static JFrame AT_frame = new JFrame();
	public static ResultSetTableModel table_AT = null;
	public static JTable resultTable_AT = null;
	   
	public static void Build_frame()
	{
		// create new window
		 System.out.println("Editar Autores-ISBN");
		 
		 AT_frame.setTitle("Registro Autores-ISBN");
		 AT_frame.setSize(900, 600);
		 AT_frame.setVisible(true);
		 
		 //create elements
		 JButton agregar = new JButton( "Nuevo..."); 
		 JButton borrar = new JButton( "Eliminar!");
		 JButton modificar = new JButton( "Modificar?");
		 JButton actualizar = new JButton( "Actualizar>>");
		 
		 
		
		 try 
		 {
			String DEFAULT_QUERYA = "SELECT \n" + 
			   		"	AuthorISBN.Num AS Lib_Num,\n" + 
			   		"	Authors.*,\n" + 
			   		"    Titles.*\n" + 
			   		"FROM books.Authors\n" + 
			   		"INNER JOIN books.AuthorISBN\n" + 
			   		"USING (AuthorID)\n" + 
			   		"INNER JOIN books.Titles\n" + 
			   		"USING (ISBN)";
			table_AT = new ResultSetTableModel( URL,
					            USERNAME, PASSWORD, DEFAULT_QUERYA );
			
			//table to modify
			resultTable_AT = new JTable( table_AT );
			AT_frame.add( new JScrollPane( resultTable_AT ), BorderLayout.CENTER );
			
			 //add elements in window
			Box boxN = Box.createHorizontalBox();
	        boxN.add(agregar);
	        boxN.add(borrar);
	        boxN.add(modificar);
	        boxN.add(actualizar);
	        AT_frame.add( boxN, BorderLayout.NORTH );
	        
	        //event handlers
	        
	        agregar.addActionListener
	        (
	       		 new ActionListener()
	       		 {
	       			 public void actionPerformed( ActionEvent event )
	       			 {
	       				 Refactor_AuISBN.nuevo_AI();
	       			 
	       			 }
	       		 }
	        );
	        
	        borrar.addActionListener
	        (
	        		new ActionListener()
		       		 {
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 eliminar_AI();
		       				 
		       				 
		       			 }
		       		 }
	        		
	        
	        );
	        
			actualizar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 actualizar_AI();
		       			 }
		       		 }
	        );
			
			modificar.addActionListener
	        (
	        		new ActionListener()
		       		 {
	        			
		       			 public void actionPerformed( ActionEvent event )
		       			 {
		       				 modif_AI();
		       			 }
		       		 }
	        );
		 } 
		 catch ( SQLException sqlException ) 
	     {
	         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
	            "Database error", JOptionPane.ERROR_MESSAGE );
	               
	         // ensure database connection is closed
	         table_AT.disconnectFromDatabase();
	         
	         System.exit( 1 ); // terminate application
	     }
		 
		 
		
	}
		   	   
	   public static void nuevo_AI()
	   {
		     JFrame agregar = new JFrame("Nuevo AI -- Seleccion Titulo");
		     agregar.setSize(500,350);
		     agregar.setVisible(true);
		     
			 String DEFAULT_QUERYA = "SELECT * FROM Titles";
			 try 
			 {
				ResultSetTableModel table_titles = null;
				table_titles = new ResultSetTableModel( URL,
							            USERNAME, PASSWORD, DEFAULT_QUERYA );
				
				 JTable resultTable_T = new JTable( table_titles );
				 agregar.add( new JScrollPane( resultTable_T ), BorderLayout.CENTER );
				 
				 JButton next = new JButton( "Continuar ->" );
				 JLabel n = new JLabel("Seleccionar Titulo ^^ ");
		         Box boxSouth = Box.createHorizontalBox();
		         boxSouth.add( n);
		         boxSouth.add( next );
		         
	 		     agregar.add( boxSouth, BorderLayout.SOUTH );
	 		     
	 		     
	 		     
	 		     next.addActionListener(
	 	 		         new ActionListener()
	 	 		         {
	 	 		            public void actionPerformed( ActionEvent evt )
	 	 		            {
	 	 		            	 int curindex = resultTable_T.getSelectedRow();
	 	 						 if(curindex < 0)
	 	 						 {
	 	 							JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );

	 	 						 }
	 	 						 String titleisbn = resultTable_T.getModel().getValueAt(curindex, 0).toString();
	 	 						 
	 	 		            	Refactor_AuISBN.insertButtonActionPerformed( evt, titleisbn, agregar);
	 	 		            	System.out.println("Titulo Seleccionado");
	 	 		            } // end method actionPerformed
	 	 		         } // end anonymous inner class
	 	 		      ); // end call to addActionListener
	 		     
	 		    
			 } 
			 catch (SQLException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
 		      
	   }
	   
	   public static void insertButtonActionPerformed( ActionEvent evt , String ISBN, JFrame agregar) 
	   {
		     JFrame autores = new JFrame("Nuevo AI -- Seleccion Autor");
		     autores.setSize(500,350);
		     autores.setVisible(true);
		     
			 String DEFAULT_QUERYA = "SELECT * FROM Authors";
			 try 
			 {
				ResultSetTableModel table_autores = null;
				table_autores = new ResultSetTableModel( URL,
							            USERNAME, PASSWORD, DEFAULT_QUERYA );
				
				 JTable resultTable_A = new JTable( table_autores );
				 autores.add( new JScrollPane( resultTable_A ), BorderLayout.CENTER );
				 
				 JButton next = new JButton( "Crear A-ISBN" );
				 JLabel n = new JLabel("Seleccionar Autor ^^ ");
		         Box boxSouth = Box.createHorizontalBox();
		         boxSouth.add( n);
		         boxSouth.add( next );
		         
	 		     autores.add( boxSouth, BorderLayout.SOUTH );
	 		     
	 		     
	 		     
	 		     next.addActionListener(
	 	 		         new ActionListener()
	 	 		         {
	 	 		            public void actionPerformed( ActionEvent evt )
	 	 		            {
	 	 		            	 int curindex = resultTable_A.getSelectedRow();
	 	 						 if(curindex < 0)
	 	 						 {
	 	 							JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );

	 	 						 }
	 	 						 String autorID = resultTable_A.getModel().getValueAt(curindex, 0).toString();
	 	 						 
	 	 						int result = addAI(ISBN, autorID);
	 	 				      
		 	 				      if ( result == 1 )
		 	 				      {
		 	 				 
		 	 				    	  JOptionPane.showMessageDialog( agregar, "Autor-ISBN ingresado!",
		 	 				  	           "Nuevo Autor-ISBN", JOptionPane.PLAIN_MESSAGE ); 
		 	 				      }
		 	 				         
		 	 				      else
		 	 				         JOptionPane.showMessageDialog( agregar, "Autor-ISBN no ingresado! --> ISBN Duplicado o AutorID",
		 	 				            "Error", JOptionPane.PLAIN_MESSAGE );
	 	 						 
	 	 						 
	 	 						 
	 	 		            	System.out.println("Autor Seleccionado");
	 	 		            } // end method actionPerformed
	 	 		         } // end anonymous inner class
	 	 		      ); // end call to addActionListener
	 		     
	 		    
			 } 
			 catch (SQLException e) 
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
	   } // end method insertButtonActionPerformed
	   
	   public static int addAI(String isbn, String aid)
	   {
	      int result = 0;
	      
	      // set parameters, then execute insertNew
	      try 
	      {
	    	  connection = 
	    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
	    	  insertNew = connection.prepareStatement( 
			            "INSERT INTO AuthorISBN " + 
			            "(AuthorID, ISBN) " + 
			            "VALUES ( ?, ?)" );
	    	  
	         insertNew.setString( 1, aid );
	         insertNew.setString( 2, isbn );
	
	         // insert the new entry; returns # of rows updated
	         result = insertNew.executeUpdate(); 
	      } // end try
	      catch ( SQLException sqlException )
	      {
	         sqlException.printStackTrace();
	         //close();
	      } // end catch
	      
	      return result;
	   } // end method addPerson
	   
	   public static void eliminar_AI()
	   {
		   System.out.println("Borrado AI");
				
				 
				 //remove from gui and db
				 try
				 {
					 
					 int curindex = resultTable_AT.getSelectedRow();
					 if(curindex < 0)
					 {
						JOptionPane.showMessageDialog( null, "Can't delete, DB not updated" );

					 }
					 String aiID = resultTable_AT.getModel().getValueAt(curindex, 0).toString();
					 String del_query = "DELETE FROM AuthorISBN WHERE Num = " + aiID ;
					 
					 System.out.println(del_query);
					 
					connection = 
				            DriverManager.getConnection( URL, USERNAME, PASSWORD );
					Delete = connection.prepareStatement(del_query);
					Delete.execute();
					
					JOptionPane.showMessageDialog(null, "Title Deleted Succesfully!" );
					
					Delete.close();
					connection.close();
					
					 
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "Can't delete title, DB not updated" );
				 }
		   
	   }

	   public static void actualizar_AI()
	   {
		   System.out.println("Actualiza AI!");
				
				 
				 //remove from gui and db
				 try
				 {
					String DEFAULT_QUERYA = "SELECT \n" + 
					   		"	AuthorISBN.Num AS Lib_Num,\n" + 
					   		"	Authors.*,\n" + 
					   		"    Titles.*\n" + 
					   		"FROM books.Authors\n" + 
					   		"INNER JOIN books.AuthorISBN\n" + 
					   		"USING (AuthorID)\n" + 
					   		"INNER JOIN books.Titles\n" + 
					   		"USING (ISBN)";
					table_AT = new ResultSetTableModel( URL,
							            USERNAME, PASSWORD, DEFAULT_QUERYA ); 
					resultTable_AT.setModel(table_AT);
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog( null, "No actualizado" );
				 }
	   }

	   public static void modif_AI()
	   {
		   System.out.println("Modificar AI");
		   
		   int curindex = resultTable_AT.getSelectedRow();
		   if(curindex < 0)
			 {
				JOptionPane.showMessageDialog( null, "No item selected" );
				
			 }
		   
		   
		   String cellid = resultTable_AT.getModel().getValueAt(curindex, 0).toString();
		   String aID = resultTable_AT.getModel().getValueAt(curindex, 1).toString();
		   String aFN = resultTable_AT.getModel().getValueAt(curindex, 2).toString();
		   String aLN = resultTable_AT.getModel().getValueAt(curindex, 3).toString();
		   String ISBN = resultTable_AT.getModel().getValueAt(curindex, 4).toString();
		   String tname = resultTable_AT.getModel().getValueAt(curindex, 5).toString();
		   String tver = resultTable_AT.getModel().getValueAt(curindex, 6).toString();
		   String tcr = resultTable_AT.getModel().getValueAt(curindex, 7).toString();
		   
		   
		   
		 //crear GUI
		   
		   	 JFrame agregar = new JFrame("Modificar Titulo");
			 JPanel navigatePanel = new JPanel();
			 JPanel displayPanel = new JPanel();
			 
			 JLabel Cid = new JLabel();
			 JTextField CidTextField = new JTextField(cellid, 10);
			 JLabel aIDNameLabel = new JLabel();
			 JTextField aIDTextField = new JTextField(aID, 10);
			 JLabel firstNameLabel = new JLabel();
			 JTextField firstNameTextField = new JTextField(aFN, 10);
			 JLabel lastNameLabel = new JLabel();
			 JTextField lastNameTextField = new JTextField(aLN, 10);
			 JLabel tis = new JLabel();
			 JTextField tisField = new JTextField(ISBN, 10);
			 JLabel tNameLabel = new JLabel();
			 JTextField tNameTextField = new JTextField(tname, 10);
			 JLabel tverNameLabel = new JLabel();
			 JTextField tverNameTextField = new JTextField(tver, 10);
			 JLabel tcrNameLabel = new JLabel();
			 JTextField tcrNameTextField = new JTextField(tcr, 10);
	
			 JButton updatebut = new JButton();
	
			 agregar.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10 ) );
			 agregar.setSize( 300, 350 );
			 agregar.setResizable(true);
	
			 navigatePanel.setLayout(
	         new BoxLayout( navigatePanel, BoxLayout.X_AXIS ) );
	
			 displayPanel.setLayout( new GridLayout( 10, 1, 4, 4 ) );
			 
			 Cid.setText( "Registro_NUM:" );
			 CidTextField.setEditable(false);
		      displayPanel.add( Cid );
		      displayPanel.add( CidTextField );
		      
			 aIDNameLabel.setText( "Author ID:" );
			 aIDTextField.setEditable(false);
		      displayPanel.add( aIDNameLabel );
		      displayPanel.add( aIDTextField );
		      
		      firstNameLabel.setText( "First Name:" );
		      displayPanel.add( firstNameLabel );
		      displayPanel.add( firstNameTextField );
	
		      lastNameLabel.setText( "Last Name:" );
		      displayPanel.add( lastNameLabel );
		      displayPanel.add( lastNameTextField );
		      
			 tis.setText( "Title ISBN:" );
			 tisField.setEditable(false);
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
			       					//update on autors
			       				  connection = 
			       		    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
			       		    	  String query1 =   
			       		    			  	"UPDATE Authors " + 
			       				            "SET FirstName = '" + firstNameTextField.getText() + "',"
			       				            + " LastName = '" + lastNameTextField.getText() + "' "
			       				            + "WHERE AuthorID = " + aID;
			       		    	  
			       		    	  insertNew = connection.prepareStatement(query1);
			       		    	  System.out.println(query1);
			       		    	  
			       		    	  insertNew.executeUpdate(); 
			       		    	  
			       		    	  JOptionPane.showMessageDialog(agregar,"Modificado Autores Correctamente", "Info" , JOptionPane.INFORMATION_MESSAGE );
			       		    	  insertNew.close();
			       		    	  connection.close();
			       		    	  
			       					//update on titles
			       		    	  connection = 
			       		    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
			       		    	  String query2 =   
			       		    			  	"UPDATE Titles " + 
			       				            "SET Title = '" + tNameTextField.getText() + "',"
			       				            + " EditionNumber = " + tverNameTextField.getText() + ","
			       				            + " Copyright = '" + tcrNameTextField.getText() + "' "
			       				            + "WHERE ISBN = '" + ISBN + "'";
			       		    	  
			       		    	  System.out.println(query2);
			       		    	  
			       		    	  insertNew = connection.prepareStatement(query2);
			       		    	  
			       		    	  insertNew.executeUpdate(); 
			       		    	  
			       		    	  JOptionPane.showMessageDialog(agregar,"Modificado Titles Correctamente", "Info" , JOptionPane.INFORMATION_MESSAGE );
			       		    	  insertNew.close();
			       		    	  connection.close();
			       		    	  
			       		    	  //update on authorISBN
			       		    	connection = 
		       		    	            DriverManager.getConnection( URL, USERNAME, PASSWORD );
			       		    	  String query3 =   
			       		    			  	"UPDATE AuthorISBN " + 
			       				            "SET AuthorID = " + aIDTextField.getText() + ","
			       				            + " ISBN = '" + tisField.getText() + "'"
			       				            + "WHERE Num = " + cellid;
			       		    	  
			       		    	  insertNew = connection.prepareStatement(query3);
			       		    	  System.out.println(query3);
			       		    	  
			       		    	  insertNew.executeUpdate(); 
			       		    	  
			       		    	  JOptionPane.showMessageDialog(agregar,"Modificado Autores--ISBN Correctamente", "Info" , JOptionPane.INFORMATION_MESSAGE );
			       		    	  insertNew.close();
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

