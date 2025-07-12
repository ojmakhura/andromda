package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.TemplateParameter;

/**
 * <p>
 * Represents a Classifier model element
 * </p>
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ClassifierFacade.
 *
 * @see org.andromda.metafacades.uml.ClassifierFacade
 * @author Bob Fields
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for ClassifierFacadeLogicImpl
     * @see org.andromda.metafacades.uml.ClassifierFacade
     * @param metaObject
     * @param context
     */
    public ClassifierFacadeLogicImpl(
        final Classifier metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(ClassifierFacadeLogicImpl.class);

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        String nameMask = "none";
        /*try
        {
            nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_NAME_MASK));
        }
        catch (Exception ignore)
        {
            LOGGER.warn("classifierNameMask not found in " + this.toString());
            nameMask = "none";
        }*/
        
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a primitive
     * type. If this type has a wrapper then it's primitive, otherwise it isn't.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitive()
     */
    @Override
    protected boolean handleIsPrimitive()
    {
        return this.getWrapperMappings() != null &&
        this.getWrapperMappings().getMappings().containsFrom(this.handleGetFullyQualifiedName());
    }

    /**
     * Indicates whether or not this classifier represents a wrapped primitive type.
     * @return wrappedPrimitive true/false
     * @see org.andromda.metafacades.uml.ClassifierFacade#isWrappedPrimitive()
     */
    @Override
    protected boolean handleIsWrappedPrimitive()
    {
        // Try both the fully qualified name and the ClassName
        return this.getWrapperMappings() != null &&
            (this.getWrapperMappings().getMappings().containsTo(this.handleGetFullyQualifiedName())
            || this.getWrapperMappings().getMappings().containsTo(this.handleGetName()));
    }

    /**
     * <p>
     * The attributes from this classifier in the form of an operation
     * call (this example would be in Java): '(String
     * attributeOne, String attributeTwo).  If there were no
     * attributes on the classifier, the result would be an empty '()'.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    @Override
    protected String handleGetOperationCallFromAttributes()
    {
        final StringBuilder call = new StringBuilder("(");
        String separator = "";
        for (AttributeFacade attribute : this.getAttributes())
        {
            call.append(separator);
            final String typeName = attribute.getType().getFullyQualifiedName();
            call.append(typeName);
            call.append(' ');
            call.append(attribute.getName());
            separator = ", ";
        }
        call.append(')');
        return call.toString();
    }

    /**
     * <p>
     * Indicates if this classifier is 'abstract'.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    @Override
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    @Override
    protected List<ModelElementFacade> handleGetProperties()
    {
        return this.handleGetProperties(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllProperties()
     */
    @Override
    public Collection<ModelElementFacade> handleGetAllProperties()
    {
        return (Collection<ModelElementFacade>) this.handleGetProperties(true);
    }

    /**
     * Can return either an AttributeFacade or AssociationFacade Collection (UML2 Property)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllRequiredConstructorParameters()
     */
    @Override
    public Collection<ModelElementFacade> handleGetAllRequiredConstructorParameters()
    {
        final Collection<ModelElementFacade> allRequiredConstructorParameters = new ArrayList<ModelElementFacade>();

        final Collection<GeneralizableElementFacade> generalizations = this.getGeneralizations();
        for (GeneralizableElementFacade parent : generalizations)
        {
            if (parent instanceof ClassifierFacade)
            {
                allRequiredConstructorParameters.addAll(
                    ((ClassifierFacade)parent).getAllRequiredConstructorParameters());
            }
        }

        allRequiredConstructorParameters.addAll(this.getRequiredConstructorParameters());

        return allRequiredConstructorParameters;
    }

    /**
     * Can return either an AttributeFacade or AssociationFacade Collection (UML2 Property)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getRequiredConstructorParameters()
     */
    @Override
    public Collection<ModelElementFacade> handleGetRequiredConstructorParameters()
    {
        final Collection<ModelElementFacade> requiredConstructorParameters = new ArrayList<ModelElementFacade>();

        final Collection<? extends ModelElementFacade> properties = this.getProperties();
        for (final Object property : properties)
        {
            if (property instanceof AttributeFacade)
            {
                final AttributeFacade attribute = (AttributeFacade)property;
                if (!attribute.isDerived() && (attribute.isRequired() || attribute.isReadOnly()))
                {
                    requiredConstructorParameters.add(attribute);
                }
            }
            else if (property instanceof AssociationEndFacade)
            {
                final AssociationEndFacade associationEnd = (AssociationEndFacade)property;
                if (!associationEnd.isDerived() && (associationEnd.isRequired() || associationEnd.isReadOnly()))
                {
                    requiredConstructorParameters.add(associationEnd);
                }
            }
        }

        return requiredConstructorParameters;
    }

    /**
     * <p>
     * True/false depending on whether or not this classifier
     * represents a datatype.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDataType()
     */
    @Override
    protected boolean handleIsDataType()
    {
        return this.metaObject instanceof DataType;
    }

    /**
     * <p>
     * True if this classifier represents an array type. False
     * otherwise.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
    @Override
    protected boolean handleIsArrayType()
    {
        // try both the mapped name and the implementation name, since byte[] is mapped to Blob
        final String name =  this.handleGetFullyQualifiedName(true);
        final String suffix = this.getArraySuffix();
        return name.endsWith(suffix) || this.handleGetFullyQualifiedName(true).endsWith(suffix);
    }

    /*
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
    private String getArraySuffix()
    {
        // TODO: Private method 'getArraySuffix' also declared in class 'org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl'
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }
     */

    /**
     * <p>
     * True if this classifier represents a collection type. False
     * otherwise.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    @Override
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.COLLECTION_TYPE_NAME);
    }

    /**
     * <p>
     * The wrapper name for this classifier if a mapped type has a
     * defined wrapper class (i.e. 'long' maps to 'Long').  If the
     * classifier doesn't have a wrapper defined for it, this method
     * will return a null.  Note that wrapper mappings must be defined
     * for the namespace by defining the 'wrapperMappingsUri', this
     * property must point to the location of the mappings file which
     * maps the primitives to wrapper types.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    @Override
    protected String handleGetWrapperName()
    {
        String wrapperName = null;
        if (this.getWrapperMappings() != null &&
            this.getWrapperMappings().getMappings().containsFrom(this.handleGetFullyQualifiedName()))
        {
            wrapperName = this.getWrapperMappings().getTo(this.handleGetFullyQualifiedName());
        }
        return wrapperName;
    }

    /**
     * Gets the mappings from primitive types to wrapper types. Some languages
     * have primitives (i.e., Java) and some languages don't, so therefore this
     * property is optional.
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        if (property instanceof String)
        {
            final String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (final Exception ex)
            {
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                ClassifierFacadeLogicImpl.LOGGER.error(
                    errMsg, ex);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * <p>
     * True when this classifier is a date type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    @Override
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.DATE_TYPE_NAME);
    }

    /**
     * <p>
     * True/false depending on whether or not this Classifier
     * represents an interface.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isInterface()
     */
    @Override
    protected boolean handleIsInterface()
    {
        return this.metaObject instanceof Interface;
    }

    /**
     * <p>
     * A String representing the new Constructor value for this classifier type to
     * be used in a Java environment.
     * </p>
     * @return new constructor
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    @Override
    protected String handleGetJavaNewString()
    {
        String javaNewString;
        if (this.isPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                    this,
                    UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNewString = "false";
            }
            else
            {
                javaNewString = "0";
            }
        }
        else if (this.isWrappedPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                this,
                UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNewString = "Boolean.FALSE";
            }
            else
            {
                javaNewString = this.handleGetFullyQualifiedName() + ".valueOf(0)";
            }
        }
        else
        {
            javaNewString = "new " + this.handleGetFullyQualifiedName() + "()";
        }
        return javaNewString;
    }

    /**
     * <p>
     * A String representing the null-value for this classifier type to
     * be used in a Java environment.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    @Override
    protected String handleGetJavaNullString()
    {
        String javaNullString;
        if (this.isPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                    this,
                    UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNullString = "false";
            }
            else
            {
                javaNullString = "0";
            }
        }
        else
        {
            javaNullString = "null";
        }
        return javaNullString;
    }

    /**
     * <p>
     * True if this classifier represents a list type. False otherwise.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    @Override
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * <p>
     * True if this classifier represents a set type. False otherwise.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    @Override
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.SET_TYPE_NAME);
    }

    /**
     * <p>
     * Returns true if this type represents a 'file' type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    @Override
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a Map type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    @Override
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a string
     * type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    @Override
    protected boolean handleIsStringType()
    {
        // Allow mapping multiple model types to String type
        return "String".equals(this.handleGetFullyQualifiedName())
           || "java.lang.String".equals(this.handleGetFullyQualifiedName())
           || UMLMetafacadeUtils.isType(
            this,
            UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * <p>
     * True if this classifier is in fact marked as an enumeration.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    @Override
    protected boolean handleIsEnumeration()
    {
        return (this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION)) || (this.metaObject instanceof Enumeration);
    }

    /**
     * <p>
     * The name of the classifier as an array.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    @Override
    protected String handleGetArrayName()
    {
        return this.handleGetName() + this.getArraySuffix();
    }

    /**
     * <p>
     * The fully qualified name of the classifier as an array.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getFullyQualifiedArrayName()
     */
    @Override
    protected String handleGetFullyQualifiedArrayName()
    {
        return this.handleGetFullyQualifiedName() + this.getArraySuffix();
    }

    /**
     * <p>
     * Returns the serial version UID of the underlying model element.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSerialVersionUID()
     */
    @Override
    protected long handleGetSerialVersionUID()
    {
        if (ClassifierFacadeLogicImpl.LOGGER.isDebugEnabled())
        {
            ClassifierFacadeLogicImpl.LOGGER.debug("Starting get serial UID");
        }
        long serialVersionUID;
        final String serialVersionString = UmlUtilities.getSerialVersionUID(this);
        if (serialVersionString == null)
        {
            serialVersionUID = MetafacadeUtils.calculateDefaultSUID(this);
        }
        else
        {
            serialVersionUID = Long.parseLong(serialVersionString);
        }
        if (ClassifierFacadeLogicImpl.LOGGER.isDebugEnabled())
        {
            ClassifierFacadeLogicImpl.LOGGER.debug("SerialVersionUID for "
                + this.metaObject.getQualifiedName() + " is " + serialVersionUID);
        }
        return serialVersionUID;
    }

    /**
     * <p>
     * Returns true if this type represents a Blob type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    @Override
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isClobType()
     */
    @Override
    protected boolean handleIsClobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.CLOB_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates if this type represents a boolean type or not.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBooleanType()
     */
    @Override
    protected boolean handleIsBooleanType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates if this type represents a char, Character, or java.lang.Character type or not.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCharacterType()
     */
    @Override
    protected boolean handleIsCharacterType()
    {
        final String characterType = UMLProfile.CHARACTER_TYPE_NAME;
        // Check both char and Character by taking the part after datatype::
        final String charType = characterType.substring(characterType.indexOf(':')+1).substring(0, 4).toLowerCase();
        return UMLMetafacadeUtils.isType(
            this,
            charType) ||
            UMLMetafacadeUtils.isType(
                this,
                characterType);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a time type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isTimeType()
     */
    @Override
    protected boolean handleIsTimeType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.TIME_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a double type.
     * </p>
     * @return isDoubleType
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDoubleType()
     */
    @Override
    protected boolean handleIsDoubleType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a float type.
     * </p>
     * @return isFloatType
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFloatType()
     */
    @Override
    protected boolean handleIsFloatType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.FLOAT_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents an integer type.
     * </p>
     * @return isIntegerType
     * @see org.andromda.metafacades.uml.ClassifierFacade#isIntegerType()
     */
    @Override
    protected boolean handleIsIntegerType()
    {
        final String integerType = UMLProfile.INTEGER_TYPE_NAME;
        // Check both int and Integer by taking the part after datatype::
        final String intType = integerType.substring(integerType.lastIndexOf(':')+1).substring(0, 3).toLowerCase();
        return UMLMetafacadeUtils.isType(
            this,
            intType) ||
            UMLMetafacadeUtils.isType(
                this,
                integerType);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a long type.
     * </p>
     * @return isLongType
     * @see org.andromda.metafacades.uml.ClassifierFacade#isLongType()
     */
    @Override
    protected boolean handleIsLongType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LONG_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    @Override
    protected List<AttributeFacade> handleGetAttributes(final boolean follow)
    {
        return this.shieldedElements(UmlUtilities.getAttributes(
            this.metaObject,
            follow));
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#findAttribute(String)
     */
    @Override
    protected AttributeFacade handleFindAttribute(final String name)
    {
        return (AttributeFacade)CollectionUtils.find(
            this.getAttributes(true),
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final AttributeFacade attribute = (AttributeFacade)object;
                    return StringUtils.trimToEmpty(attribute.getName()).equals(name);
                }
            });
    }

    /**
     * Can return either an AttributeFacade or AssociationFacade Collection (UML2 Property)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    @Override
    protected List<ModelElementFacade> handleGetProperties(final boolean follow)
    {
        final List<ModelElementFacade> properties = new ArrayList<ModelElementFacade>();
        if (follow && !this.getGeneralizations().isEmpty())
        {
            for (Object generalization : this.getGeneralizations())
            {
                if (generalization instanceof ClassifierFacade)
                {
                    properties.addAll(((ClassifierFacade)generalization).getAllProperties());
                }
            }
        }
        properties.addAll(this.getAttributes(false));
        properties.addAll(this.getNavigableConnectingEnds(false));
        return properties;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetOperations()
     */
    @Override
    protected List<Operation> handleGetOperations()
    {
        final List<Operation> operations;
        if (this.metaObject instanceof Class)
        {
            operations = ((Class)this.metaObject).getOwnedOperations();
        }
        else if (this.metaObject instanceof Interface)
        {
            operations = ((Interface)this.metaObject).getOwnedOperations();
        }
        else
        {
            operations = Collections.emptyList();
        }
        //Collections.sort(operations, new OperationComparator());

        return operations;
    }

    /**
     * Note: if this instance represents an actual class we resolve any realized interfaces recursively, in case this
     * instance represents an interface we return only the owned operations.
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    @Override
    protected Collection<Operation> handleGetImplementationOperations()
    {
        final Collection<Operation> operations;

        if (this.metaObject instanceof Class)
        {
            operations = new LinkedHashSet<Operation>(((Class)this.metaObject).getOwnedOperations());

            final Collection<Dependency> dependencies = new FilteredCollection(this.metaObject.getClientDependencies())
            {
                /** serialVersionUID = 1L */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean evaluate(final Object object)
                {
                    return object instanceof Abstraction;
                }
            };

            for (Dependency dependency : dependencies)
            {
                final List<NamedElement> suppliers = ((Abstraction)dependency).getSuppliers();
                for (NamedElement supplier : suppliers)
                {
                    if (supplier instanceof Interface)
                    {
                        operations.addAll(resolveInterfaceOperationsRecursively((Interface) supplier));
                    }
                }
            }
        }
        else if (this.metaObject instanceof Interface)
        {
            operations = new LinkedHashSet<Operation>(((Interface)this.metaObject).getOwnedOperations());
        }
        else
        {
            operations = Collections.emptyList();
        }

        return operations;
    }

    private static Collection<Operation> resolveInterfaceOperationsRecursively(final Interface classifier)
    {
        // preserve ordering
        final Collection<Operation> operations = new LinkedHashSet<Operation>(classifier.getOwnedOperations());

        final List<Classifier> generals = classifier.getGenerals();
        for (final Classifier generalObject : generals)
        {
            if (generalObject instanceof Interface)
            {
                operations.addAll(resolveInterfaceOperationsRecursively((Interface) generalObject));
            }
        }

        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    @Override
    protected List<Property> handleGetAttributes()
    {
        return UmlUtilities.getAttributes(this.metaObject, false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    @Override
    protected List<AssociationEndFacade> handleGetAssociationEnds()
    {
        return this.shieldedElements(UmlUtilities.getAssociationEnds(this.metaObject, false));
    }

    /**
     * @return Owner of this Classifier. Used to distinguish between a regular class
     * and a TemplateParameter Class/Interface/Type
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    protected Element getOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    @Override
    protected ClassifierFacade handleGetNonArray()
    {
        ClassifierFacade nonArrayType = (ClassifierFacade)this.THIS();

        final String arraySuffix = this.getArraySuffix();

        if (this.handleGetFullyQualifiedName().contains(arraySuffix))
        {
            final PackageFacade packageFacade = this.getRootPackage();
            final String fullQualifiedName = this.handleGetFullyQualifiedName(true);

            if (ClassifierFacadeLogicImpl.LOGGER.isDebugEnabled())
            {
                ClassifierFacadeLogicImpl.LOGGER.debug(
                    "Looking for non-array type of element " + fullQualifiedName + " with array suffix " + arraySuffix +
                    ", root: " + packageFacade);
                ClassifierFacadeLogicImpl.LOGGER.debug("Metaobject: " + this.metaObject + " its model is : " + this.metaObject.getModel());
            }
            nonArrayType =
                (ClassifierFacade)packageFacade.findModelElement(StringUtils.replace(
                        fullQualifiedName,
                        arraySuffix,
                        ""));
        }
        return nonArrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    @Override
    protected ClassifierFacade handleGetArray()
    {
        ClassifierFacade arrayType = (ClassifierFacade)this.THIS();
        if (this.metaObject instanceof PrimitiveType)
        {
            String name = this.handleGetFullyQualifiedName(true);
            if (!name.contains(this.getArraySuffix()))
            {
                name += this.getArraySuffix();
                final PackageFacade pkg = this.getRootPackage();
                if (pkg!=null)
                {
                    arrayType = (ClassifierFacade)this.shieldedElement(this.getRootPackage().findModelElement(name));
                }
            }
        }
        else
        {
            arrayType = null;
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    @Override
    protected Collection<AttributeFacade> handleGetStaticAttributes()
    {
        final Collection<AttributeFacade> attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object != null && ((AttributeFacade)object).isStatic();
                }
            });

        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    @Override
    protected Collection<AttributeFacade> handleGetInstanceAttributes()
    {
        final Collection<AttributeFacade> attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object != null && !((AttributeFacade)object).isStatic();
                }
            });
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    @Override
    protected List<OperationFacade> handleGetStaticOperations()
    {
        final List<OperationFacade> operations = new ArrayList<OperationFacade>();
        for (OperationFacade operation : this.getOperations())
        {
            if (operation.isStatic())
            {
                operations.add(operation);
            }
        }
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    @Override
    protected List<OperationFacade> handleGetInstanceOperations()
    {
        return this.getStaticOperations();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    @Override
    protected Collection<Abstraction> handleGetAbstractions()
    {
        final Collection<Abstraction> abstractions = new ArrayList<Abstraction>();
        for (Dependency dependency : this.metaObject.getClientDependencies())
        {
            if (dependency instanceof Abstraction)
            {
                abstractions.add((Abstraction) dependency);
            }
        }
        return abstractions;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    @Override
    protected Collection<AssociationEndFacade> handleGetNavigableConnectingEnds()
    {
        final Collection<AssociationEndFacade> connectingEnds =
            new ArrayList<AssociationEndFacade>(this.getAssociationEnds());
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    if (object == null) return null;
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object != null && ((AssociationEndFacade)object).isNavigable();
                }
            });
        return connectingEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds(boolean)
     */
    @Override
    protected List<AssociationEndFacade> handleGetNavigableConnectingEnds(final boolean follow)
    {
        final List<AssociationEndFacade> connectingEnds = this.shieldedElements(UmlUtilities.getAssociationEnds(
                    this.metaObject,
                    follow));
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    if (object == null) return null;
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object != null && ((AssociationEndFacade)object).isNavigable();
                }
            });
        if (ClassifierFacadeLogicImpl.LOGGER.isDebugEnabled())
        {
            ClassifierFacadeLogicImpl.LOGGER.debug("handleGetNavigableConnectingEnds "
                + this.metaObject.getQualifiedName() + ' ' + connectingEnds.size());
        }
        return connectingEnds;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleIsLeaf()
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetInterfaceAbstractions()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetInterfaceAbstractions()
    {
        final Collection<ClassifierFacade> interfaceAbstractions = new LinkedHashSet<ClassifierFacade>();
        if (this.getAbstractions() != null)
        {
            // TODO Changing from Iterator to for: causes ClassCastException casting DependencyFacadeLogicImpl to ClassifierFacade
            // TODO Change to handleGetAbstractions, look for target end of type Interface (not Classifier)
            for (final Iterator<ClassifierFacade> abstractionIterator = this.getAbstractions().iterator(); abstractionIterator.hasNext();)
            {
                final Object obj = abstractionIterator.next();
                try
                {
                    final DependencyFacade abstraction = (DependencyFacade)obj;
                    final ModelElementFacade element = abstraction.getTargetElement();

                    if (element instanceof ClassifierFacade)
                    {
                        final ClassifierFacade classifier = (ClassifierFacade)element;
                        if (classifier.isInterface())
                        {
                            interfaceAbstractions.add(classifier);
                        }
                    }
                }
                catch (Exception e)
                {
                    LOGGER.warn("ClassifierFacade.handleGetInterfaceAbstractions " + obj + ' ' + e.getMessage());
                }
            }
        }

        return interfaceAbstractions;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetImplementedInterfaceList()
     */
    @Override
    protected String handleGetImplementedInterfaceList()
    {
        final String interfaceList;

        final Collection<ClassifierFacade> interfaces = this.getInterfaceAbstractions();
        if (interfaces.isEmpty())
        {
            interfaceList = "";
        }
        else
        {
            final StringBuilder list = new StringBuilder();
            for (final Iterator<ClassifierFacade> iterator = interfaces.iterator(); iterator.hasNext();)
            {
                final ClassifierFacade element = iterator.next();
                list.append(element.getBindedFullyQualifiedName(this));
                if (iterator.hasNext())
                {
                    list.append(", ");
                }
            }
            interfaceList = list.toString();
        }

        return interfaceList;
    }

/*    protected Object handleFindTaggedValue(final String tagName, final boolean follow)
    {
        return null;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        return false;
    }*/

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl#handleIsTemplateParametersPresent()
     */
    protected boolean handleIsTemplateParametersPresent()
    {
        if (this.metaObject.getOwnedTemplateSignature()==null
            || this.metaObject.getOwnedTemplateSignature().getOwnedParameters()==null)
        {
            return false;
        }
        return !this.metaObject.getOwnedTemplateSignature().getOwnedParameters().isEmpty();
    }

    /** Not implemented
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl#handleCopyTaggedValues(org.andromda.metafacades.uml.ModelElementFacade)
     */
    protected void handleCopyTaggedValues(final ModelElementFacade element)
    {
        // Not implemented
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl#handleGetTemplateParameter(String)
     */
    protected DataType handleGetTemplateParameter(final String parameterName)
    {
        if (this.metaObject.getOwnedTemplateSignature()==null
                || this.metaObject.getOwnedTemplateSignature().getOwnedParameters()==null)
        {
            return null;
        }

        for (TemplateParameter param : this.metaObject.getOwnedTemplateSignature().getOwnedParameters())
        {
            final DataType element = (DataType)((ClassifierTemplateParameter)param).getOwnedParameteredElement();
            if (element.getName().equals(parameterName))
            {
                return element;
            }
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetTemplateParameters()
     */
    protected Collection<TemplateParameter> handleGetTemplateParameters()
    {
        if (this.metaObject.getOwnedTemplateSignature()==null
                || this.metaObject.getOwnedTemplateSignature().getOwnedParameters()==null)
        {
            return new ArrayList<TemplateParameter>();
        }
        return this.metaObject.getOwnedTemplateSignature().getOwnedParameters();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleIsAssociationClass()
     */
    @Override
    protected boolean handleIsAssociationClass()
    {
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetAssociatedClasses()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetAssociatedClasses()
    {
        final Set<ClassifierFacade> associatedClasses = new LinkedHashSet<ClassifierFacade>();

        final List<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (final AssociationEndFacade associationEndFacade : associationEnds)
        {
            associatedClasses.add(associationEndFacade.getOtherEnd().getType());
        }

        return associatedClasses;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetAllAssociatedClasses()
     */
    @Override
    protected Set<ClassifierFacade> handleGetAllAssociatedClasses()
    {
        final Set<ClassifierFacade> associatedClasses = new LinkedHashSet<ClassifierFacade>();
        associatedClasses.addAll(this.getAssociatedClasses());
        for (final GeneralizableElementFacade gen : this.getGeneralizations())
        {
            final ClassifierFacade parent = (ClassifierFacade)gen;
            associatedClasses.addAll(parent.getAllAssociatedClasses());
        }

        return associatedClasses;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleGetSuperClass()
     */
    @Override
    protected ClassifierFacade handleGetSuperClass()
    {
        final GeneralizableElementFacade superClass = this.getGeneralization();
        return (ClassifierFacade)(superClass instanceof ClassifierFacade ? superClass : null);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ClassifierFacadeLogic#handleIsEmbeddedValue()
     */
    @Override
    protected boolean handleIsEmbeddedValue()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_EMBEDDED_VALUE);
    }

    // Sort Operations by name, then by number of parameters, then by parameter names
    @SuppressWarnings("unused")
    private static class OperationComparator implements Comparator<Operation>
    {
        private static final long serialVersionUID = 1L;
        public int compare(final Operation operation1, final Operation operation2)
        {
            int rtn = operation1.getName().compareTo(operation2.getName());
            if (rtn == 0)
            {
                rtn = operation1.getOwnedParameters().size() - operation1.getOwnedParameters().size();
                if (rtn == 0)
                {
                    int index = 0;
                    for (Parameter parameter : operation1.getOwnedParameters())
                    {
                        rtn = parameter.getName().compareTo(operation2.getOwnedParameters().get(index).getName());
                        if (rtn != 0)
                        {
                            break;
                        }
                        index++;
                    }
                }
            }
            return rtn;
        }
    }
}
