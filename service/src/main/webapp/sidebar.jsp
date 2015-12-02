<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    final UserSession authSession = UserSessionUtil.resumeSession(request);
    if (authSession != null) {
        final User user = authSession.getUser();
        request.setAttribute("user", user);
    
        Tuple3<Iterable<Course>, Iterable<StoredFile>, Iterable<StoredFile>> tuple = 
        SQLSource.interact(new SQLInteractionInterface<Tuple3<Iterable<Course>, Iterable<StoredFile>, Iterable<StoredFile>>> (){
            @Override
            public Tuple3<Iterable<Course>, Iterable<StoredFile>, Iterable<StoredFile>> run(Connection connection)
                throws ServiceException {
                Iterable<Course> courses = user.getFavoriteCourses(connection);
                Iterable<StoredFile> favoriteFiles = user.getFavoriteFiles(connection);
                Iterable<StoredFile> notes = user.getNotes(connection); 
                return new Tuple3(courses, favoriteFiles, notes);
            }
            });

            request.setAttribute("sideCourses", tuple.value1);
            request.setAttribute("sideFiles", tuple.value2);
            request.setAttribute("notes", tuple.value3);
    }        
%>

<div class="col-sm-3 col-md-2 sidebar" style="padding-bottom: 100%; background-color: #FFD79F;">
    <%if (authSession != null) {%>
        <ul class="nav nav-sidebar">
            <h2>My Favorite Classes</h2>
            <c:forEach var="course" items="${sideCourses}">
                <li>
                    <a style="color: #7F5926;" href="/course/index.jsp?cid=${course.getID()}">${course.getCourseName()}</a>
                </li>
            </c:forEach>
        </ul>
        <ul class="nav nav-sidebar">
            <h2>My Favorite Files</h2>
            <c:forEach var="file" items="${sideFiles}">
                <li>
                    <a style="color: #7F5926;" href="/course/file.jsp?fid=${file.getID()}">${file.getTitle()}</a>
                </li>
            </c:forEach>

        </ul>
        <ul class="nav nav-sidebar">
            <h2>My Notes</h2>
            <c:forEach var="note" items="${notes}">
                <li>
                    <a style="color: #7F5926;" href="/editor.jsp?fid=${note.getID()}">${note.getTitle()}</a>
                </li>
            </c:forEach>
        </ul>
    <%}%>
</div>
