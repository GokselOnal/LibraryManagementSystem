<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 16.01.2021
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Overdue List</title>
</head>
<body>
<p style = "color :red">${overdueMessage}</p>
<%
    String[][] data = (String[][]) session.getAttribute("overdueData");

    if(data != null){
        for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %></p>
<%
        }
    }
%>
<a href="/manager">Back to main page</a>
</body>
</html>
