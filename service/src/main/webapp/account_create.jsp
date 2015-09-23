<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<html>
    <head>
        <title>Feckless Weasel Account Creation</title>
        <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    </head>
    <body class="four-column">
        <div class="column-beta">
            <h1>Create an account</h1>
            <h2>It's free!</h2>
            <input type="text" class="text-large" id="username" name="username" placeholder="username">
            <input type="password" class="text-large" id="password" name="password" placeholder="password">
            <input type="password" class="text-large" id="password" name="retype password" placeholder="retype password">
            <div>
                <button type="submit" class="button-large" id="submit" name="submit">Create</button>
            </div>
        </div>
    </body>
</html>
