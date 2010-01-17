package org.andromda.cartridges.jsf.renderkit;

import java.io.IOException;
import java.util.UUID;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.andromda.cartridges.jsf.component.TransactionToken;


/**
 * The transaction token renderer (just adds the transaction token value
 * as an attribute of the parent component so we can skip multi-submits of the same action).
 *
 * @author Chad Brandon
 */
public class TransactionTokenRenderer
    extends Renderer
{
    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    @SuppressWarnings("unchecked")
    public void encodeBegin(
        FacesContext context,
        UIComponent component)
        throws IOException
    {
        if (component.getParent() != null)
        {
            component.getParent().getAttributes().put(TransactionToken.TRANSACTION_TOKEN, UUID.randomUUID().toString());
        }
    }
}