package org.andromda.metafacades.emf.uml2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.Abstraction;
import org.eclipse.uml2.AssociationClass;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.DataType;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Enumeration;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.PrimitiveType;
import org.eclipse.uml2.Property;

/**
 * Represents a Classifier model element
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ClassifierFacade.
 *
 * @see org.andromda.metafacades.uml.ClassifierFacade
 * @author Bob Fields
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
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
    private static final Logger logger = Logger.getLogger(ClassifierFacadeLogicImpl.class);

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * Indicates whether or not this classifier represents a primitive
     * type. If this type has a wrapper then it's primitive, otherwise it isn't.
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitive()
     */
    @Override
    protected boolean handleIsPrimitive()
    {
        return this.getWrapperMappings() != null &&
        this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName());
    }

    /**
     * Indicates whether or not this classifier represents a wrapped primitive type.
     * @return isWrappedPrimitive
     * @see org.andromda.metafacades.uml.ClassifierFacade#isWrappedPrimitive()
     */
    //@Override
    protected boolean handleIsWrappedPrimitive()
    {
        // Try both the fully qualified name and the ClassName
        return this.getWrapperMappings() != null &&
        ( this.getWrapperMappings().getMappings().containsTo(this.getFullyQualifiedName())
          || this.getWrapperMappings().getMappings().containsTo(this.getName()));
    }

    /**
     * The attributes from this classifier in the form of an operation
     * call (this example would be in Java): '(String
     * attributeOne, String attributeTwo).  If there were no
     * attributes on the classifier, the result would be an empty '()'.
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    @Override
    protected String handleGetOperationCallFromAttributes()
    {
        StringBuilder call = new StringBuilder();
        String separator = "";
        call.append('(');
        for (final Iterator<AttributeFacade> iterator = this.getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = iterator.next();

            call.append(separator);
            String typeName = attribute.getType().getFullyQualifiedName();
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
    protected List handleGetProperties()
    {
        return this.getProperties(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllProperties()
     */
    @Override
    public Collection handleGetAllProperties()
    {
        return this.getProperties(true);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllRequiredConstructorParameters()
     */
    @Override
    public Collection handleGetAllRequiredConstructorParameters()
    {
        final Collection allRequiredConstructorParameters = new ArrayList();

        final Collection<GeneralizableElementFacade> generalizations = this.getGeneralizations();
        for (Iterator<GeneralizableElementFacade> parents = generalizations.iterator(); parents.hasNext();)
        {
            final Object parent = parents.next();
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getRequiredConstructorParameters()
     */
    @Override
    public Collection handleGetRequiredConstructorParameters()
    {
        final Collection requiredConstructorParameters = new ArrayList();

        final Collection properties = this.getProperties();
        for (Iterator propertyIterator = properties.iterator(); propertyIterator.hasNext();)
        {
            final Object property = propertyIterator.next();
            if (property instanceof AttributeFacade)
            {
                final AttributeFacade attribute = (AttributeFacade)property;
                if (attribute.isRequired() || attribute.isReadOnly())
                {
                    requiredConstructorParameters.add(attribute);
                }
            }
            else if (property instanceof AssociationEndFacade)
            {
                final AssociationEndFacade associationEnd = (AssociationEndFacade)property;
                if (associationEnd.isRequired() || associationEnd.isReadOnly())
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
        return this.getFullyQualifiedName(true).endsWith(this.getArraySuffix());
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        // TODO: Private method 'getArraySuffix' also declared in class 'org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl'
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

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
     * defined wrapper class (ie. 'long' maps to 'Long').  If the
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
        if (this.getWrapperMappings() != null)
        {
            if (this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName()))
            {
                wrapperName = this.getWrapperMappings().getTo(this.getFullyQualifiedName());
            }
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
            String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (final Throwable throwable)
            {
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                ClassifierFacadeLogicImpl.logger.error(
                    errMsg,
                    throwable);

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
     * @return java new string
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
        else if (this.handleIsWrappedPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                this,
                UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNewString = "Boolean.FALSE";
            }
            else
            {
                javaNewString = this.getFullyQualifiedName() + ".valueOf(0)";
            }
        }
        else
        {
            javaNewString = "new " + this.getFullyQualifiedName() + "()";
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
        return "String".equals(this.getFullyQualifiedName())
           || "java.lang.String".equals(this.getFullyQualifiedName())
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
        return this.getName() + this.getArraySuffix();
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
        return this.getFullyQualifiedName() + this.getArraySuffix();
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
        if (logger.isDebugEnabled())
        {
            logger.debug("Starting get serial UID");
        }
        long serialVersionUID;
        String serialVersionString = UmlUtilities.getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.parseLong(serialVersionString);
        }
        else
        {
            serialVersionUID = MetafacadeUtils.calculateDefaultSUID(this);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("SerialVersionUID for " + this.metaObject.getQualifiedName() + " is " + serialVersionUID);
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
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.INTEGER_TYPE_NAME);
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
        return (List<AttributeFacade>) this.shieldedElements(UmlUtilities.getAttributes(
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    @Override
    protected List handleGetProperties(final boolean follow)
    {
        final List properties = new ArrayList();
        if (follow && !this.getGeneralizations().isEmpty())
        {
            for (Iterator<GeneralizableElementFacade> iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
            {
                final Object generalization = iterator.next();
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
                public boolean evaluate(Object object)
                {
                    return object instanceof Abstraction;
                }
            };

            for (Iterator abstractionIterator = dependencies.iterator(); abstractionIterator.hasNext();)
            {
                final Abstraction abstraction = (Abstraction)abstractionIterator.next();
                final List<NamedElement> suppliers = abstraction.getSuppliers();
                for (int i = 0; i < suppliers.size(); i++)
                {
                    final Object supplierObject = suppliers.get(i);
                    if (supplierObject instanceof Interface)
                    {
                        operations.addAll(resolveInterfaceOperationsRecursively((Interface)supplierObject));
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


    private static Collection<Operation> resolveInterfaceOperationsRecursively(Interface classifier)
    {
        final Collection<Operation> operations = new LinkedHashSet<Operation>(classifier.getOwnedOperations());   // preserve ordering

        final List<Classifier> generals = classifier.getGenerals();
        for (int i = 0; i < generals.size(); i++)
        {
            final Classifier generalObject = generals.get(i);
            if (generalObject instanceof Interface)
            {
                operations.addAll(resolveInterfaceOperationsRecursively((Interface)generalObject));
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
    protected List handleGetAssociationEnds()
    {
        return UmlUtilities.getAssociationEnds(this.metaObject, false);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    @Override
    protected ClassifierFacade handleGetNonArray()
    {
        ClassifierFacade nonArrayType = (ClassifierFacade)this.THIS();

        String arraySuffix = this.getArraySuffix();

        if (this.getFullyQualifiedName().indexOf(arraySuffix) != -1)
        {
            PackageFacade packageFacade = this.getRootPackage();
            String fullQualifiedName = this.getFullyQualifiedName(true);

            if (ClassifierFacadeLogicImpl.logger.isDebugEnabled())
            {
                ClassifierFacadeLogicImpl.logger.debug(
                    "Looking for non-array type of element " + fullQualifiedName + " with array suffix " + arraySuffix +
                    ", root: " + packageFacade);
                ClassifierFacadeLogicImpl.logger.debug("Metaobject: " + this.metaObject + " its model is : " + this.metaObject.getModel());
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
            String name = this.getFullyQualifiedName(true);
            if (!name.contains(this.getArraySuffix()))
            {
                name += this.getArraySuffix();
                PackageFacade pkg = this.getRootPackage();
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
        Collection<AttributeFacade> attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AttributeFacade)object).isStatic();
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
        Collection<AttributeFacade> attributes = this.getAttributes();
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return !((AttributeFacade)object).isStatic();
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
        List<OperationFacade> operations = this.getOperations();
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            });
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    @Override
    protected List<OperationFacade> handleGetInstanceOperations()
    {
        List<OperationFacade> operations = this.getOperations();
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            });
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    @Override
    protected Collection<Abstraction> handleGetAbstractions()
    {
        final Collection dependencies = new ArrayList<Dependency>(this.metaObject.getClientDependencies());
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object instanceof Abstraction;
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    @Override
    protected Collection<AssociationEndFacade> handleGetNavigableConnectingEnds()
    {
        final Collection<AssociationEndFacade> connectingEnds = new ArrayList<AssociationEndFacade>(this.getAssociationEnds());
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).isNavigable();
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
        final List<AssociationEndFacade> connectingEnds = (List<AssociationEndFacade>) this.shieldedElements(UmlUtilities.getAssociationEnds(
                    this.metaObject,
                    follow));
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).isNavigable();
                }
            });
        if (ClassifierFacadeLogicImpl.logger.isDebugEnabled())
        {
            ClassifierFacadeLogicImpl.logger.debug("handleGetNavigableConnectingEnds " + this.metaObject.getQualifiedName() + " " + connectingEnds.size());
        }
        return connectingEnds;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.ClassifierFacadeLogic#handleIsLeaf()
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.ClassifierFacadeLogic#handleGetInterfaceAbstractions()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetInterfaceAbstractions()
    {
        final Collection<ClassifierFacade> interfaceAbstractions = new LinkedHashSet();
        if (this.getAbstractions() != null)
        {
            for (Iterator<ClassifierFacade> abstractionIterator = this.getAbstractions().iterator(); abstractionIterator.hasNext();)
            {
                final DependencyFacade abstraction = (DependencyFacade)abstractionIterator.next();
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
        }

        return interfaceAbstractions;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.ClassifierFacadeLogic#handleGetImplementedInterfaceList()
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
                list.append(element.getFullyQualifiedName());
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
       // TODO: This method has been overridden. Why ?
        return null;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO: This method has been overridden. Why ?
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO: This method has been overridden. Why ?
        return false;
    }

    protected void handleCopyTaggedValues(final ModelElementFacade element)
    {
        // TODO: This method has been overridden. Why ?
    }

    protected Object handleGetTemplateParameter(final String parameterName)
    {
        // TODO: This method has been overridden. Why ?
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO: This method has been overridden. Why ?
        return null;
    }*/

    /**
     * @see org.andromda.metafacades.emf.uml2.ClassifierFacadeLogic#handleIsAssociationClass()
     */
    @Override
    protected boolean handleIsAssociationClass()
    {
        // TODO: Check it's working.
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    @Override
    protected Collection<ClassifierFacade> handleGetAssociatedClasses()
    {
        final Set associatedClasses = new LinkedHashSet();

        final List<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (int i = 0; i < associationEnds.size(); i++)
        {
            final AssociationEndFacade associationEndFacade = associationEnds.get(i);
            associatedClasses.add(associationEndFacade.getOtherEnd().getType());
        }

        return associatedClasses;
    }

    @Override
    protected Set<ClassifierFacade> handleGetAllAssociatedClasses()
    {
        final Set<ClassifierFacade> associatedClasses = new LinkedHashSet<ClassifierFacade>();
        associatedClasses.addAll(this.getAssociatedClasses());
        for (Iterator<GeneralizableElementFacade> parentIterator = this.getGeneralizations().iterator(); parentIterator.hasNext();)
        {
            final ClassifierFacade parent = (ClassifierFacade)parentIterator.next();
            associatedClasses.addAll(parent.getAllAssociatedClasses());
        }

        return associatedClasses;
    }

    @Override
    protected ClassifierFacade handleGetSuperClass()
    {
        final GeneralizableElementFacade superClass = this.getGeneralization();
        return (ClassifierFacade)(superClass instanceof ClassifierFacade ? superClass : null);
    }

    @Override
    protected boolean handleIsEmbeddedValue()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_EMBEDDED_VALUE);
    }
}