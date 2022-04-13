package net.insta.usermanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.BasicConfigurator;

import net.insta.management.constant.Constants;
import net.insta.usermanagement.dao.Userdao;
@WebServlet("/logout1")
public class LogoutServlet 
	extends HttpServlet{
		public Userdao userDAO;
		protected void doPost(
			        HttpServletRequest request,
			        HttpServletResponse response)
			        throws ServletException, IOException
			    {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession();
			session.invalidate();
			out.print("Successfully logged out !! please login to continue further");
			RequestDispatcher rd = request.getRequestDispatcher("/page");
			rd.include(request, response);
			    }
			    

}
