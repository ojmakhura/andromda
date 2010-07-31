using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Reflection;



namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This is the helper class that is used to invoke the functionality provided by the AndroMDA.ScenarioUnit framework.
    /// </summary>
    /// <remarks>
    /// This class has a single public static method 
    /// <c>Invoke()</c> that is called from test cases to execute a specific test scenario.
    /// <para>
    /// To be able to use this testing framework, the following 
    /// configuration entries are required
    /// in the app.config file of the test harness:
    /// <code escaped="true">
    /// 	<runtime>
    ///		<assemblyBinding xmlns="urn:schemas-microsoft-com:asm.v1">
    ///<dependentAssembly>
    ///<assemblyIdentity name="nunit.framework" publicKeyToken="96d09a1eb7f44a77" culture="Neutral" />
    ///<bindingRedirect oldVersion="2.0.6.0" newVersion="2.2.8.0" />
    ///<bindingRedirect oldVersion="2.1.4.0" newVersion="2.2.8.0" />
    ///<bindingRedirect oldVersion="2.2.0.0" newVersion="2.2.8.0" />
    ///			</dependentAssembly>
    ///</assemblyBinding>
    ///</runtime>
    /// </code>
    /// </para>
    /// <note>
    /// The "NewVersion" value in these entries should be the value of the
    /// NUnit version being used in the test harness.
    /// </note>
    /// <para>
    /// Look at the MoneyServiceSample in the samples folder of the 
    /// AndroMDA.ScenarioUnit source to see examples of simple usage of this test framework.
    /// </para>
    /// <para>
    /// To see samples of all possible combinations in which this testing framework can be used, 
    /// look at the test cases implemented for this framework itself
    /// in the AndroMDA.ScenarioUnit.Tests project.
    /// </para>
    /// </remarks>
    public class TestScenarioHelper
    {
        /// <summary>
        ///  Making the constructor protected so that this class is 
        ///  not instantiated.
        /// </summary>
        protected TestScenarioHelper() { }

        /// <summary>
        /// This is the method used to invoke a test method for a specific scenario.
        /// </summary>
        /// <param name="methodName">The test method to execute. This must be a private instance method
        /// in the TestFixture class.</param>
        /// <param name="scenarioName">The test scenario.</param>
        /// <param name="testFixture">The class containing the test method.</param>
        /// <remarks>
        /// this method is called from an NUnit test case and does the following:
        /// <list type="bullet">
        /// <item>
        /// Calls the method specified by the <c>methodName</c> parameter;
        /// passing in all input parameters by using the concrete implementation of <see cref="IDataProvider"/> configured for each parameter.
        /// </item> 
        /// <item>
        /// Captures the actual output returned from the test method and
        /// compares it with the expected output using the concrete implementation of <see cref="IAsserter"/> configured for each return value 
        /// and out/ref parameter.
        /// </item>
        /// </list>
        /// </remarks>
        public static void Invoke(string methodName, string scenarioName, Object testFixture)
        {
            MethodInfo mInfo = testFixture.GetType().GetMethod(methodName, BindingFlags.NonPublic | BindingFlags.Instance | BindingFlags.IgnoreCase);
            ParameterInfo[] pInfos = mInfo.GetParameters();
            ParameterInfo returnParamInfo = mInfo.ReturnParameter;
            ArrayList allParameters = new ArrayList();
            List<ParameterInfo> outParameters = new List<ParameterInfo>();
            foreach (ParameterInfo pInfo in pInfos)
            {
                Object parameter = null;
                //Try to load a parameter 
                //only if it is an input parameter.
                //Otherwise, just add the parameterInfo for that parameter
                //into an array for assertion later.
                if (pInfo.IsOut)
                {
                    outParameters.Add(pInfo);
                }
                else
                {
                    IDataProvider dataProvider = GetDataProvider(testFixture, mInfo, pInfo);
                    if (null != dataProvider)
                    {
                        parameter = dataProvider.GetData(pInfo, methodName, scenarioName, testFixture);
                    }
                }
                //Add the parameter to the array to be used to call the method
                //irrespective of whether or not it is an out parameter.
                //For out parameters, a "null" is added in the array.
                //This is replaced by an actual object after the method is invoked.
                allParameters.Add(parameter);
            }
            //Call the test method.
            try
            {
                Object[] parameters = allParameters.ToArray();
                Object returnObj = mInfo.Invoke(testFixture, parameters);
                //Save the returned value 
                //and call the corresponding assertion only if the return type is not void.
                if (returnParamInfo.ParameterType.Name != "Void")
                {
                    IAsserter outputAsserter = GetOutputAsserter(testFixture, mInfo, returnParamInfo);
                    if (null != outputAsserter)
                    {
                        outputAsserter.AssertOutput(returnObj, returnParamInfo, methodName, scenarioName, testFixture);
                    }
                }
                //Save the remaining out parameters and 
                //call the corresponding assertions.
                foreach (ParameterInfo outPInfo in outParameters)
                {
                    object outParameter = parameters[outPInfo.Position];
                    IAsserter outputAsserter = GetOutputAsserter(testFixture, mInfo, outPInfo);
                    if (null != outputAsserter)
                    {
                        outputAsserter.AssertOutput(outParameter, outPInfo, methodName, scenarioName, testFixture);
                    }
                }
            }
            catch (TargetInvocationException e)
            {
                throw e.InnerException;
            }
        }

        #region private helper methods
        /// <summary>
        /// Helper method to get the asserter to be used for a test case.
        /// Asserters can be specified at a class, method or parameter level.
        /// The asserter specified at a specific level overrides the one specified at a more generic level.
        /// </summary>
        /// <param name="testFixture"></param>
        /// <param name="mInfo"></param>
        /// <param name="pInfo"></param>
        /// <returns>Returns the asserter applicable for the specified parameter</returns>
        private static IAsserter GetOutputAsserter(object testFixture, MethodInfo mInfo, ParameterInfo pInfo)
        {
            IAsserter outputAsserter = null;

            object[] classAttributes = testFixture.GetType().GetCustomAttributes(false);
            for (int i = 0; i < classAttributes.Length; ++i)
            {
                if (null != classAttributes[i].GetType().GetInterface("IAsserter", true))
                {
                    outputAsserter = (IAsserter)classAttributes[i];
                    break;
                }
            }

            object[] methodAttributes = mInfo.GetCustomAttributes(false);
            for (int i = 0; i < methodAttributes.Length; ++i)
            {
                //Use the class asserter if a method level asserter is not assigned.
                if (null != methodAttributes[i].GetType().GetInterface("IAsserter", true))
                {
                    outputAsserter = (IAsserter)methodAttributes[i];
                    break;
                }
            }

            object[] paramAttributes = pInfo.GetCustomAttributes(false);
            for (int i = 0; i < paramAttributes.Length; ++i)
            {
                if (null != paramAttributes[i].GetType().GetInterface("IAsserter", false))
                {
                    outputAsserter = (IAsserter)paramAttributes[i];
                    break;
                }
            }
            return outputAsserter;

        }

        /// <summary>
        /// Private helper method to get the data provider to load a parameter.
        /// This method returns the data provider specified at the parameter, method or class level in that order of precedence.
        /// </summary>
        /// <param name="testFixture"></param>
        /// <param name="mInfo"></param>
        /// <param name="pInfo"></param>
        /// <returns>Returns the data provider to be used to load the specified parameter.</returns>
        private static IDataProvider GetDataProvider(Object testFixture, MethodInfo mInfo, ParameterInfo pInfo)
        {
            IDataProvider dataProvider = null;

            object[] classAttributes = testFixture.GetType().GetCustomAttributes(false);
            for (int i = 0; i < classAttributes.Length; ++i)
            {
                if (null != classAttributes[i].GetType().GetInterface("IDataProvider", true))
                {
                    dataProvider = (IDataProvider)classAttributes[i];
                    break;
                }
            }

            object[] methodAttributes = mInfo.GetCustomAttributes(false);
            for (int i = 0; i < methodAttributes.Length; ++i)
            {
                //Use the classDataProvider if a methodDataProvider is not assigned.
                if (null != methodAttributes[i].GetType().GetInterface("IDataProvider", true))
                {
                    dataProvider = (IDataProvider)methodAttributes[i];
                    break;
                }
            }

            object[] paramAttributes = pInfo.GetCustomAttributes(false);
            for (int i = 0; i < paramAttributes.Length; ++i)
            {
                if (null != paramAttributes[i].GetType().GetInterface("IDataProvider", false))
                {
                    dataProvider = (IDataProvider)paramAttributes[i];
                    break;
                }
            }
            return dataProvider;
        }
        #endregion
    }
}
