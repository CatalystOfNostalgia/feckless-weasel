<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Feckless Weasel Hello World Index Page</title>
        <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
<<<<<<< HEAD
       <jsp:include page="header.jsp"/>
       <div class="column-beta">
            <h1>Welcome to Feckless Weasel!</h1>
            <h4><a href="/university/create.jsp">Create a University</a></h4>
=======
        <jsp:include page="header.jsp"/>
        <div class="jumbotron">
            <div class="container">
                <h1>Welcome to Feckless Weasel!</h1>
                <p>Let Feckless Weasel help you ace your next test!</p>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h2>Can't find your university?</h2>
                    <p>That's okay! Get your university started and add your university on Feckless Weasel today.</p>
                    <p>
                        <a class="btn btn-warning" href="/create.jsp" role="button">Create</a>
                    </p>
                </div>
            </div>
>>>>>>> 7d399caa9d687923966efe3696b9a6cde7a48827
        </div>
    </body>
</html>
