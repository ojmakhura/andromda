package org.andromda.metafacades.uml14;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.omg.uml.foundation.core.*;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

/**
 * Metaclass facade implementation.
 */
public class ClassifierFacadeLogicImpl
        extends ClassifierFacadeLogic
        implements org.andromda.metafacades.uml.ClassifierFacade
{
    // ---------------- constructor -------------------------------

    public ClassifierFacadeLogicImpl(org.omg.uml.foundation.core.Classifier metaObject,
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
        final String dataType = getFullyQualifiedName(true);
        return "datatype.char".equals(dataType) || "datatype.int".equals(dataType) ||
                "datatype.float".equals(dataType) || "datatype.double".equals(dataType) ||
                "datatype.long".equals(dataType) || "datatype.boolean".equals(dataType) ||
                "datatype.short".equals(dataType) || "datatype.byte".equals(dataType);
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
        String wrapperName = this.getName();
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
            wrapperName = this.wrapperMappings.getTo(wrapperName);
        }
        return wrapperName;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    public boolean handleIsCollectionType()
    {
        try
        {
            // we check the actual impl. class too because in Java there are many Collection descendants
            return getFullyQualifiedName(true).equals("datatype.Collection") ||
                    java.util.Collection.class.isAssignableFrom(Class.forName(getFullyQualifiedName()));
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    public boolean handleIsDateType()
    {
        try
        {
            // we check the actual impl. class too because in Java there are many Date descendants
            return getFullyQualifiedName(true).equals("datatype.Date") ||
                    java.util.Date.class.isAssignableFrom(Class.forName(getFullyQualifiedName()));
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    public Collection handleGetAttributes(boolean follow)
    {
        Collection attributes = this.getAttributes();
        for (ClassifierFacade superClass = (ClassifierFacade) getGeneralization(); superClass != null
                && follow; superClass = (ClassifierFacade) superClass
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
            AttributeFacade a = (AttributeFacade) it.next();

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
                return ((AttributeFacade) object).isStatic();
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
                return !((AttributeFacade) object).isStatic();
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
                return ((AssociationEndFacade) object).getOtherEnd();
            }
        }
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(connectingEnds,
                new ConnectingEndTransformer());
        class NavigableFilter
                implements Predicate
        {
            public boolean evaluate(Object object)
            {
                return ((AssociationEndFacade) object).isNavigable();
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
            nonArrayType = (ClassifierFacade) this.getRootPackage()
                    .findModelElement(this.getFullyQualifiedName(true).replaceAll("\\[\\]", ""));
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
            arrayType = (ClassifierFacade) this.getRootPackage()
                    .findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#addAttribute(java.lang.String,
            *      java.lang.String, java.lang.String)
     */
    public void handleAddAttribute(String name,
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