<%@ page import="org.andromda.adminconsole.db.Column,
                 org.andromda.adminconsole.db.RowData,
                 org.andromda.adminconsole.config.AdminConsoleConfigurator"%>
<%@ include file="/taglib-imports.jspf" %>


<div id="maintenanceInsert" class="action">
    <h3><bean:message key="maintenance.insert"/></h3>
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceInsert" onsubmit="">

            <input type="hidden" value="${form.name}"/>

            <table>
                <c:forEach items="${metaDataSession.currentTable.columns}" var="column">
                    <tr>
                        <td>${column.name}</td>
                        <td>${acf:getInsertWidget(databaseLoginSession.configurator,column,"parametersAsArray","")}</td>
                        <td>
                            <c:if test="${column.foreignKeyColumn}">
                                <c:set var="foreignTable" value="${column.importedKeyColumn.table}" scope="page"/>
                                <bean:message key="change.to.table" bundle="custom"/>
                                <html:link action="/Maintenance/MaintenanceChangeTable" styleClass="foreignTableLink"
                                    paramId="name" paramName="foreignTable" paramProperty="name" paramScope="page">
                                    ${foreignTable.name}
                                </html:link>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                          <html:submit onmouseover="hints.show('insert')" onmouseout="hints.hide()">
                              <bean:message key="maintenance.insert"/>
                          </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </div>


</div>

<br class="clr"/>
