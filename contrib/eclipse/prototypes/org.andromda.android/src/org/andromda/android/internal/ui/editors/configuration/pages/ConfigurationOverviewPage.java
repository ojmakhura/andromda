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
 * @since 27.05.2005
 */
public class ConfigurationOverviewPage
        extends FormPage
{

    private Text text_1;
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
        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 2;
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        final Section generalInformationSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR);
        generalInformationSection.setDescription("This section describes general information about this project:");
        generalInformationSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        generalInformationSection.setText("General Information");

        final Composite generalInformationComposite = toolkit.createComposite(generalInformationSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        generalInformationComposite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(generalInformationComposite);
        generalInformationSection.setClient(generalInformationComposite);

        toolkit.createLabel(generalInformationComposite, "Project name:", SWT.NONE);

        text = toolkit.createText(generalInformationComposite, null, SWT.NONE);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText("New Forms Text");

        toolkit.createLabel(generalInformationComposite, "Description:", SWT.NONE);

        text_1 = toolkit.createText(generalInformationComposite, null, SWT.NONE);
        text_1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text_1.setText("New Forms Text");

        final Section projectContentsSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR);
        projectContentsSection.setDescription("This project has X modules:");
        projectContentsSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        projectContentsSection.setText("Project Contents");

        final Composite projectContentsComposite = toolkit.createComposite(projectContentsSection, SWT.NONE);
        projectContentsComposite.setLayout(new GridLayout());
        toolkit.paintBordersFor(projectContentsComposite);
        projectContentsSection.setClient(projectContentsComposite);
    }

}
