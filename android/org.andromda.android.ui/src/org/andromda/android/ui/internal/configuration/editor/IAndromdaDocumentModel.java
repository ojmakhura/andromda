package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.ui.internal.editor.IModel;
import org.andromda.core.configuration.AndromdaDocument;

/**
 * Model wrapper interface for the AndroMDA configuration model.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public interface IAndromdaDocumentModel
        extends IModel
{
    
    /**
     * @return The AndroMDA configuration document.
     */
    AndromdaDocument getAndromdaDocument();

}
