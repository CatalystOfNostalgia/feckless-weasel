<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
        <title>Feckless Weasel Password Update</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
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

                if (emailInput.value.length > <%= User.EMAIL_MAX %>) {
                    alert("Email should be no greater than <%= User.EMAIL_MAX %> characters.")
                    return false;
                }

              return true;
            }
        </script>
    </head>
    <body class="four-column">
        <%@ include file="/header.jsp" %>
        <%-- Check user is logged in. --%>
        <%
            if (authSession == null) {
                throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
            }
        %>
        <div class="column-beta">
            <h1>Reset Password</h1>
            <form class="form-default" action="/servlet/user" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return validate()">
                <input type="hidden" name="action" value="update_password" />
                <input type="hidden" class="text-large" id="username" name="username" placeholder="username" value="<%= authSession.getUser().getUsername() %>" />
                <input type="password" class="text-large" id="password" name="password" placeholder="current password" />
                <input type="password" class="text-large" id="new-password" name="new-password" placeholder="new password" />
                <input type="password" class="text-large" id="new-password-retype" name="new-password-retype" placeholder="retype new password" />
                <div>
                    <input type="submit" class="button-large" id="submit" name="submit" value="Update Password" \>
                </div>
            </form>
        </div>
    </body>
</html>
