<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 30.12.2020
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Regular user Page</title>
</head>
<body>
<%
    String systemUsername = (String) session.getAttribute("systemUsername");
    String bookId = (String) session.getAttribute("borrowField");
    int count2 = (int) session.getAttribute("count_borrow");
    int count3 = (int) session.getAttribute("count_overdue");
    String genre_fav = (String) session.getAttribute("genre_fav");
%>
<h1>Hello <%=systemUsername%> </h1>
<p> Borrow count: <%=count2 %> </p>
<p> Favorite genre:  <%=genre_fav %> </p>
<p> Overdue count:  <%=count3%> </p>
<a href ="/searchBooks"> Search for a book </a><br>
<a href ="/borrowHistory"> Borrow History</a><br><br>
<a href ="/login"> Log out</a>
</body>
</html>