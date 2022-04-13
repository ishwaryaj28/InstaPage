package net.insta.usermanagement.utils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/authenticate/serv1")

public class displayimage extends HttpServlet {
	protected void doGet(
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws ServletException, IOException
	    {
		Blob image = null;
		Connection con = null;
		byte[ ] imgData = null ;
		Statement stmt = null;
		ResultSet rs = null;
		String sql="select postimage from postdetails where username = ?";
		 HttpSession session=request.getSession();  
	       String admin=(String) session.getAttribute("uname");
		  PrintWriter out = response.getWriter();
		try {

		    Class.forName("com.mysql.jdbc.Driver");
		   Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/instauser","root","");
		   PreparedStatement ps = conn.prepareStatement(sql); 
		  ps.setString(1, admin);
		   ResultSet res = ps.executeQuery();
		    sun.misc.BASE64Encoder encoder= new sun.misc.BASE64Encoder();
		    if (res.next()) {
		    image = res.getBlob(1);
		    imgData = image.getBytes(1,(int)image.length());
		    } 
		    else {
		    out.println("Display Blob Example");
		    out.println("image not found for given id>");
		    return;
		    }
		    // display the image
		     JSONArray list=new JSONArray();
					JSONObject obj=new JSONObject();
		    response.setContentType("image/jpg");
		   
		    obj.put("1",encoder.encode(imgData).getBytes());
		    list.add(obj);
		    System.out.println(list);
		   
		    rs.close();
		    stmt.close();
		    con.close();
		} catch (SQLException e) {
		    
		    e.printStackTrace();
		    
		    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	    }
	    

}
