using System;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using AndroMDA.ScenarioUnit;
using NUnit.Framework;
using MoneyServiceSample;


namespace MoneyServiceTests
{
    [TestFixture]
    [XMLDataProvider(InputDir = "MoneyServiceTests")]
    [XMLAsserter(ActualOutputDir = "MoneyServiceTests", ExpectedOutputDir = "MoneyServiceTests")]
    public class MoneyServiceTest
    {
        [Test]
        public void TestSimpleAdd()
        {
            TestScenarioHelper.Invoke("Add", "SimpleAdd", this);
        }

        [Test]
        public void TestMultipleCurrencyAdd()
        {
            //Money m1 = new Money(1, "CHF");
            //XmlSerializer xs = new XmlSerializer(typeof(Money));
            //using (StreamWriter sw = new StreamWriter(File.OpenWrite("money.xml")))
            //{
            //    xs.Serialize(sw, m1);
            //}

            TestScenarioHelper.Invoke("Add", "MultipleCurrencyAdd", this);
        }

        private Money Add(Money m1, Money m2)
        {
            return MoneyService.Add(m1, m2);
        }
    }
}
