package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Metaclass facade implementation.
 * 
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade
 */
public class MetafacadeFacadeLogicImpl
    extends MetafacadeFacadeLogic
    implements org.andromda.cartridges.meta.metafacades.MetafacadeFacade
{
    private static Logger logger = Logger
        .getLogger(MetafacadeFacadeLogicImpl.class);

    // ---------------- constructor -------------------------------

    public MetafacadeFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class MetafacadeFacade ...

    public java.lang.String handleGetImplSuperclassName()
    {
        Object taggedValue = findTaggedValueUpstairs(MetaProfile.TAGGEDVALUE_METAFACADE_BASECLASS);
        return (taggedValue != null) ? taggedValue.toString() : this
            .getLanguageMappings().getTo("datatype.Object");
    }

    /**
     * Finds a tagged value on the current element or on a package in the
     * hierarchy above it.
     * 
     * @param taggedValueName the name of the tagged value
     * @return the value of the tagged value
     */
    private Object findTaggedValueUpstairs(String taggedValueName)
    {
        Object taggedValue = null;
        ModelElementFacade modelElement = this;
        do
        {
            // try to find this tagged value
            taggedValue = modelElement.findTaggedValue(taggedValueName);
            if (taggedValue != null)
            {
                // return if found
                return taggedValue;
            }

            // if not found, walk up in the package hierarchy
            modelElement = modelElement.getPackage();
        }
        while (modelElement != null);
        return null; // not found
    }

    // ------------- relations ------------------

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt;&gt; that is
     * connected to the metaobject via a dependency. If no metaclass is directly
     * connected, the method walks up the supertype hierarchy.
     * 
     * @return the metaclass object
     */
    public Object handleGetMetaclass()
    {
        // delegate to recursive method
        return getMetaclass(this);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is connected
     * to cl via a dependency.
     * 
     * @param cl the source classifier
     * @return the metaclass object
     */
    private ClassifierFacade getMetaclass(ClassifierFacade cl)
    {
        for (Iterator iter = cl.getDependencies().iterator(); iter.hasNext();)
        {
            DependencyFacade dep = (DependencyFacade)iter.next();
            ClassifierFacade target = (ClassifierFacade)dep.getTargetElement();
            Collection stereotypes = target.getStereotypeNames();
            if (stereotypes != null && stereotypes.size() > 0)
            {
                String stereotypeName = (String)stereotypes.iterator().next();
                if (stereotypeName.equals(MetaProfile.STEREOTYPE_METACLASS))
                {
                    return target;
                }
            }
        }

        ClassifierFacade superclass = (ClassifierFacade)cl.getGeneralization();
        return (superclass != null) ? getMetaclass(superclass) : null;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#isMetaclassDirectDependency()
     */
    public boolean handleIsMetaclassDirectDependency()
    {
        boolean isMetaClassDirectDependency = false;
        Collection dependencies = this.getDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            // there should be only one.
            DependencyFacade dependency = (DependencyFacade)dependencies
                .iterator().next();
            if (dependency != null)
            {
                ModelElementFacade targetElement = dependency
                    .getTargetElement();
                if (targetElement != null)
                {
                    isMetaClassDirectDependency = targetElement
                        .hasStereotype(MetaProfile.STEREOTYPE_METACLASS);
                }
            }
        }
        return isMetaClassDirectDependency;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicName()
     */
    public String handleGetLogicName()
    {
        return this.getName() + "Logic";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicImplName()
     */
    public String handleGetLogicImplName()
    {
        return this.getName() + "LogicImpl";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getFullyQualifiedLogicImplName()
     */
    public String handleGetFullyQualifiedLogicImplName(
        String metamodelVersionPackage)
    {
        return this.getMetafacadeSupportClassName(metamodelVersionPackage, this
            .getLogicImplName());
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getFullyQualifiedLogicName()
     */
    public String handleGetFullyQualifiedLogicName(
        String metamodelVersionPackage)
    {
        return this.getMetafacadeSupportClassName(metamodelVersionPackage, this
            .getLogicName());
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicFile(java.lang.String)
     */
    public String handleGetLogicFile(String metamodelVersionPackage)
    {
        return this.getFullyQualifiedLogicName(
            this.getLogicPackageName(metamodelVersionPackage))
            .replace('.', '/')
            + ".java";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicPackageName(java.lang.String)
     */
    public String handleGetLogicPackageName(String metamodelVersionPackage)
    {
        String packageName = StringUtils.trimToEmpty(metamodelVersionPackage);
        if (StringUtils.isEmpty(metamodelVersionPackage))
        {
            packageName = this.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getLogicImplFile(java.lang.String)
     */
    public String handleGetLogicImplFile(String metamodelVersionPackage)
    {
        return this.getFullyQualifiedLogicImplName(
            this.getLogicPackageName(metamodelVersionPackage))
            .replace('.', '/')
            + ".java";
    }

    /**
     * Creates a metafacade support class name from the given
     * <code>metamodelVersionPackage</code> (i.e. the package for the specific
     * meta model version). Support classes are the 'Logic' classes.
     * 
     * @param metamodelVersionPackage the version of the meta model
     * @param the name of the class to append to the package.
     * @return the new metafacade support class name.
     */
    private String getMetafacadeSupportClassName(
        String metamodelVersionPackage,
        String name)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(this
            .getLogicPackageName(metamodelVersionPackage));
        if (StringUtils.isNotBlank(fullyQualifiedName.toString()))
        {
            fullyQualifiedName.append(".");
            fullyQualifiedName.append(name);
        }
        return fullyQualifiedName.toString();
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeFacade#getMethodDataForPSM(boolean)
     */
    public Collection handleGetMethodDataForPSM(boolean includeSuperclasses)
    {
        HashMap map = new HashMap();
        internalGetMethodDataForPSM(map, this);
        if (includeSuperclasses)
        {
            for (ClassifierFacade cd = (ClassifierFacade)getGeneralization(); cd != null; cd = (ClassifierFacade)cd
                .getGeneralization())
            {
                internalGetMethodDataForPSM(map, (MetafacadeFacade)cd);
            }
        }
        ArrayList result = new ArrayList(map.values());
        Collections.sort(result);
        return result;
    }

    private static void internalGetMethodDataForPSM(
        HashMap map,
        MetafacadeFacade facade)
    {
        final String methodName = "MetafacadeFacadeLogicImpl.internalGetMethodDataForPSM";
        try
        {
            final String fullyQualifiedName = facade.getFullyQualifiedName();

            // translate UML attributes to getter methods
            for (Iterator iter = facade.getAttributes().iterator(); iter
                .hasNext();)
            {
                AttributeFacade att = (AttributeFacade)iter.next();
                final MethodData md = new MethodData(
                    fullyQualifiedName,
                    "public",
                    false,
                    att.getType().getFullyQualifiedName(),
                    att.getGetterName(),
                    att.getDocumentation("    * "));
                map.put(md.buildCharacteristicKey(), md);
            }

            // translate UML operations to methods
            for (Iterator iter = facade.getOperations().iterator(); iter
                .hasNext();)
            {
                OperationFacade op = (OperationFacade)iter.next();
                final UMLOperationData md = new UMLOperationData(
                    fullyQualifiedName,
                    op);
                map.put(md.buildCharacteristicKey(), md);
            }

            // translate UML associations to getter methods
            for (Iterator iter = facade.getAssociationEnds().iterator(); iter
                .hasNext();)
            {
                AssociationEndFacade ae = (AssociationEndFacade)iter.next();
                AssociationEndFacade otherEnd = ae.getOtherEnd();
                if (otherEnd.isNavigable())
                {
                    final MethodData md = new MethodData(
                        fullyQualifiedName,
                        "public",
                        false,
                        ae.getGetterSetterTypeName(),
                        otherEnd.getGetterName(),
                        otherEnd.getDocumentation("    * "));
                    map.put(md.buildCharacteristicKey(), md);
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new MetafacadeException(errMsg, th);
        }
    }

    // ------------------------------------------------------------

}