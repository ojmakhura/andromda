<%@ include file="/taglib-imports.jspf" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>

<%@include file="/layout/template-defs.jsp" %>

<tiles:insert beanName="login_layout_definition" beanScope="request" flush="true">

    <tiles:put name="content" type="string">

        <h2>Login</h2>

        <%--
            This page is also used as the form-error-page to ask for a login again.
        --%>
        <c:if test="${not empty param.login_error}">
            <font color="red">
                Your login attempt was not successful, try again.<br />
                Reason:
                <%=
                    ((AuthenticationException)session
                    .getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY))
                    .getMessage()
                %>
            </font>
        </c:if>

        <form action="<c:url value='/j_acegi_security_check'/>" method="POST">
            <label for="username">Username</label><br />
            <input type="text" id="username" name="j_username"
                <c:if test="${not empty param.login_error}">
                    value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'
                </c:if>
            />
            <input class="checkbox" type="checkbox" name="_acegi_security_remember_me" /> Remember me<br />
            <label for="password">Password</label><br />
            <input type="password" id="password" name="j_password"/><br />
            <br />
            <input class="button" type="submit" name="submit" value="Log In" />
        </form>

    </tiles:put>

</tiles:insert>