<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 15.01.2021
  Time: 18:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Remove Book</title>
</head>
<body>
<p style = "color :green">${messageRemBook}</p>
<p style = "color :red">${errorRem}</p>
<form method="post">
    Book id : <input type="removefield" name="removefield" /><br>
    <input type="submit" value ="remove"/>
</form>
<a href="/manager">Back to main page</a>
</body>
</html>
