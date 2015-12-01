<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<html>
    <head>
        <title>Feckless Weasel File Viewer</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body class="four-column">
            <jsp:include page="/header.jsp"/>
            <%
                final UserSession authSession = UserSessionUtil.resumeSession(request);
                Tuple4<StoredFile, Course, User, Boolean> tuple =
                SQLSource.interact(new SQLInteractionInterface<Tuple4<StoredFile, Course, User, Boolean>>() {
                    @Override
                    public Tuple4<StoredFile, Course, User, Boolean> run(Connection connection) throws ServiceException {
                        final StoredFile file = StoredFile.lookup(connection,
                            OMUtil.parseInt(request.getParameter("fid")));
                        final Course course = file.lookupCourse(connection);
                        final User user = file.lookupUser(connection);
                        Boolean toggled = false;

                        toggled = user.checkIfFavFile(connection, OMUtil.parseInt(request.getParameter("fid")));

                        return new Tuple4(file, course, user, toggled);
                    }
                });

                request.setAttribute("file", tuple.value1);
                request.setAttribute("course", tuple.value2);
                request.setAttribute("user", tuple.value3);
                Boolean toggled = tuple.value4;
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
                    StoredFile file = (StoredFile) request.getAttribute("file");
                    User user = (User) request.getAttribute("user");
                    if (file.getExtension().equals("md") && file.userCanEdit(user)) {
                %>
                <a href="/editor.jsp?fid=${file.getID()}&cid=${course.getID()}">Edit</a>
                <%}%>
            </div>
        </body>
    </body>
</html>
