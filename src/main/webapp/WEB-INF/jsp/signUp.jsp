<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 30.12.2020
  Time: 20:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
<p style = "color :red">${errorMessage2}</p>
<p style = "color :red">${errorMessage3}</p>
<p style = "color :green">${messageSignup}</p>
<form method="post">
    UserId : <input type="text" name="userId" /><br>
    Password : <input type="password" name="password" required/><br>
    Username : <input type="text" name="username" required/><br>
    <input type="submit" value ="Sign Up" />
</form>
<a href="/login"> Back to login page</a>
</body>
</html>
