/**
 *
 */
package org.andromda.android.internal.ui.editors.configuration.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 28.05.2005
 */
public class SpringCartridgeDetailsPage
        implements IDetailsPage
{

    private Combo combo_1;
    private Text text;
    private IManagedForm managedForm;

    public SpringCartridgeDetailsPage()
    {
    }

    public void initialize(IManagedForm managedForm)
    {
        this.managedForm = managedForm;
    }

    public void createContents(Composite parent)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        final Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.EXPANDED | Section.TITLE_BAR);
        section.setDescription("Configure the Spring cartridge:");
        section.setLayoutData(new GridData(GridData.FILL_BOTH));
        section.setText("Spring Cartridge Configuration");

        final Composite composite = toolkit.createComposite(section, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        composite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(composite);
        section.setClient(composite);

        toolkit.createLabel(composite, "Data source", SWT.NONE);

        text = toolkit.createText(composite, null, SWT.NONE);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText("New Forms Text");

        toolkit.createLabel(composite, "Hibernate dialect:", SWT.NONE);

        combo_1 = new Combo(composite, SWT.NONE);
        combo_1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    }

    public void dispose()
    {
    }

    public void setFocus()
    {
    }

    private void update()
    {
    }

    public boolean setFormInput(Object input)
    {
        return false;
    }

    public void selectionChanged(IFormPart part, ISelection selection)
    {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        update();
    }

    public void commit(boolean onSave)
    {
    }

    public boolean isDirty()
    {
        return false;
    }

    public boolean isStale()
    {
        return false;
    }

    public void refresh()
    {
        update();
    }

}
