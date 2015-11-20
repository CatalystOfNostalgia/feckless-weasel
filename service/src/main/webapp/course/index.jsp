<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body class="four-column">
           <%
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
        </body>
    </body>
</html>