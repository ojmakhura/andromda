package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.android.ui.internal.editor.AbstractModelSectionPart;
import org.andromda.core.configuration.AndromdaDocument;
import org.eclipse.swt.widgets.Composite;

/**
 * A section part that provides easy access to the edited AndroMDA configuration document.
 * 
 * @author Peter Friese
 * @since 27.12.2005
 */
public abstract class AbstractAndromdaModelSectionPart
        extends AbstractModelSectionPart
{

    /**
     * Creates a new model section part.
     * 
     * @param page The hosting page.
     */
    public AbstractAndromdaModelSectionPart(AbstractModelFormPage page)
    {
        super(page);
    }

    /**
     * Creates a new model section part.
     * 
     * @param page
     * @param style
     */
    public AbstractAndromdaModelSectionPart(AbstractModelFormPage page,
        int style)
    {
        super(page, style);
    }

    /**
     * Creates a new model section part.
     * 
     * @param parent
     * @param page
     * @param style
     */
    public AbstractAndromdaModelSectionPart(Composite parent,
        AbstractModelFormPage page,
        int style)
    {
        super(parent, page, style);
    }

    /**
     * Creates a new model section part.
     * 
     * @param parent
     * @param page
     */
    public AbstractAndromdaModelSectionPart(Composite parent,
        AbstractModelFormPage page)
    {
        super(parent, page);
    }

    /**
     * Provides access to the AndroMDA configuration document.
     * 
     * @return The instance of the AndroMDA configuration that is being edited.
     */
    public IAndromdaDocumentEditorModel getAndromdaDocumentEditorModel()
    {
        if (getPage() instanceof AbstractAndromdaModelFormPage)
        {
            AbstractAndromdaModelFormPage andromdaModelFormPage = (AbstractAndromdaModelFormPage)getPage();
            return andromdaModelFormPage.getAndromdaDocumentEditorModel();
        }
        return null;
    }

    /**
     * Provides easy access to the AndroMDA configuration document.
     * 
     * @return The AndroMDA configuration document being edited.
     */
    public AndromdaDocument getAndromdaDocument()
    {
        return getAndromdaDocumentEditorModel().getAndromdaDocument();
    }

}
