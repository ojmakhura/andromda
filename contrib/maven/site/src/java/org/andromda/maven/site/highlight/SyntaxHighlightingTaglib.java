package org.andromda.maven.site.highlight;

import org.apache.commons.jelly.TagLibrary;
import org.andromda.maven.site.highlight.java.HighlightJavaTag;
import org.andromda.maven.site.highlight.xml.HighlightXmlTag;
import org.andromda.maven.site.highlight.velocity.HighlightVelocityTag;

public class SyntaxHighlightingTaglib extends TagLibrary
{
    public SyntaxHighlightingTaglib()
    {
        registerTag("xml", HighlightXmlTag.class);
        registerTag("java", HighlightJavaTag.class);
        registerTag("velocity", HighlightVelocityTag.class);
    }
}
