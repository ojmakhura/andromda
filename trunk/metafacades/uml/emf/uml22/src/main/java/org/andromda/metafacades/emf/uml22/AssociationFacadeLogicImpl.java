package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationFacade
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AssociationFacadeLogicImpl(
        final Association metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    @Override
    protected String handleGetRelationName()
    {
        final Collection<AssociationEndFacade> ends = this.getAssociationEnds();
        final Iterator<AssociationEndFacade> endIt = ends.iterator();
        final AssociationEndFacade firstEnd = (AssociationEndFacade)endIt.next();
        final AssociationEndFacade secondEnd = (AssociationEndFacade)endIt.next();
        return MetafacadeUtils.toRelationName(
            firstEnd==null ? "" :firstEnd.getName(),
            secondEnd==null ? "" : secondEnd.getName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR)));
    }

    /**
     * Overridden: A name may not have a name, which
     * is problematic for getSqlName (for an EntityAssociation).
     * We use the relation name as default
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetName()
     */
    @Override
    public String handleGetName() {
        String name = super.handleGetName();

        // if the name isn't defined, use the relation name
        if (StringUtils.isEmpty(name)) {
            name = this.getRelationName();
        }
        return name;
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
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEnds()
     */
    @Override
    protected List<AssociationEndFacade> handleGetAssociationEnds()
    {
        return (List)CollectionUtils.collect(
            this.metaObject.getMemberEnds(),
            UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAssociationClass()
     */
    @Override
    protected boolean handleIsAssociationClass()
    {
        // TODO: Test this if it works.
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndA()
     */
    @Override
    protected AssociationEndFacade handleGetAssociationEndA()
    {
        if (!this.getAssociationEnds().isEmpty())
        {
            return this.getAssociationEnds().get(0);
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndB()
     */
    @Override
    protected AssociationEndFacade handleGetAssociationEndB()
    {
        if (this.getAssociationEnds().size()>1)
        {
            return this.getAssociationEnds().get(1);
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
     * @see org.andromda.metafacades.uml.AssociationFacade#isBinary()
     */
    @Override
    protected boolean handleIsBinary()
    {
        return this.metaObject.isBinary();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isDerived()
     */
    @Override
    protected boolean handleIsDerived()
    {
        return this.metaObject.isDerived();
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
