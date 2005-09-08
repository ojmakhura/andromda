package org.andromda.cartridges.jsf.renderkit.html;

import java.io.IOException;

import java.util.Iterator;
import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import javax.servlet.http.HttpServletRequest;

import org.andromda.cartridges.jsf.Constants;
import org.andromda.cartridges.jsf.component.html.HtmlPopupFrame;


/**
 * A custom renderer for rendering a popup frame.
 */
public class PopupRenderer
    extends Renderer
{
    public static final String POPUP_FRAME_HIDDEN = "hiddenPopupFrame";

    /**
     * Retrieves the current request instance.
     *
     * @return the current request.
     */
    private HttpServletRequest getRequest()
    {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * Retrieve the popup resource path (the path within the
     * deployed web application) given the relative <code>path</code>
     *
     * @param path the relative path.
     * @return the complete path including the context path of the application.
     */
    private String getPopupResourcePath(final String path)
    {
        return getRequest().getContextPath() + Constants.RESOURCE_CONTEXT + path;
    }

    /**
     * keeps track of whether or not the javascript has been rendered.
     */
    private static final String JS_ATTRIBUTE = "andromda.jsf.js";

    protected void commonJavascript(
        final FacesContext context,
        final UIComponent component)
        throws IOException
    {
        final ResponseWriter writer = context.getResponseWriter();
        if (this.getRequest().getAttribute(JS_ATTRIBUTE) == null)
        {
            getRequest().setAttribute(
                JS_ATTRIBUTE,
                JS_ATTRIBUTE);
            writer.startElement(
                "script",
                component);
            writer.writeAttribute(
                "language",
                "JavaScript",
                null);
            writer.writeAttribute(
                "src",
                getPopupResourcePath("/popup/js/popup.js"),
                null);
            writer.endElement("script");
        }
        writer.startElement(
            "input",
            null);
        writer.writeAttribute(
            "type",
            "hidden",
            null);
        writer.writeAttribute(
            "name",
            POPUP_FRAME_HIDDEN,
            null);
        writer.writeAttribute(
            "value",
            "",
            null);
        writer.endElement("input");
    }

    private static final String DEFAULT_STYLE =
        "position:absolute; left:0; top:0; visibility:hidden; border:1px solid black; background-color:#FFFFFF; ";

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void decode(
        final FacesContext context,
        final UIComponent component)
    {
        final HtmlPopupFrame command = (HtmlPopupFrame)component;
        final Map parameters = context.getExternalContext().getRequestParameterMap();
        final String popupAction = (String)parameters.get(PopupRenderer.POPUP_FRAME_HIDDEN);
        if (popupAction != null && popupAction.equals(getHiddenFieldOpen(
                    command,
                    context)))
        {
            final MethodBinding binding = command.getActionOpen();
            command.setAction(binding);
            final ActionEvent actionEvent = new ActionEvent(command);
            if (command.isImmediate())
            {
                command.queueEventImmediate(actionEvent);
            }
            else
            {
                command.queueEventNormal(actionEvent);
            }
        }
        else if (popupAction != null && popupAction.equals(getHiddenFieldClose(
                    command,
                    context)))
        {
            final MethodBinding binding = command.getActionClose();
            if (binding != null)
            {
                command.setAction(binding);
                ActionEvent actionEvent = new ActionEvent(command);
                command.queueEventImmediate(actionEvent);
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        return true;
    }

    private String getHiddenFieldOpen(
        HtmlPopupFrame command,
        FacesContext context)
    {
        return command.getClientId(context) + NamingContainer.SEPARATOR_CHAR + UIViewRoot.UNIQUE_ID_PREFIX + "op";
    }

    private String getHiddenFieldClose(
        HtmlPopupFrame command,
        FacesContext context)
    {
        return command.getClientId(context) + NamingContainer.SEPARATOR_CHAR + UIViewRoot.UNIQUE_ID_PREFIX + "cl";
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(
        FacesContext context,
        UIComponent component)
        throws IOException
    {
        final HtmlPopupFrame command = (HtmlPopupFrame)component;
        if (command.isRendered())
        {
            final UIForm uiform = getForm(
                    context,
                    command);
            if (uiform == null)
            {
                throw new RuntimeException("JSF h:form needed to use this component");
            }

            final String formClientId = uiform.getClientId(context);

            // start differences from command link
            final ResponseWriter writer = context.getResponseWriter();
            commonJavascript(
                context,
                command);

            writer.startElement(
                "a",
                command);
            writer.writeAttribute(
                "href",
                "#",
                null);

            String form = "document.forms['" + formClientId + "']";

            StringBuffer buffer = new StringBuffer();
            buffer.append("showPopupFrame(");
            buffer.append(form);
            buffer.append(",this,event");
            buffer.append(",'");
            buffer.append(getHiddenFieldClose(
                    command,
                    context));
            buffer.append("','");
            buffer.append(command.getStyleClassFrame() == null ? "" : command.getStyleClassFrame());
            buffer.append("','");
            buffer.append(DEFAULT_STYLE);
            buffer.append(command.getStyleFrame() == null ? "" : command.getStyleFrame());
            buffer.append("',");
            buffer.append(command.getMouseHorizPos() == null ? "0" : command.getMouseHorizPos());
            buffer.append(",");
            buffer.append(command.getMouseVertPos() == null ? "0" : command.getMouseVertPos());
            buffer.append(",");
            buffer.append(command.getAbsolute() == null ? "false" : command.getAbsolute());
            buffer.append(",");
            buffer.append(command.getCenter() == null ? "false" : command.getCenter());
            buffer.append(",'");
            buffer.append(command.getHeight() == null ? "" : command.getHeight());
            buffer.append("','");
            buffer.append(command.getWidth() == null ? "" : command.getWidth());
            buffer.append("','");
            buffer.append(command.getScrolling() == null ? "auto" : command.getScrolling().toLowerCase());
            buffer.append("');");

            buffer.append(form);
            buffer.append(".target='");
            buffer.append("hiddenPopupFrameTarget");
            buffer.append("';");

            buffer.append(form);
            buffer.append(".elements['");
            buffer.append(POPUP_FRAME_HIDDEN);
            buffer.append("'].value='");
            buffer.append(getHiddenFieldOpen(
                    command,
                    context));
            buffer.append("';");

            buffer.append(form);
            buffer.append(".submit();");
            buffer.append(form);
            buffer.append(".elements['");
            buffer.append(POPUP_FRAME_HIDDEN);
            buffer.append("'].value='';");

            buffer.append(form);
            buffer.append(".target='';");

            buffer.append("return false;");

            writer.writeAttribute(
                "onclick",
                buffer.toString(),
                null);

            writer.writeAttribute(
                "id",
                command.getClientId(context),
                null);

            final String accesskey = command.getAccesskey();
            if (accesskey != null)
            {
                writer.writeAttribute(
                    "accesskey",
                    accesskey,
                    "accesskey");
            }

            final String directory = command.getDir();
            if (directory != null)
            {
                writer.writeAttribute(
                    "dir",
                    directory,
                    "dir");
            }

            final String lang = command.getLang();
            if (lang != null)
            {
                writer.writeAttribute(
                    "lang",
                    lang,
                    "lang");
            }

            final String tabindex = command.getTabindex();
            if (tabindex != null)
            {
                writer.writeAttribute(
                    "tabindex",
                    tabindex,
                    "tabindex");
            }

            final String title = command.getTitle();
            if (title != null)
            {
                writer.writeAttribute(
                    "title",
                    title,
                    "title");
            }

            final String styleClass = command.getStyleClass();
            if (styleClass != null)
            {
                writer.writeAttribute(
                    "class",
                    styleClass,
                    "styleClass");
            }

            final String style = command.getStyle();
            if (style != null)
            {
                writer.writeAttribute(
                    "style",
                    style,
                    "style");
            }

            String label = null;
            final Object value = ((UICommand)component).getValue();
            if (value != null)
            {
                label = value.toString();
            }
            if (label != null && label.length() != 0)
            {
                writer.write(label);
            }
            writer.flush();
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(
        final FacesContext context,
        final UIComponent component)
        throws IOException
    {
        // - only render if rendered is true
        if (component.isRendered())
        {
            for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();)
            {
                final UIComponent child = (UIComponent)iterator.next();
                child.encodeBegin(context);
                if (child.getRendersChildren())
                {
                    child.encodeChildren(context);
                }
                child.encodeEnd(context);
            }
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(
        final FacesContext context,
        final UIComponent component)
        throws IOException
    {
        final UICommand command = (UICommand)component;

        // - only render if rendered is true
        if (command.isRendered())
        {
            final ResponseWriter writer = context.getResponseWriter();

            // - complete writing Anchor element
            writer.endElement("a");
            writer.flush();
        }
    }

    /**
     * @see javax.faces.render.Renderer#convertClientId(javax.faces.context.FacesContext, java.lang.String)
     */
    public String convertClientId(
        final FacesContext context,
        final String clientId)
    {
        return clientId;
    }

    /**
     * Gets the form to which the <code>component</code> belongs
     * or null if the form can not be found.
     *
     * @param context the faces context.
     * @param component the component.
     * @return the form.
     */
    protected UIForm getForm(
        final FacesContext context,
        final UIComponent component)
    {
        UIComponent parent = component.getParent();
        while (parent != null)
        {
            if (parent instanceof UIForm)
            {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm)parent;
    }
}