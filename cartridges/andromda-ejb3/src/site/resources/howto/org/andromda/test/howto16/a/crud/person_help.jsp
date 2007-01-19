<%@ include file="/taglib-imports.jspf" %>

<tiles:insert definition="help.layout">

    <tiles:put name="title" type="string">
        <bean:message key="online.help.title"/>
        <bean:message key="person"/>
    </tiles:put>

    <tiles:put name="body" type="string">
        <h1><bean:message key="person"/></h1>
        <p>
            <bean:message key="person.online.help"/>
        </p>
        <h2><bean:message key="person.name"/></h2>
        <p>
            <bean:message key="person.name.online.help"/>
        </p>
        <h2><bean:message key="person.birth.date"/></h2>
        <p>
            <bean:message key="person.birth.date.online.help"/>
        </p>
        <h2><bean:message key="person.cars"/></h2>
        <p>
            <bean:message key="person.cars.online.help"/>
        </p>
    </tiles:put>

</tiles:insert>

