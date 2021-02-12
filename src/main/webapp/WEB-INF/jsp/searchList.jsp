<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 31.12.2020
  Time: 23:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Result</title>
</head>
<body>
<%
    String[][] searchdata = (String[][]) session.getAttribute("searchData");
    if(searchdata != null){
        for(String[] item : searchdata){
%>
<p><%= item[0] %> : <%= item[1] %> : <%=item[2]%> : <%= item[3] %> : <%= item[4] %> : <%=item[5]%> : <%=item[6]%></p>
<%
        }
    }
%>
</body>
</html>
