<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 30.12.2020
  Time: 22:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Publisher Page</title>
</head>
<body>
<%
    String sisusername = (String) session.getAttribute("systemUsername");
    int count = (int) session.getAttribute("count");
%>
<h1>Hello <%=sisusername%></h1>
<form method="post">
   <p> Borrowed book count:<%=count%></p>
    <input type="submit" value ="View all books"/>
</form>
<a href="/addRequest"> Add Request </a><br>
<a href="/removeRequest"> Remove Request </a><br><br>
<a href="/login"> Log out </a>
</body>
</html>
