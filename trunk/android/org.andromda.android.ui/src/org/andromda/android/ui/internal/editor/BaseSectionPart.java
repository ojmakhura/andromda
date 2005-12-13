package org.andromda.android.ui.internal.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 09.12.2005
 */
public class BaseSectionPart
        extends SectionPart
{

    /** The editor page owning this section. */
    private final FormPage page;

    public BaseSectionPart(Composite parent,
        FormPage page)
    {
        super(parent, page.getManagedForm().getToolkit(), Section.DESCRIPTION
                | Section.TITLE_BAR);
        this.page = page;

    }

    public BaseSectionPart(FormPage page)
    {
        super(page.getManagedForm().getForm().getBody(), page.getManagedForm().getToolkit(), Section.DESCRIPTION
                | Section.TITLE_BAR);
        this.page = page;
    }

    /**
     * @return Returns the page.
     */
    protected FormPage getPage()
    {
        return page;
    }

    /**
     * @return
     */
    protected FormEditor getEditor()
    {
        return getPage().getEditor();
    }

}
