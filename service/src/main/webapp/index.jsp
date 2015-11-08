<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Feckless Weasel Hello World Index Page</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
       <jsp:include page="header.jsp"/>
       <div class="column-beta">
            <h1>Welcome to Feckless Weasel!</h1>
            <h4><a href="/university/create.jsp">Create a University</a></h4>
        </div>
    </body>
</html>
