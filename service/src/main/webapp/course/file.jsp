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
                Tuple3<StoredFile, Course, User> tuple =
                SQLSource.interact(new SQLInteractionInterface<Tuple3<StoredFile, Course, User>>() {
                    @Override
                    public Tuple3<StoredFile, Course, User> run(Connection connection) throws ServiceException {
                        final StoredFile file = StoredFile.lookup(connection,
                            OMUtil.parseInt(request.getParameter("fid")));
                        final Course course = file.lookupCourse(connection);
                        final User user = file.lookupUser(connection);

                        return new Tuple3(file, course, user);
                    }
                });

                request.setAttribute("file", tuple.value1);
                request.setAttribute("course", tuple.value2);
                request.setAttribute("user", tuple.value3);
            %>
            <jsp:include page="/header.jsp"/>
            <div class="jumbotron">
                <div class="container">
                    <h1>${file.getTitle()}</h1>
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
                    if (file.getExtension().equals("md")) {
                %>
                <a href="/editor.jsp?fid=${file.getID()}&cid=${course.getID()}">Edit</a>
                <%}%>
            </div>
        </body>
    </body>
</html>
