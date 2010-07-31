using System;
using System.Xml.Serialization;
using AndroMDA.ScenarioUnit.Tests.TestData.Derived;

namespace AndroMDA.ScenarioUnit.Tests.TestData.Base
{
[XmlInclude(typeof(TestDerivedObject))]
    [Serializable]
    public class TestObject
    {
        int id;
        string firstName;
        string lastName;
        TestObject child;

        public int Id
        {
            get { return id; }
            set { id = value; }
        }

        public string FirstName
        {
            get { return firstName; }
            set { firstName = value; }
        }
        public string LastName
        {
            get { return lastName; }
            set { lastName = value; }
        }

        public TestObject Child
        {
            get { return child; }
            set { child = value; }
        }

    }
}