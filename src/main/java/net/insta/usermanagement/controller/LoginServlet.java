package net.insta.usermanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.insta.management.constant.Constants;
import net.insta.usermanagement.dao.Userdao;
import net.insta.usermanagement.exceptions.MyException;
import net.insta.usermanagement.model.User;

@WebServlet("/login1")
public class LoginServlet extends HttpServlet {
	public Userdao userDAO;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();
		User newUser = new User(username, password);
		userDAO = new Userdao();
		response.setContentType("text/html");
		int flag = 0;
		try {
			flag = userDAO.selectUser(newUser);
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flag == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("uname", username);

			System.out.println(Constants.homepgurl);
			response.sendRedirect(Constants.homepgurl);

		} else {
			System.out.println("******");
			out.print(Constants.incorrectusername);
			RequestDispatcher rd = request.getRequestDispatcher("/page");
			rd.include(request, response);
		}
	}
}
