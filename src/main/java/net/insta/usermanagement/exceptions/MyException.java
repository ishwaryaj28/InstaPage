package net.insta.usermanagement.exceptions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyException extends Exception {

	public String status;
	public String code;
	public String message;

	public MyException(String status, String code, String message) {
		super(message);
		System.out.print(message);
		this.status = status;
		this.code = code;
		this.message = message;

	}

}