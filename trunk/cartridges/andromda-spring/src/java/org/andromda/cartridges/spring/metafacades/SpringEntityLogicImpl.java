package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntity.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntity
 */
public class SpringEntityLogicImpl
    extends SpringEntityLogic
    implements org.andromda.cartridges.spring.metafacades.SpringEntity
{
    /**
     * Value for one Table per root class
     */
    private static final String INHERITANCE_STRATEGY_CLASS = "class";

    /**
     * Value for joined-subclass
     */
    private static final String INHERITANCE_STRATEGY_SUBCLASS = "subclass";

    /**
     * Value for one Table per concrete class
     */
    private static final String INHERITANCE_STRATEGY_CONCRETE = "concrete";

    /**
     * Value make Entity an interface, delegate attributes to subclasses.
     */
    private static final String INHERITANCE_STRATEGY_INTERFACE = "interface";

    // ---------------- constructor -------------------------------

    public SpringEntityLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoName()
     */
    protected java.lang.String handleGetDaoName()
    {
        return this.getName() + SpringGlobals.DAO_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoImplementationName()
     */
    protected java.lang.String handleGetDaoImplementationName()
    {
        return this.getName() + SpringGlobals.DAO_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.DAO_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBaseName()
     */
    protected java.lang.String handleGetDaoBaseName()
    {
        return this.getName() + SpringGlobals.DAO_BASE_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoBaseName()
     */
    protected java.lang.String handleGetFullyQualifiedDaoBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getImplementationName()
     */
    protected java.lang.String handleGetEntityImplementationName()
    {
        return this.getEntityName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedEntityImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getBeanName(boolean)
     */
    protected java.lang.String handleGetBeanName(boolean targetSuffix)
    {
        StringBuffer beanName = new StringBuffer(StringUtils
            .uncapitalize(StringUtils.trimToEmpty(this.getName())));
        beanName.append(SpringGlobals.DAO_SUFFIX);
        if (targetSuffix)
        {
            beanName.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this
            .getConfiguredProperty("entityNamePattern");
        return MessageFormat.format(entityNamePattern, new String[]
        {
            StringUtils.trimToEmpty(this.getName())
        });
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getEntityName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateGeneratorClass()
     */
    protected String handleGetHibernateGeneratorClass()
    {
        String hibernateGeneratorClass = (String)this
            .findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_GENERATOR_CLASS);
        if (StringUtils.isBlank(hibernateGeneratorClass))
        {
            hibernateGeneratorClass = (String)this
                .getConfiguredProperty("defaultHibernateGeneratorClass");
        }
        return hibernateGeneratorClass;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getRoot()
     */
    protected Object handleGetRoot()
    {
        GeneralizableElementFacade generalization = this;
        for (; generalization.getGeneralization() != null
            && SpringEntity.class.isAssignableFrom(generalization
                .getGeneralization().getClass()); generalization = generalization
            .getGeneralization());
        return generalization;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public java.util.Collection getProperties()
    {
        Collection properties = this.getAttributes();
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(connectingEnds, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        });
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;
                return end.isNavigable()
                    || (end.getOtherEnd().isChild() && isForeignHibernateGeneratorClass());
            }
        }
        CollectionUtils.filter(connectingEnds, new NavigableFilter());
        properties.addAll(connectingEnds);
        return properties;
    }

    private static final String HIBERNATE_GENERATOR_CLASS_FOREIGN = "foreign";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isForeignHibernateGeneratorClass()
     */
    protected boolean handleIsForeignHibernateGeneratorClass()
    {
        return this.getHibernateGeneratorClass().equalsIgnoreCase(
            HIBERNATE_GENERATOR_CLASS_FOREIGN);
    }

    private static final String HIBERNATE_GENERATOR_CLASS_SEQUENCE = "sequence";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isSequenceHibernateGeneratorClass()
     */
    protected boolean handleIsSequenceHibernateGeneratorClass()
    {
        return this.getHibernateGeneratorClass().equalsIgnoreCase(
            HIBERNATE_GENERATOR_CLASS_SEQUENCE);
    }

    /**
     * The namespace property storing the hibernate default-cascade value for an
     * entity.
     */
    private static final String HIBERNATE_DEFAULT_CASCADE = "hibernateDefaultCascade";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateDefaultCascade()
     */
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isEntityBusinessOperationsPresent()
     */
    protected boolean handleIsEntityBusinessOperationsPresent()
    {
        return this.getEntityBusinessOperations() != null
            && !this.getEntityBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoBusinessOperationsPresent()
     */
    protected boolean handleIsDaoBusinessOperationsPresent()
    {
        return this.getDaoBusinessOperations() != null
            && !this.getDaoBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBusinessOperations()
     */
    protected Collection handleGetDaoBusinessOperations()
    {
        // operations that are not finders and static
        Collection finders = getFinders();
        Collection operations = getOperations();

        Collection nonFinders = CollectionUtils.subtract(operations, finders);
        return new FilteredCollection(nonFinders)
        {
            public boolean evaluate(Object object)
            {
                return ((OperationFacade)object).isStatic();
            }
        };
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityBusinessOperations()
     */
    protected Collection handleGetEntityBusinessOperations()
    {
        // operations that are not finders and not static
        Collection finders = getFinders();
        Collection operations = getOperations();

        Collection nonFinders = CollectionUtils.subtract(operations, finders);
        return new FilteredCollection(nonFinders)
        {
            public boolean evaluate(Object object)
            {
                return ((OperationFacade)object).isStatic() == false;
            }
        };
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getValueObjectReferences()
     */
    protected Collection handleGetValueObjectReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && targetElement
                        .hasStereotype(UMLProfile.STEREOTYPE_VALUE_OBJECT);
            }
        };
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoImplementationRequired()
     */
    protected boolean handleIsDaoImplementationRequired()
    {
        return !this.getValueObjectReferences().isEmpty()
            || !this.getDaoBusinessOperations().isEmpty()
            || !this.getFinders(true).isEmpty();
    }

    /**
     * The suffix given to the no transformation constant.
     */
    private static final String NO_TRANSFORMATION_CONSTANT_SUFFIX = "NONE";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoNoTransformationConstantName()
     */
    protected String handleGetDaoNoTransformationConstantName()
    {
        return SpringGlobals.TRANSFORMATION_CONSTANT_PREFIX
            + NO_TRANSFORMATION_CONSTANT_SUFFIX;
    }

    /**
     * Common routine to check inheritance.
     */
    protected boolean checkHibInheritance(String inheritance)
    {
        if (logger.isDebugEnabled())
        {
            boolean result = inheritance.equals(this.getHibernateInheritanceStrategy());
            logger.debug("<<< checkHibInheritance[" + this + "]: "
                + inheritance + " = " + result);
            return result;
        }
        return inheritance.equals(getHibernateInheritanceStrategy());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceClass()
     */
    protected boolean handleIsHibernateInheritanceClass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_CLASS);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceInterface()
     */
    protected boolean handleIsHibernateInheritanceInterface()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_INTERFACE);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceSubclass()
     */
    protected boolean handleIsHibernateInheritanceSubclass()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_SUBCLASS);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isHibernateInheritanceConcrete()
     */
    protected boolean handleIsHibernateInheritanceConcrete()
    {
        return checkHibInheritance(INHERITANCE_STRATEGY_CONCRETE);
    }

    /**
     * Return all the business operations, used when leafImpl true.
     * 
     * @return all business operations
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getAllBusinessOperations()
     */
    protected Collection handleGetAllBusinessOperations()
    {
        EntityFacade superElement = (EntityFacade)this.getGeneralization();

        Collection result = super.getBusinessOperations();
        while (superElement != null)
        {
            result.addAll(superElement.getBusinessOperations());
            superElement = (EntityFacade)superElement.getGeneralization();
        }
        return result;
    }

    /**
     * Return true if this Entity is a root in terms of Hibernate, eq has a
     * hbm.xml file. interface - false
     * 
     * @return true if this Entity is a root
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#isRootInheritanceEntity()
     */
    protected boolean handleIsRootInheritanceEntity()
    {
        boolean result = false;
        try
        {

            if (logger.isDebugEnabled())
                logger.debug(">>> handleIsRootInheritanceEntity start:" + this
                    + " : " + "\"" + getInheritance(this) + "\"");

            GeneralizableElementFacade superElement = this.getGeneralization();
            if (superElement == null)
            {
                String inheritance = getInheritance(this);
                // We are a root if we are the base class and not interface
                // inheritance
                result = (inheritance == null)
                    || !inheritance.equals(INHERITANCE_STRATEGY_INTERFACE);
            }
            else
            {
                // We are a subclass
                GeneralizableElementFacade root = getRootInheritanceEntity();
                result = root.getFullyQualifiedName().equals(
                    getFullyQualifiedName());
            }
        }
        catch (Exception e)
        {
            ExceptionRecorder.record("handleIsRootInheritanceEntity", e);
            throw new RuntimeException(e.getMessage(), e);
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< handleIsRootInheritanceEntity[" + this + "]:"
                + result);
        return result;
    }

    private GeneralizableElementFacade[] getSuperClassList()
    {
        GeneralizableElementFacade superElement = this.getGeneralization();
        ArrayList hierarchy = new ArrayList();
        while (superElement != null)
        {
            if (logger.isDebugEnabled())
                logger.debug("*** getRootInheritanceEntity element:"
                    + superElement + " : " + getInheritance(superElement));
            hierarchy.add(superElement);
            superElement = superElement.getGeneralization();
        }
        GeneralizableElementFacade[] superclasses;
        superclasses = new GeneralizableElementFacade[hierarchy.size()];
        superclasses = (GeneralizableElementFacade[])hierarchy
            .toArray(superclasses);
        return superclasses;
    }

    /**
     * Return the entity which is the root in Hibernate terms. If we have class
     * there is one table from where the first Entity which is defined as class.
     * If subclass there are 1 + number of subclasses tables. So if we are the
     * subclass defined Entity or the subclass of a subclass defined Entity we
     * are a root. If concrete we are a root.
     */
    private GeneralizableElementFacade getRootInheritanceEntity()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> getRootInheritanceEntity start:" + this + " : "
                + getInheritance(this));
        GeneralizableElementFacade result = null;
        // The root of the hierarchy is the last element added.
        GeneralizableElementFacade[] superclasses;
        superclasses = getSuperClassList();
        int rootIndex = superclasses.length - 1;
        // Search down from the root looking for an inheritance
        // specification.
        String inheritance;
        for (int ctr = rootIndex; ctr > -1; ctr--)
        {
            inheritance = getInheritance(superclasses[ctr]);
            if (inheritance == null)
            {
                // Default = class
                result = superclasses[ctr];
                break;
            }
            if (inheritance.equals(INHERITANCE_STRATEGY_SUBCLASS))
            {
                result = superclasses[ctr];
                break;
            }
            if (inheritance.equals(INHERITANCE_STRATEGY_CLASS))
            {
                result = superclasses[ctr];
                break;
            }
        }
        if (result == null)
        {
            // Must be all concrete, odd
            result = this;
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< getRootInheritanceEntity return:" + result);
        return result;
    }

    private String hibernateInheritanceStrategy;

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getInheritanceStrategy()
     */
    protected String handleGetHibernateInheritanceStrategy()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> handleGetInheritanceStrategy start:" + this);

        if (hibernateInheritanceStrategy == null)
        {
            try
            {
                hibernateInheritanceStrategy = getSuperInheritance();
                if (hibernateInheritanceStrategy == null)
                {
                    hibernateInheritanceStrategy = getInheritance(this);
                }
                if (hibernateInheritanceStrategy == null)
                {
                    hibernateInheritanceStrategy = INHERITANCE_STRATEGY_CLASS;
                }
            }
            catch (Exception ex)
            {
                String errorMessage = "*** " + getClass().getName()
                    + " handleGetInheritanceStrategy exception:" + ex;
                ExceptionRecorder.record(errorMessage, ex, "hibernate");
                logger.error(errorMessage);
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< handleGetInheritanceStrategy return:"
                + hibernateInheritanceStrategy);
        return hibernateInheritanceStrategy;
    }

    /**
     * Scan back up the generalization hierarchy looking for a INHERITANCE
     * strategy specification. Cases: super subclass CLASS None Allowed SUBCLASS
     * None Allowed CONCRETE CLASS | SUBCLASS
     * 
     * @return the super inheritance strategy
     */
    private String getSuperInheritance()
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> getSuperInheritance start:" + this + " : "
                + getInheritance(this));
        String rootInheritance = null;
        GeneralizableElementFacade root = this.getRootInheritanceEntity();
        GeneralizableElementFacade[] superclasses;
        superclasses = getSuperClassList();
        rootInheritance = getInheritance(root);
        if (rootInheritance == null
            || rootInheritance.equals(INHERITANCE_STRATEGY_CLASS))
        {
            validateNoInheritance(superclasses, root);
        }
        else if (rootInheritance.equals(INHERITANCE_STRATEGY_SUBCLASS))
        {
            validateNoInheritance(superclasses, root);
        }
        else if (rootInheritance.equals(INHERITANCE_STRATEGY_CONCRETE))
        {
            rootInheritance = validateConcreteInheritance(superclasses);
        }
        else if (rootInheritance.equals(INHERITANCE_STRATEGY_INTERFACE))
        {
            rootInheritance = validateInterfaceInheritance(superclasses);
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< getSuperInheritance return:" + rootInheritance);
        return rootInheritance;
    }

    /**
     * Check no classes have an inheritance tag.
     * 
     * @param superclasses
     */
    private void validateNoInheritance(
        GeneralizableElementFacade[] superclasses,
        GeneralizableElementFacade root)
    {
        for (int i = 0; i < superclasses.length - 1; i++)
        {
            // Scan until the logical root in hibernate terms
            if (root == superclasses[i])
            {
                break;
            }
            String inheritance = getInheritance(superclasses[i]);
            if (inheritance != null)
            {
                AndroMDALogger.warn("Inheritance tagged value:" + inheritance
                    + " on " + superclasses[i] + " ignored.");
            }
        }
    }

    /**
     * Check if an intermediate class has a class or subclass tag, if so return
     * that as the inheritance strategy. This is the only permitted mixed case.
     * Also check subclass/class mixes.
     * 
     * @param superclasses
     * @return String inheritance strategy
     */
    private String validateConcreteInheritance(
        GeneralizableElementFacade[] superclasses)
    {
        if (logger.isDebugEnabled())
            logger.debug(">>> validateConcreteInheritance:" + this);
        String result = null;
        String rootInheritance = INHERITANCE_STRATEGY_CONCRETE;
        // Search from root class but 1 to lowest.
        for (int i = superclasses.length - 1; i > -1; i--)
        {
            String inheritance = getInheritance(superclasses[i]);
            if (inheritance != null)
            {
                if (result == null)
                {
                    // Dont at this point care which strategy is specified.
                    result = inheritance;
                }
                else
                {
                    if (!result.equals(inheritance))
                    {
                        // If we are still on concrete we can change
                        if (!result.equals(rootInheritance))
                        {
                            AndroMDALogger
                                .warn("Cannot mix inheritance super inheritance:"
                                    + result
                                    + " with "
                                    + inheritance
                                    + " on "
                                    + superclasses[i] + " ignored.");
                        }
                        else
                        {
                            result = inheritance;
                        }
                    }
                }
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< validateConcreteInheritance:" + result);
        return result;
    }

    /**
     * Get the inheritance below the interface inheritance class, currently only
     * support one level of interface.
     * 
     * @param superclasses
     * @return String inheritance strategy
     */
    private String validateInterfaceInheritance(
        GeneralizableElementFacade[] superclasses)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(">>> validateInterfaceInheritance:" + this);
            logger.debug("*** validateInterfaceInheritance superclasses:"
                + superclasses.length);
        }
        String result = null;
        int rootSubclassIndex = superclasses.length - 2;
        if (rootSubclassIndex > 0)
        {
            result = getInheritance(superclasses[rootSubclassIndex]);
            if (logger.isDebugEnabled())
                logger.debug("*** validateInterfaceInheritance rootSubclass:"
                    + result);
        }
        if (logger.isDebugEnabled())
            logger.debug("<<< validateInterfaceInheritance:" + result);
        return result;
    }

    /**
     * Return the inheritance tagged value for facade.
     * 
     * @param facade
     * @return String inheritance tagged value.
     */
    private String getInheritance(GeneralizableElementFacade facade)
    {
        return (String)facade
            .findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_INHERITANCE);
    }

    /**
     * Returns the SQL id column name. Invoked from vsl template as
     * $class.identifierColumn.
     * 
     * @return String the name of the SQL id column
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getIdentifierColumn()
     */
    protected String handleGetIdentifierColumn()
    {
        EntityAttributeFacade attribute = null;
        String columnName = null;
        Collection attributes = getAttributes();
        Predicate pred = new Predicate()
        {

            public boolean evaluate(Object object)
            {
                boolean result = false;
                try
                {
                    EntityAttributeFacade attribute = (EntityAttributeFacade)object;
                    logger
                        .debug("*** handleGetIdentifierColumn.evaluate check:"
                            + attribute);
                    result = attribute.isIdentifier();
                }
                catch (Exception ex)
                {
                    // ignore
                }
                return result;
            }
        };
        attribute = (EntityAttributeFacade)CollectionUtils.find(
            attributes,
            pred);
        if (logger.isDebugEnabled())
            logger.debug("*** handleGetIdentifierColumn return:"
                + (attribute == null ? null : attribute.getColumnName()));
        columnName = attribute == null ? getDefaultIdentifier() : attribute.getColumnName();
        return columnName;
    }

    private String getDefaultIdentifier()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDiscriminatorColumn()
     */
    protected String handleGetDiscriminatorColumn()
    {
        return "class";
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDiscriminatorType()
     */
    protected String handleGetDiscriminatorType()
    {
        return "string";
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDiscriminatorLength()
     */
    protected int handleGetDiscriminatorLength()
    {
        return 1;
    }

}