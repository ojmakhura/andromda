<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.CarCreationPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<jsp:include page="AdminNavHeader.jsp" />

<hr>
<h1><bean:message key="h1.CarCreationPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h21.CarCreationPage"/></h2>

<table border="1" cellpadding="6" cellspacing="3">
    <tr>
        <td><b><bean:message key="th.CarCreationPage.manufacturer"/></b></td>
        <td><b><bean:message key="th.CarCreationPage.identifier"/></b></td>
        <td><b><bean:message key="th.CarCreationPage.registrationNo"/></b></td>
        <td><b><bean:message key="th.CarCreationPage.inventoryNo"/></b></td>
    </tr>

    <logic:iterate id="element" name="CarCreationForm" property="existingCars">
      <tr>
        <td><bean:write name="element" property="carTypeData.manufacturer"/></td>
        <td><bean:write name="element" property="carTypeData.identifier"/></td>
        <td><bean:write name="element" property="carData.registrationNo"/></td>
        <td><bean:write name="element" property="carData.inventoryNo"/></td>
      </tr>
    </logic:iterate>
</table>
<p />
<hr />


<h2><bean:message key="h22.CarCreationPage"/></h2>

<bean:define id="carTypeOptions" name="CarCreationForm"
        property="existingCarTypes" type="java.util.Collection"/>

<html:form action="CarCreationAction.do">
  <table>
    <tr>
       <td><bean:message key="prompt.CarCreationPage.carType"/></td>
       <td>
          <html:select property="carTypeId">
            <html:options  collection="carTypeOptions"
                        labelProperty="identifier"
                             property="id"
            />
          </html:select>
       </td>
    </tr>
    <tr>
       <td><bean:message key="prompt.CarCreationPage.registrationNo"/></td>
       <td><html:text property="registrationNo" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.CarCreationPage.inventoryNo"/></td>
       <td><html:text property="inventoryNo" size="40" /></td>
    </tr>
  </table>
  <html:submit><bean:message key="button.submit"/></html:submit>
  <html:reset><bean:message key="button.reset"/></html:reset>
</html:form>

<jsp:include page="AdminNavFooter.jsp" />

</body>

</html:html>
