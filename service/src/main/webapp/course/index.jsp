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

                Tuple5<Course, Department, University, Iterable<StoredFile>, Boolean> tuple =
                SQLSource.interact(new SQLInteractionInterface<Tuple5<Course, Department, University, Iterable<StoredFile>, Boolean>>() {
                    @Override
                    public Tuple5<Course, Department, University, Iterable<StoredFile>, Boolean> run(Connection connection)
                        throws ServiceException {
                        final Course course = Course.lookupById(connection,
                            OMUtil.parseInt(request.getParameter("cid")));
                        final Department department = course.lookupDepartment(connection);
                        final University university = department.lookupUniversity(connection);
                        final Iterable<StoredFile> files = StoredFile.lookupCourseFiles(connection, course);
                        Boolean toggled = false;

                        if(authSession != null) {
                            User user = authSession.getUser();
                            toggled = user.checkIfFavCourse(connection, OMUtil.parseInt(request.getParameter("cid")));
                        }

                        return new Tuple5(course, department, university, files, toggled);
                    }
                });

                if (authSession != null) {
                    User user = authSession.getUser();
                    request.setAttribute("user", user);
                }
                request.setAttribute("course", tuple.value1);
                request.setAttribute("department", tuple.value2);
                request.setAttribute("university", tuple.value3);
                request.setAttribute("files", tuple.value4);
                
                //already need scriptlet for if statement, so not setting attribute
                Boolean toggled = tuple.value5;
            %>
            <jsp:include page="/header.jsp"/>
            <div class="jumbotron">
                <div class="container">
                    <h1>
                        ${department.getAcronym()} ${course.getCourseNum()}
                        <%if (authSession != null && toggled) {%>
                            <a href="/servlet/course?username=${user.getUsername()}&cid=${course.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart"></i></a>
                        <% } else if (authSession != null) { %>
                            <a href="/servlet/course?username=${user.getUsername()}&cid=${course.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart-empty"></i></a>
                        <% } %>
                    </h1>
                <h2>
                    <a href="/department?did=${department.getID()}">${department.getDeptName()}</a>
                    at
                    <a href="/university?uid=${university.getID()}" title="${university.getLongName()}">${university.getAcronym()}</a>
                </h2>
                </div>
            </div>
            <%if (request.getParameter("uploadSuccess") == null) {} else if (request.getParameter("uploadSuccess").equals("True")) {%>
            <div class="container">
                <div class="alert alert-success">
                    <i class="glyphicon glyphicon-ok"></i>
                    <strong>Yay!</strong> Upload was successful
                </div>
            </div>
            <% }else {%>
            <div class="container">
                <div class="alert alert-danger">
                    <i class="glyphicon glyphicon-remove"></i>
                    <strong>Oh no!</strong> Looks like there was an error uploading your file. Please try again.
                </div>
            </div>
            <% } %>
            <div class="container">
            <%if (authSession != null) {%>
                <jsp:include page="/file_uploader.jsp">
                    <jsp:param name="classID" value="${course.getID()}"/>
                </jsp:include>
            <% } else { %>
                <p>Login or create an account to contribute!</p>
            <% } %>
            </div>
           <div class="container">
              <c:forEach var="file" items="${files}">
                  <div class="row">
                      <div class="col-md-6">
                          <h2>
                              <a href="/course/file.jsp?fid=${file.getID()}" title="${file.getDescription()}">
                                  ${file.getTitle()} &#09; - &#09; ${file.getCreationDate()}
                              </a>
                          </h2>
                      </div>
                      <div class="col-md-3">
                            <!-- Take up space in the row-->
                      </div>
                      <div class="col-md-1">
                          <c:if test="${! file.getTag().isEmpty()}">
                              <h3><span class="label label-warning">
                                  ${file.getTag()}
                              </span></h3>
                          </c:if>
                      </div>
                  </div>
              </c:forEach>
            </div>
        </body>
    </body>
</html>
