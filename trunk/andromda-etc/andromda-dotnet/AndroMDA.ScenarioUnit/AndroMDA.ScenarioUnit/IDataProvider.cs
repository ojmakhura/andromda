using System;
using System.Collections.Generic;
using System.Text;
using System.Reflection;

namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This is the interface implemented by all data providers.
    /// </summary>
    /// <remarks>
    /// A data provider is used by the <see cref="TestScenarioHelper"/> class to load the input data for 
    /// a test scenario and pass it to a test method.
    /// <para>
    /// A data provider that implements this interface needs to 
    /// derive from the <see cref="System.Attribute"/> class so that it can
    /// be configured to be used to read input data.
    /// <para>
    /// The data provider should use the following<c>AttributeUsage</c> tag:
    /// <para>
    /// <c>[AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]</c>
    /// </para> 
    /// This allows a data provider to be configured at a <c>TestFixture</c>,
    ///  <c>TestMethod</c>, or a <c>parameter</c> level, giving fine grained control to
    /// the developers writing unit tests over how test input data is provided for each test case.
    /// </para>
    /// </para>
    /// </remarks>
    public interface IDataProvider
    {
        /// <summary>
        /// <c>TestScenarioHelper</c> calls this method on a concrete implementation of
        /// this interface once for each input parameter for a test method.
        /// </summary>
        /// <param name="pInfo">This is the <c>System.Reflection.ParameterInfo</c> object that contains information about the 
        /// input parameter to be loaded.</param>
        /// <param name="methodName">This is the name of the test method to which the loaded input parameter will be passed. 
        /// The XML and code-based data providers use this name as a part of of a naming convention to locate the source of input data.</param>
        /// <param name="scenarioName">This is the name of the test scenario for which input data needs to be loaded.
        /// The XML and code-based concrete data provider implementations use this scenario name as a part of a naming convention to locate the
        /// source of input data.</param>
        /// <param name="testFixture">This is a reference to an object that contains any helper methods or information that may be needed by a specific data provider.
        /// For example, in case of code-based data provider, this object contains the helper method that will return the loaded input parameters.</param>
        /// <returns>This method returns the loaded input test method parameter as an object.</returns>
        /// <remarks>
        /// This method is called by <see cref="TestScenarioHelper"/> on a concrete implementation
        /// of this interface for each input parameter of a test method.
        /// </remarks>
        object GetData(ParameterInfo pInfo, string methodName, string scenarioName, object testFixture);
    }
}