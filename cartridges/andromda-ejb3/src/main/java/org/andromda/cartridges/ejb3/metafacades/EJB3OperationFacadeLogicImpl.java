package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade
 */
public class EJB3OperationFacadeLogicImpl
    extends EJB3OperationFacadeLogic
{

    // ---------------- constructor -------------------------------
	
    public EJB3OperationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade#isBusinessOperation()
     */
    protected boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJB3Profile.STEREOTYPE_CREATE_METHOD) &&
                !this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_METHOD) &&
                !this.hasStereotype(EJB3Profile.STEREOTYPE_SELECT_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade#isSelectMethod()
     */
    protected boolean handleIsSelectMethod()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SELECT_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPrePersist()
     */
    protected boolean handleIsPrePersist()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_PERSIST);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPostPersist()
     */
    protected boolean handleIsPostPersist()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_PERSIST);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPreRemove()
     */
    protected boolean handleIsPreRemove()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_REMOVE);
    }

    /*
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPostRemove()
     */
    protected boolean handleIsPostRemove()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_REMOVE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPreUpdate()
     */
    protected boolean handleIsPreUpdate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_UPDATE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPostUpdate()
     */
    protected boolean handleIsPostUpdate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_UPDATE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsPostLoad()
     */
    protected boolean handleIsPostLoad()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_LOAD);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleIsLifecycleCallback()
     */
    protected boolean handleIsLifecycleCallback()
    {
        return this.isPostLoad() || 
                this.isPostPersist() ||
                this.isPostRemove() || 
                this.isPostUpdate() ||
                this.isPrePersist() ||
                this.isPreRemove() ||
                this.isPreUpdate();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleGetImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleGetImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacadeLogic#handleGetImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * EJB3Globals#IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        String implementationNamePattern = 
            (String)this.getConfiguredProperty(EJB3Globals.IMPLEMENTATION_OPERATION_NAME_PATTERN);

        return MessageFormat.format(
                implementationNamePattern,
                new Object[] {StringUtils.trimToEmpty(replacement)});
    }
}