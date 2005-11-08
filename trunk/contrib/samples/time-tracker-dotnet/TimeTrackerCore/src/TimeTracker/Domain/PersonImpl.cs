// Name: PersonImpl.cs
// license-header cs merge-point
//
// This is only generated once! It will never be overwritten.
// You can (and have to!) safely modify it by hand.

using System;

namespace TimeTracker.Domain
{
    /// <summary>
    /// @see TimeTracker.Domain.Person
    /// </summary>
    public class PersonImpl
        : TimeTracker.Domain.Person
    {
        /// <summary>
        /// @see TimeTracker.Domain.Person#AddTimecard(TimeTracker.Domain.Timecard)
        /// </summary>
        public override void AddTimecard(TimeTracker.Domain.Timecard timecard)
        {
            Timecards.Add(timecard);
            timecard.Owner = this;
        }
    }
}
