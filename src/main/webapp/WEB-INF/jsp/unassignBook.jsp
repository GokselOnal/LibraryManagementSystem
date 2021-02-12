<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Unassign Book</title>
</head>
<body>
<p style = "color :green">${messageUnassign}</p>
<p style = "color :red">${errorUnassign}</p>
<form method="post">
    User id : <input type="text" name="userId" /><br>
    Book id : <input type="text" name="bookId" /><br>
    <input type="submit" value ="Assign"/>
</form>
<a href="/manager">Back to main page</a>
</body>
</html>
