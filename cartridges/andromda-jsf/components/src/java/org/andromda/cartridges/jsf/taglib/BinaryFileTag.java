package org.andromda.cartridges.jsf.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;

import org.andromda.cartridges.jsf.component.BinaryFile;
import org.andromda.cartridges.jsf.utils.ComponentUtils;


/**
 * The tag class for the <code>s:validatorScript</code> tag.
 */
public class BinaryFileTag
    extends UIComponentTag
{
    /**
     * Sets properties for the component.
     *
     * @param component The component whose properties we're setting
     */
    public void setProperties(final UIComponent component)
    {
        final FacesContext context = this.getFacesContext();
        ComponentUtils.setValueProperty(context, component, this.getValue());
        ComponentUtils.setStringProperty(BinaryFile.FILE_NAME_ATTRIBUTE, context, component, this.getFileName());
        ComponentUtils.setStringProperty(BinaryFile.CONTENT_TYPE_ATTRIBUTE, context, component, this.getContentType());
        ComponentUtils.setBooleanProperty(BinaryFile.PROMPT_ATTRIBUTE, context, component, this.getPrompt());
        super.setProperties(component);
    }

    /**
     * Sets the <code>functionName</code> property to null.
     */
    public void release()
    {
        super.release();
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        return BinaryFile.RENDERER_TYPE;
    }

    /**
     * The component type.
     */
    private static final String COMPONENT_TYPE = BinaryFile.COMPONENT_TYPE;

    /**
     * Returns the component type, which is
     * <code>org.andromda.cartridges.jsf.component.JSFValidatorScript</code>.
     */
    public String getComponentType()
    {
        return COMPONENT_TYPE;
    }
    
    private String value;

    /**
     * Sets the value.
     * 
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Gets the value.
     * 
     * @param value The value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
    private String fileName;

    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    private String contentType;

    /**
     * @return Returns the contentType.
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
    
    private String prompt;

    /**
     * @return Returns the prompt.
     */
    public String getPrompt()
    {
        return prompt;
    }

    /**
     * @param prompt The prompt to set.
     */
    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
    }
}