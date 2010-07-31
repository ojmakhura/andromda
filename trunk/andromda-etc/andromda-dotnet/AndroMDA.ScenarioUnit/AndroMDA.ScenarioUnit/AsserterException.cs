using System;

namespace AndroMDA.ScenarioUnit
{
    public class AsserterException : Exception
    {
        public AsserterException(string message, Exception e)
            : base(message, e)
        { }

    }
}
