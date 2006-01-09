package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelComposite;
import org.andromda.android.ui.internal.editor.AbstractModelSectionPart;
import org.andromda.android.ui.internal.util.DialogUtils;
import org.andromda.android.ui.internal.util.SWTResourceManager;
import org.andromda.core.configuration.ModelDocument.Model;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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
 *
 * @author Peter Friese
 * @since 12.12.2005
 */
public class ModelDetailsComposite
        extends AbstractAndromdaModelComposite
{

    private Button removeButton;

    private Button downButton;

    private Button upButton;

    /** Move the URI up. */
    private static final int MOVE_DIRECTION_UP = -1;

    /** Move the URI down. */
    private static final int MOVE_DIRECTION_DOWN = 1;

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
            return SWTResourceManager.getPluginImage(AndroidUIPlugin.getDefault(), "/icons/modelfile.gif");
        }
    }

    private Table table;

    private Model model;

    public ModelDetailsComposite(final SectionPart parentSection,
        int style)
    {
        super(parentSection, style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        setLayout(gridLayout);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final Label label_2 = toolkit.createLabel(this, "Last modified check:", SWT.NONE);
        label_2.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        label_2.setLayoutData(new GridData());

        lastModifiedCheckButton = toolkit.createButton(this, "", SWT.CHECK);
        lastModifiedCheckButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                boolean selected = lastModifiedCheckButton.getSelection();
                model.setLastModifiedCheck(selected);
                getParentSection().markDirty();
                publishChangeEvent();
            }
        });
        lastModifiedCheckButton.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        lastModifiedCheckButton.setLayoutData(new GridData());
        new Label(this, SWT.NONE);

        final Label label = toolkit.createLabel(this, "Model type:", SWT.NONE);
        label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        modelTypeComboViewer = new ComboViewer(this, SWT.NONE);
        modelTypeComboViewer.setLabelProvider(new ListLabelProvider());
        modelTypeComboViewer.setContentProvider(new ContentProvider_1());
        modelTypeComboViewer.setInput(new Object());
        new Label(this, SWT.NONE);

        final Label label_1 = toolkit.createLabel(this, "Model files:", SWT.NONE);
        label_1.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        modelFilesTableViewer = new TableViewer(this, SWT.BORDER);
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
        addButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                if (parentSection instanceof AbstractModelSectionPart)
                {
                    IFile file = DialogUtils.selectResource(getShell(), getProject(), "Select model files...",
                            "Select one or more model files to be included in the generation process.");
                    if (file != null)
                    {
                        String uri = file.getProjectRelativePath().toString();

                        model.addUri(uri);
                        getParentSection().markDirty();
                        publishChangeEvent();
                    }
                }
            }
        });
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        removeButton = toolkit.createButton(tableButtons, "Remove", SWT.NONE);
        removeButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String uri = getSelectedUri();
                if (uri != null)
                {
                    int i = ArrayUtils.indexOf(model.getUriArray(), uri);
                    model.removeUri(i);
                    publishChangeEvent();
                }
            }
        });
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        upButton = toolkit.createButton(tableButtons, "Up", SWT.NONE);
        upButton.addSelectionListener(new SelectionAdapter()
        {

            public void widgetSelected(SelectionEvent e)
            {
                String uri = getSelectedUri();
                if (uri != null)
                {
                    moveUri(uri, MOVE_DIRECTION_UP);
                }
            }
        });
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        downButton = toolkit.createButton(tableButtons, "Down", SWT.NONE);
        downButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                String uri = getSelectedUri();
                if (uri != null)
                {
                    moveUri(uri, MOVE_DIRECTION_DOWN);
                }
            }
        });
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

    private boolean isLastModifiedCheck()
    {
        return lastModifiedCheckButton.getSelection();
    }

    private void setLastModifiedCheck(boolean checked)
    {
        lastModifiedCheckButton.setSelection(checked);
    }

    /**
     * @param modelUris
     */
    private void setModelUris(String[] modelUris)
    {
        modelFilesTableViewer.setInput(modelUris);
    }

    /**
     * @param type
     */
    private void setModelType(String type)
    {
    }

    /**
     * @param model
     */
    public void setModel(Model model)
    {
        // store for later reference
        this.model = model;

        refresh();
    }

    /**
     * TODO this method should refactored into a base class and invoked by a model change listener
     */
    private void refresh()
    {
        // last modified check
        boolean lastModifiedCheck = model.getLastModifiedCheck();
        setLastModifiedCheck(lastModifiedCheck);

        // model parts (URIs)
        String[] modelUris = model.getUriArray();
        setModelUris(modelUris);

        // model type
        String type = model.getType();
        setModelType(type);

        // enable / disabled buttons
        updateButtonStates();
    }

    /**
     * Determine the selected URI.
     *
     * @return The selected URI represented as a String.
     */
    private String getSelectedUri()
    {
        ISelection selection = modelFilesTableViewer.getSelection();
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Object firstElement = structuredSelection.getFirstElement();
            if (firstElement != null)
            {
                return firstElement.toString();
            }
        }
        return null;
    }

    /**
     * Moves the given URI around in the model.
     *
     * @param uri The URI to move.
     * @param direction The direction of the move.
     */
    private void moveUri(String uri,
        int direction)
    {
        int i = ArrayUtils.indexOf(model.getUriArray(), uri);
        String selectedUri = model.getUriArray(i);
        model.removeUri(i);
        model.insertUri(i + direction, selectedUri);
        getParentSection().markDirty();
        publishChangeEvent();
    }

    /**
     *
     */
    private void updateButtonStates()
    {
        String uri = getSelectedUri();
        int i = ArrayUtils.indexOf(model.getUriArray(), uri);
        boolean remove = true;
        boolean up = true;
        boolean down = true;
        if (i == 0)
        {
            up = false;
        }
        if (i == model.getUriArray().length - 1)
        {
            down = false;
        }
        if (i == -1)
        {
            remove = false;
        }
        removeButton.setEnabled(remove);
        upButton.setEnabled(up);
        downButton.setEnabled(down);
    }

}
