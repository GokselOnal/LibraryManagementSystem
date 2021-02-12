<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 15.01.2021
  Time: 18:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Book</title>
</head>
<body>
<p style = "color :green">${messageBook}</p>
<p style = "color :red">${errorBook}</p>
<form method="post">
    Title: <input type="text" name="title" /> Author name: <input type="text" name="author_name" />  Genre name: <input type="text" name="genre_name" /><br>
    Topic: <input type="text" name="topic_name" /> Publish date: <input type="text" name="publish_date" />  Page number: <input type="text" name="page_number" /><br><br>
    Publisher : <select id="browsers" name="publisher" datatype="String" >

<%
        String[][] data = (String[][]) session.getAttribute("pubNameData");
        for(String[] item : data){
%>

        <option value=<%=item[0]%>>
            <p><%=item[0]%></p>
            <%
                }
            %>
<br>
    <input type="submit" value ="Add request"/><br><br>
</form>
<a href="/manager">Back to main page</a>



