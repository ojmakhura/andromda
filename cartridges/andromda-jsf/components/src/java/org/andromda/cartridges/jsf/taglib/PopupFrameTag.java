package org.andromda.cartridges.jsf.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import org.andromda.cartridges.jsf.component.html.HtmlPopupFrame;

/**
 *
 */
public class PopupFrameTag
    extends UIComponentBodyTag
{
    private String value;
    private String accesskey;
    private String dir;
    private String lang;
    private String tabindex;
    private String title;
    private String style;
    private String styleClass;
    private String actionOpen;
    private String actionClose;
    private String immediate;
    private String mouseHorizPos;
    private String mouseVertPos;
    private String styleFrame;
    private String styleClassFrame;
    private String absolute;
    private String center;
    private String height;
    private String width;
    private String scrolling;

    /**
     * @param valueIn
     */
    public void setValue(String valueIn)
    {
        this.value = valueIn;
    }

    /**
     * @param accesskeyIn
     */
    public void setAccesskey(String accesskeyIn)
    {
        this.accesskey = accesskeyIn;
    }

    /**
     * @param dirIn
     */
    public void setDir(String dirIn)
    {
        this.dir = dirIn;
    }

    /**
     * @param langIn
     */
    public void setLang(String langIn)
    {
        this.lang = langIn;
    }

    /**
     * @param tabindexIn
     */
    public void setTabindex(String tabindexIn)
    {
        this.tabindex = tabindexIn;
    }

    /**
     * @param titleIn
     */
    public void setTitle(String titleIn)
    {
        this.title = titleIn;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    @Override
    public String getRendererType()
    {
        return HtmlPopupFrame.RENDERER_TYPE;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    @Override
    public String getComponentType()
    {
        return HtmlPopupFrame.COMPONENT_TYPE;
    }

    /**
     * @param styleIn
     */
    public void setStyle(String styleIn)
    {
        this.style = styleIn;
    }

    /**
     * @param styleClassIn
     */
    public void setStyleClass(String styleClassIn)
    {
        this.styleClass = styleClassIn;
    }

    /**
     * @param absoluteIn
     */
    public void setAbsolute(String absoluteIn)
    {
        this.absolute = absoluteIn;
    }

    /**
     * @param actionCloseIn
     */
    public void setActionClose(String actionCloseIn)
    {
        this.actionClose = actionCloseIn;
    }

    /**
     * @param actionOpenIn
     */
    public void setActionOpen(String actionOpenIn)
    {
        this.actionOpen = actionOpenIn;
    }

    /**
     * @param centerIn
     */
    public void setCenter(String centerIn)
    {
        this.center = centerIn;
    }

    /**
     * @param immediateIn
     */
    public void setImmediate(String immediateIn)
    {
        this.immediate = immediateIn;
    }

    /**
     * @param mouseVertPosIn
     */
    public void setMouseVertPos(String mouseVertPosIn)
    {
        this.mouseVertPos = mouseVertPosIn;
    }

    /**
     * @param mouseHorizPosIn
     */
    public void setMouseHorizPos(String mouseHorizPosIn)
    {
        this.mouseHorizPos = mouseHorizPosIn;
    }

    /**
     * @param styleClassFrameIn
     */
    public void setStyleClassFrame(String styleClassFrameIn)
    {
        this.styleClassFrame = styleClassFrameIn;
    }

    /**
     * @param styleFrameIn
     */
    public void setStyleFrame(String styleFrameIn)
    {
        this.styleFrame = styleFrameIn;
    }

    /**
     * @param heightIn
     */
    public void setHeight(String heightIn)
    {
        this.height = heightIn;
    }

    /**
     * @param widthIn
     */
    public void setWidth(String widthIn)
    {
        this.width = widthIn;
    }

    /**
     * @param scrollingIn
     */
    public void setScrolling(String scrollingIn)
    {
        this.scrolling = scrollingIn;
    }

    private ValueBinding createValueBinding(final String valueIn)
    {
        return FacesContext.getCurrentInstance().getApplication().createValueBinding(valueIn);
    }

    private MethodBinding createMethodBinding(
        final String valueIn,
        Class[] args)
    {
        return FacesContext.getCurrentInstance().getApplication().createMethodBinding(
            valueIn,
            args);
    }

    @Override
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        HtmlPopupFrame command = null;
        try
        {
            command = (HtmlPopupFrame)component;
        }
        catch (ClassCastException cce)
        {
            throw new IllegalStateException("Component " + component.toString() +
                " not expected type.  Expected: UICommand.  Perhaps you're missing a tag?");
        }

        if (this.value != null)
        {
            if (isValueReference(this.value))
            {
                ValueBinding binding = createValueBinding(this.value);
                command.setValueBinding(
                    "value",
                    binding);
            }
            else
            {
                command.setValue(this.value);
            }
        }
        if (this.accesskey != null)
        {
            if (isValueReference(this.accesskey))
            {
                ValueBinding binding = createValueBinding(this.accesskey);
                command.setValueBinding(
                    "accesskey",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "accesskey",
                    this.accesskey);
            }
        }
        if (this.dir != null)
        {
            if (isValueReference(this.dir))
            {
                ValueBinding binding = createValueBinding(this.dir);
                command.setValueBinding(
                    "dir",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "dir",
                    this.dir);
            }
        }
        if (this.lang != null)
        {
            if (isValueReference(this.lang))
            {
                ValueBinding binding = createValueBinding(this.lang);
                command.setValueBinding(
                    "lang",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "lang",
                    this.lang);
            }
        }
        if (this.tabindex != null)
        {
            if (isValueReference(this.tabindex))
            {
                ValueBinding binding = createValueBinding(this.tabindex);
                command.setValueBinding(
                    "tabindex",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "tabindex",
                    this.tabindex);
            }
        }
        if (this.title != null)
        {
            if (isValueReference(this.title))
            {
                ValueBinding binding = createValueBinding(this.title);
                command.setValueBinding(
                    "title",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "title",
                    this.title);
            }
        }
        if (this.style != null)
        {
            if (isValueReference(this.style))
            {
                ValueBinding binding = createValueBinding(this.style);
                command.setValueBinding(
                    "style",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "style",
                    this.style);
            }
        }
        if (this.styleClass != null)
        {
            if (isValueReference(this.styleClass))
            {
                ValueBinding binding = createValueBinding(this.styleClass);
                command.setValueBinding(
                    "styleClass",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleClass",
                    this.styleClass);
            }
        }
        if (this.absolute != null)
        {
            if (isValueReference(this.absolute))
            {
                ValueBinding binding = createValueBinding(this.absolute);
                command.setValueBinding(
                    "absolute",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "absolute",
                    this.absolute);
            }
        }
        if (this.actionClose != null)
        {
            if (isValueReference(this.actionClose))
            {
                final MethodBinding binding = this.createMethodBinding(
                        this.actionClose,
                        new Class[0]);
                command.setActionClose(binding);
            }
            else
            {
                throw new IllegalStateException("Invalid actionClose." + this.actionClose);
            }
        }
        if (this.actionOpen != null)
        {
            if (isValueReference(this.actionOpen))
            {
                final MethodBinding binding = this.createMethodBinding(
                        this.actionOpen,
                        new Class[0]);
                command.setActionOpen(binding);
            }
            else
            {
                throw new IllegalStateException("Invalid actionOpen." + this.actionOpen);
            }
        }
        if (this.center != null)
        {
            if (isValueReference(this.center))
            {
                ValueBinding binding = createValueBinding(this.center);
                command.setValueBinding(
                    "center",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "center",
                    this.center);
            }
        }
        if (this.immediate != null)
        {
            if (isValueReference(this.immediate))
            {
                ValueBinding binding = createValueBinding(this.immediate);
                command.setValueBinding(
                    "immediate",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "immediate",
                    this.immediate);
            }
        }
        if (this.height != null)
        {
            if (isValueReference(this.height))
            {
                ValueBinding binding = createValueBinding(this.height);
                command.setValueBinding(
                    "height",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "height",
                    this.height);
            }
        }
        if (this.width != null)
        {
            if (isValueReference(this.width))
            {
                ValueBinding binding = createValueBinding(this.width);
                command.setValueBinding(
                    "width",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "width",
                    this.width);
            }
        }
        if (this.mouseHorizPos != null)
        {
            if (isValueReference(this.mouseHorizPos))
            {
                ValueBinding binding = createValueBinding(this.mouseHorizPos);
                command.setValueBinding(
                    "mouseHorizPos",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "mouseHorizPos",
                    this.mouseHorizPos);
            }
        }
        if (this.mouseVertPos != null)
        {
            if (isValueReference(this.mouseVertPos))
            {
                ValueBinding binding = createValueBinding(this.mouseVertPos);
                command.setValueBinding(
                    "mouseVertPos",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "mouseVertPos",
                    this.mouseVertPos);
            }
        }
        if (this.styleClassFrame != null)
        {
            if (isValueReference(this.styleClassFrame))
            {
                ValueBinding binding = createValueBinding(this.styleClassFrame);
                command.setValueBinding(
                    "styleClassFrame",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleClassFrame",
                    this.styleClassFrame);
            }
        }
        if (this.styleFrame != null)
        {
            if (isValueReference(this.styleFrame))
            {
                ValueBinding binding = createValueBinding(this.styleFrame);
                command.setValueBinding(
                    "styleFrame",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleFrame",
                    this.styleFrame);
            }
        }
        if (this.scrolling != null)
        {
            if (isValueReference(this.scrolling))
            {
                ValueBinding binding = createValueBinding(this.scrolling);
                command.setValueBinding(
                    "scrolling",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "scrolling",
                    this.scrolling);
            }
        }
    }
}