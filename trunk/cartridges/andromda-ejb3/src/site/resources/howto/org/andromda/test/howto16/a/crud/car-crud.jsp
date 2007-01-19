<%@ include file="/taglib-imports.jspf" %>
<tiles:insert definition="main.layout">

    <tiles:put name="title" type="string">
        <bean:message key="car.page.title"/>
    </tiles:put>

    <tiles:put name="style" type="string">
        <link rel="stylesheet" type="text/css" media="screen" href="<html:rewrite page="/layout/default-manageable.css"/>"></link>
        <link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page="/layout/default-calendar.css"/>"/>
    </tiles:put>

    <tiles:put name="javascript" type="string">
        <script type="text/javascript" src="<html:rewrite action="/calendar"/>"></script>
        <script type="text/javascript">
        //<!--
            function setSelect(multi, form, name, value)
            {
                var select = form.elements[name];
                var options = select.options;

                // for browser compatibility's sake we go through the options ourselves
                for (var i=0; i<options.length; i++)
                {
                    if (multi)
                    {
                        // Array.indexOf is defined in Javascript 1.5, not before
                        options[i].selected = arrayContainsElement(value,options[i].value);
                    }
                    else
                    {
                        if (options[i].value == value)
                        {
                            select.selectedIndex = i;
                            break;
                        }
                    }

                }
            }

            function arrayContainsElement(array, element)
            {
              var containsElement = false;
              for (var i=0; i<array.length && !containsElement; i++) containsElement = (array[i] == element);
              return containsElement;
            }

            function setAction(crud) { document.forms['manageCarForm'].elements['crud'].value = crud; }

            function enableUpdate(enabled) { document.getElementById("updateButton").disabled = !enabled; }

            var selectionCount = 0;

            function verifyEnableDelete(checkbox)
            {
                if (checkbox.checked) selectionCount++; else selectionCount--;
                document.getElementById("deleteButton").disabled = (selectionCount < 1);
            }

            function clearFields(form)
            {
                form.reset();
                enableUpdate(false);
                setFields("","","","","",form);
            }

            function setFields(serial,name,type,id,owner,form)
            {
                form.elements["serial"].value = serial;
                form.elements["name"].value = name;
                setSelect(false,form,"type",type);
                form.elements["id"].value = id;
                setSelect(false,form,"owner",owner);
            }
        //-->
        </script>
    </tiles:put>

    <tiles:put name="body" type="string">

        <div>
            <h1><bean:message key="car.page.title"/></h1>
        </div>

        <html:form styleId="manageCarForm" action="/Car/Manage" method="post">
            <input type="hidden" name="crud" value=""/>
            <div id="criteria">
                <c:if test="${!empty manageableForm}">
                    <table>
                        <tr>
                            <td><nobr><bean:message key="car.serial"/> <div class="important">*</div></nobr></td>
                            <td>
                                <html:text name="manageableForm" property="serial" styleClass="criteriaField" styleId="serial"/>
                            </td>
                        </tr>
                        <tr>
                            <td><nobr><bean:message key="car.name"/> <div class="important">*</div></nobr></td>
                            <td>
                                <html:text name="manageableForm" property="name" styleClass="criteriaField" styleId="name"/>
                            </td>
                        </tr>
                        <tr>
                            <td><nobr><bean:message key="car.type"/> <div class="important">*</div></nobr></td>
                            <td>
                                <html:select name="manageableForm" property="type">
                                    <option value=""><bean:message key="select.option.blank"/></option>
                                    <html:option value="SEDAN">SEDAN</html:option>
                                    <html:option value="LIFTBACK">LIFTBACK</html:option>
                                    <html:option value="WAGON">WAGON</html:option>
                                </html:select>
                            </td>
                        </tr>
                    <html:hidden name="manageableForm" property="id"/>
                        <tr>
                            <td>
                                    <nobr><html:link action="/Person/Manage"><bean:message key="car.owner"/></html:link> <div class="important">*</div></nobr>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty manageableForm.ownerBackingList}">
                                        <select name="owner" disabled="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="owner">
                                            <option value=""><bean:message key="select.option.blank"/></option>
                                            <c:forEach var="valueLabel" items="${manageableForm.ownerBackingList}">
                                                <c:choose>
                                                    <c:when test="${valueLabel[0] eq manageableForm.owner}">
                                                        <option value="${valueLabel[0]}" selected="selected">${valueLabel[1]}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${valueLabel[0]}">${valueLabel[1]}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </table>

                    <input type="submit" id="readButton" class="button" value="<bean:message key="button.read"/>" onclick="this.form.elements['id'].value='';setAction('read');"/>
                    <input type="submit" id="createButton" class="button" value="<bean:message key="button.create"/>" onclick="setAction('create');"/>
                    <input type="submit" id="deleteButton" class="button" value="<bean:message key="button.delete"/>" disabled="disabled" onclick="setAction('delete');"/>
                    <input type="submit" id="updateButton" class="button" value="<bean:message key="button.update"/>" disabled="disabled" onclick="setAction('update');return validateUpdate();"/>
                    <input type="button" id="clearButton" class="button" value="<bean:message key="button.clear"/>" onclick="clearFields(this.form);"/>
                </c:if>

<div id="entitySwitcher">
    <nobr>
        <bean:message key="select.other.entity"/>
        <select onchange="document.location=this.options[this.selectedIndex].value+'?ref_Car='+this.form.elements['id'].value;">
            <option selected="selected" value="<html:rewrite page="/Car/Manage.do"/>"><bean:message key="car"/></option>
            <option value="<html:rewrite page="/Person/Manage.do"/>"><bean:message key="person"/><bean:message key="referencing.entity.marker"/></option>
        </select>
    </nobr>
</div>

            </div>

            <div id="manageableList" class="table">
                <c:if test="${!empty manageableForm.manageableList}">
                    <display:table name="${manageableForm.manageableList}" id="row" requestURI="${pageContext.request.requestURI}"
                            requestURIcontext="false"
                            export="true" pagesize="15" sort="list">
                        <display:column media="html" sortable="false">
                            <nobr>
                                <input type="radio" name="_copy" onclick="enableUpdate(true);setFields('<formatting:escape language="javascript">${row.serial}</formatting:escape>','<formatting:escape language="javascript">${row.name}</formatting:escape>','<formatting:escape language="javascript">${row.type}</formatting:escape>','<formatting:escape language="javascript">${row.id}</formatting:escape>','<formatting:escape language="javascript">${row.owner}</formatting:escape>',this.form);"/>
                                <input type="checkbox" name="selectedRows" value="${row.id}" onclick="verifyEnableDelete(this);"/>
                            </nobr>
                        </display:column>
                        <display:column media="xml csv excel pdf"
                            property="serial"
                            titleKey="car.serial"/>
                        <display:column media="html"
                            headerClass="serial" paramId="serial" maxLength="36"
                            sortProperty="serial" sortable="true"
                            titleKey="car.serial"><nobr><formatting:escape language="javascript,html">${row.serial}</formatting:escape></nobr></display:column>
                        <display:column media="xml csv excel pdf"
                            property="name"
                            titleKey="car.name"/>
                        <display:column media="html"
                            headerClass="name" paramId="name" maxLength="36"
                            sortProperty="name" sortable="true"
                            titleKey="car.name"><nobr><formatting:escape language="javascript,html">${row.name}</formatting:escape></nobr></display:column>
                        <display:column media="xml csv excel pdf"
                            property="type"
                            titleKey="car.type"/>
                        <display:column media="html"
                            headerClass="type" paramId="type" maxLength="36"
                            sortProperty="type" sortable="true"
                            titleKey="car.type"><nobr><formatting:escape language="javascript,html">${row.type}</formatting:escape></nobr></display:column>
                        <display:column media="xml csv excel pdf"
                            property="ownerLabel"
                            titleKey="car.owner"/>
                        <display:column media="html"
                            headerClass="owner" paramId="owner" maxLength="36"
                            sortProperty="ownerLabel" sortable="true"
                            titleKey="car.owner"><nobr><formatting:escape language="javascript,html">${row.ownerLabel}</formatting:escape></nobr></display:column>
                    </display:table>
                </c:if>
            </div>

        </html:form>

        <div id="pageHelpSection">
            <blockquote>
                <a href="" id="pageHelp" style="display:inline;" onclick="openWindow('<html:rewrite action="/Car/ManageHelp"/>','onlinehelp',true,false,760,540); return false;">
                    <bean:message key="online.help.href"/>
                </a>
                <html:img page="/layout/help.gif" style="display:inline;"/>
            </blockquote>
        </div>

    </tiles:put>

</tiles:insert>

