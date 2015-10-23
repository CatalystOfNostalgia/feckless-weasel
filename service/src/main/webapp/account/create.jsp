<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Account Creation</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
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
                    alert("Username is either too short or too long.");
                    return false;
                }

                if (passwordInput.value.length < <%= User.PASS_MIN %> || passwordInput.value.length > <%= User.PASS_MAX %>) {
                    alert("Password is either too long or too short.");
                    return false;
                }

                if (emailInput.value.length > <%= User.EMAIL_MAX %>) {
                    alert("Email is too long.")
                    return false;
                }

              return true;
            }
        </script>
    </head>
    <body class="four-column">
        <jsp:include page="/header.jsp"/>
        <div class="column-beta">
            <h1>Create an account</h1>
            <h2>It's free!</h2>
            <form class="form-default" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <input type="text" class="text-large" id="username" name="username" placeholder="username">
                <input type="password" class="text-large" id="password" name="password" placeholder="password">
                <input type="password" class="text-large" id="password-retype" name="password-retype" placeholder="retype password">
                <input type="text" class="text-large" id="email" name="email" placeholder="email">
                <div>
                    <button type="submit" class="button-large" id="submit" name="submit">Create Account</button>
                </div>
            </form>
        </div>
    </body>
</html>