using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System.Configuration;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using NUnit.Framework;
using AndroMDA.ScenarioUnit;
using AndroMDA.ScenarioUnit.Tests.TestData.Base;
using AndroMDA.ScenarioUnit.Tests.TestData.Derived;

namespace AndroMDA.ScenarioUnit.Tests
{
    [TestFixture]
    [XMLDataProvider(InputDir = "TestScenarioHelperTests")]
    [XMLAsserter(ActualOutputDir = "TestScenarioHelperTests", ExpectedOutputDir = "TestScenarioHelperTests", RulesDir = "TestScenarioHelperTests")]
    public class TestScenarioHelperTests
    {
        #region TestInvoke
        [Test]
        public void TestInvoke()
        {
            TestScenarioHelper.Invoke("ProcessTestObject", "default", this);
        }
        [Test]
        [ExpectedException(typeof(DataProviderException), "Could not load data for testObj from the file ../../testdata/input\\TestScenarioHelperTests\\ProcessTestObject_NoInputFile_testObj.xml for the test method ProcessTestObject and scenario NoInputFile.")]
        public void TestInvokeNoInputFile()
        {
            TestScenarioHelper.Invoke("ProcessTestObject", "NoInputFile", this);
        }

        [Test]
        [ExpectedException(typeof(AsserterException), "Could not load the expected output from the file ../../testdata/expected_output\\TestScenarioHelperTests\\ProcessTestObject_NoExpectedOutputFile.xml for the test method ProcessTestObject and scenario NoExpectedOutputFile.")]
        public void TestInvokeNoExpectedOutputFile()
        {
            TestScenarioHelper.Invoke("ProcessTestObject", "NoExpectedOutputFile", this);
        }
        [Test]
        public void TestInvokeNoRulesFile()
        {
            TestScenarioHelper.Invoke("ProcessTestObject", "NoRulesFile", this);
        }

        [Test]
        public void TestInvokeNoIgnoreRules()
        {
            TestScenarioHelper.Invoke("ProcessTestObject", "NoIgnoreRules", this);
        }
        private TestObject ProcessTestObject(TestObject testObj)
        {
            testObj.Id = 100;
            return testObj;
        }
        #endregion

        #region TestInvokeMultipleParameters
        [Test]
        public void TestInvokeMultipleInputParameters()
        {
            TestScenarioHelper.Invoke("ProcessMultipleTestObjects", "MultipleInputParameters", this);
        }
        private TestObject ProcessMultipleTestObjects(TestObject parentObj, TestObject baseChildObj, TestDerivedObject derivedChildObj)
        {
            parentObj.Id = 100;
            parentObj.Child = baseChildObj;
            baseChildObj.Child = derivedChildObj;
            return parentObj;
        }

        #endregion

        #region TestInvokeNoInputParameter
        [Test]
        public void TestInvokeNoInputParameter()
        {
            TestScenarioHelper.Invoke("GetTestObject", "NoInputParameter", this);
        }
        private TestObject GetTestObject()
        {
            TestObject testObj = new TestObject();
            testObj.FirstName = "a";
            testObj.LastName = "b";
            return testObj;
        }

        #endregion

        #region TestInvokePrimitiveInputParameter
        [Test]
        public void TestInvokePrimitiveInputParameter()
        {
            TestScenarioHelper.Invoke("GetTestObjectWithId", "PrimitiveInputParameter", this);
        }
        private TestObject GetTestObjectWithId(int id)
        {
            TestObject testObj = new TestObject();
            testObj.Id = id;
            testObj.FirstName = "a";
            testObj.LastName = "b";
            return testObj;
        }

        #endregion

        #region TestInvokeNoReturnParameter
        [Test]
        public void TestInvokeNoReturnParameter()
        {
            TestScenarioHelper.Invoke("NoReturn", "NoReturnParameter", this);
        }
        private void NoReturn()
        {
            return;
        }

        #endregion

        #region TestInvokeMultipleReturnParameters
        [Test]
        public void TestInvokeMultipleReturnParameters()
        {
            TestScenarioHelper.Invoke("GetMultipleTestObjects", "MultipleReturnParameters", this);
        }

        private TestObject GetMultipleTestObjects(TestObject parentObj, out TestDerivedObject derivedChildObj)
        {
            derivedChildObj = (TestDerivedObject)parentObj.Child;
            return parentObj;
        }

        #endregion

        #region testInvoke returns null

        [Test]
        public void TestInvokeNullReturnParameter()
        {
            TestScenarioHelper.Invoke("NullReturn", "NullReturnParameter", this);
        }
        private TestObject NullReturn()
        {
            return null;
        }


        #endregion

        #region TestInvokeCollectionParameters
        [Test]
        public void TestInvokeCollectionParameters()
        {
            TestScenarioHelper.Invoke("GetTestObjectCollection", "CollectionParameters", this);
            TestScenarioHelper.Invoke("GetTestObjectList", "ListParameter", this);
        }
        private TestObject[] GetTestObjectCollection(int[] ids)
        {

            TestObject[] tos = new TestObject[10];
            for (int i = 0; i < ids.Length && i < 10; ++i)
            {
                TestObject to = new TestObject();
                to.FirstName = ids[i].ToString();
                to.LastName = string.Format("{0}", (ids[i] + 100));
                tos[i] = to;
            }
            return tos;
        }

        private List<TestObject> GetTestObjectList(int[] ids)
        {
            List<TestObject> tos = new List<TestObject>();

            for (int i = 0; i < ids.Length && i < 10; ++i)
            {
                TestObject to = new TestObject();
                to.FirstName = ids[i].ToString();
                to.LastName = string.Format("{0}", (ids[i] + 100));
                tos.Add(to);
            }
            return tos;
        }

        #endregion

        #region TestBusinessException
        [Test]
        [ExpectedException(typeof(TestException))]
        public void TestBusinessException()
        {
            TestScenarioHelper.Invoke("ExceptionThrower", "default", this);
        }
        private void ExceptionThrower()
        {
            throw new TestException();
        }
        #endregion

        #region TestCodeDataProvider
        [Test]
        public void TestCodeDataProvider()
        {
            TestScenarioHelper.Invoke("ProcessCodeBasedTestObject", "default", this);
        }
        [CodeDataProvider]
        private void ProcessCodeBasedTestObject(TestObject codeBasedTestObject)
        {
            Assert.IsNotNull(codeBasedTestObject);
        }
        private object ProcessCodeBasedTestObject_codeBasedTestObject_data(ParameterInfo pinfo, string methodName, string scenarioName)
        {
            return new TestObject();
        }
        #endregion

        #region TestNamedCodeDataProvider
        [Test]
        public void TestNamedCodeDataProvider()
        {
            TestScenarioHelper.Invoke("ProcessTestObjectFromNamedMethod", "default", this);
        }

        [CodeDataProvider(DataMethodName = "TestObjectProvider")]
        private void ProcessTestObjectFromNamedMethod(TestObject testObject, TestDerivedObject derivedObject)
        {
            Assert.IsNotNull(testObject);
            Assert.IsNotNull(derivedObject);
        }
        private object TestObjectProvider(ParameterInfo pinfo, string methodName, string scenarioName)
        {
            if (pinfo.ParameterType == typeof(TestObject))
            {
                return new TestObject();
            }
            else if (pinfo.ParameterType == typeof(TestDerivedObject))
            {
                return new TestDerivedObject();
            }
            else
            {
                return null;
            }

        }
        #endregion

        #region TestMixedDataProviders
        [Test]
        public void TestMixedDataProvider()
        {
            TestScenarioHelper.Invoke("ProcessMixedDataProviders", "default", this);
        }
        [XMLDataProvider(InputDir = "TestScenarioHelperTests")]
        private void ProcessMixedDataProviders(TestObject testObject,
            [CodeDataProvider] TestDerivedObject derivedObject)
        {
            Assert.IsNotNull(testObject);
            Assert.IsNotNull(derivedObject);
        }

        private object ProcessMixedDataProviders_derivedObject_data(ParameterInfo pinfo, string methodName, string scenarioName)
        {
            return new TestDerivedObject();
        }
        #endregion

        #region TestCodeAsserter
        [Test]
        public void TestCodeAsserter()
        {
            TestScenarioHelper.Invoke("ProcessTestObjectForCodeAssertion", "default", this);
        }
        [Test]
        [ExpectedException(typeof(AssertionException))]
        public void TestCodeAsserterFailure()
        {
            TestScenarioHelper.Invoke("ProcessTestObjectForCodeAssertion", "AssertionFailure", this);
        }

        [CodeAsserter]
        private TestObject ProcessTestObjectForCodeAssertion()
        {
            return new TestObject();
        }

        private void ProcessTestObjectForCodeAssertion_Assert(object outputObj, ParameterInfo pInfo, string methodName, string scenarioName)
        {
            switch (scenarioName)
            {
                case "default":
                    {
                        TestObject testObject = (TestObject)outputObj;
                        Assert.IsNotNull(testObject);
                        break;
                    }
                case "AssertionFailure":
                    {
                        Assert.IsNull(outputObj);
                        break;
                    }
            }
        }
        #endregion

        #region TestNamedCodeAsserter
        [Test]
        public void TestNamedCodeAsserter()
        {
            TestScenarioHelper.Invoke("ProcessTestObjectForNamedCodeAsserter", "scenario1", this);
        }

        [CodeAsserter(AsserterMethodName = "TestObjectAsserter")]

        private TestObject ProcessTestObjectForNamedCodeAsserter(out TestDerivedObject derivedObj)
        {
            TestObject testObj = new TestObject();
            testObj.FirstName = "manish";

            derivedObj = new TestDerivedObject();
            derivedObj.FirstName = "derived";
            derivedObj.MiddleInitial = "m";
            return testObj;
        }

        private void TestObjectAsserter(object outputObj, ParameterInfo pinfo, string methodName, string scenarioName)
        {
            switch (scenarioName)
            {
                case "scenario1":
                    {
                        if (string.IsNullOrEmpty(pinfo.Name))
                        {
                            Assert.AreEqual("manish", ((TestObject)outputObj).FirstName);
                        }
                        else if (pinfo.Name == "derivedObj")
                        {
                            Assert.AreEqual("m", ((TestDerivedObject)outputObj).MiddleInitial);
                        }
                        else
                        {
                            Assert.Fail("invalid return type");
                        }
                        break;
                    }
                default:
                    {
                        Assert.Fail("invalid scenarioname");
                        break;
                    }

            }
        }
        #endregion

        #region TestMixedAsserters
        [Test]
        public void TestMixedAsserters()
        {
            TestScenarioHelper.Invoke("ProcessMixedAsserters", "default", this);
        }
        [Test]
        [ExpectedException(typeof(AssertionException))]
        public void TestProcessMixedAssertersXMLAssertionFailure()
        {
            TestScenarioHelper.Invoke("ProcessMixedAsserters", "XMLAssertionFailure", this);
        }

        [Test]
        [ExpectedException(typeof(AssertionException))]
        public void TestProcessMixedAssertersCodeAssertionFailure()
        {
            TestScenarioHelper.Invoke("ProcessMixedAsserters", "CodeAssertionFailure", this);
        }

        private TestObject ProcessMixedAsserters(
            [CodeAsserter] out TestDerivedObject derivedObject)
        {
            TestObject testObj = new TestObject();
            testObj.FirstName = "manish";

            derivedObject = new TestDerivedObject();
            derivedObject.FirstName = "derived";
            derivedObject.MiddleInitial = "m";
            return testObj;

        }

        private void ProcessMixedAsserters_derivedObject_Assert(object outputObj, ParameterInfo pinfo, string methodName, string scenarioName)
        {
            switch (scenarioName)
            {
                case "default":
                    Assert.AreEqual("m", ((TestDerivedObject)outputObj).MiddleInitial);
                    break;

                case "CodeAssertionFailure":
                    Assert.Fail("failing derived object assertion.");
                    break;
            }
        }
        #endregion

        #region test missing input or output files

        [Test]
        [ExpectedException(typeof(AsserterException), "Could not write actual output to the file ../../testdata/actual_output\\InvalidPath\\ProcessInvalidOutputPath_InvalidActualOutputPath.xml for the test method ProcessInvalidOutputPath and scenario InvalidActualOutputPath.")]
        public void TestInvokeInvalidActualOutputPath()
        {
            TestScenarioHelper.Invoke("ProcessInvalidOutputPath", "InvalidActualOutputPath", this);
        }

        [XMLAsserter(ActualOutputDir = "InvalidPath")]
        private TestObject ProcessInvalidOutputPath()
        {
            return new TestObject();
        }
        #endregion
    }
}
