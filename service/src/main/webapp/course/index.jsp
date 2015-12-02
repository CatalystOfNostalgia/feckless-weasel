<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Course</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script language="javascript">
            function filterTags(tagStub) {
                console.log(tagStub);
                if (tagStub == "noFilter") {
                    var allRows = $(".hTag");
                    for (var i=0; i<allRows.length; i++) {
                        $(allRows[i]).show();
                    }
                    return;
                }
                var rows = $(".hTag:not(."+tagStub+")");
                //hide all things with tag 
                for (var i=0; i<rows.length; i++){
                    $(rows[i]).hide();
                }

                rows = $("."+tagStub);
                for (var i=0; i<rows.length; i++){
                    $(rows[i]).show();
                }
            }

            $( document ).ready(function () {
                console.log("here");
                var ele = document.querySelectorAll("#tagFilter");
                console.log(ele);
                for(var i=0; i<ele.length; i++){
                    console.log("iterating");
                    ele[i].addEventListener("click", function(){
                        filterTags(this.value);
                    });
                }
            });
        </script>        
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
            <div class="jumbotron" style="margin-bottom: 0px;">
                <div class="container">
                    <h1>
                        ${department.getAcronym()} ${course.getCourseNum()}: ${course.getCourseName()}
                        <%if (authSession != null && toggled) {%>
                            <a class="color: #f0ad4e;" href="/servlet/course?cid=${course.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart"></i></a>
                        <% } else if (authSession != null) { %>
                            <a class="color: #f0ad4e;" href="/servlet/course?cid=${course.getID()}"><i style="float: right; color: #f0ad4e;" class="glyphicon glyphicon-heart-empty"></i></a>
                        <% } %>
                    </h1>
                <h2>
                    <a style="color:#f0ad4e;" href="/department?did=${department.getID()}">${department.getDeptName()}</a>
                    at
                    <a style="color:#f0ad4e;" href="/university?uid=${university.getID()}" title="${university.getLongName()}">${university.getAcronym()}</a>
                </h2>
                </div>
            </div>
            <div class="container-fluid row">
                <jsp:include page="/sidebar.jsp"/>
                <div class="col-md-9">
                    <center><div class="btn-group" role="group" aria-label="Tag yo doc">
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="notes">Notes</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="assignmentAnswers">Assignment Answers</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="assignment">Assignment</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="quiz">Quiz</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="quizAnswers">Quiz Answers</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="test">Test</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="testAnswers">Test Answers</button>
                            <button type="button" id="tagFilter" class="btn btn-lg btn-warning" value="noFilter">No Filter</button>

                    </div></center>
                </div>
                <div class="col-md-6">
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
                </div>
                <div class="col-md-6">
                    <div class="container">
                    <%if (authSession != null) {%>
                        <jsp:include page="/file_uploader.jsp">
                            <jsp:param name="classID" value="${course.getID()}"/>
                        </jsp:include>
                    <% } else { %>
                        <h1>Login or create an account to contribute!</h1>
                    <% } %>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="container">
                        <c:forEach var="file" items="${files}">
                            <h2 class="hTag ${file.getTag()}">
                                <a style="color: #f0ad4e;" href="/course/file.jsp?fid=${file.getID()}" title="${file.getDescription()}">
                                    ${file.getTitle()} &#09; - &#09; ${file.getCreationDate()}
                                </a>
                                <c:if test="${! file.getTag().isEmpty()}">
                                    <span style="float: right;" class="label label-warning">
                                        ${file.getTag()}
                                    </span>
                                </c:if>
                            </h2>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </body>
    </body>
</html>
