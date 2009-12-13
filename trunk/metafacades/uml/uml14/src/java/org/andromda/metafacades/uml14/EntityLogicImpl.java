package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;


/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class EntityLogicImpl
    extends EntityLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EntityLogicImpl(
        final Object metaObject,
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
    @Override
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
    @Override
    protected Collection<OperationFacade> handleGetQueryOperations()
    {
        return this.getQueryOperations(false);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getQueryOperations(boolean)
     */
    @Override
    protected Collection<OperationFacade> handleGetQueryOperations(final boolean follow)
    {
        final Collection<OperationFacade> queryOperations = new ArrayList(this.getOperations());

        MetafacadeUtils.filterByType(
            queryOperations,
            EntityQueryOperation.class);
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
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
    @Override
    protected Collection<EntityAttribute> handleGetIdentifiers()
    {
        return this.getIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers(boolean)
     */
    @Override
    protected Collection<AttributeFacade> handleGetIdentifiers(final boolean follow)
    {
        return EntityMetafacadeUtils.getIdentifiers(
            this,
            follow);
    }

    /**
     * Creates an identifier from the default identifier properties specified within a namespace.
     */
    private void createIdentifier()
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
    private void createIdentifier(
        final String name,
        final String type,
        final String visibility)
    {
        final Classifier classifier = (Classifier)this.metaObject;

        // if we auto-create entity identifiers it will only be on hierarchy roots,
        // problems would arise when calls to #checkForAndAddForeignIdentifiers()
        // navigate over associated entities, effectively initializing their facade instances:
        // this results in subclasses having an identifier generated before their ancestors
        // ideally the method mentioned above would not make use of facades but meta-classes only,
        // if one is to refactor it that way this comment may be removed together with the line of code under it
        //
        // notice how the next line of code does not make use of facades, this is done on purpose in order
        // to avoid using uninitialized facades
        //
        // (Wouter, Sept. 20 2006) also see other UML implementations
        if (!classifier.getGeneralization().isEmpty()) return;

        // only create the identifier if an identifer with the name doesn't
        // already exist
        if (!UML14MetafacadeUtils.attributeExists(
                classifier,
                name))
        {
            final Attribute identifier =
                UML14MetafacadeUtils.createAttribute(
                    name,
                    type,
                    visibility,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);

            identifier.getStereotype().add(
                UML14MetafacadeUtils.findOrCreateStereotype(UMLProfile.STEREOTYPE_IDENTIFIER));

            classifier.getFeature().add(identifier);
        }
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isIdentifiersPresent()
     */
    @Override
    protected boolean handleIsIdentifiersPresent()
    {
        final Collection<EntityAttribute> identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isDynamicIdentifiersPresent()
     */
    @Override
    protected boolean handleIsDynamicIdentifiersPresent()
    {
        return dynamicIdentifiersPresent.contains(this.getId());
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getTableName()
     */
    @Override
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
    @Override
    protected String handleGetOperationCallFromAttributes(final boolean withIdentifiers)
    {
        return this.getOperationCallFromAttributes(
            withIdentifiers,
            false);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean, boolean)
     */
    @Override
    protected String handleGetOperationCallFromAttributes(
        final boolean withIdentifiers,
        final boolean follow)
    {
        final StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append('(');

        final Collection<AttributeFacade> attributes = new ArrayList(this.getAttributes());

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            if (superClass instanceof Entity)
            {
                final Entity entity = (Entity)superClass;
                attributes.addAll(entity.getAttributes());
            }
        }

        if (!attributes.isEmpty())
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
                    buffer.append(' ');
                    buffer.append(attribute.getName());
                    separator = ", ";
                }
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeTypeList(boolean, boolean)
     */
    @Override
    protected String handleGetAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean, boolean)
     */
    @Override
    protected String handleGetAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeTypeList(boolean, boolean)
     */
    @Override
    protected String handleGetRequiredAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeNameList(boolean, boolean)
     */
    @Override
    protected String handleGetRequiredAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyTypeList(boolean, boolean)
     */
    @Override
    protected String handleGetRequiredPropertyTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredProperties(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyNameList(boolean, boolean)
     */
    @Override
    protected String handleGetRequiredPropertyNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredProperties(
                follow,
                withIdentifiers));
    }

    /**
     * Constructs a comma separated list of attribute type names from the passed in collection of
     * <code>attributes</code>.
     *
     * @param attributes the attributes to construct the list from.
     * @return the comma separated list of attribute types.
     */
    private String getTypeList(final Collection attributes)
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
     * Constructs a comma separated list of attribute names from the passed in collection of <code>attributes</code>.
     *
     * @param properties the properties to construct the list from.
     * @return the comma separated list of attribute names.
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
    @Override
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
    @Override
    protected AssociationEndFacade handleGetParentEnd()
    {
        AssociationEndFacade parentEnd = null;
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
     * @see org.andromda.metafacades.uml.Entity#getChildEnds()
     */
    @Override
    protected Collection<AssociationEndFacade> handleGetChildEnds()
    {
        final Collection<AssociationEndFacade> childEnds =
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
    @Override
    protected Collection<OperationFacade> handleGetBusinessOperations()
    {
        final Collection<OperationFacade> businessOperations = new ArrayList(this.getImplementationOperations());
        MetafacadeUtils.filterByNotType(
            businessOperations,
            EntityQueryOperation.class);
        return businessOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getEntityReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement instanceof Entity;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean, boolean)
     */
    @Override
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
                    if (!withIdentifiers && object instanceof EntityAttribute)
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
    @Override
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
    @Override
    protected Collection handleGetRequiredAttributes(
        boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(
                follow,
                withIdentifiers);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid;
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
    @Override
    protected Collection handleGetRequiredProperties(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Set properties = new LinkedHashSet(this.getProperties(
                    follow,
                    withIdentifiers));
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
    @Override
    protected Short handleGetMaxSqlNameLength()
    {
        return Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
    }

    /**
     * Returns true/false on whether or not default identifiers are allowed
     */
    private boolean isAllowDefaultIdentifiers()
    {
        return Boolean.valueOf((String) this.getConfiguredProperty(UMLMetafacadeProperties.ALLOW_DEFAULT_IDENTITIFIERS));
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
    private boolean checkForAndAddForeignIdentifiers()
    {
        boolean identifiersAdded = false;
        final EntityAssociationEnd end = this.getForeignIdentifierEnd();
        if (end != null && end.getType() instanceof Entity)
        {
            final Entity foreignEntity = (Entity)end.getOtherEnd().getType();
            final Collection<AttributeFacade> identifiers = EntityMetafacadeUtils.getIdentifiers(
                    foreignEntity,
                    true);
            for (final Iterator<AttributeFacade> iterator = identifiers.iterator(); iterator.hasNext();)
            {
                final AttributeFacade identifier = iterator.next();
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
    @Override
    public List handleGetAssociationEnds()
    {
        final List associationEnds = (List)this.shieldedElements(super.handleGetAssociationEnds());
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
    @Override
    protected boolean handleIsUsingAssignedIdentifier()
    {
        boolean assigned = false;
        final Collection<EntityAttribute> identifiers = this.getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty())
        {
            final AttributeFacade identifier = (AttributeFacade)identifiers.iterator().next();
            assigned =
                    Boolean.valueOf(
                            ObjectUtils.toString(
                                    identifier.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ASSIGNED_IDENTIFIER)));
        }
        return assigned;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getSchema()
     */
    @Override
    protected String handleGetSchema()
    {
        String schemaName = ObjectUtils.toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_SCHEMA));
        if (StringUtils.isBlank(schemaName))
        {
            schemaName = ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.SCHEMA_NAME));
        }
        return schemaName;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifierAssociationEnds()
     */
    @Override
    protected Collection handleGetIdentifierAssociationEnds()
    {
        final Collection<AssociationEndFacade> associationEnds = new ArrayList<AssociationEndFacade>(this.getAssociationEnds());
        MetafacadeUtils.filterByStereotype(
            associationEnds,
            UMLProfile.STEREOTYPE_IDENTIFIER);
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isCompositeIdentifier()
     */
    @Override
    protected boolean handleIsCompositeIdentifier()
    {
        int identifiers = (!this.getIdentifiers().isEmpty()) ? this.getIdentifiers().size() : 0;
        identifiers =
            identifiers +
            (!this.getIdentifierAssociationEnds().isEmpty() ? this.getIdentifierAssociationEnds().size() : 0);
        return identifiers >= 2;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAllEntityReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetAllEntityReferences()
    {
        final Collection<DependencyFacade> result = new LinkedHashSet<DependencyFacade> ();

        // get references of the service itself
        result.addAll(this.getEntityReferences());

        // get references of all super classes
        CollectionUtils.forAllDo(this.getAllGeneralizations(), new Closure()
        {
            public void execute(Object object)
            {
                if (object instanceof Entity)
                {
                    final Entity entity = (Entity)object;
                    result.addAll(entity.getEntityReferences());
                }
            }

        });
        return result;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getEmbeddedValues()
     */
    @Override
    protected Collection<AttributeFacade> handleGetEmbeddedValues()
    {
        final Collection<AttributeFacade> embeddedValues = new ArrayList<AttributeFacade>();
        for (final Iterator<AttributeFacade> iterator = this.getAttributes(true).iterator(); iterator.hasNext();)
        {
            final AttributeFacade attribute = iterator.next();
            final ClassifierFacade type = attribute.getType();
            if (type != null && type.isEmbeddedValue())
            {
                embeddedValues.add(attribute);
            }
        }
        return embeddedValues;
    }
}