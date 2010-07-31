using System;
using System.Collections.Generic;
using System.Text;
using System.Reflection;
namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This is a concrete implementation of the <see cref="IDataProvider"/> interface.
    /// This data provider uses helper methods written in the TestFixture class
    /// or a separate class to load input data for test cases.
    /// </summary>
    /// <remarks>
    /// This data provider uses reflection ot execute a helper method 
    /// on an object reference to load the test data for a specific scenario.
    /// This method of loading test data is almost the same as the mechanism used in 
    /// traditional unit testing where test data is separated out into helper methods.
    /// This data provider is useful when used in conjunction with the
    /// <see cref="XMLDataProviderAttribute"/> for input data types that cannot be serialized into XML
    /// and therefore cannot be loaded using <see cref="XMLDataProviderAttribute"/>.
    /// <para>
    /// This class derives from the <c>System.Attribute</c> class. This 
    /// allows this class to be configured as a data provider for a test method parameter.
    /// </para>
    /// <para>
    /// The AttributeUsage attribute for this class:
    /// <c>[AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]</c>
    ///  ensures that this attribute can be applied at a class, method or at a parameter level.
    /// </para>
    /// <para>
    /// When this attribute is specified at a <c>TestFixture</c> level, 
    /// all the test method parameters in that <c>TestFixture</c> are loaded using this data provider
    /// unless, if a different data provider is specified at a test method or parameter level.
    /// When this data provider is specified as an attribute on the test method, it
    /// overrides any data provider configured at the test fixture level.
    /// Similarly, when specified at the parameter level, this data provider is used to load that parameter, irrespective of the data providers configured at the test method or test fixture levels.
    /// </para>
    /// </remarks>
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]
    public sealed class CodeDataProviderAttribute
        : Attribute, IDataProvider
    {

        private string _dataMethodName;

        /// <summary>
        /// This is an optional property that specifies the name of the helper method to be called by
        /// this data provider to load input data.
        /// </summary>
        /// <remarks>
        /// If this property is not specified, this data provider uses a 
        /// naming convention to identify the helper method to
        /// be called.
        /// <para>
        /// The naming convention used is: 
        /// </para>
        ///<code escaped="true">
        /// [testMethodName]_[parameterName]_data()
        /// </code>
        /// <para>
        /// The helper method must be a private instance method that has the following signature:
        /// </para>
        /// <code escaped="true" lang="c#">
        /// private object [testMethodName]_[parameterName]_data(ParameterInfo pinfo, string methodName, string scenarioName)
        /// </code>
        /// </remarks>
        public string DataMethodName
        {
            get { return _dataMethodName; }
            set { _dataMethodName = value; }
        }

        /// <summary>
        /// This is the implementation of the <see cref="IDataProvider"/> interface.
        /// This implementation uses reflection to invoke a helper method on an object reference to load input data
        /// for a test scenario.
        /// </summary>
        /// <param name="pInfo">Meta information about the input parameter to be loaded.</param>
        /// <param name="methodName">The name of the test method whose input parameter needs to be loaded.
        /// This name is used in a naming convention to identify the helper method to be 
        /// called to load the input data.</param>
        /// <param name="scenarioName">The name of the test scenario for which test input data needs to be loaded.
        /// This name is passed to the helper method that returns input data.</param>
        /// <param name="testFixture">This is the object reference that contains the helper method that returns input data.
        /// Usually, this is a reference to the TestFixture itself.</param>
        /// <returns>Returns the loaded input data as returned from the helper method.</returns>
        /// <remarks>
        /// If the <see cref="DataMethodName"/>property is not specified, this data provider uses a 
        /// naming convention to identify the helper method to
        /// be called.
        /// <para>
        /// The naming convention used is: 
        /// </para>
        ///<code escaped="true">
        /// [testMethodName]_[parameterName]_data()
        /// </code>
        /// <para>
        /// The helper method must be a private instance method that has the following signature:
        /// </para>
        /// <code escaped="true" lang="c#">
        /// private object [testMethodName]_[parameterName]_data(ParameterInfo pinfo, string methodName, string scenarioName)
        /// </code>
        /// </remarks>
        public object GetData(ParameterInfo pInfo, string methodName, string scenarioName, object testFixture)
        {
            if (string.IsNullOrEmpty(DataMethodName))
            {
                DataMethodName = string.Format("{0}_{1}_Data", methodName, pInfo.Name);
            }
            try
            {
                MethodInfo mInfo = testFixture.GetType().GetMethod(DataMethodName, BindingFlags.NonPublic | BindingFlags.Instance | BindingFlags.IgnoreCase);
                object[] parameters = new object[] { pInfo, methodName, scenarioName };
                return mInfo.Invoke(testFixture, parameters);
            }
            catch (Exception e)
            {
                string errorMessage = string.Format("Could not load data for {0} from the method {1} for the test method {2} and scenario {3}.", pInfo.Name, DataMethodName, methodName, scenarioName);
                throw new DataProviderException(errorMessage, e);
            }
        }
    }
}
