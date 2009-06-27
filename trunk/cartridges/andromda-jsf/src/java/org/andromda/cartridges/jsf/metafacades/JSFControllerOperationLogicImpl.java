package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.metafacades.JSFControllerOperationLogic;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFControllerOperation.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation
 */
public class JSFControllerOperationLogicImpl
    extends JSFControllerOperationLogic
{

    public JSFControllerOperationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormName()
     */
    protected java.lang.String handleGetFormName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.FORM_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFullyQualifiedFormName()
     */
    protected java.lang.String handleGetFullyQualifiedFormName()
    {
        final StringBuffer fullyQualifiedName = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(StringUtils.capitalize(this.getFormName())).toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFullyQualifiedFormPath()
     */
    protected java.lang.String handleGetFullyQualifiedFormPath()
    {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormCall()
     */
    protected String handleGetFormCall()
    {
        final StringBuffer call = new StringBuffer();
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
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getmplementationFormSignature()
     */
    protected String handleGetImplementationFormSignature()
    {
       return this.getFormSignature(false);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormSignature()
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
    private final String getFormSignature(boolean isAbstract)
    {
        final StringBuffer signature = new StringBuffer();
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
        signature.append(") throws java.lang.Exception");
        return signature.toString();
    }
}