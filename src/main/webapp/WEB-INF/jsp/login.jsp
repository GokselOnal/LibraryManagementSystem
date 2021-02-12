<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 19.12.2020
  Time: 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login to System</title>
</head>
<body>
<%
    String username = (String) session.getAttribute("username");
    if(username == null){

%>
<p style = "color :red">${errorMessage}</p>
<form method="post">
    Username : <input type="text" name="username" /><br>
    Password : <input type="password" name="password" /><br>
    <input type="submit" value ="Login"/>
</form>
<a href="/signUp">Sign Up</a>
<%
    }
%>
</body>
</html>
