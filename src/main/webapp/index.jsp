<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="java.util.Base64" %>
<%@ page import="sun.misc.BASE64Encoder" %>
<% 
Blob image = null;
Connection con = null;
byte[ ] imgData = null ;
Statement stmt = null;
ResultSet rs = null;
response.setContentType("image/jpg");

JSONArray list=new JSONArray();
byte[] encodeBase64=null;
ResultSet res = null;
String sql="select postimage from postdetails where username = ?";
 
   String admin=(String)session.getAttribute("uname");
   System.out.print(admin);
 
try {

    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/instauser","root","");
    PreparedStatement preparedStatement = con.prepareStatement(sql); 
    preparedStatement.setString(1, admin);
	 res = preparedStatement.executeQuery();
    sun.misc.BASE64Encoder encoder= new sun.misc.BASE64Encoder();%>
    <table border="2">
    <tr><th>DISPLAYING IMAGE</th></tr>
    <tr><td>Image 2</td></tr>
    <tr><td>
    <%while(res.next()){
    	image = res.getBlob(1);
    	imgData = image.getBytes(1,(int)image.length());
        InputStream inputStream =image.getBinaryStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);                  
        }
         
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);%>
     <img src="data:image/jpg;base64,<%=base64Image%>" width="500" height="500"/>
     <%}%>
    </td></tr>
    </table>
    <%rs.close();
    stmt.close();
    con.close();}
    catch (Exception e) {
    out.println("DB problem"); 
    
    }
    
    
    %>
    </body>
    </html>