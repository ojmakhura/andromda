package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.MetafacadeProperties;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;

/**
 * Metaclass facade implementation.
 */
public class EntityFacadeLogicImpl
    extends EntityFacadeLogic
    implements org.andromda.metafacades.uml.EntityFacade
{
    // ---------------- constructor -------------------------------

    public EntityFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    public void initialize()
    {
        super.initialize();
        // if there are no identfiers on this entity,
        // create and add one.
        if (!this.isIdentifiersPresent() && this.isAllowDefaultIdentifiers())
        {
            this.createIdentifier();
        }
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders
     */
    public java.util.Collection handleGetFinders()
    {
        return this.getFinders(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders(boolean)
     */
    public java.util.Collection handleGetFinders(boolean follow)
    {
        Collection finders = this.getOperations();

        MetafacadeUtils.filterByStereotype(
            finders,
            UMLProfile.STEREOTYPE_FINDER_METHOD);

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                EntityFacade entity = (EntityFacade)superClass;
                finders.addAll(entity.getFinders(follow));
            }
        }
        return finders;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers()
     */
    public java.util.Collection handleGetIdentifiers()
    {
        return this.getIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers(boolean)
     */
    public java.util.Collection handleGetIdentifiers(boolean follow)
    {
        Collection identifiers = this.getAttributes();
        MetafacadeUtils.filterByStereotype(
            identifiers,
            UMLProfile.STEREOTYPE_IDENTIFIER);

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && identifiers.isEmpty() && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                EntityFacade entity = (EntityFacade)superClass;
                identifiers.addAll(entity.getIdentifiers(follow));
            }
        }
        return identifiers;
    }

    /**
     * Creates an identifier from the default identifier properties specified
     * within a namespace.
     */
    private void createIdentifier()
    {
        Attribute identifier = UMLMetafacadeUtils.createAttribute(this
            .getDefaultIdentifier(), this.getDefaultIdentifierType(), this
            .getDefaultIdentifierVisibility());

        identifier.getStereotype().add(
            UMLMetafacadeUtils
                .findOrCreateStereotype(UMLProfile.STEREOTYPE_IDENTIFIER));

        ((Classifier)this.metaObject).getFeature().add(identifier);
    }

    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityFacade#hasIdentifiers()
     */
    public boolean handleIsIdentifiersPresent()
    {
        Collection identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getTableName()
     */
    public String handleGetTableName()
    {
        String tableNamePrefix = StringUtils.trimToEmpty(String.valueOf(this
            .getConfiguredProperty(MetafacadeProperties.TABLE_NAME_PREFIX)));
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            tableNamePrefix,
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
            this.getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getOperationCallFromAttributes(boolean)
     */
    public String handleGetOperationCallFromAttributes(boolean withIdentifiers)
    {
        return this.getOperationCallFromAttributes(withIdentifiers, false);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getOperationCallFromAttributes(boolean,
     *      boolean)
     */
    public String handleGetOperationCallFromAttributes(
        boolean withIdentifiers,
        boolean follow)
    {
        StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");

        Collection attributes = this.getAttributes();

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY))
            {
                EntityFacade entity = (EntityFacade)superClass;
                attributes.addAll(entity.getAttributes());
            }
        }

        if (attributes != null && !attributes.isEmpty())
        {
            Iterator attributeIt = attributes.iterator();
            while (attributeIt.hasNext())
            {
                EntityAttributeFacade attribute = (EntityAttributeFacade)attributeIt
                    .next();
                if (withIdentifiers || !attribute.isIdentifier())
                {
                    buffer.append(separator);
                    String typeName = attribute.getType()
                        .getFullyQualifiedName();
                    buffer.append(typeName);
                    buffer.append(" ");
                    buffer.append(attribute.getName());
                    separator = ", ";
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getAttributeTypeList(boolean,
     *      boolean)
     */
    public String handleGetAttributeTypeList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeTypeList(this.getAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributeNameList(boolean,
     *      boolean)
     */
    public String handleGetAttributeNameList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeNameList(this.getAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getRequiredAttributeTypeList(boolean,
     *      boolean)
     */
    public String handleGetRequiredAttributeTypeList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeTypeList(this.getRequiredAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacadeLogic#getRequiredAttributeNameList(boolean,
     *      boolean)
     */
    public String handleGetRequiredAttributeNameList(
        boolean follow,
        boolean withIdentifiers)
    {
        return this.getAttributeNameList(this.getRequiredAttributes(
            follow,
            withIdentifiers));
    }

    /**
     * Constructs a comma seperated list of attribute type names from the passed
     * in collection of <code>attributes</code>.
     * 
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute types.
     */
    private String getAttributeTypeList(Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(attributes, new Closure()
        {
            public void execute(Object object)
            {
                list.append(((AttributeFacade)object).getType()
                    .getFullyQualifiedName());
                list.append(comma);
            }
        });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * Constructs a comma seperated list of attribute names from the passed in
     * collection of <code>attributes</code>.
     * 
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute names.
     */
    private String getAttributeNameList(Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(attributes, new Closure()
        {
            public void execute(Object object)
            {
                list.append(((AttributeFacade)object).getName());
                list.append(comma);
            }
        });
        if (list.toString().endsWith(comma))
        {
            list.delete(list.lastIndexOf(comma), list.length());
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#isChild()
     */
    public boolean handleIsChild()
    {
        return CollectionUtils.find(this.getAssociationEnds(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd()
                    .isComposition();
            }
        }) != null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getParentEnd()
     */
    public Object handleGetParentEnd()
    {
        Object parentEnd = null;
        AssociationEndFacade end = (AssociationEndFacade)CollectionUtils.find(
            this.getAssociationEnds(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd()
                        .isComposition();
                }
            });
        if (end != null)
        {
            parentEnd = end.getOtherEnd();
        }
        return parentEnd;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getChildren()
     */
    public Collection handleGetChildEnds()
    {
        Collection childEnds = new FilteredCollection(this.getAssociationEnds())
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).isComposition();
            }
        };
        CollectionUtils.transform(childEnds, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        });
        return childEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getBusinessOperations()
     */
    public Collection handleGetBusinessOperations()
    {
        return new FilteredCollection(this.getOperations())
        {
            public boolean evaluate(Object object)
            {
                return !((ModelElementFacade)object)
                    .hasStereotype(UMLProfile.STEREOTYPE_FINDER_METHOD);
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getEntityReferences()
     */
    public Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && EntityFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributes(boolean,
     *      boolean)
     */
    public Collection handleGetAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow);
        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = true;
                if (!withIdentifiers
                    && EntityAttributeFacade.class.isAssignableFrom(object
                        .getClass()))
                {
                    valid = !((EntityAttributeFacade)object).isIdentifier();
                }
                return valid;
            }
        });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getRequiredAttributes(boolean,
     *      boolean)
     */
    public Collection handleGetRequiredAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(
            follow,
            withIdentifiers);
        CollectionUtils.filter(attributes, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                valid = ((AttributeFacade)object).isRequired();
                if (valid
                    && !withIdentifiers
                    && EntityAttributeFacade.class.isAssignableFrom(object
                        .getClass()))
                {
                    valid = !((EntityAttributeFacade)object).isIdentifier();
                }
                return valid;
            }
        });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getRequiredProperties(boolean,
     *      boolean)
     */
    public Collection handleGetRequiredProperties(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection properties = this.getProperties();
        if (follow)
        {
            CollectionUtils.forAllDo(
                this.getAllGeneralizations(),
                new Closure()
                {
                    public void execute(Object object)
                    {
                        properties.addAll(((ClassifierFacade)object)
                            .getProperties());
                    }
                });
        }
        CollectionUtils.filter(properties, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                if (AttributeFacade.class.isAssignableFrom(object.getClass()))
                {
                    valid = ((AttributeFacade)object).isRequired();
                    if (valid
                        && !withIdentifiers
                        && EntityAttributeFacade.class.isAssignableFrom(object
                            .getClass()))
                    {
                        valid = !((EntityAttributeFacade)object).isIdentifier();
                    }
                }
                else if (AssociationEndFacade.class.isAssignableFrom(object
                    .getClass()))
                {
                    valid = ((AssociationEndFacade)object).isRequired();
                }
                return valid;
            }
        });
        return properties;
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    public Short handleGetMaxSqlNameLength()
    {
        return Short.valueOf((String)this
            .getConfiguredProperty(MetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * Returns true/false on whether or not default identifiers are allowed
     */
    private boolean isAllowDefaultIdentifiers()
    {
        return Boolean
            .valueOf(
                (String)this
                    .getConfiguredProperty(MetafacadeProperties.ALLOW_DEFAULT_IDENTITIFIERS))
            .booleanValue();
    }

    /**
     * Gets the name of the default identifier.
     */
    private String getDefaultIdentifier()
    {
        return (String)this
            .getConfiguredProperty(MetafacadeProperties.DEFAULT_IDENTIFIER);
    }

    /**
     * Gets the name of the default identifier type.
     */
    private String getDefaultIdentifierType()
    {
        return (String)this
            .getConfiguredProperty(MetafacadeProperties.DEFAULT_IDENTIFIER_TYPE);
    }

    /**
     * Gets the default identifier visibility.
     */
    private String getDefaultIdentifierVisibility()
    {
        return (String)this
            .getConfiguredProperty(MetafacadeProperties.DEFAULT_IDENTIFIER_VISIBILITY);
    }
}