package org.andromda.cartridges.jsf2.metafacades;

import java.util.Objects;

import org.andromda.cartridges.jsf2.JSFGlobals;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation
 */
public class JSFControllerOperationLogicImpl
    extends JSFControllerOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFControllerOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return formName
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getFormName()
     */
    protected String handleGetFormName()
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(JSFGlobals.FORM_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedFormName
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getFullyQualifiedFormName()
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
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getFullyQualifiedFormPath()
     */
    protected String handleGetFullyQualifiedFormPath()
    {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    /**
     * @return formCall
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getFormCall()
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
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getImplementationFormSignature()
     */
    protected String handleGetImplementationFormSignature()
    {
        return this.getFormSignature(false);
    }

    /**
     * @return getFormSignature(true)
     * @see org.andromda.cartridges.jsf2.metafacades.JSFControllerOperation#getFormSignature()
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
