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
    @Override
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
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    @Override
    public String getRendererType()
    {
        return BinaryFile.RENDERER_TYPE;
    }

    /**
     * The component type.
     */
    private static final String COMPONENT_TYPE = BinaryFile.COMPONENT_TYPE;

    /**
     * Returns the component type.
     */
    @Override
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
        return this.value;
    }

    /**
     * Gets the value.
     * 
     * @param valueIn The value to set.
     */
    public void setValue(String valueIn)
    {
        this.value = valueIn;
    }
    
    private String fileName;

    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return this.fileName;
    }

    /**
     * @param fileNameIn The fileName to set.
     */
    public void setFileName(String fileNameIn)
    {
        this.fileName = fileNameIn;
    }
    
    private String contentType;

    /**
     * @return Returns the contentType.
     */
    public String getContentType()
    {
        return this.contentType;
    }

    /**
     * @param contentTypeIn The contentType to set.
     */
    public void setContentType(String contentTypeIn)
    {
        this.contentType = contentTypeIn;
    }
    
    private String prompt;

    /**
     * @return Returns the prompt.
     */
    public String getPrompt()
    {
        return this.prompt;
    }

    /**
     * @param promptIn The prompt to set.
     */
    public void setPrompt(String promptIn)
    {
        this.prompt = promptIn;
    }
}