<%@ include file="/taglib-imports.jspf" %>

<%@include file="/layout/template-defs.jsp" %>

<tiles:insert beanName="default_layout_definition" beanScope="request" flush="true">

    <c:set var="tabId" value="Home" scope="request"/>

    <tiles:put name="title" type="string">
        <bean:message key="time.tracker.home.title"/>
    </tiles:put>

    <%@ include file="/org/andromda/timetracker/web/timetrackerhome/time-tracker-home-javascript.jspf" %>

    <tiles:put name="content" type="string">
        <h2><bean:message key="time.tracker.welcome"/></h2>
        <!--
            The table looks much better than separating the links with breaks or
            putting them in a list.
        -->
        <table>
            <tbody>
                <tr>
                    <td>
                        <html:link action="/TimeTrackerHome/TimeTrackerHomeNewTimecard">
                            <bean:message key="new.timecard.link"/>
                        </html:link>
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:link action="/TimeTrackerHome/TimeTrackerHomeLatestTimecard">
                            <bean:message key="latest.timecard.link"/>
                        </html:link>
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:link action="/TimeTrackerHome/TimeTrackerHomeSearchTimecards">
                            <bean:message key="search.timecards.link"/>
                        </html:link>
                    </td>
                </tr>
                <tr>
                    <td>
                        <html:link action="/TimeTrackerHome/TimeTrackerHomeApproveTimecards">
                            <bean:message key="approve.timecards.link"/>
                        </html:link>
                    </td>
                </tr>
            </tbody>
        </table>
    </tiles:put>

</tiles:insert>
