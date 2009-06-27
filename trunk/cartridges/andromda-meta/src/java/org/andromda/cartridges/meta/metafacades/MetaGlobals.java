package org.andromda.cartridges.meta.metafacades;


/**
 * Stores Globals specific to the meta cartridge metafacades.
 *
 * @author Chad Brandon
 */
class MetaGlobals
{
    /**
     * The property used to specify the implementation operation name pattern (for the logic of attributes, association
     * ends, and operations).
     */
    static final String PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN =
        "implementationOperationNamePattern";

    /**
     * The pattern used to create the generalization name (when using delegated inheritance).
     */
    static final String PROPERTY_GENERALIZATION_NAME_PATTERN = "generalizationNamePattern";
}