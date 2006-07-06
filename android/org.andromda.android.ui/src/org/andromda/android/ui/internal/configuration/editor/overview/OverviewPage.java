package org.andromda.android.ui.internal.configuration.editor.overview;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.andromda.android.ui.internal.editor.AbstractModelFormEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * 
 * @author Peter Friese
 * @since 28.03.2006
 */
public class OverviewPage
        extends AbstractAndromdaModelFormPage
{

    /**
     * The page ID.
     */
    public static final String PAGE_ID = "overview";

    /**
     * Creates the page.
     * 
     * @param editor The editor hosting this page.
     * @param id The ID of this page.
     * @param title The title of this page.
     */
    public OverviewPage(final AbstractModelFormEditor editor,
        final String id,
        final String title)
    {
        super(editor, id, title);
    }

    /**
     * {@inheritDoc}
     */
    protected void createFormContent(final IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();

        form.setText("Overview");

        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        Composite body = form.getBody();
        body.setLayout(gridLayout);

        toolkit.paintBordersFor(body);
    }

    /**
     * {@inheritDoc}
     */
    protected void doUpdateDocument(IDocument document)
    {
    }

    /**
     * {@inheritDoc}
     */
    protected void doUpdatePage(IDocument document)
    {
    }

}
