<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
        <body class="four-column">
            <jsp:include page="/header.jsp"/>
            <%
                final UserSession authSession = UserSessionUtil.resumeSession(request);
                Tuple4<Course, Department, University, Iterable<StoredFile>> tuple =
                SQLSource.interact(new SQLInteractionInterface<Tuple4<Course, Department, University, Iterable<StoredFile>>>() {
                    @Override
                    public Tuple4<Course, Department, University, Iterable<StoredFile>> run(Connection connection)
                        throws ServiceException {
                        final Course course = Course.lookupById(connection,
                            OMUtil.parseInt(request.getParameter("cid")));
                        final Department department = course.lookupDepartment(connection);
                        final University university = department.lookupUniversity(connection);
                        final Iterable<StoredFile> files = StoredFile.lookupCourseFiles(connection, course);

                        return new Tuple4(course, department, university, files);
                    }
                });

                request.setAttribute("course", tuple.value1);
                request.setAttribute("department", tuple.value2);
                request.setAttribute("university", tuple.value3);
                request.setAttribute("files", tuple.value4);
            %>
            <jsp:include page="/header.jsp"/>
            <div class="jumbotron">
                <div class="container">
                    <h1>${department.getAcronym()} ${course.getCourseNum()}</h1>
                    <h2>
                        <a href="/department?did=${department.getID()}">${department.getDeptName()}</a>
                        at
                        <a href="/university?uid=${university.getID()}" title="${university.getLongName()}">${university.getAcronym()}</a>
                    </h2>
                </div>
            </div>
            <div class="container">
              <h2>Course Files:</h2>
              <% if (authSession != null) { %>
                  <h3>Drag and drop anywhere to upload a new PDF or image</h3>
              <% } %>
              <c:forEach var="file" items="${files}">
                  <div class="row">
                      <div class="col-md-6">
                          <h2>
                              <a href="/course/file.jsp?fid=${file.getID()}" title="${file.getDescription()}">
                                  ${file.getTitle()} &#09; - &#09; ${file.getCreationDate()}
                              </a>
                          </h2>
                      </div>
                  </div>
              </c:forEach>
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
                <p style="color: #00FF00;">Upload Successful</p>
            <% }else {%>
                </p style="color: #00FF00;">Unknown error occured in upload </p>
            <% } %>
            </div>
        </body>
    </body>
</html>
