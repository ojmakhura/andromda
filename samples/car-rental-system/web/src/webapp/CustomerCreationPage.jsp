<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.CustomerCreationPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<jsp:include page="AdminNavHeader.jsp" />

<hr>
<h1><bean:message key="h1.CustomerCreationPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h21.CustomerCreationPage"/></h2>

<table border="1" cellpadding="10" cellspacing="3">
	<tr>
	    <td><b><bean:message key="th.CustomerCreationPage.name"/></b></td>
	    <td><b><bean:message key="th.CustomerCreationPage.customerNo"/></b></td>
	    <td><b><bean:message key="th.CustomerCreationPage.password"/></b></td>
	</tr>

	<logic:iterate id="element" name="CustomerCreationForm" property="existingCustomers">
	  <tr>
	    <td><bean:write name="element" property="name"/></td>
	    <td><bean:write name="element" property="customerNo"/></td>
	    <td><bean:write name="element" property="password"/></td>
	  </tr>
	</logic:iterate>
</table>
<p />
<hr />

<h2><bean:message key="h22.CustomerCreationPage"/></h2>

<html:form action="CustomerCreationAction.do">
    <table>
	<tr>
	   <td><bean:message key="prompt.CustomerCreationPage.customerName"/></td>
	   <td><html:text property="customerName" size="40" /></td>
	</tr>
	<tr>
	   <td><bean:message key="prompt.CustomerCreationPage.customerNo"/></td>
	   <td><html:text property="customerNo" size="40" /></td>
	</tr>
	<tr>
	   <td><bean:message key="prompt.CustomerCreationPage.password"/></td>
	   <td><html:text property="password" size="40" /></td>
	</tr>
    </table>
    <html:submit><bean:message key="button.submit"/></html:submit>
    <html:reset><bean:message key="button.reset"/></html:reset>
</html:form>

<jsp:include page="AdminNavFooter.jsp" />

</body>

</html:html>
