package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.*;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.apache.commons.lang.StringUtils;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class AssociationFacadeLogicImpl
       extends AssociationFacadeLogic
       implements org.andromda.metafacades.uml.AssociationFacade
{
    // ---------------- constructor -------------------------------
    
    public AssociationFacadeLogicImpl (org.omg.uml.foundation.core.UmlAssociation metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#handleGetAssociationEnds()
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return metaObject.getConnection();
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName() {
        String name = super.getName();      
        // if the name isn't defined, use the relation name
    	if (StringUtils.isEmpty(name)) {
    		name = this.getRelationName();
        }
        return name;
    }
    
    /**
     * 
     * @see org.andromda.metafacades.uml14.AssociationFacadeLogic#getRelationName()
     */
    public String handleGetRelationName() {
        Collection ends = this.getAssociationEnds();
        Iterator endIt = ends.iterator();
        AssociationEndFacade firstEnd = (AssociationEndFacade)endIt.next();
        AssociationEndFacade secondEnd = (AssociationEndFacade)endIt.next();
        String relationName = MetafacadeUtils.toRelationName(firstEnd.getName(), secondEnd.getName(), "-");
        return relationName;
    }

}
