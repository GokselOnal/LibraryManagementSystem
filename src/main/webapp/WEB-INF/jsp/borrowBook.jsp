<%--
  Created by IntelliJ IDEA.
  User: MSI
  Date: 15.01.2021
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Borrow Book</title>
</head>
<body>
<form method="post" name="form2">
    <p style = "color :green">${Messageborrow1}</p>
    <p style = "color :#ff6f00">${errorMessageborrow2}</p>
    <p style = "color :red">${errorMessageborrow3}</p>
    Enter book id <input type="text" name ="borrowField" value="Borrow"/>
    <input type="submit" value ="Borrow" name="button2"/>
</form>
<a href="/searchBooks">Return to the searching page</a>
</body>
</html>
