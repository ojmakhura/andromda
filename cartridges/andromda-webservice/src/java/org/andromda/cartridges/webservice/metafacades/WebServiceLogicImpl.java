package org.andromda.cartridges.webservice.metafacades;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.andromda.cartridges.webservice.WebServiceProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.webservice.metafacades.WebService.
 * 
 * @see org.andromda.cartridges.webservice.metafacades.WebService
 */
public class WebServiceLogicImpl
    extends WebServiceLogic
    implements org.andromda.cartridges.webservice.metafacades.WebService
{
    // ---------------- constructor -------------------------------

    public WebServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedOperations()
     */
    public java.util.Collection handleGetAllowedOperations()
    {
        Collection allowedOperations;
        boolean allowAll = this.getStereotypeNames().contains(
            WebServiceProfile.STEREOTYPE_WEBSERVICE);
        if (allowAll)
        {
            allowedOperations = new ArrayList();
            allowedOperations.addAll(this.getOperations());
        }
        else
        {
            allowedOperations = new FilteredCollection(this.getOperations())
            {
                public boolean evaluate(Object object)
                {
                    return ((ModelElementFacade)object)
                        .hasStereotype(WebServiceProfile.STEREOTYPE_WEBSERVICE_OPERATION);
                }
            };
        }
        return allowedOperations;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedMethods()
     */
    public java.lang.String handleGetAllowedMethods()
    {
        Collection methodNames = new ArrayList();
        Collection operations = this.getAllowedOperations();
        if (operations != null && !operations.isEmpty())
        {
            Iterator operationIt = operations.iterator();
            while (operationIt.hasNext())
            {
                OperationFacade operation = (OperationFacade)operationIt.next();
                methodNames.add(StringUtils.trimToEmpty(operation.getName()));
            }
        }
        return StringUtils.join(methodNames.iterator(), " ");
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getQName()
     */
    public String handleGetQName()
    {
        return MessageFormat.format(
            this.getQualifiedNameLocalPartPattern(),
            new String[]
            {
                StringUtils.trimToEmpty(this.getName())
            });
    }

    /**
     * The character used to seperate namespace names.
     */
    static char NAMESPACE_DELIM = '.';

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getNamespace()
     */
    public java.lang.String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = StringUtils.reverseDelimited(
                packageName,
                NAMESPACE_DELIM);
        }
        return MessageFormat.format(this.getNamespacePattern(), new String[]
        {
            StringUtils.trimToEmpty(packageName)
        });
    }

    /**
     * The default style (if one hasn't be specified in the model).
     */
    private static final String DEFAULT_STYLE = "wrapped";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getStyle()
     */
    public java.lang.String handleGetStyle()
    {
        String style = (String)this
            .findTaggedValue(WebServiceProfile.TAGGEDVALUE_WEBSERVICE_STYLE);
        if (StringUtils.isEmpty(style))
        {
            style = DEFAULT_STYLE;
        }
        return style;
    }

    /**
     * The default use (if one hasn't be specified in the model).
     */
    private static final String DEFAULT_USE = "literal";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getUse()
     */
    public java.lang.String handleGetUse()
    {
        String use = (String)this
            .findTaggedValue(WebServiceProfile.TAGGEDVALUE_WEBSERVICE_USE);
        if (StringUtils.isEmpty(use))
        {
            use = DEFAULT_USE;
        }
        return use;
    }

    /**
     * Keeps track of whether or not the type has been checked, keeps us from
     * entering infinite loops when calling loadTypes.
     */
    private Collection checkedTypes = new ArrayList();

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTypeMappingElements()
     */
    public java.util.Collection handleGetTypeMappingElements()
    {
        Collection paramTypes = new HashSet();
        Iterator operationIt = this.getAllowedOperations().iterator();
        while (operationIt.hasNext())
        {
            OperationFacade operation = (OperationFacade)operationIt.next();
            paramTypes.addAll(operation.getParameters());
        }

        Collection types = new TreeSet(new TypeComparator());
        Collection nonArrayTypes = new TreeSet(new TypeComparator());
        Iterator paramTypeIt = paramTypes.iterator();
        // clear out the cache of checkedTypes, otherwise
        // they'll be ignored the second time this method is
        // called (if the instance is reused)
        this.checkedTypes.clear();
        while (paramTypeIt.hasNext())
        {
            this.loadTypes(
                (ModelElementFacade)paramTypeIt.next(),
                types,
                nonArrayTypes);
        }

        Collection exceptions = new ArrayList();
        operationIt = this.getAllowedOperations().iterator();
        while (operationIt.hasNext())
        {
            OperationFacade operation = (OperationFacade)operationIt.next();
            exceptions.addAll(operation.getExceptions());
        }

        types.addAll(exceptions);

        // now since we're at the end, and we know the
        // non array types won't override any other types
        // (such as association ends) we
        // add the non array types to the types
        types.addAll(nonArrayTypes);

        return types;
    }

    /**
     * Loads all <code>types</code> and <code>nonArrayTypes</code> for the
     * specified <code>type</code>. For each array type we collect the
     * <code>nonArrayType</code>. Non array types are loaded seperately so
     * that they are added at the end at the type collecting process. Since the
     * types collection are a set (by the fullyQualifiedName) we don't want any
     * non array types to override things such as association ends in the the
     * <code>types</code> collection.
     * 
     * @param type the type
     * @param types the collection to load.
     * @param nonArrayTypes the collection of non array types.
     */
    private void loadTypes(
        ModelElementFacade modelElement,
        Collection types,
        Collection nonArrayTypes)
    {
        final String methodName = "WebServiceImpl.loadTypes";
        ExceptionUtils.checkNull(methodName, "types", types);
        ExceptionUtils.checkNull(methodName, "nonArrayTypes", nonArrayTypes);
        try
        {
            if (modelElement != null
                && !this.checkedTypes.contains(modelElement))
            {
                ClassifierFacade type = this.getType(modelElement);
                // only continue if the model element has a type
                if (type != null)
                {
                    this.checkedTypes.add(modelElement);
                    ClassifierFacade nonArrayType = type;
                    if (type.isArrayType())
                    {
                        // convert to non-array type since we
                        // check if that one has the stereotype
                        nonArrayType = type.getNonArray();
                        types.add(modelElement);
                        // set the type to the non array type since
                        // that will have the attributes
                        type = nonArrayType;
                    }

                    if (nonArrayType
                        .hasStereotype(WebServiceProfile.STEREOTYPE_VALUEOBJECT)
                        || nonArrayType
                            .hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION))
                    {
                        types.add(modelElement);
                        // we add the type when its a non array and has
                        // the correct stereotype (even if we have added
                        // the array type above) since we need to define
                        // both an array and non array in the WSDL if
                        // we are defining an array.
                        nonArrayTypes.add(nonArrayType);
                    }

                    if (type != null)
                    {
                        Collection properties = type.getProperties();
                        if (properties != null && !properties.isEmpty())
                        {
                            Iterator propertyIt = properties.iterator();
                            while (propertyIt.hasNext())
                            {
                                this.loadTypes((ModelElementFacade)propertyIt
                                    .next(), types, nonArrayTypes);
                            }
                        }
                    }
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing loadTypes";
            logger.error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }
    }

    /**
     * The default provider of the service.
     */
    private static final String DEFAULT_PROVIDER = "RPC";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getProvider()
     */
    public java.lang.String handleGetProvider()
    {
        String provider = (String)this
            .findTaggedValue(WebServiceProfile.TAGGEDVALUE_WEBSERVICE_PROVIDER);
        if (StringUtils.isEmpty(provider))
        {
            provider = DEFAULT_PROVIDER;
        }
        return provider;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getWsdlFile()
     */
    public java.lang.String handleGetWsdlFile()
    {
        return '/' + this.getFullyQualifiedName(true).replace('.', '/')
            + ".wsdl";
    }

    /**
     * We use this comparator to actually elimate duplicates instead of sorting
     * like a comparator is normally used.
     */
    private final class TypeComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private TypeComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(Object objectA, Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade aType = getType(a);
            if (aType == null)
            {
                aType = a;
            }
            ModelElementFacade b = (ModelElementFacade)objectB;
            ModelElementFacade bType = getType(b);
            if (bType == null)
            {
                bType = b;
            }
            return collator.compare(aType.getFullyQualifiedName(), bType
                .getFullyQualifiedName());
        }
    }

    /**
     * Gets the <code>type</code> or <code>returnType</code> of the model
     * element (if the model element has a type or returnType).
     * 
     * @param modelElement the model element we'll retrieve the type of.
     */
    private ClassifierFacade getType(Object modelElement)
    {
        try
        {
            ClassifierFacade type = null;
            String typeProperty = "type";
            // only continue if the model element has a type
            if (PropertyUtils.isReadable(modelElement, typeProperty))
            {
                type = (ClassifierFacade)PropertyUtils.getProperty(
                    modelElement,
                    typeProperty);
            }
            // try for return type if type wasn't found
            typeProperty = "returnType";
            if (type == null
                && PropertyUtils.isReadable(modelElement, typeProperty))
            {
                type = (ClassifierFacade)PropertyUtils.getProperty(
                    modelElement,
                    typeProperty);
            }
            return type;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing WebServiceLogicImpl.getType";
            logger.error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }
    }

    static final String NAMESPACE_PREFIX = "namespacePrefix";

    /**
     * Sets the <code>namespacePrefix</code> for the WSDLs type.
     * 
     * @param namespacePrefix the namespace prefix to use for these types.
     */
    public void setNamespacePrefix(String namespacePrefix)
    {
        this.registerConfiguredProperty(NAMESPACE_PREFIX, StringUtils
            .trimToEmpty(namespacePrefix));
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespacePrefix()
     */
    public String handleGetNamespacePrefix()
    {
        return (String)this.getConfiguredProperty(NAMESPACE_PREFIX);
    }

    static final String QNAME_LOCAL_PART_PATTERN = "qualifiedNameLocalPartPattern";

    /**
     * Sets the <code>qualifiedNameLocalPartPattern</code> for this service.
     * 
     * @param qualifiedNameLocalPartPattern the name prefix to use for these
     *        types.
     */
    public void setQualifiedNameLocalPartPattern(
        String qualifiedNameLocalPartPattern)
    {
        this.registerConfiguredProperty(QNAME_LOCAL_PART_PATTERN, StringUtils
            .trimToEmpty(qualifiedNameLocalPartPattern));
    }

    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this service.
     */
    protected String getQualifiedNameLocalPartPattern()
    {
        return (String)this.getConfiguredProperty(QNAME_LOCAL_PART_PATTERN);
    }

    static final String NAMESPACE_PATTERN = "namespacePattern";

    /**
     * Sets the <code>namespacePattern</code> for this service.
     * 
     * @param namespacePattern the pattern to use for creating the namespace.
     */
    public void setNamespacePattern(String namespacePattern)
    {
        this.registerConfiguredProperty(NAMESPACE_PATTERN, StringUtils
            .trimToEmpty(namespacePattern));
    }

    /**
     * Gets the <code>namespacePattern</code> for this service.
     * 
     * @return String the namespace pattern to use.
     */
    protected String getNamespacePattern()
    {
        return (String)this.getConfiguredProperty(NAMESPACE_PATTERN);
    }

    static final String REVERSE_NAMESPACE = "reverseNamespace";

    /**
     * Sets the <code>reverseNamespace</code> for this service.
     * 
     * @param reverseNamespace the pattern to use for creating the namespace.
     */
    public void setReverseNamespace(String reverseNamespace)
    {
        this.registerConfiguredProperty(REVERSE_NAMESPACE, StringUtils
            .trimToEmpty(reverseNamespace));
    }

    /**
     * Gets whether or not <code>reverseNamespace</code> is true/false for
     * this type.
     * 
     * @return boolean true/false
     */
    protected boolean isReverseNamespace()
    {
        return Boolean.valueOf(
            String.valueOf(this.getConfiguredProperty(REVERSE_NAMESPACE)))
            .booleanValue();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbJndiName()
     */
    public java.lang.String handleGetEjbJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this
            .getEjbJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    private static final String EJB_JNDI_NAME_PREFIX = "ejbJndiNamePrefix";

    /**
     * Sets the <code>ejbJndiNamePrefix</code> for an EJB provider.
     * 
     * @param ejbJndiNamePrefix the prefix to use when binding specifying an EJB
     *        provider's JNDI name.
     */
    public void setEjbJndiNamePrefix(String ejbJndiNamePrefix)
    {
        this.registerConfiguredProperty(EJB_JNDI_NAME_PREFIX, StringUtils
            .trimToEmpty(ejbJndiNamePrefix));
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for an EJB provider.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty(EJB_JNDI_NAME_PREFIX);
    }
    
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbHomeInterface()
     */
    public java.lang.String handleGetEjbHomeInterface()
    {
        return MessageFormat.format(this.getEjbHomeInterfacePattern(), new String[]
        {
            StringUtils.trimToEmpty(this.getPackageName()),
            StringUtils.trimToEmpty(this.getName())
        });
    }

    private static final String EJB_HOME_INTERFACE_PATTERN = "ejbHomeInterfacePattern";

    /**
     * Sets the <code>ejbHomeInterfacePattern</code> for an EJB provider.
     * 
     * @param ejbHomeInterfacePattern the pattern to use for the Home interface
     *        of an EJB provider.
     */
    public void setEjbHomeInterfacePattern(String ejbHomeInterfacePattern)
    {
        this.registerConfiguredProperty(EJB_HOME_INTERFACE_PATTERN, StringUtils
            .trimToEmpty(ejbHomeInterfacePattern));
    }

    /**
     * Gets the <code>ejbHomeInterfacePattern</code> for an EJB provider.
     * 
     * @return the EJB Home interface pattern
     */
    protected String getEjbHomeInterfacePattern()
    {
        return (String)this.getConfiguredProperty(EJB_HOME_INTERFACE_PATTERN);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbInterface()
     */
    public java.lang.String handleGetEjbInterface()
    {
        return MessageFormat.format(this.getEjbInterfacePattern(), new String[]
        {
            StringUtils.trimToEmpty(this.getPackageName()),
            StringUtils.trimToEmpty(this.getName())
        });
    }

    private static final String EJB_INTERFACE_PATTERN = "ejbInterfacePattern";

    /**
     * Sets the <code>ejbInterfacePattern</code> for an EJB provider.
     * 
     * @param ejbInterfacePattern the pattern to use for the interface of an EJB
     *        provider.
     */
    public void setEjbInterfacePattern(String ejbInterfacePattern)
    {
        this.registerConfiguredProperty(EJB_INTERFACE_PATTERN, StringUtils
            .trimToEmpty(ejbInterfacePattern));
    }

    /**
     * Gets the <code>ejbInterfacePattern</code> for an EJB provider.
     * 
     * @return the EJB interface pattern
     */
    protected String getEjbInterfacePattern()
    {
        return (String)this.getConfiguredProperty(EJB_INTERFACE_PATTERN);
    }
}