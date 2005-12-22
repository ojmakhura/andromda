package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.model.IModel;
import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.core.model.IModelChangedListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;

/**
 * A composite that is hosted in a {@link org.eclipse.ui.forms.SectionPart}.
 *
 * @author Peter Friese
 * @since 19.12.2005
 */
public abstract class AbstractModelComposite
        extends Composite
        implements IModelChangedListener
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
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose()
    {
        if (getModel() != null)
        {
            IModelChangeProvider provider = (IModelChangeProvider)getModel();
            provider.removeModelChangedListener(this);
        }
        super.dispose();
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

    /**
     * @return The project that the edited resource belongs to.
     */
    protected IProject getProject()
    {
        AbstractModelSectionPart baseSectionPart = (AbstractModelSectionPart)getParentSection();
        final IProject project = baseSectionPart.getProject();
        return project;
    }

    /**
     * @return The model being edited.
     */
    public IModel getModel()
    {
        AbstractModelFormEditor modelFormEditor = getModelFormEditor();
        if (modelFormEditor != null)
        {
            return modelFormEditor.getModel();
        }
        return null;
    }

    /**
     * @see org.andromda.android.core.model.IModelChangedListener#modelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public abstract void modelChanged(IModelChangedEvent event);

    /**
     *
     */
    protected void publishChangeEvent()
    {
        if (getModel() instanceof IModelChangeProvider)
        {
            IModelChangeProvider provider = (IModelChangeProvider)getModel();
            provider.fireModelChanged(null);
        }
    }

}
