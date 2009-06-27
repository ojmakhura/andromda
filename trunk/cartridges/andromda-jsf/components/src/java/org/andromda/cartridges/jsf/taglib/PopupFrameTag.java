package org.andromda.cartridges.jsf.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;

import org.andromda.cartridges.jsf.component.html.HtmlPopupFrame;


public class PopupFrameTag
    extends UIComponentBodyTag
{
    private java.lang.String value;
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

    public void setValue(java.lang.String value)
    {
        this.value = value;
    }

    public void setAccesskey(java.lang.String accesskey)
    {
        this.accesskey = accesskey;
    }

    public void setDir(java.lang.String dir)
    {
        this.dir = dir;
    }

    public void setLang(java.lang.String lang)
    {
        this.lang = lang;
    }

    public void setTabindex(java.lang.String tabindex)
    {
        this.tabindex = tabindex;
    }

    public void setTitle(java.lang.String title)
    {
        this.title = title;
    }

    public String getRendererType()
    {
        return HtmlPopupFrame.RENDERER_TYPE;
    }

    public String getComponentType()
    {
        return HtmlPopupFrame.COMPONENT_TYPE;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    public void setAbsolute(String absolute)
    {
        this.absolute = absolute;
    }

    public void setActionClose(String actionClose)
    {
        this.actionClose = actionClose;
    }

    public void setActionOpen(String actionOpen)
    {
        this.actionOpen = actionOpen;
    }

    public void setCenter(String center)
    {
        this.center = center;
    }

    public void setImmediate(String immediate)
    {
        this.immediate = immediate;
    }

    public void setMouseVertPos(String mouseVertPos)
    {
        this.mouseVertPos = mouseVertPos;
    }

    public void setMouseHorizPos(String mouseHorizPos)
    {
        this.mouseHorizPos = mouseHorizPos;
    }

    public void setStyleClassFrame(String styleClassFrame)
    {
        this.styleClassFrame = styleClassFrame;
    }

    public void setStyleFrame(String styleFrame)
    {
        this.styleFrame = styleFrame;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public void setScrolling(String scrolling)
    {
        this.scrolling = scrolling;
    }

    private ValueBinding createValueBinding(final String value)
    {
        return FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
    }

    private MethodBinding createMethodBinding(
        final String value,
        Class[] args)
    {
        return FacesContext.getCurrentInstance().getApplication().createMethodBinding(
            value,
            args);
    }

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

        if (value != null)
        {
            if (isValueReference(value))
            {
                ValueBinding binding = createValueBinding(value);
                command.setValueBinding(
                    "value",
                    binding);
            }
            else
            {
                command.setValue(value);
            }
        }
        if (accesskey != null)
        {
            if (isValueReference(accesskey))
            {
                ValueBinding binding = createValueBinding(accesskey);
                command.setValueBinding(
                    "accesskey",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "accesskey",
                    accesskey);
            }
        }
        if (dir != null)
        {
            if (isValueReference(dir))
            {
                ValueBinding binding = createValueBinding(dir);
                command.setValueBinding(
                    "dir",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "dir",
                    dir);
            }
        }
        if (lang != null)
        {
            if (isValueReference(lang))
            {
                ValueBinding binding = createValueBinding(lang);
                command.setValueBinding(
                    "lang",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "lang",
                    lang);
            }
        }
        if (tabindex != null)
        {
            if (isValueReference(tabindex))
            {
                ValueBinding binding = createValueBinding(tabindex);
                command.setValueBinding(
                    "tabindex",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "tabindex",
                    tabindex);
            }
        }
        if (title != null)
        {
            if (isValueReference(title))
            {
                ValueBinding binding = createValueBinding(title);
                command.setValueBinding(
                    "title",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "title",
                    title);
            }
        }
        if (style != null)
        {
            if (isValueReference(style))
            {
                ValueBinding binding = createValueBinding(style);
                command.setValueBinding(
                    "style",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "style",
                    style);
            }
        }
        if (styleClass != null)
        {
            if (isValueReference(styleClass))
            {
                ValueBinding binding = createValueBinding(styleClass);
                command.setValueBinding(
                    "styleClass",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleClass",
                    styleClass);
            }
        }
        if (absolute != null)
        {
            if (isValueReference(absolute))
            {
                ValueBinding binding = createValueBinding(absolute);
                command.setValueBinding(
                    "absolute",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "absolute",
                    absolute);
            }
        }
        if (actionClose != null)
        {
            if (isValueReference(actionClose))
            {
                final MethodBinding binding = this.createMethodBinding(
                        actionClose,
                        new Class[0]);
                command.setActionClose(binding);
            }
            else
            {
                throw new IllegalStateException("Invalid actionClose." + actionClose);
            }
        }
        if (actionOpen != null)
        {
            if (isValueReference(actionOpen))
            {
                final MethodBinding binding = this.createMethodBinding(
                        actionOpen,
                        new Class[0]);
                command.setActionOpen(binding);
            }
            else
            {
                throw new IllegalStateException("Invalid actionOpen." + actionOpen);
            }
        }
        if (center != null)
        {
            if (isValueReference(center))
            {
                ValueBinding binding = createValueBinding(center);
                command.setValueBinding(
                    "center",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "center",
                    center);
            }
        }
        if (immediate != null)
        {
            if (isValueReference(immediate))
            {
                ValueBinding binding = createValueBinding(immediate);
                command.setValueBinding(
                    "immediate",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "immediate",
                    immediate);
            }
        }
        if (height != null)
        {
            if (isValueReference(height))
            {
                ValueBinding binding = createValueBinding(height);
                command.setValueBinding(
                    "height",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "height",
                    height);
            }
        }
        if (width != null)
        {
            if (isValueReference(width))
            {
                ValueBinding binding = createValueBinding(width);
                command.setValueBinding(
                    "width",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "width",
                    width);
            }
        }
        if (mouseHorizPos != null)
        {
            if (isValueReference(mouseHorizPos))
            {
                ValueBinding binding = createValueBinding(mouseHorizPos);
                command.setValueBinding(
                    "mouseHorizPos",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "mouseHorizPos",
                    mouseHorizPos);
            }
        }
        if (mouseVertPos != null)
        {
            if (isValueReference(mouseVertPos))
            {
                ValueBinding binding = createValueBinding(mouseVertPos);
                command.setValueBinding(
                    "mouseVertPos",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "mouseVertPos",
                    mouseVertPos);
            }
        }
        if (styleClassFrame != null)
        {
            if (isValueReference(styleClassFrame))
            {
                ValueBinding binding = createValueBinding(styleClassFrame);
                command.setValueBinding(
                    "styleClassFrame",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleClassFrame",
                    styleClassFrame);
            }
        }
        if (styleFrame != null)
        {
            if (isValueReference(styleFrame))
            {
                ValueBinding binding = createValueBinding(styleFrame);
                command.setValueBinding(
                    "styleFrame",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "styleFrame",
                    styleFrame);
            }
        }
        if (scrolling != null)
        {
            if (isValueReference(scrolling))
            {
                ValueBinding binding = createValueBinding(scrolling);
                command.setValueBinding(
                    "scrolling",
                    binding);
            }
            else
            {
                command.getAttributes().put(
                    "scrolling",
                    scrolling);
            }
        }
    }
}