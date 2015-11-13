<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<html>
<head>
    <title>Markdown Editor</title>
    <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/css/vue.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/assets/js/vue/vue.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/vue/marked.min.js"></script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <form action="/servlet/markdown/upload" method="post" class="form-vertical" style="margin-top:1%;">
        <div class="container">
            <input type="text" class="form-control" id="title" name="title" placeholder="title">
            <textarea class="form-control" id="description" rows="5" name="description">description</textarea>
            <input type="hidden" name="class" id="class" value="0"> <%--TODO: Un-hardcode this! --%>
        </div>
        <div class="container editor" id="editor">
            <textarea id="markdown" name="markdown" v-model="input" debounce="300"></textarea>
            <div v-html="input | marked"></div>
        </div>
        <div class="container submit-btn">
            <button type="submit" class="btn btn-lg btn-warning">Submit</button>
        </div>
    </form>
    <script src="${pageContext.request.contextPath}/assets/js/vue/feckless_vue.js"></script>
</body>
</html>
