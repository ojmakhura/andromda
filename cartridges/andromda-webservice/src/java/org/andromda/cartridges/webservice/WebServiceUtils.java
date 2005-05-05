package org.andromda.cartridges.webservice;

import org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType;
import org.andromda.cartridges.webservice.metafacades.WSDLType;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * Contains utilities used within the WebService cartridge.
 *
 * @author Chad Brandon
 */
public class WebServiceUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        final Collection allRoles = new HashSet();
        CollectionUtils.forAllDo(services, new Closure()
        {
            public void execute(Object object)
            {
                if (object != null && Service.class.isAssignableFrom(object.getClass()))
                {
                    allRoles.addAll(((Service)object).getAllRoles());
                }
            }
        });
        return allRoles;
    }

    /**
     * Reverses the <code>packageName</code>.
     *
     * @param packageName the package name to reverse.
     * @return the reversed package name.
     */
    public static String reversePackage(String packageName)
    {
        return StringUtils.reverseDelimited(packageName, WebServiceGlobals.NAMESPACE_DELIMITER);
    }

    /**
     * <p/> Creates and returns the schema type for the given <code>type</code>.
     * It finds the mapped schema type from the passed in
     * <code>schemaTypeMappings</code>.
     * </p>
     * 
     * @param type the ClassifierFacade instance
     * @param schemaTypeMappings contains the mappings from model datatypes to
     *        schema datatypes.
     * @param namespacePrefix the prefix given to the schema type if it's a
     *        custom type (non XSD type).
     * @param qName the qualifed name
     * @param wrappedArrayTypePrefix a prefix to give to wrapped array types.
     * @param withPrefix a flag indicating whether or not the type should have
     *        the prefix defined
     * @param preserveArray true/false, if true then if the schema type is an
     *        array we'll preserve the fact that its an array and return an
     *        array schema type name. If false we will return back the non array
     *        type even if its an array.
     * @return the schema type name.
     */
    public static java.lang.String getSchemaType(
        ClassifierFacade type,
        TypeMappings schemaTypeMappings,
        String namespacePrefix,
        String qName,
        String wrappedArrayTypePrefix,
        boolean withPrefix,
        boolean preserveArray)
    {
        StringBuffer schemaType = new StringBuffer();
        String modelName = type.getFullyQualifiedName(true);
        if (schemaTypeMappings != null)
        {
            namespacePrefix = namespacePrefix + ':';
            String mappedValue = schemaTypeMappings.getTo(modelName);
            if (!mappedValue.equals(modelName))
            {
                schemaType.append(mappedValue);
            }
            else
            {
                if (withPrefix)
                {
                    schemaType.append(namespacePrefix);
                }
                if (type.isArrayType())
                {
                    ClassifierFacade nonArray = type.getNonArray();
                    if (nonArray != null)
                    {
                        if (nonArray instanceof WSDLType)
                        {
                            schemaType.append(((WSDLType)nonArray).getQName());
                        }
                        else if (nonArray instanceof WSDLEnumerationType)
                        {
                            schemaType.append(((WSDLEnumerationType)nonArray).getQName());
                        }
                    }
                }
                else
                {
                    schemaType.append(qName);
                }
            }
            // remove any array '[]' suffix
            schemaType = new StringBuffer(schemaType.toString().replaceAll("\\[\\]", ""));
            if (preserveArray && type.isArrayType())
            {
                int insertIndex = namespacePrefix.length();
                if (!schemaType.toString().startsWith(namespacePrefix))
                {
                    if (withPrefix)
                    {
                        // add the prefix for any normal XSD types
                        // that may not have been set above
                        schemaType.insert(0, namespacePrefix);                     
                    }
                    else
                    {
                        // since we aren't adding the prefix, set
                        // the correct insert index
                        insertIndex = 0;
                    }
                }
                schemaType.insert(insertIndex, wrappedArrayTypePrefix);
            }
            if (withPrefix && !schemaType.toString().startsWith(namespacePrefix))
            {
                schemaType.insert(0, WebServiceGlobals.XSD_NAMESPACE_PREFIX);
            }
        }
        return schemaType.toString();
    }
}
