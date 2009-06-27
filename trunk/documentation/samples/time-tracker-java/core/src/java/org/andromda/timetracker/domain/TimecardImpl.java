// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

/**
 * @see org.andromda.timetracker.domain.Timecard
 */
public class TimecardImpl
    extends org.andromda.timetracker.domain.Timecard
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 4974757278671085699L;

    /**
     * @see org.andromda.timetracker.domain.Timecard#addTimeAllocation(org.andromda.timetracker.domain.TimeAllocation)
     */
    public void addTimeAllocation(org.andromda.timetracker.domain.TimeAllocation timeAllocation)
    {
        getAllocations().add(timeAllocation);
        timeAllocation.setTimecard(this);
    }
}