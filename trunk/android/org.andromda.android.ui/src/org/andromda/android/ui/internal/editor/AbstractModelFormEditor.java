package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.model.IEditorModel;
import org.andromda.android.ui.editor.AbstractMultipageSourceEditor;

/**
 *
 * @author Peter Friese
 * @since 15.12.2005
 */
public abstract class AbstractModelFormEditor
        extends AbstractMultipageSourceEditor
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
