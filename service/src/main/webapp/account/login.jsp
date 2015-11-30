<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Feckless Weasel Account Login</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="/header.jsp"/>
        <div class="container">
            <form class="form-signin" action="/servlet/user_session" method="post" enctype="application/x-www-form-urlencoded">
                <h2 class="form-signin-heading">Sign in</h2>
                <input type="hidden" name="action" value="create"/>
                <label for="username" class="sr-only">Username</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
                <label for"password" class="sr-only">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="password" required>
                <button type="submit" class="btn btn-lg btn-warning btn-block" id="submit" name="submit">Login</button>
                <p>Don't have an account? Make an account <a href="/account/create.jsp">here!</a></p>
            </form>
        </div>
    </body>
</html>
