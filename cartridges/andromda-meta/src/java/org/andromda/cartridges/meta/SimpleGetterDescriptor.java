package org.andromda.cartridges.meta;

/**
 * Describes a simple getter. Used to exclude a getter from the
 * list of decorated methods. The getter is used to access an
 * association.
 * 
 * @since 10.12.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class SimpleGetterDescriptor extends MethodDescriptor
{
    public SimpleGetterDescriptor(
        String fullyQualifiedTypeName,
        String fieldName)
    {
        super(
            new MethodData(
                "",
                "public",
                false,
                fullyQualifiedTypeName,
                "get"
                    + fieldName.substring(0,1).toUpperCase()
                    + fieldName.substring(1)));
    }
}
