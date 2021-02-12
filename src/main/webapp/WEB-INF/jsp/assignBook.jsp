<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 12:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Assign Book</title>
</head>
<body>
<p style = "color :green">${messageAssign}</p>
<p style = "color :red">${errorAssign}</p>
<form method="post">
    User id : <input type="text" name="userId" /><br>
    Book id : <input type="text" name="bookId" /><br>
    <input type="submit" value ="Assign"/>
</form>
<a href="/manager">Back to main page</a>
</body>
</html>
