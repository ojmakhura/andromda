package org.andromda.cartridges.meta.metafacades;

/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @since 10.12.2003
 */
public class ArgumentData
{
    private String fullyQualifiedTypeName;
    private String name;

    public ArgumentData(String fullyQualifiedTypeName, String name)
    {
        this.fullyQualifiedTypeName = fullyQualifiedTypeName;
        this.name = name;
    }

    /**
     * @return
     */
    public String getFullyQualifiedTypeName()
    {
        return fullyQualifiedTypeName;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

}
