
//BASE PARA EL PROYECTO TOMADA DE LOS EJERCICIOS FIG28_30_32 Y FIG28_25_28 DEL LIBRO DE DEITEL:HOW TO PROGRAM JAVA USADO EN CLASE
//FUNCIONES Y CLASES UTILIZADAS Y ADAPTADAS PARA LA COMPLETICION DE ESTE PROYECTO

//AUTORES: ERICK NAUNAY
//			JONATHAN CAZCO
// 2019/09/30

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;



public class Library_Display extends JFrame
{
	// database URL, username and password
	   static final String DATABASE_URL = "jdbc:mysql://localhost/books";
	   static final String USERNAME = "deitel";
	   static final String PASSWORD = "deitel";
	   
	   // default query retrieves all data joined from the three tables
	   static final String DEFAULT_QUERY = "SELECT \n" + 
	   		"	AuthorISBN.Num AS Lib_Num,\n" + 
	   		"	Authors.*,\n" + 
	   		"    Titles.*\n" + 
	   		"FROM books.Authors\n" + 
	   		"INNER JOIN books.AuthorISBN\n" + 
	   		"USING (AuthorID)\n" + 
	   		"INNER JOIN books.Titles\n" + 
	   		"USING (ISBN)";
	   
	   private ResultSetTableModel tableModel;
	   
	   // create ResultSetTableModel and GUI
	   public Library_Display() 
	   {   
	      super( "Library INDEX" );
	        
	      // create ResultSetTableModel and display database table
	      try 
	      {
	         // create TableModel for results of query 
	         tableModel = new ResultSetTableModel( DATABASE_URL,
	            USERNAME, PASSWORD, DEFAULT_QUERY );
	         
	         // buttons
	         JButton AutoresButton = new JButton( "Editar Autores"); 
	         JButton TitulosButton = new JButton( "Editar Titulos");
	         JButton MainButton = new JButton( "Editar INDEX"); 
	         JButton UpdateButton = new JButton( "Update"); 
	         
	       
	         // create Box to manage placement of queryArea and 
	         // submitButton in GUI
	         Box boxNorth = Box.createHorizontalBox();
	         boxNorth.add(AutoresButton);
	         boxNorth.add(TitulosButton);
	         boxNorth.add(MainButton);
	         boxNorth.add(UpdateButton);

	         // create JTable based on the tableModel
	         JTable resultTable = new JTable( tableModel );
	         
	         JLabel filterLabel = new JLabel( "Filter:" );
	         final JTextField filterText = new JTextField();
	         JButton filterButton = new JButton( "Apply Filter" );
	         Box boxSouth = Box.createHorizontalBox();
	         
	         boxSouth.add( filterLabel );
	         boxSouth.add( filterText );
	         boxSouth.add( filterButton );
	         
	         // place GUI components on content pane
	         add( boxNorth, BorderLayout.NORTH );
	         add( new JScrollPane( resultTable ), BorderLayout.CENTER );
	         add( boxSouth, BorderLayout.SOUTH );

	         
	         
	         AutoresButton.addActionListener
	         (
	        		 new ActionListener()
	        		 {
	        			 public void actionPerformed( ActionEvent event )
	        			 {
	        				 Refactor_Autores.Build_frame();
	        			 }
	        		 } 
	         );
	         
	         TitulosButton.addActionListener
	         (
	        		 new ActionListener()
	        		 {
	        			 public void actionPerformed( ActionEvent event )
	        			 {
	        				 Refactor_Titles.Build_frame();
	        			 }
	        		 } 
	         );
	         
	         MainButton.addActionListener
	         (
	        		 new ActionListener()
	        		 {
	        			 public void actionPerformed( ActionEvent event )
	        			 {
	        				 Refactor_AuISBN.Build_frame();
	        			 }
	        		 } 
	         );
	         
	         UpdateButton.addActionListener
	         (
	        		 new ActionListener()
	        		 {
	        			 public void actionPerformed( ActionEvent event )
	        			 {
	        				 System.out.println("Actualiza All!");
	        					
	        				 
	        				 //remove from gui and db
	        				 try
	        				 {
	        					
	        					tableModel = new ResultSetTableModel( DATABASE_URL,
	        							            USERNAME, PASSWORD, DEFAULT_QUERY ); 
	        					resultTable.setModel(tableModel);
	        				 }
	        				 catch(Exception e)
	        				 {
	        					JOptionPane.showMessageDialog( null, "No actualizado" );
	        				 }
	        			 }
	        		 } 
	         );
	         
	         
	         final TableRowSorter< TableModel > sorter = 
	            new TableRowSorter< TableModel >( tableModel );
	         resultTable.setRowSorter( sorter );
	         setSize( 800, 500 ); // set window size
	         setVisible( true ); // display window  
	         
	      // create listener for filterButton
	         filterButton.addActionListener(            
	            new ActionListener() 
	            {
	               // pass filter text to listener
	               public void actionPerformed( ActionEvent e ) 
	               {
	                  String text = filterText.getText();

	                  if ( text.length() == 0 )
	                     sorter.setRowFilter( null );
	                  else
	                  {
	                     try
	                     {
	                        sorter.setRowFilter( 
	                           RowFilter.regexFilter( text ) );
	                     } // end try
	                     catch ( PatternSyntaxException pse ) 
	                     {
	                        JOptionPane.showMessageDialog( null,
	                           "Bad regex pattern", "Bad regex pattern",
	                           JOptionPane.ERROR_MESSAGE );
	                     } // end catch
	                  } // end else
	               } // end method actionPerfomed
	            } // end annonymous inner class
	         ); // end call to addActionLister
	        
	      } // end try
	      catch ( SQLException sqlException ) 
	      {
	         JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
	            "Database error", JOptionPane.ERROR_MESSAGE );
	               
	         // ensure database connection is closed
	         tableModel.disconnectFromDatabase();
	         
	         System.exit( 1 ); // terminate application
	      } // end catch
	      
	      // dispose of window when user quits application (this overrides
	      // the default of HIDE_ON_CLOSE)
	      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
	      
	      // ensure database connection is closed when user quits application
	      addWindowListener(
	      
	         new WindowAdapter() 
	         {
	            // disconnect from database and exit when window has closed
	            public void windowClosed( WindowEvent event )
	            {
	               tableModel.disconnectFromDatabase();
	               System.exit( 0 );
	            } // end method windowClosed
	         } // end WindowAdapter inner class
	      ); // end call to addWindowListener
	   } // end DisplayQueryResults constructor
	   
	   // execute application
	   public static void main( String args[] ) 
	   {
	      new Library_Display();     
	   } // end main
	

}
