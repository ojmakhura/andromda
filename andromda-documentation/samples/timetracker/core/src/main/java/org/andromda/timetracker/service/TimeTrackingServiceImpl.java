// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import java.util.List;
import org.andromda.timetracker.vo.TimecardSearchCriteriaVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;

/**
 * @see org.andromda.timetracker.service.TimeTrackingService
 */
public class TimeTrackingServiceImpl
    extends TimeTrackingServiceBase
{
    /**
     * @param criteria 
     * @return timecards
     * @throws Exception 
     * @see org.andromda.timetracker.service.TimeTrackingService#findTimecards(TimecardSearchCriteriaVO)
     */
    @Override
    protected TimecardSummaryVO[] handleFindTimecards(TimecardSearchCriteriaVO criteria)
        throws Exception
    {
        List timecards = getTimecardDao().findByCriteria(criteria);
        getTimecardDao().toTimecardSummaryVOCollection(timecards);
        return (TimecardSummaryVO[]) timecards.toArray(new TimecardSummaryVO[timecards.size()]);
    }
}