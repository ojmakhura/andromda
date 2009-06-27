// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.timetracker.ServiceLocator;
import org.andromda.timetracker.vo.TimeAllocationVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.andromda.timetracker.vo.TimecardVO;

/**
 * @see org.andromda.timetracker.domain.Timecard
 */
public class TimecardDaoImpl
    extends org.andromda.timetracker.domain.TimecardDaoBase
{
    /**
     * @see @see org.andromda.timetracker.domain.TimecardDao#toTimecardVO(org.andromda.timetracker.domain.Timecard, org.andromda.timetracker.vo.TimecardVO)
     */
    public void toTimecardVO(
        org.andromda.timetracker.domain.Timecard sourceEntity,
        org.andromda.timetracker.vo.TimecardVO targetVO)
    {
        toTimecardSummaryVO(sourceEntity, targetVO);

        // Create allocations
        Collection allocations = new ArrayList(sourceEntity.getAllocations());
        ((TimeAllocationDao)ServiceLocator.instance().getService("timeAllocationDao"))
            .toTimeAllocationVOCollection(allocations);
        targetVO.setAllocations((TimeAllocationVO[])allocations
            .toArray(new TimeAllocationVO[allocations.size()]));
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardVO(org.andromda.timetracker.domain.Timecard)
     */
    public org.andromda.timetracker.vo.TimecardVO toTimecardVO(final org.andromda.timetracker.domain.Timecard entity)
    {
        TimecardVO targetVO = new TimecardVO();
        toTimecardVO(entity, targetVO);
        return targetVO;
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.Timecard loadTimecardFromTimecardVO(org.andromda.timetracker.vo.TimecardVO timecardVO)
    {
        // @todo implement loadTimecardFromTimecardVO
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.domain.loadTimecardFromTimecardVO(org.andromda.timetracker.vo.TimecardVO) not yet implemented.");

        /* A typical implementation looks like this:
        org.andromda.timetracker.domain.Timecard timecard = this.load(timecardVO.getId());
        if (timecard == null)
        {
            timecard = org.andromda.timetracker.domain.Timecard.Factory.newInstance();
        }
        return timecard;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardVOToEntity(org.andromda.timetracker.vo.TimecardVO)
     */
    public org.andromda.timetracker.domain.Timecard timecardVOToEntity(org.andromda.timetracker.vo.TimecardVO timecardVO)
    {
        // @todo verify behavior of timecardVOToEntity
        org.andromda.timetracker.domain.Timecard timecard = this.loadTimecardFromTimecardVO(timecardVO);
        this.timecardVOToEntity(timecardVO, timecard, true);
        return timecard;
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardVOToEntity(org.andromda.timetracker.vo.TimecardVO, org.andromda.timetracker.domain.Timecard)
     */
    public void timecardVOToEntity(
        org.andromda.timetracker.vo.TimecardVO sourceVO,
        org.andromda.timetracker.domain.Timecard targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of timecardVOToEntity
        super.timecardVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    /**
     * @see @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(org.andromda.timetracker.domain.Timecard, org.andromda.timetracker.vo.TimecardSummaryVO)
     */
    public void toTimecardSummaryVO(
        org.andromda.timetracker.domain.Timecard sourceEntity,
        org.andromda.timetracker.vo.TimecardSummaryVO targetVO)
    {
        super.toTimecardSummaryVO(sourceEntity, targetVO);
        targetVO.setSubmitterName(sourceEntity.getSubmitter().getUsername());
        targetVO.setApproverName(sourceEntity.getApprover().getUsername());
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(org.andromda.timetracker.domain.Timecard)
     */
    public org.andromda.timetracker.vo.TimecardSummaryVO toTimecardSummaryVO(final org.andromda.timetracker.domain.Timecard entity)
    {
        TimecardSummaryVO targetVO = new TimecardSummaryVO();
        toTimecardSummaryVO(entity, targetVO);
        return targetVO;
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.Timecard loadTimecardFromTimecardSummaryVO(org.andromda.timetracker.vo.TimecardSummaryVO timecardSummaryVO)
    {
        // @todo implement loadTimecardFromTimecardSummaryVO
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.domain.loadTimecardFromTimecardSummaryVO(org.andromda.timetracker.vo.TimecardSummaryVO) not yet implemented.");

        /* A typical implementation looks like this:
        org.andromda.timetracker.domain.Timecard timecard = this.load(timecardSummaryVO.getId());
        if (timecard == null)
        {
            timecard = org.andromda.timetracker.domain.Timecard.Factory.newInstance();
        }
        return timecard;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardSummaryVOToEntity(org.andromda.timetracker.vo.TimecardSummaryVO)
     */
    public org.andromda.timetracker.domain.Timecard timecardSummaryVOToEntity(org.andromda.timetracker.vo.TimecardSummaryVO timecardSummaryVO)
    {
        // @todo verify behavior of timecardSummaryVOToEntity
        org.andromda.timetracker.domain.Timecard timecard = this.loadTimecardFromTimecardSummaryVO(timecardSummaryVO);
        this.timecardSummaryVOToEntity(timecardSummaryVO, timecard, true);
        return timecard;
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardSummaryVOToEntity(org.andromda.timetracker.vo.TimecardSummaryVO, org.andromda.timetracker.domain.Timecard)
     */
    public void timecardSummaryVOToEntity(
        org.andromda.timetracker.vo.TimecardSummaryVO sourceVO,
        org.andromda.timetracker.domain.Timecard targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of timecardSummaryVOToEntity
        super.timecardSummaryVOToEntity(sourceVO, targetEntity, copyIfNull);
    }
}