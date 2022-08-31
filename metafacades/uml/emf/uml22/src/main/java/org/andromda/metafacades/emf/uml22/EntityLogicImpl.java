package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.andromda.metafacades.uml.EnumerationFacade;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Entity.
 *
 * @see org.andromda.metafacades.uml.Entity
 * @author Bob Fields
 */
public class EntityLogicImpl
    extends EntityLogic
{
    private static final long serialVersionUID = 9022811369852920861L;

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
     * A collection of MOF ids for entities that have dynamic identifiers
     * present.
     */
    private static final Collection<String> DYNAMIC_IDENTIFIERS_PRESENT
        = Collections.synchronizedList(new ArrayList<String>());

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        super.initialize();

        // if there are no identifiers on this entity, create and add one.
        // enumerations don't have identifiers since they are not entities
        if (!this.isIdentifiersPresent() && this.isAllowDefaultIdentifiers())
        {
            this.createIdentifier();
            DYNAMIC_IDENTIFIERS_PRESENT.add(this.getId());
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
        final Collection<OperationFacade> operations = new ArrayList<OperationFacade>(this.getOperations());
        final Collection<OperationFacade> queryOperations = new ArrayList<OperationFacade>();

        MetafacadeUtils.filterByType(
            operations,
            EntityQueryOperation.class);
        for (OperationFacade operation : operations)
        {
            queryOperations.add((EntityQueryOperation)operation);
        }
        for (ClassifierFacade superClass = (ClassifierFacade)this.getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            if (Entity.class.isAssignableFrom(superClass.getClass()))
            {
                final Entity entity = (Entity)superClass;
                queryOperations.addAll(entity.getQueryOperations());
            }
        }
        return queryOperations;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers()
     */
    @Override
    protected Collection<ModelElementFacade> handleGetIdentifiers()
    {
        return this.handleGetIdentifiers(true);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifiers(boolean)
     */
    @Override
    protected Collection<ModelElementFacade> handleGetIdentifiers(final boolean follow)
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
     * @param name the name to give the identifier
     * @param type the type to give the identifier
     * @param visibility the visibility to give the identifier
     */
    private void createIdentifier(
        final String name,
        final String type,
        final String visibility)
    {
        final Class umlClass = (Class)this.metaObject;

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
        if (!umlClass.getGeneralizations().isEmpty()) {return;}

        if (umlClass.getAttribute(name, umlClass) == null)
        {
            // ((org.eclipse.uml2.uml.Classifier)metaObject).getModel();
            final Object modelElement =
                UmlUtilities.findByFullyQualifiedName(
                    umlClass.eResource().getResourceSet(),
                    type,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
            if (modelElement instanceof Type)
            {
                final Type element = (Type)modelElement;
                // Identifiers will always have lowerBound 0, since they are optional when creating the Entity class but still valid.
                // Creates a property with the specified name, type, lower bound, and upper bound
                final Property property = umlClass.createOwnedAttribute(
                        name,
                        element,
                        0,
                        1);
                VisibilityKind kind = VisibilityKind.PUBLIC_LITERAL;
                if ("package".equalsIgnoreCase(visibility))
                {
                    kind = VisibilityKind.PACKAGE_LITERAL;
                }
                if ("private".equalsIgnoreCase(visibility))
                {
                    kind = VisibilityKind.PRIVATE_LITERAL;
                }
                if ("protected".equalsIgnoreCase(visibility))
                {
                    kind = VisibilityKind.PROTECTED_LITERAL;
                }
                property.setVisibility(kind);
                final Stereotype stereotype =
                    UmlUtilities.findApplicableStereotype(
                        property,
                        UMLProfile.STEREOTYPE_IDENTIFIER);
                if (stereotype == null)
                {
                    throw new MetafacadeException("Could not apply '" + UMLProfile.STEREOTYPE_IDENTIFIER + "' to " +
                        property + ", the stereotype could not be found");
                }
                property.applyStereotype(stereotype);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isIdentifiersPresent()
     */
    @Override
    protected boolean handleIsIdentifiersPresent()
    {
        final Collection<ModelElementFacade> identifiers = this.getIdentifiers(true);
        return identifiers != null && !identifiers.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#isDynamicIdentifiersPresent()
     */
    @Override
    protected boolean handleIsDynamicIdentifiersPresent()
    {
        return DYNAMIC_IDENTIFIERS_PRESENT.contains(this.getId());
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
            Short.valueOf(this.getMaxSqlNameLength()),
            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
            this.getConfiguredProperty(UMLMetafacadeProperties.SHORTEN_SQL_NAMES_METHOD));
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
     * @see org.andromda.metafacades.uml.Entity#getOperationCallFromAttributes(boolean,
     *      boolean)
     */
    @Override
    protected String handleGetOperationCallFromAttributes(
        final boolean withIdentifiers,
        final boolean follow)
    {
        final StringBuilder buffer = new StringBuilder("(");

        final Set<AttributeFacade> attributes = new LinkedHashSet<AttributeFacade>(this.getAttributes());

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
            String separator = "";
            for (AttributeFacade attribute : attributes)
            {
                final EntityAttribute entityAttribute = (EntityAttribute)attribute;
                if (withIdentifiers || !entityAttribute.isIdentifier())
                {
                    buffer.append(separator);
                    if (entityAttribute.getType() != null)
                    {
                        buffer.append(entityAttribute.getType().getFullyQualifiedName());
                    }
                    buffer.append(' ');
                    buffer.append(entityAttribute.getName());
                    separator = ", ";
                }
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeTypeList(boolean,
     *      boolean)
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
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean,
     *      boolean)
     */
    @Override
    protected String handleGetAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers)
    {
        return this.getAttributeNameList(follow, withIdentifiers, true);
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributeNameList(boolean,
     *      boolean, boolean)
     */
    @Override
    protected String handleGetAttributeNameList(
        final boolean follow,
        final boolean withIdentifiers,
        final boolean withDerived)
    {
        return this.getNameList(this.getAttributes(
                follow,
                withIdentifiers,
                withDerived));
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeTypeList(boolean,
     *      boolean)
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
     * @see org.andromda.metafacades.uml.Entity#getRequiredAttributeNameList(boolean,
     *      boolean)
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
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyTypeList(boolean,
     *      boolean)
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
     * @see org.andromda.metafacades.uml.Entity#getRequiredPropertyNameList(boolean,
     *      boolean)
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
     * Constructs a comma separated list of attribute type names from the passed
     * in collection of <code>attributes</code>.
     *
     * @param properties
     *            the attributes and associationEnds to construct the list from.
     * @return the comma separated list of attribute types.
     */
    private String getTypeList(final Collection<? extends ModelElementFacade> properties)
    {
        final StringBuilder list = new StringBuilder();
        final String comma = ", ";
        CollectionUtils.forAllDo(
            properties,
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
     *            the attributes and associationEnds to construct the list from.
     * @return the comma separated list of attribute names.
     */
    private String getNameList(final Collection properties)
    {
        final StringBuilder list = new StringBuilder();
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
    @Override
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
    @Override
    protected AssociationEndFacade handleGetParentEnd()
    {
        AssociationEndFacade parentEnd = null;
        final AssociationEndFacade end =
            (AssociationEndFacade)CollectionUtils.find(
                this.getAssociationEnds(),
                new Predicate()
                {
                    public boolean evaluate(final Object object)
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
                private static final long serialVersionUID = -7200489183737785955L;

                @Override
                public boolean evaluate(final Object object)
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
    @Override
    protected Collection<OperationFacade> handleGetBusinessOperations()
    {
        final Collection<OperationFacade> businessOperations = new ArrayList<OperationFacade>(this.getImplementationOperations());
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
            private static final long serialVersionUID = -302184888689870478L;

            @Override
                public boolean evaluate(final Object object)
                {
                final ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                    return targetElement instanceof Entity;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean, boolean)
     */
    @Override
    protected Collection<AttributeFacade> handleGetAttributes(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection<AttributeFacade> attributes = this.getAttributes(follow);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = true;
                    if (!withIdentifiers && object != null && object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isIdentifier();
                    }
                    return valid;
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAttributes(boolean, boolean, boolean)
     */
    @Override
    protected Collection<AttributeFacade> handleGetAttributes(
        final boolean follow,
        final boolean withIdentifiers,
        final boolean withDerived)
    {
        final Collection<AttributeFacade> attributes = this.getAttributes(follow);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = true;
                    if (!withIdentifiers && object != null && object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isIdentifier();
                    }
                    if (valid && !withDerived && object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isDerived();
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
    protected Collection<ModelElementFacade> handleGetProperties(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection<ModelElementFacade> properties = this.getProperties(follow);
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
    @Override
    protected Collection<AttributeFacade> handleGetRequiredAttributes(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Collection<AttributeFacade> attributes = this.getAttributes(
                follow,
                withIdentifiers,
                false);

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
    @Override
    protected Collection<ModelElementFacade> handleGetRequiredProperties(
        final boolean follow,
        final boolean withIdentifiers)
    {
        final Set<ModelElementFacade> properties = new LinkedHashSet<ModelElementFacade>(this.getProperties(
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
                        final AttributeFacade attribute = (AttributeFacade)object;
                        valid = attribute.isRequired() && !attribute.isDerived();
                        if (valid && !withIdentifiers && object instanceof EntityAttribute)
                        {
                            valid = !((EntityAttribute)object).isIdentifier();
                        }
                    }
                    else if (object instanceof AssociationEndFacade)
                    {
                        final AssociationEndFacade assocationEnd = (AssociationEndFacade)object;
                        valid = assocationEnd.isRequired() && !assocationEnd.isDerived();
                    }
                    return valid;
                }
            });

        final List<ModelElementFacade> sortedProperties = new ArrayList<ModelElementFacade>(properties);
        MetafacadeUtils.sortByFullyQualifiedName(sortedProperties);
        return sortedProperties;
    }

    /**
     * Gets the maximum name length SQL names may be
     * @return UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH
     */
    @Override
    protected short handleGetMaxSqlNameLength()
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
            StringUtilsHelper.lowerCamelCaseName(this.handleGetName()));
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
            final Collection<ModelElementFacade> identifiers = EntityMetafacadeUtils.getIdentifiers(
                    foreignEntity,
                    true);
            for (ModelElementFacade identifier : identifiers)
            {
                if (identifier instanceof EntityAttribute)
                {
                    this.createIdentifier(
                            identifier.getName(),
                            ((EntityAttribute)identifier).getType().getFullyQualifiedName(true),
                            identifier.getVisibility());
                }
                else if (identifier instanceof EntityAssociationEnd)
                {
                    this.createIdentifier(
                            identifier.getName(),
                            ((EntityAssociationEnd)identifier).getType().getFullyQualifiedName(true),
                            identifier.getVisibility());
                }
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
    @Override
    public List<AssociationEndFacade> handleGetAssociationEnds()
    {
        final List<AssociationEndFacade> associationEnds = this.shieldedElements(super.handleGetAssociationEnds());
        CollectionUtils.filter(
            associationEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final ClassifierFacade type = ((AssociationEndFacade)object).getOtherEnd().getType();
                    /*if (!(type instanceof Entity || type instanceof EnumerationFacade))
                    {
                        logger.debug("EntityLogic.getAssociationEnds " + type);
                    }*/
                    return type != null && (type instanceof Entity || type instanceof EnumerationFacade
                        || type.hasStereotype(UMLProfile.STEREOTYPE_EMBEDDED_VALUE));
                }
            });
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.EntityLogic#handleIsUsingForeignIdentifier()
     */
    @Override
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
                        final EntityAssociationEnd end = (EntityAssociationEnd)object;
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
        final Collection<ModelElementFacade> identifiers = this.getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty())
        {
            final ModelElementFacade identifier = identifiers.iterator().next();
            // TODO Does not use the value of taggedValue Entity andromda_hibernate_generator_class (13 - assigned)
            assigned =
                    Boolean.valueOf(
                        ObjectUtils.toString(
                        identifier.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ASSIGNED_IDENTIFIER)))
                       .booleanValue();
            // Association relationships (FK part of PK related to PK in another table) are always assigned.
            if (!assigned && identifier instanceof AssociationEndFacade)
            {
                assigned = true;
            }
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
    protected Collection<AssociationEndFacade> handleGetIdentifierAssociationEnds()
    {
        final Collection<AssociationEndFacade> associationEnds = new ArrayList(this.getAssociationEnds());
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
        int identifiers = this.getIdentifiers().isEmpty() ? 0 : this.getIdentifiers().size();
        /*identifiers +=
            this.getIdentifierAssociationEnds().isEmpty() ? 0 : this.getIdentifierAssociationEnds().size();*/
        return identifiers >= 2;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getAllEntityReferences()
     */
    @Override
    protected Collection<DependencyFacade> handleGetAllEntityReferences()
    {
        final Collection<DependencyFacade> result = new LinkedHashSet<DependencyFacade>();

        // get references of the service itself
        result.addAll(this.getEntityReferences());

        // get references of all super classes
        CollectionUtils.forAllDo(this.getAllGeneralizations(), new Closure()
        {
            public void execute(final Object object)
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
        for (final AttributeFacade attribute : (Iterable<AttributeFacade>) this.getAttributes(true))
        {
            final ClassifierFacade type = attribute.getType();
            if (type != null && type.isEmbeddedValue())
            {
                embeddedValues.add(attribute);
            }
        }
        return embeddedValues;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getFullyQualifiedIdentifierTypeName()
     */
    @Override
    public String handleGetFullyQualifiedIdentifierTypeName()
    {
        String rtn = null;
        if(isCompositeIdentifier())
        {
            rtn = getFullyQualifiedName()+(String)this.getConfiguredProperty(
                UMLMetafacadeProperties.COMPOSITE_IDENTIFIER_TYPE_NAME_SUFIX);
        }
        else
        {
            final Collection<ModelElementFacade> identifiers = getIdentifiers();
            if (identifiers != null && !identifiers.isEmpty())
            {
                final ModelElementFacade facade = identifiers.iterator().next();
                if (facade instanceof EntityAttribute)
                {
                    rtn = ((EntityAttribute)facade).getType().getFullyQualifiedName();
                }
                else if (facade instanceof EntityAssociationEnd)
                {
                    rtn = ((EntityAssociationEnd)facade).getType().getFullyQualifiedName();
                }
                else
                {
                    rtn = "";
                }
            }
            else
            {
                rtn = "";
            }
        }
        return rtn;
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifierName()
     */
    @Override
    public String handleGetIdentifierName()
    {
        if(isCompositeIdentifier())
        {
            return StringUtils.uncapitalize(getName()) + (String)this.getConfiguredProperty(
                UMLMetafacadeProperties.COMPOSITE_IDENTIFIER_NAME_SUFIX);
        }
        else
        {
            final Collection<ModelElementFacade> identifiers = getIdentifiers();
            if (identifiers != null && !identifiers.isEmpty())
            {
                return getIdentifiers().iterator().next().getName();
            }
            else
            {
                return "";
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifierTypeName()
     */
    @Override
    public String handleGetIdentifierTypeName()
    {
        if(isCompositeIdentifier())
        {
            return getName()+(String)this.getConfiguredProperty(UMLMetafacadeProperties.COMPOSITE_IDENTIFIER_TYPE_NAME_SUFIX);
        }
        final Collection<ModelElementFacade> identifiers = getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty())
        {
            ModelElementFacade facade = identifiers.iterator().next();
            if (facade instanceof EntityAttribute)
            {
                return ((EntityAttribute)facade).getType().getName();
            }
            else if (facade instanceof EntityAssociationEnd)
            {
                return ((EntityAssociationEnd)facade).getType().getName();
            }
            else
            {
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifierGetterName()
     */
    @Override
    public String handleGetIdentifierGetterName()
    {
        return "get"+StringUtils.capitalize(getIdentifierName());
    }

    /**
     * @see org.andromda.metafacades.uml.Entity#getIdentifierSetterName()
     */
    @Override
    public String handleGetIdentifierSetterName()
    {
        return "set"+StringUtils.capitalize(getIdentifierName());
    }

    @Override
    protected Map handleGetUniqueConstraints() {

        Map<String, Collection<String>> uniqueConstraints = new HashMap<>();

        for(AttributeFacade attr : this.getAttributes()) {
            if(attr instanceof EntityAttribute ) {
                EntityAttribute attribute = (EntityAttribute)attr;
                if(StringUtilsHelper.isNotBlank(attribute.getUniqueGroup())) {
                    
                    Collection<String> uqs = uniqueConstraints.get(attribute.getUniqueGroup());

                    if(uqs == null) {
                        uqs = new ArrayList<>();
                        uniqueConstraints.put(attribute.getUniqueGroup(), uqs);
                    }

                    uqs.add(attribute.getColumnName());
                }
            }
        }

        for(AssociationEndFacade ae : getAssociationEnds()) {
            if(ae instanceof EntityAssociationEnd) {

                EntityAssociationEnd assEnd = (EntityAssociationEnd)ae.getOtherEnd();
                if(StringUtilsHelper.isNotBlank(assEnd.getUniqueGroup())) {
                    Collection<String> uqs = uniqueConstraints.get(assEnd.getUniqueGroup());

                    if(uqs == null) {
                        uqs = new ArrayList<>();
                        uniqueConstraints.put(assEnd.getUniqueGroup(), uqs);
                    }

                    uqs.add(assEnd.getColumnName());
                }
            }
        }

        return uniqueConstraints.size() > 0 ? uniqueConstraints : null;
    }
}
