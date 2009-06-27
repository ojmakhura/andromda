// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import java.util.Collection;

import org.andromda.timetracker.domain.Task;
import org.andromda.timetracker.domain.TaskDao;
import org.andromda.timetracker.domain.TimeAllocation;
import org.andromda.timetracker.domain.Timecard;
import org.andromda.timetracker.domain.TimecardDao;
import org.andromda.timetracker.vo.TaskVO;
import org.andromda.timetracker.vo.TimeAllocationVO;
import org.andromda.timetracker.vo.TimecardSummaryVO;
import org.andromda.timetracker.vo.TimecardVO;

/**
 * @see org.andromda.timetracker.service.TimeTrackingService
 */
public class TimeTrackingServiceImpl
    extends org.andromda.timetracker.service.TimeTrackingServiceBase
{

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#createTimecard(org.andromda.timetracker.vo.TimecardVO)
     */
    protected java.lang.Long handleCreateTimecard(org.andromda.timetracker.vo.TimecardVO timecardVO)
        throws java.lang.Exception
    {
        // Create new timecard from timecardVO
        Timecard timecard = Timecard.Factory.newInstance();
        getTimecardDao().timecardVOToEntity(timecardVO, timecard, true);

        // Set submitter and approver associations
        timecard.setSubmitter(getPersonDao().findByUsername(timecardVO.getSubmitterName()));
        timecard.setApprover(getPersonDao().findByUsername(timecardVO.getApproverName()));

        // Set allocations
        TimeAllocationVO allocations[] = timecardVO.getAllocations();
        for (int i=0; i<allocations.length; i++) {
            // Create TimeAllocation from TimeAllocationVO
            TimeAllocationVO allocationVO = allocations[i];
            TimeAllocation allocation = TimeAllocation.Factory.newInstance();
            getTimeAllocationDao().timeAllocationVOToEntity(allocationVO, allocation, true);

            // Connect to timecard
            timecard.addTimeAllocation(allocation);

            // Connect to task
            allocation.setTask(getTaskDao().load(allocationVO.getTaskId()));
        }

        // Create the timecard
        getTimecardDao().create(timecard);
        return timecard.getId();
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#updateTimecard(org.andromda.timetracker.vo.TimecardVO)
     */
    protected void handleUpdateTimecard(org.andromda.timetracker.vo.TimecardVO timecardVO)
        throws java.lang.Exception
    {
        // @todo implement protected void handleUpdateTimecard(org.andromda.timetracker.vo.TimecardVO timecardVO)
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.service.TimeTrackingService.handleUpdateTimecard(org.andromda.timetracker.vo.TimecardVO timecardVO) Not implemented!");
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#submitTimecard(java.lang.Long)
     */
    protected void handleSubmitTimecard(java.lang.Long timecardId)
        throws java.lang.Exception
    {
        // @todo implement protected void handleSubmitTimecard(java.lang.Long timecardId)
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.service.TimeTrackingService.handleSubmitTimecard(java.lang.Long timecardId) Not implemented!");
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#approveTimecard(java.lang.Long)
     */
    protected void handleApproveTimecard(java.lang.Long timecardId)
        throws java.lang.Exception
    {
        // @todo implement protected void handleApproveTimecard(java.lang.Long timecardId)
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.service.TimeTrackingService.handleApproveTimecard(java.lang.Long timecardId) Not implemented!");
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#getTimecard(java.lang.Long)
     */
    protected org.andromda.timetracker.vo.TimecardVO handleGetTimecard(java.lang.Long id)
        throws java.lang.Exception
    {
        return (TimecardVO)getTimecardDao().load(TimecardDao.TRANSFORM_TIMECARDVO, id);
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#getAllTimecardSummaries()
     */
    protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleGetAllTimecardSummaries()
        throws java.lang.Exception
    {
        Collection timecards = getTimecardDao().loadAll(TimecardDao.TRANSFORM_TIMECARDSUMMARYVO);
        return (TimecardSummaryVO[])timecards.toArray(new TimecardSummaryVO[timecards.size()]);
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#getTimecardSummariesForSubmitter(java.lang.String)
     */
    protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleGetTimecardSummariesForSubmitter(java.lang.String submitterName)
        throws java.lang.Exception
    {
        // @todo implement protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleGetTimecardSummariesForSubmitter(java.lang.String submitterName)
        return null;
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#getTimecardSummariesForApprover(java.lang.String)
     */
    protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleGetTimecardSummariesForApprover(java.lang.String approverName)
        throws java.lang.Exception
    {
        // @todo implement protected org.andromda.timetracker.vo.TimecardSummaryVO[] handleGetTimecardSummariesForApprover(java.lang.String submitterName)
        return null;
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#createTask(org.andromda.timetracker.vo.TaskVO)
     */
    protected java.lang.Long handleCreateTask(org.andromda.timetracker.vo.TaskVO taskVO)
        throws java.lang.Exception
    {
        Task task = Task.Factory.newInstance();
        getTaskDao().taskVOToEntity(taskVO, task, true);
        getTaskDao().create(task);
        return task.getId();
    }

    /**
     * @see org.andromda.timetracker.service.TimeTrackingService#getAllTasks()
     */
    protected org.andromda.timetracker.vo.TaskVO[] handleGetAllTasks()
        throws java.lang.Exception
    {
        Collection tasks = getTaskDao().loadAll(TaskDao.TRANSFORM_TASKVO);
        return (TaskVO[])tasks.toArray(new TaskVO[tasks.size()]);
    }
}