<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Feckless Weasel File Viewer</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
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
                        Created ${file.getCreationDate()}
                        By <a href="/account?user=${user.getUsername()}">${user.getUsername()}</a>.
                        Belongs to
                        <a href="/course?cid=${course.getID()}">${course.getCourseNum()}</a>
                        course.
                    </h2>
                </div>
            </div>
            <div class="container">
                <p>
                    ${file.getDescription()}
                </p>
                <a href="/servlet/file/download?fid=${file.getID()}">Download</a>
                <p>
                    TODO: Display the actual file here :D Still need the file download servlet first.
                </p>
                <%
                    if (file.getExtension().equals("md") && file.userCanEdit(user)) {
                %>
                <a href="/editor.jsp?fid=${file.getID()}&cid=${course.getID()}">Edit</a>
                <%}%>
            </div>
            <!-- Writing comments -->
            <div class="container">
            <form class="form-comment" action="/servlet/file/comment" method="post" enctype="application/x-www-form-urlencoded">
                <h2>Add comment</h2>
                <input for="text" name="text" placeholder="username">
                <input type="hidden" name="fileid" value='${file.getID()}'>
                <input type="hidden" name="username" value='${user.getUsername()}'>
                <button type="submit" id="submit" name="submit">Comment</button>
            </form>
            <div>
            <c:forEach var="comment" items="${comments}">
                <p>${comment.getText()}</p>
            </c:forEach>
            </div>
        </div>
        </body>
    </body>
    <% connection.close(); %>
</html>
