<%@ include file="/taglib-imports.jspf" %>


<div id="connect" class="action">
    <h3><bean:message key="maintenance.connect"/></h3>
    <div class="trigger">
        <html:form action="/Maintenance/EnterLoginInfoConnect" onsubmit="ensureUrl(); return validateEnterLoginInfoConnectForm(this);">


            <table>
                <tbody>
                    <c:set var="knownUrls" value="${databaseLoginSession.configurator.knownDatabaseUrls}"/>
                    <c:choose>
                        <c:when test="${databaseLoginSession.configurator.arbitraryUrlAllowed}">
                            <tr>
                                <td><bean:message key="maintenance.param.url"/></td>
                                <td><html:text name="form" property="url" onmouseover="hints.show('maintenance.param.url.title')" onmouseout="hints.hide()" /></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <c:if test="${!empty knownUrls}">
                                        <input type="checkbox" name="chooseKnownUrl" onclick="toggleUrlList();"/>
                                        <select name="selectedUrl" disabled="true">
                                            <c:forEach var="url" items="${knownUrls}">
                                                <c:choose>
                                                    <c:when test="${url == form.url}">
                                                        <option selected value="${url}">${url}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${url}">${url}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </c:if>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <html:hidden name="form" property="url"/>
                            <tr>
                                <td><bean:message key="maintenance.param.url"/></td>
                                <td>
                                    <select name="selectedUrl">
                                        <c:forEach var="url" items="${knownUrls}">
                                            <c:choose>
                                                <c:when test="${url == form.url}">
                                                    <option selected value="${url}">${url}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${url}">${url}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td><bean:message key="maintenance.param.user"/> <div class="important">*</div></td>
                        <td>
                           <html:text name="form" property="user" onmouseover="hints.show('maintenance.param.user.title')" onmouseout="hints.hide()" />
                        </td>
                    </tr>
                    <tr>
                        <td><bean:message key="maintenance.param.password"/></td>
                        <td>
                           <html:text name="form" property="password" onmouseover="hints.show('maintenance.param.password.title')" onmouseout="hints.hide()" />
                        </td>
                    </tr>
                    <tr>
                        <td><bean:message key="maintenance.param.schema"/></td>
                        <td>
                           <html:text name="form" property="schema" onmouseover="hints.show('maintenance.param.schema.title')" onmouseout="hints.hide()" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                              <html:submit onmouseover="hints.show('connect')" onmouseout="hints.hide()">
                                  <bean:message key="maintenance.connect"/>
                              </html:submit>
                        </td>
                    </tr>
                </tbody>
            </table>
        </html:form>
    </div>


</div>

<br class="clr"/>

<script type="text/javascript" language="Javascript1.1">
//<!--
    function toggleUrlList()
    {
        var checkbox = document.forms[0].chooseKnownUrl;
        var textfield = document.forms[0].url;
        var select =  document.forms[0].selectedUrl;

        select.disabled = !checkbox.checked;
        textfield.readOnly = checkbox.checked;
    }

    function ensureUrl()
    {
        var checkbox = document.forms[0].chooseKnownUrl;
        if (checkbox.checked)
        {
            document.forms[0].url.value = document.forms[0].selectedUrl.value;
        }
    }
//-->
</script>
