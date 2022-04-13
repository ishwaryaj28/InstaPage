package net.insta.usermanagement.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoginFilter implements Filter {
	public void init(FilterConfig filterConfig) 
			throws ServletException {
	 
		}  
	 
	      public void doFilter(ServletRequest request, 
				ServletResponse response, FilterChain chain) 
		                throws IOException, ServletException {  
	 System.out.print("in login");
		response.setContentType("text/html"); 
	    	PrintWriter out = response.getWriter();
	 
	    	//get parameters from request object.
	    	String userName = request.getParameter("username");
	    	String password = request.getParameter("password");
	 
	    	//check for null and empty values.
	    	if(userName == null || userName.equals("") || 
	    			password == null || password.equals("")){
	    		out.print("Please enter both username " +
	    				"and password. <br/><br/>");
	    		RequestDispatcher requestDispatcher = 
	    			request.getRequestDispatcher("/page");
	    		requestDispatcher.include(request, response);
	    	}//Check for valid username and password.
	    	else{
	    		 chain.doFilter(request, response);
	    	}	          
	     }  
	 
	    public void destroy() {
	 
	    }  
	}