<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" isErrorPage="true" %>
<html>
    <head>
        <title>Feckless Weasel - Bad Request</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Bad Request</h1>
            <p>
                <%= exception != null ?
                    exception.getMessage() : "Browser sent bad request." %>
            </p>
        </div>
    </body>
</html>
