package org.andromda.core.metadecorators.uml14;

public interface TestModel
{
    public static final String XMI_FILE_URL = "jar:file:./src/uml/TestModel.zuml!/TestModel.xmi";
    public static final String CLASSA_NAME = "ClassA";
    public static final String CLASSA_PACKAGE_NAME = "org.andromda";
    public static final String CLASSA_STEREOTYPE_NAME = "EntityBean";
    
    public static final String OPERATIONA_NAME = "operationA";
    public static final String OPERATIONA_SIGNATURE = 
        "int operationA(int parameterA)";
    public static final String OPERATIONA_STEREOTYPE = "FinderMethod";
    public static final String OPERATIONA_VISIBILITY = "public";
    
    public static final String ATTRIBUTEA_TYPE = "int";
    
    // Assocation Names
    public static final String ONE2ONE = "one2one";
    public static final String ONE2MANY= "one2many";
    public static final String MANY2MANY = "many2many";
    public static final String MANY2ONE = "many2one";
}
