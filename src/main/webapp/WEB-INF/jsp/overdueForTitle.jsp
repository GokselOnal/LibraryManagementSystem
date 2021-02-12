<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 17.01.2021
  Time: 04:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Overdue For Title</title>
</head>
<body>
<%
    String c = (String) session.getAttribute("overdue_count_title");
%>
<form method="post">
    Title : <input type="text" name="title" /><br>
    <input type="submit" value ="Search"/>
</form>
<%
    if(c != null){
%>
<p>overdue count <%=c%></p>
<%
    }
%>
<a href="/statistics">Back to statistics page</a>
</body>
</html>
