package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 *
 * @author Peter Friese
 * @since 16.12.2005
 */
public class AbstractAndromdaModelFormPage
        extends AbstractModelFormPage
{

    /**
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
     * @param id
     * @param title
     */
    public AbstractAndromdaModelFormPage(String id,
        String title)
    {
        super(id, title);
    }
    
    public IAndromdaDocumentModel getAndromdaDocumentModel()
    {
        return (IAndromdaDocumentModel)getModel();
    }

}
