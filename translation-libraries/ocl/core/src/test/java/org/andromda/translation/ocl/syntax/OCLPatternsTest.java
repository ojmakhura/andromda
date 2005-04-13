package org.andromda.translation.ocl.syntax;

import junit.framework.TestCase;

/**
 * Tests {@link OCLPatterns}
 *
 * @author Chad Brandon
 */
public class OCLPatternsTest
        extends TestCase
{
    public void testIsCollectionOperationResultNavigationalPath()
    {
        final String validPatternOne = "attributes->first().type.fullyQualifiedName";
        assertTrue(OCLPatterns.isCollectionOperationResultNavigationalPath(validPatternOne));
        final String validPatternTwo = " attributes -> first ( ) . type . fullyQualifiedName";
        assertTrue(OCLPatterns.isCollectionOperationResultNavigationalPath(validPatternTwo));
        final String invalidPatternOne = "attributes -> forAll ( type . fullyQualifiedName = literalTypeName )";
        assertFalse(OCLPatterns.isCollectionOperationResultNavigationalPath(invalidPatternOne));
    }
}
