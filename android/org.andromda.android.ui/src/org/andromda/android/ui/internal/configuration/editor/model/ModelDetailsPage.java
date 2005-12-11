package org.andromda.android.ui.internal.configuration.editor.model;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * @author Peter Friese
 * @since 11.12.2005
 */
public class ModelDetailsPage
        implements IDetailsPage
{

    class TableLabelProvider_1 extends LabelProvider implements ITableLabelProvider {
        public String getColumnText(Object element, int columnIndex)
        {
            return element.toString();
        }
        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }
    }
    class ContentProvider_1 implements IStructuredContentProvider {
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
    private Table table_1;
    class TreeLabelProvider
            extends LabelProvider
    {
        public String getText(Object element)
        {
            return super.getText(element);
        }

        public Image getImage(Object element)
        {
            return null;
        }
    }

    class TreeContentProvider
            implements IStructuredContentProvider, ITreeContentProvider
    {
        public void inputChanged(Viewer viewer,
            Object oldInput,
            Object newInput)
        {
        }

        public void dispose()
        {
        }

        public Object[] getElements(Object inputElement)
        {
            return getChildren(inputElement);
        }

        public Object[] getChildren(Object parentElement)
        {
            return new Object[] { "item_0", "item_1", "item_2" };
        }

        public Object getParent(Object element)
        {
            return null;
        }

        public boolean hasChildren(Object element)
        {
            return getChildren(element).length > 0;
        }
    }

    private Tree tree;

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

        public void inputChanged(Viewer viewer,
            Object oldInput,
            Object newInput)
        {
        }
    }

    class TableLabelProvider
            extends LabelProvider
            implements ITableLabelProvider
    {
        public String getColumnText(Object element,
            int columnIndex)
        {
            return element.toString();
        }

        public Image getColumnImage(Object element,
            int columnIndex)
        {
            return null;
        }
    }

    private Table table;

    private IManagedForm managedForm;

    public ModelDetailsPage()
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
        parent.setLayout(gridLayout);

        final Section searchLocationsSection = toolkit.createSection(parent, Section.TWISTIE | Section.DESCRIPTION | Section.EXPANDED | Section.TITLE_BAR);
        searchLocationsSection.setDescription("Define where AndroMDA should look for model parts.");
        searchLocationsSection.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        searchLocationsSection.setText("Search Locations");

        final Composite composite = toolkit.createComposite(searchLocationsSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        composite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(composite);
        searchLocationsSection.setClient(composite);

        final TableViewer tableViewer = new TableViewer(composite, SWT.BORDER);
        tableViewer.setContentProvider(new ContentProvider());
        tableViewer.setLabelProvider(new TableLabelProvider());
        table = tableViewer.getTable();
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.heightHint = 110;
        table.setLayoutData(gridData);
        tableViewer.setInput(new Object());

        final Composite composite_1 = new Composite(composite, SWT.NONE);
        toolkit.adapt(composite_1);
        composite_1.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_4 = new GridLayout();
        gridLayout_4.marginHeight = 0;
        composite_1.setLayout(gridLayout_4);

        final Button addButton = toolkit.createButton(composite_1, "Add...", SWT.NONE);
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button removeButton = toolkit.createButton(composite_1, "Remove", SWT.NONE);
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button upButton = toolkit.createButton(composite_1, "Up", SWT.NONE);
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button = toolkit.createButton(composite_1, "Down", SWT.NONE);
        button.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Section modelPackagesSection = toolkit.createSection(parent, Section.TWISTIE | Section.DESCRIPTION | Section.EXPANDED | Section.TITLE_BAR);
        modelPackagesSection.setDescription("Defiine the packages to be considered during generation.");
        modelPackagesSection.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        modelPackagesSection.setText("Model Packages");

        final Composite composite_2 = toolkit.createComposite(modelPackagesSection, SWT.NONE);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.numColumns = 2;
        composite_2.setLayout(gridLayout_2);
        toolkit.paintBordersFor(composite_2);
        modelPackagesSection.setClient(composite_2);

        final CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(composite_2, SWT.BORDER);
        checkboxTreeViewer.setLabelProvider(new TreeLabelProvider());
        checkboxTreeViewer.setContentProvider(new TreeContentProvider());
        tree = checkboxTreeViewer.getTree();
        tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        checkboxTreeViewer.setInput(new Object());

        final Composite composite_3 = toolkit.createComposite(composite_2, SWT.NONE);
        composite_3.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_5 = new GridLayout();
        gridLayout_5.marginHeight = 0;
        composite_3.setLayout(gridLayout_5);
        toolkit.paintBordersFor(composite_3);

        final Button button_4 = toolkit.createButton(composite_3, "Add..", SWT.NONE);
        button_4.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_3 = toolkit.createButton(composite_3, "Remove", SWT.NONE);
        button_3.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_2 = toolkit.createButton(composite_3, "Up", SWT.NONE);
        button_2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_1 = toolkit.createButton(composite_3, "Down", SWT.NONE);
        button_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Section constraintsSection = toolkit.createSection(parent, Section.TWISTIE | Section.DESCRIPTION | Section.EXPANDED | Section.TITLE_BAR);
        constraintsSection.setDescription("Defiine the constraints for this model.");
        constraintsSection.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        constraintsSection.setText("Constraints");

        final Composite composite_4 = toolkit.createComposite(constraintsSection, SWT.NONE);
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.numColumns = 2;
        composite_4.setLayout(gridLayout_3);
        toolkit.paintBordersFor(composite_4);
        constraintsSection.setClient(composite_4);

        final TableViewer tableViewer_1 = new TableViewer(composite_4, SWT.BORDER);
        tableViewer_1.setLabelProvider(new TableLabelProvider_1());
        tableViewer_1.setContentProvider(new ContentProvider_1());
        table_1 = tableViewer_1.getTable();
        table_1.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        tableViewer_1.setInput(new Object());

        final Composite composite_7 = toolkit.createComposite(composite_4, SWT.NONE);
        composite_7.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_6 = new GridLayout();
        gridLayout_6.marginHeight = 0;
        composite_7.setLayout(gridLayout_6);
        toolkit.paintBordersFor(composite_7);

        final Button button_5 = toolkit.createButton(composite_7, "Add...", SWT.NONE);
        button_5.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_6 = toolkit.createButton(composite_7, "Remove", SWT.NONE);
        button_6.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_7 = toolkit.createButton(composite_7, "Up", SWT.NONE);
        button_7.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_8 = toolkit.createButton(composite_7, "Down", SWT.NONE);
        button_8.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    public boolean isDirty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
     */
    public boolean setFormInput(Object input)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#setFocus()
     */
    public void setFocus()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#isStale()
     */
    public boolean isStale()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#refresh()
     */
    public void refresh()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IFormPart part,
        ISelection selection)
    {
        // TODO Auto-generated method stub
        
    }
}
