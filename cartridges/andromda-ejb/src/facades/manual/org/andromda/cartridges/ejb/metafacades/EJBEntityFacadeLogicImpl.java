package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.OperationFacade;


/**
 * <p>
 *  Represents an entity EJB.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class EJBEntityFacadeLogicImpl
       extends EJBEntityFacadeLogic
       implements org.andromda.cartridges.ejb.metafacades.EJBEntityFacade
{
    // ---------------- constructor -------------------------------
    
    public EJBEntityFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFacade#getIdentifiers()
     */
    public Collection getIdentifiers() {

        Collection identifiers = new ArrayList();
        Iterator iter = this.getDependencies().iterator();
        while (iter.hasNext()) {
            DependencyFacade dep =
                (DependencyFacade) iter.next();
            if (dep.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER)) {
                identifiers =
                    ((ClassifierFacade)dep.getTargetElement()).getInstanceAttributes();
                MetafacadeUtils.filterByStereotype(
                    identifiers, 
                    EJBProfile.STEREOTYPE_IDENTIFIER);
                return identifiers;
            }
        }

        // No PK dependency found - try a PK attribute
        if (super.getIdentifiers() != null && !super.getIdentifiers().isEmpty()) {
            AttributeFacade attr = (AttributeFacade)super.getIdentifiers().iterator().next();
            identifiers.add(attr);
            return identifiers;
        }

        // Still nothing found - recurse up the inheritance tree
        EJBEntityFacade decorator =
            (EJBEntityFacade) this.getGeneralization();
        return decorator.getIdentifiers();
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getAllEntityRelations()
     */
    public java.util.Collection getAllEntityRelations() {

        // Only concrete entities may have EJB relations. Return
        // an empty collection for everything else
        if (this.isAbstract()) {
            return Collections.EMPTY_LIST;
        }

        Collection result = new ArrayList();
        result.addAll(getEntityRelations());

        ClassifierFacade classifier = (ClassifierFacade)this.getGeneralization();
        while (classifier != null
            && classifier instanceof EJBEntityFacade
            && classifier.isAbstract()) {

            EJBEntityFacade entity = (EJBEntityFacade) classifier;
            result.add(entity.getEntityRelations());
            classifier = (ClassifierFacade)this.getGeneralization();
        }
        return result;
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBEntityFacade#getViewType()
     */
    public String getViewType() {
        return EJBMetafacadeUtils.getViewType(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getEntityRelations()
     */
    public java.util.Collection getEntityRelations() {

        Collection result = new ArrayList();

        Iterator i = this.getAssociationEnds().iterator();
        while (i.hasNext()) {
            EJBAssociationEndFacade assoc =
                (EJBAssociationEndFacade)i.next(); 
            ClassifierFacade target = assoc.getOtherEnd().getType();
            if (target instanceof EJBEntityFacade
                && assoc.getOtherEnd().isNavigable()) {
                // Check the integrity constraint
                String generateCmr =
                    assoc.getOtherEnd().getAssociation().findTaggedValue(
                      EJBProfile.TAGGEDVALUE_GENERATE_CMR);
                if (target.isAbstract()
                    && !"false".equalsIgnoreCase(generateCmr)) {
                    throw new IllegalStateException(
                        "Relation '"
                            + assoc.getAssociation().getName()
                            + "' has the abstract target '"
                            + target.getName()
                            + "'. Abstract targets are not allowed in EJB.");
                } else {
                    result.add(assoc);
                }
            }
        }
        return result;
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBEntityFacade#getAllInstanceAttributes()
     */
    public List getAllInstanceAttributes() {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);  
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBEntityFacade#getInheritedInstanceAttributes()
     */
    public List getInheritedInstanceAttributes() {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBEntityFacade#getCreateMethods(boolean)
     */
    public Collection getCreateMethods(boolean follow) {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metadecorators.uml14.EJBEntityFacade#getSelectMethods(boolean)
     */
    public Collection getSelectMethods(boolean follow) {
        Collection retval = new ArrayList();
        EJBEntityFacade entity = null;
        do {
            Collection ops = this.getOperations();
            for (Iterator i = ops.iterator(); i.hasNext();) {
                OperationFacade op = (OperationFacade) i.next();
                if (op.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD)) {
                    retval.add(op);
                }
            }
            if (follow) {
                entity = (EJBEntityFacade)this.getGeneralization();
            } else {
                break;
            }
        } while (entity != null);
        return retval;
    }
    
    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBEntityFacade#getHomeInterfaceName()
     */
    public String getHomeInterfaceName() {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

}
