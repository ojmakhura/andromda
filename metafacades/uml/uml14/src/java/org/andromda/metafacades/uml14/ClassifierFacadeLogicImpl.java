package org.andromda.metafacades.uml14;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ClassifierFacadeLogicImpl(
        Classifier metaObject,
        String context)
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

    protected Collection<Operation> handleGetOperations()
    {
        return new FilteredCollection(this.metaObject.getFeature())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof org.omg.uml.foundation.core.Operation;
            }
        };
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
        final Collection<Operation> operations = new LinkedHashSet();

        // add all of this classifier's operations
        operations.addAll(new FilteredCollection(metaObject.getFeature())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof Operation;
                }
            });

        if (!this.isInterface())
        {
            final Collection<ClassifierFacade> interfaces = this.getInterfaceAbstractions();
            for (Iterator interfaceIterator = interfaces.iterator(); interfaceIterator.hasNext();)
            {
                final ClassifierFacade interfaceElement = (ClassifierFacade)interfaceIterator.next();
                operations.addAll(resolveInterfaceOperationsRecursively(interfaceElement));
            }
        }

        return operations;
    }

    private static Collection<Operation> resolveInterfaceOperationsRecursively(ClassifierFacade interfaceClassifier)
    {
        final Collection<Operation> operations = new LinkedHashSet(interfaceClassifier.getOperations()); // preserve ordering

        final Collection<GeneralizableElementFacade> generalizations = interfaceClassifier.getGeneralizations();
        for (Iterator<GeneralizableElementFacade> generalizationIterator = generalizations.iterator(); generalizationIterator.hasNext();)
        {
            final ClassifierFacade parent = (ClassifierFacade)generalizationIterator.next();
            if (parent.isInterface())
            {
                operations.addAll(resolveInterfaceOperationsRecursively(parent));
            }
        }

        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    @Override
    protected List handleGetAssociationEnds()
    {
        List associationEnds;
        Collection participantAssociation =
            UML14MetafacadeUtils.getCorePackage().getAParticipantAssociation().getAssociation(metaObject);

        if (participantAssociation instanceof List)
        {
            associationEnds = (List)participantAssociation;
        }
        else
        {
            associationEnds = new ArrayList();
            associationEnds.addAll(participantAssociation);
        }

        return associationEnds;
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
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
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
     * Gets the mappings from primitive types to wrapper types. Some languages have primitives (i.e., Java) and some
     * languages don't, so therefore this property is optional.
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (property instanceof String)
        {
            uri = (String)property;
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
                logger.error(
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    @Override
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    @Override
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    @Override
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBooleanType()
     */
    @Override
    protected boolean handleIsBooleanType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    @Override
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.DATE_TYPE_NAME);
    }

    /**
     * <p>
     * Indicates whether or not this classifier represents a time type.
     * </p>
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDoubleType()
     */
    //@Override
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFloatType()
     */
    //@Override
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isIntegerType()
     */
    //@Override
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isLongType()
     */
    //@Override
    protected boolean handleIsLongType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LONG_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isTimeType()
     */
    @Override
    protected boolean handleIsTimeType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.TIME_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    @Override
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    @Override
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.BLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isClobType()
     */
    @Override
    protected boolean handleIsClobType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.CLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    @Override
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(this, UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    @Override
    protected boolean handleIsStringType()
    {
        // Allow mapping multiple model types to String type
        return "String".equals(this.getFullyQualifiedName())
           || "java.lang.String".equals(this.getFullyQualifiedName())
           || UMLMetafacadeUtils.isType(this, UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    @Override
    protected Collection<Attribute> handleGetAttributes()
    {
        final Collection attributes = new ArrayList(this.metaObject.getFeature());
        for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
        {
            if (!(iterator.next() instanceof Attribute))
            {
                iterator.remove();
            }
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    @Override
    protected Collection<AttributeFacade> handleGetAttributes(boolean follow)
    {
        final Collection<AttributeFacade> attributes = new ArrayList<AttributeFacade>(this.getAttributes());
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
             superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            for (final Iterator<AttributeFacade> iterator = superClass.getAttributes().iterator(); iterator.hasNext();)
            {
                final AttributeFacade superAttribute = iterator.next();
                boolean present = false;
                for (final Iterator<AttributeFacade> attributeIterator = this.getAttributes().iterator(); attributeIterator.hasNext();)
                {
                    final AttributeFacade attribute = attributeIterator.next();
                    if (attribute.getName().equals(superAttribute.getName()))
                    {
                        present = true;
                        break;
                    }
                }
                if (!present)
                {
                    attributes.add(superAttribute);
                }
            }
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    @Override
    protected Collection handleGetProperties()
    {
        final Collection properties = new ArrayList(this.getAttributes());
        properties.addAll(this.getNavigableConnectingEnds());
        return properties;
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
        allRequiredConstructorParameters.addAll(this.getRequiredConstructorParameters());

        final Collection<GeneralizableElementFacade> generalizations = this.getGeneralizations();
        for (Iterator<GeneralizableElementFacade> parents = generalizations.iterator(); parents.hasNext();)
        {
            final GeneralizableElementFacade parent = parents.next();
            if (parent instanceof ClassifierFacade)
            {
                allRequiredConstructorParameters.addAll(((ClassifierFacade)parent).getAllRequiredConstructorParameters());
            }
        }

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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    @Override
    protected Collection handleGetProperties(boolean follow)
    {
        final Collection properties = new ArrayList(this.getAttributes(follow));
        properties.addAll(this.getNavigableConnectingEnds());
        if (follow)
        {
            for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
                 superClass = (ClassifierFacade)superClass.getGeneralization())
            {
                for (final Iterator<ClassifierFacade> iterator = superClass.getNavigableConnectingEnds().iterator(); iterator.hasNext();)
                {
                    final AssociationEndFacade superAssociationEnd = (AssociationEndFacade)iterator.next();
                    boolean present = false;
                    for (final Iterator<AssociationEndFacade> endIterator = this.getAssociationEnds().iterator(); endIterator.hasNext();)
                    {
                        final AssociationEndFacade associationEnd = endIterator.next();
                        if (associationEnd.getName().equals(superAssociationEnd.getName()))
                        {
                            present = true;
                            break;
                        }
                    }
                    if (!present)
                    {
                        properties.add(superAssociationEnd);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    @Override
    protected String handleGetOperationCallFromAttributes()
    {
        final StringBuffer call = new StringBuffer();
        String separator = "";
        call.append('(');
        for (final Iterator iterator = getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();

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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    @Override
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    @Override
    protected Collection<AttributeFacade> handleGetStaticAttributes()
    {
        return new FilteredCollection(this.getAttributes())
            {
                public boolean evaluate(Object object)
                {
                    return ((AttributeFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInterfaceAbstractions()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetInterfaceAbstractions()
    {
        final Collection<ClassifierFacade> interfaceAbstractions = new LinkedHashSet<ClassifierFacade>();
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getImplementedInterfaceList()
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
            final StringBuffer list = new StringBuffer();
            for (final Iterator iterator = interfaces.iterator(); iterator.hasNext();)
            {
                final ModelElementFacade element = (ModelElementFacade)iterator.next();
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

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    @Override
    protected Collection<AttributeFacade> handleGetInstanceAttributes()
    {
        return new FilteredCollection(this.getAttributes())
            {
                public boolean evaluate(Object object)
                {
                    return !((AttributeFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetAbstractions()
    {
        return new FilteredCollection(this.metaObject.getClientDependency())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof Abstraction;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDataType()
     */
    @Override
    protected boolean handleIsDataType()
    {
        return DataType.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isInterface()
     */
    @Override
    protected boolean handleIsInterface()
    {
        return Interface.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    @Override
    protected ClassifierFacade handleGetNonArray()
    {
        ClassifierFacade nonArrayType = (ClassifierFacade)this.THIS();
        if (this.getFullyQualifiedName().indexOf(this.getArraySuffix()) != -1)
        {
            nonArrayType =
                (ClassifierFacade)this.getRootPackage().findModelElement(
                    StringUtils.replace(
                        this.getFullyQualifiedName(true),
                        this.getArraySuffix(),
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
        String name = this.getFullyQualifiedName(true);
        if (!name.contains(this.getArraySuffix()))
        {
            name = name + this.getArraySuffix();
            arrayType = (ClassifierFacade)this.getRootPackage().findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    @Override
    protected boolean handleIsEnumeration()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    @Override
    protected String handleGetJavaNullString()
    {
        String javaNullString;
        if (isPrimitive())
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    @Override
    protected Collection<OperationFacade> handleGetStaticOperations()
    {
        return new FilteredCollection(this.getOperations())
            {
                public boolean evaluate(Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    @Override
    protected Collection<OperationFacade> handleGetInstanceOperations()
    {
        return new FilteredCollection(this.getOperations())
            {
                public boolean evaluate(Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            };
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
                public boolean evaluate(Object object)
                {
                    final AttributeFacade attribute = (AttributeFacade)object;
                    return StringUtils.trimToEmpty(attribute.getName()).equals(name);
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    @Override
    protected String handleGetArrayName()
    {
        return this.getName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getFullyQualifiedArrayName()
     */
    @Override
    protected String handleGetFullyQualifiedArrayName()
    {
        return this.getFullyQualifiedName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSerialVersionUID()
     */
    @Override
    protected long handleGetSerialVersionUID()
    {
        long serialVersionUID;
        final String serialVersionString = UML14MetafacadeUtils.getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.parseLong(serialVersionString);
        }
        else
        {
            serialVersionUID = MetafacadeUtils.calculateDefaultSUID(this);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    @Override
    protected Collection handleGetNavigableConnectingEnds()
    {
        // TODO Change model return type from <ClassifierFacade> to <AssociationEndFacade>
        final ArrayList<AssociationEndFacade> connectingEnds = new ArrayList<AssociationEndFacade>(this.getAssociationEnds());
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public AssociationEndFacade transform(final Object object)
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
    protected Collection<AssociationEndFacade> handleGetNavigableConnectingEnds(boolean follow)
    {
        final Collection<AssociationEndFacade> connectionEnds = new ArrayList(this.getNavigableConnectingEnds());

        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
             superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            for (final Iterator<ClassifierFacade> iterator = superClass.getNavigableConnectingEnds().iterator(); iterator.hasNext();)
            {
                final AssociationEndFacade superAssociationEnd = (AssociationEndFacade)iterator.next();
                boolean present = false;
                for (final Iterator<AssociationEndFacade> endIterator = this.getAssociationEnds().iterator(); endIterator.hasNext();)
                {
                    final AssociationEndFacade associationEnd = endIterator.next();
                    if (associationEnd.getName().equals(superAssociationEnd.getName()))
                    {
                        present = true;
                        break;
                    }
                }
                if (!present)
                {
                    connectionEnds.add(superAssociationEnd);
                }
            }
        }
        return connectionEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isLeaf()
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogic#handleIsAssociationClass()
     */
    protected boolean handleIsAssociationClass()
    {
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    protected Collection<ClassifierFacade> handleGetAssociatedClasses()
    {
        final Set<ClassifierFacade> associatedClasses = new LinkedHashSet<ClassifierFacade>();

        final List<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (int i = 0; i < associationEnds.size(); i++)
        {
            final AssociationEndFacade associationEndFacade = associationEnds.get(i);
            associatedClasses.add(associationEndFacade.getOtherEnd().getType());
        }

        return associatedClasses;
    }

    protected Collection<ClassifierFacade> handleGetAllAssociatedClasses()
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

    protected ClassifierFacade handleGetSuperClass()
    {
        final GeneralizableElementFacade superClass = this.getGeneralization();
        return (ClassifierFacade)(superClass instanceof ClassifierFacade ? superClass : null);
    }

    protected boolean handleIsEmbeddedValue()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_EMBEDDED_VALUE);
    }
}