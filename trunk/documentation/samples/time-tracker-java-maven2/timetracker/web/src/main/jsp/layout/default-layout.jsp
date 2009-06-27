<%@ include file="/taglib-imports.jspf" %>

<html:html lang="true">

<head>
    <%-- Title --%>
    <title>
        <tiles:insert attribute="title"/>
    </title>

    <%-- Meta Tags --%>
    <meta http-equiv="Content-Type" content="text/html; charset:UTF-8" />

    <%-- Style Sheets --%>
    <link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/global.css"/>"></link>
    <link rel="stylesheet" type="text/css" href="<html:rewrite page="/layout/default-calendar.css"/>"/>
    <tiles:insert attribute="style"/>

    <%-- JavaScript --%>
    <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/layout-common.js"/>"></script>
    <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/key-events.js"/>"></script>
    <tiles:insert attribute="javascript"/>
</head>

<body>
    <%-- Body Includes --%>
    <tiles:insert attribute="body_includes"/>

    <%-- Top Menu --%>
    <div class="topmenu">
        <tiles:insert attribute="topmenu"/>
    </div>

    <%-- Header --%>
    <div class="header">
        <tiles:insert attribute="header"/>
    </div>

    <%-- Menu bar --%>
    <div class="menubar">
        <tiles:insert attribute="menubar"/>
    </div>

    <%-- Content --%>
    <div class="content">
        <tiles:insert attribute="content"/>
    </div>
</body>

</html:html>