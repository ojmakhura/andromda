using System;

namespace AndroMDA.ScenarioUnit
{
    public class DataProviderException : Exception
    {
        public DataProviderException(string message, Exception e)
            : base(message, e)
        { }

    }
}
