<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Feckless Weasel Account Creation</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Create an account</h1>
            <h2>It's free!</h2>
            <form class="form-default" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded">
                <input type="text" class="text-large" id="username" name="username" placeholder="username">
                <input type="password" class="text-large" id="password" name="password" placeholder="password">
                <input type="password" class="text-large" id="password" name="retype password" placeholder="retype password">
                <input type="text" class="text-large" id="email" name="email" placeholder="email">
                <div>
                    <button type="submit" class="button-large" id="submit" name="submit">Create Account</button>
                </div>
            </form>
        </div>
    </body>
</html>
