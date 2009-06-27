<%@ include file="/taglib-imports.jspf" %>

<%@include file="/layout/template-defs.jsp" %>

<tiles:insert beanName="default_layout_definition" beanScope="request" flush="true">

    <c:set var="tabId" value="SearchTimecards" scope="request"/>

    <tiles:put name="title" type="string">
        <bean:message key="search.timecards.title"/>
    </tiles:put>

    <tiles:put name="body_includes" type="string">
        <%@ include file="/org/andromda/timetracker/web/timecardsearch/search-timecards-vars.jspf" %>
    </tiles:put>

    <%@ include file="/org/andromda/timetracker/web/timecardsearch/search-timecards-javascript.jspf" %>

    <tiles:put name="content" type="string">
        <div class="sidebar">
            <%@ include file="/org/andromda/timetracker/web/timecardsearch/search-timecards-search.jspf" %>
        </div>
        <div>
            <%@ include file="/org/andromda/timetracker/web/timecardsearch/search-timecards-timecardSummaries.jspf" %>
        </div>
    </tiles:put>

</tiles:insert>