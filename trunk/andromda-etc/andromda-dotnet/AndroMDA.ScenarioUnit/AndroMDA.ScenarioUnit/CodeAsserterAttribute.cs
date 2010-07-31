using System;
using System.Collections.Generic;
using System.Text;
using System.Reflection;
namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This is a concrete implementation of the <see cref="IAsserter"/> interface
    /// that uses helper methods to assert actual output from a test case against expected output.
    /// </summary>
    /// <remarks>
    /// This asserter is similar to the traditional mechanism of using
    /// helper methods to separate out assertion logic from the main testing code.
    /// <para>
    /// This asserter is helpful when used in conjunction with <see cref="XMLAsserterAttribute"/> for return values/output parameters
    /// that cannot be serialized into XML.
    /// </para>
    /// <para>
    /// This class derives from the <c>System.Attribute</c> class. This 
    /// allows this class to be configured as the asserter for a test method return value or output parameter.
    /// </para>
    /// <para>
    /// The AttributeUsage attribute for this class:
    /// <c>[AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]</c>
    /// ensures that this attribute can be applied at a class, method or at a parameter level.
    /// </para>
    /// <para>
    /// When this attribute is specified at a <c>TestFixture</c> level, 
    /// all the test method return values and output parameters in that <c>TestFixture</c> are asserted using this asserter
    /// unless, if a different asserter is specified at a test method or parameter level.
    /// When this asserter is specified as an attribute on the test method, it
    /// overrides any asserter configured at the test fixture level.
    /// Similarly, when specified at the return value or output parameter level, 
    /// this data provider is used to assert that parameter, irrespective of the asserter configured at the test method or test fixture levels.
    /// </para>
    /// </remarks>
    [AttributeUsage(AttributeTargets.Class | AttributeTargets.Method | AttributeTargets.Parameter)]
    public sealed class CodeAsserterAttribute
        : Attribute, IAsserter
    {

        private string _asserterMethodName;

        /// <summary>
        /// This is an optional property to specify the name of the helper method that is called
        /// to assert actual output against expected output.
        /// </summary>
        /// <remarks>
        /// If this property is not set, a naiming convention is used to identify the method to be called.
        /// The naming convention is:
        /// <code escaped="true">
        /// [testMethodName_[parameterName]_assert()
        /// </code>
        /// <note>
        /// In case of return values, there is no "parameterName" part in the naming convention.
        /// </note>
        /// </remarks>
        public string AsserterMethodName
        {
            get { return _asserterMethodName; }
            set { _asserterMethodName = value; }
        }

        /// <summary>
        /// This is the implementation of the <see cref="IAsserter"/> interface.
        /// It uses a helper method to assert actual output against expected test output.
        /// </summary>
        /// <param name="outputObj">The object containing the actual test output</param>
        /// <param name="pInfo">Meta information about the <paramref name="outputObj"/>.</param>
        /// <param name="methodName">The name of the test method. This name is used to identify the helper assertion method using a naming convention.</param>
        /// <param name="scenarioName">The name of the test scenario.</param>
        /// <param name="testFixture">A reference to the object containing the helper method. 
        /// Usually, this is a reference to the TestFixture itself.</param>
        /// <remarks>
        /// If the <see cref="AsserterMethodName"/>property is not specified, this data provider uses a 
        /// naming convention to identify the helper method to
        /// be called.
        /// <para>
        /// The naming convention used is: 
        /// </para>
        ///<code escaped="true">
        /// [testMethodName]_[parameterName]_Assert()
        /// </code>
        /// <para>
        /// The helper method must be a private instance method that has the following signature:
        /// </para>
        /// <code escaped="true" lang="c#">
        ///private void [testMethodName]_[parameterName]_Assert(object outputObj, ParameterInfo pInfo, string methodName, string scenarioName)
        /// </code>
        /// <note>
        /// To assert return values, the "parameterName" part of the helper method name is ommited.
        /// </note>
        /// </remarks>
        public void AssertOutput(Object outputObj, ParameterInfo pInfo, string methodName, string scenarioName, object testFixture)
        {

            if (string.IsNullOrEmpty(AsserterMethodName))
            {
                if (string.IsNullOrEmpty(pInfo.Name))
                {
                    AsserterMethodName = string.Format("{0}_Assert", methodName);
                }
                else
                {
                    AsserterMethodName = string.Format("{0}_{1}_Assert", methodName, pInfo.Name);
                }
            }
            MethodInfo mInfo = testFixture.GetType().GetMethod(AsserterMethodName, BindingFlags.NonPublic | BindingFlags.Instance | BindingFlags.IgnoreCase);
            object[] parameters = new object[] { outputObj, pInfo, methodName, scenarioName };
            mInfo.Invoke(testFixture, parameters);
        }
    }
}
