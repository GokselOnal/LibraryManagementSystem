<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 5.01.2021
  Time: 12:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Remove Request</title>
</head>
<body>
<%
    String title = (String) session.getAttribute("title");
    String author_name = (String) session.getAttribute("author_name");
    if(title == null || author_name == null){
%>
<h3>Request to delete</h3>
<p style = "color :green">${messageRemReq}</p>
<p style = "color :red">${errorRemReq}</p>
<form method="post">
    Title: <input type="text" name="title" />  Author name: <input type="text" name="author_name" />
    <input type="submit" value ="Delete request"/>
</form>
<a href="/publisher"> Go back to the Main Screen</a>
<%
    }
%>
</body>
</html>
