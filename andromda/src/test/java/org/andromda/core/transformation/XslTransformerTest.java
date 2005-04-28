package org.andromda.core.transformation;

import java.io.InputStream;
import java.net.URL;

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
        URL transformation1 = XslTransformerTest.class.getResource("transformation1.xsl");
        assertNotNull(transformation1);
        URL transformation2 = XslTransformerTest.class.getResource("transformation2.xsl");
        assertNotNull(transformation2);
        Transformation[] transformations = 
            new Transformation[]
            {
                new Transformation(transformation1),
                new Transformation(transformation2)
            };
        InputStream stream = XslTransformer.instance().transform(model, transformations);
        assertNotNull(stream);
    }
}
