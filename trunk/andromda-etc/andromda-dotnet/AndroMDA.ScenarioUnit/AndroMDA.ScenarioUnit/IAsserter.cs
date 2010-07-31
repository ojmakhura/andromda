using System;
using System.Reflection;

namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This interface is implemented by all concrete Asserter classes. 
    /// The implementation of this interface provides the ability to assert that actual output is equal to expected output for a specific test scenario.
    /// </summary>
    /// <remarks>
    /// The implementation of this interface receives the actual output returned 
    /// by invoking the system under test for a specific scenario
    /// and compares this output with expected data stored outside the actual test cases (Similar to the data provider concept.)
    /// <para>
    /// A concrete asserter that implements this interface needs to 
    /// derive from the <see cref="System.Attribute"/> class so that it can
    /// be configured to be used to assert output data.
    /// <para>
    /// The asserter should use the following<c>AttributeUsage</c> tag:
    /// <para>
    /// <c>[AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]</c>
    /// </para> 
    /// This allows an asserter to be configured at a <c>TestFixture</c>,
    ///  <c>TestMethod</c>, or a <c>parameter</c> level, giving fine grained control to
    /// the developers writing unit tests over how test output is asserted.
    /// </para>
    /// </para>
    /// </remarks>
    public interface IAsserter
    {
        /// <summary>
        /// This method asserts actual output for a specific test scenario
        /// with expected output.
        /// The <see cref="TestScenarioHelper"/> class calls this method
        /// once for each return and output paramter
        /// of a test method.
        /// </summary>
        /// <param name="outputObj">The actual output obtained by invoking the system under test.</param>
        /// <param name="pInfo">Meta information about the actual output object. 
        /// This information can be used by concrete IAsserter implementations to help load the expected output and perform 
        /// the comparison of actual output with expected output.</param>
        /// <param name="methodName">This is the name of the test method being used to invoke the functionality under test.
        /// This information can be used by concrete IAsserter implementations to help load the expected output and perform 
        /// the comparison of actual output with expected output.</param>
        /// <param name="scenarioName">The name of the test scenario being executed.
        /// This information can be used by concrete IAsserter implementations to help load the expected output and perform 
        /// the comparison of actual output with expected output.</param>
        /// <param name="testFixture">A reference to the class containing the test case or any other helper methods
        /// that can help in asserting the output.</param>
        /// <remarks>
        /// This method receives only the actual output as a parameter
        /// and not the expected output because the concrete asserter
        /// is required to load/find the expected output on its own using concepts similar to the data provider.
        /// Meta information about the output, test method and the test scenario being executed is passed along to this method.
        /// </remarks>
        void AssertOutput(Object outputObj, ParameterInfo pInfo, string methodName, string scenarioName, object testFixture);
    }
}
