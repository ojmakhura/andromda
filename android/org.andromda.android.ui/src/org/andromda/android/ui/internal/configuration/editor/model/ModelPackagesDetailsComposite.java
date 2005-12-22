package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.ui.internal.editor.AbstractModelComposite;
import org.andromda.core.configuration.ModelPackagesDocument.ModelPackages;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
 * This composite contains control that allow the user to configure which model packages will be generated.
 *
 * @author Peter Friese
 * @since 22.12.2005
 */
public class ModelPackagesDetailsComposite
        extends AbstractModelComposite
{

    private Button removeButton;

    private Button downButton;

    private Button upButton;

    /** Move the URI up. */
    private static final int MOVE_DIRECTION_UP = -1;

    /** Move the URI down. */
    private static final int MOVE_DIRECTION_DOWN = 1;

    private CheckboxTableViewer modelFilesTableViewer;

    private Button processAllPackagesCheckButton;

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

    private ModelPackages modelPackages;

    public ModelPackagesDetailsComposite(final SectionPart parentSection,
        int style)
    {
        super(parentSection, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final Label processAllLabel = toolkit.createLabel(this, "Process all packages:", SWT.NONE);
        processAllLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        processAllLabel.setLayoutData(new GridData());

        processAllPackagesCheckButton = toolkit.createButton(this, "", SWT.CHECK);
        processAllPackagesCheckButton.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        processAllPackagesCheckButton.setLayoutData(new GridData());
        new Label(this, SWT.NONE);
        final Label modelPackagesLabel = toolkit.createLabel(this, "Model packages:", SWT.NONE);
        modelPackagesLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        modelFilesTableViewer = CheckboxTableViewer.newCheckList(this, SWT.BORDER);
        modelFilesTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                updateButtonStates();
            }
        });
        modelFilesTableViewer.setLabelProvider(new TableLabelProvider());
        modelFilesTableViewer.setContentProvider(new ArrayContentProvider());
        table = modelFilesTableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        modelFilesTableViewer.setInput(new Object());

        final Composite tableButtons = toolkit.createComposite(this, SWT.NONE);
        tableButtons.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginHeight = 0;
        tableButtons.setLayout(gridLayout_1);
        toolkit.paintBordersFor(tableButtons);

        final Button addButton = toolkit.createButton(tableButtons, "Add...", SWT.NONE);
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        removeButton = toolkit.createButton(tableButtons, "Remove", SWT.NONE);
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        upButton = toolkit.createButton(tableButtons, "Up", SWT.NONE);
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        downButton = toolkit.createButton(tableButtons, "Down", SWT.NONE);
        downButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        //
        subscribeToModelChanges();
    }

    /**
     * Subscribe to model change events.
     */
    private void subscribeToModelChanges()
    {
        if (getModel() instanceof IModelChangeProvider)
        {
            IModelChangeProvider provider = (IModelChangeProvider) getModel();
            provider.addModelChangedListener(this);
        }
    }

    public void dispose()
    {
        super.dispose();
    }

    protected void checkSubclass()
    {
    }

    /**
     * @param model
     */
    public void setModelPackages(ModelPackages modelPackages)
    {
        // store for later reference
        this.modelPackages = modelPackages;

        refresh();
    }

    /**
     * TODO this method should refactored into a base class and invoked by a model change listener
     */
    private void refresh()
    {
        boolean processAll = modelPackages.getProcessAll();
//        setProcessAll(processAll);

        // enable / disabled buttons
        updateButtonStates();
    }

    /**
     *
     */
    private void updateButtonStates()
    {
        boolean remove = true;
        boolean up = true;
        boolean down = true;

        // check conditions

        removeButton.setEnabled(remove);
        upButton.setEnabled(up);
        downButton.setEnabled(down);
    }

    /**
     * @see org.andromda.android.ui.internal.editor.AbstractModelComposite#modelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public void modelChanged(IModelChangedEvent event)
    {
        refresh();
    }

}
