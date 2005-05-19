package org.andromda.core.transformation;

import java.io.InputStream;
import java.net.URL;

import org.andromda.core.configuration.Transformation;

import junit.framework.TestCase;


/**
 * Tests the {@link org.andromda.core.transformation.XslTransformer}
 * 
 * @author Chad Brandon
 */
public class XslTransformerTest
    extends TestCase
{
    public void testTransform()
    {
        assertNull(XslTransformer.instance().transform(null, new Transformation[0]));
        
        URL model = XslTransformerTest.class.getResource("model.xml");
        assertNotNull(model);
        URL transformation1Uri = XslTransformerTest.class.getResource("transformation1.xsl");
        assertNotNull(transformation1Uri);
        URL transformation2Uri = XslTransformerTest.class.getResource("transformation2.xsl");
        assertNotNull(transformation2Uri);
        Transformation transformation1 = new Transformation();
        transformation1.setUrl(transformation1Uri);
        Transformation transformation2 = new Transformation();
        transformation2.setUrl(transformation2Uri);
        Transformation[] transformations = 
            new Transformation[]
            {
                transformation1,
                transformation2
            };
        InputStream stream = XslTransformer.instance().transform(model, transformations);
        assertNotNull(stream);
    }
}
