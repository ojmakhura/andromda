<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:message key="prompt.AdminMenuPage.adminTasks"/>
<ul>
<li><a href="ListCustomersAction.do"><bean:message key="prompt.AdminMenuPage.createCustomers"/></a></li>
<li><a href="ListCarTypesAction.do"><bean:message key="prompt.AdminMenuPage.createCarTypes"/></a></li>
<li><a href="ListCarsAction.do"><bean:message key="prompt.AdminMenuPage.createCars"/></a></li>
<li><a href="MainMenu.jsp"><bean:message key="prompt.AdminMenuPage.logout"/></a></li>
</ul>
