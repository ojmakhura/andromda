package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CrudEntity.
 *
 * @see org.andromda.metafacades.uml.CrudEntity
 */
public class ManageableEntityLogicImpl extends ManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    public ManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#getCreateModelClassName()
     */
    protected java.lang.String handleGetCreateModelClassName()
    {
        return "Create" + getName() + "Form";
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#getFullCreateControllerPath()
     */
    protected java.lang.String handleGetFullCreateControllerPath()
    {
        return "a/" + getName();// @todo
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#getCrudPackageName()
     */
    protected java.lang.String handleGetCrudPackageName()
    {
        String crudPackageName = "";

        final String parentPackage = super.getPackageName();
        if (parentPackage != null && parentPackage.length() > 0)
        {
            crudPackageName = parentPackage + ".";
        }

        return crudPackageName += "crud";
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#getCreateControllerName()
     */
    protected java.lang.String handleGetCreateControllerName()
    {
        return "Create" + getName() + "Controller";
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#getAssociatedEntities()
     */
    protected java.util.Collection handleGetAssociatedEntities()
    {
        final Collection associatedEntities = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade) associationEndIterator.next();
            final AssociationEndFacade otherEnd = associationEnd.getOtherEnd();

            if (otherEnd.isNavigable())
            {
                final ClassifierFacade otherEndType = otherEnd.getType();
                if (otherEndType instanceof Entity)
                {
                    associatedEntities.add(otherEndType);
                }
            }
        }

        return associatedEntities;
    }

    /**
     * @see org.andromda.metafacades.uml.CrudEntity#isCreate()
     */
    protected boolean handleIsCreate()
    {
        return true;
    }

    protected String handleGetServiceAccessorCall()
    {
        return "SERVICE_CALL";
    }

    protected boolean handleIsRead()
    {
        return true;
    }

    protected boolean handleIsUpdate()
    {
        return true;
    }

    protected boolean handleIsDelete()
    {
        return true;
    }
}
