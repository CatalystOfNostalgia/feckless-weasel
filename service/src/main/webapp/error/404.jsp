<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" isErrorPage="true" %>
<html>
    <head>
        <title>Feckless Weasel - Page Not Found</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Not Found</h1>
            <p>
                <%= exception != null ?
                    exception.getMessage() : "Cannot find specified page." %>
            </p>
        </div>
    </body>
</html>
