<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 2.01.2021
  Time: 00:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Borrow History</title>
</head>
<body>
<%
    String[][] data = (String[][]) session.getAttribute("borrowHistoryData");
    if(data.length == 0){
%>
<br>
<p>Empty History</p>
<%
    }
    if(data.length != 0){
%>
<h1>Borrow History</h1>
<h5>book Id : title : author name : genre name : publish date : return date: page number : availability : floor number : part no</h5>
<%
        for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %> : <%=item[2]%> : <%= item[3] %> : <%= item[4] %> : <%=item[5]%> : <%=item[6]%> : <%=item[7]%> : <%=item[8]%> : <%=item[9]%></p>
<%
        }
    }
%>
<br>
<h3>Return Book</h3>
<form method="post">
    Book id: <input type="text" name="returnbook"/>
    <input type="submit" value ="return">
</form>
<a href="/regular_user">Return to the user page</a>
</body>
</html>