package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * Metaclass facade implementation.
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
    implements org.andromda.metafacades.uml.AssociationFacade
{
    public AssociationFacadeLogicImpl(
        org.omg.uml.foundation.core.UmlAssociation metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEnds()
     */
    public java.util.List handleGetAssociationEnds()
    {
        return metaObject.getConnection();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    public String handleGetName()
    {
        String name = super.handleGetName();

        // if the name isn't defined, use the relation name
        if (StringUtils.isEmpty(name))
        {
            name = this.getRelationName();
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    public String handleGetRelationName()
    {
        final Collection ends = this.getAssociationEnds();
        final Iterator endIt = ends.iterator();
        final AssociationEndFacade firstEnd = (AssociationEndFacade)endIt.next();
        final AssociationEndFacade secondEnd = (AssociationEndFacade)endIt.next();
        return MetafacadeUtils.toRelationName(
            firstEnd.getName(),
            secondEnd.getName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR)));
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        return ((AssociationEndFacade)this.getAssociationEnds().iterator().next()).isMany2Many();
    }
}