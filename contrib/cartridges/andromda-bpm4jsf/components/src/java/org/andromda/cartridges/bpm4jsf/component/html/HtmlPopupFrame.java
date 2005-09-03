package org.andromda.cartridges.bpm4jsf.component.html;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;


public class HtmlPopupFrame
    extends UICommand
{
    public static final String COMPONENT_TYPE = "org.andromda.cartridges.bpm4jsf.HtmlPopupFrame";
    public static final String RENDERER_TYPE = "org.andromda.cartridges.bpm4jsf.Popup";
    private MethodBinding actionOpen;
    private MethodBinding actionClose;
    private String immediate;
    private String accesskey;
    private String dir;
    private String lang;
    private String tabindex;
    private String title;
    private String mouseHorizPos;
    private String mouseVertPos;
    private String style;
    private String styleClass;
    private String styleFrame;
    private String styleClassFrame;
    private String absolute;
    private String center;
    private String height;
    private String width;
    private String scrolling;

    public HtmlPopupFrame()
    {
        super();
        setRendererType(RENDERER_TYPE);
    }

    public java.lang.String getAccesskey()
    {
        if (null != this.accesskey)
        {
            return this.accesskey;
        }
        ValueBinding _vb = getValueBinding("accesskey");
        if (_vb != null)
        {
            return (java.lang.String)_vb.getValue(getFacesContext());
        }
        return null;
    }

    /**
     * Returns the component's family. In this case, the component is not
     * associated with a family, so this method returns null.
     */
    public String getFamily()
    {
        return "org.andromda.cartridges.bpm4jsf.Popup";
    }

    public void setAccesskey(java.lang.String accesskey)
    {
        this.accesskey = accesskey;
    }

    public java.lang.String getDir()
    {
        if (null != this.dir)
        {
            return this.dir;
        }
        ValueBinding binding = getValueBinding("dir");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setDir(java.lang.String dir)
    {
        this.dir = dir;
    }

    public java.lang.String getLang()
    {
        if (null != this.lang)
        {
            return this.lang;
        }
        ValueBinding binding = getValueBinding("lang");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setLang(java.lang.String lang)
    {
        this.lang = lang;
    }

    public java.lang.String getTabindex()
    {
        if (null != this.tabindex)
        {
            return this.tabindex;
        }
        ValueBinding binding = getValueBinding("tabindex");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }

        return null;
    }

    public void setTabindex(java.lang.String tabindex)
    {
        this.tabindex = tabindex;
    }

    public java.lang.String getTitle()
    {
        if (null != this.title)
        {
            return this.title;
        }
        ValueBinding binding = getValueBinding("title");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setTitle(java.lang.String title)
    {
        this.title = title;
    }

    public String getHeight()
    {
        if (null != this.height)
        {
            return this.height;
        }
        ValueBinding binding = getValueBinding("height");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }

        return null;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getWidth()
    {
        if (null != this.width)
        {
            return this.width;
        }
        ValueBinding binding = getValueBinding("width");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getMouseHorizPos()
    {
        if (null != this.mouseHorizPos)
        {
            return this.mouseHorizPos;
        }
        ValueBinding binding = getValueBinding("mouseRelHorizPos");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setMouseHorizPos(String mouseHorizPos)
    {
        this.mouseHorizPos = mouseHorizPos;
    }

    public String getMouseVertPos()
    {
        if (null != this.mouseVertPos)
        {
            return this.mouseVertPos;
        }
        ValueBinding binding = getValueBinding("mouseRelVertPos");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setMouseVertPos(String mouseVertPos)
    {
        this.mouseVertPos = mouseVertPos;
    }

    public String getStyle()
    {
        if (null != this.style)
        {
            return this.style;
        }
        ValueBinding binding = getValueBinding("style");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getStyleClass()
    {
        if (null != this.styleClass)
        {
            return this.styleClass;
        }
        ValueBinding binding = getValueBinding("styleClass");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    public String getStyleFrame()
    {
        if (null != this.styleFrame)
        {
            return this.styleFrame;
        }
        ValueBinding binding = getValueBinding("styleFrame");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setStyleFrame(String styleFrame)
    {
        this.styleFrame = styleFrame;
    }

    public String getStyleClassFrame()
    {
        if (null != this.styleClassFrame)
        {
            return this.styleClassFrame;
        }
        ValueBinding binding = getValueBinding("styleClassFrame");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setStyleClassFrame(String styleClassFrame)
    {
        this.styleClassFrame = styleClassFrame;
    }

    public String getAbsolute()
    {
        if (null != this.absolute)
        {
            return this.absolute;
        }
        ValueBinding binding = getValueBinding("absolute");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setAbsolute(String absolute)
    {
        this.absolute = absolute;
    }

    public MethodBinding getActionClose()
    {
        return actionClose;
    }

    public void setActionClose(MethodBinding actionClose)
    {
        this.actionClose = actionClose;
    }

    public MethodBinding getActionOpen()
    {
        return actionOpen;
    }

    public void setActionOpen(MethodBinding actionOpen)
    {
        this.actionOpen = actionOpen;
    }

    public String getCenter()
    {
        if (null != this.center)
        {
            return this.center;
        }
        ValueBinding binding = getValueBinding("center");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setCenter(String center)
    {
        this.center = center;
    }

    public String getImmediate()
    {
        if (null != this.immediate)
        {
            return this.immediate;
        }
        ValueBinding binding = getValueBinding("immediate");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setImmediate(String immediate)
    {
        this.immediate = immediate;
    }

    public String getScrolling()
    {
        if (null != this.scrolling)
        {
            return this.scrolling;
        }
        ValueBinding binding = getValueBinding("scrolling");
        if (binding != null)
        {
            return (java.lang.String)binding.getValue(getFacesContext());
        }
        return null;
    }

    public void setScrolling(String disableScroll)
    {
        this.scrolling = disableScroll;
    }

    public Object saveState(FacesContext _context)
    {
        Object[] _values = new Object[20];
        _values[0] = super.saveState(_context);
        _values[1] = accesskey;
        _values[2] = lang;
        _values[3] = dir;
        _values[4] = tabindex;
        _values[5] = title;
        _values[6] = mouseHorizPos;
        _values[7] = mouseVertPos;
        _values[8] = style;
        _values[9] = styleClass;
        _values[10] = styleFrame;
        _values[11] = styleClassFrame;
        _values[12] = saveAttachedState(
                _context,
                actionOpen);
        _values[13] = saveAttachedState(
                _context,
                actionClose);
        _values[14] = immediate;
        _values[15] = absolute;
        _values[16] = center;
        _values[17] = height;
        _values[18] = width;
        _values[19] = scrolling;
        return _values;
    }

    public void restoreState(
        FacesContext _context,
        Object _state)
    {
        Object[] _values = (Object[])_state;
        super.restoreState(
            _context,
            _values[0]);
        accesskey = (String)_values[1];
        lang = (String)_values[2];
        dir = (String)_values[3];
        tabindex = (String)_values[4];
        title = (String)_values[5];
        mouseHorizPos = (String)_values[6];
        mouseVertPos = (String)_values[7];
        style = (String)_values[8];
        styleClass = (String)_values[9];
        styleFrame = (String)_values[10];
        styleClassFrame = (String)_values[11];
        actionOpen = (MethodBinding)restoreAttachedState(
                _context,
                _values[12]);
        actionClose = (MethodBinding)restoreAttachedState(
                _context,
                _values[13]);
        immediate = (String)_values[14];
        absolute = (String)_values[15];
        center = (String)_values[16];
        height = (String)_values[17];
        width = (String)_values[18];
        scrolling = (String)_values[19];
    }

    public void queueEventNormal(FacesEvent event)
    {
        if (event instanceof ActionEvent)
        {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        if (event == null)
        {
            throw new NullPointerException();
        }
        UIComponent parent = getParent();
        if (parent == null)
        {
            throw new IllegalStateException();
        }
        parent.queueEvent(event);
    }

    public void queueEventImmediate(FacesEvent event)
    {
        if (event instanceof ActionEvent)
        {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        }
        if (event == null)
        {
            throw new NullPointerException();
        }
        UIComponent parent = getParent();
        if (parent == null)
        {
            throw new IllegalStateException();
        }
        parent.queueEvent(event);
    }
}