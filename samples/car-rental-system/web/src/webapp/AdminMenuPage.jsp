<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.AdminMenuPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<hr>
<h1><bean:message key="h1.AdminMenuPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h2.AdminMenuPage"/></h2>

<jsp:include page="AdminNavigator.jsp" />

<hr>
<h5>&copy; 2002-2004 The AndroMDA Team</h5>

</body>

</html:html>
