package org.andromda.metafacades.uml14;

import java.util.List;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.AssociationClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.UmlAssociation;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AssociationFacadeLogicImpl(
        UmlAssociation metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEnds()
     */
    @Override
    public List<AssociationEnd> handleGetAssociationEnds()
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
            name = this.handleGetRelationName();
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    @Override
    public String handleGetRelationName()
    {
        //Avoid StackOverflow with getName calling shieldedElements.
        /* final String firstEnd = this.handleGetAssociationEndA().getName();
        String secondEnd = "";
        if (!(metaObject.getConnection().size()>1))
        {
            secondEnd = this.handleGetAssociationEndB().getName();
        }
        return MetafacadeUtils.toRelationName(
            (firstEnd==null ? "" : firstEnd),
            (secondEnd==null ? "" : secondEnd),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR))); */
        /*final AssociationEndFacade firstEnd = (AssociationEndFacade)this.shieldedElement(this.handleGetAssociationEndA());
        final AssociationEndFacade secondEnd = (AssociationEndFacade)this.shieldedElement(this.handleGetAssociationEndB());
        return MetafacadeUtils.toRelationName(
            firstEnd.getName(),
            secondEnd.getName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR))); */
        final AssociationEndFacade firstEnd = this.getAssociationEndA();
        final AssociationEndFacade secondEnd = this.getAssociationEndB();
        //System.out.println("firstEnd=" + firstEnd + " secondEnd=" + secondEnd);
        final String nameA = firstEnd==null ? "" :firstEnd.getName();
        final String nameB = secondEnd==null ? "" : secondEnd.getName();
        //System.out.println("nameA=" + nameA + " nameB=" + nameB);
        return MetafacadeUtils.toRelationName(
                nameA,
                nameB,
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR)));
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isMany2Many()
     */
    @Override
    protected boolean handleIsMany2Many()
    {
        return ((AssociationEndFacade)this.getAssociationEnds().iterator().next()).isMany2Many();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAssociationClass()
     */
    @Override
    protected boolean handleIsAssociationClass()
    {
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndA()
     */
    @Override
    protected AssociationEnd handleGetAssociationEndA()
    {
        List<AssociationEnd> ends = this.handleGetAssociationEnds();
        if (!ends.isEmpty())
        {
            return ends.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndB()
     */
    @Override
    protected AssociationEnd handleGetAssociationEndB()
    {
        List<AssociationEnd> ends = this.handleGetAssociationEnds();
        if (ends.size()>1)
        {
            return ends.get(1);
        }
        else
        {
            return null;
        }
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAbstract()
     */
    @Override
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * 
     * @return true always
     * @see org.andromda.metafacades.uml.AssociationFacade#isBinary()
     */
    @Override
    protected boolean handleIsBinary()
    {
        return true;
    }

    /**
     * 
     * @return false always
     * @see org.andromda.metafacades.uml.AssociationFacade#isDerived()
     */
    @Override
    protected boolean handleIsDerived()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isLeaf
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }
}