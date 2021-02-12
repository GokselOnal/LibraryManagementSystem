<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 5.01.2021
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Books</title>
</head>
<body>
<%
    String[][] data = (String[][]) session.getAttribute("searchData");
%>
<form method="post">
    <p style = "color :gray"> You dont need to write anything in searching field for Available option</p>
    <input type="radio" id="advance" name="type" value="advance" checked = "checked">
    <label for="advance">Advance Search</label><br>
    <input type="radio" id="title" name="type" value="title">
    <label for="title">Title</label><br>
    <input type="radio" id="author" name="type" value="author">
    <label for="author">Author Name</label><br>
    <input type="radio" id="genre" name="type" value="genre">
    <label for="genre">Genre Name</label><br>
    <input type="radio" id="topic" name="type" value="topic">
    <label for="topic">Topic Name</label><br>
    <input type="radio" id="publisher" name="type" value="publisher">
    <label for="publisher">Publisher Name</label><br>
    <input type="radio" id="available" name="type" value="available">
    <label for="available">Available </label><br>
    <input type="radio" id="year" name="type" value="year">
    <label for="year">Year </label><br>
    <input type="text" name ="searchfield" value="Search for books"/><br>
    <input type="submit" value ="Search" name="button1"/> <br>
</form>
<a href="/borrowBook"> Borrow Book </a><br>
<a href="/publishersForGenre"> Publishers with given genre </a><br><br>
<a href="/regular_user"> Return main page </a>
<%
    if(data != null){
%>
<h1>Results</h1>
<h5>book Id : title : author name : genre name : publish date : page number : availability : floor number : part no</h5>
<%
    ArrayList<String> aa = new ArrayList<String>();
    aa.add("");
        for(String[] item : data){
            if(!aa.contains(item[0])){
            aa.add(item[0]);
%>
<p><%= item[0] %> : <%= item[1] %> : <%=item[2]%> : <%= item[3] %> : <%= item[4] %> : <%=item[5]%> : <%=item[6]%> : <%=item[7]%> <%=item[8]%></p>
<%
        }
    }
%>
<%
    }
%>
</body>
</html>
