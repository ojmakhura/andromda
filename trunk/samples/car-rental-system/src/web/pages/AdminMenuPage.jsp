<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

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
<h5>&copy; 2002-2003 Matthias Bohlen and the AndroMDA team</h5>

</body>

</html:html>
