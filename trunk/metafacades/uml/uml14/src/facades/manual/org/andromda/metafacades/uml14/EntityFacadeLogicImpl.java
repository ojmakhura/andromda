package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.*;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityAttributeFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class EntityFacadeLogicImpl
       extends EntityFacadeLogic
       implements org.andromda.metafacades.uml.EntityFacade
{
    // ---------------- constructor -------------------------------
    
    public EntityFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityFacade ...

    // ------------- relations ------------------
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders
     */
    public java.util.Collection getFinders() {
        return this.getFinders(false);
    }   
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getFinders(boolean)
     */
    public java.util.Collection getFinders(boolean follow) {
        Collection finders = this.getOperations();
        
        MetafacadeUtils.filterByStereotype(
                finders, 
                UMLProfile.STEREOTYPE_FINDER_METHOD);
        
        for (ClassifierFacade superClass = (ClassifierFacade) getGeneralization();
             superClass != null && follow;
             superClass = (ClassifierFacade) superClass.getGeneralization()) {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
                EntityFacade entity = (EntityFacade)superClass;
                finders.addAll(entity.getFinders(follow));
            }               
        }
        return finders;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers()
     */
    public java.util.Collection getIdentifiers() {
        return this.getIdentifiers(false);
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers(boolean)
     */
    public java.util.Collection getIdentifiers(boolean follow) {
        
        Collection attributes = this.getAttributes();       
        MetafacadeUtils.filterByStereotype(
                attributes, 
                UMLProfile.STEREOTYPE_IDENTIFIER);
        
        for (ClassifierFacade superClass = (ClassifierFacade) getGeneralization();
             superClass != null && attributes.isEmpty() && follow;
             superClass = (ClassifierFacade) superClass.getGeneralization()) {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
                EntityFacade entity = (EntityFacade)superClass;
                attributes = entity.getIdentifiers(follow);
            }               
        }

        return attributes;
    }   
    
    /**
     * @see edu.duke.dcri.mda.model.metafacade.EntityFacade#hasIdentifiers()
     */
    public boolean hasIdentifiers() {
        return this.getIdentifiers() != null && !this.getIdentifiers().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getTableName()
     */
    public String getTableName() {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                this, 
                UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                this.getMaxSqlNameLength());
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributesAsList(boolean, boolean)
     */
    public String getAttributesAsList(boolean withTypeNames, boolean withIdentifiers) {
        return this.getAttributesAsList(withTypeNames, withIdentifiers, false);
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getAttributesAsList(boolean, boolean, boolean)
     */
    public String getAttributesAsList(
        boolean withTypeNames, 
        boolean withIdentifiers, 
        boolean follow) {
        StringBuffer buffer = new StringBuffer();
        String separator = "";
        buffer.append("(");
        
        Collection attributes = this.getAttributes();
        
        for (ClassifierFacade superClass = (ClassifierFacade) getGeneralization();
        	superClass != null && follow;
        	superClass = (ClassifierFacade) superClass.getGeneralization()) {
            if (superClass.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
                EntityFacade entity = (EntityFacade)superClass;
                attributes.addAll(entity.getAttributes());
            }
       	}               
        
        if (attributes != null && !attributes.isEmpty()) {
            Iterator attributeIt = attributes.iterator();
            while (attributeIt.hasNext()) {
                EntityAttributeFacade attribute = 
                    (EntityAttributeFacade)attributeIt.next();
                if (withIdentifiers || !attribute.isIdentifier()) {
                    buffer.append(separator);
                    if (withTypeNames) {
                        String typeName = attribute.getType().getFullyQualifiedName();
                        buffer.append(typeName);
                        buffer.append(" ");
                        buffer.append(attribute.getName());    
                    } else {
                        buffer.append(attribute.getGetterName());
                        buffer.append("()");
                    }
                    separator = ", ";
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
    
    /**
     * SQL type specific mappings property reference.
     */
    private final static String MAX_SQL_NAME_LENGTH = "maxSqlNameLength";
    
    /**
     * Sets the maximum lenght to which a persistent SQL
     * name may be.
     * 
     * @param maxSqlNameLength the maximum length a SQL name 
     *        may be.
     */
    public void setMaxSqlNameLength(Short maxSqlNameLength) {
        this.registerConfiguredProperty(MAX_SQL_NAME_LENGTH, maxSqlNameLength);
    }
    
    /**
     * Gets the maximum name length SQL names may be 
     */
    public Short getMaxSqlNameLength() {
        return (Short)this.getConfiguredProperty(MAX_SQL_NAME_LENGTH);
    }

}
