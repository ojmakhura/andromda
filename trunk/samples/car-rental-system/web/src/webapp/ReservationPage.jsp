<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.ReservationPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<jsp:include page="CustomerNavHeader.jsp" />

<hr>
<h1><bean:message key="h1.ReservationPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h21.ReservationPage"/></h2>

<table border="1" cellpadding="6" cellspacing="3">
    <tr>
        <td><b><bean:message key="th.ReservationPage.comfortClass"/></b></td>
        <td><b><bean:message key="th.ReservationPage.reservationDate"/></b></td>
    </tr>

    <logic:iterate id="element" name="ReservationForm" property="reservationList">
      <tr>
        <td><bean:write name="element" property="comfortClass"/></td>
        <td><bean:write name="element" property="reservationDate"/></td>
      </tr>
    </logic:iterate>
</table>
<p />
<hr />

<h2><bean:message key="h22.ReservationPage"/></h2>

<html:form action="CarReservationAction.do">
  <table>
    <tr>
       <td><bean:message key="prompt.ReservationPage.comfortClass"/></td>
       <td><html:text property="comfortClass" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.ReservationPage.reservationDate"/></td>
       <td><html:text property="reservationDate" size="40" /></td>
    </tr>
  </table>
  <html:submit><bean:message key="button.submit"/></html:submit>
  <html:reset><bean:message key="button.reset"/></html:reset>
</html:form>

<jsp:include page="CustomerNavFooter.jsp" />

</body>

</html:html>

