// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import org.andromda.timetracker.vo.TimeAllocationVO;
import org.andromda.timetracker.vo.TimePeriodVO;

/**
 * @see org.andromda.timetracker.domain.TimeAllocation
 */
public class TimeAllocationDaoImpl
    extends org.andromda.timetracker.domain.TimeAllocationDaoBase
{
    /**
     * @see @see org.andromda.timetracker.domain.TimeAllocationDao#toTimeAllocationVO(org.andromda.timetracker.domain.TimeAllocation, org.andromda.timetracker.vo.TimeAllocationVO)
     */
    public void toTimeAllocationVO(
        org.andromda.timetracker.domain.TimeAllocation sourceEntity, 
        org.andromda.timetracker.vo.TimeAllocationVO targetVO)
    {
        super.toTimeAllocationVO(sourceEntity, targetVO);
        TimePeriod timePeriod = sourceEntity.getTimePeriod();
        targetVO.setTimePeriodVO(
            new TimePeriodVO(timePeriod.getStartTime(), timePeriod.getEndTime()));
        targetVO.setTaskId(sourceEntity.getTask().getId());
    }


    /**
     * @see org.andromda.timetracker.domain.TimeAllocationDao#toTimeAllocationVO(org.andromda.timetracker.domain.TimeAllocation)
     */
    public org.andromda.timetracker.vo.TimeAllocationVO toTimeAllocationVO(final org.andromda.timetracker.domain.TimeAllocation entity)
    {
        TimeAllocationVO targetVO = new TimeAllocationVO();
        toTimeAllocationVO(entity, targetVO);
        return targetVO;
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store, 
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.TimeAllocation loadTimeAllocationFromTimeAllocationVO(org.andromda.timetracker.vo.TimeAllocationVO timeAllocationVO)
    {
        // @todo implement loadTimeAllocationFromTimeAllocationVO
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.domain.loadTimeAllocationFromTimeAllocationVO(org.andromda.timetracker.vo.TimeAllocationVO) not yet implemented.");

        /* A typical implementation looks like this:        
        org.andromda.timetracker.domain.TimeAllocation timeAllocation = this.load(timeAllocationVO.getId());
        if (timeAllocation == null)
        {
            timeAllocation = org.andromda.timetracker.domain.TimeAllocation.Factory.newInstance();
        }
        return timeAllocation;
        */
    }

    
    /**
     * @see org.andromda.timetracker.domain.TimeAllocationDao#timeAllocationVOToEntity(org.andromda.timetracker.vo.TimeAllocationVO)
     */
    public org.andromda.timetracker.domain.TimeAllocation timeAllocationVOToEntity(org.andromda.timetracker.vo.TimeAllocationVO timeAllocationVO)
    {
        // @todo verify behavior of timeAllocationVOToEntity
        org.andromda.timetracker.domain.TimeAllocation timeAllocation = this.loadTimeAllocationFromTimeAllocationVO(timeAllocationVO);
        this.timeAllocationVOToEntity(timeAllocationVO, timeAllocation, true);
        return timeAllocation;
    }


    /**
     * @see org.andromda.timetracker.domain.TimeAllocationDao#timeAllocationVOToEntity(org.andromda.timetracker.vo.TimeAllocationVO, org.andromda.timetracker.domain.TimeAllocation)
     */
    public void timeAllocationVOToEntity(
        org.andromda.timetracker.vo.TimeAllocationVO sourceVO,
        org.andromda.timetracker.domain.TimeAllocation targetEntity,
        boolean copyIfNull)
    {
        super.timeAllocationVOToEntity(sourceVO, targetEntity, copyIfNull);
        TimePeriodVO timePeriodVO = sourceVO.getTimePeriodVO();
        targetEntity.setTimePeriod(
            TimePeriod.newInstance(timePeriodVO.getStartTime(), timePeriodVO.getEndTime()));
    }
}