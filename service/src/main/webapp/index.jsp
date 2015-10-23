<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Feckless Weasel Hello World Index Page</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body>
       <jsp:include page="header.jsp"/>
       <div class="column-beta">
            <h1>Welcome to Feckless Weasel!</h1>
            <p>
                Hello World! Today is <%= new java.util.Date() %> 
            </p>
            <h2> Create Your Class Page</h2>
            <p>     
            <br>    <input type="text" class="text-large"  id="username" name="username" placeholder="Enter your Username"> </br>
            <br>    <input type="text" class="text-large"  id="password" name="password" placeholder="Enter your password" > </br>
            <br>    <input type="text" class="text-large" id="college" name="college" placeholder="Enter your university"> </br>
            <br>    <input type="text" class="text-large" id="class" name="class" placeholder="Enter your class (i.e. EECS-390)"></br>
            <br>    <button type="submit" class="button-small" id="search-submit" name="search-submit" onclick="classroom.jsp">Submit</button>
            <!-- call javascript function in classroom page here-->
            </p>
        </div>
    </body>
</html>
