package net.insta.usermanagement.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.insta.management.constant.Constants;

// This is the annotation-based
// mapping URL to Servlet.
@WebServlet("/authenticate/serv")

@MultipartConfig(maxFileSize = 16177215)

public class ImageServlet extends HttpServlet {

	// auto generated
	private static final long serialVersionUID = 1L;
	private String jdbcURL = "jdbc:mysql://localhost:3306/instauser";
	private String jdbcUsername = "root";
	private String jdbcPassword = "";

	protected Connection getConnection() throws IOException {
		Connection connection = null;
String jdbcPassword1=null;
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
			connection = DriverManager.getConnection(jdbcURL1, jdbcUsername1, jdbcPassword1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block

		}
		return connection;
	}

	// This Method takes in All the information
	// required and is used to store in the
	// MySql Database.
	public int uploadFile(String uname,

			InputStream file) throws IOException {
		String sql = "INSERT INTO postdetails " + "(username, " + "postimage) values (?, ?)";
		int row = 0;

		Connection connection = getConnection();

		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, uname);

			if (file != null) {

				// Fetches the input stream
				// of the upload file for
				// the blob column
				preparedStatement.setBlob(2, file);
			}

			row = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return row;
	}

	// As Submit button is hit from
	// the Web Page, request is made
	// to this Servlet and
	// doPost method is invoked.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Getting the parametes from web page
		PrintWriter out = response.getWriter();
		System.out.print("********************");
		// Input stream of the upload file
		InputStream inputStream = null;

		String message = null;

		// Obtains the upload file
		// part in this multipart request
		Part filePart = request.getPart("myimg");
		response.setContentType("text/html");
		if (filePart.getSize() == 0) {
			out.print("file not uploaded");

			throw new FileNotFoundException();

		}
		if (filePart != null) {

			// Prints out some information
			// for debugging
			System.out.println(filePart.getName());
			System.out.println(filePart.getSize());
			System.out.println(filePart.getContentType());

			// Obtains input stream of the upload file
			inputStream = filePart.getInputStream();
		} else

		{

			out.print("file not uploaded");
			RequestDispatcher rd = request.getRequestDispatcher("/authenticate/homepage");
			rd.include(request, response);

		}
		HttpSession session = request.getSession();
		String admin = (String) session.getAttribute("uname");
		// Sends the statement to the
		// database server
		int row = uploadFile(admin, inputStream);
		if (row > 0) {
			message = "File uploaded and " + "saved into database";
		}
		System.out.println(message);
		RequestDispatcher rd = request.getRequestDispatcher("/authenticate/homepage#feedpage");
		rd.forward(request, response);

	}
}
