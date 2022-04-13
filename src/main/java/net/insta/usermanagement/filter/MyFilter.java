package net.insta.usermanagement.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.insta.management.constant.Constants;

public class MyFilter implements Filter {

	private ArrayList<String> urlList;

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String url = request.getServletPath();
		System.out.println(url);

		HttpSession session = request.getSession(false);

		boolean loggedIn = session != null && session.getAttribute(Constants.uname) != null;

		if (loggedIn) {
			System.out.println("in");
			 response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		        response.setHeader("Pragma", "no-cache");
			chain.doFilter(request, response);
		} else {
			System.out.println("null");
			response.sendRedirect("/InstaPage/page");
		}
	}

	public void init(FilterConfig config) throws ServletException {

	}

}