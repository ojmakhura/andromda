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
    </tiles:put>

    <tiles:put name="body" type="string">

        <c:set var="configurator" value="${databaseLoginSession.configurator}"/>
        <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
        <c:set var="tableConfig" value="${acf:getTableConfiguration(configurator,currentTable)}"/>

        <div>
            <h1><bean:message key="maintenance.maintenance.title"/></h1>
        </div>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-change-table.jsp" flush="false"/>

        <table>
            <tr>
                <c:if test="${tableConfig.insertable}">
                    <td>
                        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-insert.jsp" flush="false"/>
                    </td>
                </c:if>
                <td id="importedTables">
                    <c:if test="${currentTable.importingTablesCount > 0}">
                        <bean:message key="this.table.is.referenced.from" bundle="custom"/>
                        <ul>
                            <c:forEach var="foreignTable" items="${currentTable.importingTables}">
                                <c:if test="${column.table.name != foreignTable.name}"> <%-- don't render link to yourself --%>
                                    <c:if test="${acf:contains(metaDataSession.tableNames,foreignTable.name)}"> <%-- only render allowed tables --%>
                                        <li>
                                            <html:link action="/Maintenance/MaintenanceChangeTable" styleClass="foreignTableLink"
                                                paramId="name" paramName="foreignTable" paramProperty="name" paramScope="page">
                                                ${foreignTable.name}
                                            </html:link>
                                        </li>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </c:if>
                </td>
            </tr>
        </table>

        <div id="recordTable">
            <html:form action="/Maintenance/MaintenanceDelete" onsubmit="return verifySelection();">
                <display:table name="${metaDataSession.currentTableData}" id="row"
                               requestURI="${pageContext.request.requestURL}"
                               export="${tableConfig.exportable}" pagesize="${tableConfig.pageSize}" sort="list">
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
                <input type="submit" value="<bean:message key="maintenance.maintenance.delete"/>"
                       onclick="this.form.name='maintenanceMaintenanceDeleteForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceDelete"/>';"/>

                <input type="submit" value="<bean:message key="maintenance.maintenance.update"/>"
                       onclick="this.form.name='maintenanceMaintenanceUpdateForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceUpdate"/>';"/>
            </html:form>
        </div>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-reset.jsp" flush="false"/>

        <tiles:insert page="/org/andromda/adminconsole/maintenance/maintenance-reload.jsp" flush="false"/>

    </tiles:put>

</tiles:insert>

<script type="text/javascript" language="Javascript1.1">
//<!--
    function verifySelection()
    {
        var valid = false;
        var checkboxes = document.getElementsByName('selectedRowsAsArray');

        if ( (checkboxes == null) || (checkboxes.length==0) )
        {
            alert('<bean:message key="no.rows.to.operate.on" bundle="custom"/>');
        }
        else
        {
            for (var i=0; (i<checkboxes.length) && (valid==false); i++)
            {
                valid = checkboxes[i].checked;
            }
            if (valid == false)
            {
                alert('<bean:message key="at.least.one.record.must.be.selected" bundle="custom"/>');
            }
        }

        return valid;
    }
//-->
</script>

