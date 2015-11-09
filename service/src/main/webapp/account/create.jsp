<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Account Creation</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet">
        <script language="javascript">
            function validate(event) {
                var usernameInput = document.getElementById('username');
                var emailInput = document.getElementById('email');
                var passwordInput = document.getElementById('password');
                var passwordRetypeInput = document.getElementById('password-retype');

                if (!(passwordInput.value === passwordRetypeInput.value)) {
                    alert("Passwords do not match.");
                    return false;
                }

                if (usernameInput.value.length < <%= User.USER_MIN %> || usernameInput.value.length > <%= User.USER_MAX %>) {
                    alert("Username must be between <%= User.USER_MIN %> and <%= User.USER_MAX %> characters.");
                    return false;
                }

                if (passwordInput.value.length < <%= User.PASS_MIN %> || passwordInput.value.length > <%= User.PASS_MAX %>) {
                    alert("Password must be <%= User.PASS_MIN %> and <%= User.PASS_MAX %> characters.");
                    return false;
                }

                if (emailInput.value.length > <%= User.EMAIL_MAX %>) {
                    alert("Email should be no greater than <%= User.EMAIL_MAX %> characters.")
                    return false;
                }

              return true;
            }
        </script>
    </head>
    <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="container">
            <form class="form-signin" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <h2 class="form-signin-heading">Create an account!</h2>
                <h2 class="form-signin-heading">It's free</h2>
                <label for="username" class="sr-only">Username</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="username">
                <label for="email" class="sr-only">Email</label>
                <input type="text" class="form-control" id="email" name="email" placeholder="email">
                <label for="password" class="sr-only">Password</label>
                <input type="password" style="margin-bottom: 0px;" class="form-control" id="password" name="password" placeholder="password">
                <label for="password-retype" class="sr-only">Retype-password</label>
                <input type="password" class="form-control" id="password-retype" name="password-retype" placeholder="retype password">
                <button type="submit" class="btn btn-lg btn-warning btn-block" id="submit" name"submit">Create</button>
                <p>Already have an account? Login <a href="/account/login.jsp">here</a></p>
            </form>
        </div>
    </body>
</html>
