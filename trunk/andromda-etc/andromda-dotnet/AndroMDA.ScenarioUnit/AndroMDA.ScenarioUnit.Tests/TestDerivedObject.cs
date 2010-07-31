using System;
using System.Xml;
using System.Xml.Serialization;
using System.Collections.Generic;
using System.Text;

namespace AndroMDA.ScenarioUnit.Tests.TestData.Derived
{
   
    public class TestDerivedObject:
        AndroMDA.ScenarioUnit.Tests.TestData.Base.TestObject 
                {
        string middleInitial;
        public string MiddleInitial
        {
            get {return middleInitial;}
            set {middleInitial = value;}
        }
    }

}