<%@ page import="org.andromda.adminconsole.db.Column,
                 org.andromda.adminconsole.db.RowData,
                 org.andromda.adminconsole.config.AdminConsoleConfigurator"%>
<%@ include file="/taglib-imports.jspf" %>

<div id="insert" class="action">
    <h3><bean:message key="maintenance.maintenance.insert"/></h3>
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceInsert" onsubmit="">
            <table>
                <c:forEach items="${metaDataSession.currentTable.columns}" var="column">
                    <bean:parameter id="value" name="${column.name}" value=""/>
                    <tr>
                        <td>${column.name}</td>
                        <td>${acf:getInsertWidget(databaseLoginSession.configurator,column,value)}</td>
                        <td>
                            <c:if test="${column.foreignKeyColumn}">
                                <c:set var="foreignTable" value="${column.importedKeyColumn.table}" scope="page"/>
                                <c:if test="${column.table.name != foreignTable.name}"> <%-- don't render link to yourself --%>
                                    <c:if test="${acf:contains(metaDataSession.tableNames,foreignTable.name)}"> <%-- only render allowed tables --%>
                                        <bean:message key="show.table" bundle="custom"/>
                                        <html:link action="/Maintenance/MaintenanceChangeTable" styleClass="foreignTableLink"
                                            paramId="name" paramName="foreignTable" paramProperty="name" paramScope="page">
                                            ${foreignTable.name}
                                        </html:link>
                                    </c:if>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <input type="submit" value="<bean:message key="maintenance.maintenance.insert"/>"
                               onclick="this.form.name='maintenanceMaintenanceInsertForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceInsert"/>';"/>
                        <input type="submit" value="<bean:message key="maintenance.maintenance.search"/>"
                               onclick="this.form.name='maintenanceMaintenanceSearchForm';this.form.action='<html:rewrite action="/Maintenance/MaintenanceSearch"/>';"/>
                    </td>
                    <td>
                        <html:checkbox name="form" property="exactMatches"/>
                        <bean:message key="maintenance.maintenance.param.exact.matches"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </div>
</div>
<br class="clr"/>
