// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.List;

import org.andromda.timetracker.vo.TimecardSearchCriteriaVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @see Timecard
 */
public class TimecardDaoImpl
    extends TimecardDaoBase
{
    private Log logger = LogFactory.getLog(TimecardDaoImpl.class);

    /**
     * @see TimecardDao#findByCriteria(TimecardSearchCriteriaVO)
     */
    @Override
    protected List handleFindByCriteria(TimecardSearchCriteriaVO criteria)
    {
        // Create the timecard criteria
        Criteria timecardCriteria = this.hibernateSession.createCriteria(Timecard.class);
        timecardCriteria.setFetchMode("submitter", FetchMode.JOIN);
        timecardCriteria.setFetchMode("approver", FetchMode.JOIN);

        // Add submitter criteria
        if (criteria.getSubmitterId() != null)
        {
            timecardCriteria.createCriteria("submitter").add(Restrictions.idEq(criteria.getSubmitterId()));
        }

        // Add approver criteria
        if (criteria.getApproverId() != null)
        {
            timecardCriteria.createCriteria("approver").add(Restrictions.idEq(criteria.getApproverId()));
        }

        // Add status criteria
        if (criteria.getStatus() != null)
        {
            timecardCriteria.add(Restrictions.eq("status", criteria.getStatus()));
        }

        // Add startDateMin criteria
        if (criteria.getStartDateMin() != null)
        {
            timecardCriteria.add(Restrictions.ge("startDate", criteria.getStartDateMin()));
        }

        // Add startDateMax criteria
        if (criteria.getStartDateMax() != null)
        {
            timecardCriteria.add(Restrictions.le("startDate", criteria.getStartDateMax()));
        }

        List timecards = timecardCriteria.list();
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug(timecards.size() + " timecards found");
        }
        return timecards;
    }

    /**
     * @see TimecardDao#toTimecardSummaryVO(Timecard, TimecardSummaryVO)
     */
    @Override
    public void toTimecardSummaryVO(
        Timecard sourceEntity,
        TimecardSummaryVO targetVO)
    {
        // TODO verify behavior of toTimecardSummaryVO
        super.toTimecardSummaryVO(sourceEntity, targetVO);

        // add the related entities
        targetVO.setSubmitterName(sourceEntity.getSubmitter().getUsername());
        if (sourceEntity.getApprover() != null)
        {
            targetVO.setApproverName(sourceEntity.getApprover().getUsername());
        }
    }


    /**
     * @see TimecardDao#toTimecardSummaryVO(Timecard)
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
    @SuppressWarnings("static-method")
    private Timecard loadTimecardFromTimecardSummaryVO(TimecardSummaryVO timecardSummaryVO)
    {
        // TODO implement loadTimecardFromTimecardSummaryVO
        throw new java.lang.UnsupportedOperationException("loadTimecardFromTimecardSummaryVO(TimecardSummaryVO) not yet implemented.");

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
     * @see TimecardDao#timecardSummaryVOToEntity(TimecardSummaryVO)
     */
    @Override
    public Timecard timecardSummaryVOToEntity(TimecardSummaryVO timecardSummaryVO)
    {
        // TODO verify behavior of timecardSummaryVOToEntity
        Timecard entity = this.loadTimecardFromTimecardSummaryVO(timecardSummaryVO);
        this.timecardSummaryVOToEntity(timecardSummaryVO, entity, true);
        return entity;
    }

    /**
     * @see TimecardDao#timecardSummaryVOToEntity(TimecardSummaryVO, Timecard, boolean)
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
}