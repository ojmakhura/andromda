package org.andromda.android.ui.internal.project.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.core.project.cartridge.ProjectCartridgeRegistry;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.settings.preferences.AndroMDALocationsPreferencePage;
import org.andromda.android.ui.internal.settings.preferences.AndroidProjectLayoutPreferencePage;
import org.andromda.android.ui.internal.widgets.FormBrowser;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
public class ChooseProjectCartridgeWizardPage
        extends WizardSelectionPage
{

    /** This link will be displayed if the user didn't setup the cartidge directory. */
    private Link youNeedToLink;

    /** This table viewer contains the list of project cartridges. */
    private TableViewer tableViewer;

    class TableLabelProvider
            extends LabelProvider
            implements ITableLabelProvider
    {
        public String getColumnText(Object element,
            int columnIndex)
        {
            if (element instanceof IProjectCartridgeDescriptor)
            {
                IProjectCartridgeDescriptor projectCartridgeDescriptor = (IProjectCartridgeDescriptor)element;
                try
                {
                    return projectCartridgeDescriptor.getType();
                }
                catch (CartridgeParsingException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
            return element.toString();
        }

        public Image getColumnImage(Object element,
            int columnIndex)
        {
            return null;
        }
    }

    private Table table;

    /** The project generator will be configured with this map. */
    private final Map projectProperties;

    private IProjectCartridgeDescriptor projectCartridgeDescriptor;

    private FormBrowser descriptionBrowser;

    /**
     * Create the wizard.
     *
     * @param projectCartridgeDescriptor
     * @param projectProperties the project generator will be configured with this map.
     */
    public ChooseProjectCartridgeWizardPage(final Map projectProperties)
    {
        super("wizardPage");
        this.projectProperties = projectProperties;
        setTitle("Project cartridges");
        setDescription("Select one of the available project cartridges to create your new project.");
    }

    /**
     * Create contents of the wizard.
     *
     * @param parent The parent composite.
     */
    public void createControl(final Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        container.setLayout(gridLayout);
        //
        setControl(container);

        final Label availablecartridgesLabel = new Label(container, SWT.NONE);
        availablecartridgesLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        availablecartridgesLabel.setText("Available &cartridges:");

        tableViewer = new TableViewer(container, SWT.BORDER);
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(final SelectionChangedEvent event)
            {
                setErrorMessage(null);
                IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                IProjectCartridgeDescriptor cartridgeDescriptor = (IProjectCartridgeDescriptor)selection
                        .getFirstElement();
                if (cartridgeDescriptor == null)
                {
                    setMessage(null);
                    setSelectedNode(null);
                    return;
                }
                else
                {
                    // @tag NewProjectWizard (project cartridge): Set project cartridge description
                    try
                    {
                        String documentation = StringUtils.trimToEmpty(cartridgeDescriptor.getDocumentation());
//                        documentation = "<p>This wizard creates standard plug-in directory structure and adds the following:</p> " +
//                                "<li><b>Sample Incremental Project Builder</b>. The sample builder checks XML files in the project and adds a problem marker to not well formed files.</li> " +
//                                "<li><b>Sample Project Nature</b>. This nature owns the builder.  Builder runs for projects of this nature.</li> " +
//                                "<li><b>Sample Problem Marker</b>. The builder uses this sub-type of a problem marker to mark errors.</li> " +
//                                "<li><b>Sample Popup Menu Action</b>. An action in a project context menu allows adding or removing the sample nature to or from a workspace project.</li> " +
//                                "<p><b>Extensions Used</b></p> " +
//                                "<li>org.eclipse.core.resources.builders</li> " +
//                                "<li>org.eclipse.core.resources.markers</li> " +
//                                "<li>org.eclipse.core.resources.natures</li> " +
//                                "<li>org.eclipse.ui.popupMenus</li>";
                        descriptionBrowser.setText(documentation);
                    }
                    catch (CartridgeParsingException e)
                    {
                        AndroidUIPlugin.log(e);
                    }
                }

                setSelectedProjectCartridge(cartridgeDescriptor);
            }

        });
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setInput(new Object());
        table = tableViewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        descriptionBrowser = new FormBrowser(container, SWT.BORDER);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        descriptionBrowser.setLayoutData(gridData);

        youNeedToLink = new Link(container, SWT.NONE);
        youNeedToLink.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        youNeedToLink
                .setText("You need to specify a root directory for AndroMDA cartridges. <a>Configure Android now.</a>");
        youNeedToLink.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(final SelectionEvent e)
            {
                PreferencesUtil.createPreferenceDialogOn(
                        getShell(),
                        AndroMDALocationsPreferencePage.PREFERENCEPAGE_ID,
                        new String[] { AndroMDALocationsPreferencePage.PREFERENCEPAGE_ID,
                                AndroidProjectLayoutPreferencePage.PREFERENCEPAGE_ID }, null).open();
                setupData();
            }
        });
        youNeedToLink.setVisible(false);

        setupData();
    }

    /**
     * @param projectCartridgeDescriptor
     */
    protected void setSelectedProjectCartridge(final IProjectCartridgeDescriptor projectCartridgeDescriptor)
    {
        this.projectCartridgeDescriptor = projectCartridgeDescriptor;
        IWizardNode node = new IWizardNode()
        {

            public void dispose()
            {
            }

            public Point getExtent()
            {
                return null;
            }

            public IWizard getWizard()
            {
                return new DynamicWizard(ChooseProjectCartridgeWizardPage.this.projectCartridgeDescriptor,
                        projectProperties);
            }

            public boolean isContentCreated()
            {
                return false;
            }

        };
        setSelectedNode(node);
    }

    /**
     *
     */
    private void setupData()
    {
        if (AndroidCore.getAndroidSettings().isConfigurationValid())
        {
            youNeedToLink.setVisible(false);
            try
            {
                getContainer().run(false, false, new IRunnableWithProgress()
                {

                    public void run(final IProgressMonitor monitor) throws InvocationTargetException,
                            InterruptedException
                    {
                        monitor.beginTask("Retrieving cartridges", 100);
                        monitor.worked(30);
                        IProjectCartridgeDescriptor[] cartridgeDescriptors = ProjectCartridgeRegistry.getInstance()
                                .getCartridgeDescriptors();
                        monitor.worked(60);

                        tableViewer.setInput(cartridgeDescriptors);
                        if (cartridgeDescriptors.length > 0)
                        {
                            tableViewer.setSelection(new StructuredSelection(cartridgeDescriptors[0]));
                        }
                        monitor.worked(10);
                        monitor.done();
                    }

                });
            }
            catch (InvocationTargetException e)
            {
                AndroidUIPlugin.log(e);
            }
            catch (InterruptedException e)
            {
                // that's ok
            }
        }
        else
        {
            youNeedToLink.setVisible(true);
        }
    }

    protected void focusAndSelectFirst() {
        Table table = tableViewer.getTable();
        table.setFocus();
        TableItem[] items = table.getItems();
        if (items.length > 0) {
            TableItem first = items[0];
            Object obj = first.getData();
            tableViewer.setSelection(new StructuredSelection(obj));
        }
    }

}
