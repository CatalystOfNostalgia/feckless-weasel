<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<html>
    <head>
        <title>Feckless Weasel - Handled Exception</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Unable to Complete Action</h1>
            <p>
                <%= exception != null ?
                    exception.getMessage() : "No exception." %>
            </p>
        </div>
    </body>
</html>
