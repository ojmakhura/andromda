<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:message key="prompt.CustomerMenuPage.customerTasks"/>
<ul>
<li><a href="ListOfReservationsAction.do"><bean:message key="prompt.CustomerMenuPage.reserveCars"/></a></li>
<li><a href="MainMenu.jsp"><bean:message key="prompt.CustomerMenuPage.logout"/></a></li>
</ul>
