package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.android.ui.internal.editor.AbstractModelComposite;
import org.andromda.core.configuration.AndromdaDocument;
import org.eclipse.ui.forms.SectionPart;

/**
 * This composite provides easy access to the edited model.
 *
 * @author Peter Friese
 * @since 28.12.2005
 */
public abstract class AbstractAndromdaModelComposite
        extends AbstractModelComposite
{

    /**
     * Creates a new composite.
     *
     * @param parentSection
     * @param style
     */
    public AbstractAndromdaModelComposite(SectionPart parentSection,
        int style)
    {
        super(parentSection, style);
    }

    /**
     * Provides access to the AndroMDA configuration document.
     *
     * @return The instance of the AndroMDA configuration that is being edited.
     */
    public IAndromdaDocumentEditorModel getAndromdaDocumentEditorModel()
    {
        if (getParentSection() instanceof AbstractAndromdaModelSectionPart)
        {
            AbstractAndromdaModelSectionPart andromdaModelSection = (AbstractAndromdaModelSectionPart)getParentSection();
            return andromdaModelSection.getAndromdaDocumentEditorModel();
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
