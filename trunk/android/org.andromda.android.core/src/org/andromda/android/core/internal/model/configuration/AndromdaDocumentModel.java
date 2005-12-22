package org.andromda.android.core.internal.model.configuration;

import org.andromda.android.core.internal.model.AbstractModel;
import org.andromda.android.core.model.configuration.IAndromdaDocumentModel;
import org.andromda.core.configuration.AndromdaDocument;

/**
 * A model wrapper for the AndroMDA configuration document.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public class AndromdaDocumentModel
        extends AbstractModel
        implements IAndromdaDocumentModel
{

    /** The AndroMDA configuration document wrapper by this model wrapper. */
    private final AndromdaDocument andromdaDocument;

    /**
     * Creates a new model wrapper for the given AndroMDA configuration document.
     */
    public AndromdaDocumentModel(final AndromdaDocument andromdaDocument)
    {
        this.andromdaDocument = andromdaDocument;
    }

    /**
     * @see org.andromda.android.core.model.configuration.IAndromdaDocumentModel#getAndromdaDocument()
     */
    public AndromdaDocument getAndromdaDocument()
    {
        return andromdaDocument;
    }

}
