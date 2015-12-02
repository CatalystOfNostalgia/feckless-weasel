<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Feckless Weasel File Viewer</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/css/comment.css" rel="stylesheet">
    </head>
        <body class="four-column">
            <%@ include file="/header.jsp" %>
            <%
                final Connection connection = SQLSource.open();
                final StoredFile file = StoredFile.lookup(connection,
                OMUtil.parseInt(request.getParameter("fid")));
                final Course course = file.lookupCourse(connection);
                final User user = file.lookupUser(connection);
                boolean toggled = user.checkIfFavFile(connection, OMUtil.parseInt(request.getParameter("fid")));
                final List<Comment> comments = file.lookupComments(connection, 0, 10);

                request.setAttribute("file", file);
                request.setAttribute("course", course);
                request.setAttribute("user", user);
                request.setAttribute("comments", comments);
            %>
            <jsp:include page="/header.jsp"/>
            <div class="jumbotron">
                <div class="container">
                    <h1>
                        ${file.getTitle()}
                        <%if (authSession != null && toggled) {%>
                            <a href="/servlet/file?username=${user.getUsername()}&fid=${file.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart"></i></a>
                        <% } else if (authSession != null) { %>
                            <a href="/servlet/file?username=${user.getUsername()}&fid=${file.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart-empty"></i></a>
                        <% } %>
                    </h1>
                    <h2>
                        ${file.getDescription()}
                    </h2>
                    <p>
                        Created by <a style="color: #f0ad4e;" href="/account?user=${user.getUsername()}">${user.getUsername()}</a> on ${file.getCreationDate()}.
                        <br>
                        <a style="color: #f0ad4e;" href="/course?cid=${course.getID()}">${course.getCourseNum()}: ${course.getCourseName()}</a>
       
                        <a style="color: #f0ad4e;" href="/servlet/file/download?fid=${file.getID()}">
                            <span style="color: #f0ad4e; float: right;" class="glyphicon glyphicon-download-alt"></span>
                        </a>
                    </p>
                </div>
            </div>
            <div class="container">
                <%
                    if (file.getExtension().equals("md") && file.userCanEdit(user)) {
                %>
                <a href="/editor.jsp?fid=${file.getID()}&cid=${course.getID()}">Edit</a>
                <%}%>
            </div>
            <div class="container">
            <!-- Displaying comments -->
            <c:forEach var="comment" items="${comments}">
                <div class="row">
                    <div class="col-sm-5">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <a href="/account?user=${comment.getUser().getUsername()}"><strong>${comment.getUser().getUsername()}</strong></a>
                                <span class="text-muted">commented on ${comment.getTime()}</span>
                            </div>
                            <div class="panel-body">
                                ${comment.getText()}
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <!-- Writing comments -->
            <%if (authSession != null) {%>
            <form class="form-comment" action="/servlet/file/comment" method="post" enctype="application/x-www-form-urlencoded">
                <h2>Have something to say?</h2>
                <textarea class="form-control" rows="5" name="text" placeholder="username">What's on your mind?</textarea>
                <input type="hidden" name="fileid" value='${file.getID()}'>
                <input type="hidden" name="username" value='${user.getUsername()}'>
                <button type="submit" id="submit" class="btn btn-lg btn-warning" name="submit">Comment</button>
            </form>
            <%}else {%>
            <h2>Login to comment!</h2>
            <%}%>
            </div>
        </div>
        </body>
    </body>
    <% connection.close(); %>
</html>
