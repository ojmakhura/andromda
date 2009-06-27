package org.andromda.metafacades.emf.uml2;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeException;
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
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.VisibilityKind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Entity.
 *
 * @see org.andromda.metafacades.uml.Entity
 * @author Bob Fields
 */
public class EntityLogicImpl
    extends EntityLogic
{
    public EntityLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * A collection of MOF ids for entities that have dynamic identifiers
     * present.
     */
    private static final Collection dynamicIdentifiersPresent = new ArrayList();

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    public void initialize()
    {
        super.initialize();

        // if there are no identifiers on this entity, create and add one.
        // enumerations don't have identifiers since they are not entities
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
        final Collection queryOperations = new ArrayList(this.getOperations());

        MetafacadeUtils.filterByType(
            queryOperations,
            EntityQueryOperation.class);
        for (ClassifierFacade superClass = (ClassifierFacade)this.getGeneralization(); superClass != null && follow;
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
        return EntityMetafacadeUtils.getIdentifiers(
            this,
            follow);
    }

    /**
     * Creates an identifier from the default identifier properties specified
     * within a namespace.
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
     * @param name
     *            the name to give the identifier
     * @param type
     *            the type to give the identifier
     * @param visibility
     *            the visibility to give the identifier
     */
    private void createIdentifier(
        final String name,
        final String type,
        final String visibility)
    {
        final org.eclipse.uml2.Class umlClass = (org.eclipse.uml2.Class)this.metaObject;

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
        if (!umlClass.getGeneralizations().isEmpty()) return;

        if (umlClass.getAttribute(name) == null)
        {
            // ((org.eclipse.uml2.Classifier)metaObject).getModel();
            final Object modelElement =
                UmlUtilities.findByFullyQualifiedName(
                    umlClass.eResource().getResourceSet(),
                    type,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
            if (modelElement instanceof Type)
            {
                Type element = (Type)modelElement;
                final Property property = umlClass.createOwnedAttribute(
                        name,
                        element,
                        1,
                        1);
                VisibilityKind kind = VisibilityKind.PUBLIC_LITERAL;
                if (visibility.equalsIgnoreCase("package"))
                {
                    kind = VisibilityKind.PACKAGE_LITERAL;
                }
                if (visibility.equalsIgnoreCase("private"))
                {
                    kind = VisibilityKind.PRIVATE_LITERAL;
                }
                if (visibility.equalsIgnoreCase("protected"))
                {
                    kind = VisibilityKind.PROTECTED_LITERAL;
                }
                property.setVisibility(kind);
                Stereotype stereotype =
                    UmlUtilities.findApplicableStereotype(
                        property,
                        UMLProfile.STEREOTYPE_IDENTIFIER);
                if (stereotype == null)
                {
                    throw new MetafacadeException("Could not apply '" + UMLProfile.STEREOTYPE_IDENTIFIER + "' to " +
                        property + ", the stereotype could not be found");
                }
                property.apply(stereotype);
            }
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
        return this.getOperationCallFromAttributes(
            withIdentifiers,
            false);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean,
     *      boolean)
     */
    protected String handleGetOperationCallFromAttributes(
        final boolean withIdentifiers,
        final boolean follow)
    {
        final StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");

        final Set attributes = new LinkedHashSet(this.getAttributes());

        for (ClassifierFacade superClass = (ClassifierFacade)this.getGeneralization(); superClass != null && follow;
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
     * @see org.andromda.metafacades.uml.Entity#getAttributeTypeList(boolean,
     *      boolean)
     */
    protected String handleGetAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean,
     *      boolean)
     */
    protected String handleGetAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeTypeList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredAttributeTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeNameList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredAttributes(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyTypeList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredPropertyTypeList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getTypeList(this.getRequiredProperties(
                follow,
                withIdentifiers));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyNameList(boolean,
     *      boolean)
     */
    protected String handleGetRequiredPropertyNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getNameList(this.getRequiredProperties(
                follow,
                withIdentifiers));
    }

    /**
     * Constructs a comma separated list of attribute type names from the passed
     * in collection of <code>attributes</code>.
     *
     * @param attributes
     *            the attributes to construct the list from.
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
     * Constructs a comma separated list of attribute names from the passed in
     * collection of <code>attributes</code>.
     *
     * @param properties
     *            the attributes to construct the list from.
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
                public void execute(final Object object)
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
                public boolean evaluate(final Object object)
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
     * @see org.andromda.metafacades.uml.Entity#getChildEnds()
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
                public Object transform(final Object object)
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
        final Collection businessOperations = new ArrayList(this.getImplementationOperations());
        MetafacadeUtils.filterByNotType(
            businessOperations,
            EntityQueryOperation.class);
        return businessOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getEntityReferences()
     */
    protected Collection handleGetEntityReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(final Object object)
                {
                    ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement instanceof Entity;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean, boolean)
     */
    protected Collection handleGetAttributes(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
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
    protected Collection handleGetProperties(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection properties = this.getProperties(follow);

        // only filter when we don't want identifiers
        if (!withIdentifiers)
        {
            CollectionUtils.filter(
                properties,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        return !(object instanceof EntityAttribute) || !((EntityAttribute)object).isIdentifier();
                    }
                });
        }

        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributes(boolean,
     *      boolean)
     */
    protected Collection handleGetRequiredAttributes(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(
                follow,
                withIdentifiers);

        // only filter when we don't want identifiers
        if (!withIdentifiers)
        {
            CollectionUtils.filter(
                attributes,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        final AttributeFacade attribute = (AttributeFacade)object;
                        return
                            attribute.isRequired() &&
                            (!(object instanceof EntityAttribute) || !((EntityAttribute)object).isIdentifier());
                    }
                });
        }

        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredProperties(boolean,
     *      boolean)
     */
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
     * Checks to see if this entity has any associations where the foreign
     * identifier flag may be set, and if so creates and adds identifiers just
     * like the foreign entity to this entity.
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
            final Collection identifiers = EntityMetafacadeUtils.getIdentifiers(
                    foreignEntity,
                    true);
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
     * Override to filter out any association ends that point to model elements
     * other than other entities.
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    public List handleGetAssociationEnds()
    {
        final List associationEnds = (List)this.shieldedElements(super.handleGetAssociationEnds());
        CollectionUtils.filter(
            associationEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd().getType() instanceof Entity;
                }
            });
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.EntityLogic#handleIsUsingForeignIdentifier()
     */
    protected boolean handleIsUsingForeignIdentifier()
    {
        return this.getForeignIdentifierEnd() != null;
    }

    /**
     * Gets the association end that is flagged as having the foreign identifier
     * set (or null if none is).
     */
    private EntityAssociationEnd getForeignIdentifierEnd()
    {
        return (EntityAssociationEnd)CollectionUtils.find(
            this.getAssociationEnds(),
            new Predicate()
            {
                public boolean evaluate(final Object object)
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
            final Object id = identifiers.iterator().next();
            AttributeFacade identifier = (AttributeFacade)id;
            assigned =
                Boolean.valueOf(
                    ObjectUtils.toString(
                        identifier.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ASSIGNED_IDENTIFIER)))
                       .booleanValue();
        }
        return assigned;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getSchema()
     */
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
    protected Collection handleGetIdentifierAssociationEnds() {
        Collection associationEnds = new ArrayList(this.getAssociationEnds());
        if (associationEnds != null)
        {
            MetafacadeUtils.filterByStereotype(
                associationEnds,
                UMLProfile.STEREOTYPE_IDENTIFIER);
        }
        return associationEnds;
    }
    
    /**
     * @see org.andromda.metafacades.uml.Entity#isCompositeIdentifier()
     */
    protected boolean handleIsCompositeIdentifier() {
        int identifiers = (!this.getIdentifiers().isEmpty()) ? this.getIdentifiers().size() : 0;
        identifiers =
            identifiers +
            (!this.getIdentifierAssociationEnds().isEmpty() ? this.getIdentifierAssociationEnds().size() : 0);
        return identifiers >= 2;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityLogic#getAllEntityReferences()
     */
    protected Collection handleGetAllEntityReferences()
    {
        final Collection result = new LinkedHashSet();

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
     * @see org.andromda.metafacades.uml.EntityLogic#getEmbeddedValues()
     */
    protected Collection handleGetEmbeddedValues()
    {
        final Collection embeddedValues = new ArrayList();
        for (final Iterator iterator = this.getAttributes(true).iterator(); iterator.hasNext();)
        {
            final AttributeFacade attribute = (AttributeFacade)iterator.next();
            final ClassifierFacade type = attribute.getType();
            if (type != null && type.isEmbeddedValue())
            {
                embeddedValues.add(attribute);
            }
        }
        return embeddedValues;
    }
}