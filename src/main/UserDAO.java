package net.insta.usermanagement.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.sun.xml.internal.bind.CycleRecoverable.Context;
import net.insta.usermanagement.model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/instauser";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";
    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (firstname,lastname,email,username,password) VALUES " +
            " (?,?, ?, ?,?);";
    private static final String SELECT_USER_BY_ID = "select * from users where username =? and password=?";
    private static final String SELECT_USER_BY_USERName="select followers from followersdetail where username =?";
    private static final String INSERT_pendingrequest = "INSERT INTO requestdetails" + "  (username,pendingrequest) VALUES " +
            " (?,?);";
    private static final String INSERT_followrequest = "INSERT INTO followrequestdetails" + "  (username,followrequest) VALUES " +
            " (?,?);";
    private static final String DELETE_pendingrequest = "delete from requestdetails where username =? and pendingrequest=?";
    private static final String DELETE_followrequest="delete from followrequestdetails where username =? and followrequest=?";
    private static final String INSERT_intofollowers1="INSERT INTO followersdetail" + "  (username,followers) VALUES " +
            " (?,?);";
    private static final String INSERT_intofollowers2="INSERT INTO followersdetail" + "  (username,followers) VALUES " +
            " (?,?);";
    private static final String  SELECT_Pendingrequest="select * from requestdetails where username =?";
    private static final String SELECT_USER_For_Suggestion="SELECT username from users  where username != all(select followers from followersdetail where username=?) and username != all(select pendingrequest from requestdetails where username=?) and username != all(select followrequest from followrequestdetails where username=?) and username!=?";
    public UserDAO() {}
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }
    public void insertUser(User user) throws SQLException {
        System.out.println("000");
        // try-with-resource statement will auto close the connection.
        try 
        {    Connection connection = getConnection(); 
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL) ;
           preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2,  user.getLastname());
            preparedStatement.setString(3,  user.getEmailid());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
    public void requestUser(String currentuser,String requestname) throws SQLException {
        
        // try-with-resource statement will auto close the connection.
        try 
        {    Connection connection = getConnection(); 
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_pendingrequest) ;
           preparedStatement.setString(1, requestname);
            preparedStatement.setString(2, currentuser);
           
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
           
            PreparedStatement preparedStatement1 = connection.prepareStatement( INSERT_followrequest) ;
            preparedStatement1.setString(1, currentuser);
             preparedStatement1.setString(2, requestname);
            
             System.out.println(preparedStatement1);
             preparedStatement1.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
 public void acceptUser(String currentuser,String requestname) throws SQLException {
        
        // try-with-resource statement will auto close the connection.
        try 
        {    Connection connection = getConnection(); 
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_intofollowers1) ;
           preparedStatement.setString(1, requestname);
            preparedStatement.setString(2, currentuser);
           
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            PreparedStatement preparedStatement1 = connection.prepareStatement(INSERT_intofollowers2) ;
            preparedStatement1.setString(1, currentuser);
             preparedStatement1.setString(2, requestname);
            
             System.out.println(preparedStatement1);
             preparedStatement1.executeUpdate();
             PreparedStatement preparedStatement2 = connection.prepareStatement(DELETE_pendingrequest) ;
             preparedStatement2.setString(1, currentuser);
              preparedStatement2.setString(2,requestname);
             
              System.out.println(preparedStatement2);
              preparedStatement2.executeUpdate();
              PreparedStatement preparedStatement3 = connection.prepareStatement(DELETE_followrequest) ;
              preparedStatement3.setString(1, requestname);
               preparedStatement3.setString(2,currentuser);
              
               System.out.println(preparedStatement3);
               preparedStatement3.executeUpdate();
              
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
    public int selectUser(User newuser) {
       String name=null;
       int flag=0;
        // Step 1: Establishing a Connection
        try {Connection connection = getConnection();
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID); 
            preparedStatement.setString(1, newuser.getUsername());
            preparedStatement.setString(2, newuser.getPassword());
            System.out.println(preparedStatement);
            
            // Step 3: Execute the query or update query
            ResultSet res = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            if (res.next()) {
				if (res.getString("username").equals(newuser.getUsername()) && res.getString("password").equals(newuser.getPassword()))

				{flag = 1;
				} 
			
			}

            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return  flag;
    }
    public JSONArray selectFollowers(String admin) {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
         // Step 1: Establishing a Connection
         try {Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERName); 
             preparedStatement.setString(1, admin);
             System.out.println(preparedStatement);
             
             // Step 3: Execute the query or update query
             ResultSet res = preparedStatement.executeQuery();

             // Step 4: Process the ResultSet object.
            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("followers");
				 obj.put("followersname", name1);
				 list.add(obj);
 			}

             
         } catch (SQLException e) {
         	e.printStackTrace();
         }
         return  list;
     }
    public JSONArray selectPendingRequest(String currentuser) {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
         // Step 1: Establishing a Connection
         try {Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_Pendingrequest); 
             preparedStatement.setString(1, currentuser);
             System.out.println(preparedStatement);
             
             // Step 3: Execute the query or update query
             ResultSet res = preparedStatement.executeQuery();

             // Step 4: Process the ResultSet object.
            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("pendingrequest");
				 obj.put("pendingrequest", name1);
				 list.add(obj);
 			}

             
         } catch (SQLException e) {
         	e.printStackTrace();
         }
         return  list;
     }
    public JSONArray allUser(String admin) {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
         // Step 1: Establishing a Connection
         try {Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_For_Suggestion); 
             preparedStatement.setString(1, admin);
             preparedStatement.setString(2, admin);
             preparedStatement.setString(3, admin);
             preparedStatement.setString(4, admin);
             System.out.println(preparedStatement);
             
             // Step 3: Execute the query or update query
             ResultSet res = preparedStatement.executeQuery();

             // Step 4: Process the ResultSet object.
            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("username");
				 obj.put("users", name1);
				 list.add(obj);
 			}

             
         } catch (SQLException e) {
         	e.printStackTrace();
         }
         return  list;
     }
    public int insertimage(String currentuser, InputStream file) throws FileNotFoundException {
    	String SQL
        = "INSERT INTO users "
          + "(first_name, last_name, "
          + "photo) values (?, ?, ?)";
    int row = 0;
         try {Connection connection = getConnection();
         // Step 2:Create a statement using connection object
         PreparedStatement preparedStatement;
         preparedStatement
             = connection.prepareStatement(SQL);
         
         preparedStatement
             .setString(1, currentuser);


         if (file != null) {

             // Fetches the input stream
             // of the upload file for
             // the blob column
             preparedStatement.setBlob(3, file);
         }

         // Sends the statement to
         // the database server
         row = preparedStatement
                   .executeUpdate();
     }
     catch (SQLException e) {
         System.out.println(e.getMessage());
     }

     return row;
        
     }
}
