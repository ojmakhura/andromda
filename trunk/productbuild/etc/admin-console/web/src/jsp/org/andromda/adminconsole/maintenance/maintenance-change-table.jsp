<%@ include file="/taglib-imports.jspf" %>

<div id="changeTable" class="action">
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceChangeTable" onsubmit="return validateMaintenanceChangeTableForm(this);">
            <c:choose>
               <c:when test="${!empty metaDataSession.tableNames}">
                  <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
                  <html:select name="currentTable" property="name" onchange="this.form.submit();" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()"  >
                      <html:options name="metaDataSession" property="tableNames"/>
                  </html:select>
               </c:when>
               <c:otherwise>
                   <html:text name="form" property="name" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()" readonly="true"/>
               </c:otherwise>
            </c:choose>
        </html:form>
    </div>
</div>
