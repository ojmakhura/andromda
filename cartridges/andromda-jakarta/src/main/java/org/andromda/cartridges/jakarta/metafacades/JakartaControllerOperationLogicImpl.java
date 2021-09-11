package org.andromda.cartridges.jakarta.metafacades;

import java.util.Objects;

import org.andromda.cartridges.jakarta.JakartaGlobals;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation
 */
public class JakartaControllerOperationLogicImpl
    extends JakartaControllerOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return formName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormName()
     */
    protected String handleGetFormName()
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(JakartaGlobals.FORM_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedFormName
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFullyQualifiedFormName()
     */
    protected String handleGetFullyQualifiedFormName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(StringUtils.capitalize(this.getFormName())).toString();
    }

    /**
     * @return getFullyQualifiedFormName().replace('.', '/')
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFullyQualifiedFormPath()
     */
    protected String handleGetFullyQualifiedFormPath()
    {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    /**
     * @return formCall
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormCall()
     */
    protected String handleGetFormCall()
    {
        final StringBuilder call = new StringBuilder();
        call.append(this.getName());
        call.append("(");
        if (!this.getFormFields().isEmpty())
        {
            call.append("form");
        }
        call.append(")");
        return call.toString();
    }

    /**
     * @return getFormSignature(false)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getImplementationFormSignature()
     */
    protected String handleGetImplementationFormSignature()
    {
        return this.getFormSignature(false);
    }

    /**
     * @return getFormSignature(true)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaControllerOperation#getFormSignature()
     */
    protected String handleGetFormSignature()
    {
        return this.getFormSignature(true);
    }

    /**
     * Constructs the signature that takes the form for this operation.
     *
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    private String getFormSignature(boolean isAbstract)
    {
        final StringBuilder signature = new StringBuilder();
        signature.append(this.getVisibility() + ' ');
        if (isAbstract)
        {
            signature.append("abstract ");
        }
        final ModelElementFacade returnType = this.getReturnType();
        signature.append(returnType != null ? returnType.getFullyQualifiedName() : null);
        signature.append(" " + this.getName() + "(");
        if (!this.getFormFields().isEmpty())
        {
            signature.append(this.getFormName() + " form");
        }
        signature.append(")");
        return signature.toString();
    }
}
