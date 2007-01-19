<%@ include file="/taglib-imports.jspf" %>

<tiles:insert definition="help.layout">

    <tiles:put name="title" type="string">
        <bean:message key="online.help.title"/>
        <bean:message key="car"/>
    </tiles:put>

    <tiles:put name="body" type="string">
        <h1><bean:message key="car"/></h1>
        <p>
            <bean:message key="car.online.help"/>
        </p>
        <h2><bean:message key="car.serial"/></h2>
        <p>
            <bean:message key="car.serial.online.help"/>
        </p>
        <h2><bean:message key="car.name"/></h2>
        <p>
            <bean:message key="car.name.online.help"/>
        </p>
        <h2><bean:message key="car.type"/></h2>
        <p>
            <bean:message key="car.type.online.help"/>
        </p>
        <h2><bean:message key="car.owner"/></h2>
        <p>
            <bean:message key="car.owner.online.help"/>
        </p>
    </tiles:put>

</tiles:insert>

