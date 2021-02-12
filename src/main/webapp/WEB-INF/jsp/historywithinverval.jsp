<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 17.01.2021
  Time: 01:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Time Interval History</title>
</head>
<body>
<%
    LocalDate date_now = LocalDate.now();
    String[][] data = (String[][]) session.getAttribute("intervalData");
%>
<form method="post">
    <label for="start">Start date:</label>
    <input type="date" id="start" name="trip_start"
           value="<%=date_now%>"
           min="2021-01-01" max="<%=date_now%>">

    <label for="finish">Finish date:</label>
    <input type="date" id="finish" name="trip_finish"
           value="<%=date_now%>"
           min="2021-01-01" max="<%=date_now%>">
    <input type="submit" value ="Search"/>
</form>
<%
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
