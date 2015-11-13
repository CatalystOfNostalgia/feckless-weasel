<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" isErrorPage="true" %>
<html>
    <head>
        <title>Feckless Weasel - Bad Request</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="jumbotron">
            <div class="container">
                <h1>Bad Request</h1>
                <p>
                    <%= exception != null ?
                    exception.getMessage() : "Browser sent bad request." %>
                </p>
            </div>
        </div>
    </body>
</html>
