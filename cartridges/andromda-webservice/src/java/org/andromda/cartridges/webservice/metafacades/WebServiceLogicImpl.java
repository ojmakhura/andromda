package org.andromda.cartridges.webservice.metafacades;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebService.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebService
 */
public class WebServiceLogicImpl
        extends WebServiceLogic
{
    // ---------------- constructor -------------------------------

    public WebServiceLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedOperations()
     */
    protected java.util.Collection handleGetAllowedOperations()
    {
        List operations = new ArrayList(this.getOperations());
        CollectionUtils.filter(operations, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = WebServiceOperation.class.isAssignableFrom(object.getClass());
                if (valid)
                {
                    valid = ((WebServiceOperation)object).isExposed();
                }
                return valid;
            }
        });
        if (this.getWSDLOperationSortMode().equals(OPERATION_SORT_MODE_NAME))
        {
            Collections.sort(operations, new OperationNameComparator());
        }
        return operations;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedMethods()
     */
    protected java.lang.String handleGetAllowedMethods()
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
    protected String handleGetQName()
    {
        return MessageFormat.format(this.getQualifiedNameLocalPartPattern(), new Object[]{
            StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getNamespace()
     */
    protected java.lang.String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = WebServiceUtils.reversePackage(packageName);
        }
        return MessageFormat.format(this.getNamespacePattern(), new Object[]{StringUtils.trimToEmpty(packageName)});
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_STYLE = "defaultStyle";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getStyle()
     */
    protected java.lang.String handleGetStyle()
    {
        String style = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_STYLE);
        if (StringUtils.isEmpty(style))
        {
            style = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_STYLE));
        }
        return style;
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_USE = "defaultUse";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getUse()
     */
    protected java.lang.String handleGetUse()
    {
        String use = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_USE);
        if (StringUtils.isEmpty(use))
        {
            use = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_USE));
        }
        return use;
    }

    /**
     * Keeps track of whether or not the type has been checked, keeps us from entering infinite loops when calling
     * loadTypes.
     */
    private Collection checkedTypes = new ArrayList();

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTypeMappingElements()
     */
    protected java.util.Collection handleGetTypeMappingElements()
    {
        Collection paramTypes = new HashSet();
        Iterator operationIt = this.getAllowedOperations().iterator();
        while (operationIt.hasNext())
        {
            OperationFacade operation = (OperationFacade)operationIt.next();
            paramTypes.addAll(operation.getParameters());
        }

        Set types = new TreeSet(new TypeComparator());
        Collection nonArrayTypes = new TreeSet(new TypeComparator());
        Iterator paramTypeIt = paramTypes.iterator();
        // clear out the cache of checkedTypes, otherwise
        // they'll be ignored the second time this method is
        // called (if the instance is reused)
        this.checkedTypes.clear();
        while (paramTypeIt.hasNext())
        {
            this.loadTypes((ModelElementFacade)paramTypeIt.next(), types, nonArrayTypes);
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
     * <p/>
     * Loads all <code>types</code> and <code>nonArrayTypes</code> for the specified <code>type</code>. For each array
     * type we collect the <code>nonArrayType</code>. Non array types are loaded seperately so that they are added at
     * the end at the type collecting process. Since the types collection is a set (by the fullyQualifiedName) we don't
     * want any non array types to override things such as association ends in the <code>types</code> collection. </p>
     *
     * @param type          the type
     * @param types         the collection to load.
     * @param nonArrayTypes the collection of non array types.
     */
    private void loadTypes(ModelElementFacade modelElement, Set types, Collection nonArrayTypes)
    {
        final String methodName = "WebServiceImpl.loadTypes";
        ExceptionUtils.checkNull(methodName, "types", types);
        ExceptionUtils.checkNull(methodName, "nonArrayTypes", nonArrayTypes);
        try
        {
            if (modelElement != null && !this.checkedTypes.contains(modelElement))
            {
                ClassifierFacade type = this.getType(modelElement);
                // only continue if the model element has a type
                if (type != null)
                {
                    this.checkedTypes.add(modelElement);
                    if (!this.containsManyType(types, modelElement))
                    {
                        ClassifierFacade nonArrayType = type;
                        final boolean arrayType = type.isArrayType();
                        if (arrayType || this.isValidAssociationEnd(modelElement))
                        {
                            types.add(modelElement);
                            if (arrayType)
                            {
                                // convert to non-array type since we
                                // check if that one has the stereotype
                                nonArrayType = type.getNonArray();
                                // set the type to the non array type since
                                // that will have the attributes
                                type = nonArrayType;
                            }
                        }
                        if (nonArrayType != null)
                        {
                            if (nonArrayType.hasStereotype(UMLProfile.STEREOTYPE_VALUE_OBJECT) ||
                                    nonArrayType.isEnumeration())
                            {
                                // we add the type when its a non array and has
                                // the correct stereotype (even if we have added
                                // the array type above) since we need to define
                                // both an array and non array in the WSDL if
                                // we are defining an array.
                                nonArrayTypes.add(nonArrayType);
                            }
                        }
                    }
                    if (type != null)
                    {
                        Collection properties = type.getProperties();
                        if (properties != null && !properties.isEmpty())
                        {
                            Iterator propertyIt = properties.iterator();
                            while (propertyIt.hasNext())
                            {
                                ModelElementFacade property = (ModelElementFacade)propertyIt.next();
                                this.loadTypes(property, types, nonArrayTypes);
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
     * <p/>
     * Checks to see if the <code>types</code> collection contains the <code>modelElement</code>. It does this by
     * checking to see if the model element is either an association end or some type of model element that has a type
     * that's an array. If it's either an array <strong>OR </strong> an association end, then we check to see if the
     * type is stored within the <code>types</code> collection. If so, we return true, otherwise we return false. </p>
     *
     * @param types        the previously collected types.
     * @param modelElement the model element to check to see if it represents a <code>many</code> type
     * @return true/false depending on whether or not the model element is a many type.
     */
    private boolean containsManyType(final Collection types, final Object modelElement)
    {
        ClassifierFacade classifier = null;
        if (modelElement instanceof AssociationEndFacade)
        {
            AssociationEndFacade end = (AssociationEndFacade)modelElement;
            if (end.isMany())
            {
                classifier = ((AssociationEndFacade)modelElement).getType();
            }
        }
        else if (modelElement instanceof ClassifierFacade)
        {
            classifier = (ClassifierFacade)modelElement;
        }
        else if (modelElement instanceof ParameterFacade)
        {
            classifier = ((ParameterFacade)modelElement).getType();
        }
        if (classifier != null)
        {
            if (classifier.isArrayType())
            {
                classifier = classifier.getNonArray();
            }
        }
        final ClassifierFacade compareType = classifier;
        boolean containsManyType = false;
        if (compareType != null)
        {
            containsManyType = CollectionUtils.find(types, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    if (object != null)
                    {
                        ClassifierFacade type = null;
                        if (object instanceof AssociationEndFacade)
                        {
                            AssociationEndFacade end = (AssociationEndFacade)object;
                            if (end.isMany())
                            {
                                type = ((AssociationEndFacade)object).getType();
                            }
                        }
                        else if (object instanceof ClassifierFacade)
                        {
                            type = (ClassifierFacade)object;
                            if (type.isArrayType())
                            {
                                type = type.getNonArray();
                            }
                            else
                            {
                                type = null;
                            }
                        }
                        if (type != null)
                        {
                            valid = type.equals(compareType);
                        }
                    }
                    return valid;
                }
            }) != null;
        }
        return containsManyType;
    }

    /**
     * Returns true/false depending on whether or not this class represents a valid association end (meaning it has a
     * multiplicify of many)
     *
     * @param modelElement the model element to check.
     * @return true/false
     */
    private boolean isValidAssociationEnd(Object modelElement)
    {
        return modelElement instanceof AssociationEndFacade && ((AssociationEndFacade)modelElement).isMany();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getProvider()
     */
    protected java.lang.String handleGetProvider()
    {
        String provider = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_PROVIDER);
        if (StringUtils.isEmpty(provider))
        {
            provider = (String)this.getConfiguredProperty("defaultProvider");
        }
        return provider;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getWsdlFile()
     */
    protected java.lang.String handleGetWsdlFile()
    {
        return '/' + StringUtils.replace(this.getFullyQualifiedName(),
                String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)), "/") +
                ".wsdl";
    }

    /**
     * We use this comparator to actually elimate duplicates instead of sorting like a comparator is normally used.
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
            return collator.compare(aType.getFullyQualifiedName(), bType.getFullyQualifiedName());
        }
    }

    /**
     * Gets the <code>type</code> or <code>returnType</code> of the model element (if the model element has a type or
     * returnType).
     *
     * @param modelElement the model element we'll retrieve the type of.
     */
    protected ClassifierFacade getType(Object modelElement)
    {
        try
        {
            final Introspector introspector = Introspector.instance();
            ClassifierFacade type = null;
            String typeProperty = "type";
            // only continue if the model element has a type
            if (introspector.isReadable(modelElement, typeProperty))
            {
                type = (ClassifierFacade)introspector.getProperty(modelElement, typeProperty);
            }
            // try for return type if type wasn't found
            typeProperty = "returnType";
            if (type == null && introspector.isReadable(modelElement, typeProperty))
            {
                type = (ClassifierFacade)introspector.getProperty(modelElement, typeProperty);
            }
            return type;
        }
        catch (final Throwable throwable)
        {
            String errMsg = "Error performing WebServiceLogicImpl.getType";
            logger.error(errMsg, throwable);
            throw new MetafacadeException(errMsg, throwable);
        }
    }

    static final String NAMESPACE_PREFIX = "namespacePrefix";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespacePrefix()
     */
    protected String handleGetNamespacePrefix()
    {
        return (String)this.getConfiguredProperty(NAMESPACE_PREFIX);
    }

    static final String QNAME_LOCAL_PART_PATTERN = "qualifiedNameLocalPartPattern";

    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this service.
     */
    protected String getQualifiedNameLocalPartPattern()
    {
        return (String)this.getConfiguredProperty(QNAME_LOCAL_PART_PATTERN);
    }

    static final String NAMESPACE_PATTERN = "namespacePattern";

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
     * Gets whether or not <code>reverseNamespace</code> is true/false for this type.
     *
     * @return boolean true/false
     */
    protected boolean isReverseNamespace()
    {
        return Boolean.valueOf(String.valueOf(this.getConfiguredProperty(REVERSE_NAMESPACE))).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbJndiName()
     */
    protected java.lang.String handleGetEjbJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for an EJB provider.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty("ejbJndiNamePrefix");
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbHomeInterface()
     */
    protected java.lang.String handleGetEjbHomeInterface()
    {
        return MessageFormat.format(this.getEjbHomeInterfacePattern(), new Object[]{
            StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the <code>ejbHomeInterfacePattern</code> for an EJB provider.
     *
     * @return the EJB Home interface pattern
     */
    protected String getEjbHomeInterfacePattern()
    {
        return (String)this.getConfiguredProperty("ejbHomeInterfacePattern");
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbInterface()
     */
    protected java.lang.String handleGetEjbInterface()
    {
        return MessageFormat.format(this.getEjbInterfacePattern(), new Object[]{
            StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the <code>ejbInterfacePattern</code> for an EJB provider.
     *
     * @return the EJB interface pattern
     */
    protected String getEjbInterfacePattern()
    {
        return (String)this.getConfiguredProperty("ejbInterfacePattern");
    }

    private static final String RPC_CLASS_NAME_PATTERN = "rpcClassNamePattern";

    /**
     * Gets the <code>rpcClassNamePattern</code> for this service.
     */
    protected String getRpcClassNamePattern()
    {
        return (String)this.getConfiguredProperty(RPC_CLASS_NAME_PATTERN);
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getRpcClassName()
     */
    protected String handleGetRpcClassName()
    {
        return MessageFormat.format(this.getRpcClassNamePattern(), new Object[]{
            StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName())});
    }

    private static final String WSDL_OPERATION_SORT_MODE = "wsdlOperationSortMode";

    /**
     * Used to sort operations by <code>name</code>.
     */
    private final static class OperationNameComparator
            implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private OperationNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(Object objectA, Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade b = (ModelElementFacade)objectB;

            return collator.compare(a.getName(), b.getName());
        }
    }

    /**
     * The model specifying operations should be sorted by name.
     */
    private static final String OPERATION_SORT_MODE_NAME = "name";

    /**
     * The model specifying operations should NOT be sorted.
     */
    private static final String OPERATION_SORT_MODE_NONE = "none";

    /**
     * Gets the sort mode WSDL operations.
     *
     * @return String
     */
    private String getWSDLOperationSortMode()
    {
        Object property = this.getConfiguredProperty(WSDL_OPERATION_SORT_MODE);
        return property != null || property.equals(OPERATION_SORT_MODE_NAME) ?
                (String)property : OPERATION_SORT_MODE_NONE;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isSecured()
     */
    protected boolean handleIsSecured()
    {
        Collection roles = this.getAllRoles();
        return roles != null && !roles.isEmpty();
    }

    /**
     * Overridden to only allow the exposed operations in the returned roles collection.
     *
     * @see org.andromda.metafacades.uml.Service#getAllRoles()
     */
    public Collection getAllRoles()
    {
        final Collection roles = new HashSet(this.getRoles());
        CollectionUtils.forAllDo(this.getAllowedOperations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object != null && ServiceOperation.class.isAssignableFrom(object.getClass()))
                {
                    roles.addAll(((ServiceOperation)object).getRoles());
                }
            }
        });
        return roles;
    }

    /**
     * The pattern used to construct the test package name.
     */
    private static final String TEST_PACKAGE_NAME_PATTERN = "testPackageNamePattern";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestPackageName()
     */
    protected String handleGetTestPackageName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_PACKAGE_NAME_PATTERN)).replaceAll("\\{0\\}",
                this.getPackageName());
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getFullyQualifiedTestName()
     */
    protected String handleGetFullyQualifiedTestName()
    {
        return this.getTestPackageName() + '.' + this.getTestName();
    }

    /**
     * The pattern used to construct the test name.
     */
    private static final String TEST_NAME_PATTERN = "testNamePattern";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestName()
     */
    protected String handleGetTestName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_NAME_PATTERN)).replaceAll("\\{0\\}", this.getName());
    }

    /**
     * Represents a "wrapped" style.
     */
    private static final String STYLE_WRAPPED = "wrapped";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isWrappedStyle()
     */
    protected boolean handleIsWrappedStyle()
    {
        return this.getStyle().equalsIgnoreCase(STYLE_WRAPPED);
    }

    /**
     * Represents a "document" style.
     */
    private static final String STYLE_DOCUMENT = "document";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isDocumentStyle()
     */
    protected boolean handleIsDocumentStyle()
    {
        return this.getStyle().equalsIgnoreCase(STYLE_DOCUMENT);
    }

    /**
     * Represents a "rpc" style.
     */
    private static final String STYLE_RPC = "rpc";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isRpcStyle()
     */
    protected boolean handleIsRpcStyle()
    {
        return this.getStyle().equalsIgnoreCase(STYLE_RPC);
    }

    /**
     * Represents an "literal" use.
     */
    private static final String USE_LITERAL = "literal";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isLiteralUse()
     */
    protected boolean handleIsLiteralUse()
    {
        return this.getStyle().equalsIgnoreCase(USE_LITERAL);
    }

    /**
     * Represents an "encoded" use.
     */
    private static final String USE_ENCODED = "encoded";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isEncodedUse()
     */
    protected boolean handleIsEncodedUse()
    {
        return this.getStyle().equalsIgnoreCase(USE_ENCODED);
    }

    /**
     * The pattern used to construct the test implementation name.
     */
    private static final String TEST_IMPLEMENTATION_NAME_PATTERN = "testImplementationNamePattern";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestImplementationName()
     */
    protected String handleGetTestImplementationName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_IMPLEMENTATION_NAME_PATTERN)).replaceAll("\\{0\\}",
                this.getName());
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getFullyQualifiedTestImplementationName()
     */
    protected String handleGetFullyQualifiedTestImplementationName()
    {
        return this.getTestPackageName() + '.' + this.getTestImplementationName();
    }
}