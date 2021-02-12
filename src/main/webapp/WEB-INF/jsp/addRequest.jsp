<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 5.01.2021
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Request</title>
</head>
<body>
<h3>Request to add</h3>
<p style = "color :green">${messageRequest}</p>
<p style = "color :red">${errorRequest}</p>
<form method="post">
    Title: <input type="text" name="title" /> Author name: <input type="text" name="author_name" />  Genre name: <input type="text" name="genre_name" /> <br><br>
    Topic: <input type="text" name="topic_name" />  Publish date: <input type="text" name="publish_date" />  Page number: <input type="text" name="page_number" /><br><br>
    <input type="submit" value ="Add request"/><br><br>
</form>
<a href="/publisher"> Go back to the Main Screen</a>
</body>
</html>
