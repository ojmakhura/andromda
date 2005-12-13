package org.andromda.android.ui.internal.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A basic section part hat features easy access to the editor and the hosting editor page.
 *
 * @author Peter Friese
 * @since 09.12.2005
 */
public class BaseSectionPart
        extends SectionPart
{

    /** The editor page owning this section. */
    private final FormPage page;

    /**
     * Creates a new section part.
     *
     * @param parent The parent composite.
     * @param page The page that hosts this section.
     * @param style The SWT style for the section.
     */
    public BaseSectionPart(Composite parent,
        FormPage page, int style)
    {
        super(parent, page.getManagedForm().getToolkit(), style);
        this.page = page;

    }

    /**
     * Creates a new section part.
     *
     * @param parent The parent composite.
     * @param page The page that hosts this section.
     */
    public BaseSectionPart(Composite parent,
        FormPage page)
    {
        this(parent, page, Section.DESCRIPTION | Section.TITLE_BAR);
    }

    /**
     * Creates a new section part.
     *
     * @param page The page that hosts this section.
     * @param style The SWT style of the section.
     */
    public BaseSectionPart(FormPage page, int style)
    {
        this(page.getManagedForm().getForm().getBody(), page, style);
    }

    /**
     * Creates a new section part.
     *
     * @param page The page that hosts this section.
     */
    public BaseSectionPart(FormPage page)
    {
        this(page.getManagedForm().getForm().getBody(), page);
    }

    /**
     * @return Returns the page.
     */
    protected FormPage getPage()
    {
        return page;
    }

    /**
     * @return The editor.
     */
    protected FormEditor getEditor()
    {
        return getPage().getEditor();
    }

}
