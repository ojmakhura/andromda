package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.model.IEditorModel;
import org.andromda.android.ui.editor.AbstractFormEditorPage;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Provides convenience methods for accessing the model edited by the editor.
 *
 * @author Peter Friese
 * @since 15.12.2005
 */
public abstract class AbstractModelFormPage
        extends AbstractFormEditorPage
{

    /**
     * @param editor
     * @param id
     * @param title
     */
    public AbstractModelFormPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
    }

    /**
     * @param id
     * @param title
     */
    public AbstractModelFormPage(String id,
        String title)
    {
        super(id, title);
    }

    public AbstractModelFormEditor getModelEditor()
    {
        return (AbstractModelFormEditor)getEditor();
    }

    public IEditorModel getModel()
    {
        return getModelEditor().getEditorModel();
    }

}
