package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacade
 */
public class EJB3ManageableEntityFacadeLogicImpl
    extends EJB3ManageableEntityFacadeLogic
{
    /**
     * The property which stores the pattern defining the manageable create exception name.
     */
    public static final String CREATE_EXCEPTION_NAME_PATTERN = "manageableCreateExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable read exception name.
     */
    public static final String READ_EXCEPTION_NAME_PATTERN = "manageableReadExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable update exception name.
     */
    public static final String UPDATE_EXCEPTION_NAME_PATTERN = "manageableUpdateExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable delete exception name.
     */
    public static final String DELETE_EXCEPTION_NAME_PATTERN = "manageableDeleteExceptionNamePattern";
    
    /**
     * The property that stores the JNDI name prefix.
     */
    public static final String SERVICE_JNDI_NAME_PREFIX = "jndiNamePrefix";

    /**
     * The property that stores the manageable service base name pattern
     */
    public static final String MANAGEABLE_SERVICE_BASE_NAME_PATTERN = "manageableServiceBaseNamePattern";
    
    /**
     * Constructor
     * 
     * @param metaObject
     * @param context
     */
    public EJB3ManageableEntityFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceCreateExceptionName()
     */
    protected String handleGetManageableServiceCreateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(CREATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceCreateExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceCreateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceCreateExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceReadExceptionName()
     */
    protected String handleGetManageableServiceReadExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(READ_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceReadExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceReadExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceReadExceptionName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceUpdateExceptionName()
     */
    protected String handleGetManageableServiceUpdateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(UPDATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceUpdateExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceUpdateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceUpdateExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceDeleteExceptionName()
     */
    protected String handleGetManageableServiceDeleteExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(DELETE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceDeleteExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceDeleteExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceDeleteExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetDefaultPersistenceContextUnitName()
     */
    protected String handleGetDefaultPersistenceContextUnitName()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.PERSISTENCE_CONTEXT_UNIT_NAME)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#handleGetJndiNamePrefix()
     */
    protected String handleGetJndiNamePrefix()
    {
        return this.isConfiguredProperty(SERVICE_JNDI_NAME_PREFIX) ? 
                ObjectUtils.toString(this.getConfiguredProperty(SERVICE_JNDI_NAME_PREFIX)) : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceBaseName()
     */
    protected String handleGetManageableServiceBaseName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(MANAGEABLE_SERVICE_BASE_NAME_PATTERN);

        return MessageFormat.format(
            exceptionNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getManageableServiceName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceBaseFullPath()
     */
    protected String handleGetManageableServiceBaseFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableServiceBaseName(), ".", "/");
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceBaseName()
     */
    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceBaseName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#isDeleteWorkaround()
     */
    protected boolean handleIsDeleteWorkaround()
    {
        return (this.getIdentifiers(true).iterator().next() != null ? true : false);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#isUpdateWorkaround()
     */
    protected boolean handleIsUpdateWorkaround()
    {
        return (this.getIdentifiers(true).iterator().next() != null ? true : false);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#getManageableIdentifierWorkaround()
     */
    protected EntityAttribute handleGetManageableIdentifierWorkaround()
    {
        return (EntityAttribute)this.getIdentifiers(true).iterator().next();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#handleGetDisplayAttributeWorkaround()
     */
    protected AttributeFacade handleGetDisplayAttributeWorkaround()
    {
        AttributeFacade displayAttribute = null;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = findAttribute(StringUtils.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection attributes = getAttributes(true);
        for (final Iterator attributeIterator = attributes.iterator();
            attributeIterator.hasNext() && displayAttribute == null;)
        {
            final EntityAttribute attribute = (EntityAttribute)attributeIterator.next();
            if (attribute.isUnique())
            {
                displayAttribute = attribute;
            }
        }

        if (displayAttribute == null)
        {
            if (!getIdentifiers().isEmpty())
            {
                displayAttribute = (EntityAttribute)getIdentifiers().iterator().next();
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = (EntityAttribute)attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

}