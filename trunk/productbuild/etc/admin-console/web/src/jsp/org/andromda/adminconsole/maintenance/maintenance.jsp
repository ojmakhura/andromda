<%@ page import="org.andromda.adminconsole.db.Column,
                 org.andromda.adminconsole.config.xml.ColumnConfiguration,
                 org.andromda.adminconsole.config.AdminConsoleConfigurator,
                 org.andromda.adminconsole.db.Table,
                 org.andromda.adminconsole.config.xml.TableConfiguration,
                 org.andromda.adminconsole.db.RowData"%>
<%@ include file="/taglib-imports.jspf" %>

<tiles:insert definition="main.layout">

    <tiles:put name="title" type="string">
        <bean:message key="maintenance.maintenance.title"/>
    </tiles:put>

    <tiles:put name="style" type="string">
        <link rel="stylesheet" type="text/css" media="screen" href="<html:rewrite page="/org/andromda/adminconsole/maintenance/maintenance.css"/>"></link>
    </tiles:put>

    <tiles:put name="javascript" type="string">
        <script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/form-validation.jsp"/>"></script>
        <html:javascript formName="maintenanceMaintenanceChangeTableForm" method="validateMaintenanceChangeTableForm" dynamicJavascript="true" staticJavascript="false" htmlComment="true" cdata="false"/>
    	<script type="text/javascript" language="Javascript1.1" src="<html:rewrite page="/layout/hints.js"/>"></script>
    	<script type="text/javascript" language="Javascript1.1">
        //<!--
            var HINTS_ITEMS = {
                'maintenance.param.table.title':'<formatting:escape language="javascript"><bean:message key="maintenance.param.table.title"/></formatting:escape>',
                'maintenance.param.column.name.title':'<formatting:escape language="javascript"><bean:message key="maintenance.param.column.name.title"/></formatting:escape>',
                'reset':'<formatting:escape language="javascript"><bean:message key="maintenance.reset.title"/></formatting:escape>',
                'reset_no':'<formatting:escape language="javascript"><bean:message key="maintenance.reset.title.notallowed"/></formatting:escape>',
                'reset_reset':'<formatting:escape language="javascript"><bean:message key="maintenance.reset.title.reset"/></formatting:escape>',
                'reset_noreset':'<formatting:escape language="javascript"><bean:message key="maintenance.reset.title.reset.not.allowed"/></formatting:escape>',
                'change table':'<formatting:escape language="javascript"><bean:message key="maintenance.change.table.title"/></formatting:escape>',
                'change table_no':'<formatting:escape language="javascript"><bean:message key="maintenance.change.table.title.notallowed"/></formatting:escape>',
                'change table_reset':'<formatting:escape language="javascript"><bean:message key="maintenance.change.table.title.reset"/></formatting:escape>',
                'change table_noreset':'<formatting:escape language="javascript"><bean:message key="maintenance.change.table.title.reset.not.allowed"/></formatting:escape>',
                'calendar.popup':'<formatting:escape language="javascript"><bean:message key="calendar.popup"/></formatting:escape>'
            };

            var hints = new THints (HINTS_CFG, HINTS_ITEMS);
        //-->
    	</script>
    </tiles:put>

    <tiles:put name="body" type="string">

        <c:set var="configurator" value="${databaseLoginSession.configurator}"/>
        <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
        <c:set var="tableConfig" value="${acf:getTableConfiguration(configurator,currentTable)}"/>

        <div>
            <h1><bean:message key="maintenance.maintenance.title"/></h1>
        </div>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-change-table.jsp" flush="false"/>

        <c:if test="${tableConfig.insertable}">
            <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-insert.jsp" flush="false"/>
        </c:if>

<%--
        <div id="applyChanges" class="action">
--%>
            <html:form action="/Maintenance/MaintenanceApplyChanges">
                <display:table name="${metaDataSession.currentTableData}" id="row"
                               requestURI="${pageContext.request.requestURL}"
                               export="${tableConfig.export}" pagesize="${tableConfig.pageSize}" sort="list">
                    <c:set var="index" value="${row_rowNum-1}"/>
                    <display:column media="html"
                        title="" autolink="false" nulls="false"
                        sortable="false" paramId="${column.name}">
                        <input type="checkbox" id="change-${index}" name="selectedRowsAsArray" value="${index}"/>
                    </display:column>
                    <c:forEach items="${currentTable.columns}" var="column">
                        <c:set var="columnConfig" value="${acf:getColumnConfiguration(configurator,column)}"/>
                        <c:if test="${columnConfig.exportable}">
                            <display:column media="xml excel csv"
                                property="${column.name}" title="${column.name}"
                                autolink="true" nulls="false" paramId="${column.name}" />
                        </c:if>
                        <display:column media="html"
                            title="${column.name}" autolink="false" nulls="false"
                            sortable="${columnConfig.sortable}" paramId="${column.name}">
                            ${acf:getUpdateWidget(configurator, column, row, index)}
                        </display:column>
                    </c:forEach>
                </display:table>
                <input type="submit" name="kind" value="delete"/>
                <input type="submit" name="kind" value="update"/>
            </html:form>
<%--
        </div>
--%>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-reset.jsp" flush="false"/>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-reload.jsp" flush="false"/>

    </tiles:put>

</tiles:insert>
