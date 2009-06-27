package org.andromda.taglibs.formatting;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class EscapeTag extends BodyTagSupport
{
    private String language = null;

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public int doAfterBody() throws JspException
    {
        String escapedString = this.getBodyContent().getString();

        final String[] languages = this.language.split(",");
        for (int i = 0; i < languages.length; i++)
        {
            final String language = languages[i].trim();

            if ("html".equalsIgnoreCase(language))
            {
                escapedString = StringEscapeUtils.escapeHtml(escapedString);
            }
            else if ("javascript".equalsIgnoreCase(language))
            {
                escapedString = StringEscapeUtils.escapeJavaScript(escapedString);
            }
            else if ("java".equalsIgnoreCase(language))
            {
                escapedString = StringEscapeUtils.escapeJava(escapedString);
            }
        }

        try
        {
            this.getPreviousOut().print(escapedString);
        }
        catch (IOException e)
        {
            throw new JspException("Unable to print out escaped body text");
        }
        return super.doAfterBody();
    }
}
