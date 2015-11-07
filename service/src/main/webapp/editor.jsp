<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>
<html>
<head>
    <title>Markdown Editor</title>
    <link href="${pageContext.request.contextPath}/assets/css/base.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/css/vue.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/assets/js/vue/vue.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/vue/marked.min.js"></script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <div id="editor">
        <textarea v-model="input" debounce="300"></textarea>
        <div v-html="input | marked"></div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/vue/feckless_vue.js"></script>
</body>
</html>
