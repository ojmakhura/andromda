package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.metafacades.uml.EntityAttribute;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacade.
 *
 * @see EJB3ManageableEntityFacade
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
    public EJB3ManageableEntityFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceCreateExceptionName()
     */
    @Override
    protected String handleGetManageableServiceCreateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(CREATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetFullyQualifiedManageableServiceCreateExceptionName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceCreateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceCreateExceptionName(),
                null);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceReadExceptionName()
     */
    @Override
    protected String handleGetManageableServiceReadExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(READ_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetFullyQualifiedManageableServiceReadExceptionName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceReadExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceReadExceptionName(),
                null);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceUpdateExceptionName()
     */
    @Override
    protected String handleGetManageableServiceUpdateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(UPDATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetFullyQualifiedManageableServiceUpdateExceptionName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceUpdateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceUpdateExceptionName(),
                null);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceDeleteExceptionName()
     */
    @Override
    protected String handleGetManageableServiceDeleteExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(DELETE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetFullyQualifiedManageableServiceDeleteExceptionName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceDeleteExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceDeleteExceptionName(),
                null);
    }

    /**
     * @return getConfiguredProperty(EJB3Globals.PERSISTENCE_CONTEXT_UNIT_NAME)
     * @see EJB3EntityFacadeLogic#handleGetDefaultPersistenceContextUnitName()
     */
    protected String handleGetDefaultPersistenceContextUnitName()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.PERSISTENCE_CONTEXT_UNIT_NAME)));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetJndiNamePrefix()
     */
    @Override
    protected String handleGetJndiNamePrefix()
    {
        return this.isConfiguredProperty(SERVICE_JNDI_NAME_PREFIX) ?
                ObjectUtils.toString(this.getConfiguredProperty(SERVICE_JNDI_NAME_PREFIX)) : null;
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceBaseName()
     */
    @Override
    protected String handleGetManageableServiceBaseName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(MANAGEABLE_SERVICE_BASE_NAME_PATTERN);

        return MessageFormat.format(
            exceptionNamePattern,
                StringUtils.trimToEmpty(this.getManageableServiceName()));
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableServiceBaseFullPath()
     */
    @Override
    protected String handleGetManageableServiceBaseFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableServiceBaseName(), ".", "/");
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetFullyQualifiedManageableServiceBaseName()
     */
    @Override
    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceBaseName(),
                null);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#handleGetManageableRolesAllowed()
     */
    @Override
    protected String handleGetManageableRolesAllowed()
    {
        StringBuilder rolesAllowed = null;
        String[] roles = StringUtils.split(
                StringUtils.trimToEmpty(
                        ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.MANAGEABLE_ROLES_ALLOWED))),
                ',');
        String separator = "";

        for (int i = 0; i < roles.length; i++)
        {
            if (rolesAllowed == null)
            {
                rolesAllowed = new StringBuilder();
            }
            rolesAllowed.append(separator);
            rolesAllowed.append('"');
            rolesAllowed.append(roles[i]);
            rolesAllowed.append('"');
            separator = ", ";
        }
        return rolesAllowed != null ? rolesAllowed.toString() : null;
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#getIdentifier()
     *
     * Override the implementation in EJB3EntityFacade as UML2 models cannot retrieve the identifier via the
     * super EJB3EntityFacade.
     */
    @Override
    public EJB3EntityAttributeFacade getIdentifier()
    {
        return (EJB3EntityAttributeFacade)super.getIdentifiers().iterator().next();
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#getAllInstanceAttributes()
     *
     * Override the implementation in EJB3EntityFacade as UML2 models will not get an
     * EJB3ManageableEntityAttributeFacade when retrieving the attributes.
     */
    @Override public List getAllInstanceAttributes()
    {
        return EJB3MetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#getInheritedInstanceAttributes()
     *
     * Override the implementation in EJB3EntityFacade as UML2 models will not get an
     * EJB3ManageableEntityAttributeFacade when retrieving the attributes.
     */
    @Override
    public List getInheritedInstanceAttributes()
    {
        return EJB3MetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    /**
     * @see EJB3ManageableEntityFacadeLogic#getInstanceAttributes(boolean, boolean)
     *
     * Override the implementation in EJB3EntityFacade as UML2 models will not get an
     * EJB3ManageableEntityAttributeFacade when retrieving the attributes.
     */
    @Override
    public Collection getInstanceAttributes(final boolean follow, final boolean withIdentifiers)
    {
        final Collection attributes = this.getAttributes(follow, withIdentifiers);
        CollectionUtils.filter(
            attributes,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid = true;
                    if (object instanceof EntityAttribute)
                    {
                        valid = !((EntityAttribute)object).isStatic();
                    }
                    return valid;
                }
            });
        return attributes;
    }
}
