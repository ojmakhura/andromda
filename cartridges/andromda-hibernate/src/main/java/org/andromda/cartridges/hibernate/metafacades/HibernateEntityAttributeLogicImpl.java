package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.cartridges.hibernate.HibernateUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.JavaTypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
 * @author Bob Fields
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
{
    private static final long serialVersionUID = 34L;

    /**
     * The property that stores the default entity ID generator type
     */
    public static final String ENTITY_DEFAULT_GENERATOR_TYPE = "entityDefaultGeneratorType";

    /**
     * The property that stores the default generator initial value
     */
    public static final String ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE = "entityDefaultGeneratorInitialValue";

    /**
     * The property that stores the default generator allocation size for
     * incrementing ids
     */
    public static final String ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE = "entityDefaultGeneratorAllocationSize";

    /**
     * The property that stores the default enumeration string literal column
     * length.
     */
    public static final String DEFAULT_ENUM_LITERAL_COLUMN_LENGTH = "entityDefaultEnumLiteralColumnLength";

    /**
     * The property that stores the default temporal type for date based attributes
     */
    public static final String ENTITY_DEFAULT_TEMPORAL_TYPE = "entityDefaultTemporalType";

    /**
     * @param metaObject
     * @param context
     */
    // ---------------- constructor -------------------------------
    public HibernateEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof HibernateEntity)
        {
            HibernateEntity entity = (HibernateEntity)this.getOwner();
            if (entity.isHibernateInheritanceClass() && entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }

    /**
     * Override to provide java specific handling of the default value.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    @Override
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String fullyQualifiedName = StringUtils.trimToEmpty(type.getFullyQualifiedName());
            if (("java.lang.String".equals(fullyQualifiedName) || "String".equals(fullyQualifiedName)) && fullyQualifiedName.indexOf('"')<0)
            {
                defaultValue = '\"' + defaultValue.replaceAll("\"", "") + '\"';
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ')';
            }
            /*else if (type.isEnumeration())
            {
                final String mask = (String)this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK);
                defaultValue = defaultValue;
            }*/
        }
        return defaultValue;
    }

    /**
     * @return returnValue = true if this.getType() instanceof HibernateEmbeddedValue
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isContainsEmbeddedObject()
     */
    @Override
    protected boolean handleIsContainsEmbeddedObject()
    {
        boolean returnValue = false;
        if (this.getType() instanceof HibernateEmbeddedValue)
        {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * @param prefix
     * @param name
     * @return returnValue prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#concatColumnName(String, String)
     */
    @Override
    protected String handleConcatColumnName(
        String prefix,
        String name)
    {
        String returnValue = name;
        if (StringUtils.isNotBlank(prefix)) 
        {
            returnValue = prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name;

            // handle maxSqlNameLength
            Short maxSqlNameLength =
                Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
            final String method = (String)super.getConfiguredProperty(UMLMetafacadeProperties.SHORTEN_SQL_NAMES_METHOD);
            returnValue = EntityMetafacadeUtils.ensureMaximumNameLength(returnValue, maxSqlNameLength,method);
        }
        return returnValue;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isLazy()
     */
    @Override
    protected boolean handleIsLazy()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : false;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#getFormula()
     */
    @Override
    protected String handleGetFormula()
    {
        return (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_FORMULA);
    }

    /**
     * @return HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isInsertEnabled()
     */
    @Override
    protected boolean handleIsInsertEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    /**
     * @return HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isUpdateEnabled()
     */
    @Override
    protected boolean handleIsUpdateEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    private boolean isXmlPersistenceActive()
    {
       return HibernateUtils.isXmlPersistenceActive((String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION),
                                                    (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE));
    }

    private boolean persistIDAsAttribute()
    {
        boolean persistAsAttribute = true;
        String prop = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE_ID_AS_ATTRIBUTE);
        if (prop != null && "false".equalsIgnoreCase(prop))
            persistAsAttribute = false;

        return persistAsAttribute;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttributeLogic#handleGetXmlTagName()
     */
    @Override
    protected String handleGetXmlTagName()
    {
        String tagName = null;

        if (isXmlPersistenceActive())
        {
            tagName = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_XML_TAG_NAME);

            if (StringUtils.isBlank(tagName))
            {
                if (this.isIdentifier() && this.persistIDAsAttribute())
                    tagName = '@' + this.getName();
                else
                    tagName = this.getName();
            }

        }
        return (StringUtils.isBlank(tagName)) ? null : tagName;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttributeLogic#handleGetFullyQualifiedHibernateType()
     */
    @Override
    protected String handleGetFullyQualifiedHibernateType()
    {
        String fullyQualifiedName;

        if (this.getType() != null && this.getType().isEnumeration())
        {
            fullyQualifiedName = "org.andromda.persistence.hibernate.usertypes.HibernateEnumType";
        }
        else
        {
            final String hibernateTypeMappingsUri = (String)this.getConfiguredProperty("hibernateTypeMappingsUri");
            final TypeMappings mappings = TypeMappings.getInstance(hibernateTypeMappingsUri);

            if (this.getType() != null && mappings == null)
            {
                fullyQualifiedName = this.getType().getFullyQualifiedName();
            }
            else
            {
                final String fullyQualifiedModelName = this.getType().getFullyQualifiedName(true);
                if (mappings.getMappings().containsFrom(fullyQualifiedModelName))
                {
                    fullyQualifiedName = mappings.getTo(fullyQualifiedModelName);
                }
                else
                {
                    fullyQualifiedName = this.getType().getFullyQualifiedName();
                }
            }
            fullyQualifiedName = JavaTypeConverter.getJavaLangTypeName(fullyQualifiedName);
        }

        return fullyQualifiedName;
    }

    @Override
    protected String handleGetLobType() {
        return StringUtils.trimToEmpty((String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_EJB_PERSISTENCE_LOB_TYPE));
    }

    @Override
    protected String handleGetOverrideType() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_OVERRIDE_TYPE);
    }

    @Override
    protected String handleGetGeneratorGenericStrategy() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_GENERIC_STRATEGY);
    }

    @Override
    protected boolean handleIsGeneratorTypeGeneric() {
        boolean isGeneric = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_GENERIC.equalsIgnoreCase(this.getGeneratorType())) {
                isGeneric = true;
            }
        }
        return isGeneric;
    }

    @Override
    protected boolean handleIsLob() {
        return this.getType().isBlobType() || this.getType().isClobType();
    }

    @Override
    protected boolean handleIsVersion() {
        boolean isVersion = false;
        if (this.hasStereotype(HibernateProfile.STEREOTYPE_VERSION)) {
            isVersion = true;
        }
        return isVersion;
    }

    @Override
    protected boolean handleIsColumnNullable() {
        boolean nullable = true;
        String nullableString = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE);

        if (StringUtils.isBlank(nullableString)) {
            nullable = (this.isIdentifier() || this.isUnique()) ? false : !this.isRequired();
        } else {
            nullable = Boolean.valueOf(nullableString).booleanValue();
        }
        return nullable;
    }

    @Override
    protected boolean handleIsGeneratorTypeSequence() {
        boolean isSequence = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_SEQUENCE.equalsIgnoreCase(this.getGeneratorType())) {
                isSequence = true;
            }
        }
        return isSequence;
    }

    @Override
    protected boolean handleIsGeneratorTypeTable() {
        boolean isTable = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_TABLE.equalsIgnoreCase(this.getGeneratorType())) {
                isTable = true;
            }
        }
        return isTable;
    }

    @Override
    protected boolean handleIsGeneratorTypeAuto() {
        boolean isAuto = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_AUTO.equalsIgnoreCase(this.getGeneratorType())) {
                isAuto = true;
            }
        }
        return isAuto;
    }

    @Override
    protected boolean handleIsGeneratorTypeNone() {
        boolean isNone = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_NONE.equalsIgnoreCase(this.getGeneratorType())) {
                isNone = true;
            }
        }
        return isNone;
    }

    @Override
    protected boolean handleIsGeneratorTypeIdentity() {
        boolean isIdentity = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) {
            if (HibernateGlobals.GENERATOR_TYPE_IDENTITY.equalsIgnoreCase(this.getGeneratorType())) {
                isIdentity = true;
            }
        }
        return isIdentity;
    }

    @Override
    protected boolean handleIsEager() {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType())) {
            if (HibernateGlobals.FETCH_TYPE_EAGER.equalsIgnoreCase(this.getFetchType())) {
                isEager = true;
            }
        }
        return isEager;
    }

    @Override
    protected String handleGetFetchType() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);
    }

    @Override
    protected String handleGetGeneratorType() {
        String genType = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE);
        if (StringUtils.isBlank(genType)) {
            if (this.getType().isStringType() || this.getType().isDateType() || this.getType().isTimeType()) {
                genType = HibernateGlobals.GENERATOR_TYPE_NONE;
            } else {
                genType = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_TYPE));
                if (StringUtils.isBlank(genType)) {
                    genType = HibernateGlobals.GENERATOR_TYPE_AUTO;
                }
            }
        }
        return genType;
    }

    @Override
    protected String handleGetColumnDefinition() {
        String definition = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
        if (StringUtils.isBlank(definition) && this.getType().isEnumeration()) {
            boolean isOrdinal = false;
            int length = NumberUtils
                    .toInt(String.valueOf(this.getConfiguredProperty(DEFAULT_ENUM_LITERAL_COLUMN_LENGTH)));
            for (AttributeFacade attribute : this.getType().getAttributes()) {
                if (!attribute.getType().isStringType()) {
                    isOrdinal = true;
                    break;
                }
                if (attribute.getName().length() > length) {
                    length = attribute.getName().length();
                }
            }
            if (!isOrdinal) {
                definition = "VARCHAR(" + length + ')';
            }
        }
        return definition;
    }

    @Override
    protected String handleGetColumnPrecision() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION);
    }

    @Override
    protected String handleGetColumnScale() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE);
    }

    /** Generator */
    public static final String GENERATOR = "Generator";
    /** _ */
    public static final char UNDERSCORE = '_';

    @Override
    protected String handleGetGeneratorName() {
        String generatorName = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME);
        if (StringUtils.isBlank(generatorName)) {
            generatorName = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME);
            if (StringUtils.isBlank(generatorName)) {
                generatorName = this.getOwner().getName() + GENERATOR;
            } else {
                generatorName = this.getOwner().getName() + UNDERSCORE + generatorName;
            }
        }
        return generatorName;
    }

    @Override
    protected String handleGetGeneratorSourceName() {
        String sourceName = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME);
        if (StringUtils.isBlank(sourceName)) {
            sourceName = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME);
        }
        return sourceName;
    }

    @Override
    protected String handleGetGeneratorPkColumnValue() {
        String pkColumnValue = (String) this
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE);

        if (StringUtils.isBlank(pkColumnValue)) {
            pkColumnValue = this.getOwner().getName() + UNDERSCORE + this.getColumnName();
        }
        return pkColumnValue;
    }

    @Override
    protected int handleGetGeneratorInitialValue() {
        int initialValue = 1;
        String initialValueStr = (String) this
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE);
        if (StringUtils.isNotBlank(initialValueStr)) {
            initialValue = NumberUtils.toInt(initialValueStr);
        } else {
            initialValueStr = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE));
            if (StringUtils.isNotBlank(initialValueStr)) {
                initialValue = NumberUtils.toInt(initialValueStr);
            }
        }

        return initialValue;
    }

    @Override
    protected int handleGetGeneratorAllocationSize() {
        int allocationSize = 1;
        String allocationSizeStr = (String) this
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE);
        if (StringUtils.isNotBlank(allocationSizeStr)) {
            allocationSize = NumberUtils.toInt(allocationSizeStr);
        } else {
            allocationSizeStr = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE));
            if (StringUtils.isNotBlank(allocationSizeStr)) {
                allocationSize = NumberUtils.toInt(allocationSizeStr);
            }
        }

        return allocationSize;
    }

    @Override
    protected String handleGetTemporalType() {
        String temporalType = null;
        if (this.getType().isDateType()) {
            temporalType = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE);
            if (StringUtils.isBlank(temporalType)) {
                temporalType = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_TEMPORAL_TYPE));
            }
        }
        return temporalType;
    }

    @Override
    protected String handleGetEnumerationType() {
        return (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_ENUMERATION_TYPE);
    }
}