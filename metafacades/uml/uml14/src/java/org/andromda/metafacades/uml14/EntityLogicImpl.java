package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EntityQueryOperation;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;


/**
 * Metaclass facade implementation.
 */
public class EntityLogicImpl
    extends EntityLogic
{
    public EntityLogicImpl(
        final java.lang.Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * A collection of MOF ids for entities that have dynamic
     * identifiers present.
     */
    private static final Collection dynamicIdentifiersPresent = new ArrayList();

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    public void initialize()
    {
        super.initialize();

        // if there are no identifiers on this entity, create and add one.
        // enumeration don't have identifiers since they are not entities
        if (!this.isIdentifiersPresent() && this.isAllowDefaultIdentifiers())
        {
            this.createIdentifier();
            dynamicIdentifiersPresent.add(this.getId());
        }
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENTITY_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getQueryOperations()
     */
    protected java.util.Collection handleGetQueryOperations()
    {
        return this.getQueryOperations(false);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getQueryOperations(boolean)
     */
    protected java.util.Collection handleGetQueryOperations(final boolean follow)
    {
        final Collection queryOperations = this.getOperations();

        MetafacadeUtils.filterByType(queryOperations, EntityQueryOperation.class);
        for (
            ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            if (Entity.class.isAssignableFrom(superClass.getClass()))
            {
                Entity entity = (Entity)superClass;
                queryOperations.addAll(entity.getQueryOperations());
            }
        }
        return queryOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers()
     */
    protected java.util.Collection handleGetIdentifiers()
    {
        return this.getIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers(boolean)
     */
    protected java.util.Collection handleGetIdentifiers(final boolean follow)
    {
        return EntityMetafacadeUtils.getIdentifiers(this, follow);
    }

    /**
     * Creates an identifier from the default identifier properties specified within a namespace.
     */
    private final void createIdentifier()
    {
        // first check if the foreign identifier flag is set, and
        // let those taken precedence if so
        if (!this.checkForAndAddForeignIdentifiers())
        {
            this.createIdentifier(
                this.getDefaultIdentifier(),
                this.getDefaultIdentifierType(),
                this.getDefaultIdentifierVisibility());
        }
    }

    /**
     * Creates a new identifier and adds it to the underlying meta model
     * classifier instance.
     *
     * @param name the name to give the identifier
     * @param type the type to give the identifier
     * @param visibility the visibility to give the identifier
     */
    private final void createIdentifier(
        final String name,
        final String type,
        final String visibility)
    {
        // only create the identifier if an identifer with the name doesn't
        // already exist
        if (!UML14MetafacadeUtils.attributeExists(this.metaObject, name))
        {
            final Attribute identifier =
                UML14MetafacadeUtils.createAttribute(
                    name, type, visibility, MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);

            identifier.getStereotype().add(
                UML14MetafacadeUtils.findOrCreateStereotype(UMLProfile.STEREOTYPE_IDENTIFIER));

            ((Classifier)this.metaObject).getFeature().add(identifier);
        }
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isIdentifiersPresent()
     */
    protected boolean handleIsIdentifiersPresent()
    {
        final Collection identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isDynamicIdentifiersPresent()
     */
    protected boolean handleIsDynamicIdentifiersPresent()
    {
        return dynamicIdentifiersPresent.contains(this.getId());
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getTableName()
     */
    protected String handleGetTableName()
    {
        final String prefixProperty = UMLMetafacadeProperties.TABLE_NAME_PREFIX;
        final String tableNamePrefix =
            this.isConfiguredProperty(prefixProperty)
            ? ObjectUtils.toString(this.getConfiguredProperty(prefixProperty)) : null;
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            tableNamePrefix,
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
            this.getMaxSqlNameLength(),
            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean)
     */
    protected String handleGetOperationCallFromAttributes(final boolean withIdentifiers)
    {
        return this.getOperationCallFromAttributes(withIdentifiers, false);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean, boolean)
     */
    protected String handleGetOperationCallFromAttributes(
        final boolean withIdentifiers,
        final boolean follow)
    {
        final StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");

        final Collection attributes = this.getAttributes();

        for (
            ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            if (superClass instanceof Entity)
            {
                final Entity entity = (Entity)superClass;
                attributes.addAll(entity.getAttributes());
            }
        }

        if (attributes != null && !attributes.isEmpty())
        {
            for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
            {
                final EntityAttribute attribute = (EntityAttribute)iterator.next();
                if (withIdentifiers || !attribute.isIdentifier())
                {
                    buffer.append(separator);
                    if (attribute.getType() != null)
                    {
                        buffer.append(attribute.getType().getFullyQualifiedName());
                    }
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
     * @see org.andromda.metafacades.uml.EntityLogic#getAttributeTypeList(boolean, boolean)
     */
    protected String handleGetAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getAttributes(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean, boolean)
     */
    protected String handleGetAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getAttributes(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeTypeList(boolean, boolean)
     */
    protected String handleGetRequiredAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredAttributes(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeNameList(boolean, boolean)
     */
    protected String handleGetRequiredAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredAttributes(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyTypeList(boolean, boolean)
     */
    protected String handleGetRequiredPropertyTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredProperties(follow, withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyNameList(boolean, boolean)
     */
    protected String handleGetRequiredPropertyNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredProperties(follow, withIdentifiers));
    }

    /**
     * Constructs a comma seperated list of attribute type names from the passed in collection of
     * <code>attributes</code>.
     *
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute types.
     */
    private final String getTypeList(final Collection attributes)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            attributes,
            new Closure()
            {
                public void execute(final Object object)
                {
                    if (object instanceof AttributeFacade)
                    {
                        final AttributeFacade attribute = (AttributeFacade)object;
                        if (attribute.getType() != null)
                        {
                            list.append(attribute.getType().getFullyQualifiedName());
                            list.append(comma);
                        }
                    }
                    if (object instanceof AssociationEndFacade)
                    {
                        final AssociationEndFacade associationEnd = (AssociationEndFacade)object;
                        if (associationEnd.getType() != null)
                        {
                            list.append(associationEnd.getType().getFullyQualifiedName());
                            list.append(comma);
                        }
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(
                list.lastIndexOf(comma),
                list.length());
        }
        return list.toString();
    }

    /**
     * Constructs a comma seperated list of attribute names from the passed in collection of <code>attributes</code>.
     *
     * @param attributes the attributes to construct the list from.
     * @return the comma seperated list of attribute names.
     */
    private String getNameList(final Collection properties)
    {
        final StringBuffer list = new StringBuffer();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            properties,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof EntityAttribute)
                    {
                        list.append(((AttributeFacade)object).getName());
                        list.append(comma);
                    }
                    if (object instanceof EntityAssociationEnd)
                    {
                        list.append(((AssociationEndFacade)object).getName());
                        list.append(comma);
                    }
                }
            });
        if (list.toString().endsWith(comma))
        {
            list.delete(
                list.lastIndexOf(comma),
                list.length());
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isChild()
     */
    protected boolean handleIsChild()
    {
        return CollectionUtils.find(
            this.getAssociationEnds(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd().isComposition();
                }
            }) != null;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getParentEnd()
     */
    protected Object handleGetParentEnd()
    {
        Object parentEnd = null;
        final AssociationEndFacade end =
            (AssociationEndFacade)CollectionUtils.find(
                this.getAssociationEnds(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((AssociationEndFacade)object).getOtherEnd().isComposition();
                    }
                });
        if (end != null)
        {
            parentEnd = end.getOtherEnd();
        }
        return parentEnd;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getChildren()
     */
    protected Collection handleGetChildEnds()
    {
        final Collection childEnds =
            new FilteredCollection(this.getAssociationEnds())
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).isComposition();
                }
            };
        CollectionUtils.transform(
            childEnds,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        return childEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getBusinessOperations()
     */
    protected Collection handleGetBusinessOperations()
    {
        final Collection businessOperations = this.getOperations();
        MetafacadeUtils.filterByNotType(businessOperations, EntityQueryOperation.class);
        return businessOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getEntityReferences()
     */
    protected Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement != null && Entity.class.isAssignableFrom(targetElement.getClass());
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean, boolean)
     */
    protected Collection handleGetAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = true;
                    if (!withIdentifiers && EntityAttribute.class.isAssignableFrom(object.getClass()))
                    {
                        valid = !((EntityAttribute)object).isIdentifier();
                    }
                    return valid;
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getProperties(boolean, boolean)
     */
    protected Collection handleGetProperties(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection properties = this.getProperties(follow);
        CollectionUtils.filter(
            properties,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = true;
                    if (!withIdentifiers && object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isIdentifier();
                    }
                    return valid;
                }
            });
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributes(boolean, boolean)
     */
    protected Collection handleGetRequiredAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow, withIdentifiers);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    valid = ((AttributeFacade)object).isRequired();
                    if (valid && !withIdentifiers && object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isIdentifier();
                    }
                    return valid;
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredProperties(boolean, boolean)
     */
    protected Collection handleGetRequiredProperties(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Set properties = new HashSet(this.getProperties());
        if (follow)
        {
            CollectionUtils.forAllDo(
                this.getAllGeneralizations(),
                new Closure()
                {
                    public void execute(final Object object)
                    {
                        properties.addAll(((ClassifierFacade)object).getProperties());
                    }
                });
        }
        CollectionUtils.filter(
            properties,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = false;
                    if (object instanceof AttributeFacade)
                    {
                        valid = ((AttributeFacade)object).isRequired();
                        if (valid && !withIdentifiers && object instanceof EntityAttribute)
                        {
                            valid = !((EntityAttribute)object).isIdentifier();
                        }
                    }
                    else if (object instanceof AssociationEndFacade)
                    {
                        valid = ((AssociationEndFacade)object).isRequired();
                    }
                    return valid;
                }
            });

        List sortedProperties = new ArrayList(properties);
        MetafacadeUtils.sortByFullyQualifiedName(sortedProperties);
        return sortedProperties;
    }

    /**
     * Gets the maximum name length SQL names may be
     */
    protected Short handleGetMaxSqlNameLength()
    {
        return Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * Returns true/false on whether or not default identifiers are allowed
     */
    private boolean isAllowDefaultIdentifiers()
    {
        return Boolean.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.ALLOW_DEFAULT_IDENTITIFIERS))
                      .booleanValue();
    }

    /**
     * Gets the name of the default identifier.
     */
    private String getDefaultIdentifier()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER_PATTERN))
                          .replaceAll(
            "\\{0\\}",
            StringUtilsHelper.lowerCamelCaseName(this.getName()));
    }

    /**
     * Gets the name of the default identifier type.
     */
    private String getDefaultIdentifierType()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER_TYPE);
    }

    /**
     * Gets the default identifier visibility.
     */
    private String getDefaultIdentifierVisibility()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_IDENTIFIER_VISIBILITY);
    }

    /**
     * Checks to see if this entity has any associations where the foreign identifier flag may be set, and if so creates
     * and adds identifiers just like the foreign entity to this entity.
     *
     * @return true if any identifiers were added, false otherwise
     */
    private final boolean checkForAndAddForeignIdentifiers()
    {
        boolean identifiersAdded = false;
        final EntityAssociationEnd end = this.getForeignIdentifierEnd();
        if (end != null && end.getType() instanceof Entity)
        {
            final Entity foreignEntity = (Entity)end.getOtherEnd().getType();
            final Collection identifiers = EntityMetafacadeUtils.getIdentifiers(foreignEntity, true);
            for (final Iterator iterator = identifiers.iterator(); iterator.hasNext();)
            {
                final AttributeFacade identifier = (AttributeFacade)iterator.next();
                this.createIdentifier(
                    identifier.getName(),
                    identifier.getType().getFullyQualifiedName(true),
                    identifier.getVisibility());
                identifiersAdded = true;
            }
        }
        return identifiersAdded;
    }

    /**
     * Override to filter out any association ends that point to model elements other than other entities.
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    public Collection handleGetAssociationEnds()
    {
        final Collection associationEnds = this.shieldedElements(super.handleGetAssociationEnds());
        CollectionUtils.filter(
            associationEnds,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd().getType() instanceof Entity;
                }
            });
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml14.EntityLogic#handleIsUsingForeignIdentifier()
     */
    protected boolean handleIsUsingForeignIdentifier()
    {
        return this.getForeignIdentifierEnd() != null;
    }

    /**
     * Gets the association end that is flagged as having the foreign identifier set (or null if none is).
     */
    private EntityAssociationEnd getForeignIdentifierEnd()
    {
        return (EntityAssociationEnd)CollectionUtils.find(
            this.getAssociationEnds(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    if (object != null && EntityAssociationEnd.class.isAssignableFrom(object.getClass()))
                    {
                        EntityAssociationEnd end = (EntityAssociationEnd)object;
                        valid = end.isForeignIdentifier();
                    }
                    return valid;
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isUsingAssignedIdentifier()
     */
    protected boolean handleIsUsingAssignedIdentifier()
    {
        boolean assigned = false;
        final Collection identifiers = this.getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty())
        {
            final AttributeFacade identifier = (AttributeFacade)identifiers.iterator().next();
            assigned =
                Boolean.valueOf(
                    ObjectUtils.toString(
                        identifier.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ASSIGNED_IDENTIFIER)))
                       .booleanValue();
        }
        return assigned;
    }
}