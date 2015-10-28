<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.fecklessweasel.service.objectmodel.*" %>
<html>
    <head>
     <jsp:include page="header.jsp"/>
        <title>Feckless Weasel University Create Page</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <div class="column-beta">
            <h1>Create a University</h1>
            <form class="form-default" action="/servlet/university" method="post" enctype="application/x-www-form-urlencoded">
                <input type="text" class="text-large" id="longName" name="longName" placeholder="name">
                <input type="text" class="text-large" id="acronym" name="acronym" placeholder="acronym">
                <input type="text" class="text-large" id="city" name="city" placeholder="city">
                <input type="text" class="text-large" id="state" name="state" placeholder="state">
                <input type="text" class="text-large" id="country" name="country" placeholder="country">
                <div>
                    <button type="submit" class="button-large" id="submit" name="submit" onclick = "listUni()">Create</button>
                </div>
            </form>
            <h3> Universities </h3>
            <script type="text/javascript">
            function listUni(){
                var p = document.createElement("P");
                var t = document.createTextNode(longName);
                p = p.appendChild(t)
                document.body.appendChild(p);
           }
        </div>
    </body>
</html>