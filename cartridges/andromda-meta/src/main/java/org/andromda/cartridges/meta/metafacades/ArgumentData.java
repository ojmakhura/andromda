package org.andromda.cartridges.meta.metafacades;

/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @since 10.12.2003
 * @author Bob Fields
 */
public class ArgumentData
{
    private String fullyQualifiedTypeName;
    private String name;

    /**
     * @param fullyQualifiedTypeNameIn
     * @param nameIn
     */
    public ArgumentData(String fullyQualifiedTypeNameIn, String nameIn)
    {
        this.fullyQualifiedTypeName = fullyQualifiedTypeNameIn;
        this.name = nameIn;
    }

    /**
     * @return this.fullyQualifiedTypeName
     */
    public String getFullyQualifiedTypeName()
    {
        return this.fullyQualifiedTypeName;
    }

    /**
     * @return this.name
     */
    public String getName()
    {
        return this.name;
    }

}
