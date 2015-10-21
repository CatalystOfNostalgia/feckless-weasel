<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Feckless Weasel Account Login</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Login to Feckless Weasel</h1>
            <form class="form-default" action="/servlet/user_session" method="post" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="action" value="create"/>
                <input type="text" class="text-large" id="username" name="username" placeholder="username">
                <input type="password" class="text-large" id="password" name="password" placeholder="password">
                <div>
                    <button type="submit" class="button-large" id="submit" name="submit">Login</button>
                </div>
            </form>
        </div>
    </body>
</html>
