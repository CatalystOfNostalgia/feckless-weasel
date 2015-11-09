<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="com.fecklessweasel.service.objectmodel.UserSession,com.fecklessweasel.service.UserSessionUtil" %>

<nav class="navbar navbar-fixed-top navbar-inverse">
    <div class="container">
        <div class="navbar-header">    
            <a href="#" class="pull-left">
                <img style="max-width:50px; margin-top: -7px" src="${pageContext.request.contextPath}/assets/img/logo.png">
            </a>
        </div>  
        <div id="navbar" class="navbar-collapse collapse">
        <form class="navbar-form navbar-nav">
            <div class="form-group">
                <input type="text" class="form-control" id="college" name="college" placeholder="Universty">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="class" name="class" placeholder="Class">
            </div>
            <button type="submit" class="btn btn-warning" id="search-submit" name="search-submit">Go</button>
        </form>
        <ul class="nav navbar-nav navbar-right">
            <%-- Instantiate the session if there is one. You can use this in pages that include this. --%>
            <%
                UserSession authSession = UserSessionUtil.resumeSession(request);
             %>
            <%-- Show signup and login or logout button --%>
            <%= authSession != null ? String.format(
                "<form class='form-default' action='/servlet/user_session' method='post'>" +
                "<input type='hidden' name='action' value='delete'/><input type='submit' " +
                "class='button-large' id='submit' name='submit' value='Logout %s'/></form>",
                    authSession.getUser().getUsername()) :
                "<li><a href='/account/create.jsp'>Sign up</a></li>" +
                "<li><a href='/account/login.jsp'>Login</a></li>"
                %>
        </ul>
        </div>
    </div>
</nav>
