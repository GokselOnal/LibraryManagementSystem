<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 02:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Publisher</title>
</head>
<body>
<p style = "color :red">${errorPub}</p>
<p style = "color :green">${messagePub}</p>
<form method="post">
    Publisher Id : <input type="text" name="publisherID" /><br>
    Password : <input type="password" name="password" required/><br>
    Publisher name : <input type="text" name="publisher_name" required/><br>

    <input type="submit" value ="Sign Up" />
</form>
<a href="/manager">Back to main page</a>
</body>
</html>
