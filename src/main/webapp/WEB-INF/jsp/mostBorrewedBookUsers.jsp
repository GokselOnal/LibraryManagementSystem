<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 8.01.2021
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Most Borrowed Book Users</title>
</head>
<body>
<%
    String[][] data = (String[][]) session.getAttribute("users");

    if(data == null){
%>
<p>There is no any book borrowed</p>
<%
    }
    if(data != null){
        for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %></p>
<%
        }
    }
%>
<a href="/statistics">Back to statistics page</a>
</body>
</html>
