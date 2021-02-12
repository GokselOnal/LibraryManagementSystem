<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 17.01.2021
  Time: 02:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Publisher For Genre</title>
</head>
<body>
<%
    ArrayList<String> data = (ArrayList<String>) session.getAttribute("publishersData");
%>
<form method="post">
    Genre name: <input type="text" name="genre_field" /><br>
    <input type="submit" value ="Search"/>
</form>
<%
    if(data != null){
        for(String item : data){
%>
<p><%= item %></p>
<%
    }
    }
%>
<a href="/searchBooks">Return to the searching page</a>
</body>
</html>
