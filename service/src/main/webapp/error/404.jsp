<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" isErrorPage="true" %>
<html>
    <head>
        <title>Feckless Weasel - Page Not Found</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="jumbotron">
            <div class="container">
                <h1>Not Found</h1>
                <p>
                    <%= exception != null ?
                        exception.getMessage() : "Cannot find specified page." %>
                </p>
            </div>
        </div>
    </body>
</html>
