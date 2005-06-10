/**
 *
 */
package org.andromda.android.internal.ui.editors.configuration.pages;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
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
public class ModelPage
        extends FormPage
{

    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
        public String getColumnText(Object element, int columnIndex)
        {
            return element.toString();
        }
        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }
    }
    class ContentProvider
            implements IStructuredContentProvider
    {
        public Object[] getElements(Object inputElement)
        {
            return new Object[] { "item_0", "item_1", "item_2" };
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
        }
    }

    private Table table;

    public ModelPage(String id, String title)
    {
        super(id, title);
    }

    public ModelPage(FormEditor editor, String id, String title)
    {
        super(editor, id, title);
    }

    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Models");
        Composite body = form.getBody();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 2;
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        final Section modelsSection = toolkit.createSection(body, Section.DESCRIPTION
                | Section.TITLE_BAR);
        modelsSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        modelsSection.setDescription("Specify the list of models you want to be processed:");
        modelsSection.setText("Processed models");

        final Composite modelsComposite = toolkit.createComposite(modelsSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        modelsComposite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(modelsComposite);
        modelsSection.setClient(modelsComposite);

        final TableViewer tableViewer = new TableViewer(modelsComposite, SWT.BORDER);
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setContentProvider(new ContentProvider());
        table = tableViewer.getTable();
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 59;
        table.setLayoutData(gridData);
        tableViewer.setInput(new Object());

        final Composite composite = toolkit.createComposite(modelsComposite, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER
                | GridData.VERTICAL_ALIGN_BEGINNING));
        composite.setLayout(new GridLayout());
        toolkit.paintBordersFor(composite);

        final Button addModelButton = toolkit.createButton(composite, "Add...", SWT.NONE);
        addModelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Button removeModelButton = toolkit.createButton(composite, "Remove", SWT.NONE);
        removeModelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Button moveModelUpButton = toolkit.createButton(composite, "Up", SWT.NONE);
        moveModelUpButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Button moveDownButton = toolkit.createButton(composite, "Down", SWT.NONE);
        moveDownButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Button propertiesButton = toolkit.createButton(composite, "Properties...", SWT.NONE);
        propertiesButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));

        final Section optionsSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR);
        optionsSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        optionsSection.setText("Options");

        final Composite optionsComposite = toolkit.createComposite(optionsSection, SWT.NONE);
        optionsComposite.setBounds(0, 0, 0, 0);
        optionsComposite.setLayout(new GridLayout());
        toolkit.paintBordersFor(optionsComposite);
        optionsSection.setClient(optionsComposite);

        final Button validateModelsCheck = toolkit.createButton(optionsComposite, "Validate models", SWT.CHECK);
    }
}
