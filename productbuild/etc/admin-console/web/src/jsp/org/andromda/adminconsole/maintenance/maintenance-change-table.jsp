<%@ include file="/taglib-imports.jspf" %>

<div id="changeTable" class="action">
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceChangeTable" onsubmit="return validateMaintenanceChangeTableForm(this);">
            <c:choose>
               <c:when test="${!empty form.nameBackingList}">
                  <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
                  <html:select name="currentTable" property="name" onchange="this.form.submit();" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()"  >
                      <html:optionsCollection name="form" property="nameBackingList" label="label" value="value"/>
                  </html:select>
               </c:when>
               <c:otherwise>
                   <html:text name="form" property="name" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()" readonly="true"/>
               </c:otherwise>
            </c:choose>
        </html:form>
    </div>
</div>
