package net.insta.usermanagement.dao;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import net.insta.management.constant.Constants;
import net.insta.usermanagement.exceptions.MyException;
import net.insta.usermanagement.model.User;

public class Userdao {
	//private String jdbcURL = "jdbc:mysql://localhost:3306/instauser";
	
	//private String jdbcURL = getproperty("db_host_name")
	//config.properties
	//db_host_name=mysql-database-instance1.cynvfbiwlpul.us-east-1.rds.amazonaws.com


private String jdbcURL ="jdbc:mysql://localhost:3306/instauser";
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
    private static final String SELECT_feed="select * from postdetails where username=? ";
    private static final String  SELECT_Pendingrequest="select * from requestdetails where username=?";
    private static final String  SELECT_id_feed="select id,username from postdetails where id in(select id from postdetails where username in(select followers from followersdetail where username =?) order by id desc) order by id desc limit 10";
    private static final String SELECT_id_post="select id from postdetails where username=? order by id desc";
    private static final String  SELECT_Followrequest="select * from followrequestdetails where username =?";
    private static final String SELECT_USER_For_Suggestion="SELECT username from users  where username != all(select followers from followersdetail where username=?) and username != all(select pendingrequest from requestdetails where username=?) and username != all(select followrequest from followrequestdetails where username=?) and username!=?";
    public Userdao() {}
 
        protected Connection getConnection() throws MyException, ClassNotFoundException, SQLException, IOException {
        Connection connection = null;
        String  jdbcPassword1=null;
        System.out.print("in");
        try {
        	
        	File configDir = new File(System.getProperty("catalina.home"), "conf");
        	System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        	System.out.println(configDir);
   	    File configFile = new File(configDir, "spring.properties");
   	 System.out.println(configFile);
  	    InputStream stream = new FileInputStream(configFile);
   	    Properties props = new Properties();
   	    props.load(stream);
   	    String jdbcClass= props.getProperty("spring.db.class");
   	    String jdbcURL1= props.getProperty("spring.db.host");
    	    String jdbcUsername1= props.getProperty("spring.db.username");
   	    jdbcPassword1= props.getProperty("spring.db.password");
           Class.forName(jdbcClass);
          System.out.print("123455");
     
         System.out.print(jdbcPassword1);
          System.out.print(jdbcUsername1);
        	Class.forName(jdbcClass);
        	   connection = DriverManager.getConnection(jdbcURL1, jdbcUsername1, jdbcPassword1);

                   }
        catch(CommunicationsException c)
        {
        	 
       	 throw new CommunicationsException(jdbcPassword1, c);
        }
        catch(ClassNotFoundException e){
                      throw new ClassNotFoundException();
                   } 
       

        
        return connection;
    }
        

    public void insertUser(User user) throws SQLException, MyException {
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
            throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
        }
        catch(Exception e)
        {
       	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
        }
    }
    public void requestUser(String currentuser,String requestname) throws SQLException, MyException {
        
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
            throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
        }
        catch(Exception e)
        {
       	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
        }
    }
 public JSONArray acceptUser(String currentuser,String requestname) throws SQLException, MyException {
        
        // try-with-resource statement will auto close the connection.
	 JSONArray list = new JSONArray();
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
               PreparedStatement preparedStatement4 = connection.prepareStatement(SELECT_feed); 
               
               preparedStatement4.setString(1, requestname);
               System.out.println(preparedStatement4);
               
               // Step 3: Execute the query or update query
               ResultSet res = preparedStatement4.executeQuery();

              while(res.next()) {
              	JSONObject obj = new JSONObject(); 
              	int id = res.getInt("id");
              	String name1 = res.getString("username");
              	obj.put("id",id);
              	obj.put("follower",name1);
  				 obj.put("image","authenticate/getfeed/"+id);
  				 list.add(obj);
  			
              
        } 
        } catch (SQLException e) {
            throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
        }
        catch(Exception e)
        {
       	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
        }
        return list;
     }
 public void declineUser(String currentuser,String declineidname) throws SQLException, MyException {
     
     // try-with-resource statement will auto close the connection.
     try 
     {    Connection connection = getConnection();
         
          PreparedStatement preparedStatement2 = connection.prepareStatement(DELETE_pendingrequest) ;
          preparedStatement2.setString(1, currentuser);
           preparedStatement2.setString(2,declineidname);
          
           System.out.println(preparedStatement2);
           preparedStatement2.executeUpdate();
           PreparedStatement preparedStatement3 = connection.prepareStatement(DELETE_followrequest) ;
           preparedStatement3.setString(1, declineidname);
            preparedStatement3.setString(2,currentuser);
           
            System.out.println(preparedStatement3);
            preparedStatement3.executeUpdate();
           
     } catch (SQLException e) {
         throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
     }
     catch(Exception e)
     {
    	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
     }
 }
    public int selectUser(User newuser) throws MyException {
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
        	 throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
        }
        catch(Exception e)
        {
       	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
        }
        return  flag;
    }
    public JSONArray selectFollowers(String admin) throws MyException  {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
         try {
        	 Connection connection = getConnection();
             // Step 2:Create a statement using connection object
         
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERName); 
             preparedStatement.setString(1,admin);
             System.out.println(preparedStatement);
             
             ResultSet res = preparedStatement.executeQuery();
             JSONObject obj2 = new JSONObject(); 
			 
             // Step 4: Process the ResultSet object.
            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("followers");
				 obj.put("followersname", name1);
				 list.add(obj);
 			}
             
         } 
         catch(CommunicationsException c)
         {
        	 c.printStackTrace();
        	 throw new MyException(Constants.failure,Constants.resourceerr,Constants.servererr);
         }
         catch (SQLException e) {
        throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
         }
         catch(Exception e)
         {
        	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
         }
        
         return  list;
     }
    public int uploadFile(String currentuser,InputStream file) throws MyException, ClassNotFoundException, IOException {
         String name=null;
         int flag=0;
         String sql
         = "INSERT INTO postdetails "
           + "(username, "
           + "postimage) values (?, ?)";
     int row = 0;
        
          // Step 1: Establishing a Connection
          try {Connection connection = getConnection();
              // Step 2:Create a statement using connection object
              PreparedStatement preparedStatement = connection.prepareStatement(sql); 
              preparedStatement.setString(1, currentuser);
              if (file != null) {
            	
	                preparedStatement.setBlob(2, file);
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
    public JSONArray selectPendingRequest(String currentuser) throws MyException {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
       
         try {Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_Pendingrequest); 
             preparedStatement.setString(1, currentuser);
            
            
             System.out.println(preparedStatement);
             
             // Step 3: Execute the query or update query
             ResultSet res = preparedStatement.executeQuery();
             
             
            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("pendingrequest");
				 obj.put("pendingrequest", name1);
				 list.add(obj);
 			}
         }
         catch(CommunicationsException c)
         {
        	 throw new MyException(Constants.failure,Constants.resourceerr,Constants.servererr);
         }
         catch (SQLException e) {
        throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
         }
         catch(Exception e)
         {
        	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
         }
         
         return  list;
         
     }
    public byte[] selectimage(int id) throws Exception {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
        byte[] bytes=null;
         // Step 1: Establishing a Connection
         try {Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement("select postimage from postdetails where id = ?"); 
             preparedStatement.setInt(1, id);
            
            
             System.out.println(preparedStatement);
             
             // Step 3: Execute the query or update query
             ResultSet res = preparedStatement.executeQuery();
             
             // Step 4: Process the ResultSet object.
             if(res.next()){
				    Blob	image = res.getBlob(1);
			

			 bytes = image.getBytes(1, (int) image.length());
			
				}
				res.close();
				preparedStatement.close();
			    connection.close();
			    }
			    

             
        catch (SQLException e) {
         	throw new SQLException();
         }
         catch (Exception e) {
          	throw new Exception();
          }
         return  bytes;
     }
    
    public JSONArray selectpost(String currentuser) throws MyException {
         String name=null;
         int flag=0;
         JSONArray list = new JSONArray();
          // Step 1: Establishing a Connection
          try {Connection connection = getConnection();
              // Step 2:Create a statement using connection object
              PreparedStatement preparedStatement = connection.prepareStatement(SELECT_id_post); 
              preparedStatement.setString(1, currentuser);
              
              System.out.println(preparedStatement);
              
              // Step 3: Execute the query or update query
              ResultSet res = preparedStatement.executeQuery();
             
              // Step 4: Process the ResultSet object.
             while(res.next()) {
             	JSONObject obj = new JSONObject(); 
             	int name1 = res.getInt("id");
 				 obj.put("image","authenticate/getimage/"+name1);
 				 list.add(obj);
  			}  
          }    catch(CommunicationsException c)
          {
         	 
         	 throw new MyException(Constants.failure,Constants.resourceerr,Constants.servererr);
          }
          catch (SQLException e) {
         throw new MyException(Constants.failure,"sql",Constants.dberr);
          }
          catch(Exception e)
          {
         	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
          }
          return  list;
      }
     public JSONArray selectfeed(String currentuser) throws MyException {
         String name=null;
         int flag=0;
         JSONArray list = new JSONArray();
          // Step 1: Establishing a Connection
          try {Connection connection = getConnection();
              // Step 2:Create a statement using connection object
              PreparedStatement preparedStatement = connection.prepareStatement(SELECT_id_feed); 
            
              preparedStatement.setString(1, currentuser);
              System.out.println(preparedStatement);
              
              // Step 3: Execute the query or update query
              ResultSet res = preparedStatement.executeQuery();

             while(res.next()) {
             	JSONObject obj = new JSONObject(); 
             	int id = res.getInt("id");
             	String name1 = res.getString("username");
             	obj.put("id",id);
             	obj.put("follower",name1);
 				 obj.put("image","authenticate/getfeed/"+id);
 				 list.add(obj);
  			}
          } 
          catch(CommunicationsException c)
          {
         	 throw new MyException(Constants.failure,Constants.resourceerr,Constants.servererr);
          }
          catch (SQLException e) {
         throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
          }
          catch(Exception e)
          {
         	 throw new MyException(Constants.failure,Constants.apperr,Constants.apperr);
          }
         
          return  list;
      }
    public JSONArray allUser(String admin) throws MyException {
        String name=null;
        int flag=0;
        JSONArray list = new JSONArray();
         
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

            while(res.next()) {
            	JSONObject obj = new JSONObject(); 
            	String name1 = res.getString("username");
				 obj.put("users", name1);
				 list.add(obj);
 			}  
         }   catch(CommunicationsException c)
         {
        	 throw new MyException(Constants.failure,Constants.resourceerr,Constants.servererr);
         }
         catch (SQLException e) {
        throw new MyException(Constants.failure,Constants.sql,Constants.dberr);
         }
         
         catch(Exception e)
         {
        	 throw new MyException("failure",Constants.apperr,Constants.apperr);
         }
         
         return  list;
     }
    public int insertimage(String currentuser, InputStream file) throws Exception {
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
        	 throw new MyException(Constants.failure,"sql",Constants.dberr);
          }
          catch (Exception e) {
           	throw new Exception();
           }
     return row;
        
     }
    
}
