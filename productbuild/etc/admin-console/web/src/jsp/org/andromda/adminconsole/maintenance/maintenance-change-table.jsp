<%@ include file="/taglib-imports.jspf" %>


<div id="changeTable" class="action">
    <h3><bean:message key="maintenance.change.table"/></h3>
    <div class="trigger">
        <html:form action="/Maintenance/MaintenanceChangeTable" onsubmit="return validateMaintenanceChangeTableForm(this);">


            <table>
                <tbody>
                    <tr>
                        <td><bean:message key="maintenance.param.name"/> <div class="important">*</div></td>
                        <td>
                           <c:choose>
                               <c:when test="${!empty form.nameBackingList}">
                                  <c:set var="currentTable" value="${metaDataSession.currentTable}"/>
                                  <html:select name="currentTable" property="name" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()"  >
                                      <html:optionsCollection name="form" property="nameBackingList" label="label" value="value"/>
                                  </html:select>
                               </c:when>
                               <c:otherwise>
                                   <html:text name="form" property="name" onmouseover="hints.show('maintenance.param.table.title')" onmouseout="hints.hide()" readonly="true"/>
                               </c:otherwise>
                           </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td>
                              <html:submit onmouseover="hints.show('change table')" onmouseout="hints.hide()">
                                  <bean:message key="maintenance.change.table"/>
                              </html:submit>
                        </td>
                    </tr>
                </tbody>
            </table>
        </html:form>
    </div>


</div>

<br class="clr"/>
