<%@ include file="/taglib-imports.jspf" %>

<div id="changeTable" class="action">
    <div class="trigger">
        <c:if test="${!empty metaDataSession.tableNames}">
            <html:form action="/Maintenance/MaintenanceChangeTable" onsubmit="return validateMaintenanceChangeTableForm(this);">
                <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
                <html:select name="currentTable" property="name" onchange="this.form.submit();">
                    <html:options name="metaDataSession" property="tableNames"/>
                </html:select>
            </html:form>
        </c:if>
    </div>
</div>
