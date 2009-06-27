<%@ include file="/taglib-imports.jspf" %>

<html:html lang="true">

<head>
    <%-- Title --%>
    <title>
        <bean:message key="login.title"/>
    </title>

    <%-- Meta Tags --%>
    <meta http-equiv="Content-Type" content="text/html; charset:UTF-8" />

    <%-- Style Sheets --%>
    <link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/global.css"/>"></link>
    <link rel="stylesheet" type="text/css" href="<html:rewrite page="/layout/default-calendar.css"/>"/>

    <%-- JavaScript --%>
    <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/layout-common.js"/>"></script>
    <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/key-events.js"/>"></script>
</head>

<body>
    <%-- Top Menu --%>
    <div class="topmenu">
        <a class="menu" href="/timetracker/login/login-form.jsp">Log in</a>
    </div>

    <%-- Header --%>
    <div class="header">
        <tiles:insert attribute="header"/>
    </div>

    <%-- Content --%>
    <div class="content">
        <tiles:insert attribute="content"/>
    </div>
</body>

</html:html>