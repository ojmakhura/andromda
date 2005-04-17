package org.andromda.core.transformation;

import java.io.InputStream;
import java.net.URL;

/**
 * Is able to perform transformation of documents (such as XMI files).
 * 
 * @author Chad Brandon
 */
public interface Transformer
{
    /**
     * Transforms the given <code>model</code> with the given
     * <code>transformations</code>.  Applies the transformations
     * in the order that they are found.
     * 
     * @param model the model to transform.
     * @param transformations the XSTL files to perform the transformation, in the order
     *        they should be applied.
     * @return the transformed result as an input stream.
     */
    public InputStream transform(URL model, URL[] transformations);
}
