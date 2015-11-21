<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body class="four-column">
            <jsp:include page="/header.jsp"/>
            <%
                final UserSession authSession = UserSessionUtil.resumeSession(request);
                Tuple<Course, Department> tuple =
                SQLSource.interact(new SQLInteractionInterface<Tuple<Course, Department>>() {
                @Override
                    public Tuple<Course, Department> run(Connection connection) throws ServiceException {
                        final Course course = Course.lookupById(connection, OMUtil.parseInt(request.getParameter("cid")));
                        final Department department = course.lookupDepartment(connection);

                        return new Tuple(course, department);
                    }
                });
                request.setAttribute("course", tuple.value1);
                request.setAttribute("department", tuple.value2);
            %>
            <jsp:include page="/header.jsp"/>
            <div class="jumbotron">
                <div class="container">
                    <h1>${department.getDeptName()}</h1>
                    <h2>${course.getCourseNum()}</h2>
                </div>
            </div>
            <div class="container">
            <%if (authSession != null) {%>    
                <jsp:include page="/file_uploader.jsp"> 
                    <jsp:param name="classID" value="${course.getID()}"/>
                </jsp:include>
            <% } else { %>
                <p>Login or create an account to contribute!</p>  
            <% } %>
            <%if (request.getParameter("uploadSuccess") == null) {} else if (request.getParameter("uploadSuccess").equals("True")) {%>
                <p>Upload Successful</p>
            <% }else {%>
                </p>Unknown error occured in upload </p>
            <% } %>
            </div>
        </body>
    </body>
</html>
