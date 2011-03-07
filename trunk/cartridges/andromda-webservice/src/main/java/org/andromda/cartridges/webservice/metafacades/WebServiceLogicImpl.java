package org.andromda.cartridges.webservice.metafacades;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.andromda.cartridges.webservice.WebServiceGlobals;
import org.andromda.cartridges.webservice.WebServiceUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WebService.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WebService
 * @author Bob Fields
 */
public class WebServiceLogicImpl
    extends WebServiceLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public WebServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(WebServiceLogicImpl.class);

    private static final String DEFAULT = "default";

    /**
     * @return operations filtered by ((WebServiceOperation)object).isExposed()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedOperations()
     */
    protected Collection<OperationFacade> handleGetAllowedOperations()
    {
        List<OperationFacade> operations = new ArrayList<OperationFacade>(this.getOperations());
        CollectionUtils.filter(
            operations,
            new Predicate()
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
            Collections.sort(
                operations,
                new OperationNameComparator());
        }
        return operations;
    }

    /**
     * @return this.getAllowedOperations() separated by " "
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getAllowedMethods()
     */
    protected String handleGetAllowedMethods()
    {
        Collection<String> methodNames = new ArrayList<String>();
        Collection<WebServiceOperation> operations = this.getAllowedOperations();
        if (operations != null && !operations.isEmpty())
        {
            for (WebServiceOperation operation : operations)
            {
                methodNames.add(StringUtils.trimToEmpty(operation.getName()));
            }
        }
        return StringUtils.join(
            methodNames.iterator(),
            " ");
    }

    /**
     * @return this.getName() formatted as this.getQualifiedNameLocalPartPattern()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getQName()
     */
    protected String handleGetQName()
    {
        return MessageFormat.format(
            this.getQualifiedNameLocalPartPattern(),
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @return this.getPackageName() reversed if this.isReverseNamespace()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getNamespace()
     */
    protected String handleGetNamespace()
    {
        String packageName = this.getPackageName();
        if (this.isReverseNamespace())
        {
            packageName = WebServiceUtils.reversePackage(packageName);
        }
        return MessageFormat.format(
            this.getNamespacePattern(),
                StringUtils.trimToEmpty(packageName));
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_DEFAULT_STYLE = "defaultStyle";

    /**
     * @return UMLProfile.TAGGEDVALUE_WEBSERVICE_STYLE or this.getConfiguredProperty(PROPERTY_DEFAULT_STYLE)
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getStyle()
     */
    protected String handleGetStyle()
    {
        String style = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_STYLE);
        if (StringUtils.isBlank(style) || style.equals(DEFAULT))
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
     * @return UMLProfile.TAGGEDVALUE_WEBSERVICE_USE or this.getConfiguredProperty(PROPERTY_DEFAULT_USE
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getUse()
     */
    protected String handleGetUse()
    {
        String use = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_USE);
        if (StringUtils.isBlank(use) || use.equals(DEFAULT))
        {
            use = String.valueOf(this.getConfiguredProperty(PROPERTY_DEFAULT_USE));
        }
        return use;
    }

    /**
     * Sorted list of all type mapping elements (package.class), used to iterate through all elements in a service
     */
    private Set<ModelElementFacade> elementSet = new TreeSet<ModelElementFacade>(new TypeComparator());

    /**
     * Keeps track of whether or not the type has been checked, keeps us from entering infinite loops when calling
     * loadTypes.
     */
    private Collection<ModelElementFacade> checkedTypes = new ArrayList<ModelElementFacade>();

    /**
     * @return this.elementSet types
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTypeMappingElements()
     */
    protected Collection<ModelElementFacade> handleGetTypeMappingElements()
    {
        final Collection<ParameterFacade> parameterTypes = new LinkedHashSet<ParameterFacade>();
        for (final WebServiceOperation operation : this.getAllowedOperations())
        {
            parameterTypes.addAll(operation.getParameters());
        }

        final Set<ModelElementFacade> types = new TreeSet<ModelElementFacade>(new TypeComparator());
        final Collection<ModelElementFacade> nonArrayTypes = new TreeSet<ModelElementFacade>(new TypeComparator());

        // clear out the cache of checkedTypes, otherwise
        // they'll be ignored the second time this method is
        // called (if the instance is reused)
        this.checkedTypes.clear();
        for (final Iterator<ParameterFacade> iterator = parameterTypes.iterator(); iterator.hasNext();)
        {
            this.loadTypes((ModelElementFacade)iterator.next(), types, nonArrayTypes);
        }

        final Collection<ModelElementFacade> exceptions = new ArrayList<ModelElementFacade>();
        for (final WebServiceOperation operation : this.getAllowedOperations())
        {
            exceptions.addAll(operation.getExceptions());
        }

        types.addAll(exceptions);

        // now since we're at the end, and we know the
        // non array types won't override any other types
        // (such as association ends) we
        // add the non array types to the types
        types.addAll(nonArrayTypes);

        this.elementSet = types;
        //setPkgAbbr(types);
        return types;
    }

    /**
     * <p> Loads all <code>types</code> and <code>nonArrayTypes</code> for
     * the specified <code>type</code>. For each array type we collect the
     * <code>nonArrayType</code>. Non array types are loaded separately so
     * that they are added at the end at the type collecting process. Since the
     * types collection is a set (by the fullyQualifiedName) we don't want any
     * non array types to override things such as association ends in the
     * <code>types</code> collection.
     * </p>
     *
     * @param type the type
     * @param types the collection to load.
     * @param nonArrayTypes the collection of non array types.
     */
    private void loadTypes(ModelElementFacade modelElement, Set<ModelElementFacade> types, 
        Collection<ModelElementFacade> nonArrayTypes)
    {
        ExceptionUtils.checkNull("types", types);
        ExceptionUtils.checkNull("nonArrayTypes", nonArrayTypes);

        try
        {
            if (modelElement != null && !this.checkedTypes.contains(modelElement))
            {
                final ClassifierFacade parameterType = this.getType(modelElement);

                // only continue if the model element has a type
                if (parameterType != null)
                {
                    final Set<ModelElementFacade> allTypes = new LinkedHashSet<ModelElementFacade>();
                    allTypes.add(parameterType);

                    // add all generalizations and specializations of the type
                    final Collection<GeneralizableElementFacade> generalizations = parameterType.getAllGeneralizations();

                    if (generalizations != null)
                    {
                        allTypes.addAll(generalizations);
                    }

                    final Collection<GeneralizableElementFacade> specializations = parameterType.getAllSpecializations();

                    if (specializations != null)
                    {
                        allTypes.addAll(specializations);
                    }

                    if (!this.checkedTypes.contains(parameterType))
                    {
                        this.checkedTypes.add(parameterType);
    
                        for (final Iterator allTypesIterator = allTypes.iterator(); allTypesIterator.hasNext();)
                        {
                            ClassifierFacade type = (ClassifierFacade) allTypesIterator.next();
    
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
                                    if (nonArrayType.hasStereotype(UMLProfile.STEREOTYPE_VALUE_OBJECT)
                                            || nonArrayType.isEnumeration())
                                    {
                                        // we add the type when its a non array and
                                        // has the correct stereotype (even if we have
                                        // added the array type above) since we need to
                                        // define both an array and non array in the WSDL
                                        // if we are defining an array.
                                        nonArrayTypes.add(nonArrayType);
                                    }
                                }
                            }
    
                            if (type != null)
                            {
                                final List<? extends ModelElementFacade> properties = type.getProperties();
                                if (properties != null && !properties.isEmpty())
                                {
                                    for (final ModelElementFacade property : properties)
                                    {
                                        this.loadTypes((ModelElementFacade)property, types, nonArrayTypes);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            final String message = "Error performing loadTypes";
            logger.error(throwable);
            throw new MetafacadeException(message, throwable);
        }
    }

    /**
     * Cross reference between package name and namespace abbreviation, used to annotate foreign schema elements
     */
    private Map<String, String> packageAbbr = new TreeMap<String, String>();
    private static final String EMPTY_STRING = "";

    /**
     * Get a unique list of packages populated from the results of GetTypeMappingElements
     * @return pkgAbbr TreeSet containing unique package list
     */
    protected Collection<String> handleGetPackages()
    {
        if (this.elementSet == null || this.elementSet.size()<1)
        {
            this.elementSet = (TreeSet<ModelElementFacade>)handleGetTypeMappingElements();
        }
        setPkgAbbr(this.elementSet);
        String pkgList = EMPTY_STRING;
        for (final String pkg : this.packageAbbr.keySet())
        {
            pkgList += pkg + ", ";
        }
        return this.packageAbbr.keySet();
    }

    /**
     * @param pkgName
     * @return this.packageAbbr.get(pkgName)
     */
    protected String handleGetPkgAbbr(String pkgName)
    {
        if (StringUtils.isBlank(pkgName) || pkgName.length()<1)
        {
            return EMPTY_STRING;
        }
        if (this.elementSet == null || this.elementSet.size()<1)
        {
            this.elementSet = (TreeSet<ModelElementFacade>)handleGetTypeMappingElements();
        }
        if (this.packageAbbr == null || this.packageAbbr.size()<1)
        {
            setPkgAbbr(this.elementSet);
        }
        String rtn = this.packageAbbr.get(pkgName);
        if (StringUtils.isBlank(rtn))
        {
            // Package reference was never added originally - needs to be fixed
            int namespaceCount = this.packageAbbr.size();
            rtn = "ns" + namespaceCount;
            this.packageAbbr.put(pkgName, rtn);
            logger.info(this.getName() + " missing PkgAbbr for " + pkgName);
        }
        return rtn;
    }

    /**
     * Creates a list of sorted unique package names and namespace abbreviations for each one.
     * Run this after running getTypeMappingElements(), to populate the namespace Map.
     * Namespaces are in order ns1 through x
     * @param types
     * @return pkgAbbr
     */
    private Map<String, String> setPkgAbbr(Set<ModelElementFacade> types)
    {
        Map<String, String> pkgAbbr = new TreeMap<String, String>();
        int namespaceCount = 1;
        // Copy package names and abbreviations to package list
        for (final OperationFacade op : this.getOperations())
        {
            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
            {
                ModelElementFacade arg = (ModelElementFacade)opiterator.next();
                String pkg = arg.getPackageName();
                if (!pkgAbbr.containsKey(pkg) && pkg != null && pkg.indexOf('.') > 0)
                {
                    pkgAbbr.put(pkg, "ns" + namespaceCount);
                    namespaceCount++;
                }
            }
            for (final ParameterFacade arg : op.getArguments())
            {
                String pkg = arg.getPackageName();
                if (!pkgAbbr.containsKey(pkg) && pkg != null && pkg.indexOf('.') > 0)
                {
                    pkgAbbr.put(pkg, "ns" + namespaceCount);
                    namespaceCount++;
                }
            }
            if (op.getReturnType()!=null)
            {
                String pkg = op.getReturnType().getPackageName();
                if (!pkgAbbr.containsKey(pkg) && pkg != null && pkg.indexOf('.') > 0)
                {
                    pkgAbbr.put(pkg, "ns" + namespaceCount);
                    namespaceCount++;
                }
            }
        }
        for (final Iterator iterator = types.iterator(); iterator.hasNext();)
        {
            ModelElementFacade type = ((ModelElementFacade)iterator.next());
            String pkg = type.getPackageName();
            if (!pkgAbbr.containsKey(pkg) && pkg != null && pkg.indexOf('.') > 0)
            {
                pkgAbbr.put(pkg, "ns" + namespaceCount);
                namespaceCount++;
            }
        }
        this.packageAbbr = pkgAbbr;
        return pkgAbbr;
    }

    /**
     * Cross reference between package name and collection of foreign package referenced elements
     */
    private Map<String, Set<String>> packageRefs = new HashMap<String, Set<String>>();

    /**
     * Get a unique list of packages referenced by the referring package
     * @param pkg PackageName to find related packages for xs:schema import
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return Collection TreeSet containing referenced package list
     */
    protected Collection<String> handleGetPackageReferences(String pkg, boolean follow)
    {
        //if (this.elementSet == null || this.elementSet.size()<1)
        //{
            this.elementSet = (TreeSet<ModelElementFacade>)handleGetTypeMappingElements();
        //}
        //if (this.packageRefs == null || this.packageRefs.size()<1)
        //{
            setPkgRefs(this.elementSet, follow);
        //}
        return this.packageRefs.get(pkg);
    }

    /**
     * Creates a list of referenced packages for each package.
     * Run this after running getTypeMappingElements(), to populate the namespace Map.
     * @param types TreeSet of unique packageNames referenced in each package
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgAbbr
     */
    private Map<String, Set<String>> setPkgRefs(Set<ModelElementFacade> types, boolean follow)
    {
        // Copy package names and collection of related packages to package references list
        // Iterate through previously collected type references to find all packages referenced by each type
        for (final Iterator<ModelElementFacade> iterator = types.iterator(); iterator.hasNext();)
        {
            try
            {
                MetafacadeBase element = (MetafacadeBase)iterator.next();
                if (element instanceof WSDLTypeLogicImpl)
                {
                    WSDLTypeLogicImpl type = (WSDLTypeLogicImpl)element;
                    String pkg = type.getPackageName();
                    if (pkg != null && pkg.indexOf('.') > 0)
                    {
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (final Iterator<ModelElementFacade> itAttr = type.getAttributes(follow).iterator(); itAttr.hasNext();)
                        {
                            try
                            {
                                ModelElementFacade attr = (itAttr.next());
                                if (getType(attr) != null)
                                {
                                    attr = getType(attr);
                                }
                                addPkgRef(pkg, attr.getPackageName(), attr);
                            }
                            catch (Exception e)
                            {
                                logger.error("WebServiceLogicImpl.setPkgRefs getAttributes: " + e);
                            }
                        }
                    }
                }
                else if (element instanceof WSDLTypeAssociationEndLogicImpl)
                {
                    WSDLTypeAssociationEndLogicImpl type = (WSDLTypeAssociationEndLogicImpl)element;
                    String pkg = type.getPackageName();
                    if (pkg != null && pkg.indexOf('.') > 0)
                    {
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (final Iterator<AssociationEndFacade> otherEnds = type.getType().getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                        {
                            try
                            {
                                ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                if (getType(otherEnd) != null)
                                {
                                    otherEnd = getType(otherEnd);
                                }
                                addPkgRef(pkg, otherEnd.getPackageName(), otherEnd);
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("WebServiceLogicImpl.setPkgRefs getNavigableConnectingEnds: " + e);
                            }
                        }
                    }
                }
                else
                {
                    // Log the type so we can extend this logic later...
                    logger.error("Unexpected element type: " + element);
                }
            }
            catch (Exception e)
            {
                logger.error("WebServiceLogicImpl.setPkgRefs types: " + e);
            }
        }
        // Copy package names and collection of related packages to package references list
        /* for (final Iterator iterator = types.iterator(); iterator.hasNext();)
        {
            TreeSet pkgRef;
            ClassifierFacade element = ((ClassifierFacade)iterator.next());
            String pkg = element.getPackageName();
            if (!packageRefs.containsKey(pkg))
            {
                // TypeComparator disallows adding nonunique referenced packageNames
                pkgRef = new TreeSet(new TypeComparator());
            }
            else
            {
                // Reference to Set contained in pkgAbbr already, can be changed dynamically
                pkgRef = (TreeSet)packageRefs.get(pkg);
            }
            // Duplicates logic in wsdl.vsl so that referenced packages are the same.
            for (final Iterator itAttr = element.getAttributes(follow).iterator(); itAttr.hasNext();)
            {
                ClassifierFacade attr = ((ClassifierFacade)itAttr.next());
                if (getType(attr) != null)
                {
                    attr = getType(attr);
                }
                if (!pkgRef.contains(attr) && attr != null && attr.getPackageName().length() > 0)
                {
                    pkgRef.add(attr.getPackageName());
                }
            }
            for (final Iterator otherEnds = element.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
            {
                ClassifierFacade otherEnd = ((ClassifierFacade)otherEnds.next());
                if (getType(otherEnd) != null)
                {
                    otherEnd = getType(otherEnd);
                }
                if (!pkgRef.contains(otherEnd))
                {
                    pkgRef.add(otherEnd.getPackageName());
                }
            }
            if (!packageRefs.containsKey(pkg))
            {
                packageRefs.put(pkg, pkgRef);
            }
        } */

        // Add references from the operations of the service package itself
        for (final OperationFacade op : this.getOperations())
        {
            for (final Object exception : op.getExceptions())
            {
                ModelElementFacade arg = (ModelElementFacade)exception;
                addPkgRef(this.getPackageName(), arg.getPackageName(), arg);
            }
            for (final ParameterFacade arg : op.getArguments())
            {
                addPkgRef(this.getPackageName(), arg.getPackageName(), arg);
            }
            if (op.getReturnType()!=null)
            {
                String pkg = op.getReturnType().getPackageName();
                addPkgRef(this.getPackageName(), pkg, op.getReturnType());
            }
        }
        return packageRefs;
    }

    private void addPkgRef(String pkg, String pkgRef, ModelElementFacade type)
    {
        Set<String> pkgRefSet;
        if (!packageRefs.containsKey(pkg))
        {
            // TypeComparator disallows adding nonunique referenced packageNames
            pkgRefSet = new TreeSet<String>();
            packageRefs.put(pkg, pkgRefSet);
        }
        else
        {
            // Reference to Set contained in pkgAbbr already, can be changed dynamically
            pkgRefSet = packageRefs.get(pkg);
        }
        if (pkgRef!=null && pkg!=null &&  !pkgRef.equals(pkg) && pkgRef.indexOf('.') > 0 && !pkgRefSet.contains(pkgRef))
        {
            pkgRefSet.add(pkgRef);
            logger.debug("Added pkgRef " + pkg + " references " + pkgRef + " in " + type.getName());
        }
    }

    /**
     * <p> Checks to see if the <code>types</code> collection contains the
     * <code>modelElement</code>. It does this by checking to see if the
     * model element is either an association end or some type of model element
     * that has a type that's an array. If it's either an array <strong>OR
     * </strong> an association end, then we check to see if the type is stored
     * within the <code>types</code> collection. If so, we return true,
     * otherwise we return false.
     * </p>
     *
     * @param types the previously collected types.
     * @param modelElement the model element to check to see if it represents a
     *        <code>many</code> type
     * @return true/false depending on whether or not the model element is a
     *         many type.
     */
    private boolean containsManyType(
        final Collection<ModelElementFacade> types,
        final Object modelElement)
    {
        final ClassifierFacade compareType = this.getClassifier(modelElement);
        boolean containsManyType = false;
        if (compareType != null)
        {
            containsManyType =
                CollectionUtils.find(
                    types,
                    new Predicate()
                    {
                        public boolean evaluate(Object object)
                        {
                            return compareType.equals(getClassifier(object));
                        }
                    }) != null;
        }
        return containsManyType;
    }

    /**
     * Attempts to get the classifier attached to the given <code>element</code>.
     *
     * @param element the element from which to retrieve the classifier.
     * @return the classifier if found, null otherwise
     */
    private ClassifierFacade getClassifier(final Object element)
    {
        ClassifierFacade type = null;
        if (element instanceof AssociationEndFacade)
        {
            AssociationEndFacade end = (AssociationEndFacade)element;
            if (end.isMany())
            {
                type = ((AssociationEndFacade)element).getType();
            }
        }
        else if (element instanceof AttributeFacade)
        {
            type = ((AttributeFacade)element).getType();
        }
        else if (element instanceof ParameterFacade)
        {
            type = ((ParameterFacade)element).getType();
        }
        if (element instanceof ClassifierFacade)
        {
            type = (ClassifierFacade)element;
        }
        if (type != null)
        {
            if (type.isArrayType())
            {
                type = type.getNonArray();
            }
        }
        return type;
    }

    /**
     * Returns true/false depending on whether or not this class represents a valid association end (meaning it has a
     * multiplicity of many)
     *
     * @param modelElement the model element to check.
     * @return true/false
     */
    private boolean isValidAssociationEnd(Object modelElement)
    {
        return modelElement instanceof AssociationEndFacade && ((AssociationEndFacade)modelElement).isMany();
    }

    /**
     * @return this.getConfiguredProperty("defaultProvider")
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getProvider()
     */
    protected String handleGetProvider()
    {
        String provider = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_WEBSERVICE_PROVIDER);
        if (StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = (String)this.getConfiguredProperty("defaultProvider");
        }
        return provider;
    }

    /**
     * @return this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getWsdlFile()
     */
    protected String handleGetWsdlFile()
    {
        return StringUtils.replace(
            this.getFullyQualifiedName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/") + ".wsdl";
    }

    /**
     * We use this comparator to actually eliminate duplicates instead of sorting like a comparator is normally used.
     */
    public final class TypeComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        /**
         * We use this comparator to actually eliminate duplicates instead of sorting like a comparator is normally used.
         */
        public TypeComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(
            Object objectA,
            Object objectB)
        {
            final ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade aType = getType(a);
            if (aType == null)
            {
                aType = a;
            }
            final ModelElementFacade b = (ModelElementFacade)objectB;
            ModelElementFacade bType = getType(b);
            if (bType == null)
            {
                bType = b;
            }
            return collator.compare(
                aType.getFullyQualifiedName(),
                bType.getFullyQualifiedName());
        }
    }

    /**
     * Gets the <code>type</code> or <code>returnType</code> of the model element (if the model element has a type or
     * returnType).
     *
     * @param modelElement the model element we'll retrieve the type of.
     * @return ClassifierFacade Type of modelElement Object
     */
    public ClassifierFacade getType(Object modelElement)
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

    /**
     * namespacePrefix
     */
    static final String NAMESPACE_PREFIX = "namespacePrefix";

    /**
     * @return this.getConfiguredProperty(NAMESPACE_PREFIX)
     * @see org.andromda.cartridges.webservice.metafacades.WSDLType#getNamespacePrefix()
     */
    protected String handleGetNamespacePrefix()
    {
        return (String)this.getConfiguredProperty(NAMESPACE_PREFIX);
    }

    /**
     * qualifiedNameLocalPartPattern
     */
    static final String QNAME_LOCAL_PART_PATTERN = "qualifiedNameLocalPartPattern";

    /**
     * Gets the <code>qualifiedNameLocalPartPattern</code> for this service.
     * @return this.getConfiguredProperty(QNAME_LOCAL_PART_PATTERN)
     */
    protected String getQualifiedNameLocalPartPattern()
    {
        return (String)this.getConfiguredProperty(QNAME_LOCAL_PART_PATTERN);
    }

    /**
     * namespacePattern
     */
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

    /**
     * reverseNamespace
     */
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
     * @return this.getEjbJndiNamePrefix() + ejb/ + this.getFullyQualifiedName()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbJndiName()
     */
    protected String handleGetEjbJndiName()
    {
        StringBuilder jndiName = new StringBuilder();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
        if (StringUtils.isNotBlank(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append('/');
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
        final String property = "ejbJndiNamePrefix";
        return this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property)) : null;
    }

    /**
     * @return this.getEjbHomeInterfacePattern() formatted as this.getPackageName() + this.getName()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbHomeInterface()
     */
    protected String handleGetEjbHomeInterface()
    {
        return MessageFormat.format(
            this.getEjbHomeInterfacePattern(),
                StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName()));
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
     * @return this.getEjbInterfacePattern() formatted as this.getPackageName() + this.getName()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getEjbInterface()
     */
    protected String handleGetEjbInterface()
    {
        return MessageFormat.format(
            this.getEjbInterfacePattern(),
                StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName()));
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
     * @return this.getConfiguredProperty(RPC_CLASS_NAME_PATTERN)
     */
    protected String getRpcClassNamePattern()
    {
        return (String)this.getConfiguredProperty(RPC_CLASS_NAME_PATTERN);
    }

    /**
     * @return this.getRpcClassNamePattern() formatted as this.getPackageName() + this.getName()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getRpcClassName()
     */
    protected String handleGetRpcClassName()
    {
        return MessageFormat.format(
            this.getRpcClassNamePattern(),
                StringUtils.trimToEmpty(this.getPackageName()), StringUtils.trimToEmpty(this.getName()));
    }

    private static final String WSDL_OPERATION_SORT_MODE = "wsdlOperationSortMode";

    /**
     * Used to sort operations by <code>name</code>.
     */
    public final static class OperationNameComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        /**
         *
         */
        public OperationNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(
            Object objectA,
            Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade b = (ModelElementFacade)objectB;

            return collator.compare(
                a.getName(),
                b.getName());
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
        return property != null ? (property.equals(OPERATION_SORT_MODE_NAME) ? (String)property : OPERATION_SORT_MODE_NONE) : OPERATION_SORT_MODE_NONE;
    }

    /**
     * @return !this.getAllRoles().isEmpty()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#isSecured()
     */
    protected boolean handleIsSecured()
    {
        Collection<Role> roles = this.getAllRoles();
        return roles != null && !roles.isEmpty();
    }

    /**
     * Overridden to only allow the exposed operations in the returned roles collection.
     *
     * @see org.andromda.metafacades.uml.Service#getAllRoles()
     */
    public Collection<Role> getAllRoles()
    {
        final Collection<Role> roles = new LinkedHashSet<Role>(this.getRoles());
        CollectionUtils.forAllDo(
            this.getAllowedOperations(),
            new Closure()
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
     * @return this.getPackageName() formatted as this.getConfiguredProperty(TEST_PACKAGE_NAME_PATTERN)
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestPackageName()
     */
    protected String handleGetTestPackageName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_PACKAGE_NAME_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getPackageName());
    }

    /**
     * @return this.getTestPackageName() + '.' + this.getTestName()
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
     * @return this.getName() formatted with this.getConfiguredProperty(TEST_NAME_PATTERN)
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestName()
     */
    protected String handleGetTestName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_NAME_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Represents a "wrapped" style.
     */
    private static final String STYLE_WRAPPED = "wrapped";

    /**
     * @return this.getStyle().equalsIgnoreCase(STYLE_WRAPPED)
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
     * @return this.getStyle().equalsIgnoreCase("document")
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
     * @return this.getStyle().equalsIgnoreCase("rpc")
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
     * @return this.getStyle().equalsIgnoreCase("literal")
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
     * @return this.getStyle().equalsIgnoreCase("encoded")
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
     * @return this.getName() formatted as this.getConfiguredProperty(TEST_IMPLEMENTATION_NAME_PATTERN)
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getTestImplementationName()
     */
    protected String handleGetTestImplementationName()
    {
        return String.valueOf(this.getConfiguredProperty(TEST_IMPLEMENTATION_NAME_PATTERN)).replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @return this.getTestPackageName() + '.' + this.getTestImplementationName()
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getFullyQualifiedTestImplementationName()
     */
    protected String handleGetFullyQualifiedTestImplementationName()
    {
        return this.getTestPackageName() + '.' + this.getTestImplementationName();
    }

    /**

     * @return TypeMappings from WebServiceGlobals.SCHEMA_TYPE_MAPPINGS_URI "schemaTypeMappingsUri"
     * @see org.andromda.cartridges.webservice.metafacades.WebService#getSchemaMappings()
     */
    protected TypeMappings handleGetSchemaMappings()
    {
        final String propertyName = WebServiceGlobals.SCHEMA_TYPE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                mappings.setArraySuffix(this.getArraySuffix());
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                logger.error(errMsg, th);
                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#handleGetAllowedOperationExceptions()
     */
    protected Collection handleGetAllowedOperationExceptions()
    {
        final Collection exceptions = new HashSet();

        // collect the exceptions of all allowed operations into a single set
        for (final OperationFacade operation : this.getAllowedOperations())
        {
            exceptions.addAll(operation.getExceptions());
        }

        return exceptions;
    }

    /**
     * @return packages from this.getAllowedOperations()
     * @see org.andromda.cartridges.webservice.WebServiceUtils#getPackages(WebServiceLogicImpl, Set, boolean)
     */
    public Collection<PackageFacade> getPackages() {
        return new WebServiceUtils().getPackages(this, (Set) this.getAllowedOperations(), true);
    }

    /**
     * @param pkg
     * @return WebServiceUtils().getPkgAbbr(pkg)
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogicImpl#getPkgAbbr(PackageFacade)
     */
    public String getPkgAbbr(PackageFacade pkg) {
        return new WebServiceUtils().getPkgAbbr(pkg);
    }

    /**
     * The property defining if the web service XML should be validated against the wsdl/xsd schema.
     */
    private static final String PROPERTY_SCHEMA_VALIDATION = "schemaValidation";
    private static final String BOOLEAN_FALSE = "false";
    private static final String BOOLEAN_TRUE = "true";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#handleIsSchemaValidation()
     */
    @Override
    protected boolean handleIsSchemaValidation()
    {
        String mode = (String)this.findTaggedValue(WebServiceGlobals.XML_SCHEMA_VALIDATION);
        if (StringUtils.isBlank(mode) || mode.equals(DEFAULT))
        {
            mode = String.valueOf(this.getConfiguredProperty(PROPERTY_SCHEMA_VALIDATION));
        }
        if (StringUtils.isBlank(mode) || mode.equals(DEFAULT))
        {
            mode = BOOLEAN_FALSE;
        }
        return Boolean.parseBoolean(mode);
    }

    /**
     * The property defining the default style to give the web services.
     */
    private static final String PROPERTY_SIMPLE_BINDING_MODE = "simpleBindingMode";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#handleIsSimpleBindingMode()
     */
    @Override
    protected boolean handleIsSimpleBindingMode()
    {
        String mode = (String)this.findTaggedValue(WebServiceGlobals.JAXB_SIMPLE_BINDING_MODE);
        if (StringUtils.isBlank(mode) || mode.equals(DEFAULT))
        {
            mode = String.valueOf(this.getConfiguredProperty(PROPERTY_SIMPLE_BINDING_MODE));
        }
        return Boolean.parseBoolean(mode);
    }

    /**
     * The property defining the Jaxb XJC arguments used with wsdl2java utility.
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getSchemaMappings()
     */
    private static final String PROPERTY_XJC_ARGUMENTS = "xjcArguments";

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getXjcArguments()
     */
    @Override
    protected String handleGetXjcArguments()
    {
        String mode = (String)this.findTaggedValue(WebServiceGlobals.JAXB_XJC_ARGUMENTS);
        if (StringUtils.isBlank(mode) || mode.equals(DEFAULT))
        {
            mode = String.valueOf(this.getConfiguredProperty(PROPERTY_XJC_ARGUMENTS));
        }
        return mode;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestCacheType()
     */
    @Override
    protected String handleGetRestCacheType()
    {
        String cacheType = (String)this.findTaggedValue(WebServiceGlobals.CACHE_TYPE);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(cacheType) || cacheType.equals(DEFAULT))
        {
            cacheType = EMPTY_STRING;
        }
        return cacheType;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestConsumes()
     */
    @Override
    protected String handleGetRestConsumes()
    {
        String consumes = (String)this.findTaggedValue(WebServiceGlobals.REST_CONSUMES);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(consumes) || consumes.equals(DEFAULT))
        {
            consumes = EMPTY_STRING;
        }
        else
        {
            consumes = WebServiceOperationLogicImpl.translateMediaType(consumes);
        }
        return consumes;
    }

    /**
     * Contexts should be in the form fullyqualifiedclassname variable
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestContexts()
     */
    @Override
    protected List<String> handleGetRestContexts()
    {
        List<String> contexts = new ArrayList<String>();
        String context = (String)this.findTaggedValue(WebServiceGlobals.REST_CONTEXT);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(context) || context.equals(DEFAULT))
        {
            context = EMPTY_STRING;
        }
        else
        {
            // Parse comma/pipe/semicolon delimited elements into ArrayList
            String[] parsed = StringUtils.split(context, ",;|");
            for (int i=0; i<parsed.length; i++)
            {
                contexts.add(parsed[i]);
            }
        }
        return contexts;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestMethod()
     */
    @Override
    protected String handleGetRestMethod()
    {
        String method = (String)this.findTaggedValue(WebServiceGlobals.REST_HTTP_METHOD);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(method) || method.equals(DEFAULT))
        {
            method = EMPTY_STRING;
        }
        return method;
    }

    private static final String SLASH = "/";
    private static final String QUOTE = "\"";
    //private static final String LBRACKET = "{";
    //private static final String RBRACKET = "}";
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestPath()
     */
    @Override
    protected String handleGetRestPath()
    {
        String path = (String)this.findTaggedValue(WebServiceGlobals.REST_PATH);
        if (StringUtils.isBlank(path))
        {
            path = EMPTY_STRING;
        }
        if (!(this.getRestCount()>0) || StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            path = QUOTE + SLASH + this.getName().toLowerCase() + SLASH + QUOTE;
        }
        else
        {
            if (!path.startsWith(QUOTE))
            {
                path = QUOTE + path;
            }
            if (!path.endsWith(QUOTE) || path.length()<2)
            {
                path = path + QUOTE;
            }
        }
        return path;
    }

    //private static final String PRODUCE_DEFAULT = "application/xml";
    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestProduces()
     */
    @Override
    protected String handleGetRestProduces()
    {
        return WebServiceOperationLogicImpl.translateMediaType((String)this.findTaggedValue(WebServiceGlobals.REST_PRODUCES));
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestProvider()
     */
    @Override
    protected String handleGetRestProvider()
    {
        String provider = (String)this.findTaggedValue(WebServiceGlobals.REST_PROVIDER);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = EMPTY_STRING;
        }
        return provider;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestRetention()
     */
    @Override
    protected String handleGetRestRetention()
    {
        String retention = (String)this.findTaggedValue(WebServiceGlobals.REST_RETENTION);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(retention) || retention.equals(DEFAULT))
        {
            retention = EMPTY_STRING;
        }
        return retention;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#getRestTarget()
     */
    @Override
    protected String handleGetRestTarget()
    {
        String target = (String)this.findTaggedValue(WebServiceGlobals.REST_TARGET);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(target) || target.equals(DEFAULT))
        {
            target = EMPTY_STRING;
        }
        return target;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#handleIsRestAtom()
     */
    @Override
    protected boolean handleIsRestAtom()
    {
        boolean restAtom = false;
        if (this.getRestCount()>0)
        {
            Collection<WebServiceOperation> operations = this.getAllowedOperations();
            for (WebServiceOperation operation : operations)
            {
                String restProduces = operation.getRestProduces();
                if (StringUtils.isNotBlank(restProduces) && restProduces.contains("atom"))
                {
                    restAtom = true;
                    break;
                }
            }
            if (!restAtom)
            {
                restAtom = StringUtils.isNotBlank(this.getRestProduces()) && this.getRestProduces().indexOf("atom") > -1;
            }
        }
        return restAtom;
    }

    /**
     * @see org.andromda.cartridges.webservice.metafacades.WebServiceLogic#handleGetRestCount()
     */
    @Override
    protected int handleGetRestCount()
    {
        int restCount = 0;
        String rest = (String)this.findTaggedValue(WebServiceGlobals.REST);
        for (WebServiceOperation operation : this.getAllowedOperations())
        {
            if (StringUtils.isNotBlank(rest) && (operation.isRest() || rest.equals(BOOLEAN_TRUE)))
            {
                restCount++;
            }
        }
        return restCount;
    }
}
