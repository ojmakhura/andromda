<%@ include file="/taglib-imports.jspf" %>

<ul>
    <%-- Home --%>
    <li class="first">
        <c:set var="title"><bean:message key="time.tracker.home"/></c:set>
        <c:set var="action" value="/TimeTrackerHome/TimeTrackerHome"/>
        <c:choose>
            <c:when test="${tabId == 'Home'}">
                <html:link styleClass="selected" action="${action}">${title}</html:link>
            </c:when>
            <c:otherwise>
                <html:link action="${action}">${title}</html:link>
            </c:otherwise>
        </c:choose>
    </li>

    <%-- Timecard Details --%>
    <li>
        <c:set var="title"><bean:message key="timecard.details.title"/></c:set>
        <c:set var="action" value="/TimeTrackerHome/TimeTrackerHome"/>
        <c:choose>
            <c:when test="${tabId == 'TimecardDetails'}">
                <html:link styleClass="selected" action="${action}">${title}</html:link>
            </c:when>
            <c:otherwise>
                <html:link action="${action}">${title}</html:link>
            </c:otherwise>
        </c:choose>
    </li>

    <%-- Search Timecards --%>
    <li>
        <c:set var="title"><bean:message key="search.timecards.title"/></c:set>
        <c:set var="action" value="/TimeTrackerHome/TimeTrackerHomeSearchTimecards"/>
        <c:choose>
            <c:when test="${tabId == 'SearchTimecards'}">
                <html:link styleClass="selected" action="${action}">${title}</html:link>
            </c:when>
            <c:otherwise>
                <html:link action="${action}">${title}</html:link>
            </c:otherwise>
        </c:choose>
    </li>

    <%-- Approve Timecards --%>
    <li>
        <c:set var="title"><bean:message key="approve.timecards.title"/></c:set>
        <c:set var="action" value="/TimeTrackerHome/TimeTrackerHome"/>
        <c:choose>
            <c:when test="${tabId == 'ApproveTimecards'}">
                <html:link styleClass="selected" action="${action}">${title}</html:link>
            </c:when>
            <c:otherwise>
                <html:link action="${action}">${title}</html:link>
            </c:otherwise>
        </c:choose>
    </li>
</ul>