<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Password Update</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet">
        <script language="javascript">
            function validate(event) {
                var passwordInput = document.getElementById('password');
                var newPasswordInput = document.getElementById('new-password');
                var newPasswordRetypeInput = document.getElementById('new-password-retype');

                if (passwordInput.value.length === 0) {
                    alert("Must type in current password to reset password.");
                    return false;
                }

                if (newPasswordInput.value.length < <%= User.PASS_MIN %> || passwordInput.value.length > <%= User.PASS_MAX %>) {
                    alert("New password must be <%= User.PASS_MIN %> and <%= User.PASS_MAX %> characters.");
                    return false;
                }

                if (!(newPasswordInput.value === newPasswordRetypeInput.value)) {
                    alert("Passwords do not match.");
                    return false;
                }

                return true;
            }
        </script>
    </head>
    <bodyx>
        <%@ include file="/header.jsp" %>
        <%-- Check user is logged in. --%>
        <%
            if (authSession == null) {
                throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
            }
        %>
        <div class="container">
            <form class="form-signin" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <h2 class="form-signin-heading">Update Password</h1>
                <input type="hidden" name="action" value="update_password" />
                <input type="hidden" id="username" name="username" value="<%= authSession.getUser().getUsername() %>" />
                <input type="password" class="form-control" id="password" name="password" placeholder="current password" />
                <input type="password" class="form-control" id="new-password" name="new-password" placeholder="new password" />
                <input type="password" class="form-control" id="new-password-retype" name="new-password-retype" placeholder="retype new password" />
                <input type="submit" class="btn btn-lg btn-warning btn-block" id="submit" name="submit" value="Update Password" \>
            </form>
        </div>
    </body>
</html>
