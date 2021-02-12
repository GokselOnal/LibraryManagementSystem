<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 8.01.2021
  Time: 00:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Borrow Count</title>
</head>
<body>
<%
    String borrowCount = (String) session.getAttribute("borrowCount");
    if(borrowCount == null){
%>

<form method="post">
    Book id : <input type="text" name="bookId2" /><br>
    <input type="submit" value ="look"/>
</form>
<%
    }
    else {
        %>
<p style = "color :red">${errorCount}</p>
<form method="post">
    Book id : <input type="text" name="bookId2" /><br>
    <input type="submit" value ="look"/>
</form>
<p>Borrow Count: <%=borrowCount%></p>
<%
    }
%>
<a href="/manager">Back to main page</a>
</body>
</html>
