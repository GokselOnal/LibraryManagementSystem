<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 7.01.2021
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Borrow History For Manager</title>
</head>
<body>
<%
    String[][] data = (String[][]) session.getAttribute("borrow_history_user");
%>
<p style = "color :red">${errorHistory}</p>
<form method="post">
    User id : <input type="text" name="userId" /><br>
    <input type="submit" value ="look"/>
</form>
<%
    if(data != null){
        for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %> <%= item[2] %> : <%= item[3] %> </p>
<%
        }
    }
%>
<a href="/manager">Back to main page</a>
</body>
</html>
