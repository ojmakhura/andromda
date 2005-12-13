package org.andromda.android.ui.internal.configuration.editor.model;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 *
 * @author Peter Friese
 * @since 12.12.2005
 */
public class ModelDetailsComposite
        extends Composite
{

    class ListLabelProvider
            extends LabelProvider
    {
        public String getText(Object element)
        {
            return element.toString();
        }

        public Image getImage(Object element)
        {
            return null;
        }
    }

    class ContentProvider_1
            implements IStructuredContentProvider
    {
        public Object[] getElements(Object inputElement)
        {
            return new Object[] { "UML 1.4", "UML 2.0" };
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

    private TableViewer modelFilesTableViewer;

    private ComboViewer modelTypeComboViewer;

    private Button lastModifiedCheckButton;

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

    class ContentProvider
            implements IStructuredContentProvider
    {
        public Object[] getElements(Object inputElement)
        {
            return new Object[] { "mda/src/uml/model1.zip", "mda/src/uml/model2.zip", "mda/src/uml/model3.zip", "mda/src/uml/model4.zip" };
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

    private Table table;

    public ModelDetailsComposite(SectionPart parent,
        int style)
    {
        super(parent.getSection(), style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        lastModifiedCheckButton = toolkit.createButton(this, "Last modified check", SWT.CHECK);
        lastModifiedCheckButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
        new Label(this, SWT.NONE);

        toolkit.createLabel(this, "Model type:", SWT.NONE);

        modelTypeComboViewer = new ComboViewer(this, SWT.READ_ONLY);
        modelTypeComboViewer.setLabelProvider(new ListLabelProvider());
        modelTypeComboViewer.setContentProvider(new ContentProvider_1());
        modelTypeComboViewer.setInput(new Object());
        new Label(this, SWT.NONE);

        toolkit.createLabel(this, "Model files:", SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        modelFilesTableViewer = new TableViewer(this, SWT.BORDER);
        modelFilesTableViewer.setLabelProvider(new TableLabelProvider());
        modelFilesTableViewer.setContentProvider(new ContentProvider());
        table = modelFilesTableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        modelFilesTableViewer.setInput(new Object());

        final Composite tableButtons = toolkit.createComposite(this, SWT.NONE);
        tableButtons.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        tableButtons.setLayout(new GridLayout());
        toolkit.paintBordersFor(tableButtons);

        final Button addButton = toolkit.createButton(tableButtons, "Add...", SWT.NONE);
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button removeButton = toolkit.createButton(tableButtons, "Remove", SWT.NONE);
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button upButton = toolkit.createButton(tableButtons, "Up", SWT.NONE);
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button downButton = toolkit.createButton(tableButtons, "Down", SWT.NONE);
        downButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        //
    }

    public void dispose()
    {
        super.dispose();
    }

    protected void checkSubclass()
    {
    }

    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheckButton.getSelection();
    }

    public void setLastModifiedCheck(boolean checked)
    {
        lastModifiedCheckButton.setSelection(checked);
    }

}
