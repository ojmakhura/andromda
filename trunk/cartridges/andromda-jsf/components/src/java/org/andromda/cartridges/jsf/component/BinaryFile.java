package org.andromda.cartridges.jsf.component;

import java.io.InputStream;

import java.util.Properties;

import javax.faces.component.UIComponentBase;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BinaryFile
    extends UIComponentBase
{
    private static final Log logger = LogFactory.getLog(BinaryFile.class);
    public static final String COMPONENT_TYPE = BinaryFile.class.getName();
    public static final String RENDERER_TYPE = "org.andromda.cartridges.jsf.BinaryFile";

    public BinaryFile()
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

    /**
     * Stores the name of the attriubte that holds the value.
     */
    public static final String VALUE_ATTRIBUTE = "value";

    /**
     * Stores the value of this binary file.
     */
    private Object value;

    /**
     * Gets the current value of this binary file.
     *
     * @return the value of this binary file.
     */
    public Object getValue()
    {
        if (this.value == null)
        {
            final ValueBinding binding = this.getValueBinding(VALUE_ATTRIBUTE);
            if (binding != null)
            {
                this.value = binding.getValue(this.getFacesContext());
            }
        }
        return this.value;
    }

    /**
     * Stores the name of the attriubte that holds the fileName.
     */
    public static final String FILE_NAME_ATTRIBUTE = "fileName";

    /**
     * The name of the file to render.
     */
    private String fileName;

    /**
     * Sets the file name for this component.
     *
     * @param fileName the name of the binary file to be rendered.
     */
    public void setFileName(final String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * Gets the file name for rending the binary file.
     * @return the name of the file to render.
     */
    public String getFileName()
    {
        if (this.fileName == null)
        {
            final ValueBinding binding = this.getValueBinding(FILE_NAME_ATTRIBUTE);
            if (binding != null)
            {
                this.fileName = (String)binding.getValue(this.getFacesContext());
            }
        }
        return this.fileName;
    }

    /**
     * The name of the attribute that stores the content type.
     */
    public static final String CONTENT_TYPE_ATTRIBUTE = "contentType";

    /**
     * The content type to use when rendering the file.
     */
    private String contentType;

    /**
     * Gets the explicity content type to render the file in.
     *
     * @return Returns the contentType.
     */
    public String getContentType()
    {
        if (this.contentType == null)
        {
            final ValueBinding binding = this.getValueBinding(CONTENT_TYPE_ATTRIBUTE);
            if (binding != null)
            {
                this.contentType = (String)binding.getValue(this.getFacesContext());
            }

            // - if the content type is still null, lets guess
            if (this.contentType == null)
            {
                final String fileName = this.getFileName();
                if (fileName != null && fileName.trim().length() > 0)
                {
                    int lastDotIndex = fileName.lastIndexOf('.');
                    if (lastDotIndex != -1)
                    {
                        final String extension = fileName.substring(
                                lastDotIndex,
                                fileName.length());
                        this.contentType = contentTypes.getProperty(extension);
                    }
                }
            }
        }
        return this.contentType;
    }

    /**
     * Sets the explicit content type in which to render the file.
     *
     * @param contentType The contentType to set.
     */
    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * Whether or not we should be prompted to save the file when its rendered.
     */
    public static final String PROMPT_ATTRIBUTE = "prompt";

    /**
     * Stores the 'prompt' value.
     */
    private Boolean prompt;

    /**
     * Gets whether or not the prompt should be rendered.
     *
     * @return Returns the prompt.
     */
    public boolean isPrompt()
    {
        if (this.prompt == null)
        {
            final ValueBinding binding = this.getValueBinding(CONTENT_TYPE_ATTRIBUTE);
            if (binding != null)
            {
                this.prompt = (Boolean)binding.getValue(this.getFacesContext());
            }
        }
        return this.prompt != null ? this.prompt.booleanValue() : false;
    }

    /**
     * Sets whether or not the prompt should be rendered.
     *
     * @param prompt The prompt to set.
     */
    public void setPrompt(final boolean prompt)
    {
        this.prompt = Boolean.valueOf(prompt);
    }

    /**
     * Stores the default content types.
     */
    final static Properties contentTypes = new Properties();

    /**
     * Load up the default content types
     */
    static
    {
        final String fileName = "contenttypes.properties";
        final InputStream stream = BinaryFile.class.getResourceAsStream(fileName);
        if (stream == null)
        {
            logger.error("Could not load file from '" + fileName + "'");
        }
        try
        {
            contentTypes.load(stream);
        }
        catch (final Throwable throwable)
        {
            logger.error(throwable);
        }
        try
        {
            stream.close();
        }
        catch (final Throwable throwable)
        {
            // - ignore
        }
    }
}