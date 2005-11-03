package org.andromda.cartridges.nspring.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.nspring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd.
 *
 * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd
 */
public class SpringAssociationEndLogicImpl
    extends SpringAssociationEndLogic
{
    private static final Logger logger = Logger.getLogger(SpringAssociationEndLogicImpl.class);

    public SpringAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
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
     * Value for bags
     */
    private static final String COLLECTION_TYPE_BAG = "bag";

    /**
     * Value for list
     */
    private static final String COLLECTION_TYPE_LIST = "list";

    /**
     * Value for collections
     */
    private static final String COLLECTION_TYPE_COLLECTION = "collection";

    /**
     * Stores the valid collection types
     */
    private static final Collection collectionTypes = new ArrayList();

    static
    {
        collectionTypes.add(COLLECTION_TYPE_SET);
        collectionTypes.add(COLLECTION_TYPE_MAP);
        collectionTypes.add(COLLECTION_TYPE_BAG);
        collectionTypes.add(COLLECTION_TYPE_LIST);
        collectionTypes.add(COLLECTION_TYPE_COLLECTION);
    }

    /**
     * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd#isBag()
     */
    protected boolean handleIsBag()
    {
        return this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_BAG);
    }

    /**
     * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd#isList()
     */
    protected boolean handleIsList()
    {
        boolean isList = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_LIST);

        if (!isList && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isList = this.isOrdered();
        }

        return isList;
    }

    /**
     * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd#isMap()
     */
    protected boolean handleIsMap()
    {
        boolean isMap = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_MAP);

        if (isMap && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isMap = !this.isOrdered();
        }

        return isMap;
    }

    /**
     * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd#isSet()
     */
    protected boolean handleIsSet()
    {
        boolean isSet = this.getCollectionType().equalsIgnoreCase(COLLECTION_TYPE_SET);

        if (isSet && StringUtils.isBlank(this.getSpecificCollectionType()))
        {
            isSet = !this.isOrdered();
        }

        return isSet;
    }

    /**
     * @see org.andromda.cartridges.nspring.metafacades.SpringAssociationEnd#getCollectionType()
     */
    protected java.lang.String handleGetCollectionType()
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
                    (String)this.getConfiguredProperty(SpringGlobals.ASSOCIATION_COLLECTION_TYPE);
            }
        }

        return collectionType;
    }

    /**
     * Gets the collection type defined on this association end.
     *
     * @return the specific collection type.
     */
    private String getSpecificCollectionType()
    {
        return ObjectUtils.toString(
            this.findTaggedValue(SpringProfile.TAGGEDVALUE_ASSOCIATION_COLLECTION_TYPE));
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        logger.debug("Enter getGetterSetterTypeName()");

        String getterSetterTypeName = super.getGetterSetterTypeName();

        if (!this.isMany())
        {
            ClassifierFacade type = this.getType();

            if (type instanceof SpringEntity)
            {
                final String typeName = ((SpringEntity)type).getFullyQualifiedEntityName();

                if (StringUtils.isNotEmpty(typeName))
                {
                    getterSetterTypeName = typeName;
                }
            }
        }

        if (this.isMany())
        {
            final boolean specificInterfaces =
                Boolean.valueOf(
                    ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.SPECIFIC_COLLECTION_INTERFACES)))
                       .booleanValue();

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
                }
                else
                {
                    getterSetterTypeName =
                        ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.DEFAULT_COLLECTION_INTERFACE));
                }
            }
            else
            {
                getterSetterTypeName =
                    ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.DEFAULT_COLLECTION_INTERFACE));
            }
        }

        return getterSetterTypeName;
    }
}