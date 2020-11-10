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
import org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType;
import org.andromda.cartridges.webservice.metafacades.WSDLEnumerationTypeLogic;
import org.andromda.cartridges.webservice.metafacades.WSDLType;
import org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd;
import org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEndLogic;
import org.andromda.cartridges.webservice.metafacades.WSDLTypeAttributeLogic;
import org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic;
import org.andromda.cartridges.webservice.metafacades.WebServiceLogic;
import org.andromda.cartridges.webservice.metafacades.WebServiceLogicImpl;
import org.andromda.cartridges.webservice.metafacades.WebServiceLogicImpl.OperationNameComparator;
import org.andromda.cartridges.webservice.metafacades.WebServiceOperation;
import org.andromda.cartridges.webservice.metafacades.WebServiceParameter;
import org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogic;
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
public class WebServiceUtils
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(WebServiceUtils.class);

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

    /**
     * Creates a list of referenced packages for the service. Populates pkgAbbr static map.
     * I tried this in the WebServiceLogicImpl metafacade but couldn't get repeatable results.
     * Called from jaxws-included.xsd for CXF implementation
     * @param service WebServiceLogicImpl The service for which to find referenced packages
     * @param types Set<MetafacadeBase> of all serviceOperations, from $service.allowedOperations
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs Collection<PackageFacade> - all referenced packages
     */
    public Collection<PackageFacade> getPackages(WebServiceLogicImpl service, Set<ModelElementFacade> types, boolean follow)
    {
        return setPkgAbbr(service, types, follow);
    }

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
            rtn = (String)pkg.findTaggedValue(WebServiceGlobals.XML_XMLNS);
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
     * Creates a list of sorted unique package names and namespace abbreviations for each one.
     * Run this after running getTypeMappingElements(), to populate the namespace Map.
     * Namespaces are in order ns1 through x
     * @param types
     * @return pkgAbbr
     */
    private Set<PackageFacade> setPkgAbbr(WebServiceLogicImpl service, Set<ModelElementFacade> types, boolean follow)
    {
        // Contains references to only packages needed from this service
        Set<PackageFacade> pkgSet = new TreeSet<PackageFacade>();
        if (logger.isDebugEnabled())
        {
            logger.debug(service.getName() + " setPkgAbbr");
        }
        int namespaceCount = packageAbbr.size() + 1;
        PackageFacade pkg = (PackageFacade) service.getPackage();
        String name = null;
        if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
        {
            if (!pkgSet.contains(pkg))
            {
                pkgSet.add(pkg);
            }
            if (!packageAbbr.containsKey(pkg))
            {
                packageAbbr.put(pkg, "ns" + namespaceCount);
                if (logger.isDebugEnabled())
                {
                    logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + service.getName() + " servicePackage");
                }
                namespaceCount++;
            }
        }
        // Copy package names and abbreviations to package list
        for (final WebServiceOperation op : service.getAllowedOperations())
        {
            for (final ModelElementFacade arg : (Collection<ModelElementFacade>)op.getExceptions())
            {
                pkg = (PackageFacade) arg.getPackage();
                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (!pkgSet.contains(pkg))
                    {
                        pkgSet.add(pkg);
                    }
                    if (!packageAbbr.containsKey(pkg))
                    {
                        packageAbbr.put(pkg, "ns" + namespaceCount);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + op.getName() + " getExceptions " + arg.getName());
                        }
                        namespaceCount++;
                    }
                }
            }
            for (final ParameterFacade arg : op.getParameters())
            {
                pkg = (PackageFacade) arg.getType().getPackage();
                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (!pkgSet.contains(pkg))
                    {
                        pkgSet.add(pkg);
                    }
                    if (!packageAbbr.containsKey(pkg))
                    {
                        packageAbbr.put(pkg, "ns" + namespaceCount);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + op.getName() + " getParameters " + arg.getName());
                        }
                        namespaceCount++;
                    }
                }
            }
            if (op.getReturnType()!=null)
            {
                pkg = (PackageFacade) op.getReturnType().getPackage();
                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (!pkgSet.contains(pkg))
                    {
                        pkgSet.add(pkg);
                    }
                    if (!packageAbbr.containsKey(pkg))
                    {
                        packageAbbr.put(pkg, "ns" + namespaceCount);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + op.getName() + " getReturnType " + op.getReturnType().getName());
                        }
                        namespaceCount++;
                    }
                }
            }
        }
        for (final ModelElementFacade element : types)
        {
            ClassifierFacade facade = service.getType(element);
            if (facade != null)
            {
                pkg = (PackageFacade) facade.getPackage();
                if (logger.isDebugEnabled())
                {
                    name = facade.getName();
                    logger.debug("setPkgAbbr facade " + pkg + '.' + name);
                }
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    // This element is contained in this package, see what it references
                    for (ModelElementFacade attr : (Collection<ModelElementFacade>)facade.getProperties(follow))
                    {
                        try
                        {
                            ClassifierFacade attrType = getType(attr);
                            if (attrType != null)
                            {
                                pkg = (PackageFacade) attrType.getPackage();
                                name = attrType.getName();
                                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    if (!pkgSet.contains(pkg))
                                    {
                                        pkgSet.add(pkg);
                                    }
                                    if (!packageAbbr.containsKey(pkg))
                                    {
                                        packageAbbr.put(pkg, "ns" + namespaceCount);
                                        if (logger.isDebugEnabled())
                                        {
                                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + facade.getName());
                                        }
                                        namespaceCount++;
                                    }
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + facade.getName());
                        }
                    }
                    for (AssociationEndFacade otherEnd : (List<AssociationEndFacade>)facade.getNavigableConnectingEnds(follow))
                    {
                        try
                        {
                            ClassifierFacade endType = getType(otherEnd);
                            if (endType != null)
                            {
                                pkg = (PackageFacade) endType.getPackage();
                                name = endType.getName();
                                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    if (!pkgSet.contains(pkg))
                                    {
                                        pkgSet.add(pkg);
                                    }
                                    if (!packageAbbr.containsKey(pkg))
                                    {
                                        packageAbbr.put(pkg, "ns" + namespaceCount);
                                        if (logger.isDebugEnabled())
                                        {
                                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + facade.getName());
                                        }
                                        namespaceCount++;
                                    }
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + facade.getName());
                        }
                    }
                }
            }
            // TODO remove 'else' and add ParameterFacade logic type
            else if (element instanceof ClassifierFacade)
            {
                ClassifierFacade type = (ClassifierFacade)element;
                if (getType(type) != null)
                {
                    type = getType(type);
                }
                pkg = (PackageFacade) type.getPackage();
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    //int cnt = type.getAttributes(follow).size();
                    /*if (logger.isDebugEnabled())
                    {
                        logger.debug("WSDLTypeLogicImpl pkg=" + pkg + " name=" + type.getName() + " size=" + cnt + " propsize=" + type.getProperties(follow).size());
                    }*/
                    // Duplicates logic in jaxws-included.vsl so that referenced packages are the same.
                    // Except that vsl uses getAttributes, not getProperties. getProperties returns all attributes, getAttributes does not.
                    for (ModelElementFacade attr : (List<ModelElementFacade>)type.getProperties(follow))
                    {
                        try
                        {
                            ClassifierFacade attrType = getType(attr);
                            if (getType(attrType) != null)
                            {
                                pkg = (PackageFacade) attrType.getPackage();
                                name = attrType.getName();
                                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    if (!pkgSet.contains(pkg))
                                    {
                                        pkgSet.add(pkg);
                                    }
                                    if (!packageAbbr.containsKey(pkg))
                                    {
                                        packageAbbr.put(pkg, "ns" + namespaceCount);
                                        if (logger.isDebugEnabled())
                                        {
                                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName() + '.' + name);
                                        }
                                        namespaceCount++;
                                    }
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName() + ": " + e);
                        }
                    }
                    for (AssociationEndFacade otherEnd : (List<AssociationEndFacade>)type.getNavigableConnectingEnds(follow))
                    {
                        try
                        {
                            ClassifierFacade endType = getType(otherEnd);
                            if (endType != null)
                            {
                                pkg = (PackageFacade) endType.getPackage();
                                name = endType.getName();
                                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    if (!pkgSet.contains(pkg))
                                    {
                                        pkgSet.add(pkg);
                                    }
                                    if (!packageAbbr.containsKey(pkg))
                                    {
                                        packageAbbr.put(pkg, "ns" + namespaceCount);
                                        if (logger.isDebugEnabled())
                                        {
                                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName() + '.' + name);
                                        }
                                        namespaceCount++;
                                    }
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName() + ": " + e);
                        }
                    }
                }
            }
            else if (element instanceof AssociationEndFacade)
            {
                AssociationEndFacade type = (AssociationEndFacade)element;
                facade = getType(type);
                if (facade instanceof AssociationEndFacade)
                {
                    type = (AssociationEndFacade)facade;
                }
                pkg = (PackageFacade) type.getPackage();
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (facade != null && facade instanceof ClassifierFacade)
                    {
                        ClassifierFacade typeLogic = facade;
                        pkg = (PackageFacade) typeLogic.getPackage();
                        name = typeLogic.getName();
                        if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                        {
                            if (!pkgSet.contains(pkg))
                            {
                                pkgSet.add(pkg);
                            }
                            if (!packageAbbr.containsKey(pkg))
                            {
                                packageAbbr.put(pkg, "ns" + namespaceCount);
                                if (logger.isDebugEnabled())
                                {
                                    logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName());
                                }
                                namespaceCount++;
                            }
                        }
                    }
                    else
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("setPkgAbbr element association " + pkg + '.' + name);
                        }
                        name = type.getName();
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (AssociationEndFacade otherEnd : (List<AssociationEndFacade>)type.getType().getNavigableConnectingEnds(follow))
                        {
                            try
                            {
                                ClassifierFacade endType = getType(otherEnd);
                                if (endType != null)
                                {
                                    pkg = (PackageFacade) endType.getPackage();
                                    name = endType.getName();
                                    if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        if (!pkgSet.contains(pkg))
                                        {
                                            pkgSet.add(pkg);
                                        }
                                        if (!packageAbbr.containsKey(pkg))
                                        {
                                            packageAbbr.put(pkg, "ns" + namespaceCount);
                                            if (logger.isDebugEnabled())
                                            {
                                                logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName());
                                            }
                                            namespaceCount++;
                                        }
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.debug("setPkgAbbr error in service " + pkg + '.' + name + " ns" + namespaceCount);
                            }
                        }
                    }
                }
            }
            else if (element instanceof EnumerationFacade)
            {
                EnumerationFacade type = (EnumerationFacade)element;
                if (getType(type) != null)
                {
                    type = (EnumerationFacade)getType(type);
                }
                pkg = (PackageFacade) type.getPackage();
                if (pkg!=null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (!pkgSet.contains(pkg))
                    {
                        pkgSet.add(pkg);
                    }
                    if (!packageAbbr.containsKey(pkg))
                    {
                        packageAbbr.put(pkg, "ns" + namespaceCount);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(service.getName() + " ns" + namespaceCount + ' ' + pkg + ' ' + type.getName() + '.' + name);
                        }
                        namespaceCount++;
                    }
                }
            }
            else if (element.getName().endsWith("[]"))
            {
                // Ignore modeled array types - assume non-array type is already in the package
            }
            else
            {
                // Log the type so we can extend this logic later...
                logger.error("setPkgAbbr Unexpected element in service " + pkg + '.' + name + " type: " + element);
            }
        }
        return pkgSet;
    }

    /**
     * Creates a list of referenced packages for each package.
     * Run this after running getTypeMappingElements(), to populate the namespace Map and referenced imports from each namespace.
     * @param service WebService containing WS operations with references.
     * @param types Collection<String> of all schemaTypeMappings referenced in the WebService class operations
     * @param packageName Package / namespace for which to find all related (referenced) packages
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs
     */
    public Collection<PackageFacade> getPackageReferences(WebServiceLogicImpl service, Set<MetafacadeBase> types, String packageName, boolean follow)
    {
        Collection<PackageFacade> pkgRef = new TreeSet<PackageFacade>();
        if (StringUtils.isNotBlank(packageName))
        {
            String name = null;
            PackageFacade pkg = null;
            String pkgRefs = "";
            if (types!=null)
            {
                // Copy package names and collection of related packages to package references list
                for (final MetafacadeBase element : types)
                {
                    ClassifierFacade facade = service.getType(element);
                    if (facade != null)
                    {
                        pkg = (PackageFacade) facade.getPackage();
                        if (logger.isDebugEnabled())
                        {
                            name = facade.getName();
                            //logger.debug("getPackageReferences packageName=" + packageName + " facade " + pkg + "." + name);
                        }
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            // This element is contained in this package, see what it references
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
                    }
                    // TODO remove 'else' and add ParameterFacade logic type
                    else if (element instanceof ClassifierFacade)
                    {
                        ClassifierFacade type = (ClassifierFacade)element;
                        if (getType(type) != null)
                        {
                            type = getType(type);
                        }
                        pkg = (PackageFacade) type.getPackage();
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " ClassifierFacade " + pkg + '.' + name);
                        }
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                            for (ModelElementFacade attr : (List<ModelElementFacade>)type.getProperties(follow))
                            {
                                try
                                {
                                    ClassifierFacade attrType = getType(attr);
                                    if (attrType != null)
                                    {
                                        pkg = (PackageFacade) attr.getPackage();
                                        name = attr.getName();
                                    }
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add attribute " + pkg + '.' + name);
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add attribute " + pkg + '.' + name + ": " + e);
                                }
                            }
                            for (final Object object : type.getNavigableConnectingEnds(follow))
                            {
                                try
                                {
                                    ModelElementFacade endFacade = (ModelElementFacade)object;
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
                                                logger.debug("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + '.' + name);
                                            }
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + '.' + name + ": " + e);
                                }
                            }
                        }
                    }
                    else if (element instanceof AssociationEndFacade)
                    {
                        AssociationEndFacade type = (AssociationEndFacade)element;
                        facade = getType(type);
                        // TODO: When can ClassifierFacade ever be an instanceof AssociationEndFacade
                        if (facade instanceof AssociationEndFacade)
                        {
                            type = (AssociationEndFacade)facade;
                        }
                        pkg = (PackageFacade) type.getPackage();
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " AssociationEndFacade " + pkg + '.' + name);
                        }
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            if (facade != null && facade instanceof ClassifierFacade)
                            {
                                ClassifierFacade typeLogic = facade;
                                pkg = (PackageFacade) typeLogic.getPackage();
                                if (logger.isDebugEnabled())
                                {
                                    name = typeLogic.getName();
                                    logger.debug("getPackageReferences packageName=" + packageName + " element typeLogic " + pkg + '.' + name);
                                }
                                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add typeLogic " + pkg + '.' + name);
                                    }
                                }
                            }
                            else
                            {
                                if (logger.isDebugEnabled())
                                {
                                    logger.debug("getPackageReferences packageName=" + packageName + " element association " + pkg + '.' + name);
                                }
                                name = type.getName();
                                // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                                for (final Object object : type.getType().getNavigableConnectingEnds(follow))
                                {
                                    try
                                    {
                                        ModelElementFacade endFacade = (ModelElementFacade)object;
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
                                                    logger.debug("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + '.' + name);
                                                }
                                            }
                                        }
                                    }
                                    catch (RuntimeException e)
                                    {
                                        logger.error("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + '.' + name + ": " + e);
                                    }
                                }
                            }
                        }
                    }
                    else if (element instanceof EnumerationFacade)
                    {
                        EnumerationFacade type = (EnumerationFacade)element;
                        if (getType(type) != null)
                        {
                            type = (EnumerationFacade)getType(type);
                        }
                        pkg = (PackageFacade) type.getPackage();
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            for (ModelElementFacade attr : (List<ModelElementFacade>)type.getAllProperties())
                            {
                                ClassifierFacade attrType = getType(attr);
                                if (attrType != null)
                                {
                                    pkg = (PackageFacade) attr.getPackage();
                                    name = attr.getName();
                                }
                                if (!pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add enumeration attribute " + pkg + '.' + name);
                                    }
                                }
                            }
                        }
                    }
                    else if (element.getValidationName() != null && element.getValidationName().endsWith("[]"))
                    {
                        // Ignore modeled array types - assume non-array type is already in the package
                    }
                    else
                    {
                        // Log the type so we can extend this logic later...
                        logger.error("getPackageReferences Unexpected element type in service " + packageName + '.' + name + " : " + element);
                    }
                }
            }
            if (packageName.equals(service.getPackageName()))
            {
                // Add references from the operations of the service package itself
                for (WebServiceOperation op : service.getAllowedOperations())
                {
                    for (Object opit : op.getExceptions())
                    {
                        if (opit instanceof ModelElementFacade)
                        {
                            ModelElementFacade arg = (ModelElementFacade)opit;
                            pkg = (PackageFacade) arg.getPackage();
                            name = arg.getName();
                        }
                        else if (opit instanceof PackageFacade)
                        {
                            pkg = (PackageFacade) opit;
                        }
                        else if (opit instanceof WSDLType)
                        {
                            WSDLType type = (WSDLType) opit;
                            if (!type.getAssociationEnds().isEmpty())
                            {
                                // Get the first Exception attribute (the FaultBean)
                                ClassifierFacade fault = type.getAssociationEnds().get(0).getOtherEnd().getType();
                                pkg = (PackageFacade) fault.getPackage();
                                name = fault.getName();
                            }
                        }
                        else
                        {
                            pkg = null;
                        }
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(service.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                        {
                            pkgRef.add(pkg);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += pkg + ",";
                                logger.debug("getPackageReferences packageName=" + packageName + " add exception " + pkg + '.' + name);
                            }
                        }
                    }
                    for (final ParameterFacade arg : op.getParameters())
                    {
                        pkg = (PackageFacade) arg.getType().getPackage();
                        name = arg.getType().getName();
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(service.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
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
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " return " + pkg + '.' + name);
                        }
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(service.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
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
    public List<WebServiceOperation> getAllowedOperations(PackageFacade packageFacade)
    {
        List<WebServiceOperation> operations = new ArrayList<WebServiceOperation>();
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
                        operations.add((WebServiceOperation) op);
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
     * Filter schema types list and related object to only a single package. Called for each referenced package.
     * Run this after running getTypeMappingElements(), to populate the namespace Map and referenced imports from each namespace.
     * @param service WebService containing WS operations with references.
     * @param types Collection<ModelElementFacade> of all schemaTypeMappings referenced in the WebService class operations
     * @param packageName Package / namespace for which to find all related (referenced) packages
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs
     */
    public Collection<ModelElementFacade> getPackageTypes(WebServiceLogicImpl service, Set<MetafacadeBase> types, String packageName, boolean follow)
    {
        Collection<ModelElementFacade> pkgTypes = new TreeSet<ModelElementFacade>(service.new TypeComparator());
        if (StringUtils.isNotBlank(packageName))
        {
            String name = null;
            String pkg = null;
            String pkgRefs = "";
            // Copy package names and collection of related packages to package references list
            for (final MetafacadeBase element : types)
            {
                //TreeSet pkgRef = new TreeSet(new TypeComparator());
                ClassifierFacade facade = service.getType(element);
                if (facade != null)
                {
                    pkg = facade.getPackageName();
                    if (logger.isDebugEnabled())
                    {
                        name = facade.getName();
                        logger.debug("getPackageTypes packageName=" + packageName + " facade " + pkg + '.' + name);
                    }
                    if (pkg != null && pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(facade))
                    {
                        pkgTypes.add(facade);
                        if (logger.isDebugEnabled())
                        {
                            pkgRefs += facade.getName() + ',';
                            logger.debug("getPackageTypes packageName=" + packageName + " add facade " + facade.getPackageName() + '.' + facade.getName());
                        }
                    }
                    /*if (facade instanceof ClassifierFacade)
                    {
                        ClassifierFacade type = (ClassifierFacade)facade;
                        pkg = type.getPackageName();
                        name = type.getName();
                    }
                    else if (facade instanceof AssociationEndFacade)
                    {
                        AssociationEndFacade type = (AssociationEndFacade)facade;
                        pkg = type.getPackageName();
                        name = type.getName();
                    }*/
                }
                // TODO remove 'else' and add ParameterFacade logic type
                if (element instanceof ClassifierFacade)
                {
                    ClassifierFacade type = (ClassifierFacade)element;
                    facade = getType(type);
                    if (facade != null)
                    {
                        if (facade instanceof ClassifierFacade)
                        {
                            type = getType(type);
                        }
                    }
                    pkg = type.getPackageName();
                    if (logger.isDebugEnabled())
                    {
                        name = type.getName();
                        logger.debug("getPackageTypes packageName=" + packageName + " elementTypeLogic " + pkg + '.' + name);
                    }
                    if (pkg != null)
                    {
                        if (pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(type))
                        {
                            pkgTypes.add(type);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += type.getName() + ',';
                                logger.debug("getPackageTypes packageName=" + packageName + " add typeLogic " + type.getPackageName() + '.' + type.getName());
                            }
                        }
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("ClassifierFacade pkg=" + packageName + " refPkg=" + pkg + " name=" + type.getName());
                        }
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (ModelElementFacade attr : (List<ModelElementFacade>)type.getProperties(follow))
                        {
                            try
                            {
                                ClassifierFacade attrType = getType(attr);
                                if (attrType != null)
                                {
                                    pkg = attrType.getPackageName();
                                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(attrType))
                                    {
                                        pkgTypes.add(attrType);
                                        if (logger.isDebugEnabled())
                                        {
                                            name = attrType.getName();
                                            pkgRefs += attrType.getName() + ',';
                                            logger.debug("getPackageTypes packageName=" + packageName + " add attr " + attrType.getPackageName() + '.' + attrType.getName());
                                        }
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageTypes packageName=" + packageName + " add attr " + pkg + '.' + name + ": " + e);
                            }
                        }
                    }
                }
                else if (element instanceof AssociationEndFacade)
                {
                    AssociationEndFacade type = (AssociationEndFacade)element;
                    facade = getType(type);
                    // TODO When can ClassifierFacade ever be an instanceof AssociationEndFacade
                    if (facade instanceof AssociationEndFacade)
                    {
                        type = (AssociationEndFacade)facade;
                    }
                    if (facade != null && facade instanceof ClassifierFacade)
                    {
                        ClassifierFacade typeLogic = facade;
                        pkg = typeLogic.getPackageName();
                        if (pkg != null && pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(typeLogic))
                        {
                            pkgTypes.add(typeLogic);
                            if (logger.isDebugEnabled())
                            {
                                name = typeLogic.getName();
                                pkgRefs += typeLogic.getName() + ',';
                                logger.debug("getPackageTypes packageName=" + packageName + " add typeLogic " + type.getPackageName() + '.' + type.getName());
                            }
                        }
                    }
                    else
                    {
                        pkg = type.getPackageName();
                        if (logger.isDebugEnabled())
                        {
                            name = type.getName();
                            logger.debug("getPackageTypes packageName=" + packageName + " associationEnd " + pkg + '.' + name);
                        }
                        if (pkg != null)
                        {
                            if (pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(type))
                            {
                                pkgTypes.add(type);
                                if (logger.isDebugEnabled())
                                {
                                    pkgRefs += type.getName() + ',';
                                    logger.debug("getPackageTypes packageName=" + packageName + " add typeAssoc " + type.getPackageName() + '.' + type.getName());
                                }
                            }
                            // Get the other end reference itself, then get the navigable connecting ends
                            try
                            {
                                AssociationEndFacade otherEnd = (type.getOtherEnd());
                                ClassifierFacade endType = getType(otherEnd);
                                if (endType != null)
                                {
                                    pkg = endType.getPackageName();
                                    if (logger.isDebugEnabled())
                                    {
                                        name = endType.getName();
                                        logger.debug("getPackageTypes packageName=" + packageName + " otherEnd " + pkg + '.' + name);
                                    }
                                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(endType))
                                    {
                                        pkgTypes.add(endType);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += endType.getName() + ',';
                                            logger.debug("getPackageTypes packageName=" + packageName + " add otherEnd " + endType.getPackageName() + '.' + otherEnd.getName());
                                        }
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageTypes packageName=" + packageName + " add otherEnd " + pkg + '.' + name + ": " + e);
                            }
                            // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                            for (final Object object : type.getType().getNavigableConnectingEnds(follow))
                            {
                                ClassifierFacade otherEnd = null;
                                try
                                {
                                    AssociationEndFacade endFacade = (AssociationEndFacade)object;
                                    if (getType(endFacade) != null)
                                    {
                                        otherEnd = getType(endFacade);
                                        pkg = otherEnd.getPackageName();
                                        if (logger.isDebugEnabled())
                                        {
                                            name = otherEnd.getName();
                                            logger.debug("getPackageTypes packageName=" + packageName + " NavOtherEnd " + otherEnd.getPackageName() + '.' + otherEnd.getName());
                                        }
                                        if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(otherEnd))
                                        {
                                            pkgTypes.add(otherEnd);
                                            if (logger.isDebugEnabled())
                                            {
                                                pkgRefs += otherEnd.getName() + ',';
                                                logger.debug("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + otherEnd.getPackageName() + '.' + otherEnd.getName());
                                            }
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    if (otherEnd!=null)
                                    {
                                        logger.error("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + otherEnd.getPackageName() + '.' + otherEnd.getName() + ": " + e);
                                    }
                                    else
                                    {
                                        logger.error("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + type.getType().getPackageName() + '.' + type.getType().getName() + ": " + e);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (element instanceof EnumerationFacade)
                {
                    EnumerationFacade type = (EnumerationFacade)element;
                    if (getType(type) != null)
                    {
                        type = (EnumerationFacade)getType(type);
                    }
                    pkg = type.getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(type))
                    {
                        pkgTypes.add(type);
                        if (logger.isDebugEnabled())
                        {
                            pkgRefs += type.getName() + ',';
                            logger.debug("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + type.getPackageName() + '.' + type.getName());
                        }
                    }
                }
                else if (facade != null && facade.getName().endsWith("[]"))
                {
                    // Ignore modeled array types - assume non-array type is already in the package
                }
                else
                {
                    // Log the type so we can extend this logic later...
                    logger.error("getPackageTypes Unexpected element in service " + pkg + '.' + element + " type: " + facade);
                }
            }
            // Add package types from the operations of the service package itself
            for (final WebServiceOperation op : service.getAllowedOperations())
            {
                for (final ModelElementFacade arg : (Collection<ModelElementFacade>)op.getExceptions())
                {
                    pkg = arg.getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg))
                    {
                        pkgTypes.add(arg);
                        if (logger.isDebugEnabled())
                        {
                            name = arg.getName();
                            pkgRefs += arg.getName() + ',';
                            logger.debug("getPackageTypes packageName=" + packageName + " add service exception " + arg.getPackageName() + '.' + arg.getName());
                        }
                    }
                }
                for (final ParameterFacade arg : op.getParameters())
                {
                    pkg = arg.getType().getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg.getType()))
                    {
                        pkgTypes.add(arg.getType());
                        if (logger.isDebugEnabled())
                        {
                            name = arg.getName();
                            pkgRefs += arg.getName() + ',';
                            logger.debug("getPackageTypes packageName=" + packageName + " add service parameter " + arg.getPackageName() + '.' + arg.getName());
                        }
                    }
                }
                if (op.getReturnType()!=null)
                {
                    pkg = op.getReturnType().getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(op.getReturnType()))
                    {
                        pkgTypes.add(op.getReturnType());
                        if (logger.isDebugEnabled())
                        {
                            name = op.getReturnType().getName();
                            pkgRefs += op.getReturnType().getName() + ',';
                            logger.debug("getPackageTypes packageName=" + packageName + " add service returnType " + op.getReturnType().getPackageName() + '.' + op.getReturnType().getName());
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
            namespace = StringUtils.reverseDelimited(hostname, WebServiceGlobals.NAMESPACE_DELIMITER)
                + namespace.substring(namespace.indexOf('/'), namespace.length());
        }
        else
        {
            namespace = StringUtils.reverseDelimited(hostname, WebServiceGlobals.NAMESPACE_DELIMITER);
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
        namespace = StringUtils.replaceChars(namespace, '/', WebServiceGlobals.NAMESPACE_DELIMITER);
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
        return WebServiceUtils.reversePackage(namespace) + '.' + elementName;
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @return Constructor String with facade name
     */
    public String createConstructor(ModelElementFacade facade)
    {
        return createConstructor(facade, false);
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @param useMany Return constructor with multiplicity type instead of underlying type
     * @return Constructor String with facade name
     */
    public String createConstructor(ModelElementFacade facade, boolean useMany)
    {
        return createConstructor(facade, useMany, null);
    }

    /**
     * Supplies a result for type = <new value>; initialization for all types
     * @param facade Type to create default object for
     * @param useMany Return constructor with multiplicity type instead of underlying type
     * @param parent Object containing this facade, which may have an attribute named dependency to a different type
     * @return Constructor String with facade name
     */
    @SuppressWarnings("null")
    public String createConstructor(ModelElementFacade facade, boolean useMany, ModelElementFacade parent)
    {
        if (facade==null)
        {
            return "facade was null";
        }
        String rtn = "";
        String toString = "";
        ClassifierFacade type = null;
        String typeName = facade.getFullyQualifiedName();
        String name = facade.getName();
        String defaultValue = "";
        // TODO: Default collection type from properties
        String collectionType = "java.util.ArrayList";
        Boolean isMany = null;
        boolean isEnumeration = false;
        /*if (parent != null)
        {
            // See if a named dependency exists with the same facadeName
            for (final DependencyFacade dependency : parent.getSourceDependencies())
            {
                if (dependency.getName().equals(facade.getName()) && dependency instanceof DependencyFacade)
                {
                    facade = ((DependencyFacade)dependency).getTargetElement();
                    toString = ".toString()";
                    break;
                }
            }
        }*/
        try {
            if (logger.isDebugEnabled())
            {
                logger.debug("facade=" + facade + " parent=" + parent + " useMany=" + useMany);
            }
            if (facade instanceof ClassifierFacade)
            {
                ClassifierFacade classifier = (ClassifierFacade) facade;
                type = classifier;
                typeName = classifier.getFullyQualifiedName();
            }
            if (facade instanceof AttributeFacade)
            {
                AttributeFacade attr = (AttributeFacade) facade;
                defaultValue = attr.getDefaultValue();
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
            }
            else if (facade instanceof WSDLTypeAttributeLogic)
            {
                WSDLTypeAttributeLogic attr = (WSDLTypeAttributeLogic) facade;
                defaultValue = attr.getDefaultValue();
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
            }
            else if (facade instanceof ParameterFacade)
            {
                ParameterFacade attr = (ParameterFacade) facade;
                defaultValue = attr.getDefaultValue();
                type = attr.getType();
                typeName = type.getFullyQualifiedName();
                if (type.isEnumeration())
                {
                    facade = type;
                }
                else if (useMany)
                {
                    typeName = collectionType + '<' + type.getFullyQualifiedName() + '>';
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
            }
            if (facade instanceof AssociationEndFacade)
            {
                AssociationEndFacade attr = (AssociationEndFacade) facade;
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
                facade = attr.getType();
            }
            if (facade instanceof WSDLTypeAssociationEnd)
            {
                WSDLTypeAssociationEnd attr = (WSDLTypeAssociationEnd) facade;
                type = attr.getType();
                if (useMany)
                {
                    typeName = attr.getGetterSetterTypeName();
                }
                else
                {
                    typeName = type.getFullyQualifiedName();
                }
                if (attr.getUpper()>1 || attr.getUpper()==-1)
                {
                    isMany = true;
                }
                facade = attr.getType();
            }
            // TODO: Make this work for attribute types other than String.
            if (parent != null && StringUtils.isEmpty(defaultValue) && ("String".equals(typeName) || "java.lang.String".equals(typeName)))
            {
                // See if a named dependency exists with the same facadeName
                for (final DependencyFacade dependency : parent.getSourceDependencies())
                {
                    if (dependency.getName().equals(facade.getName()))
                    {
                        facade = dependency.getTargetElement();
                        // DependencyFacade type comes back empty for UML2::Integer
                        // Need to get metaObject Name property and verify it is not null.
                        if (facade instanceof WSDLTypeLogic)
                        {
                            WSDLTypeLogic wsdlType = (WSDLTypeLogic) facade;
                            if (logger.isDebugEnabled())
                            {
                                logger.debug(wsdlType + " fqn=" + wsdlType.getFullyQualifiedName() + " name="
                                    + wsdlType.getName() + " id=" + wsdlType.getId() + " properties="
                                    + wsdlType.getAllProperties() + " MetaObject=" + wsdlType.getMetaObject() + " properties="
                                    + wsdlType.getProperties());
                            }
                            if (StringUtils.isEmpty(wsdlType.getName()))
                            {
                                break;
                            }
                        }
                        if (facade instanceof ClassifierFacade)
                        {
                            type = (ClassifierFacade) facade;
                        }
                        typeName = facade.getFullyQualifiedName();
                        toString = ".toString()";
                        if (logger.isDebugEnabled())
                        {
                            logger.debug(parent + " " + facade + " = "
                                    + dependency + " type=" + type + " typeName="
                                    + typeName);
                        }
                        break;
                    }
                }
            }
            if (type instanceof WSDLEnumerationTypeLogic)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("facade=" + facade + " type=" + type + " default=" + defaultValue);
                }
                WSDLEnumerationTypeLogic enumer = (WSDLEnumerationTypeLogic) type;
                //type = enumer.getLiteralType().getFullyQualifiedName();
                Collection<AttributeFacade> literals = enumer.getLiterals();
                if (StringUtils.isEmpty(defaultValue) && !literals.isEmpty())
                {
                    // Just get the first enumeration literal
                    Object literal = literals.iterator().next();
                    if (literal instanceof EnumerationLiteralFacade)
                    {
                        EnumerationLiteralFacade enumLiteral = (EnumerationLiteralFacade) literal;
                        // Use the literal name to retrieve the value with .valueOf(). XML version has only the value.
                        Boolean useEnumValueInXSD = Boolean.valueOf(String.valueOf(enumer.getConfiguredProperty("useEnumValueInXSD")));
                        if (useEnumValueInXSD)
                        {
                            defaultValue = enumLiteral.getValue();
                        }
                        else
                        {
                            defaultValue = enumLiteral.getName();
                        }
                    }
                    else if (literal instanceof AttributeFacade)
                    {
                        AttributeFacade attrib = (AttributeFacade) literal;
                        defaultValue = attrib.getEnumerationValue();
                        if (defaultValue==null)
                        {
                            defaultValue = attrib.getDefaultValue();
                        }
                    }
                    // Literal value is always a String. Remove quotes if part of default (i.e. class attribute).
                    // wsdl2java may add unexpected underscores such that name and literal value no longer match.
                    defaultValue = StringUtils.remove(defaultValue, "\"");
                    defaultValue = enumer.getFullyQualifiedName() + ".fromValue(\"" + defaultValue + "\")";
                }
                else
                {
                    defaultValue = enumer.getName() + '.' + defaultValue;
                }
                isEnumeration = true;
            }
            if (useMany && (isMany==null || isMany.booleanValue()))
            {
                if (!typeName.startsWith("java.util"))
                {
                    rtn = "new " + collectionType + '<' + typeName + ">()";
                }
                /*if (type.equals("java.util.Collection") || typeName.equals("java.util.List"))
                {
                    rtn = "new " + collectionType + "<" + typeName + ">()";
                }
                else if (typeName.equals("java.util.Set"))
                {
                    rtn = "new java.util.HashSet<" + typeName + ">";
                }
                else if (typeName.equals("java.util.Map"))
                {
                    rtn = "new java.util.HashMap<" + typeName + ">";
                }*/
                else
                {
                    // Assume array or type Collection<type>
                    rtn = "new " + typeName + "()";
                }
            }
            else if ("String".equals(typeName) || "java.lang.String".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? defaultValue : '\"' + name + '\"');
            }
            else if ("Boolean".equals(typeName) || "java.lang.Boolean".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? "Boolean." + defaultValue.toUpperCase() : "Boolean.TRUE");
            }
            else if ("boolean".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? defaultValue : "true");
            }
            else if ("int".equals(typeName) || "short".equals(typeName) || "long".equals(typeName)
                    || "byte".equals(typeName) || "float".equals(typeName) || "double".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? defaultValue : "1");
            }
            else if ("java.util.Date".equals(typeName))
            {
                rtn = "new " + typeName + "()";
            }
            else if ("java.sql.Timestamp".equals(typeName))
            {
                rtn = "new java.sql.Timestamp(System.currentTimeMillis())";
            }
            else if ("java.util.Calendar".equals(typeName))
            {
                rtn = "java.util.Calendar.getInstance()";
            }
            else if ("org.joda.time.LocalTime".equals(typeName))
            {
                rtn = "new org.joda.time.LocalTime(1, 1)";
            }
            else if ("char".equals(typeName))
            {
                rtn = "'" + (StringUtils.isNotEmpty(defaultValue) ? defaultValue : name.substring(0, 1)) + "'";
            }
            else if ("Character".equals(typeName))
            {
                rtn = "new Character('" + (StringUtils.isNotEmpty(defaultValue) ? "new Character(" + defaultValue : name.substring(0, 1)) + "')";
            }
            else if ("Byte".equals(typeName) || "java.lang.Byte".equals(typeName))
            {
                rtn = "new Byte(\"" + facade.getName() + "\")";
            }
            else if ("Short".equals(typeName) || "java.lang.Short".equals(typeName)
                    || "Integer".equals(typeName) || "java.lang.Integer".equals(typeName)
                    || "Long".equals(typeName) || "java.lang.Long".equals(typeName)
                    || "Float".equals(typeName) || "java.lang.Float".equals(typeName)
                    || "Double".equals(typeName) || "java.lang.Double".equals(typeName)
                    || "java.math.BigDecimal".equals(typeName))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? typeName + ".valueOf(" + defaultValue + ")" : typeName + ".valueOf(1)");
            }
            else if ("java.math.BigInteger".equals(typeName))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? "java.math.BigInteger.valueOf(" + defaultValue + ')' : "java.math.BigInteger.valueOf(1)");
            }
            else if ("byte[]".equals(typeName))
            {
                rtn = (StringUtils.isNotBlank(defaultValue) ? defaultValue : '\"' + name + '\"') + ".getBytes()";
            }
            else if ("char[]".equals(typeName))
            {
                String value = StringUtils.isNotBlank(defaultValue) ? defaultValue : name;
                if (!value.startsWith("\""))
                {
                    value = "\"" + value;
                }
                if (!value.endsWith("\""))
                {
                    value = value + "\"";
                }
                rtn = value + ".toCharArray()";
            }
            else if ("String[]".equals(typeName))
            {
                rtn = "new String[] { " + (StringUtils.isNotBlank(defaultValue) ? defaultValue : '\"' + name + '\"') + " }";
            }
            else if (isEnumeration)
            {
                if (useMany)
                {
                    rtn = collectionType + '<' + defaultValue + '>';
                }
                else
                {
                    rtn = defaultValue;
                }
            }
            else if (!StringUtils.isEmpty(defaultValue))
            {
                rtn = "new " + typeName + '(' + defaultValue + ')';
            }
            else if (type != null && type.hasStereotype("Entity"))
            {
                // Entity classes will always be abstract with Impl generated classes.
                rtn = '(' + typeName + ")new " + typeName + "Impl()";
            }
            else if (type instanceof GeneralizableElementFacade)
            {
                // If type has a descendant with name <typeName>Impl, assume typeNameImpl must be instantiated instead of typeName
                if (typeName.endsWith("[]"))
                {
                    rtn = "{ new " + typeName.substring(0, typeName.length()-2) + "() }";
                }
                else
                {
                    rtn = "new " + typeName + "()";
                }
                //if (facade instanceof ClassifierFacade)
                //{
                    //ClassifierFacade classifier = (ClassifierFacade)facade;
                    // If type is abstract, choose Impl descendant if exists, or the last descendant
                    if (type.isAbstract())
                    {
                        // Can't instantiate abstract class - pick some descendant
                        for (GeneralizableElementFacade spec : type.getSpecializations())
                        {
                            if (spec.getName().equals(type.getName() + "Impl"))
                            {
                                rtn = '(' + type.getName() + ")new " + typeName + "Impl()";
                                break;
                            }
                            rtn = '(' + type.getName() + ")new " + spec.getFullyQualifiedName() + "()";
                        }
                    }
                //}
                GeneralizableElementFacade generalization = (GeneralizableElementFacade)type;
                for (GeneralizableElementFacade spec : generalization.getSpecializations())
                {
                    if (spec.getName().equals(type.getName() + "Impl"))
                    {
                        rtn = '(' + type.getName() + ")new " + spec.getFullyQualifiedName() + "Impl()";
                    }
                }
            }
            else if (typeName.endsWith("[]"))
            {
                rtn = "{ new " + typeName.substring(0, typeName.length()-2) + "() }";
            }
            else
            {
                rtn = "new " + typeName + "()";
            }
            rtn = StringUtils.replace(rtn, "java.util.Collection", "java.util.ArrayList") + toString;
            rtn = StringUtils.replace(rtn, "java.util.Set", "java.util.HashSet") + toString;
            if (logger.isDebugEnabled())
            {
                logger.debug("facade=" + facade + " facadeName=" + facade.getName() + " type=" + type + " typeName=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue + " rtn=" + rtn);
            }
        } catch (RuntimeException e) {
            logger.error(e + " facade=" + facade + " facadeName=" + facade.getName() + " parent=" + parent + " type=" + type + " typeName=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue);
            e.printStackTrace();
        }
        return rtn;
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
     * Creates a list of referenced packages for all subclasses referenced by the service.
     * Used to add XmlSeeAlso references to the SEI Interface.
     * @param service WebService containing WS operations with references.
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return typeRef Collection<ModelElementFacade> referenced types
     */
    public Collection<PackageFacade> getServiceDescendantPackages(WebServiceLogicImpl service, boolean follow)
    {
        // Note: The way XmlSeeAlso is supposed to work: any descendant classes from parameter or return or exception classes
        // not directly referenced by the XML types should have their package ObjectFactory added to the reference list.
        // The way CXF works: parameter types and type hierarchy referenced by the service are added.
        Collection<PackageFacade> pkgRef = new HashSet<PackageFacade>();
        // Keep track of elements already iterated, avoid stackOverflow.
        Collection<ModelElementFacade> added = new HashSet<ModelElementFacade>();
        // For each service parameter and return type and exception, find all descendants
        for (final WebServiceOperation op : service.getAllowedOperations())
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + '.' + op.getName() + " parms=" + op.getParameters().size() + " size=" + pkgRef.size());
            }
            /*for (final ModelElementFacade ex : op.getExceptions())
            {
                if (!added.contains(ex))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + '.' + op.getName() + " exception=" + ex.getFullyQualifiedName() + " size=" + pkgRef.size());
                    }
                    added.add(ex);
                    pkgRef = getServiceDescendantPackages(ex, pkgRef, added);
                }
            }*/
            for (final ParameterFacade arg : op.getParameters())
            {
                if (!added.contains(arg.getType()))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + '.' + op.getName() + " parameter=" + arg.getName() + " size=" + pkgRef.size());
                    }
                    added.add(arg.getType());
                    pkgRef = getServiceDescendantPackages(arg.getType(), pkgRef, added);
                }
            }

        }
        // Don't put current package into package reference list
        pkgRef.remove(service.getPackage());
        return pkgRef;
    }

    /**
     * Creates a list of referenced types for the service.
     * Used to add element references to the service namespace, for bare parameters.
     * @param service WebService containing WS operations with references.
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return typeRef Collection<ModelElementFacade> referenced types
     */
    public Collection<ModelElementFacade> getServiceReferences(WebServiceLogicImpl service, boolean follow)
    {
        Collection<ModelElementFacade> typeRef = new HashSet<ModelElementFacade>();
        // Temporary holder until we determine that the service can be bare
        Collection<ModelElementFacade> opRef = new HashSet<ModelElementFacade>();
        //String name = null;
        String pkg = null;
        @SuppressWarnings("unused")
        String typeRefs = "";
        // Copy package names and collection of related packages to package references list
        // Add references from the operations of the service package itself
        for (final WebServiceOperation op : service.getAllowedOperations())
        {
            boolean isMany = false;
            /*for (final ModelElementFacade ex : op.getExceptions())
            {
                pkg = ex.getPackageName();
                //name = ex.getName();
                if (pkg!=null && pkg.indexOf('.') > 0 && !typeRef.contains(ex) && !opRef.contains(ex))
                {
                    opRef.add(ex);
                    if (logger.isDebugEnabled())
                    {
                        typeRefs += ex + ",";
                        logger.debug("getServiceReferences exception " + pkg + '.' + ex.getName());
                    }
                }
            }*/
            for (final ParameterFacade arg : op.getParameters())
            {
                pkg = arg.getType().getPackageName();
                //name = arg.getType().getName();
                if (arg.getUpper()>1 || arg.getUpper()==-1)
                {
                    // Can't handle return size > 1 for object - need wrapper
                    isMany = true;
                }
                if (pkg!=null && pkg.indexOf('.') > 0 && !typeRef.contains(arg.getType()) && !opRef.contains(arg.getType()))
                {
                    opRef.add(arg.getType());
                    if (logger.isDebugEnabled())
                    {
                        typeRefs += arg.getType() + ",";
                        logger.debug("getServiceReferences parameter " + pkg + '.' + arg.getType().getName());
                    }
                }
            }
/*            if (op.getReturnType()!=null)
            {
                ClassifierFacade rtnType = op.getReturnType();
                pkg = rtnType.getPackageName();
                //name = rtnType.getName();
                if (logger.isDebugEnabled())
                {
                    logger.debug("getServiceReferences packageName=" + packageName + " return " + pkg + "." + name);
                }
                if (pkg!=null && !pkg.equals(service.getPackageName()) && pkg.indexOf('.') > 0 && !typeRef.contains(rtnType))
                {
                    typeRef.add(rtnType);
                    //pkgRefs += pkg + ",";
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("getServiceReferences packageName=" + packageName + " add return " + pkg + "." + name);
                    }
                }
            }*/
            if (!isMany)
            {
                typeRef.addAll(opRef);
            }
        }
        /*for (final ModelElementFacade type : typeRef)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("getServiceReferences packageName=" + type.getPackageName() + "." + type.getName());
            }
        }*/
        if (logger.isDebugEnabled())
        {
            logger.debug("getServiceReferences typeRef " + service.getPackageName() + '.' + service.getName() + ' ' + typeRef);
        }
        return typeRef;
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
        return StringUtils.reverseDelimited(packageName, WebServiceGlobals.NAMESPACE_DELIMITER);
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
     * <p> Creates and returns the schema type for the given <code>type</code>.
     * It finds the mapped schema type from the passed in
     * <code>schemaTypeMappings</code>.
     * </p>
     *
     * @param type the ClassifierFacade instance
     * @param schemaTypeMappings contains the mappings from model datatypes to
     *        schema datatypes.
     * @param namespacePrefix the prefix given to the schema type if it's a
     *        custom type (non XSD type).
     * @param qName the qualified name
     * @param wrappedArrayTypePrefix a prefix to give to wrapped array types.
     * @param withPrefix a flag indicating whether or not the type should have
     *        the prefix defined
     * @param preserveArray true/false, if true then if the schema type is an
     *        array we'll preserve the fact that its an array and return an
     *        array schema type name. If false we will return back the non array
     *        type even if its an array.
     * @return the schema type name - String
     */
    public static String getSchemaType(
        ClassifierFacade type,
        TypeMappings schemaTypeMappings,
        String namespacePrefix,
        String qName,
        String wrappedArrayTypePrefix,
        boolean withPrefix,
        boolean preserveArray)
    {
        StringBuilder schemaType = new StringBuilder();
        String modelName = type.getFullyQualifiedName(true);
        if (schemaTypeMappings != null)
        {
            namespacePrefix += ':';
            String mappedValue = schemaTypeMappings.getTo(modelName);
            if (!mappedValue.equals(modelName) || StringUtils.isEmpty(type.getPackageName()))
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
                            schemaType.append(((WSDLType)nonArray).getName());
                        }
                        else if (nonArray instanceof WSDLEnumerationType)
                        {
                            schemaType.append(((WSDLEnumerationType)nonArray).getName());
                        }
                    }
                }
                else
                {
                    schemaType.append(qName);
                }
            }
            // remove any array '[]' suffix
            schemaType = new StringBuilder(schemaType.toString().replaceAll("\\[\\]", ""));
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
        else if (element instanceof WSDLTypeAttributeLogic)
        {
            WSDLTypeAttributeLogic attrib = (WSDLTypeAttributeLogic)element;
            type = attrib.getType();
            many = attrib.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof WSDLTypeAssociationEndLogic)
        {
            WSDLTypeAssociationEndLogic association = (WSDLTypeAssociationEndLogic)element;
            type = association.getType();
            many = association.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof WebServiceParameterLogic)
        {
            WebServiceParameterLogic param = (WebServiceParameterLogic)element;
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
    public static String getRequestType(String requestType) {
        
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

    public static String getSpringOperationPath(WebServiceOperation operation) {

        StringBuilder builder = new StringBuilder();

        String path = operation.getRestPath();
        if(path.indexOf('{') != -1) {
            path = path.substring(0, path.indexOf('{'));
        }

        builder.append("\"/");
        builder.append(path.substring(2, path.length() - 1));

        for(ParameterFacade parameter : operation.getArguments()) {
            WebServiceParameter param = (WebServiceParameter)parameter;
            String paramType = param.getRestParamType();
            if(paramType.contains("PathParam")) {
                builder.append("/{" + parameter.getName() + "}");
            }
        }
        
        return builder.toString() + "\"";
    }
    
    public static Collection<String> getSpringOperationArgs(WebServiceOperation operation) {

        ArrayList<String> args = new ArrayList<>();

        for(ParameterFacade parameter : operation.getArguments()) {
            WebServiceParameter param = (WebServiceParameter)parameter;
            StringBuilder builder = new StringBuilder();
            String paramType = param.getRestParamType();

            if(param.getType().getAttributes().size() > 0) {
                builder.append("@org.springframework.web.bind.annotation.RequestBody");
            }

            if(paramType.contains("PathParam")) {
                if(builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append("@org.springframework.web.bind.annotation.PathVariable(\"");
                builder.append(param.getName() + "\")");
            }
         
            if(paramType.contains("RequestParam")) {
                if(builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append("@org.springframework.web.bind.annotation.RequestParam");
            }

            if(builder.length() > 0) {
                builder.append(" ");
            }

            builder.append(param.getType().getFullyQualifiedName() + " " + param.getName());

            args.add(builder.toString());
        }

        return args;
    }

    public static boolean isService(ModelElementFacade element) {

        if(element.getClass().getName().equals("WebServiceLogicImpl")) {
            return true;
        }

        return false;
    }

    public static boolean isWebService(ModelElementFacade element) {

        if(element instanceof WebServiceLogic) {
            return true;
        }

        return false;
    }

    public static boolean isServiceOnly(ModelElementFacade element) {

        if(isService(element) && !(element instanceof WebServiceLogic)) {
            return true;
        }

        return false;
    }
}
