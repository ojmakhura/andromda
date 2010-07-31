using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Configuration;
using System.Xml.Serialization;
using System.Reflection;
using System.IO;


namespace AndroMDA.ScenarioUnit
{
    /// <summary>
    /// This is a concrete implementation of the <see cref="IDataProvider"/> interface.
    /// This data provider loads input data stored as serialized XML objects in files.
    /// </summary>
    /// <remarks>
    /// To use this data provider, the following configuration entry is required in the configuration file of the test harness: 
    /// <para>
    /// <code escaped="true">
    /// <add key="TestDataInputDir" value="[somePath]" />
    /// </code>
    /// </para>
    /// <para>
    /// This entry specifies the path where all the input
    /// XML files are located.
    /// <note>
    /// A path relative to the test harness assembly binary (usually located in testHarness/bin/debug) can be specified for this configuration entry. 
    /// </note>
    /// </para>
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
    public sealed class XMLDataProviderAttribute
    : Attribute, IDataProvider
    {
        #region member variables and properties.
        private string _inputDir;

        /// <summary>
        /// This property is used in conjunction with the 
        /// configuration entry 
        /// <code escaped="true">
        /// <add key="TestDataInputDir" value="[some path]"/>
        /// </code>
        /// This configuration entry specifies the directory that contains all input data XML files.
        /// This property is used to specify a sub-directory below the TestDataInputDir
        /// folder that will be used to load the input data.
        /// </summary>
        /// <remarks>
        /// This property helps organize test input data in
        /// sub-folders below the TestDataInputDir folder. 
        /// <para>
        /// This property is optional and if it is not
        /// set, the methods in this class will look for test input XML files in the TestDataInputDir folder.
        /// </para>
        /// </remarks>
        public string InputDir
        {
            get
            {
                string testInputDir = ConfigurationManager.AppSettings["TestDataInputDir"];
                if (!string.IsNullOrEmpty(_inputDir))
                {
                    testInputDir = Path.Combine(testInputDir, _inputDir);
                }
                return testInputDir;
            }
            set { _inputDir = value; }
        }

        #endregion
        #region public methods
        /// <summary>
        /// This is the concrete implementation of the 
        /// <see cref="IDataProvider"/> interface.
        /// This method loads an input parameter for a test method for a specific scenario from an XML file.
        /// </summary>
        /// <param name="pInfo">This is the <c>System.Reflection.ParameterInfo</c> object that contains information about the 
        /// input parameter to be loaded.</param>
        /// <param name="methodName">This is the name of the test method to which the loaded input parameter will be passed. 
        /// This data provider uses this methodName value as a part of a naming convention to locate the XML file that contains input data.
        /// The input XML file is named using the convention [methodName_scenarioName_parameterName.xml] </param>
        /// <param name="scenarioName">This is the name of the test scenario for which input data needs to be loaded.
        /// This data provider uses the scenarioName value as a part of a naming convention to locate the XML file that contains input data.
        /// The input XML file is named using the convention [methodName_scenarioName_parameterName.xml] </param>
        /// <param name="testFixture">This parameter is not currently used by this data provider. </param>
        /// <returns>This method returns the loaded input test method parameter as an object.</returns>
        /// <remarks>
        /// The input data is stored as serialized XML for the object to be loaded.
        /// The XML schema used is the one generated by serializing the object using <c>System.Xml.Serialization.XmlSerializer</c>.
        /// This method uses a convention to locate the XML file to be read to load and return the object.
        /// <para>
        /// The XML file name is constructed using the three parameters <paramref name="pInfo"/>, <paramref name="methodName"/>, and <paramref name="scenarioName"/>. 
        /// The file name is assumed to be :
        /// <para>
        /// methodName_scenarioName_parameterName.xml
        /// </para>
        /// For example, to load the input parameters for the test method <c>private Money Add(Money m1, Money m2)</c> 
        /// for the following invokation: <c>TestScenarioHelper.Invoke("Add", "SimpleAdd", this)</c>
        /// this method will look for the files Add_SimpleAdd_m1.xml and
        /// Add_SimpleAdd_m2.xml in the <see cref="XMLDataProvider.InputDir"/> directory.
        /// </para>
        /// <note>
        /// you can use code similar to the following to generate the first XML file for an object and then copy it 
        /// over and over for multiple scenarios replacing specific values according to the input scenario.
        /// <para>
        /// <code lang="c#">
        /// Money m1 = new Money(1, "USD");
        /// XMLSerializer xs = new XMLSerializer(typeof(Money));
        /// using (StreamWriter sw = new StreamWriter(File.OpenWrite("Add_SimpleAdd_m1.xml")))
        /// {
        ///     xs.Serialize(sw, m1);
        /// }
        /// </code>
        /// </para>
        /// </note>
        /// <note>
        /// While trying to read polymorphic objects,
        /// i.e. trying to read a derived object while the input parameter is of the base type,
        /// make sure that the base type definition has an <c>[XMLInclude()]</c> attribute refering to the derived type.
        /// </note>
        /// </remarks>
        /// <example>
        /// This example shows the serialized form of the following Money class:
        /// <code lang="c#">
        /// 
        /// namespace MoneyServiceSample
        ///{
        ///    public class Money
        ///    {
        ///        public int amount;
        ///        public string currency;
        ///        public Money() { }
        ///
        ///        public Money(int amt, string crncy)
        ///        {
        ///            amount = amt;
        ///            currency = crncy;
        ///        }
        ///    }
        ///}
        ///
        /// </code>
        /// The following is the Add_SimpleAdd_m1.xml file:
        /// <code escaped="true">
        /// <?xml version="1.0" encoding="utf-8"?>
        ///<Money xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        ///    <amount>12</amount>
        ///    <currency>CHF</currency>
        ///</Money>
        /// </code>
        /// </example>
        public object GetData(ParameterInfo pInfo, string methodName, string scenarioName, object testFixture)
        {
            string inputFileName = GetFileName(pInfo, methodName, scenarioName);
            string inputPath = Path.Combine(InputDir, inputFileName);
            try
            {
                XmlSerializer xs = new XmlSerializer(pInfo.ParameterType);
                using (StreamReader sr = new StreamReader(inputPath))
                {
                    return xs.Deserialize(sr);
                }
            }
            catch (Exception e)
            {
                string errorMessage = string.Format("Could not load data for {0} from the file {1} for the test method {2} and scenario {3}.", pInfo.Name, inputPath, methodName, scenarioName);
                throw new DataProviderException(errorMessage, e);
            }
        }

        #endregion

        #region private helper methods
        /// <summary>
        /// Helper method to determine the XML file name for a parameter.
        /// </summary>
        /// <param name="pInfo"></param>
        /// <param name="methodName"></param>
        /// <param name="scenarioName"></param>
        /// <returns></returns>
        private static string GetFileName(ParameterInfo pInfo, string methodName, string scenarioName)
        {
            string fileName = methodName + "_" + scenarioName;
            if (!string.IsNullOrEmpty(pInfo.Name))
            {
                fileName += "_" + pInfo.Name;
            }
            fileName += ".xml";
            return fileName;
        }

        #endregion
    }
}
