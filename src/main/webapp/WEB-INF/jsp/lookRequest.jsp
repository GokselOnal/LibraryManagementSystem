<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 7.01.2021
  Time: 23:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Requests</title>
</head>
<body>
<%
    String[][] data = (String[][]) session.getAttribute("requestData");
%>
<form method="post">
    <input type="radio" id="add" name="type" value="add" checked = "checked">
    <label for="add">Add Requests</label><br>
    <input type="radio" id="remove" name="type" value="remove">
    <label for="remove">Remove Requests</label><br>
    <input type="submit" value ="look"/>
</form>
<%
    if(data != null){
%>
<h1>Results</h1>
<h5>book Id : title : author name : genre name : publish date : page number : availability</h5>
<%
    for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %> : <%=item[2]%> : <%= item[3] %> : <%= item[4] %> : <%=item[5]%> : <%=item[6]%> </p>
<%
    }

%>
<a href="/acceptRequest">Accept Request</a><br>
<a href="/acceptRemove">Accept Remove</a><br>
<%
    }
%>
<br>
<a href="/manager">Back to main page</a>
</body>
</html>
