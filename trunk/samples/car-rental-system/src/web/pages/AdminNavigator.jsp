<%@ page language="java" %>
<%@ taglib uri="struts-bean.tld" prefix="bean" %>
<%@ taglib uri="struts-html.tld" prefix="html" %>
<%@ taglib uri="struts-logic.tld" prefix="logic" %>

<bean:message key="prompt.AdminMenuPage.adminTasks"/>
<ul>
<li><a href="ListCustomersAction.do"><bean:message key="prompt.AdminMenuPage.createCustomers"/></a></li>
<li><a href="ListCarTypesAction.do"><bean:message key="prompt.AdminMenuPage.createCarTypes"/></a></li>
<li><a href="ListCarsAction.do"><bean:message key="prompt.AdminMenuPage.createCars"/></a></li>
<li><a href="MainMenu.jsp"><bean:message key="prompt.AdminMenuPage.logout"/></a></li>
</ul>
