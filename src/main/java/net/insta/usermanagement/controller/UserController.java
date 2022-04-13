package net.insta.usermanagement.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.insta.management.constant.*;
import net.insta.usermanagement.dao.*;
import net.insta.usermanagement.exceptions.*;
import net.insta.usermanagement.model.*;

import com.mysql.cj.protocol.Resultset;

@Controller

@MultipartConfig(maxFileSize = 16177215)
@RequestMapping(value = "/authenticate")
public class UserController {

	
	public Userdao userDAO;
	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	
	 
	@RequestMapping(value = "/logout")
	private void logoutuser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		session.invalidate();
		
		LOGGER.info("logged out ");

		response.sendRedirect(Constants.pgurl);

	}
	
	@RequestMapping(value = "/followers",produces = MediaType.APPLICATION_JSON_VALUE)
	private void followers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		userDAO = new Userdao();
		JSONObject data = new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject message = new JSONObject();
		PrintWriter out = response.getWriter();
		JSONArray followers_list = null;
		try {

			HttpSession session = request.getSession();
			String admin = (String) session.getAttribute(Constants.uname);
			int flag = Integer.parseInt(request.getParameter("id"));
			System.out.print(flag);

			if (flag == 0) {
				followers_list= userDAO.selectFollowers(admin);
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);
				message.put(Constants.message1, followers_list);
				data.put(Constants.message, message);
				LOGGER.info("successfully fetched followers list");
			} else {
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);

			}
		} catch (MyException m) {

			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status,status);
			LOGGER.error(m);

		} catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status,status);
			LOGGER.error(m);

		}

		out.println(data.toJSONString());
		out.flush();
		System.out.println(data);
	}

	@RequestMapping(value = "/request")
	private void pendinglist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		userDAO = new Userdao();
		JSONObject data= new JSONObject();
		JSONObject status= new JSONObject();
		JSONObject message= new JSONObject();
		JSONArray pendingreq_list = null;
		response.setContentType("application/json");
		try {
			System.out.println("in getrequest");
			HttpSession session = request.getSession();
			String currentuser = (String) session.getAttribute(Constants.uname);
			int flag = Integer.parseInt(request.getParameter("id"));

			if (flag == 0) {
				pendingreq_list  = userDAO.selectPendingRequest(currentuser);
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);
				message.put(Constants.message1, pendingreq_list );
				data.put(Constants.message, message);
				LOGGER.info("successfully fetched pendingrequest list");
			} else {
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);

			}

		} catch (MyException m) {
			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status, status);
			LOGGER.error(m);
		}

		catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status, status);
			LOGGER.error(m);

		}

		out.println(data.toJSONString());
		out.flush();
		System.out.print(data);

	}

	@RequestMapping(value = "/suggestion")
	private void suggestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		userDAO = new Userdao();
		JSONObject data= new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject message= new JSONObject();
		JSONArray suggestion_list= null;
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession();
			String admin = (String) session.getAttribute(Constants.uname);
			int flag = Integer.parseInt(request.getParameter("id"));

			if (flag == 0) {
				suggestion_list= userDAO.allUser(admin);
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);
				message.put(Constants.message1, suggestion_list);
				data.put(Constants.message, message);
				LOGGER.info("successfully fetched suggestion list");
			} else {
				status.put(Constants.status, Constants.success);
				status.put(Constants.code, Constants.success);
				status.put(Constants.message, Constants.success);
				data.put(Constants.status, status);

			}

		} catch (MyException m) {
			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status, status);
			LOGGER.error(m);
		} catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status,status);
			LOGGER.error(m);

		}
		out.println(data.toJSONString());
		out.flush();
		System.out.print(data);
	}

	@RequestMapping(value = "/sendrequest")
	private void requestlist(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {

		LOGGER.info("in send request");
		String requestname = request.getParameter(Constants.id);
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String currentuser = (String) session.getAttribute(Constants.uname);
		userDAO = new Userdao();
		JSONObject data = new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject message = new JSONObject();
		JSONObject requested_user= new JSONObject();
		JSONArray list1 = null;
		response.setContentType("application/json");
		try {
			userDAO.requestUser(currentuser, requestname);
status.put(Constants.status, Constants.success);
status.put(Constants.code, Constants.success);
status.put(Constants.message, Constants.success);
			data.put(Constants.status, status);
			requested_user.put("requesteduser", requestname);
			message.put(Constants.message1, requested_user);
			data.put(Constants.message, message);
			LOGGER.info("successfully sent request");

		} catch (MyException m) {
			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status, status);
			LOGGER.error(m);
		} catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status, status);
			LOGGER.error(m);

		}
		out.println(data.toJSONString());
		out.flush();
		System.out.print(data);

	}

	@RequestMapping(value = "/accept")
	private void requestaccept(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		response.setContentType("application/json");
		LOGGER.info("in accept request");
		String acceptidname = request.getParameter(Constants.id);
		userDAO = new Userdao();
		JSONObject data= new JSONObject();
		JSONObject status= new JSONObject();
		JSONObject message= new JSONObject();
		JSONObject accept_user_image= new JSONObject();
		JSONArray accept_user_list = null;
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession();
			String currentuser = (String) session.getAttribute(Constants.uname);
			accept_user_list = userDAO.acceptUser(currentuser, acceptidname);
			status.put(Constants.status, Constants.success);
			status.put(Constants.code, Constants.success);
			status.put(Constants.message, Constants.success);
data.put(Constants.status, status);
			accept_user_image.put("imagedetails",accept_user_list);
			accept_user_image.put("followers", acceptidname);
			message.put(Constants.message1,accept_user_image);
			data.put(Constants.message, message);
			LOGGER.info("successfully added user to followers list after accept");

		} catch (MyException m) {
			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status, status);
			LOGGER.error(m);
		} catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status, status);
			LOGGER.error(m);

		}
		out.println(data.toJSONString());
		out.flush();
		System.out.print(data);

	}

	@RequestMapping(value = "/decline")
	private void requestdecline(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException {

		LOGGER.info("in request decline");
		response.setContentType("application/json");
		userDAO = new Userdao();
		JSONObject data= new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject message= new JSONObject();
		JSONObject suggestion_list= new JSONObject();
		JSONArray decline_user_list = null;
		PrintWriter out = response.getWriter();
		try {
			String declineidname = request.getParameter(Constants.id);
			HttpSession session = request.getSession();
			String currentuser = (String) session.getAttribute(Constants.uname);
			userDAO.declineUser(currentuser, declineidname);
			status.put(Constants.status, Constants.success);
			status.put(Constants.code, Constants.success);
			status.put(Constants.message, Constants.success);
			data.put(Constants.status, status);
			suggestion_list.put("suggestion", declineidname);
			message.put(Constants.message1, suggestion_list);
			data.put(Constants.message, message);
			LOGGER.info("successfully declined request");

		} catch (MyException m) {
			status.put(Constants.status, m.status);
			status.put(Constants.code, m.code);
			status.put(Constants.message, m.message);
			data.put(Constants.status, status);
			LOGGER.error(m);
		} catch (Exception m) {

			status.put(Constants.status, Constants.status);
			status.put(Constants.code, Constants.apperr);
			status.put(Constants.message, Constants.apperr);
			data.put(Constants.status, status);
			LOGGER.error(m);

		}
		out.println(data.toJSONString());
		out.flush();
		System.out.print(data);

	}

	@RequestMapping(value = "/uploadimage", method = RequestMethod.POST, produces = { "application/json" })
	public @ResponseBody void insertimage(MultipartHttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MultipartFile filePart = request.getFile(Constants.myimg);
		InputStream inputStream = null;
		String message = null;
		if (filePart != null) {

			inputStream = filePart.getInputStream();
		}
		HttpSession session = request.getSession();
		String currentuser = (String) session.getAttribute(Constants.uname);
		userDAO = new Userdao();
		int row = userDAO.uploadFile(currentuser, inputStream);

		if (row > 0) {
			message = Constants.fileupload;
		}
		System.out.println(message);
		JSONArray list = new JSONArray();

	}

	@RequestMapping(value = "/post")
	public void getuserpost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("inpost");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		userDAO = new Userdao();
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONArray list1 = null;
		try {
			HttpSession session = request.getSession();
			String currentuser = (String) session.getAttribute(Constants.uname);

			list1 = userDAO.selectpost(currentuser);
			obj1.put(Constants.status, Constants.success);
			obj1.put(Constants.code, Constants.success);
			obj1.put(Constants.message, Constants.success);
			obj.put(Constants.status, obj1);
			obj2.put(Constants.message1, list1);
			obj.put(Constants.message, obj2);
			LOGGER.info("successfully fetched post images");

		} catch (MyException m) {
			obj1.put(Constants.status, m.status);
			obj1.put(Constants.code, m.code);
			obj1.put(Constants.message, m.message);
			obj.put(Constants.status, obj1);
			LOGGER.error(m);
		} catch (Exception m) {

			obj1.put(Constants.status, Constants.status);
			obj1.put(Constants.code, Constants.apperr);
			obj1.put(Constants.message, Constants.apperr);
			obj.put(Constants.status, obj1);
			LOGGER.error(m);

		}
		out.println(obj.toJSONString());
		out.flush();
		System.out.print(obj);

	}

	@RequestMapping(value = "/feed")
	public void getfeed(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("infeedpost");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		userDAO = new Userdao();
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONArray list1 = null;
		try {
			HttpSession session = request.getSession();
			String currentuser = (String) session.getAttribute(Constants.uname);

			list1 = userDAO.selectfeed(currentuser);
			obj1.put(Constants.status, Constants.success);
			obj1.put(Constants.code, Constants.success);
			obj1.put(Constants.message, Constants.success);
			obj.put(Constants.status, obj1);
			obj2.put(Constants.message1, list1);
			obj.put(Constants.message, obj2);
			LOGGER.info("successfully fetched feed post");

		} catch (MyException m) {
			obj1.put(Constants.status, m.status);
			obj1.put(Constants.code, m.code);
			obj1.put(Constants.message, m.message);
			obj.put(Constants.status, obj1);
			LOGGER.error(m);
		} catch (Exception m) {

			obj1.put(Constants.status, Constants.status);
			obj1.put(Constants.code, Constants.apperr);
			obj1.put(Constants.message, Constants.apperr);
			obj.put(Constants.status, obj1);
			LOGGER.error(m);

		}
		out.println(obj.toJSONString());
		out.flush();
		System.out.print(obj);

	}

	@RequestMapping(value = "/getfeed/{id}")
	public void getfeedPhoto(HttpServletResponse response, @PathVariable("id") int id) throws Exception {
		response.setContentType("image/jpeg");

		try {
			userDAO = new Userdao();
			byte[] bytes = userDAO.selectimage(id);

			InputStream inputStream = new ByteArrayInputStream(bytes);
			IOUtils.copy(inputStream, response.getOutputStream());

		} catch (MyException e) {
			LOGGER.error(e);

			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getimage/{id}")
	public void getPhoto(HttpServletResponse response, @PathVariable("id") int id) throws Exception {
		response.setContentType("image/jpeg");

		try {
			userDAO = new Userdao();
			byte[] bytes = userDAO.selectimage(id);

			InputStream inputStream = new ByteArrayInputStream(bytes);
			IOUtils.copy(inputStream, response.getOutputStream());

		} catch (MyException e) {
			LOGGER.error(e);

			e.printStackTrace();
		}
	}

}
