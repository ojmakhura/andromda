<%@ include file="/taglib-imports.jspf" %>
<tiles:insert definition="main.layout">

    <tiles:put name="title" type="string">
        <bean:message key="person.page.title"/>
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

            function setAction(crud) { document.forms['managePersonForm'].elements['crud'].value = crud; }

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
                setFields("","","","[]",form);
            }

            function setFields(name,birthDate,id,cars,form)
            {
                form.elements["name"].value = name;
                form.elements["birthDateAsString"].value = birthDate;
                form.elements["id"].value = id;
                setSelect(true,form,"cars",cars.substring(1,cars.length-1).split(", "));
            }
        //-->
        </script>
    </tiles:put>

    <tiles:put name="body" type="string">

        <div>
            <h1><bean:message key="person.page.title"/></h1>
        </div>

        <html:form styleId="managePersonForm" action="/Person/Manage" method="post">
            <input type="hidden" name="crud" value=""/>
            <div id="criteria">
                <c:if test="${!empty manageableForm}">
                    <table>
                        <tr>
                            <td><nobr><bean:message key="person.name"/> <div class="important">*</div></nobr></td>
                            <td>
                                <html:text name="manageableForm" property="name" styleClass="criteriaField" styleId="name"/>
                            </td>
                        </tr>
                        <tr>
                            <td><nobr><bean:message key="person.birth.date"/> <div class="important">*</div></nobr></td>
                            <td>
                                <html:text name="manageableForm" property="birthDateAsString" styleClass="criteriaField" styleId="birthDate"/>
                                <html:img page="/layout/calendar/calendar.gif" styleId="birthDate_trigger" styleClass="calendar"/>
                                <script type="text/javascript">
                                //<!--
                                    Calendar.setup({
                                        inputField     :    "birthDate",
                                        ifFormat       :    "%d/%m/%Y",
                                        daFormat       :    "%d/%m/%Y",
                                        showsTime      :    false,
                                        button         :    "birthDate_trigger",
                                        singleClick    :    true
                                    });
                                //-->
                                </script>
                            </td>
                        </tr>
                    <html:hidden name="manageableForm" property="id"/>
                        <tr>
                            <td>
                                    <html:link action="/Car/Manage"><bean:message key="person.cars"/></html:link>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty manageableForm.carsBackingList}">
                                        <select name="cars" disabled="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="cars" multiple="multiple">
                                            <c:forEach var="valueLabel" items="${manageableForm.carsBackingList}">
                        <option value="${valueLabel[0]}"<collections:contains item="${valueLabel}" array="${manageableForm.cars}"> selected="selected"</collections:contains>>${valueLabel[1]}</option>
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
        <select onchange="document.location=this.options[this.selectedIndex].value+'?ref_Person='+this.form.elements['id'].value;">
            <option value="<html:rewrite page="/Car/Manage.do"/>"><bean:message key="car"/></option>
            <option selected="selected" value="<html:rewrite page="/Person/Manage.do"/>"><bean:message key="person"/></option>
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
                            <fmt:formatDate var="birthDateFormatted" value="${row.birthDate}" pattern="dd/MM/yyyy"/>
                            <nobr>
                                <input type="radio" name="_copy" onclick="enableUpdate(true);setFields('<formatting:escape language="javascript">${row.name}</formatting:escape>','<formatting:escape language="javascript">${birthDateFormatted}</formatting:escape>','<formatting:escape language="javascript">${row.id}</formatting:escape>','<formatting:escape language="javascript">${row.cars}</formatting:escape>',this.form);"/>
                                <input type="checkbox" name="selectedRows" value="${row.id}" onclick="verifyEnableDelete(this);"/>
                            </nobr>
                        </display:column>
                        <display:column media="xml csv excel pdf"
                            property="name"
                            titleKey="person.name"/>
                        <display:column media="html"
                            headerClass="name" paramId="name" maxLength="36"
                            sortProperty="name" sortable="true"
                            titleKey="person.name"><nobr><formatting:escape language="javascript,html">${row.name}</formatting:escape></nobr></display:column>
                        <display:column media="xml csv excel pdf" titleKey="person.birth.date">${birthDateFormatted}</display:column>
                        <display:column media="html"
                            headerClass="birthDate" paramId="birthDate" maxLength="36"
                            sortProperty="birthDate" sortable="true"
                            titleKey="person.birth.date"><nobr>${birthDateFormatted}</nobr></display:column>
                        <display:column media="xml csv excel pdf"
                            property="carsLabels"
                            titleKey="person.cars"/>
                        <display:column media="html"
                            headerClass="cars" paramId="cars" maxLength="36"
                            sortProperty="carsLabels" sortable="true"
                            titleKey="person.cars"><nobr><formatting:escape language="javascript,html">${row.carsLabels}</formatting:escape></nobr></display:column>
                    </display:table>
                </c:if>
            </div>

        </html:form>

        <div id="pageHelpSection">
            <blockquote>
                <a href="" id="pageHelp" style="display:inline;" onclick="openWindow('<html:rewrite action="/Person/ManageHelp"/>','onlinehelp',true,false,760,540); return false;">
                    <bean:message key="online.help.href"/>
                </a>
                <html:img page="/layout/help.gif" style="display:inline;"/>
            </blockquote>
        </div>

    </tiles:put>

</tiles:insert>

