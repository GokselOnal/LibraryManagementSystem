<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 2.01.2021
  Time: 21:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Books</title>
</head>
<body>
<a href="/publisher"> Return to Publisher page </a>
<h5>book Id : title : author name : genre name : publish date : page number : availability : floor number : part no</h5>
<%
    String[][] data = (String[][]) session.getAttribute("allBookData");

    if(data != null){
        for(String[] item : data){
%>
<p><%= item[0] %> : <%= item[1] %> : <%=item[2]%> : <%= item[3] %> : <%= item[4] %> : <%=item[5]%> : <%=item[6]%> : <%=item[7]%> : <%=item[8]%> </p>
<%
        }
    }
%>
</body>
</html>
