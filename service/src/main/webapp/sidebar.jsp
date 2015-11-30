<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.*, com.fecklessweasel.service.*, com.fecklessweasel.service.objectmodel.*, com.fecklessweasel.service.datatier.*, java.sql.Connection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    final UserSession authSession = UserSessionUtil.resumeSession(request);

%>
<div class="col-sm-3 col-md-2 sidebar" style="padding-bottom: 100%; background-color: #FFD79F;">
    <ul class="nav nav-sidebar">
        <h2>My Notes</h2>
    </ul>
    <ul class="nav nav-sidebar">
        <h2>My Favorite Classes</h2>
    </ul>
    <ul class="nav nav-sidebar">
        <h2>My Favorite Files</h2>
    </ul>
</div>
