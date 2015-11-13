<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Feckless Weasel - Server Error</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="jumbotron">
            <div class="container">
                <h1>Server Error</h1>
                <p>
                    An internal server error was encountered. If the problem persists, please contact us.
                </p>
            </div>
        </div>
    </body>
</html>
