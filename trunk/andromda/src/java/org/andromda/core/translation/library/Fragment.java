package org.andromda.core.translation.library;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A Translation "fragment" of a translation file. This fragment belongs to a
 * Translation object.
 * 
 * @see org.andromda.core.translation.library.Translation
 * @author Chad Brandon
 */
public class Fragment
{

    private String name;
    private String handlerMethod;

    /**
     * The Translation to which this Fragment belongs.
     */
    private Translation translation;

    /**
     * The possible kinds available to this Fragment
     */
    private Map kinds = new HashMap();

    /**
     * It doesn't make any sense to instatiate this object explicitly. It
     * intended to be instantiated as part of a Translation.
     * 
     * @see org.andromda.core.translation.library.Translation
     */
    public Fragment()
    {}

    /**
     * Gets the name of this fragment
     * 
     * @return the name of this fragment.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this fragment.
     * 
     * @param name the name to set.
     */
    public void setName(String name)
    {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * Returns the kinds contained within this translation fragment
     * 
     * @return Map the Kinds keyed by name.
     */
    public Map getKinds()
    {
        return this.kinds;
    }

    /**
     * Returns the body for the fragment kind with the specified name.
     * 
     * @param name
     *            the name of the kind to get.
     * @return FragmentKind
     */
    public String getKind(String name)
    {
        final String methodName = "Fragment.getKind";

        //clean the name first
        name = StringUtils.trimToEmpty(name);

        ExceptionUtils.checkEmpty(methodName, "name", name);
        String kind = StringUtils.trimToEmpty((String)kinds.get(name));
        if (kind == null)
        {
            throw new LibraryException(
                methodName + " - no kind '" + name
                    + "' could be found for the translation fragment '"
                    + this.getName() + "' check the fragment '"
                    + this.getName() + "' in translation template --> '"
                    + getTranslation().getLibraryTranslation().getTemplate()
                    + "'");
        }
        return kind;
    }

    /**
     * Adds the specified kind having the specified name and body to the
     * Fragment.
     * 
     * @param name
     *            the name of the kind of expression.
     * @param body
     *            the body of the kind of expression.
     */
    public void addKind(String name, String body)
    {
        kinds.put(StringUtils.trimToEmpty(name), body);
    }

    /**
     * Returns the name of the handler method.
     * 
     * @return Returns the handlerMethod.
     */
    public String getHandlerMethod()
    {
        return this.handlerMethod;
    }

    /**
     * Sets the name of the handler method. This method is the method within the
     * Translator that handles the processing of the fragment.
     * 
     * @see org.andromda.core.translation.Translator
     * @param handlerMethod
     *            The handlerMethod to set.
     */
    public void setHandlerMethod(String handlerMethod)
    {
        this.handlerMethod = handlerMethod;
    }

    /**
     * Gets the Translation to which this Fragment belongs.
     * 
     * @return Translation
     */
    public Translation getTranslation()
    {
        final String methodName = "Fragment.getTranslation";
        //should never happen, but it doesn't hurt to be safe
        if (this.translation == null)
        {
            throw new LibraryException(
                methodName + " - translation can not be null");
        }
        return translation;
    }

    /**
     * Sets the Translation to which this Fragment belongs.
     * 
     * @param translation
     */
    public void setTranslation(Translation translation)
    {
        this.translation = translation;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

}