// Name: TaskImpl.cs
// license-header cs merge-point
//
// This is only generated once! It will never be overwritten.
// You can (and have to!) safely modify it by hand.

using System;

namespace TimeTracker.Domain
{
    /// <summary>
    /// @see TimeTracker.Domain.Task
    /// </summary>
    public class TaskImpl
        : TimeTracker.Domain.Task
    {
        /// <summary>
        /// @see TimeTracker.Domain.Task#AddTimeAllocation(TimeTracker.Domain.TimeAllocation)
        /// </summary>
        public override void AddTimeAllocation(TimeTracker.Domain.TimeAllocation timeAllocation)
        {
            Allocations.Add(timeAllocation);
            timeAllocation.Task = this;
        }
    }
}
