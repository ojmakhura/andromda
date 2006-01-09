package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * This form page class provides access to the AndroMDA configuration document.
 *
 * @author Peter Friese
 * @since 16.12.2005
 */
public abstract class AbstractAndromdaModelFormPage
        extends AbstractModelFormPage
{

    /**
     * Creates a new form page.
     *
     * @param editor
     * @param id
     * @param title
     */
    public AbstractAndromdaModelFormPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
    }

    /**
     * Creates a new form page.
     *
     * @param id
     * @param title
     */
    public AbstractAndromdaModelFormPage(String id,
        String title)
    {
        super(id, title);
    }

    /**
     * Provides access to the AndroMDA configuration document.
     *
     * @return The instance of the AndroMDA configuration that is being edited.
     */
    public IAndromdaDocumentEditorModel getAndromdaDocumentEditorModel()
    {
        return (IAndromdaDocumentEditorModel)getModel();
    }

}
