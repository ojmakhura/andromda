package org.andromda.metafacades.uml14;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.ResourceUtils;
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
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogic#handleGetOperations()
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
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogic#handleGetAttributes()
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
     * @see org.andromda.metafacades.uml14.ClassifierFacadeLogic#handleGetAssociationEnds()
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return UMLMetafacadeUtils.getCorePackage().getAParticipantAssociation()
            .getAssociation(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitiveType()
     */
    public boolean handleIsPrimitiveType()
    {
        final String dataType = StringUtils
            .trimToEmpty(getFullyQualifiedName(true));
        return "datatype.char".equals(dataType)
            || "datatype.int".equals(dataType)
            || "datatype.float".equals(dataType)
            || "datatype.double".equals(dataType)
            || "datatype.long".equals(dataType)
            || "datatype.boolean".equals(dataType)
            || "datatype.short".equals(dataType)
            || "datatype.byte".equals(dataType);
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
     * The location on the classpath of where to find the wrapper types.
     */
    private static final String WRAPPER_NAMES_LOCATION = "META-INF/wrapper-names.xml";

    /**
     * Stores the type mappings for mapping primitive types to wrapper types.
     */
    private Mappings wrapperMappings = null;

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    public String handleGetWrapperName()
    {
        String wrapperName = this.getFullyQualifiedName();
        if (wrapperMappings == null)
        {
            URL mappingsUri = ResourceUtils.getResource(WRAPPER_NAMES_LOCATION);
            if (mappingsUri == null)
            {
                logger.warn("Wrapper names --> '" + WRAPPER_NAMES_LOCATION
                    + "' could not be found, not "
                    + "attempting to retrieve wrapper name");
            }
            else
            {
                this.wrapperMappings = Mappings.getInstance(mappingsUri);
            }
        }
        if (this.wrapperMappings != null)
        {
            if (this.wrapperMappings.containsFrom(wrapperName))
            {
                wrapperName = this.wrapperMappings.getTo(wrapperName);
            }
            else
            {
                wrapperName = this.getName();
            }
        }
        return wrapperName;
    }
   
    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#hasWrapper()
     */
    public boolean handleHasWrapper()
    {
        // we check for the actual mapped type instead of the model type
        // because when it comes to generating code we only care whether
        // the type thats returned has a wrapper or not
        return !this.getFullyQualifiedName().equals(this.getWrapperName());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    public boolean handleIsCollectionType()
    {
        return this.isType(UMLMetafacadeGlobals.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    public boolean handleIsDateType()
    {
        return this.isType(UMLMetafacadeGlobals.DATE_TYPE_NAME);
    }

    /**
     * Returns true or false depending on whether or not this Classifier or any
     * of its specializations is of the given type having the specified
     * <code>typeName</code>
     * 
     * @param typeName the name of the type (i.e. datatype.Collection)
     * @return true/false
     */
    private boolean isType(String typeName)
    {
        final String type = StringUtils.trimToEmpty(typeName);
        String name = StringUtils.trimToEmpty(this.getFullyQualifiedName(true));
        boolean isType = name.equals(type);
        // if this isn't a collection type, see if we can find any
        // types that inherit from the collection type.
        if (!isType)
        {
            isType = CollectionUtils.find(
                this.getAllGeneralizations(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        String name = StringUtils
                            .trimToEmpty(((ModelElementFacade)object)
                                .getFullyQualifiedName(true));
                        return name.equals(type);
                    }
                }) != null;
        }
        return isType;
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
            attributes.addAll(superClass.getAttributes(follow));
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributesAsList(boolean)
     */
    public String handleGetAttributesAsList(boolean withTypeNames)
    {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");

        for (Iterator it = getAttributes().iterator(); it.hasNext();)
        {
            AttributeFacade a = (AttributeFacade)it.next();

            sb.append(separator);
            if (withTypeNames)
            {
                String typeName = a.getType().getFullyQualifiedName();
                sb.append(typeName);
                sb.append(" ");
                sb.append(a.getName());
            }
            else
            {
                sb.append(a.getGetterName());
                sb.append("()");
            }
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
    public boolean handleIsDatatype()
    {
        return DataType.class.isAssignableFrom(this.metaObject.getClass());
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
}