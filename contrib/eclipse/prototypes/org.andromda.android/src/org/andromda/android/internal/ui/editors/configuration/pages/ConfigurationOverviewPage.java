/**
 *
 */
package org.andromda.android.internal.ui.editors.configuration.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 26.05.2005
 */
public class ConfigurationOverviewPage
        extends FormPage
{

    private Text text;
    public ConfigurationOverviewPage(String id, String title)
    {
        super(id, title);
    }

    public ConfigurationOverviewPage(FormEditor editor, String id, String title)
    {
        super(editor, id, title);
    }

    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Overview");
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);

        final Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR);
        section.setDescription("This section shows general information on the project.");
        section.setText("General Information");

        final Composite composite_1 = toolkit.createComposite(section, SWT.NONE);
        composite_1.setLayout(new GridLayout());
        toolkit.paintBordersFor(composite_1);

        toolkit.createLabel(composite_1, "New Forms Label", SWT.NONE);

        text = toolkit.createText(composite_1, null, SWT.NONE);
        text.setText("New Forms Text");

    }

}
