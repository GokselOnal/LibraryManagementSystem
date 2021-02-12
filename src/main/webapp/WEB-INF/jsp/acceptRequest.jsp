<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Accept Request</title>
</head>
<body>
<p style = "color :green">${messageAccept}</p>
<p style = "color :red">${errorAccept}</p>
<form method="post">
    Book id : <input type="text" name="bookId" /><br>
    <input type="submit" value ="Accept"/>
</form>
<a href="/return5">Back to Requests page</a>
</body>
</html>
