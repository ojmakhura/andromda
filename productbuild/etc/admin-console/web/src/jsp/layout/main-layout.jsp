<%@ include file="/taglib-imports.jspf" %>



<html:html lang="true">

    <head>
        <title>
            <tiles:insert attribute="title" flush="true"/>
        </title>
        <html:base/>
        <meta http-equiv="Content-Type" content="text/html; charset:utf-8" />
<%-- uncomment this to enable the browser 'favorites' icons
        <link rel="shortcut icon" href="my-custom-image.ico"></link>
        <link rel="icon" type="image/gif" href="my-custom-image.gif"></link>
--%>
        <link rel="stylesheet" type="text/css" media="screen" href="<html:rewrite page="/layout/default-application.css"/>"></link>
        <link rel="stylesheet" type="text/css" media="screen" href="<html:rewrite page="/layout/default.css"/>"></link>
        <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/layout-common.js"/>"></script>
    	<script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/menu/menu-expandable.js"/>"></script>
        <tiles:insert attribute="style" flush="true"/>
        <tiles:insert attribute="javascript" flush="true"/>
    </head>

    <body>
        <div id="container">
            <div id="content">
                <tiles:insert attribute="body" flush="true"/>
                <tiles:insert attribute="messages" flush="true"/>
            </div>
        </div>
    </body>

</html:html>