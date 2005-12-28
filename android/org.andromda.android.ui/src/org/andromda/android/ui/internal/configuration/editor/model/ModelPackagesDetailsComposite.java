package org.andromda.android.ui.internal.configuration.editor.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelComposite;
import org.andromda.android.ui.internal.util.SWTResourceManager;
import org.andromda.core.configuration.ModelDocument.Model;
import org.andromda.core.configuration.ModelPackageDocument.ModelPackage;
import org.andromda.core.configuration.ModelPackagesDocument.ModelPackages;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
        extends AbstractAndromdaModelComposite
{

    /** Regular expressions for legal package names. */
    private static final Pattern PATTERN_PACKAGE = Pattern.compile("((\\w*)(::\\w+)*)*");

    private Button removeButton;

    private Button downButton;

    private Button upButton;

    private CheckboxTableViewer modelPackagesTableViewer;

    private Button processAllPackagesCheckButton;

    class TableLabelProvider
            extends LabelProvider
            implements ITableLabelProvider
    {
        public String getColumnText(Object element,
            int columnIndex)
        {
            if (element instanceof ModelPackage)
            {
                ModelPackage modelPackage = (ModelPackage)element;
                return modelPackage.getStringValue();
            }
            return element.toString();
        }

        public Image getColumnImage(Object element,
            int columnIndex)
        {
            return SWTResourceManager.getPluginImage(AndroidUIPlugin.getDefault(), "icons/package.gif");
        }
    }

    private Table table;

    private Model model;

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
        processAllPackagesCheckButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                boolean selected = processAllPackagesCheckButton.getSelection();
                getModelPackages().setProcessAll(selected);
                getParentSection().markDirty();
                publishChangeEvent();
            }
        });
        processAllPackagesCheckButton.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        processAllPackagesCheckButton.setLayoutData(new GridData());
        new Label(this, SWT.NONE);
        final Label modelPackagesLabel = toolkit.createLabel(this, "Model packages:", SWT.NONE);
        modelPackagesLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        modelPackagesTableViewer = CheckboxTableViewer.newCheckList(this, SWT.BORDER);
        modelPackagesTableViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                updateButtonStates();
            }
        });
        modelPackagesTableViewer.setLabelProvider(new TableLabelProvider());
        modelPackagesTableViewer.setContentProvider(new ArrayContentProvider());
        table = modelPackagesTableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        modelPackagesTableViewer.setInput(new Object());

        final Composite tableButtons = toolkit.createComposite(this, SWT.NONE);
        tableButtons.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginHeight = 0;
        tableButtons.setLayout(gridLayout_1);
        toolkit.paintBordersFor(tableButtons);

        final Button addButton = toolkit.createButton(tableButtons, "Add...", SWT.NONE);
        addButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                InputDialog dialog = new InputDialog(getShell(), "Enter package name",
                        "Enter the name of a package to be included:", null, new IInputValidator()
                        {
                            public String isValid(String newText)
                            {
                                Matcher matcher = PATTERN_PACKAGE.matcher(newText);
                                if (matcher.matches())
                                {
                                    return null;
                                }
                                else
                                {
                                    return "Illegal package name. Use :: to separate packages: org::andromda::test";
                                }
                            }
                        });
                if (dialog.open() == Window.OK)
                {
                    String packageName = dialog.getValue();
                    ModelPackage modelPackage = getModelPackages().addNewModelPackage();
                    modelPackage.setStringValue(packageName);
                    getParentSection().markDirty();
                    publishChangeEvent();
                }
            }
        });
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        removeButton = toolkit.createButton(tableButtons, "Remove", SWT.NONE);
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        upButton = toolkit.createButton(tableButtons, "Up", SWT.NONE);
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        downButton = toolkit.createButton(tableButtons, "Down", SWT.NONE);
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

    /**
     * TODO this method should refactored into a base class and invoked by a model change listener
     */
    private void refresh()
    {
        boolean processAll = getModelPackages().getProcessAll();
        setProcessAll(processAll);

        ModelPackages modelPackages = getModelPackages();
        setModelPackages(modelPackages);

        // enable / disabled buttons
        updateButtonStates();
    }

    /**
     * Sets the state of the "process all" checkbox.
     *
     * @param processAll Whether to process all model packages.
     */
    private void setProcessAll(boolean processAll)
    {
        processAllPackagesCheckButton.setSelection(processAll);
    }

    /**
     * @param modelPackages
     */
    private void setModelPackages(ModelPackages modelPackages)
    {
        ModelPackage[] modelPackageArray = modelPackages.getModelPackageArray();
        modelPackagesTableViewer.setInput(modelPackageArray);
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

    /**
     * @param model
     */
    public void setModel(Model model)
    {
        this.model = model;
        refresh();
    }

    /**
     * @return
     */
    private ModelPackages getModelPackages()
    {
        ModelPackages modelPackages = model.getModelPackages();
        if (modelPackages == null)
        {
            modelPackages = model.addNewModelPackages();
        }
        return modelPackages;
    }

}
