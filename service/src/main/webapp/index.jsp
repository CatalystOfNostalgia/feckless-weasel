<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Feckless Weasel Hello World Index Page</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%@ include file="/header.jsp" %>
        <%
          Iterable<StoredFile> recentFiles =
          SQLSource.interact(new SQLInteractionInterface<Iterable<StoredFile>>() {
              @Override
              public Iterable<StoredFile> run(Connection connection) throws ServiceException {
                  if (authSession == null) {
                      return new ArrayList<StoredFile>();
                  }
                  return authSession.getUser().lookupMostRecentFilesFromFavoriteCourses(connection);
              }
          });
 
          request.setAttribute("recentFiles", recentFiles);
        %>
        
        <div class="jumbotron" style="margin-bottom: 0px;">
            <div class="container">
                <h1>Welcome to Feckless Weasel!</h1>
                <p>Let Feckless Weasel help you ace your next test!</p>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="sidebar.jsp"/>
                <div class="container">
                    <div class="container">
                        <div class="col-md-4">
                            <h2>Can't find your university?</h2>
                            <p>That's okay! Get your university started and add your university on Feckless Weasel today.</p>
                            <p>
                                <a class="btn btn-warning" href="/university/create.jsp" role="button">Create</a>
                            </p>

                            <div class="container">
                                <% if (authSession != null) { %>
                                    <h3>New files from your classes!</h3>
                                <% } %>
                                <c:forEach var="file" items="${recentFiles}">
                                    <div class="row">
                                        <div class="col-sm-5">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <strong title="${file.getDescription()}">
                                                        <a href="/course/file.jsp?fid=${file.getID()}">${file.getTitle()}</a>
                                                    </strong>
                                                    <span class="text-muted">created on ${file.getCreationDate()}</span>
                                                </div>
                                                <div class="panel-body">
                                                    ${file.getDescription()}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
