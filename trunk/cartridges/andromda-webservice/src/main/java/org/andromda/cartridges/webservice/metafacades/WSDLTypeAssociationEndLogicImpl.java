package org.andromda.cartridges.webservice.metafacades;

import java.util.Collection;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.translation.ocl.validation.OCLIntrospector;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd.
 *
 * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd
 * @author Bob Fields
 */
public class WSDLTypeAssociationEndLogicImpl
        extends WSDLTypeAssociationEndLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public WSDLTypeAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return !this.isRequired()
     * @see org.andromda.cartridges.webservice.metafacades.WSDLTypeAssociationEnd#isNillable()
     */
    protected boolean handleIsNillable()
    {
        return !this.isRequired();
    }

    /**
     * <p><b>Constraint:</b> org::andromda::cartridges::webservice::metafacades::WSDLTypeAssociationEnd::association end must start with a lowercase letter</p>
     * <p><b>Error:</b> AssociationEnd name must start with a lowercase letter.</p>
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        super.validateInvariants(validationMessages);
        try
        {
            final Object contextElement = this.THIS();
            final String name = (String)OCLIntrospector.invoke(contextElement,"name");
            if (name != null && name.length()>0 && !StringUtilsHelper.startsWithLowercaseLetter(name))
            {
                validationMessages.add(
                    new ModelValidationMessage(
                        (MetafacadeBase)contextElement ,
                        "org::andromda::cartridges::webservice::metafacades::WSDLTypeAssociationEnd::association end must start with a lowercase letter",
                        "AssociationEnd name must start with a lowercase letter."));
            }
        }
        catch (Throwable th)
        {
            Throwable cause = th.getCause();
            int depth = 0; // Some throwables have infinite recursion
            while (cause != null && depth < 7)
            {
                th = cause;
                depth++;
            }
            logger.error("Error validating constraint 'org::andromda::cartridges::webservice::WSDLTypeAssociationEnd::association end must start with a lowercase letter' ON "
                + this.THIS().toString() + ": " + th.getMessage(), th);
        }
    }
}
