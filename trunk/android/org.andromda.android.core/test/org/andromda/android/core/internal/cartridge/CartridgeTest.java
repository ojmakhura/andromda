package org.andromda.android.core.internal.cartridge;

import junit.framework.TestCase;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.core.namespace.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertiesDocument.Properties;
import org.andromda.core.namespace.PropertyDocument.Property;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;

/**
 * 
 * @author Peter Friese
 * @since 17.11.2005
 */
public class CartridgeTest
        extends TestCase
{

    public void testParsing()
    {
        CartridgeDescriptor cartridgeDescriptor = new CartridgeDescriptor(
                "D:/Einstellungen/U402101/.maven/repository/andromda/jars/", "spring", "3.2-RC1-SNAPSHOT");
        try
        {
            Namespace namespace = cartridgeDescriptor.getNamespace();
            System.out.println("Namespace: " + namespace.getName());
            Properties[] propertiesArray = namespace.getPropertiesArray();
            for (int i = 0; i < propertiesArray.length; i++)
            {
                Properties properties = propertiesArray[i];
                PropertyGroup[] propertyGroupArray = properties.getPropertyGroupArray();
                for (int j = 0; j < propertyGroupArray.length; j++)
                {
                    PropertyGroup group = propertyGroupArray[j];
                    String doc = "";
                    if (group.getDocumentation() != null)
                    {
                        doc = group.getDocumentation().toString();
                    }
                    System.out.println(" + " + group.getName() + doc);
                    Property[] propertyArray = group.getPropertyArray();
                    for (int k = 0; k < propertyArray.length; k++)
                    {
                        Property property = propertyArray[k];
                        String required;
                        if (property.getRequired())
                        {
                            required = "*";
                        }
                        else
                        {
                            required = "";
                        }
                        System.out.println("   + " + property.getName() + " [" + property.getDefault() + "] ["
                                + required + "]");
                    }
                }
            }
        }
        catch (CartridgeParsingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
