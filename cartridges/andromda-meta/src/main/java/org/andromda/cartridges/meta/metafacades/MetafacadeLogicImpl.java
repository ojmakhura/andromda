package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Metaclass facade implementation.
 *
 * @see org.andromda.cartridges.meta.metafacades.Metafacade
 * @author Bob Fields
 */
public class MetafacadeLogicImpl
    extends MetafacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * This defines the metamodel version package name (i.e.
     * org.andromda.metafacades.uml14, org.andromda.metafacades.um22, etc) used
     * by this cartridge to create the generated impl package name, if left
     * empty then the impl package will be the same as the metafacade package
     * (therefore we default to an empty name)
     */
    private static final String METAMODEL_VERSION_PACKAGE = "metamodelVersionPackage";
    private Map<ClassifierFacade, Collection<MethodData>> featureMap = null;
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(MetafacadeLogicImpl.class);

    /**
     * @param metaObjectIn
     * @param context
     */
    public MetafacadeLogicImpl(
        Object metaObjectIn,
        String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt;&gt; that is
     * connected to the metaobject via a dependency. If no metaclass is directly
     * connected, the method walks up the supertype hierarchy.
     *
     * @return the metaclass object
     */
    @Override
    protected ClassifierFacade handleGetMetaclass()
    {
        // delegate to recursive method
        return getMetaclass(this);
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is connected
     * to classifier via a dependency.
     *
     * @param classifier the source classifier
     * @return the metaclass object
     */
    private ClassifierFacade getMetaclass(ClassifierFacade classifier)
    {
        for (DependencyFacade dep : classifier.getSourceDependencies())
        {
            ClassifierFacade target = (ClassifierFacade)dep.getTargetElement();
            Collection<String> stereotypes = target.getStereotypeNames();
            if ((stereotypes != null) && (!stereotypes.isEmpty()))
            {
                String stereotypeName = stereotypes.iterator().next();
                if (stereotypeName.equals(MetaProfile.STEREOTYPE_METACLASS))
                {
                    return target;
                }
            }
        }

        ClassifierFacade superclass = (ClassifierFacade)classifier.getGeneralization();
        return (superclass != null) ? getMetaclass(superclass) : null;
    }

    /**
     * @see Metafacade#isMetaclassDirectDependency()
     */
    @Override
    protected boolean handleIsMetaclassDirectDependency()
    {
        boolean isMetaClassDirectDependency = false;
        Collection<DependencyFacade> dependencies = this.getSourceDependencies();
        if ((dependencies != null) && !dependencies.isEmpty())
        {
            // there should be only one.
            DependencyFacade dependency = dependencies.iterator().next();
            if (dependency != null)
            {
                ModelElementFacade targetElement = dependency.getTargetElement();
                if (targetElement != null)
                {
                    isMetaClassDirectDependency = targetElement.hasStereotype(MetaProfile.STEREOTYPE_METACLASS);
                }
            }
        }
        return isMetaClassDirectDependency;
    }

    /**
     * @see Metafacade#getLogicName()
     */
    @Override
    protected String handleGetLogicName()
    {
        return this.getName() + "Logic";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getLogicImplName()
     */
    @Override
    protected String handleGetLogicImplName()
    {
        return this.getName() + "LogicImpl";
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getFullyQualifiedLogicImplName()
     */
    @Override
    protected String handleGetFullyQualifiedLogicImplName()
    {
        return this.getMetafacadeSupportClassName(this.getLogicImplName());
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getFullyQualifiedLogicName()
     */
    @Override
    protected String handleGetFullyQualifiedLogicName()
    {
        return this.getMetafacadeSupportClassName(this.getLogicName());
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getLogicFile()
     */
    @Override
    protected String handleGetLogicFile()
    {
        return this.getFullyQualifiedLogicName().replace('.', '/') + ".java";
    }

    /**
     * Gets the metamodel version package name (i.e.
     * org.andromda.metafacades.uml14, org.andromda.metafacades.um20, etc) used
     * by this cartridge to create the generated impl package name, if left
     * empty then the impl package will be the same as the metafacade package
     * (therefore we default to an empty name)
     */
    private String getMetaModelVersionPackage()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(METAMODEL_VERSION_PACKAGE));
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getLogicPackageName()
     */
    @Override
    protected String handleGetLogicPackageName()
    {
        String packageName = this.getMetaModelVersionPackage();
        if (StringUtils.isEmpty(packageName))
        {
            packageName = this.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getLogicImplFile()
     */
    @Override
    protected String handleGetLogicImplFile()
    {
        return this.getFullyQualifiedLogicImplName().replace('.', '/') + ".java";
    }

    /**
     * Creates a metafacade support class name from the given
     * <code>metamodelVersionPackage</code> (i.e. the package for the specific
     * meta model version). Support classes are the 'Logic' classes.
     *
     * @param name the name of the class to append to the package.
     * @return the new metafacade support class name.
     */
    private String getMetafacadeSupportClassName(String name)
    {
        StringBuilder fullyQualifiedName = new StringBuilder(this.getLogicPackageName());
        if (StringUtils.isNotBlank(fullyQualifiedName.toString()))
        {
            fullyQualifiedName.append('.');
            fullyQualifiedName.append(name);
        }
        return fullyQualifiedName.toString();
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeLogic#handleGetMethodDataForPSM(org.andromda.metafacades.uml.ClassifierFacade)
     */
    @Override
    protected Collection<MethodData> handleGetMethodDataForPSM(ClassifierFacade facade)
    {
        return this.getMethodDataForPSM(facade, true);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getMethodDataForPSM()
     */
    @Override
    protected Collection<MethodData> handleGetMethodDataForPSM()
    {
        return this.getMethodDataForPSM(null, false);
    }

    /**
     * Return collection of all methods of all generalization classes for the classifier
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getMethodDataForPSM(boolean)
     */
    private final Collection<MethodData> getMethodDataForPSM(
        final ClassifierFacade facade,
        final boolean includeSuperclasses)
    {
        try
        {
            final Set<String> declarationSet = new LinkedHashSet<String>();
            if (this.featureMap == null)
            {
                this.featureMap = new HashMap<ClassifierFacade, Collection<MethodData>>();
                if (includeSuperclasses && this.getGeneralizations() != null)
                {
                    for (GeneralizableElementFacade general : this.getGeneralizations())
                    {
                        final Map<String, MethodData> methodDataMap = new HashMap<String, MethodData>();
                        final ClassifierFacade metafacade = (ClassifierFacade)general;
                        for (ClassifierFacade classifier = metafacade; classifier instanceof Metafacade;
                             classifier = (ClassifierFacade)classifier.getGeneralization())
                        {
                            this.getAllFeatures(methodDataMap, declarationSet, (Metafacade)classifier);
                        }
                        this.featureMap.put(metafacade, methodDataMap.values());
                    }
                }
            }
            final List<MethodData> result = new ArrayList<MethodData>();
            if (this.featureMap != null)
            {
                Collection<MethodData> features = this.featureMap.get(facade);
                if (features != null)
                {
                    result.addAll(features);
                }
            }
            if (!includeSuperclasses)
            {
                final Map<String, MethodData> methodDataMap = new HashMap<String, MethodData>();
                this.getAllFeatures(methodDataMap, declarationSet, this);
                result.addAll(methodDataMap.values());
            }
            Collections.sort(result);
            return result;
        }
        catch (Throwable th)
        {
            throw new RuntimeException(th);
        }
    }

    /**
     * Returns method data (name, visibility, type, doc) for each property and operation in the Metafacade
     * @param methodDataMap
     * @param declarationSet
     * @param facade
     */
    private final void getAllFeatures(
        final Map<String, MethodData> methodDataMap,
        final Set<String> declarationSet,
        final Metafacade facade)
    {
        try
        {
            final String methodVisibility = "public";
            final String indendation = "     * ";
            final String fullyQualifiedName = facade.getFullyQualifiedName();

            // translate UML attributes and association ends to getter methods
            for (final Object obj : facade.getProperties())
            {
                final ModelElementFacade property = (ModelElementFacade)obj;
                MethodData method = null;
                if (property instanceof AttributeFacade)
                {
                    final AttributeFacade attribute = (AttributeFacade)property;
                    method =
                        new MethodData(
                            fullyQualifiedName,
                            methodVisibility,
                            false,
                            attribute.getGetterSetterTypeName(),
                            attribute.getGetterName(),
                            attribute.getDocumentation(indendation));
                }
                else
                {
                    final AssociationEndFacade association = (AssociationEndFacade)property;
                    method =
                        new MethodData(
                            fullyQualifiedName,
                            methodVisibility,
                            false,
                            association.getGetterSetterTypeName(),
                            association.getGetterName(),
                            association.getDocumentation(indendation));
                }
                final String declaration = method.buildMethodDeclaration(true);

                // don't add the new method data if we already have the
                // declaration from a previous generalization.
                if (!declarationSet.contains(declaration))
                {
                    methodDataMap.put(
                        method.buildCharacteristicKey(),
                        method);
                    declarationSet.add(declaration);
                }
            }

            // translate UML operations to methods
            for (OperationFacade operation : facade.getOperations())
            {
                final UMLOperationData method = new UMLOperationData(fullyQualifiedName, operation);

                // don't add the new method data if we already have the
                // declaration from a previous generalization.
                final String declaration = method.buildMethodDeclaration(true);
                if (!declarationSet.contains(declaration))
                {
                    methodDataMap.put(
                        method.buildCharacteristicKey(),
                        method);
                    declarationSet.add(declaration);
                }
            }
        }
        catch (final Throwable throwable)
        {
            MetafacadeLogicImpl.logger.error(throwable);
            throw new MetafacadeException(throwable);
        }
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#isRequiresInheritanceDelegatation()
     */
    @Override
    protected boolean handleIsRequiresInheritanceDelegatation()
    {
        boolean requiresInheritanceDelegation = false;
        final ModelElementFacade superMetafacade = this.getGeneralization();
        if (superMetafacade != null)
        {
            requiresInheritanceDelegation =
                !superMetafacade.getPackageName().equals(this.getPackageName()) ||
                (this.getGeneralizations().size() > 1);
        }
        return requiresInheritanceDelegation;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#isConstructorRequiresMetaclassCast()
     */
    @Override
    protected boolean handleIsConstructorRequiresMetaclassCast()
    {
        boolean requiresCast = false;
        final Metafacade superMetafacade = (Metafacade)this.getGeneralization();
        if (superMetafacade != null)
        {
            requiresCast = superMetafacade.isMetaclassDirectDependency() && !this.isRequiresInheritanceDelegatation();
        }
        return requiresCast;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    @Override
    public Collection<GeneralizableElementFacade> getGeneralizations()
    {
        final List generalizations = new ArrayList(super.getGeneralizationLinks());
        Collections.sort(
            generalizations,
            new GeneralizationPrecedenceComparator());
        CollectionUtils.transform(
            generalizations,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((GeneralizationFacade)object).getParent();
                }
            });
        CollectionUtils.filter(generalizations,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object instanceof Metafacade;
                }
            });
        return generalizations;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.Metafacade#getGeneralizationCount()
     */
    @Override
    protected int handleGetGeneralizationCount()
    {
        int count = 0;
        final Collection<GeneralizableElementFacade> generalizations = this.getGeneralizations();
        if (generalizations != null)
        {
            count = generalizations.size();
        }
        return count;
    }

    /**
     * Used to sort metafacade generalizations by precedence.
     */
    static final class GeneralizationPrecedenceComparator
        implements Comparator
    {
        /**
         * @see java.util.Comparator#compare(Object, Object)
         */
        public int compare(
            Object objectA,
            Object objectB)
        {
            MetafacadeGeneralization a = (MetafacadeGeneralization)objectA;
            MetafacadeGeneralization b = (MetafacadeGeneralization)objectB;
            return a.getPrecedence().compareTo(b.getPrecedence());
        }
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeLogic#getAllParents()
     */
    @Override
    protected Collection<GeneralizableElementFacade> handleGetAllParents()
    {
        Set<GeneralizableElementFacade> allParents = new LinkedHashSet<GeneralizableElementFacade> ();
        final Collection<GeneralizableElementFacade> parents = this.getGeneralizations();
        allParents.addAll(parents);
        for (Object object : parents)
        {
            if (object instanceof Metafacade)
            {
                final Metafacade metafacade = (Metafacade)object;
                allParents.addAll(metafacade.getAllParents());
            }
        }
        return allParents;
    }
    
	@Override
    public Collection<String> getAdditionalAnnotations() {
        HashSet<String> annotations = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_ADDITIONAL_ANNOTATION))
        {
            annotations.add(o.toString());
        }
        return annotations;
    }
}