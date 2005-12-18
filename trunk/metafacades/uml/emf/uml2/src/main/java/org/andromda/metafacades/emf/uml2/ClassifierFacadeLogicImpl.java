package org.andromda.metafacades.emf.uml2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
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
import org.eclipse.uml2.Association;
import org.eclipse.uml2.DataType;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.PrimitiveType;
import org.eclipse.uml2.Property;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ClassifierFacade.
 *
 * @see org.andromda.metafacades.uml.ClassifierFacade
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
    public ClassifierFacadeLogicImpl(
        org.eclipse.uml2.Classifier metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitive()
     */
    protected boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive,
        // otherwise it isn't
        return this.getWrapperMappings() != null &&
        this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName());
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    protected java.lang.String handleGetOperationCallFromAttributes()
    {
        final StringBuffer call = new StringBuffer();
        String separator = "";
        call.append("(");
        for (final Iterator iterator = getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();

            call.append(separator);
            String typeName = attribute.getType().getFullyQualifiedName();
            call.append(typeName);
            call.append(" ");
            call.append(attribute.getName());
            separator = ", ";
        }
        call.append(")");
        return call.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    protected java.util.Collection handleGetProperties()
    {
        final Collection properties = this.getAttributes();
        properties.addAll(this.getNavigableConnectingEnds());
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllProperties()
     */
    public Collection handleGetAllProperties()
    {
        return this.getProperties(true);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAllRequiredConstructorParameters()
     */
    public Collection handleGetAllRequiredConstructorParameters()
    {
        final Collection allRequiredConstructorParameters = new ArrayList();

        final Collection generalizations = this.getGeneralizations();
        for (Iterator parents = generalizations.iterator(); parents.hasNext();)
        {
            final Object parent = parents.next();
            if (parent instanceof ClassifierFacade)
            {
                allRequiredConstructorParameters.addAll(((ClassifierFacade)parent).getAllRequiredConstructorParameters());
            }
        }

        allRequiredConstructorParameters.addAll(this.getRequiredConstructorParameters());

        return allRequiredConstructorParameters;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getRequiredConstructorParameters()
     */
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDataType()
     */
    protected boolean handleIsDataType()
    {
        return metaObject instanceof DataType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
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
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.DATE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isInterface()
     */
    protected boolean handleIsInterface()
    {
        return metaObject instanceof Interface;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    protected boolean handleIsStringType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    protected boolean handleIsEnumeration()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    protected String handleGetArrayName()
    {
        return this.getName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml14.ClassifierFacade#getFullyQualifiedArrayName()
     */
    protected String handleGetFullyQualifiedArrayName()
    {
        return this.getFullyQualifiedName() + this.getArraySuffix();
    }

    /**
     * Calculates the serial version UID of this classifier based on the
     * signature of the classifier (name, visibility, attributes and methods).
     * The algorithm is inspired by
     * {@link java.io.ObjectStreamClass#getSerialVersionUID()}.
     *
     * The value should be stable as long as the classifier remains unchanged
     * and should change as soon as there is any change in the signature of the
     * classifier.
     *
     * @return the serial version UID of this classifier.
     */
    private Long calculateDefaultSUID()
    {
        final StringBuffer buffer = new StringBuffer();

        // class name
        buffer.append(this.getName());

        // class modifiers (visibility)
        buffer.append(this.getVisibility());

        // generalizations
        for (final Iterator iterator = this.getAllGeneralizations().iterator(); iterator.hasNext();)
        {
            ClassifierFacade classifier = (ClassifierFacade)iterator.next();
            buffer.append(classifier.getName());
        }

        // declared fields
        for (final Iterator iterator = this.getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();
            buffer.append(attribute.getName());
            buffer.append(attribute.getVisibility());
            buffer.append(attribute.getType());
        }

        // operations
        for (final Iterator iter = this.getOperations().iterator(); iter.hasNext();)
        {
            OperationFacade operation = (OperationFacade)iter.next();
            buffer.append(operation.getName());
            buffer.append(operation.getVisibility());
            buffer.append(operation.getReturnType());
            for (final Iterator iterator = operation.getParameters().iterator(); iterator.hasNext();)
            {
                final ParameterFacade parameter = (ParameterFacade)iterator.next();
                buffer.append(parameter.getName());
                buffer.append(parameter.getVisibility());
                buffer.append(parameter.getType());
            }
        }
        final String signature = buffer.toString();

        Long serialVersionUID = new Long(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(
                        hashBytes.length,
                        8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = new Long(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            final String errMsg = "Error performing ModelElementFacadeImpl.getSerialVersionUID";
            logger.error(
                errMsg,
                exception);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Default UID for " + metaObject.getQualifiedName() + " is " + serialVersionUID);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSerialVersionUID()
     */
    protected java.lang.Long handleGetSerialVersionUID()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Starting get serial UID");
        }
        Long serialVersionUID;
        String serialVersionString = UmlUtilities.getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.valueOf(serialVersionString);
        }
        else
        {
            serialVersionUID = calculateDefaultSUID();
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("SerialVersionUID for " + metaObject.getQualifiedName() + " is " + serialVersionUID);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBooleanType()
     */
    protected boolean handleIsBooleanType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isTimeType()
     */
    protected boolean handleIsTimeType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.TIME_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    protected java.util.Collection handleGetAttributes(boolean follow)
    {
        final Set attributes = new LinkedHashSet(this.getAttributes());
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            attributes.addAll(
                new FilteredCollection(superClass.getAttributes(follow))
                {
                    public boolean evaluate(final Object object)
                    {
                        boolean valid = true;
                        for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
                        {
                            final AttributeFacade attribute = (AttributeFacade)iterator.next();
                            final AttributeFacade superAttribute = (AttributeFacade)object;
                            if (attribute.getName().equals(superAttribute.getName()))
                            {
                                valid = false;
                                break;
                            }
                        }
                        return valid;
                    }
                });
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#findAttribute(java.lang.String)
     */
    protected org.andromda.metafacades.uml.AttributeFacade handleFindAttribute(java.lang.String name)
    {
        final String aName = name;
        return (AttributeFacade)CollectionUtils.find(
            this.getAttributes(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    final AttributeFacade attribute = (AttributeFacade)object;
                    return StringUtils.trimToEmpty(attribute.getName()).equals(aName);
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    protected java.util.Collection handleGetProperties(boolean follow)
    {
        final Collection properties = this.getAttributes(follow);
        final Collection associations = this.getNavigableConnectingEnds();
        if (follow)
        {
            for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
                superClass = (ClassifierFacade)superClass.getGeneralization())
            {
                // - filter duplicate association ends from each generalization.
                properties.addAll(
                    new FilteredCollection(superClass.getNavigableConnectingEnds())
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = true;
                            for (final Iterator iterator = associations.iterator(); iterator.hasNext();)
                            {
                                final AssociationEndFacade association = (AssociationEndFacade)iterator.next();
                                final AssociationEndFacade superAssociation = (AssociationEndFacade)object;
                                if (association.getName().equals(superAssociation.getName()))
                                {
                                    valid = false;
                                    break;
                                }
                            }
                            return valid;
                        }
                    });
            }
        }
        properties.addAll(associations);
        return properties;
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    protected java.util.Collection handleGetOperations()
    {
        if (metaObject instanceof org.eclipse.uml2.Class)
        {
            return UmlUtilities.getOperations(
                (org.eclipse.uml2.Class)metaObject,
                false);
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    protected java.util.Collection handleGetAttributes()
    {
        final Collection attributes = new ArrayList();
        if (this.metaObject instanceof org.eclipse.uml2.Class)
        {
            final Collection properties = UmlUtilities.getProperties(
                    (org.eclipse.uml2.Class)metaObject,
                    false,
                    false);
            for (final Iterator iterator = properties.iterator(); iterator.hasNext();)
            {
                final Property property = (Property)iterator.next();

                // - the property is an association end 
                final Association association = property.getAssociation();
                if (association == null)
                {
                    attributes.add(property);
                }
            }
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    protected java.util.Collection handleGetAssociationEnds()
    {
        final Collection associationEnds = new ArrayList();
        if (this.metaObject instanceof org.eclipse.uml2.Class)
        {
            final Collection properties = UmlUtilities.getProperties(
                    (org.eclipse.uml2.Class)metaObject,
                    false,
                    false);
            for (final Iterator iterator = properties.iterator(); iterator.hasNext();)
            {
                final Property property = (Property)iterator.next();

                final Object associationEnd = UmlUtilities.getOppositeAssociationEnd(property);
                if (associationEnd != null)
                {
                    associationEnds.add(associationEnd);
                }
            }
        }
        return associationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    protected Object handleGetNonArray()
    {
        ClassifierFacade nonArrayType = this;
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
    protected Object handleGetArray()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Entered getArray for " + metaObject);
        }
        ClassifierFacade arrayType = this;
        if (this.metaObject instanceof PrimitiveType)
        {
            String name = this.getFullyQualifiedName(true);
            if (name.indexOf(this.getArraySuffix()) == -1)
            {
                name = name + this.getArraySuffix();
                arrayType = (ClassifierFacade)this.getRootPackage().findModelElement(name);
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
    protected java.util.Collection handleGetStaticAttributes()
    {
        Collection c = UmlUtilities.getProperties(
                (org.eclipse.uml2.Class)metaObject,
                false,
                false);
        ArrayList stats = new ArrayList();
        for (Iterator i = c.iterator(); i.hasNext();)
        {
            Property p = (Property)i.next();
            if (p.isStatic())
            {
                stats.add(p);
            }
        }
        return c;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    protected java.util.Collection handleGetInstanceAttributes()
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    protected java.util.Collection handleGetStaticOperations()
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
    protected java.util.Collection handleGetInstanceOperations()
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    protected java.util.Collection handleGetAbstractions()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    protected java.util.Collection handleGetNavigableConnectingEnds()
    {
        final Collection connectingEnds = this.getAssociationEnds();
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

    protected boolean handleIsLeaf()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected String handleGetImplementedInterfaceList()
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Object handleFindTaggedValue(
        String tagName,
        boolean follow)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected void handleCopyTaggedValues(ModelElementFacade element)
    {
        // TODO Auto-generated method stub
    }

    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }
}