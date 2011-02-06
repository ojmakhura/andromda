package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p/>
 * Represents an EJB association end. </p>
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3AssociationEndFacade.
 *
 * @see EJB3AssociationEndFacade
 */
public class EJB3AssociationEndFacadeLogicImpl
    extends EJB3AssociationEndFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * The default composite association cascade property
     */
    private static final String ENTITY_DEFAULT_COMPOSITE_CASCADE = "entityCompositeCascade";

    /**
     * The default aggregation association cascade property
     */
    private static final String ENTITY_DEFAULT_AGGREGATION_CASCADE = "entityAggregationCascade";

    /**
     * The namespace property storing default collection type for associations
     */
    private static final String ASSOCIATION_COLLECTION_TYPE = "associationCollectionType";

    /**
     * A flag indicating whether or not specific (java.util.Set, java.util.List,
     * etc) collection interfaces should be used in assocation mutators and
     * accessors or whether the generic java.util.Collection interface should be
     * used.
     */
    private static final String SPECIFIC_COLLECTION_INTERFACES = "specificCollectionInterfaces";

    /**
     * The property that defines the default collection interface, this is the
     * interface used if the property defined by
     * {@link #SPECIFIC_COLLECTION_INTERFACES} is false.
     */
    private static final String DEFAULT_COLLECTION_INTERFACE = "defaultCollectionInterface";

    /**
     * Stores the default collection index name.
     */
    private static final String COLLECTION_INDEX_NAME = "associationEndCollectionIndexName";

    /**
     * Stores the default collection index type.
     */
    private static final String COLLECTION_INDEX_TYPE = "associationEndCollectionIndexType";

    /**
     * Represents the EJB3 <code>ALL</code> cascade option and fully qualified representation.
     */
    private static final String ENTITY_CASCADE_ALL = "ALL";
    private static final String ENTITY_CASCADE_ALL_FQN = "javax.persistence.CascadeType.ALL";

    /**
     * Represents the EJB3 <code>PERSIST</code> cascade option.
     */
    private static final String ENTITY_CASCADE_PERSIST = "PERSIST";
    private static final String ENTITY_CASCADE_PERSIST_FQN = "javax.persistence.CascadeType.PERSIST";

    /**
     * Represents the EJB3 <code>MERGE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_MERGE = "MERGE";
    private static final String ENTITY_CASCADE_MERGE_FQN = "javax.persistence.CascadeType.MERGE";

    /**
     * Represents the EJB3 <code>REMOVE</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REMOVE = "REMOVE";
    private static final String ENTITY_CASCADE_REMOVE_FQN = "javax.persistence.CascadeType.REMOVE";

    /**
     * Represents the EJB3 <code>REFRESH</code> cascade option.
     */
    private static final String ENTITY_CASCADE_REFRESH = "REFRESH";
    private static final String ENTITY_CASCADE_REFRESH_FQN = "javax.persistence.CascadeType.REFRESH";

    /**
     * Represents the value used to represents NO cascade option.
     */
    private static final String ENTITY_CASCADE_NONE = "NONE";

    /**
     * Stores the cascade map of fully qualified cascade types
     */
    private static final Map<String, String> cascadeTable = new Hashtable<String, String>();

    static
    {
        cascadeTable.put(ENTITY_CASCADE_ALL, ENTITY_CASCADE_ALL_FQN);
        cascadeTable.put(ENTITY_CASCADE_PERSIST, ENTITY_CASCADE_PERSIST_FQN);
        cascadeTable.put(ENTITY_CASCADE_MERGE, ENTITY_CASCADE_MERGE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REMOVE, ENTITY_CASCADE_REMOVE_FQN);
        cascadeTable.put(ENTITY_CASCADE_REFRESH, ENTITY_CASCADE_REFRESH_FQN);
    }

    /**
     * Value for set
     */
    private static final String COLLECTION_TYPE_SET = "set";

    /**
     * Value for map
     */
    private static final String COLLECTION_TYPE_MAP = "map";

    /**
     * Value for list
     */
    private static final String COLLECTION_TYPE_LIST = "list";

    /**
     * Value for collections
     */
    private static final String COLLECTION_TYPE_COLLECTION = "bag";

    /**
     * Stores the valid collection types
     */
    private static final Collection<String> collectionTypes = new ArrayList<String>();

    static
    {
        collectionTypes.add(COLLECTION_TYPE_SET);
        collectionTypes.add(COLLECTION_TYPE_MAP);
        collectionTypes.add(COLLECTION_TYPE_LIST);
        collectionTypes.add(COLLECTION_TYPE_COLLECTION);
    }

    /**
     * Stores the property indicating whether or not composition should define
     * the eager loading strategy and aggregation define lazy loading strategy.
     */
    private static final String COMPOSITION_DEFINES_EAGER_LOADING = "compositionDefinesEagerLoading";

    /**
     * The property that stores whether relationship collection caching is enabled.
     */
    private static final String HIBERNATE_ASSOCIATION_ENABLE_CACHE = "hibernateEnableAssociationsCache";

    /**
     * Stores the default cache strategy for relationship Collections.
     */
    private static final String HIBERNATE_ASSOCIATION_CACHE = "hibernateAssociationCache";

    /**
     * The 'list' type implementation to use.
     */
    private static final String LIST_TYPE_IMPLEMENTATION = "listTypeImplementation";

    /**
     * The 'set' type implementation to use.
     */
    private static final String SET_TYPE_IMPLEMENTATION = "setTypeImplementation";

    /**
     * The 'map' type implementation to use.
     */
    private static final String MAP_TYPE_IMPLEMENTATION = "mapTypeImplementation";

    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJB3AssociationEndFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------


    /**
     * @see AssociationEndFacade#getGetterSetterTypeName()
     */
    @Override public String getGetterSetterTypeName()
    {
        String getterSetterTypeName = null;

        if (this.isMany())
        {
            final boolean specificInterfaces =
                Boolean.valueOf(
                    ObjectUtils.toString(this.getConfiguredProperty(SPECIFIC_COLLECTION_INTERFACES))).booleanValue();

            final TypeMappings mappings = this.getLanguageMappings();
            if (mappings != null)
            {
                if (this.isMap())
                {
                    getterSetterTypeName = mappings.getTo(UMLProfile.MAP_TYPE_NAME);
                }
                else if (specificInterfaces)
                {
                    if (this.isSet())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.SET_TYPE_NAME);
                    }
                    else if (this.isList())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.LIST_TYPE_NAME);
                    }
                    else if (this.isCollection())
                    {
                        getterSetterTypeName = mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);
                    }
                }
                else
                {
                    getterSetterTypeName = this.getDefaultCollectionInterface();
                }
            }
            else
            {
                getterSetterTypeName = this.getDefaultCollectionInterface();
            }
        }
        else
        {
            final ClassifierFacade type = this.getType();
            if (type instanceof EJB3EntityFacade)
            {
                final String typeName = ((EJB3EntityFacade)type).getFullyQualifiedEntityName();
                if (StringUtils.isNotBlank(typeName))
                {
                    getterSetterTypeName = typeName;
                }
            }
        }

        if (getterSetterTypeName == null)
        {
            getterSetterTypeName = super.getGetterSetterTypeName();
        }
        else if (this.isMany())
        {
            /**
             * Set this association end's type as a template parameter if required
             */
            if ("true".equals(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)))
            {
                getterSetterTypeName += '<'
                    + (this.isMap() ? this.getCollectionIndexType() + ", " : "")
                    + this.getType().getFullyQualifiedName() + '>';
            }
        }
        return getterSetterTypeName;
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see AssociationEndFacade#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        Object type = this.getOtherEnd().getType();

        if ((type != null) && EJB3EntityFacade.class.isAssignableFrom(type.getClass()))
        {
            EJB3EntityFacade entity = (EJB3EntityFacade)type;

            /**
             * Exclude ONLY if single table inheritance exists
             */
            if (entity.isRequiresGeneralizationMapping() && entity.isInheritanceSingleTable()
                    && !entity.isEmbeddableSuperclassGeneralizationExists())
            {
                required = false;
            }
        }

        return required;
    }

    /**
     * @return fetch type
     * @see EJB3AssociationEndFacade#getFetchType()
     */
    @Override
    protected String handleGetFetchType()
    {
        String fetchType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);

        if (StringUtils.isBlank(fetchType))
        {
            // check whether or not composition defines eager loading is turned on
            final boolean compositionDefinesEagerLoading =
                Boolean.valueOf(String.valueOf(this.getConfiguredProperty(COMPOSITION_DEFINES_EAGER_LOADING)))
                       .booleanValue();

            if (compositionDefinesEagerLoading)
            {
                if (this.getOtherEnd().isComposition())
                {
                    fetchType = EJB3Globals.FETCH_TYPE_EAGER;
                }
                else if (this.getOtherEnd().isAggregation())
                {
                    fetchType = EJB3Globals.FETCH_TYPE_LAZY;
                }
            }
        }

        /**
         * Go for defaults if blank
         */
        if (StringUtils.isBlank(fetchType))
        {
            if (this.getOtherEnd().isOne2Many() || this.getOtherEnd().isMany2Many())
            {
                fetchType = EJB3Globals.FETCH_TYPE_LAZY;
            }
            else
            {
                fetchType = EJB3Globals.FETCH_TYPE_EAGER;
            }
        }
        return fetchType;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsEager()
     */
    @Override
    protected boolean handleIsEager()
    {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (EJB3Globals.FETCH_TYPE_EAGER.equalsIgnoreCase(this.getFetchType()))
            {
                isEager = true;
            }
        }
        return isEager;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsLazy()
     */
    @Override
    protected boolean handleIsLazy()
    {
        boolean isLazy = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (EJB3Globals.FETCH_TYPE_LAZY.equalsIgnoreCase(this.getFetchType()))
            {
                isLazy = true;
            }
        }
        return isLazy;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsOwning()
     */
    @Override
    protected boolean handleIsOwning()
    {
        boolean owning = false;
        if (this.isAggregation() || this.isComposition())
        {
            owning = true;
        }
        else if (!this.isNavigable())
        {
            owning = true;
        }
        return owning;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsOptional()
     */
    @Override
    protected boolean handleIsOptional()
    {
        boolean optional = true;
        String optionalString = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_OPTIONAL);

        if (StringUtils.isBlank(optionalString))
        {
            optional = !this.isRequired();
        }
        else
        {
            optional = Boolean.valueOf(optionalString).booleanValue();
        }
        return optional;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetOrderByClause()
     */
    @Override
    protected String handleGetOrderByClause()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_ORDERBY);
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetColumnDefinition()
     */
    @Override
    protected String handleGetColumnDefinition()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
    }

    /**
     * Returns true if the tagged name exists for this association end.
     *
     * @param name The tagged name to lookup.
     * @return boolean True if the tagged name exists.  False otherwise.
     *
     * @see EJB3AssociationEndFacadeLogic#handleHasTaggedValue(String)
     */
    @Override
    protected boolean handleHasTaggedValue(String name)
    {
        boolean exists = false;
        if (StringUtils.isNotBlank(name))
        {
            // trim to remove leading/trailing spaces
            name = StringUtils.trimToEmpty(name);

            // loop over tagged values and match the argument tagged value name
            for (TaggedValueFacade taggedValue : this.getTaggedValues())
            {
                // return with true on the first match found
                //if (name.equals(taggedValue.getName()))
                String tagName = taggedValue.getName();
                if (name.equals(tagName) || MetafacadeUtils.getEmfTaggedValue(name).equals(tagName)
                    || MetafacadeUtils.getUml14TaggedValue(name).equals(tagName))
                {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    /**
     * Resolves a comma separated list of cascade types from andromda.xml

     * @param cascadesString
     * @return fully qualified cascade type sequence
     */
    private String getFullyQualifiedCascadeTypeList(final String cascadesString)
    {
        StringBuilder buf = null;
        if (StringUtils.isNotBlank(cascadesString))
        {
            String[] ct = cascadesString.split(",");
            for (int i = 0; i < ct.length; i++)
            {
                final String value = ct[i].trim();
                if (StringUtils.isNotBlank(value))
                {
                    if (buf == null)
                    {
                        buf = new StringBuilder();
                    }
                    else
                    {
                        buf.append(", ");
                    }

                    buf.append(cascadeTable.get(value));
                }
            }
        }
        return buf == null ? null : buf.toString();
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCascadeType()
     */
    @Override
    protected String handleGetCascadeType()
    {
        String cascade = null;
        final Collection<String> taggedValues = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_PERSISTENCE_CASCADE_TYPE);
        if (taggedValues != null && !taggedValues.isEmpty())
        {
            StringBuilder buf = null;
            for (String value : taggedValues)
            {
                if (buf == null)
                {
                    buf = new StringBuilder();
                }
                else
                {
                    buf.append(", ");
                }
                if (StringUtils.isNotBlank(value))
                {
                    buf.append(cascadeTable.get(value));
                }
            }
            if (buf != null)
            {
                cascade = buf.toString();
            }
        }
        else if ((this.getOtherEnd() != null) &&
                 (this.getOtherEnd().isAggregation() || this.getOtherEnd().isComposition()))
        {
            cascade = cascadeTable.get(ENTITY_CASCADE_REMOVE);
            if (this.getOtherEnd().isComposition())
            {
                if (StringUtils.isBlank(this.getCompositionCascadeType()))
                {
                    if (this.getType() instanceof EJB3EntityFacade)
                    {
                        EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                        cascade = (ENTITY_CASCADE_NONE.equalsIgnoreCase(entity.getDefaultCascadeType()) ?
                                null : this.getFullyQualifiedCascadeTypeList(entity.getDefaultCascadeType()));
                    }
                }
                else
                {
                    cascade = (ENTITY_CASCADE_NONE.equalsIgnoreCase(this.getCompositionCascadeType()) ?
                            null : this.getFullyQualifiedCascadeTypeList(this.getCompositionCascadeType()));
                }
            }
            else if (this.getOtherEnd().isAggregation())
            {
                if (StringUtils.isBlank(this.getAggregationCascadeType()))
                {
                    if (this.getType() instanceof EJB3EntityFacade)
                    {
                        EJB3EntityFacade entity = (EJB3EntityFacade)this.getType();
                        cascade = (ENTITY_CASCADE_NONE.equalsIgnoreCase(entity.getDefaultCascadeType()) ?
                                null : this.getFullyQualifiedCascadeTypeList(entity.getDefaultCascadeType()));
                    }
                }
                else
                {
                    cascade = (ENTITY_CASCADE_NONE.equalsIgnoreCase(this.getAggregationCascadeType()) ?
                            null : this.getFullyQualifiedCascadeTypeList(this.getAggregationCascadeType()));
                }
            }
        }
        else if (this.isComposition())
        {
            /*
             * On the composite side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propagation - overriding a default cascade
             * value
             */
            // TODO cascade can only be null at this point - anything else to change?
            //cascade = null;
        }
        else if (this.isAggregation())
        {
            /*
             * On the aggregation side of the relationship, always enforce no cascade delete
             * property indicating no cascadable propagation - overriding a default cascade
             * value
             */
            // TODO cascade can only be null at this point - anything else to change?
            //cascade = null;
        }
        return cascade;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCompositionCascadeType()
     */
    @Override
    protected String handleGetCompositionCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(ENTITY_DEFAULT_COMPOSITE_CASCADE)));
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetAggregationCascadeType()
     */
    @Override
    protected String handleGetAggregationCascadeType()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(ENTITY_DEFAULT_AGGREGATION_CASCADE)));
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionType()
     */
    @Override
    protected String handleGetCollectionType()
    {
        String collectionType = this.getSpecificCollectionType();
        if (!collectionTypes.contains(collectionType))
        {
            if (this.isOrdered())
            {
                collectionType = COLLECTION_TYPE_LIST;
            }
            else
            {
                collectionType =
                    (String)this.getConfiguredProperty(ASSOCIATION_COLLECTION_TYPE);
            }
        }
        return collectionType;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionTypeImplemenationClass()
     */
    @Override
    protected String handleGetCollectionTypeImplemenationClass()
    {
        String collectionTypeImplementationClass = null;
        if (this.isMany())
        {
            if (this.isSet())
            {
                collectionTypeImplementationClass = String.valueOf(
                        this.getConfiguredProperty(SET_TYPE_IMPLEMENTATION));
            }
            else if (this.isMap())
            {
                collectionTypeImplementationClass = String.valueOf(
                        this.getConfiguredProperty(MAP_TYPE_IMPLEMENTATION));
            }
            else if (this.isList() || this.isCollection())
            {
                collectionTypeImplementationClass = String.valueOf(
                        this.getConfiguredProperty(LIST_TYPE_IMPLEMENTATION));
            }
        }
        return collectionTypeImplementationClass;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionTypeImplementation()
     */
    @Override
    protected String handleGetCollectionTypeImplementation()
    {
        return this.getCollectionTypeImplementation(null);
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionTypeImplementation(String)
     */
    @Override
    protected String handleGetCollectionTypeImplementation(final String arg)
    {
        StringBuilder implementation = new StringBuilder();
        if (this.isMany())
        {
            implementation.append("new ");
            implementation.append(this.getCollectionTypeImplemenationClass());

            // set this association end's type as a template parameter if required
            if (Boolean.valueOf(String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)))
                       .booleanValue())
            {
                implementation.append("<");
                if (this.isMap())
                {
                    implementation.append(this.getCollectionIndexType());
                    implementation.append(", ");
                }
                implementation.append(this.getType().getFullyQualifiedName());
                implementation.append(">");
            }
            implementation.append("(");
            if (StringUtils.isNotBlank(arg))
            {
                implementation.append(arg);
            }
            implementation.append(")");
        }

        return implementation.toString();
    }

    /**
     * Gets the collection type defined on this association end.
     *
     * @return the specific collection type.
     */
    private String getSpecificCollectionType()
    {
        return ObjectUtils.toString(
            this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_COLLECTION_TYPE));
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionIndexType()
     */
    @Override
    protected String handleGetCollectionIndexType()
    {
        Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_INDEX_TYPE);
        if (value == null)
        {
            Object name = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_INDEX);
            if (name == null)
            {
                // Find the identifier
                EJB3EntityAttributeFacade identifier = ((EJB3EntityFacade)this.getOtherEnd().getType()).getIdentifier();
                value = identifier.getType().getFullyQualifiedName();
                return value.toString();
            }
            // Find the attribute corresponding to name
            Collection<AttributeFacade> attributes = ((EJB3EntityFacade)this.getOtherEnd().getType()).getAttributes();
            for (AttributeFacade attrib : attributes)
            {
                EJB3EntityAttributeFacade attribute = (EJB3EntityAttributeFacade)attrib;
                if (attribute.getName().equals(name))
                {
                    value = attribute.getType().getFullyQualifiedName();
                    return value.toString();
                }
            }

            // value can only be null at this point
            value = this.getConfiguredProperty(COLLECTION_INDEX_TYPE);
            if (StringUtils.isBlank(ObjectUtils.toString(value)))
            {
                value = null;
            }
         }

        if (value != null)
        {
            if (value instanceof String)
            {
                value = this.getRootPackage().findModelElement((String)value);
            }
            if (value instanceof EJB3TypeFacade)
            {
                value = ((EJB3TypeFacade)value).getFullyQualifiedEJB3Type();
            }
        }
        return (value != null) ? ObjectUtils.toString(value) : null;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCollectionIndexName()
     */
    @Override
    protected String handleGetCollectionIndexName()
    {
        Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_ASSOCIATION_INDEX);
        if ((value == null) && this.isConfiguredProperty(COLLECTION_INDEX_NAME))
        {
            value = this.getConfiguredProperty(COLLECTION_INDEX_NAME);
            if (StringUtils.isBlank(ObjectUtils.toString(value)))
            {
                value = null;
            }
        }

        if (value != null)
        {
            return ObjectUtils.toString(value);
        }
        final String otherEntityName = ((EJB3EntityFacade)this.getOtherEnd().getType()).getEntityName();
        final Object separator = this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR);
        return EntityMetafacadeUtils.toSqlName(
            otherEntityName,
            separator) + separator + EntityMetafacadeUtils.toSqlName(
            this.getName(),
            separator) + separator + "IDX";
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsMap()
     */
    @Override
    protected boolean handleIsMap()
    {
        boolean isMap = COLLECTION_TYPE_MAP.equalsIgnoreCase(this.getCollectionType());
        if (isMap && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isMap = !this.isOrdered();
        }
        return isMap;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsList()
     */
    @Override
    protected boolean handleIsList()
    {
        boolean isList = COLLECTION_TYPE_LIST.equalsIgnoreCase(this.getCollectionType());
        if (!isList && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isList = this.isOrdered();
        }
        return isList;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsSet()
     */
    @Override
    protected boolean handleIsSet()
    {
        boolean isSet = COLLECTION_TYPE_SET.equalsIgnoreCase(this.getCollectionType());
        if (isSet && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isSet = !this.isOrdered();
        }
        return isSet;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsCollection()
     */
    @Override
    protected boolean handleIsCollection()
    {
        boolean isCollection = COLLECTION_TYPE_COLLECTION.equalsIgnoreCase(this.getCollectionType());
        if (!isCollection && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isCollection = this.isOrdered();
        }
        return isCollection;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetLabelName()
     */
    @Override
    protected String handleGetLabelName()
    {
        String labelNamePattern = (this.isMany() ?
                (String)this.getConfiguredProperty(EJB3Globals.LABEL_COLLECTION_NAME_PATTERN) :
                    (String)this.getConfiguredProperty(EJB3Globals.LABEL_SINGLE_NAME_PATTERN));

        return MessageFormat.format(
                labelNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetGetterLabelName()
     */
    @Override
    protected String handleGetGetterLabelName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getLabelName());
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetSetterLabelName()
     */
    @Override
    protected String handleGetSetterLabelName()
    {
        return "set" + StringUtils.capitalize(this.getLabelName());
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetCacheType()
     */
    @Override
    protected String handleGetCacheType()
    {
        String cacheType = (String)super.findTaggedValue(EJB3Profile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
        if (StringUtils.isBlank(cacheType))
        {
            cacheType = String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_CACHE));
        }
        return StringUtils.trimToEmpty(cacheType);
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsAssociationCacheEnabled()
     */
    @Override
    protected boolean handleIsAssociationCacheEnabled()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(HIBERNATE_ASSOCIATION_ENABLE_CACHE)));
    }


    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsForeignKeyConstraintDefined()
     */
    @Override
    protected boolean handleIsForeignKeyConstraintDefined()
    {
        boolean fkConstraintDefined = false;
        if (super.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME) != null)
        {
            fkConstraintDefined = true;
        }
        return fkConstraintDefined;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetForeignKeyConstraintName(String)
     */
    @Override
    protected String handleGetForeignKeyConstraintName(final String suffix)
    {
        String constraintName;

        final Object taggedValueObject = super.findTaggedValue(
                UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);

        /**
         * Construct our own foreign key constraint name here
         */
        StringBuilder buffer = new StringBuilder();

        if (taggedValueObject == null)
        {
            final ClassifierFacade type = this.getOtherEnd().getType();
            if (type instanceof Entity)
            {
                //Entity entity = (Entity)type;
                //Instead of using the entity name, use the association end name to avoid duplication of
                //FK constraint names which causes failures during table creation for some DBs (MySQL)
                buffer.append(
                        EntityMetafacadeUtils.toSqlName(
                                this.getOtherEnd().getName(),
                                this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR)));
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }

            buffer.append(this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));

            /**
             * Add the suffix - which is the name of the identifier pk column if not blank
             * otherwise use the column name of the relationship
             */
            if (StringUtils.isNotBlank(suffix))
            {
                buffer.append(suffix);
            }
            else
            {
                buffer.append(this.getColumnName());
            }
            constraintName = buffer.toString();

            final String constraintSuffix =
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.CONSTRAINT_SUFFIX)).trim();

            /**
             * we take into consideration the maximum length allowed
             */
            final String maxLengthString = (String)super.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
            final short maxLength = (short)(Short.valueOf(maxLengthString).shortValue() - constraintSuffix.length());
            buffer = new StringBuilder(
                    EntityMetafacadeUtils.ensureMaximumNameLength(constraintName, Short.valueOf(maxLength)));
            buffer.append(constraintSuffix);
        }
        else
        {
            // use the tagged value
            buffer.append(taggedValueObject.toString());
        }

        return buffer.toString();
    }


    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetForeignKeyName(String)
     */
    @Override
    protected String handleGetForeignKeyName(String suffix)
    {
        if (StringUtils.isNotBlank(suffix))
        {
            suffix = new String(
                    this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) +
                    suffix +
                    this.getForeignKeySuffix());
        }
        else
        {
            suffix = this.getForeignKeySuffix();
        }

        String columnName = null;
        // prevent ClassCastException if the association isn't an Entity
        if (this.getType() instanceof Entity)
        {
            final String columnNamePrefix =
                this.isConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)
                ? ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)) : null;
            columnName =
                EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                    columnNamePrefix,
                    this,
                    UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
                    ((Entity)this.getType()).getMaxSqlNameLength(),
                    suffix,
                    this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
        }
        return columnName;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetDefaultCollectionInterface()
     */
    @Override
    protected String handleGetDefaultCollectionInterface()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(DEFAULT_COLLECTION_INTERFACE));
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsCollectionInterfaceSortedSet()
     */
    @Override
    protected boolean handleIsCollectionInterfaceSortedSet()
    {
        boolean isInterfaceSortedSet = false;
        if (this.getGetterSetterTypeName().startsWith(EJB3Globals.COLLECTION_INTERFACE_SORTED_SET))
        {
            isInterfaceSortedSet = true;
        }
        return isInterfaceSortedSet;
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleGetHibernateCascadeType()
     */
    @Override
    protected String handleGetHibernateCascadeType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_HIBERNATE_CASCADE);
    }

    /**
     * @see EJB3AssociationEndFacadeLogic#handleIsHibernateCascadeExists()
     */
    @Override
    protected boolean handleIsHibernateCascadeExists()
    {
        return StringUtils.isNotBlank(this.getHibernateCascadeType()) ? true : false;
    }
}