package org.andromda.android.ui.internal.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * A composite that is hosted in a {@link org.eclipse.ui.forms.SectionPart}.
 * 
 * @author Peter Friese
 * @since 19.12.2005
 */
public abstract class AbstractModelComposite
        extends Composite
{

    /** The section we're hosted in. */
    private final SectionPart parentSection;

    /**
     * Creates a new composite that is hosted in the given {@link SectionPart}.
     * 
     * @param parentSection The host of this composite.
     * @param style The SWT style for this composite.
     */
    public AbstractModelComposite(final SectionPart parentSection,
        int style)
    {
        super(parentSection.getSection(), style);
        this.parentSection = parentSection;
    }

    /**
     * @return Returns the parentSection.
     */
    public SectionPart getParentSection()
    {
        return parentSection;
    }

    /**
     * @return The model form editor.
     */
    public AbstractModelFormEditor getModelFormEditor()
    {
        if (parentSection instanceof AbstractModelSectionPart)
        {
            AbstractModelSectionPart modelSection = (AbstractModelSectionPart)parentSection;
            return modelSection.getModelFormEditor();
        }
        return null;
    }

}
