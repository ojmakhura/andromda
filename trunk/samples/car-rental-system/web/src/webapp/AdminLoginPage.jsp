<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html locale="true">

<head>
<title><bean:message key="title.AdminLoginPage"/></title>
<html:base/>
</head>

<body bgcolor="white">

<hr>
<h1><bean:message key="h1.AdminLoginPage"/></h1>
<html:errors/>
<hr>

<h2><bean:message key="h2.AdminLoginPage"/></h2>

    <html:form action="AdminLoginAction.do">
      <table>
    <tr>
       <td><bean:message key="prompt.AdminLoginPage.accountNo"/></td>
       <td><html:text property="accountNo" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.AdminLoginPage.password"/></td>
       <td><html:password property="password" size="40" /></td>
    </tr>
    <tr>
       <td><bean:message key="prompt.AdminLoginPage.name"/></td>
       <td><html:text property="name" size="40" /></td>
    </tr>
  </table>
  <html:submit><bean:message key="button.submit"/></html:submit>
  <html:reset><bean:message key="button.reset"/></html:reset>
</html:form>

<hr>
<h5>&copy; 2002-2004 The AndroMDA Team</h5>

</body>

</html:html>
