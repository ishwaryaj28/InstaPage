package net.insta.usermanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.insta.management.constant.Constants;
import net.insta.usermanagement.dao.Userdao;
import net.insta.usermanagement.exceptions.MyException;
import net.insta.usermanagement.model.User;
import net.insta.usermanagement.dao.Userdao;

@WebServlet("/signup1")
public class SignupServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(SignupServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Userdao userDAO;
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String firstname = request.getParameter(Constants.firstname);
		String lastname = request.getParameter(Constants.lastname);
		String emailid = request.getParameter(Constants.emailid);
		String username = request.getParameter(Constants.username);
		String password = request.getParameter(Constants.password);
		if (!(((firstname.charAt(0) >= 'a' && firstname.charAt(0) <= 'z')
				|| (firstname.charAt(0) >= 'A' && firstname.charAt(0) <= 'Z'))
				&& ((username.charAt(0) >= 'a' && username.charAt(0) <= 'z')
						|| (username.charAt(0) >= 'A' && username.charAt(0) <= 'Z') || username.charAt(0) == '_')
				&& ((emailid.contains("com") && emailid.contains("@"))
						&& (emailid.charAt(0) >= 'a' && emailid.charAt(0) <= 'z')
						|| (emailid.charAt(0) >= 'A' && emailid.charAt(0) <= 'Z') || emailid.charAt(0) == '_')
				|| ((password.charAt(0) >= 'a' && password.charAt(0) <= 'z')
						&& (password.charAt(0) >= 'A' && password.charAt(0) <= 'Z') || password.charAt(0) == '_'))) {
			out.print("input is incorrect");
			RequestDispatcher rd = request.getRequestDispatcher("/signup");
			rd.include(request, response);
		}

		else {

			try {
				User newUser = new User(firstname, lastname, emailid, username, password);
				userDAO = new Userdao();
				userDAO.insertUser(newUser);
				LOGGER.info("successfully signed up");
				out.println(Constants.reg);
				RequestDispatcher rd = request.getRequestDispatcher("/page");
				rd.include(request, response);
			} catch (MyException e) {
				LOGGER.info(e.message);
				out.print(e);
				e.printStackTrace();
			} catch (SQLException e) {
				LOGGER.info(e);
				out.print("SQL error");
				e.printStackTrace();
			}

		}

	}

}
