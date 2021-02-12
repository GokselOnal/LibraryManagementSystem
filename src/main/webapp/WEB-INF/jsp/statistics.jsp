<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 8.01.2021
  Time: 01:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Statistics</title>
</head>
<body>
<%
    String mbg = (String) session.getAttribute("mostBorGen");
    String mb3 = (String) session.getAttribute("mostBookName3");
    String mbp = (String) session.getAttribute("mostBorPub");
    String title = (String) session.getAttribute("title");
    int overdueCount = (int) session.getAttribute("overdueCount");
    String overdueCountForGiven = (String) session.getAttribute("overdueCountForGiven");
%>
<h3>Most Borrowed Genre name: <%=mbg%></h3>
<h3>Most Borrowed Book name in last 3 mount: <%=mb3%></h3>
<h3>Most Borrowed Publisher name: <%=mbp%></h3>
<h3>Overdue count at the moment: <%=overdueCount%></h3>
<a href="/mostBorrewedBookUsers">Most borrowed book's borrowers</a><br>
<a href="/overdueForTitle">Overdue count For a Book</a><br>
<a href="/manager">Back to main page</a>
</body>
</html>
