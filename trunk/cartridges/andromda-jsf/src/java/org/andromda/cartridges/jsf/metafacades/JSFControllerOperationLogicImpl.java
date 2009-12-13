package org.andromda.cartridges.jsf.metafacades;

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

    /**
     * Public constructor for JSFControllerOperationLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation
     */
    public JSFControllerOperationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return FormName
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormName()
     */
    protected String handleGetFormName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.FORM_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return FullyQualifiedFormName
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFullyQualifiedFormName()
     */
    protected String handleGetFullyQualifiedFormName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName).append('.');
        }
        return fullyQualifiedName.append(StringUtils.capitalize(this.getFormName())).toString();
    }

    /**
     * @return getFullyQualifiedFormName().replace('.', '/')
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFullyQualifiedFormPath()
     */
    protected String handleGetFullyQualifiedFormPath()
    {
        return this.getFullyQualifiedFormName().replace('.', '/');
    }

    /**
     * @return FormCall
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormCall()
     */
    protected String handleGetFormCall()
    {
        final StringBuilder call = new StringBuilder();
        call.append(this.getName());
        call.append('(');
        if (!this.getFormFields().isEmpty())
        {
            call.append("form");
        }
        call.append(')');
        return call.toString();
    }
    
    /**
     * @return getFormSignature(false)
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getImplementationFormSignature()
     */
    protected String handleGetImplementationFormSignature()
    {
       return this.getFormSignature(false);
    }
    
    /**
     * @return getFormSignature(true)
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getFormSignature()
     */
    protected String handleGetFormSignature()
    {
       return this.getFormSignature(true);
    }
    
    /**
     * Constructs the signature that takes the form for this operation.
     * @see org.andromda.cartridges.jsf.metafacades.JSFControllerOperation#getImplementationFormSignature()
     *  
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    private final String getFormSignature(boolean isAbstract)
    {
        final StringBuilder signature = new StringBuilder();
        signature.append(this.getVisibility()).append(' ');
        if (isAbstract)
        {
            signature.append("abstract ");
        }
        final ModelElementFacade returnType = this.getReturnType();
        signature.append(returnType != null ? returnType.getFullyQualifiedName() : null);
        signature.append(' ').append(this.getName()).append('(');
        if (!this.getFormFields().isEmpty())
        {
            signature.append(this.getFormName()).append(" form");
        }
        signature.append(") throws Exception");
        return signature.toString();
    }
}