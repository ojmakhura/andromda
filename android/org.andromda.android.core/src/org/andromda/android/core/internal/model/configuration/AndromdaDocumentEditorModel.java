package org.andromda.android.core.internal.model.configuration;

import org.andromda.android.core.internal.model.AbstractEditorModel;
import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.core.configuration.AndromdaDocument;

/**
 * A model wrapper for the AndroMDA configuration document.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public class AndromdaDocumentEditorModel
        extends AbstractEditorModel
        implements IAndromdaDocumentEditorModel
{

    /** The AndroMDA configuration document wrapper by this model wrapper. */
    private final AndromdaDocument andromdaDocument;

    /**
     * Creates a new model wrapper for the given AndroMDA configuration document.
     */
    public AndromdaDocumentEditorModel(final AndromdaDocument andromdaDocument)
    {
        this.andromdaDocument = andromdaDocument;
    }

    /**
     * @see org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel#getAndromdaDocument()
     */
    public AndromdaDocument getAndromdaDocument()
    {
        return andromdaDocument;
    }

}
