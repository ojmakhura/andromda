package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;
import java.util.Collection;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.FilteredCollection;
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
    public java.lang.String handleGetDaoName()
    {
        return this.getName() + SpringGlobals.DAO_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoName()
     */
    public java.lang.String handleGetFullyQualifiedDaoName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoImplementationName()
     */
    public java.lang.String handleGetDaoImplementationName()
    {
        return this.getName() + SpringGlobals.DAO_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedDaoImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.DAO_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBaseName()
     */
    public java.lang.String handleGetDaoBaseName()
    {
        return this.getName() + SpringGlobals.DAO_BASE_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoBaseName()
     */
    public java.lang.String handleGetFullyQualifiedDaoBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getImplementationName()
     */
    public java.lang.String handleGetEntityImplementationName()
    {
        return this.getEntityName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedEntityImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getBeanName(boolean)
     */
    public java.lang.String handleGetBeanName(boolean targetSuffix)
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
    public String handleGetEntityName()
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
    public String handleGetFullyQualifiedEntityName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getEntityName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateGeneratorClass()
     */
    public String handleGetHibernateGeneratorClass()
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
    protected SpringEntity handleGetRoot()
    {
        GeneralizableElementFacade generalization = this;
        for (; generalization.getGeneralization() != null
            && SpringEntity.class.isAssignableFrom(generalization
                .getGeneralization().getClass()); generalization = generalization
            .getGeneralization());
        return (SpringEntity)generalization;
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
     * The namespace property storing the hibernate default-cascade value
     * for an entity.
     */
    private static final String HIBERNATE_DEFAULT_CASCADE = "hibernateDefaultCascade";
    
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getHibernateDefaultCascade()
     */
    protected String handleGetHibernateDefaultCascade()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(HIBERNATE_DEFAULT_CASCADE)));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isEntityBusinessOperationsPresent()
     */
    protected boolean handleIsEntityBusinessOperationsPresent()
    {
        return this.getEntityBusinessOperations() != null && !this.getEntityBusinessOperations().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#isDaoBusinessOperationsPresent()
     */
    protected boolean handleIsDaoBusinessOperationsPresent()
    {
        return this.getDaoBusinessOperations() != null && !this.getDaoBusinessOperations().isEmpty();
    }
    
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBusinessOperations()
     */
    protected Collection handleGetDaoBusinessOperations()
    {
        // those operations that are no finders and static
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
        // those operations that are no finders and not static
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
    

}