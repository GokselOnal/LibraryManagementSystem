<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Accept Remove</title>
</head>
<body>
<p style = "color :green">${messageAcceptRem}</p>
<p style = "color :red">${errorAcceptRem}</p>
<form method="post">
    Book id : <input type="text" name="bookId" /><br>
    <input type="submit" value ="Accept"/>
</form>
<a href="/return6">Back to Requests page</a>
</body>
</html>
