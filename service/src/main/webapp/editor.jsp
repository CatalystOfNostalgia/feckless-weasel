<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*, com.fecklessweasel.service.objectmodel.StoredFile"%>
<%@ page import="com.fecklessweasel.service.objectmodel.OMUtil" %>
<%@ page import="com.fecklessweasel.service.datatier.SQLSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.fecklessweasel.service.objectmodel.ServiceException" %>
<%@ page import="com.fecklessweasel.service.datatier.SQLInteractionInterface" %>
<html>
<head>
    <title>Markdown Editor</title>
    <link href="${pageContext.request.contextPath}/assets/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/login.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/assets/css/vue.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/assets/js/vue/vue.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/vue/marked.min.js"></script>
    <script language="javascript">
        function validate(event) {
            var noteTitleInput = document.getElementById('title');
            var noteDescInput  = document.getElementById('description');

            if (noteTitleInput.value.length <= 0 || noteTitleInput.value.length > <%= StoredFile.MAX_TITLE %>) {
                alert("Note title must be nonzero and be shorter than <%= StoredFile.MAX_TITLE %> characters.");
                return false;
            }

            if (noteDescInput.value.length <= 0 || noteDescInput.value.length > <%= StoredFile.MAX_DESCRIPTION %>) {
                alert("Note description must be nonzero and shorter than <%= StoredFile.MAX_DESCRIPTION %> characters.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <jsp:include page="header.jsp"/>
    <%
        String title = "";
        String description = "";
        String textareaContents = "";
        if (request.getParameter("fid") != null) {
            int fid = OMUtil.parseInt(request.getParameter("fid"));
            StoredFile file = SQLSource.interact(new SQLInteractionInterface<StoredFile>() {
                @Override
                public StoredFile run(Connection connection) throws ServiceException {
                    return StoredFile.lookup(connection, OMUtil.parseInt(request.getParameter("fid")));
                }
            });
            title = file.getTitle();
            description = file.getDescription();
            textareaContents = StoredFile.getMarkdownText(fid);
        }
    %>
    <form action="/servlet/markdown/upload" method="post" class="form-vertical" style="margin-top:1%;" onsubmit="return validate()">
        <div class="container">
            <input type="text" class="form-control" id="title" name="title" placeholder="title" value="<%=title%>">
            <textarea class="form-control" id="description" rows="5" placeholder="Description" name="description"><%=description%></textarea>
            <input type="hidden" name="cid" id="cid" value="${param.cid}">
            <input type="hidden" name="fid" id="fid" value="<%=request.getParameter("fid")%>">
        </div>
        <div class="container editor" id="editor">
            <textarea id="markdown" name="markdown" v-model="input" debounce="300"><%=textareaContents%></textarea>
            <div v-html="input | marked"></div>
        </div>
        <div class="container submit-btn">
            <button type="submit" class="btn btn-lg btn-warning">Submit</button>
        </div>
    </form>
    <script src="${pageContext.request.contextPath}/assets/js/vue/feckless_vue.js"></script>
</body>
</html>
