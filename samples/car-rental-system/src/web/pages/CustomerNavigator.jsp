<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<bean:message key="prompt.CustomerMenuPage.customerTasks"/>
<ul>
<li><a href="ListOfReservationsAction.do"><bean:message key="prompt.CustomerMenuPage.reserveCars"/></a></li>
<li><a href="MainMenu.jsp"><bean:message key="prompt.CustomerMenuPage.logout"/></a></li>
</ul>
