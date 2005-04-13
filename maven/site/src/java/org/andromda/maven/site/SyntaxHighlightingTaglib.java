package org.andromda.maven.site;

import org.apache.commons.jelly.TagLibrary;

public class SyntaxHighlightingTaglib
        extends TagLibrary
{
    public SyntaxHighlightingTaglib()
    {
        registerTag("xml", HighlightXmlTag.class);
        registerTag("java", HighlightJavaTag.class);
        registerTag("velocity", HighlightVelocityTag.class);
    }
}
