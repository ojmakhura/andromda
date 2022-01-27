package org.andromda.cartridges.webservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.andromda.cartridges.OperationNameComparator;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.EnumerationLiteralFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

/**
 * Contains utilities used within the WebService cartridge.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class CartridgeWebServiceUtils
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(CartridgeWebServiceUtils.class);

    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection<Role> getAllRoles(Collection<Service> services)
    {
        final Collection<Role> allRoles = new LinkedHashSet<Role>();
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
     * Cross reference between package name and namespace abbreviation, used to annotate foreign schema elements
     */
    private static Map<PackageFacade, String> packageAbbr = new TreeMap<PackageFacade, String>();

    /** Adds the package namespace abbreviation for this package
     * @param pkg Package for which to get the abbreviation. Uses a static Map so that
     * all schemas globally for this model will use the same namespace abbreviations
     * @param pkgAbbr Package Abbreviation to be added to namespace map
     * @return PackageMap TreeMap of package <-> abbreviation cross references
     */
    public Map<PackageFacade, String> addPkgAbbr(PackageFacade pkg, String pkgAbbr)
    {
        if (packageAbbr==null)
        {
            packageAbbr = new TreeMap<PackageFacade, String>();
        }
        if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
        {
            if (packageAbbr.containsKey(pkg))
            {
                logger.debug(pkg + " abbreviation " + pkgAbbr + " already exists in Package namespace Map");
            }
            else
            {
                packageAbbr.put(pkg, pkgAbbr);
            }
        }
        return packageAbbr;
    }

    /** Get the package namespace abbreviation for this package
     * @param pkg Package for which to get the abbreviation. Uses a static Map so that
     * all schemas globally for this model will use the same namespace abbreviations
     * @return Package abbreviation nsX
     */
    public String getPkgAbbr(PackageFacade pkg)
    {
        return this.getPkgAbbr(pkg, null);
    }

    /** Get the package namespace abbreviation for this package
     * @param pkg Package for which to get the abbreviation. Uses a static Map so that
     * all schemas globally for this model will use the same namespace abbreviations
     * @param currentPackage The package which is referencing the pkg. Return 'impl' if the same as pkg.
     * @return Package abbreviation nsX
     */
    public String getPkgAbbr(PackageFacade pkg, PackageFacade currentPackage)
    {
        if (pkg==null)
        {
            logger.error("getPkgAbbr Null pkg " + packageAbbr.size() + ": " + packageAbbr);
            return "impl";
        }
        if (currentPackage != null && pkg.getFullyQualifiedName().equals(currentPackage.getFullyQualifiedName()))
        {
            return "impl";
        }
        if (packageAbbr==null)
        {
            packageAbbr = new TreeMap<PackageFacade, String>();
        }
        String rtn = packageAbbr.get(pkg);
        String pkgName = pkg.getFullyQualifiedName();
        if (StringUtils.isEmpty(pkgName) || pkgName.startsWith("java.lang") || pkgName.startsWith("java.util")
            || pkgName.endsWith("PrimitiveTypes") || pkgName.endsWith("datatype"))
        {
            // Assume simple types if no package
            rtn="xs";
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("getPkgAbbr " + pkg + ' ' + rtn + ' ' + packageAbbr.size() + ' ' + pkgName);
        }
        if (StringUtils.isEmpty(rtn))
        {
            rtn = (String)pkg.findTaggedValue(CartridgeWebServiceGlobals.XML_XMLNS);
            if (StringUtils.isEmpty(rtn))
            {
                // Package reference was never added originally - needs to be fixed in getPackageReferences()
                int namespaceCount = packageAbbr.size()+1;
                rtn = "ns" + namespaceCount;
            }
            packageAbbr.put(pkg, rtn);
            if (logger.isDebugEnabled())
            {
                logger.debug("getPkgAbbr " + pkg + ' ' + rtn + ' ' + packageAbbr.size());
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("Missing PkgAbbr for " + pkgName + ": added " + rtn);
            }
        }
        return rtn;
    }

    /**
     * Creates a list of referenced exception packages for each package.
     * Populates schema reference list for import for the wsdl
     * @param packageFacade Package / namespace for which to find all related (referenced) packages
     * @return pkgRefs Collection<String> package names
     */
    public Collection<PackageFacade> getExceptionReferences(PackageFacade packageFacade)
    {
        Collection<PackageFacade> pkgRef = new TreeSet<PackageFacade>();
        if (packageFacade != null)
        {
            String name = null;
            PackageFacade pkg = null;
            String packageName = packageFacade.getFullyQualifiedName();
            @SuppressWarnings("unused")
            String pkgRefs = "";
            // Copy package names and collection of related packages to package references list
            for (final ClassifierFacade classifier : packageFacade.getClasses())
            {
                //logger.debug("getPackageReferences packageName=" + packageName);
                if (classifier != null)
                {
                    pkg = (PackageFacade) classifier.getPackage();
                    if (classifier.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE))
                    {
                        // Add references from the operations of the service package itself
                        for (final OperationFacade op : classifier.getOperations())
                        {
                            for (final ModelElementFacade arg : (Collection<ModelElementFacade>)op.getExceptions())
                            {
                                // TODO Why does MEF.getPackage return a ModelElementFacade instead of Package?
                                pkg = (PackageFacade) arg.getPackage();
                                if (pkg != null && !pkg.getFullyQualifiedName().equals(classifier.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add parameter " + pkg + '.' + name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return pkgRef;
    }

    /**
     * Creates a list of referenced packages for each package.
     * Populates schema reference list for import
     * @param packageFacade Package / namespace for which to find all related (referenced) packages
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs Collection<String> package names
     */
    public Collection<PackageFacade> getPackageReferences(PackageFacade packageFacade, boolean follow)
    {
        Collection<PackageFacade> pkgRef = new TreeSet<PackageFacade>();
        if (packageFacade != null)
        {
            String name = null;
            PackageFacade pkg = null;
            String packageName = packageFacade.getFullyQualifiedName();
            String pkgRefs = "";
            // Copy package names and collection of related packages to package references list
            for (final ClassifierFacade facade : packageFacade.getClasses())
            {
                //logger.debug("getPackageReferences packageName=" + packageName);
                if (facade != null)
                {
                    pkg = (PackageFacade) facade.getPackage();
                    if (logger.isDebugEnabled())
                    {
                        name = facade.getName();
                        logger.debug("getPackageReferences packageName=" + packageName + " facade " + pkg + '.' + name);
                    }
                    // Add reference to immediate ancestor to import packages
                    GeneralizableElementFacade generalization = facade.getGeneralization();
                    if (generalization != null)
                    {
                        pkg = (PackageFacade) generalization.getPackage();
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg))
                        {
                            pkgRef.add(pkg);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += pkg + ",";
                                logger.debug("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + '.' + name);
                            }
                        }
                    }
                    if (facade.hasStereotype("ValueObject") || facade.hasStereotype("Exception") || facade.hasStereotype("UnexpectedException") || facade.hasStereotype("ApplicationException"))
                    {
                        // This element is contained in this package, see what it references
                        for (ModelElementFacade attr : (List<ModelElementFacade>)facade.getProperties(follow))
                        {
                            try
                            {
                                ClassifierFacade attrType = getType(attr);
                                if (attrType != null)
                                {
                                    pkg = (PackageFacade) attrType.getPackage();
                                    name = attrType.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + '.' + name);
                                        }
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + '.' + name + ": " + e);
                            }
                        }
                        for (final AssociationEndFacade endFacade : (List<AssociationEndFacade>)facade.getNavigableConnectingEnds(follow))
                        {
                            try
                            {
                                if (getType(endFacade) != null)
                                {
                                    ClassifierFacade otherEnd = getType(endFacade);
                                    pkg = (PackageFacade) otherEnd.getPackage();
                                    name = otherEnd.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + '.' + name);
                                        }
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + '.' + name + ": " + e);
                            }
                        }
                    }
                    else if (facade.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE))
                    {
                        // Add references from the operations of the service package itself
                        for (final OperationFacade op : facade.getOperations())
                        {
                            for (final ModelElementFacade arg : (Collection<ModelElementFacade>)op.getExceptions())
                            {
                                // TODO Why does MEF.getPackage return a ModelElementFacade instead of Package?
                                pkg = (PackageFacade) arg.getPackage();
                                if (pkg != null && !pkg.getFullyQualifiedName().equals(facade.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add parameter " + pkg + '.' + name);
                                    }
                                }
                            }
                            for (final ParameterFacade arg : op.getParameters())
                            {
                                pkg = (PackageFacade) arg.getType().getPackage();
                                name = arg.getType().getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(facade.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add parameter " + pkg + '.' + name);
                                    }
                                }
                            }
                            if (op.getReturnType()!=null)
                            {
                                pkg = (PackageFacade) op.getReturnType().getPackage();
                                name = op.getReturnType().getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(facade.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add return " + pkg + '.' + name);
                                    }
                                }
                            }
                        }
                    }
                    /*else if (facade.isEnumeration() || facade.hasStereotype("Enumeration"))
                    {

                    }*/
                }
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageReferences packageName=" + packageName + ' ' + pkgRefs);
            }
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageReferences packageName=null");
            }
        }
        return pkgRef;
    }

    /**
     * Get all operations under WebService or with WebServiceOperation in a package
     * @param packageFacade Package / namespace for which to find all service operations
     * @return operations
     */
    public List<OperationFacade> getAllowedOperations(PackageFacade packageFacade)
    {
        List<OperationFacade> operations = new ArrayList<OperationFacade>();
        for (final ModelElementFacade mefacade : packageFacade.getOwnedElements())
        {
            if (mefacade != null && mefacade instanceof ClassifierFacade)
            {
                ClassifierFacade facade = (ClassifierFacade)mefacade;
                boolean webService = facade.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE);
                for (final OperationFacade op : facade.getOperations())
                {
                    boolean visibility = op.getVisibility().equals("public") || op.getVisibility().equals("protected");
                    if (visibility && (webService || op.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION)))
                    {
                        operations.add((OperationFacade) op);
                    }
                }
            }
        }
        // Always sort by name so that operations can be easily found in a long list.
        Collections.sort(
                operations,
                new OperationNameComparator());
        return operations;
    }

    /**
     * Get all operations under WebService or with WebServiceOperation in a package
     * @param packageFacade Package / namespace for which to find all service operations
     * @return operations
     */
    public Collection<ModelElementFacade> getAllowedOperationExceptions(PackageFacade packageFacade)
    {
        final Collection<ModelElementFacade> exceptions = new HashSet<ModelElementFacade>();

        // collect the exceptions of all allowed operations into a single set
        for (final OperationFacade operation : this.getAllowedOperations(packageFacade))
        {
            exceptions.addAll(operation.getExceptions());
        }

        return exceptions;
    }

    /**
     * Filter schema types list and related object to only a single package. Called for each referenced package.
     * Run this after running getTypeMappingElements(), to populate the namespace Map and referenced imports from each namespace.
     * @param packageFacade Package / namespace for which to find all related (referenced) packages
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs
     */
    public Collection<ModelElementFacade> getPackageTypes(PackageFacade packageFacade, boolean follow)
    {
        Collection<ModelElementFacade> pkgTypes = new HashSet<ModelElementFacade>();
        if (packageFacade != null)
        {
            String name = null;
            String pkg = null;
            String packageName = packageFacade.getFullyQualifiedName();
            String pkgRefs = "";
            // Copy package names and collection of related packages to package references list
            //ClassifierFacade facade = null;
            for (final ModelElementFacade mefacade : packageFacade.getOwnedElements())
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("getPackageTypes packageName=" + packageName + " element " + mefacade);
                }
                if (mefacade != null && mefacade instanceof ClassifierFacade)
                {
                    ClassifierFacade facade = (ClassifierFacade)mefacade;
                    if (logger.isDebugEnabled())
                    {
                        name = facade.getName();
                        logger.debug("getPackageTypes packageName=" + packageName + " facade " + name);
                    }
                    if (facade.hasStereotype("ValueObject"))
                    {
                        if (!pkgTypes.contains(facade))
                        {
                            pkgTypes.add(facade);
                            if (logger.isDebugEnabled())
                            {
                                name = facade.getName();
                                pkgRefs += name + ',';
                                logger.debug("getPackageTypes packageName=" + packageName + " add facadeValueObject " + name);
                            }
                        }
                    }
                    else if (facade.isEnumeration() || facade.hasStereotype("Enumeration"))
                    {
                        if (!pkgTypes.contains(facade))
                        {
                            pkgTypes.add(facade);
                            if (logger.isDebugEnabled())
                            {
                                name = facade.getName();
                                pkgRefs += name + ',';
                                logger.debug("getPackageTypes packageName=" + packageName + " add facadeEnumeration " + name);
                            }
                        }
                    }
                    else if (facade.hasStereotype("Exception") || facade.hasStereotype("ApplicationException")
                            || facade.hasStereotype("UnexpectedException") || facade.hasStereotype("WebFault"))
                    {
                        if (!pkgTypes.contains(facade))
                        {
                            pkgTypes.add(facade);
                            if (logger.isDebugEnabled())
                            {
                                name = facade.getName();
                                pkgRefs += name + ',';
                                logger.debug("getPackageTypes packageName=" + packageName + " add facadeEnumeration " + name);
                            }
                        }
                    }
                    else if (facade.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE))
                    {
                        // Add references from the operations of the service package itself
                        for (final OperationFacade op : facade.getOperations())
                        {
                            for (final ModelElementFacade arg : (Collection<ModelElementFacade>)op.getExceptions())
                            {
                                pkg = arg.getPackageName();
                                if (pkg!=null && pkg.equals(facade.getPackageName()) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg))
                                {
                                    pkgTypes.add(arg);
                                    if (logger.isDebugEnabled())
                                    {
                                        name = arg.getName();
                                        pkgRefs += name + ',';
                                        logger.debug("getPackageTypes packageName=" + packageName + " add exception " + pkg + '.' + name);
                                    }
                                }
                            }
                            for (final ParameterFacade arg : op.getParameters())
                            {
                                pkg = arg.getType().getPackageName();
                                name = arg.getType().getName();
                                //ClassifierFacade arg = ((ParameterFacade)opiterator.next()).getType();
                                //pkg = arg.getPackageName();
                                if (pkg!=null && pkg.equals(facade.getPackageName()) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg.getType()))
                                {
                                    pkgTypes.add(arg.getType());
                                    if (logger.isDebugEnabled())
                                    {
                                        name = arg.getName();
                                        pkgRefs += name + ',';
                                        logger.debug("getPackageTypes packageName=" + packageName + " add parameter " + pkg + '.' + name);
                                    }
                                }
                            }
                            if (op.getReturnType()!=null)
                            {
                                pkg = op.getReturnType().getPackageName();
                                if (logger.isDebugEnabled())
                                {
                                    name = op.getReturnType().getName();
                                    logger.debug("getPackageTypes packageName=" + packageName + " return " + pkg + '.' + name);
                                }
                                if (pkg!=null && pkg.equals(facade.getPackageName()) && pkg.indexOf('.') > 0 && !pkgTypes.contains(op.getReturnType()))
                                {
                                    pkgTypes.add(op.getReturnType());
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += name + ',';
                                        logger.debug("getPackageTypes packageName=" + packageName + " add return " + pkg + '.' + name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageTypes packageName=" + packageName + ' ' + pkgRefs);
            }
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageTypes packageName=null");
            }
        }
        return pkgTypes;
    }

    /**
     * Gets the <code>type</code> or <code>returnType</code> of the model element (if the model element has a type or
     * returnType). Duplicate of method in WebServiceLogicImpl.
     *
     * @param modelElement the model element we'll retrieve the type of.
     * @return ClassifierFacade Type of modelElement Object
     */
    private ClassifierFacade getType(Object modelElement)
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
     * Determine if an object has schema complex types (associations, multiplicity > 1, or complex attributes).
     * Needed when useAttributes=true so wsdl/xsd outputs attributes instead of elements , no complex type element declarations.
     * If no complex types, only attribute declarations are needed in the object schema definition.
     * Sees if attribute packages are something other than java.lang, java,util, java.math.
     * @param facade Type to determine if it contains complex types
     * @param follow Follow inheritance hierarchy for type when determining complex types
     * @return pkgRefs
     */
    public boolean hasComplexTypes(ClassifierFacade facade, boolean follow)
    {
        boolean rtn = false;
        // Associations to other types are automatically complex, can only be elements
        if (!facade.getNavigableConnectingEnds(follow).isEmpty())
        {
            return true;
        }
        // Determine if attributes are multiple or anything other than simple types
        for (final Object obj : facade.getAttributes(follow))
        {
            AttributeFacade attr = (AttributeFacade)obj;
            if (attr.getUpper() > 1 || attr.getUpper() == -1)
            {
                return true;
            }
            // can't think of an easy way to determine simple type, just look at attribute package / type
            String pkg = attr.getType().getFullyQualifiedName(false);
            if (logger.isDebugEnabled())
            {
                String fqn = attr.getGetterSetterTypeName();
                String cpkg= attr.getType().getPackageName();
                logger.debug("attr=" + attr.getName() + " pkg=" + pkg + " fqn=" + fqn + " cpkg=" + cpkg);
            }
            if (StringUtils.isEmpty(pkg) || pkg.indexOf('.')<1)
            {
                //TODO: Make sure all types are mapped
                // Type mapping is missing? No FQN type, but xs:<type> is still output.
            }
            else if (pkg.length()<9)
            {
                // Assume complex type if package name is too short but still contains '.'
                return true;
            }
            else
            {
                pkg=pkg.substring(0, 9);
                if (!"java.lang".equals(pkg) && !"java.util".equals(pkg) && !"java.math".equals(pkg))
                {
                    return true;
                }
            }
        }
        return rtn;
    }

    /**
     * Creates the package name from the element namespace, following the JAXB rules.
     * Reverse the hostname (separated by .)
     * Add the service context (separated by /)
     * Substitute underscore _ for dash - in name
     * Put _ in front of numeric values for package components.
     *
     * @param namespace the XML namespace.
     * @return the reversed package name.
     */
    public static String getPackageName(String namespace)
    {
        if (StringUtils.isBlank(namespace))
        {
            return "";
        }
        if (namespace.startsWith("http://"))
        {
            namespace = namespace.substring(7);
        }
        if (namespace.endsWith("/"))
        {
            namespace = namespace.substring(0, namespace.length()-1);
        }
        if (namespace.endsWith(".xsd"))
        {
            namespace = namespace.substring(0, namespace.length()-4);
        }
        String hostname = namespace;
        if (namespace.indexOf('/')>0)
        {
            hostname = namespace.substring(0, namespace.indexOf('/'));
            namespace = StringUtils.reverseDelimited(hostname, CartridgeWebServiceGlobals.NAMESPACE_DELIMITER)
                + namespace.substring(namespace.indexOf('/'), namespace.length());
        }
        else
        {
            namespace = StringUtils.reverseDelimited(hostname, CartridgeWebServiceGlobals.NAMESPACE_DELIMITER);
        }
        // TODO Change to tokenizer + pattern matcher
        /*StrTokenizer tok = new StrTokenizer(namespace, WebServiceGlobals.NAMESPACE_DELIMITER);
        for (String token : (List<String>)tok.getTokenList())
        {
            if (token.s)
        }*/
        namespace = StringUtils.replaceChars(namespace, '-', '_');
        namespace = StringUtils.replace(namespace, ".0", "_0");
        namespace = StringUtils.replace(namespace, ".1", "_1");
        namespace = StringUtils.replace(namespace, ".2", "_2");
        namespace = StringUtils.replace(namespace, ".3", "_3");
        namespace = StringUtils.replace(namespace, ".4", "_4");
        namespace = StringUtils.replace(namespace, ".5", "_5");
        namespace = StringUtils.replace(namespace, ".6", "_6");
        namespace = StringUtils.replace(namespace, ".7", "_7");
        namespace = StringUtils.replace(namespace, ".8", "_8");
        namespace = StringUtils.replace(namespace, ".9", "_9");
        namespace = StringUtils.replace(namespace, "$", "");
        namespace = StringUtils.replaceChars(namespace, '/', CartridgeWebServiceGlobals.NAMESPACE_DELIMITER);
        namespace = StringUtils.replace(namespace, ".0", "._0");
        namespace = StringUtils.replace(namespace, ".1", "._1");
        namespace = StringUtils.replace(namespace, ".2", "._2");
        namespace = StringUtils.replace(namespace, ".3", "._3");
        namespace = StringUtils.replace(namespace, ".4", "._4");
        namespace = StringUtils.replace(namespace, ".5", "._5");
        namespace = StringUtils.replace(namespace, ".6", "._6");
        namespace = StringUtils.replace(namespace, ".7", "._7");
        namespace = StringUtils.replace(namespace, ".8", "._8");
        namespace = StringUtils.replace(namespace, ".9", "._9");
        return namespace;
    }

    /**
     * Gets the fully qualified name of a class given the XML Element Name and namespace.
     * Assumes the namespace is the reversed package name.
     *
     * @param elementName the XML element name.
     * @param namespace the XML namespace for the element.
     * @return String the package + element name
     */
    public String getElementClassName(String elementName, String namespace)
    {
        if (StringUtils.isBlank(elementName) || StringUtils.isBlank(namespace))
        {
            return "";
        }
        return CartridgeWebServiceUtils.reversePackage(namespace) + '.' + elementName;
    }

    /**
     * Creates a list of referenced packages for all subclasses referenced by the element.
     * Used to add XmlSeeAlso references to the SEI Interface.
     * @param service WebService containing WS operations with references.
     * @param pkgRef Current List of package references to be modified
     * @return pkgRef Collection<ModelElementFacade> referenced types
     */
    private Collection<PackageFacade> getServiceDescendantPackages(ModelElementFacade element, Collection<PackageFacade> pkgRef, Collection<ModelElementFacade> added)
    {
        if (element==null) return pkgRef;
        ModelElementFacade pkg = element.getPackage();
        if (pkg==null || pkg.getFullyQualifiedName().indexOf('.') < 1
            || pkg.getFullyQualifiedName().startsWith("java.util.")) return pkgRef;
        //name = ex.getName();
        if (!pkgRef.contains(pkg) && pkg instanceof PackageFacade)
        {
            pkgRef.add((PackageFacade)pkg);
        }
        if (logger.isDebugEnabled() && pkg.getName().indexOf('.')>0)
        {
            logger.debug("getServiceDescendantPackages " + element.getFullyQualifiedName() + " package " + pkg.getName() + " size=" + pkgRef.size());
        }
        if (element instanceof ClassifierFacade)
        {
            ClassifierFacade classifier = (ClassifierFacade) element;
            // Use commented and change getAllProperties to getProperties, if only descendant references are needed
            /*for (final GeneralizableElementFacade descendant : classifier.getAllSpecializations())
            {
                pkgRef = getServiceDescendantPackages(descendant, pkgRef);
                // Get all associations and attributes, down the inheritance hierarchy
            }*/
            for (final ModelElementFacade property : (List<ModelElementFacade>)classifier.getAllProperties())
            {
                if (property instanceof AttributeFacade)
                {
                    AttributeFacade attrib = (AttributeFacade) property;
                    if (!attrib.getType().equals(classifier) && !added.contains(attrib.getType())
                        && attrib.getType().getPackageName().indexOf('.')>0)
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getServiceDescendantPackages " + attrib.getName() + ' ' + attrib.getType().getFullyQualifiedName() + " attribute in " + classifier.getFullyQualifiedName() + " size=" + pkgRef.size());
                        }
                        added.add(attrib.getType());
                        pkgRef = getServiceDescendantPackages(attrib.getType(), pkgRef, added);
                    }
                }
                else if (property instanceof AssociationEndFacade)
                {
                    AssociationEndFacade assoc = (AssociationEndFacade) property;
                    if (!assoc.getType().equals(classifier) && !added.contains(assoc.getType())
                        && assoc.getType().getPackageName().indexOf('.')>0)
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getServiceDescendantPackages " + assoc.getName() + ' ' + assoc.getType().getFullyQualifiedName() + " association in " + classifier.getFullyQualifiedName() + " size=" + pkgRef.size());
                        }
                        added.add(assoc.getType());
                        pkgRef = getServiceDescendantPackages(assoc.getType(), pkgRef, added);
                    }
                }
            }
        }
        return pkgRef;
    }

    /**
     * Reverses the <code>packageName</code>.
     *
     * @param packageName the package name to reverse.
     * @return the reversed package name.
     */
    public static String reversePackage(String packageName)
    {
        if (StringUtils.isBlank(packageName))
        {
            return "";
        }
        if (packageName.startsWith("http://"))
        {
            packageName = packageName.substring(7);
        }
        return StringUtils.reverseDelimited(packageName, CartridgeWebServiceGlobals.NAMESPACE_DELIMITER);
    }

    private static FastDateFormat df = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ssZ");

    /**
     * Returns the current Date in the specified format.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.getPattern()))
        {
            df = FastDateFormat.getInstance(format);
        }
        return df.format(new Date());
    }

    /**
     * Returns the current Date in the specified format.
     *
     * @return the current date with the default format .
     */
    public static String getDate()
    {
        return df.format(new Date());
    }

    /**
     * Determine how a model type is mapped to a java implementation type.
     * Used to resolve conflicts between java and webservice type mappings.
     * i.e. webservice maps Integer as BigInteger and Date/Time/DateTime as Calendar, so
     * Value Object attributes will not match webservice attribute types.
     * Called from vsl $webServiceUtils.getTypeMapping($service.
     * @param mappings
     * @param from
     *
     * @return the String mapping type to.
     */
    public String getTypeMapping(TypeMappings mappings, String from)
    {
        // URI is the namespace value in andromda.xml configuration parameter for the type mapping
        //MetafacadeFactory.getInstance().getRegisteredProperty( metafacade, "languageMappingsUri");
        //Object property = metafacade.getConfiguredProperty("languageMappingsUri");
        //TypeMappings mappings = TypeMappings.getInstance(property);
        return mappings.getTo("datatype::"+from);
    }

    /**
     * <p> Returns true if java.lang.* or java.util.* datatype and not many*
     * </p>
     *
     * @param element the ClassifierFacade instance
     * @return if type is one of the PrimitiveTypes and not an array/list
     */
    public static boolean isSimpleType(ModelElementFacade element)
    {
        boolean simple = false;
        String typeName = null;
        ClassifierFacade type = null;
        boolean many = false;
        if (element instanceof AttributeFacade)
        {
            AttributeFacade attrib = (AttributeFacade)element;
            type = attrib.getType();
            many = attrib.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof AssociationEndFacade)
        {
            AssociationEndFacade association = (AssociationEndFacade)element;
            type = association.getType();
            many = association.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof ParameterFacade)
        {
            ParameterFacade param = (ParameterFacade)element;
            type = param.getType();
            many = param.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof AttributeFacade)
        {
            AttributeFacade attrib = (AttributeFacade)element;
            type = attrib.getType();
            many = attrib.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof AssociationEndFacade)
        {
            AssociationEndFacade association = (AssociationEndFacade)element;
            type = association.getType();
            many = association.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof ParameterFacade)
        {
            ParameterFacade param = (ParameterFacade)element;
            type = param.getType();
            many = param.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof ClassifierFacade)
        {
            ClassifierFacade classifier = (ClassifierFacade)element;
            type = classifier;
        }
        else
        {
            return simple;
        }
        typeName = type.getFullyQualifiedName();
        if (type.isPrimitive() || typeName.startsWith("java.lang.") || typeName.startsWith("java.util.")
            || !typeName.contains("."))
        {
            if (!many)
            {
                simple = true;
            }
        }
        return simple;
    }
    
    /**
     * Convert request type to Spring request types
     * 
     * @param requestType
     * @return 
     */
    public static String getSpringRequestType(String requestType) {

        if(StringUtils.isBlank(requestType)) {
            return "@org.springframework.web.bind.annotation.PostMapping";
        }
        
        String[] splits = requestType.split("\\.");        
        String rt = splits[splits.length-1];
        
        if(rt == null) {
            rt = "POST";
        }
        
        if(rt.equals("GET")) {
            return "@org.springframework.web.bind.annotation.GetMapping";
        } else if(rt.equals("DELETE")) {
            return "@org.springframework.web.bind.annotation.DeleteMapping";
        } else if(rt.equals("PATCH")) {
            return "@org.springframework.web.bind.annotation.PatchMapping";
        } else if(rt.equals("PUT")) {
            return "@org.springframework.web.bind.annotation.PutMapping";
        } else {
            return "@org.springframework.web.bind.annotation.PostMapping";
        }
        
    }

    public static boolean isService(ModelElementFacade element) {
        
        boolean service = false;
        for(String stereo : element.getStereotypeNames()) {

            if(stereo.equals("Service")) {
                service = true;
            }
        }

        if(service) {
            return true;
        }
        
        return false;
    }

    public static boolean isServiceOnly(ModelElementFacade obj) {

        boolean service = false;
        boolean webservice = false;
        for(String stereo : obj.getStereotypeNames()) {

            if(stereo.equals("Service")) {
                service = true;
            }

            if(stereo.equals("WebService")) {
                webservice = true;
            }
        }

        if(service && !webservice) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isWebService(ModelElementFacade obj) {

        boolean webservice = false;
        for(String stereo : obj.getStereotypeNames()) {

            if(stereo.equals("WebService")) {
                webservice = true;
            }
        }

        if(webservice) {
            return true;
        }
        
        return false;
    }
}
