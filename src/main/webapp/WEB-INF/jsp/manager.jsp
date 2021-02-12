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
    <title>Manager Page</title>
</head>
<body>
<%
    String sisusername = (String) session.getAttribute("systemUsername");
%>
<form method="post">
    <h1>Hello <%=sisusername%></h1>
</form>
<a href="/lookRequest"> Requests</a><br>
<a href="/borrowHistoryForManager">User history</a><br>
<a href="/borrowCount"> Borrow Count </a><br>
<a href="/historywithinverval">Borrow History with Time inverval</a><br>
<a href="/statistics"> Statistics </a><br>
<a href="/addPublisher"> Add new Publisher </a><br>
<a href="/addBook">Add Book</a><br>
<a href="/removeBook"> Remove Book</a><br>
<a href="/assignBook"> Assign Book</a><br>
<a href="/unassignBook">Unassign Book</a><br>
<a href="/overdueList"> Overdue List</a><br><br>
<a href="/login"> Log out</a><br>
</body>
</html>
