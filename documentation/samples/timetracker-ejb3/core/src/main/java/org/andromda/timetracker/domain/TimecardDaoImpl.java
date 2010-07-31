// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @see org.andromda.timetracker.domain.Timecard
 */
public class TimecardDaoImpl
    extends org.andromda.timetracker.domain.TimecardDaoBase
{
    private Log logger = LogFactory.getLog(TimecardDaoImpl.class);
    
    /**
     * @see org.andromda.timetracker.domain.TimecardDao#findByCriteria(org.andromda.timetracker.vo.TimecardSearchCriteriaVO)
     */
    protected java.util.List handleFindByCriteria(org.andromda.timetracker.vo.TimecardSearchCriteriaVO criteria)
    {
        // Create the timecard criteria
        Criteria timecardCriteria = hibernateSession.createCriteria(Timecard.class);
        timecardCriteria.setFetchMode("submitter", FetchMode.JOIN);
        timecardCriteria.setFetchMode("approver", FetchMode.JOIN);
        
        // Add sumitter criteria
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
        if (logger.isDebugEnabled())
        {
            logger.debug(timecards.size() + " timecards found");
        }
        return timecards;
    }

    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(org.andromda.timetracker.domain.Timecard, org.andromda.timetracker.vo.TimecardSummaryVO)
     */
    public void toTimecardSummaryVO(
        org.andromda.timetracker.domain.Timecard sourceEntity, 
        org.andromda.timetracker.vo.TimecardSummaryVO targetVO)
    {
        // ${toDoTag} verify behavior of toTimecardSummaryVO
        super.toTimecardSummaryVO(sourceEntity, targetVO);
        
        // add the related entities
        targetVO.setSubmitterName(sourceEntity.getSubmitter().getUsername());
        if (sourceEntity.getApprover() != null)
        {
            targetVO.setApproverName(sourceEntity.getApprover().getUsername());
        }
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#toTimecardSummaryVO(org.andromda.timetracker.domain.Timecard)
     */
    public org.andromda.timetracker.vo.TimecardSummaryVO toTimecardSummaryVO(final org.andromda.timetracker.domain.Timecard entity)
    {
        // ${toDoTag} verify behavior of toTimecardSummaryVO
        return super.toTimecardSummaryVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store, 
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.Timecard loadTimecardFromTimecardSummaryVO(org.andromda.timetracker.vo.TimecardSummaryVO timecardSummaryVO)
    {
        // ${toDoTag} implement loadTimecardFromTimecardSummaryVO
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
        // ${toDoTag} verify behavior of timecardSummaryVOToEntity
        org.andromda.timetracker.domain.Timecard entity = this.loadTimecardFromTimecardSummaryVO(timecardSummaryVO);
        this.timecardSummaryVOToEntity(timecardSummaryVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.TimecardDao#timecardSummaryVOToEntity(org.andromda.timetracker.vo.TimecardSummaryVO, org.andromda.timetracker.domain.Timecard)
     */
    public void timecardSummaryVOToEntity(
        org.andromda.timetracker.vo.TimecardSummaryVO sourceVO,
        org.andromda.timetracker.domain.Timecard targetEntity,
        boolean copyIfNull)
    {
        // ${toDoTag} verify behavior of timecardSummaryVOToEntity
        super.timecardSummaryVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

}