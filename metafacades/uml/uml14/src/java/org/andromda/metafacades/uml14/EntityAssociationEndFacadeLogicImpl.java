package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * <p>
 *  Represents an association end of an entity.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class EntityAssociationEndFacadeLogicImpl
       extends EntityAssociationEndFacadeLogic
       implements org.andromda.metafacades.uml.EntityAssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public EntityAssociationEndFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityAssociationEndFacade ...

    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityAssociationEndFacade#getColumnName()()
     */
    public java.lang.String getColumnName() {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            this, 
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
            ((EntityFacade)this.getType()).getMaxSqlNameLength(),
            this.getForeignKeySuffix());
    }   

    /**
     * The foreign key suffix.
     */
    private final static String FOREIGN_KEY_SUFFIX = "foreignKeySuffix";
    
    /**
     * Sets the suffix for foreign keys.
     * 
     * @param foreignKeySuffix the suffix for foreign keys 
     * (i.e. '_FK').
     */
    public void setForeignKeySuffix(String foreignKeySuffix) {
    	this.registerConfiguredProperty(FOREIGN_KEY_SUFFIX, foreignKeySuffix);
    }
    
    /**
     * Gets the maximum name length SQL names may be 
     */
    public String getForeignKeySuffix() {
        return (String)this.getConfiguredProperty(FOREIGN_KEY_SUFFIX);
    }
    

    // ------------- relations ------------------

}
