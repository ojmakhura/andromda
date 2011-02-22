// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.List;
import org.andromda.timetracker.vo.TimecardSearchCriteriaVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.andromda.timetracker.vo.TimecardVO;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @see Timecard
 */
public class TimecardDaoImpl
    extends TimecardDaoBase
{
    /**
     * @see org.andromda.timetracker.domain.TimecardDao#findByCriteria(TimecardSearchCriteriaVO)
     */
    @Override
    protected List handleFindByCriteria(TimecardSearchCriteriaVO criteria)
    {
        // Create the timecard criteria
        Criteria timecardCriteria = this.getSession()
            .createCriteria(Timecard.class)
            .setFetchMode("submitter", FetchMode.JOIN)
            .setFetchMode("approver", FetchMode.JOIN);

        // Add submitter criteria
        if (criteria.getSubmitterId() != null) {
            timecardCriteria.createCriteria("submitter")
                .add(Restrictions.idEq(criteria.getSubmitterId()));
        }

        // Add approver criteria
        if (criteria.getApproverId() != null) {
            timecardCriteria.createCriteria("approver")
                .add(Restrictions.idEq(criteria.getApproverId()));
        }

        // Add status criteria
        if (criteria.getStatus() != null) {
            timecardCriteria.add(Restrictions.eq("status", criteria.getStatus()));
        }

        // Add startDateMin criteria
        if (criteria.getStartDateMin() != null) {
            timecardCriteria.add(Restrictions.ge("startDate", criteria.getStartDateMin()));
        }

        // Add startDateMax criteria
        if (criteria.getStartDateMax() != null) {
            timecardCriteria.add(Restrictions.le("startDate", criteria.getStartDateMax()));
        }

        List timecards = timecardCriteria.list();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(timecards.size() + " timecards found");
        }
        return timecards;
    }

    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(Timecard, TimecardSummaryVO)
     */
    @Override
    public void toTimecardSummaryVO(
        Timecard sourceEntity,
        TimecardSummaryVO targetVO)
    {
        super.toTimecardSummaryVO(sourceEntity, targetVO);
        targetVO.setSubmitterName(sourceEntity.getSubmitter().getUsername());
        if (sourceEntity.getApprover() != null) {
            targetVO.setApproverName(sourceEntity.getApprover().getUsername());
        }
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(Timecard)
     */
    @Override
    public TimecardSummaryVO toTimecardSummaryVO(final Timecard entity)
    {
        // TODO verify behavior of toTimecardSummaryVO
        return super.toTimecardSummaryVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private Timecard loadTimecardFromTimecardSummaryVO(TimecardSummaryVO timecardSummaryVO)
    {
        // TODO implement loadTimecardFromTimecardSummaryVO
        throw new UnsupportedOperationException("org.andromda.timetracker.domain.loadTimecardFromTimecardSummaryVO(TimecardSummaryVO) not yet implemented.");

        /* A typical implementation looks like this:
        Timecard timecard = this.load(timecardSummaryVO.getId());
        if (timecard == null)
        {
            timecard = Timecard.Factory.newInstance();
        }
        return timecard;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardSummaryVOToEntity(TimecardSummaryVO)
     */
    public Timecard timecardSummaryVOToEntity(TimecardSummaryVO timecardSummaryVO)
    {
        // TODO verify behavior of timecardSummaryVOToEntity
        Timecard entity = this.loadTimecardFromTimecardSummaryVO(timecardSummaryVO);
        this.timecardSummaryVOToEntity(timecardSummaryVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardSummaryVOToEntity(TimecardSummaryVO, Timecard, boolean)
     */
    @Override
    public void timecardSummaryVOToEntity(
        TimecardSummaryVO sourceVO,
        Timecard targetEntity,
        boolean copyIfNull)
    {
        // TODO verify behavior of timecardSummaryVOToEntity
        super.timecardSummaryVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardVO(Timecard, TimecardVO)
     */
    @Override
    public void toTimecardVO(
        Timecard sourceEntity,
        TimecardVO targetVO)
    {
        // TODO verify behavior of toTimecardVO
        super.toTimecardVO(sourceEntity, targetVO);
        // WARNING! No conversion for targetVO.allocations (can't convert sourceEntity.getAllocations():org.andromda.timetracker.domain.TimeAllocation to org.andromda.timetracker.vo.TimeAllocationVO[]
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardVO(Timecard)
     */
    @Override
    public TimecardVO toTimecardVO(final Timecard entity)
    {
        // TODO verify behavior of toTimecardVO
        return super.toTimecardVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private Timecard loadTimecardFromTimecardVO(TimecardVO timecardVO)
    {
        // TODO implement loadTimecardFromTimecardVO
        throw new UnsupportedOperationException("org.andromda.timetracker.domain.loadTimecardFromTimecardVO(TimecardVO) not yet implemented.");

        /* A typical implementation looks like this:
        Timecard timecard = this.load(timecardVO.getId());
        if (timecard == null)
        {
            timecard = Timecard.Factory.newInstance();
        }
        return timecard;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardVOToEntity(TimecardVO)
     */
    public Timecard timecardVOToEntity(TimecardVO timecardVO)
    {
        // TODO verify behavior of timecardVOToEntity
        Timecard entity = this.loadTimecardFromTimecardVO(timecardVO);
        this.timecardVOToEntity(timecardVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardVOToEntity(TimecardVO, Timecard, boolean)
     */
    @Override
    public void timecardVOToEntity(
        TimecardVO sourceVO,
        Timecard targetEntity,
        boolean copyIfNull)
    {
        // TODO verify behavior of timecardVOToEntity
        super.timecardVOToEntity(sourceVO, targetEntity, copyIfNull);
    }
}
