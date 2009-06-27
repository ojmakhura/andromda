// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import java.util.List;

import org.andromda.timetracker.vo.TimecardSummaryVO;

/**
 * @see org.andromda.timetracker.service.TimeTrackingService
 */
public class TimeTrackingServiceImpl
    extends org.andromda.timetracker.service.TimeTrackingServiceBase
{
    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#findTimecards(org.andromda.timetracker.vo.TimecardSearchCriteriaVO)
     */
    protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleFindTimecards(org.andromda.timetracker.vo.TimecardSearchCriteriaVO criteria)
        throws java.lang.Exception
    {
        List timecards = getTimecardDao().findByCriteria(criteria);
        getTimecardDao().toTimecardSummaryVOCollection(timecards);
        return (TimecardSummaryVO[])timecards.toArray(new TimecardSummaryVO[0]);
    }
}