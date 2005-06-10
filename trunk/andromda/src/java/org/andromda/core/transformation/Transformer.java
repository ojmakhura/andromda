package org.andromda.core.transformation;

import java.io.InputStream;

import org.andromda.core.configuration.Transformation;


/**
 * Is able to apply transformations to a given model (such as an XMI file).
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
     * @param the URI to the model be transform.
     * @param transformations the files to perform the transformation, in the order
     *        they should be applied.
     * @return the transformed result as an input stream.
     */
    public InputStream transform(
        String modelUri,
        Transformation[] transformations);
}