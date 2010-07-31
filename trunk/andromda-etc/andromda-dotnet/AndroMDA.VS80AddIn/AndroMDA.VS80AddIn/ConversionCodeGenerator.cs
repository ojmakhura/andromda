
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Text;
using EnvDTE80;
using EnvDTE;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class ConversionCodeGenerator
    {

        #region Property class

        public class Property
        {
            private string m_name;
            private string m_type;
            private Property m_sourceProperty;

            public Property()
            {
                m_name = string.Empty;
                m_type = string.Empty;
                m_sourceProperty = null;
            }

            public Property(string name, string type)
            {
                m_name = name;
                m_type = type;
                m_sourceProperty = null;
            }

            public string Name
            {
                get { return m_name; }
                set { m_name = value; }
            }

            public string Type
            {
                get { return m_type; }
                set { m_type = value; }
            }

            public Property SourceProperty
            {
                get { return m_sourceProperty; }
                set { m_sourceProperty = value; }
            }

            public bool IsNullableType
            {
                get { return m_type.EndsWith("?"); }
            }
        }

        #endregion

        private ArrayList m_properties;

        public ArrayList Properties
        {
            get { return m_properties; }
            set { m_properties = value; }
        }

        public ConversionCodeGenerator()
        {
            m_properties = new ArrayList(); ;
        }

        public ConversionCodeGenerator(ArrayList properties)
        {
            m_properties = properties;
        }

        public string GenerateCode(CodeFunction2 method)
        {

            CodeParameter2 param = method.Parameters.Item(1) as CodeParameter2;
            CodeClass2 containingClass = method.Parent as CodeClass2;

            string returnType = method.Type.AsFullName;
            string paramType = param.Type.AsFullName;
            string paramName = param.Name;
            string returnVariableName = string.Empty;
            string codeToInsert = string.Empty;

            bool convertingToEntity = CodeModelUtils.IsEntityClass(method.Type.CodeType);

            // If we are converting to an entity
            if (convertingToEntity)
            {
                returnVariableName = "entity";
                codeToInsert += "// VO to entity conversion\n";
                // Add code to create a new entity with the Factory.NewInstance() method
                codeToInsert += returnType + " " + returnVariableName + " = " + returnType + ".Factory.NewInstance();\n\n";
            }
            else
            {
                returnVariableName = "valueObject";
                codeToInsert += "// Entity to VO conversion\n\n";
                // Add code to create a new VO with a new statement
                codeToInsert += returnType + " " + returnVariableName + " = new " + returnType + "();\n\n";
            }

            ArrayList unmappedProperties = new ArrayList();
            foreach (Property prop in m_properties)
            {
                if (prop.SourceProperty != null)
                {
                    if (prop.IsNullableType)
                    {
                        codeToInsert += "if (" + paramName + "." +prop.Name + ".HasValue)\n{\n";
                        codeToInsert += returnVariableName + "." + prop.Name + " = " + paramName + "." + prop.SourceProperty.Name + ".Value;\n}\n";
                    }
                    else
                    {
                        codeToInsert += returnVariableName + "." + prop.Name + " = " + paramName + "." + prop.SourceProperty.Name + ";\n";
                    }
                }
                else
                {
                    unmappedProperties.Add(prop);
                }
            }

            foreach (Property unmappedProp in unmappedProperties)
            {
                codeToInsert += "// " + returnVariableName + "." + unmappedProp.Name + "\n";
            }

            // Add the return statement
            codeToInsert += "\nreturn " + returnVariableName + ";\n\n";

            return codeToInsert;

        }

        public void AddProperty(string toName, string toType)
        {
            m_properties.Add(CreateProperty(toName, toType));
        }

        public void AddProperty(string toName, string toType, string fromName, string fromType)
        {
            m_properties.Add(CreateProperty(toName, toType, fromName, fromType));
        }
        
        public static Property CreateProperty(string toName, string toType)
        {
            return new Property(toName, toType);
        }

        public static Property CreateProperty(string toName, string toType, string fromName, string fromType)
        {
            Property prop = new Property(toName, toType);
            prop.SourceProperty = new Property(fromName, fromType);
            return prop;
        }
    }
}