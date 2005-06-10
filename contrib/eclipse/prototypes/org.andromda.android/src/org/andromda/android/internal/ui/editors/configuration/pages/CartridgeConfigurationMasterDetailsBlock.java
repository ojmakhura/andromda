/**
 *
 */
package org.andromda.android.internal.ui.editors.configuration.pages;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 27.05.2005
 */
public class CartridgeConfigurationMasterDetailsBlock
        extends MasterDetailsBlock
{

    class TableLabelProvider
            extends LabelProvider
            implements ITableLabelProvider
    {
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
            return new String[] { "item_0", "item_1", "item_2" };
        }

        public void dispose()
        {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
        }
    }

    private Table table;

    private IDetailsPage springCartridgeDetailsPage;

    public CartridgeConfigurationMasterDetailsBlock()
    {
        springCartridgeDetailsPage = new SpringCartridgeDetailsPage();
    }

    protected void createMasterPart(IManagedForm managedForm, Composite parent)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        final Section section = toolkit.createSection(parent, Section.DESCRIPTION
                | Section.TITLE_BAR);
        section.setDescription("Select a cartridge to configure:");
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(gridData);
        section.setText("Cartridges");

        final Composite composite = toolkit.createComposite(section, SWT.NONE);
        composite.setLayout(new GridLayout());
        toolkit.paintBordersFor(composite);
        section.setClient(composite);

        final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite,
                SWT.CHECK);
        checkboxTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                ISelection selection = e.getSelection();
                detailsPart.selectionChanged(null, selection);
            }
        });
        checkboxTableViewer.setLabelProvider(new TableLabelProvider());
        checkboxTableViewer.setContentProvider(new ContentProvider());
        checkboxTableViewer.setInput(new Object());
        table = checkboxTableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(200);
        tableColumn.setText("Cartridge");
    }

    protected void registerPages(DetailsPart detailsPart)
    {
        detailsPart.registerPage(String.class, springCartridgeDetailsPage);
    }

    protected void createToolBarActions(IManagedForm managedForm)
    {
    }

}
