package org.andromda.cartridges.jsf.component;

import javax.faces.component.UIComponentBase;

/**
 * The transaction token component, basically just registers its renderer.
 *
 * @author Chad Brandon
 */
public class TransactionToken
    extends UIComponentBase
{
    /**
     * The name of the attribute that stores the action transaction token.
     */
    public static final String TRANSACTION_TOKEN = "AndroMDA_Transaction_Token";

    private static final String RENDERER_TYPE = "org.andromda.cartridges.jsf.TransactionToken";

    public TransactionToken()
    {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    /**
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily()
    {
        return RENDERER_TYPE;
    }
}