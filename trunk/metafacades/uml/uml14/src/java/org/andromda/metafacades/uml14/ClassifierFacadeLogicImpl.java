package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;

/**
 * Metaclass facade implementation.
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
    implements org.andromda.metafacades.uml.ClassifierFacade
{
    // ---------------- constructor -------------------------------

    public ClassifierFacadeLogicImpl(
        org.omg.uml.foundation.core.Classifier metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getOperations()
     */
    public java.util.Collection handleGetOperations()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof Operation;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getAttributes()
     */
    public java.util.Collection handleGetAttributes()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            public boolean evaluate(Object object)
            {
                return object instanceof Attribute;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacadeLogic#getAssociationEnds()
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return UMLMetafacadeUtils.getCorePackage().getAParticipantAssociation()
            .getAssociation(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitiveType()
     */
    public boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive, 
        // otherwise it isn't
        return this.getWrapperMappings() != null
            && this.getWrapperMappings().containsFrom(
                this.getFullyQualifiedName());
    }

    /**
     * The suffix of an array type.
     */
    private static String ARRAY_SUFFIX = "[]";

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
    public boolean handleIsArrayType()
    {
        return this.getFullyQualifiedName(true).endsWith(ARRAY_SUFFIX);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    public String handleGetWrapperName()
    {
        String wrapperName = null;
        if (this.getWrapperMappings() != null)
        {
            if (this.getWrapperMappings().containsFrom(this.getFullyQualifiedName()))
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
    public Mappings getWrapperMappings()
    {
        final String propertyName = "wrapperMappingsUri";
        Object property = this.getConfiguredProperty(propertyName);
        Mappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = Mappings.getInstance(uri);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '"
                    + uri + "'";
                logger.error(errMsg, th);
                //don't throw the exception
            }
        }
        else
        {
            mappings = (Mappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    public boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(this, UMLMetafacadeGlobals.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    public boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(this, UMLMetafacadeGlobals.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    public boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(this, UMLMetafacadeGlobals.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    public boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(this, UMLMetafacadeGlobals.DATE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    public Collection handleGetAttributes(boolean follow)
    {
        Collection attributes = this.getAttributes();
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null
            && follow; superClass = (ClassifierFacade)superClass
            .getGeneralization())
        {
            attributes.addAll(superClass.getAttributes());
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    public String handleGetOperationCallFromAttributes()
    {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");

        for (Iterator it = getAttributes().iterator(); it.hasNext();)
        {
            AttributeFacade a = (AttributeFacade)it.next();

            sb.append(separator);
            String typeName = a.getType().getFullyQualifiedName();
            sb.append(typeName);
            sb.append(" ");
            sb.append(a.getName());
            separator = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    public boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    public Collection handleGetStaticAttributes()
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    public java.util.Collection handleGetInstanceAttributes()
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public java.util.Collection handleGetProperties()
    {
        Collection properties = this.getAttributes();
        class ConnectingEndTransformer
            implements Transformer
        {
            public Object transform(Object object)
            {
                return ((AssociationEndFacade)object).getOtherEnd();
            }
        }
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(
            connectingEnds,
            new ConnectingEndTransformer());
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade)object).isNavigable();
            }
        }
        CollectionUtils.filter(connectingEnds, new NavigableFilter());
        properties.addAll(connectingEnds);
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    public Collection handleGetAbstractions()
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
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    public boolean handleIsDataType()
    {
        return DataType.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    public boolean handleIsInterface()
    {
        return Interface.class.isAssignableFrom(this.metaObject.getClass());
    }
    
    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    public Object handleGetNonArray()
    {
        ClassifierFacade nonArrayType = this;
        if (this.getFullyQualifiedName().indexOf(ARRAY_SUFFIX) != -1)
        {
            nonArrayType = (ClassifierFacade)this.getRootPackage()
                .findModelElement(
                    this.getFullyQualifiedName(true).replaceAll("\\[\\]", ""));
        }
        return nonArrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    public Object handleGetArray()
    {
        ClassifierFacade arrayType = this;
        String name = this.getFullyQualifiedName(true);
        if (name.indexOf(ARRAY_SUFFIX) == -1)
        {
            name = name + ARRAY_SUFFIX;
            arrayType = (ClassifierFacade)this.getRootPackage()
                .findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#addAttribute(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void handleAddAttribute(
        String name,
        String fullyQualifiedType,
        String visibility)
    {
        CorePackage corePackage = UMLMetafacadeUtils.getCorePackage();
        Attribute attribute = corePackage.getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UMLMetafacadeUtils
            .getVisibilityKind(visibility));
        this.metaObject.getFeature().add(attribute);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    public boolean handleIsEnumeration()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION);
    }

    public String handleGetJavaNullString()
    {
        String javaNullString = null;
        final String typeName = getFullyQualifiedName(false);
        if (isPrimitive())
        {
            if ("boolean".equals(typeName) || "java.lang.Boolean".equals(typeName))
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
}