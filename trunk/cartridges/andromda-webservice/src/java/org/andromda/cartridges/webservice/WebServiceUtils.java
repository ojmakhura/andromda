package org.andromda.cartridges.webservice;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.andromda.cartridges.webservice.metafacades.WSDLEnumerationType;
import org.andromda.cartridges.webservice.metafacades.WSDLType;
import org.andromda.cartridges.webservice.metafacades.WSDLTypeLogic;
import org.andromda.cartridges.webservice.metafacades.WebServiceLogicImpl;
import org.andromda.cartridges.webservice.metafacades.WebServiceOperation;
import org.andromda.cartridges.webservice.metafacades.WebServiceOperationLogicImpl;
import org.andromda.cartridges.webservice.metafacades.WebServicePackageLogic;
import org.andromda.cartridges.webservice.metafacades.WebServiceParameterLogicImpl;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
    private static Map<WebServicePackageLogic, String> packageAbbr = new TreeMap<WebServicePackageLogic, String>();
    
    /**
     * Creates a list of referenced packages for the service. Populates pkgAbbr static map.
     * I tried this in the WebServiceLogicImpl metafacade but couldn't get repeatable results.
     * Called from jaxws-included.xsd for CXF implementation
     * @param service WebServiceLogicImpl The service for which to find referenced packages
     * @param types Set<MetafacadeBase> of all serviceOperations, from $service.allowedOperations
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs Collection<WebServicePackageLogic> - all referenced packages
     */
    public Collection<WebServicePackageLogic> getPackages(WebServiceLogicImpl service, Set<MetafacadeBase> types, boolean follow)
    {
        return setPkgAbbr(service, types, follow);
    }
    
    /** Adds the package namespace abbreviation for this package
     * @param pkg Package for which to get the abbreviation. Uses a static Map so that
     * all schemas globally for this model will use the same namespace abbreviations
     * @param pkgAbbr Package Abbreviation to be added to namespace map
     * @return PackageMap TreeMap of package <-> abbreviation cross references
     */
    public Map addPkgAbbr(WebServicePackageLogic pkg, String pkgAbbr)
    {
        if (packageAbbr==null)
        {
            packageAbbr = new TreeMap();
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
    public String getPkgAbbr(WebServicePackageLogic pkg)
    {
        if (packageAbbr==null)
        {
            packageAbbr = new TreeMap();
        }
        String rtn = (String)packageAbbr.get(pkg);
        String pkgName = pkg.getFullyQualifiedName();
        if (StringUtils.isEmpty(pkgName) || pkgName.startsWith("java.lang") || pkgName.startsWith("java.util")
            || pkgName.endsWith("PrimitiveTypes") || pkgName.endsWith("datatype"))
        {
            // Assume simple types if no package
            rtn="xs";
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("getPkgAbbr " + pkg + " " + rtn + " " + packageAbbr.size() + " " + pkgName);
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
                logger.debug("getPkgAbbr " + pkg + " " + rtn + " " + packageAbbr.size());
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
    private Set<WebServicePackageLogic> setPkgAbbr(WebServiceLogicImpl service, Set<MetafacadeBase> types, boolean follow)
    {
        // Contains references to only packages needed from this service
        Set<WebServicePackageLogic> pkgSet = new TreeSet<WebServicePackageLogic>();
        if (logger.isDebugEnabled())
        {
            logger.debug(service.getName() + " setPkgAbbr");
        }
        int namespaceCount = packageAbbr.size() + 1;
        WebServicePackageLogic pkg = (WebServicePackageLogic) service.getPackage();
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
                    logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + service.getName() + " servicePackage");
                }
                namespaceCount++;
            }
        }
        // Copy package names and abbreviations to package list
        for (final Iterator iterator = service.getAllowedOperations().iterator(); iterator.hasNext();)
        {
            WebServiceOperationLogicImpl op = (WebServiceOperationLogicImpl)iterator.next();
            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
            {
                ModelElementFacade arg = (ModelElementFacade)opiterator.next();
                pkg = (WebServicePackageLogic) arg.getPackage();
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
                            logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + op.getName() + " getExceptions " + arg.getName());
                        }
                        namespaceCount++;
                    }
                }
            }
            for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
            {
                WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
                pkg = (WebServicePackageLogic) arg.getType().getPackage();
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
                            logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + op.getName() + " getParameters " + arg.getName());
                        }
                        namespaceCount++;
                    }
                }
            }
            if (op.getReturnType()!=null)
            {
                pkg = (WebServicePackageLogic) op.getReturnType().getPackage();
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
                            logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + op.getName() + " getReturnType " + op.getReturnType().getName());
                        }
                        namespaceCount++;
                    }
                }
            }
        }
        for (final Iterator iterator = types.iterator(); iterator.hasNext();)
        {
            MetafacadeBase element = (MetafacadeBase)iterator.next();
            ClassifierFacade facade = service.getType(element);
            if (facade != null)
            {
                pkg = (WebServicePackageLogic) facade.getPackage();
                if (logger.isDebugEnabled())
                {
                    name = facade.getName();
                    logger.debug("setPkgAbbr facade " + pkg + "." + name);
                }
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    // This element is contained in this package, see what it references
                    for (final Iterator itAttr = facade.getProperties(follow).iterator(); itAttr.hasNext();)
                    {
                        try
                        {
                            ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                            if (getType(attr) != null)
                            {
                                attr = getType(attr);
                            }
                            pkg = (WebServicePackageLogic) attr.getPackage();
                            name = attr.getName();
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
                                        logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + facade.getName());
                                    }
                                    namespaceCount++;
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + " " + pkg + " " + facade.getName());
                        }
                    }
                    for (final Iterator otherEnds = facade.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                    {
                        try
                        {
                            ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                            if (getType(otherEnd) != null)
                            {
                                otherEnd = getType(otherEnd);
                            }
                            pkg = (WebServicePackageLogic) otherEnd.getPackage();
                            name = otherEnd.getName();
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
                                        logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + facade.getName());
                                    }
                                    namespaceCount++;
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + " " + pkg + " " + facade.getName());
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
                    type = (ClassifierFacade)getType(type);
                }
                pkg = (WebServicePackageLogic) type.getPackage();
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    //int cnt = type.getAttributes(follow).size();
                    /*if (logger.isDebugEnabled())
                    {
                        logger.debug("WSDLTypeLogicImpl pkg=" + pkg + " name=" + type.getName() + " size=" + cnt + " propsize=" + type.getProperties(follow).size());
                    }*/
                    // Duplicates logic in jaxws-included.vsl so that referenced packages are the same.
                    // Except that vsl uses getAttributes, not getProperties. getProperties returns all attributes, getAttributes does not.
                    for (final Iterator itAttr = type.getProperties(follow).iterator(); itAttr.hasNext();)
                    {
                        try
                        {
                            ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                            if (getType(attr) != null)
                            {
                                attr = getType(attr);
                            }
                            pkg = (WebServicePackageLogic) attr.getPackage();
                            name = attr.getName();
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
                                        logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName() + "." + name);
                                    }
                                    namespaceCount++;
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName() + ": " + e);
                        }
                    }
                    for (final Iterator otherEnds = type.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                    {
                        try
                        {
                            ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                            if (getType(otherEnd) != null)
                            {
                                otherEnd = getType(otherEnd);
                            }
                            pkg = (WebServicePackageLogic) otherEnd.getPackage();
                            name = otherEnd.getName();
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
                                        logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName() + "." + name);
                                    }
                                    namespaceCount++;
                                }
                            }
                        }
                        catch (RuntimeException e)
                        {
                            logger.debug("setPkgAbbr error in service " + service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName() + ": " + e);
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
                pkg = (WebServicePackageLogic) type.getPackage();
                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0)
                {
                    if (facade != null && facade instanceof ClassifierFacade)
                    {
                        ClassifierFacade typeLogic = (ClassifierFacade)facade;
                        pkg = (WebServicePackageLogic) typeLogic.getPackage();
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
                                    logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName());
                                }
                                namespaceCount++;
                            }
                        }
                    }
                    else
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("setPkgAbbr element association " + pkg + "." + name);
                        }
                        name = type.getName();
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (final Iterator otherEnds = type.getType().getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                        {
                            try
                            {
                                ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                if (getType(otherEnd) != null)
                                {
                                    otherEnd = getType(otherEnd);
                                }
                                pkg = (WebServicePackageLogic) otherEnd.getPackage();
                                name = otherEnd.getName();
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
                                            logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName());
                                        }
                                        namespaceCount++;
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
                pkg = (WebServicePackageLogic) type.getPackage();
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
                            logger.debug(service.getName() + " ns" + namespaceCount + " " + pkg + " " + type.getName() + "." + name);
                        }
                        namespaceCount++;
                    }
                }
            }
            else
            {
                // Log the type so we can extend this logic later...
                logger.error("setPkgAbbr Unexpected element in service " + pkg + "." + name + " type: " + element);
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
        if (StringUtils.isNotEmpty(packageName))
        {
            String name = null;
            PackageFacade pkg = null;
            String pkgRefs = "";
            if (types!=null)
            {
                // Copy package names and collection of related packages to package references list
                for (final Iterator<MetafacadeBase> iterator = types.iterator(); iterator.hasNext();)
                {
                    MetafacadeBase element = iterator.next();
                    /*if (logger.isDebugEnabled())
                    {
                        logger.debug("getPackageReferences packageName=" + packageName + " element " + pkg + "." + name);
                    }*/
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
                            for (final Iterator itAttr = facade.getProperties(follow).iterator(); itAttr.hasNext();)
                            {
                                try
                                {
                                    ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                                    if (getType(attr) != null)
                                    {
                                        attr = getType(attr);
                                    }
                                    pkg = (PackageFacade) attr.getPackage();
                                    name = attr.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + "." + name);
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + "." + name + ": " + e);
                                }
                            }
                            for (final Iterator otherEnds = facade.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                            {
                                try
                                {
                                    ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                    if (getType(otherEnd) != null)
                                    {
                                        otherEnd = getType(otherEnd);
                                    }
                                    pkg = (PackageFacade) otherEnd.getPackage();
                                    name = otherEnd.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + "." + name);
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + "." + name + ": " + e);
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
                            type = (ClassifierFacade)getType(type);
                        }
                        pkg = (PackageFacade) type.getPackage();
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " ClassifierFacade " + pkg + "." + name);
                        }
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                            for (final Iterator itAttr = type.getProperties(follow).iterator(); itAttr.hasNext();)
                            {
                                try
                                {
                                    ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                                    if (getType(attr) != null)
                                    {
                                        attr = getType(attr);
                                    }
                                    pkg = (PackageFacade) attr.getPackage();
                                    name = attr.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add attribute " + pkg + "." + name);
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add attribute " + pkg + "." + name + ": " + e);
                                }
                            }
                            for (final Iterator otherEnds = type.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                            {
                                try
                                {
                                    ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                    if (getType(otherEnd) != null)
                                    {
                                        otherEnd = getType(otherEnd);
                                    }
                                    pkg = (PackageFacade) otherEnd.getPackage();
                                    name = otherEnd.getName();
                                    if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                    {
                                        pkgRef.add(pkg);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += pkg + ",";
                                            logger.debug("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + "." + name);
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    logger.error("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + "." + name + ": " + e);
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
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " AssociationEndFacade " + pkg + "." + name);
                        }
                        if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && pkg.getFullyQualifiedName().equals(packageName))
                        {
                            if (facade != null && facade instanceof ClassifierFacade)
                            {
                                ClassifierFacade typeLogic = (ClassifierFacade)facade;
                                pkg = (PackageFacade) typeLogic.getPackage();
                                if (logger.isDebugEnabled())
                                {
                                    name = typeLogic.getName();
                                    logger.debug("getPackageReferences packageName=" + packageName + " element typeLogic " + pkg + "." + name);
                                }
                                if (pkg != null && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add typeLogic " + pkg + "." + name);
                                    }
                                }
                            }
                            else
                            {
                                if (logger.isDebugEnabled())
                                {
                                    logger.debug("getPackageReferences packageName=" + packageName + " element association " + pkg + "." + name);
                                }
                                name = type.getName();
                                // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                                for (final Iterator otherEnds = type.getType().getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                                {
                                    try
                                    {
                                        ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                        if (getType(otherEnd) != null)
                                        {
                                            otherEnd = getType(otherEnd);
                                        }
                                        pkg = (PackageFacade) otherEnd.getPackage();
                                        name = otherEnd.getName();
                                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                        {
                                            pkgRef.add(pkg);
                                            if (logger.isDebugEnabled())
                                            {
                                                pkgRefs += pkg + ",";
                                                logger.debug("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + "." + name);
                                            }
                                        }
                                    }
                                    catch (RuntimeException e)
                                    {
                                        logger.error("getPackageReferences packageName=" + packageName + " add otherEnd " + pkg + "." + name + ": " + e);
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
                            for (final Iterator properties = type.getAllProperties().iterator(); properties.hasNext();)
                            {
                                ModelElementFacade attr = ((ModelElementFacade)properties.next());
                                if (getType(attr) != null)
                                {
                                    attr = getType(attr);
                                }
                                pkg = (PackageFacade) attr.getPackage();
                                name = attr.getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add enumeration attribute " + pkg + "." + name);
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        // Log the type so we can extend this logic later...
                        logger.error("getPackageReferences Unexpected element type in service " + packageName + "." + name + " : " + element);
                    }
                }
            }
            if (packageName.equals(service.getPackageName()))
            {
                // Add references from the operations of the service package itself
                for (final Iterator<WebServiceOperation> iterator = service.getAllowedOperations().iterator(); iterator.hasNext();)
                {
                    WebServiceOperation op = (WebServiceOperation)iterator.next();
                    for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
                    {
                        Object opit = opiterator.next();
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
                                logger.debug("getPackageReferences packageName=" + packageName + " add exception " + pkg + "." + name);
                            }
                        }
                    }
                    for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
                    {
                        WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
                        pkg = (PackageFacade) arg.getType().getPackage();
                        name = arg.getType().getName();
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(service.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                        {
                            pkgRef.add(pkg);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += pkg + ",";
                                logger.debug("getPackageReferences packageName=" + packageName + " add parameter " + pkg + "." + name);
                            }
                        }
                    }
                    if (op.getReturnType()!=null)
                    {
                        pkg = (PackageFacade) op.getReturnType().getPackage();
                        name = op.getReturnType().getName();
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getPackageReferences packageName=" + packageName + " return " + pkg + "." + name);
                        }
                        if (pkg!=null && !pkg.getFullyQualifiedName().equals(service.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                        {
                            pkgRef.add(pkg);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += pkg + ",";
                                logger.debug("getPackageReferences packageName=" + packageName + " add return " + pkg + "." + name);
                            }
                        }
                    }
                }
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageReferences packageName=" + packageName + " " + pkgRefs);
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
            for (final Iterator iterator = packageFacade.getClasses().iterator(); iterator.hasNext();)
            {
                //MetafacadeBase element = (MetafacadeBase)iterator.next();
                //logger.debug("getPackageReferences packageName=" + packageName);
                ClassifierFacade facade = (ClassifierFacade)iterator.next();
                if (facade != null)
                {
                    pkg = (PackageFacade) facade.getPackage();
                    if (logger.isDebugEnabled())
                    {
                        name = facade.getName();
                        logger.debug("getPackageReferences packageName=" + packageName + " facade " + pkg + "." + name);
                    }
                    if (facade.hasStereotype("ValueObject") || facade.hasStereotype("Exception") || facade.hasStereotype("ApplicationException") || facade.hasStereotype("UnexpectedException"))
                    {
                        // This element is contained in this package, see what it references
                        for (final Iterator itAttr = facade.getProperties(follow).iterator(); itAttr.hasNext();)
                        {
                            try
                            {
                                //AttributeFacade attr = ((AttributeFacade)itAttr.next());
                                ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                                if (getType(attr) != null)
                                {
                                    attr = getType(attr);
                                }
                                pkg = (PackageFacade) attr.getPackage();
                                name = attr.getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + "." + name);
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageReferences packageName=" + packageName + " add facadeAttribute " + pkg + "." + name + ": " + e);
                            }
                        }
                        for (final Iterator otherEnds = facade.getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                        {
                            try
                            {
                                ModelElementFacade otherEnd = ((ModelElementFacade)otherEnds.next());
                                if (getType(otherEnd) != null)
                                {
                                    otherEnd = getType(otherEnd);
                                }
                                pkg = (PackageFacade) otherEnd.getPackage();
                                name = otherEnd.getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(packageName) && !pkgRef.contains(pkg) && pkg.getFullyQualifiedName().indexOf('.') > 0)
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + "." + name);
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageReferences packageName=" + packageName + " add facadeOtherEnd " + pkg + "." + name + ": " + e);
                            }
                        }
                    }
                    else if (facade.hasStereotype("WebService"))
                    {
                        // Add references from the operations of the service package itself
                        for (final Iterator operations = facade.getOperations().iterator(); operations.hasNext();)
                        {
                            OperationFacade op = (OperationFacade)operations.next();
                            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
                            {
                                ModelElementFacade arg = (ModelElementFacade)opiterator.next();
                                pkg = (PackageFacade) arg.getPackage();
                                name = arg.getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(facade.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add exception " + pkg + "." + name);
                                    }
                                }
                            }
                            for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
                            {
                                WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
                                pkg = (PackageFacade) arg.getType().getPackage();
                                name = arg.getType().getName();
                                if (pkg!=null && !pkg.getFullyQualifiedName().equals(facade.getPackageName()) && pkg.getFullyQualifiedName().indexOf('.') > 0 && !pkgRef.contains(pkg))
                                {
                                    pkgRef.add(pkg);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += pkg + ",";
                                        logger.debug("getPackageReferences packageName=" + packageName + " add parameter " + pkg + "." + name);
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
                                        logger.debug("getPackageReferences packageName=" + packageName + " add return " + pkg + "." + name);
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
                logger.debug("getPackageReferences packageName=" + packageName + " " + pkgRefs);
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
     * Filter schema types list and related object to only a single package. Called for each referenced package.
     * Run this after running getTypeMappingElements(), to populate the namespace Map and referenced imports from each namespace.
     * @param packageFacade Package / namespace for which to find all related (referenced) packages
     * @param follow Follow Inheritance references $extensionInheritanceDisabled
     * @return pkgRefs
     */
    public Collection<ModelElementFacade> getPackageTypes(WebServicePackageLogic packageFacade, boolean follow)
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
            for (final Iterator iterator = packageFacade.getOwnedElements().iterator(); iterator.hasNext();)
            {
                //MetafacadeBase element = (MetafacadeBase)iterator.next();
                Object mefacade = iterator.next();
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
                                pkgRefs += name + ",";
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
                                pkgRefs += name + ",";
                                logger.debug("getPackageTypes packageName=" + packageName + " add facadeEnumeration " + name);
                            }
                        }
                    }
                    else if (facade.hasStereotype("Exception") || facade.hasStereotype("UnexpectedException") || facade.hasStereotype("ApplicationException"))
                    {
                        if (!pkgTypes.contains(facade))
                        {
                            pkgTypes.add(facade);
                            if (logger.isDebugEnabled())
                            {
                                name = facade.getName();
                                pkgRefs += name + ",";
                                logger.debug("getPackageTypes packageName=" + packageName + " add facadeEnumeration " + name);
                            }
                        }
                    }
                    else if (facade.hasStereotype("WebService"))
                    {
                        // Add references from the operations of the service package itself
                        for (final Iterator operations = facade.getOperations().iterator(); operations.hasNext();)
                        {
                            OperationFacade op = (OperationFacade)operations.next();
                            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
                            {
                                ModelElementFacade arg = (ModelElementFacade)opiterator.next();
                                pkg = arg.getPackageName();
                                if (pkg!=null && pkg.equals(facade.getPackageName()) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg))
                                {
                                    pkgTypes.add(arg);
                                    if (logger.isDebugEnabled())
                                    {
                                        name = arg.getName();
                                        pkgRefs += name + ",";
                                        logger.debug("getPackageTypes packageName=" + packageName + " add exception " + pkg + "." + name);
                                    }
                                }
                            }
                            for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
                            {
                                WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
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
                                        pkgRefs += name + ",";
                                        logger.debug("getPackageTypes packageName=" + packageName + " add parameter " + pkg + "." + name);
                                    }
                                }
                            }
                            if (op.getReturnType()!=null)
                            {
                                pkg = op.getReturnType().getPackageName();
                                if (logger.isDebugEnabled())
                                {
                                    name = op.getReturnType().getName();
                                    logger.debug("getPackageTypes packageName=" + packageName + " return " + pkg + "." + name);
                                }
                                if (pkg!=null && pkg.equals(facade.getPackageName()) && pkg.indexOf('.') > 0 && !pkgTypes.contains(op.getReturnType()))
                                {
                                    pkgTypes.add(op.getReturnType());
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += name + ",";
                                        logger.debug("getPackageTypes packageName=" + packageName + " add return " + pkg + "." + name);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageTypes packageName=" + packageName + " " + pkgRefs);
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
        if (StringUtils.isNotEmpty(packageName))
        {
            String name = null;
            String pkg = null;
            String pkgRefs = "";
            // Copy package names and collection of related packages to package references list
            for (final Iterator iterator = types.iterator(); iterator.hasNext();)
            {
                //TreeSet pkgRef = new TreeSet(new TypeComparator());
                MetafacadeBase element = (MetafacadeBase)iterator.next();
                /*if (element instanceof ClassifierFacade)
                {
                    ClassifierFacade type = (ClassifierFacade)element;
                    pkg = type.getPackageName();
                    name = type.getName();
                }
                else if (element instanceof AssociationEndFacade)
                {
                    AssociationEndFacade type = (AssociationEndFacade)element;
                    pkg = type.getPackageName();
                    name = type.getName();
                } */
                //logger.debug("getPackageTypes packageName=" + packageName + " element " + pkg + "." + name);
                ClassifierFacade facade = service.getType(element);
                if (facade != null)
                {
                    pkg = facade.getPackageName();
                    if (logger.isDebugEnabled())
                    {
                        name = facade.getName();
                        logger.debug("getPackageTypes packageName=" + packageName + " facade " + pkg + "." + name);
                    }
                    if (pkg != null && pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(facade))
                    {
                        pkgTypes.add(facade);
                        if (logger.isDebugEnabled())
                        {
                            pkgRefs += facade.getName() + ",";
                            logger.debug("getPackageTypes packageName=" + packageName + " add facade " + facade.getPackageName() + "." + facade.getName());
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
                else if (element instanceof ClassifierFacade)
                {
                    ClassifierFacade type = (ClassifierFacade)element;
                    facade = getType(type);
                    if (facade != null)
                    {
                        if (facade instanceof ClassifierFacade)
                        {
                            type = (ClassifierFacade)getType(type);
                        }
                    }
                    pkg = type.getPackageName();
                    if (logger.isDebugEnabled())
                    {
                        name = type.getName();
                        logger.debug("getPackageTypes packageName=" + packageName + " elementTypeLogic " + pkg + "." + name);
                    }
                    if (pkg != null)
                    {
                        if (pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(type))
                        {
                            pkgTypes.add(type);
                            if (logger.isDebugEnabled())
                            {
                                pkgRefs += type.getName() + ",";
                                logger.debug("getPackageTypes packageName=" + packageName + " add typeLogic " + type.getPackageName() + "." + type.getName());
                            }
                        }
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("ClassifierFacade pkg=" + packageName + " refPkg=" + pkg + " name=" + type.getName());
                        }
                        // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                        for (final Iterator itAttr = type.getProperties(follow).iterator(); itAttr.hasNext();)
                        {
                            try
                            {
                                ModelElementFacade attr = ((ModelElementFacade)itAttr.next());
                                if (getType(attr) != null)
                                {
                                    attr = getType(attr);
                                }
                                pkg = attr.getPackageName();
                                if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(attr))
                                {
                                    pkgTypes.add(attr);
                                    if (logger.isDebugEnabled())
                                    {
                                        name = attr.getName();
                                        pkgRefs += attr.getName() + ",";
                                        logger.debug("getPackageTypes packageName=" + packageName + " add attr " + attr.getPackageName() + "." + attr.getName());
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageTypes packageName=" + packageName + " add attr " + pkg + "." + name + ": " + e);
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
                    if (facade != null && facade instanceof ClassifierFacade)
                    {
                        ClassifierFacade typeLogic = (ClassifierFacade)facade;
                        pkg = typeLogic.getPackageName();
                        if (pkg != null && pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(typeLogic))
                        {
                            pkgTypes.add(typeLogic);
                            if (logger.isDebugEnabled())
                            {
                                name = typeLogic.getName();
                                pkgRefs += typeLogic.getName() + ",";
                                logger.debug("getPackageTypes packageName=" + packageName + " add typeLogic " + type.getPackageName() + "." + type.getName());
                            }
                        }
                    }
                    else
                    {
                        pkg = type.getPackageName();
                        if (logger.isDebugEnabled())
                        {
                            name = type.getName();
                            logger.debug("getPackageTypes packageName=" + packageName + " associationEnd " + pkg + "." + name);
                        }
                        if (pkg != null)
                        {
                            if (pkg.indexOf('.') > 0 && pkg.equals(packageName) && !pkgTypes.contains(type))
                            {
                                pkgTypes.add(type);
                                if (logger.isDebugEnabled())
                                {
                                    pkgRefs += type.getName() + ",";
                                    logger.debug("getPackageTypes packageName=" + packageName + " add typeAssoc " + type.getPackageName() + "." + type.getName());
                                }
                            }
                            // Get the other end reference itself, then get the navigable connecting ends
                            try
                            {
                                ModelElementFacade otherEnd = (type.getOtherEnd());
                                if (getType(otherEnd) != null)
                                {
                                    otherEnd = getType(otherEnd);
                                }
                                pkg = otherEnd.getPackageName();
                                if (logger.isDebugEnabled())
                                {
                                    name = otherEnd.getName();
                                    logger.debug("getPackageTypes packageName=" + packageName + " otherEnd " + pkg + "." + name);
                                }
                                if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(otherEnd))
                                {
                                    pkgTypes.add(otherEnd);
                                    if (logger.isDebugEnabled())
                                    {
                                        pkgRefs += otherEnd.getName() + ",";
                                        logger.debug("getPackageTypes packageName=" + packageName + " add otherEnd " + otherEnd.getPackageName() + "." + otherEnd.getName());
                                    }
                                }
                            }
                            catch (RuntimeException e)
                            {
                                logger.error("getPackageTypes packageName=" + packageName + " add otherEnd " + pkg + "." + name + ": " + e);
                            }
                            // Duplicates logic in wsdl.vsl so that referenced packages are the same.
                            for (final Iterator otherEnds = type.getType().getNavigableConnectingEnds(follow).iterator(); otherEnds.hasNext();)
                            {
                                ModelElementFacade otherEnd = null;
                                try
                                {
                                    otherEnd = ((ModelElementFacade)otherEnds.next());
                                    if (getType(otherEnd) != null)
                                    {
                                        otherEnd = getType(otherEnd);
                                    }
                                    pkg = otherEnd.getPackageName();
                                    if (logger.isDebugEnabled())
                                    {
                                        name = otherEnd.getName();
                                        logger.debug("getPackageTypes packageName=" + packageName + " NavOtherEnd " + otherEnd.getPackageName() + "." + otherEnd.getName());
                                    }
                                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(otherEnd))
                                    {
                                        pkgTypes.add(otherEnd);
                                        if (logger.isDebugEnabled())
                                        {
                                            pkgRefs += otherEnd.getName() + ",";
                                            logger.debug("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + otherEnd.getPackageName() + "." + otherEnd.getName());
                                        }
                                    }
                                }
                                catch (RuntimeException e)
                                {
                                    if (otherEnd!=null)
                                    {
                                        logger.error("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + otherEnd.getPackageName() + "." + otherEnd.getName() + ": " + e);
                                    }
                                    else
                                    {
                                        logger.error("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + type.getType().getPackageName() + "." + type.getType().getName() + ": " + e);
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
                            pkgRefs += type.getName() + ",";
                            logger.debug("getPackageTypes packageName=" + packageName + " add NavOtherEnd " + type.getPackageName() + "." + type.getName());
                        }
                    }
                }
                else
                {
                    // Log the type so we can extend this logic later...
                    logger.error("getPackageTypes Unexpected element in service " + pkg + "." + element + " type: " + facade);
                }
            }
            // Add package types from the operations of the service package itself
            for (final Iterator iterator = service.getAllowedOperations().iterator(); iterator.hasNext();)
            {
                WebServiceOperationLogicImpl op = (WebServiceOperationLogicImpl)iterator.next();
                for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
                {
                    ModelElementFacade arg = (ModelElementFacade)opiterator.next();
                    pkg = arg.getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg))
                    {
                        pkgTypes.add(arg);
                        if (logger.isDebugEnabled())
                        {
                            name = arg.getName();
                            pkgRefs += arg.getName() + ",";
                            logger.debug("getPackageTypes packageName=" + packageName + " add service exception " + arg.getPackageName() + "." + arg.getName());
                        }
                    }
                }
                for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
                {
                    WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
                    pkg = arg.getType().getPackageName();
                    if (pkg!=null && pkg.equals(packageName) && pkg.indexOf('.') > 0 && !pkgTypes.contains(arg.getType()))
                    {
                        pkgTypes.add(arg.getType());
                        if (logger.isDebugEnabled())
                        {
                            name = arg.getName();
                            pkgRefs += arg.getName() + ",";
                            logger.debug("getPackageTypes packageName=" + packageName + " add service parameter " + arg.getPackageName() + "." + arg.getName());
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
                            pkgRefs += op.getReturnType().getName() + ",";
                            logger.debug("getPackageTypes packageName=" + packageName + " add service returnType " + op.getReturnType().getPackageName() + "." + op.getReturnType().getName());
                        }
                    }
                }
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("getPackageTypes packageName=" + packageName + " " + pkgRefs);
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
        if (facade.getNavigableConnectingEnds(follow).size() > 0)
        {
            return true;
        }
        // Determine if attributes are multiple or anything other than simple types
        for (final Iterator it = facade.getAttributes(follow).iterator(); it.hasNext();)
        {
            AttributeFacade attr = (AttributeFacade)it.next();
            if (attr.getUpper() > 1 || attr.getUpper() == -1)
            {
                return true;
            }
            // can't think of an easy way to determine simple type, just look at attribute package / type
            String pkg = attr.getType().getFullyQualifiedName(false);
            if (StringUtils.isEmpty(pkg) || pkg.indexOf('.')<1)
            {
                //TODO: Make sure all types are mapped
                // Type mapping is missing? No FQN type, but xs:<type> is still output.
                if (logger.isDebugEnabled())
                {
                    String fqn = attr.getGetterSetterTypeName();
                    String cpkg= attr.getType().getPackageName();
                    logger.debug("attr=" + attr.getName() + " pkg=" + pkg + " fqn=" + fqn + " cpkg=" + cpkg);
                }
            }
            else if (pkg.length()<9)
            {
                // Assume complex type if package name is too short but still contains '.'
                return true;
            }
            else
            {
                pkg=pkg.substring(0, 9);
                if (!pkg.equals("java.lang") && !pkg.equals("java.util") && !pkg.equals("java.math"))
                {
                    return true;
                }
            }
        }
        return rtn;
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
            for (Iterator iterator = parent.getSourceDependencies().iterator(); iterator.hasNext();) 
            {
                ModelElementFacade dependency = (ModelElementFacade) iterator.next();
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
                logger.debug("facade=" + facade + " parent=" + parent);
            }
            if (facade instanceof ClassifierFacade)
            {
                ClassifierFacade attr = (ClassifierFacade) facade;
                type = attr;
                typeName = attr.getFullyQualifiedName();
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
                    typeName = collectionType + "<" + type.getFullyQualifiedName() + ">";
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
            // TODO: Make this work for attribute types other than String.
            if (parent != null && StringUtils.isEmpty(defaultValue) && (typeName.equals("String") || typeName.equals("java.lang.String")))
            {
                // See if a named dependency exists with the same facadeName
                for (Iterator iterator = parent.getSourceDependencies().iterator(); iterator.hasNext();) 
                {
                    ModelElementFacade dependency = (ModelElementFacade) iterator.next();
                    if (dependency.getName().equals(facade.getName()) && dependency instanceof DependencyFacade)
                    {
                        facade = ((DependencyFacade)dependency).getTargetElement();
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
                            if (StringUtils.isEmpty(wsdlType.getQName()))
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
            if (facade instanceof EnumerationFacade)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("facade=" + facade + " type=" + type + " default=" + defaultValue);
                }
                EnumerationFacade enumer = (EnumerationFacade) facade;
                //type = enumer.getLiteralType().getFullyQualifiedName();
                Collection<AttributeFacade> literals = enumer.getLiterals();
                if (StringUtils.isEmpty(defaultValue) && !literals.isEmpty())
                {
                    // Just get the first enumeration literal
                    Iterator it = literals.iterator();
                    defaultValue = ((ModelElementFacade) it.next()).getName();
                    // Literal value is always a String. Remove quotes if part of default.
                    StringUtils.remove(defaultValue, "\"");
                    defaultValue = enumer.getFullyQualifiedName() + "." + defaultValue;
                }
                else
                {
                    defaultValue = enumer.getName() + "." + defaultValue;
                }
                isEnumeration = true;
            }
            if (useMany && (isMany==null || isMany ))
            {
                if (!typeName.startsWith("java.util"))
                {
                    rtn = "new " + collectionType + "<" + typeName + ">()";
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
            else if (typeName.equals("String") || typeName.equals("java.lang.String"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? defaultValue : "\"" + name + "\"");
            }
            else if (typeName.equals("Boolean") || typeName.equals("java.lang.Boolean"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? "new Boolean(" + defaultValue +")" : "new Boolean(true)");
            }
            else if (typeName.equals("boolean"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? defaultValue : "true");
            }
            else if (typeName.equals("int") || typeName.equals("short") || typeName.equals("long") 
                    || typeName.equals("byte") || typeName.equals("float") || typeName.equals("double"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? defaultValue : "1");
            }
            else if (typeName.equals("java.util.Date"))
            {
                rtn = "new " + typeName + "()";
            }
            else if (typeName.equals("java.sql.Timestamp"))
            {
                rtn = "new java.sql.Timestamp(System.currentTimeMillis())";
            }
            else if (typeName.equals("java.util.Calendar"))
            {
                rtn = "java.util.Calendar.getInstance()";
            }
            else if (typeName.equals("char"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? defaultValue : "'c'");
            }
            else if (typeName.equals("Character"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? "new Character(" + defaultValue : "new Character('0')");
            }
            else if (typeName.equals("Byte") || typeName.equals("java.lang.Byte"))
            {
                rtn = "new Byte(\"" + facade.getName() + "\")";
            }
            else if (typeName.equals("Short") || typeName.equals("java.lang.Short")
                    || typeName.equals("Integer") || typeName.equals("java.lang.Integer")
                    || typeName.equals("Long") || typeName.equals("java.lang.Long")
                    || typeName.equals("Float") || typeName.equals("java.lang.Float")
                    || typeName.equals("Double") || typeName.equals("java.lang.Double")
                    || typeName.equals("java.math.BigDecimal"))
            {
                rtn = (!StringUtils.isEmpty(defaultValue) ? "new " + typeName + "(" + defaultValue + ")" : "new " + typeName + "(1)");
            }
            else if (typeName.equals("java.math.BigInteger"))
   {
            rtn = (!StringUtils.isEmpty(defaultValue) ? "java.math.BigInteger.valueOf(" + defaultValue + ")" : "java.math.BigInteger.valueOf(1)");
   }
            else if (typeName.equals("byte[]"))
            {
                rtn = "{1}";
            }
            else if (typeName.equals("byte[]"))
            {
                rtn = "{1}";
            }
            else if (isEnumeration)
            {
                if (useMany)
                {
                    rtn = collectionType + "<" + defaultValue + ">";
                }
                else
                {
                    rtn = defaultValue;
                }
            }
            else if (!StringUtils.isEmpty(defaultValue))
            {
                rtn = "new " + typeName + "(" + defaultValue + ")";
            }
            else if (type != null && type.hasStereotype("Entity"))
            {
                // Entity classes will always be abstract with Impl generated classes.
                rtn = "(" + typeName + ")new " + typeName + "Impl()";
            }
            else if (facade instanceof GeneralizableElementFacade)
            {
                // If type has a descendant with name <typeName>Impl, assume typeNameImpl must be instantiated instead of typeName
                rtn = "new " + typeName + "()";
                if (facade instanceof ClassifierFacade)
                {
                    ClassifierFacade classifier = (ClassifierFacade)facade;
                    // If type is abstract, choose Impl descendant if exists, or the last descendant
                    if (classifier.isAbstract())
                    {
                        // Can't instantiate abstract class - pick some descendant
                        Iterator iter = classifier.getSpecializations().iterator();
                        while (iter.hasNext())
                        {
                            ClassifierFacade spec = (ClassifierFacade)iter.next();
                            if (spec.getName().equals(facade.getName() + "Impl"))
                            {
                                rtn = "(" + facade.getName() + ")new " + typeName + "Impl()";
                                break;
                            }
                            rtn = "(" + facade.getName() + ")new " + spec.getFullyQualifiedName() + "()";
                        }
                    }
                }
                GeneralizableElementFacade generalization = (GeneralizableElementFacade)facade;
                Iterator iter = generalization.getSpecializations().iterator();
                while (iter.hasNext())
                {
                    ClassifierFacade spec = (ClassifierFacade)iter.next();
                    if (spec.getName().equals(facade.getName() + "Impl"))
                    {
                        rtn = "(" + facade.getName() + ")new " + spec.getFullyQualifiedName() + "Impl()";
                    }
                }
            }
            else
            {
                rtn = "new " + typeName + "()";
            }
            rtn = StringUtils.replace(rtn, "java.util.Collection", "java.util.ArrayList") + toString;
            if (logger.isDebugEnabled())
            {
                logger.debug("facade=" + facade + " facadeName=" + facade.getName() + " type=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue + " rtn=" + rtn);
            }
        } catch (RuntimeException e) {
            logger.error(e + " facade=" + facade + " facadeName=" + facade.getName() + " parent=" + parent + " type=" + typeName + " name=" + name + " isMany=" + isMany + " defaultValue=" + defaultValue);
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
    private Collection<WebServicePackageLogic> getServiceDescendantPackages(ModelElementFacade element, Collection<WebServicePackageLogic> pkgRef, Collection<ModelElementFacade> added)
    {
        if (element==null) return pkgRef;                
        ModelElementFacade pkg = element.getPackage();
        if (pkg==null || pkg.getFullyQualifiedName().indexOf('.') < 1
            || pkg.getFullyQualifiedName().startsWith("java.util.")) return pkgRef;                
        //name = ex.getName();
        if (!pkgRef.contains(pkg) && pkg instanceof WebServicePackageLogic)
        {
            pkgRef.add((WebServicePackageLogic)pkg);
        }
        if (logger.isDebugEnabled() && pkg.getName().indexOf('.')>0)
        {
            logger.debug("getServiceDescendantPackages " + element.getFullyQualifiedName() + " package " + pkg.getName() + " size=" + pkgRef.size());
        }
        if (element instanceof ClassifierFacade)
        {
            ClassifierFacade classifier = (ClassifierFacade) element;
            // Use commented and change getAllProperties to getProperties, if only descendant references are needed
            /*for (final Iterator descendants = classifier.getAllSpecializations().iterator(); descendants.hasNext();)
            {
                ModelElementFacade descendant = (ModelElementFacade)descendants.next();
                pkgRef = getServiceDescendantPackages(descendant, pkgRef);
                // Get all associations and attributes, down the inheritance hierarchy
            }*/
            for (final Iterator argiterator = classifier.getAllProperties().iterator(); argiterator.hasNext();)
            {
                ModelElementFacade property = (ModelElementFacade)argiterator.next();
                if (property instanceof AttributeFacade)
                {
                    AttributeFacade attrib = (AttributeFacade) property;
                    if (!attrib.getType().equals(classifier) && !added.contains(attrib.getType())
                        && attrib.getType().getPackageName().indexOf('.')>0)
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("getServiceDescendantPackages " + attrib.getName() + " " + attrib.getType().getFullyQualifiedName() + " attribute in " + classifier.getFullyQualifiedName() + " size=" + pkgRef.size());
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
                            logger.debug("getServiceDescendantPackages " + assoc.getName() + " " + assoc.getType().getFullyQualifiedName() + " association in " + classifier.getFullyQualifiedName() + " size=" + pkgRef.size());
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
    public Collection<WebServicePackageLogic> getServiceDescendantPackages(WebServiceLogicImpl service, boolean follow)
    {
        // Note: The way XmlSeeAlso is supposed to work: any descendant classes from parameter or return or exception classes
        // not directly referenced by the XML types should have their package ObjectFactory added to the reference list.
        // The way CXF works: parameter types and type hierarchy referenced by the service are added.
        Collection<WebServicePackageLogic> pkgRef = new HashSet<WebServicePackageLogic>();
        // Keep track of elements already iterated, avoid stackOverflow.
        Collection<ModelElementFacade> added = new HashSet<ModelElementFacade>();
        // For each service parameter and return type and exception, find all descendants
        for (final Iterator<WebServiceOperation> iterator = service.getAllowedOperations().iterator(); iterator.hasNext();)
        {
            WebServiceOperation op = iterator.next();
            if (logger.isDebugEnabled())
            {
                logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + "." + op.getName() + " parms=" + op.getParameters().size() + " size=" + pkgRef.size());
            }
            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
            {
                ModelElementFacade ex = (ModelElementFacade)opiterator.next();
                if (!added.contains(ex))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + "." + op.getName() + " exception=" + ex.getFullyQualifiedName() + " size=" + pkgRef.size());
                    }
                    added.add(ex);
                    pkgRef = getServiceDescendantPackages(ex, pkgRef, added);
                }
            }
            for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
            {
                WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
                if (!added.contains(arg.getType()))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("getServiceDescendantPackages " + service.getFullyQualifiedName() + "." + op.getName() + " parameter=" + arg.getName() + " size=" + pkgRef.size());
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
        String typeRefs = "";
        // Copy package names and collection of related packages to package references list
        // Add references from the operations of the service package itself
        for (final Iterator iterator = service.getAllowedOperations().iterator(); iterator.hasNext();)
        {
            boolean isMany = false;
            WebServiceOperationLogicImpl op = (WebServiceOperationLogicImpl)iterator.next();
            for (final Iterator opiterator = op.getExceptions().iterator(); opiterator.hasNext();)
            {
                ModelElementFacade ex = (ModelElementFacade)opiterator.next();
                pkg = ex.getPackageName();
                //name = ex.getName();
                if (pkg!=null && pkg.indexOf('.') > 0 && !typeRef.contains(ex) && !opRef.contains(ex))
                {
                    opRef.add(ex);
                    if (logger.isDebugEnabled())
                    {
                        typeRefs += ex + ",";
                        logger.debug("getServiceReferences exception " + pkg + "." + ex.getName());
                    }
                }
            }
            for (final Iterator opiterator = op.getParameters().iterator(); opiterator.hasNext();)
            {
                WebServiceParameterLogicImpl arg = (WebServiceParameterLogicImpl)opiterator.next();
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
                        logger.debug("getServiceReferences parameter " + pkg + "." + arg.getType().getName());
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
/*        for (final Iterator iterator = typeRef.iterator(); iterator.hasNext();)
        {
            ClassifierFacade type = (ClassifierFacade)iterator.next();
            if (logger.isDebugEnabled())
            {
                logger.debug("getServiceReferences packageName=" + type.getPackageName() + "." + type.getName());
            }
        }*/
        if (logger.isDebugEnabled())
        {
            logger.debug("getServiceReferences typeRef " + service.getPackageName() + "." + service.getName() + " " + typeRef);
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
        if (packageName.startsWith("http://"))
        {
            packageName = packageName.substring(7);
        }
        return StringUtils.reverseDelimited(packageName, WebServiceGlobals.NAMESPACE_DELIMITER);
    }

    private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssZ");
    
    /**
     * Returns the current Date in the specified format.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.toLocalizedPattern()))
        {
            df = new SimpleDateFormat(format);
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
        StringBuffer schemaType = new StringBuffer();
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
