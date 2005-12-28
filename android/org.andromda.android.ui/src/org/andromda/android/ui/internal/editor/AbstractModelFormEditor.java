package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.model.IEditorModel;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public abstract class AbstractModelFormEditor
        extends FormEditor
{

    /**
     * 
     */
    public AbstractModelFormEditor()
    {
        super();
    }

    /**
     * @return
     */
    public abstract IEditorModel getEditorModel();

}
