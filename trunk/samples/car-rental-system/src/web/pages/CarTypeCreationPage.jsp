<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.CarTypeCreationPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<jsp:include page="AdminNavHeader.jsp" />

<hr>
<h1><bean:message key="h1.CarTypeCreationPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h21.CarTypeCreationPage"/></h2>

<table border="1" cellpadding="6" cellspacing="3">
    <tr>
        <td><b><bean:message key="th.CarTypeCreationPage.comfortClass"/></b></td>
        <td><b><bean:message key="th.CarTypeCreationPage.identifier"/></b></td>
        <td><b><bean:message key="th.CarTypeCreationPage.manufacturer"/></b></td>
        <td><b><bean:message key="th.CarTypeCreationPage.orderNo"/></b></td>
    </tr>

    <logic:iterate id="element" name="CarTypeCreationForm" property="existingCarTypes">
      <tr>
        <td><bean:write name="element" property="comfortClass"/></td>
        <td><bean:write name="element" property="identifier"/></td>
        <td><bean:write name="element" property="manufacturer"/></td>
        <td><bean:write name="element" property="orderNo"/></td>
      </tr>
    </logic:iterate>
</table>
<p />
<hr />

<h2><bean:message key="h22.CarTypeCreationPage"/></h2>

<html:form action="CarTypeCreationAction.do">
      <table>
    <tr>
       <td><bean:message key="prompt.CarTypeCreationPage.comfortClass"/></td>
       <td><html:text property="comfortClass" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.CarTypeCreationPage.identifier"/></td>
       <td><html:text property="identifier" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.CarTypeCreationPage.manufacturer"/></td>
       <td><html:text property="manufacturer" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.CarTypeCreationPage.orderNo"/></td>
       <td><html:text property="orderNo" size="40" /></td>
    </tr>
  </table>
  <html:submit><bean:message key="button.submit"/></html:submit>
  <html:reset><bean:message key="button.reset"/></html:reset>
</html:form>

<jsp:include page="AdminNavFooter.jsp" />

</body>

</html:html>
