<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.CustomerMenuPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<hr>
<h1><bean:message key="h1.CustomerMenuPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h2.CustomerMenuPage"/></h2>

<jsp:include page="CustomerNavigator.jsp" />

<hr>
<h5>&copy; 2002 John Doe</h5>

</body>

</html:html>
