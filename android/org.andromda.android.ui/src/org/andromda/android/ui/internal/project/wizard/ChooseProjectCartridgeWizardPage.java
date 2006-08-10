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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
 * using this wizard, the user can choose a project cartridge to create a new AndroMDA project.
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

    /**
     * This label provider labels the project cartridge entries in the list.
     *
     * @author Peter Friese
     * @since 22.05.2006
     */
    class TableLabelProvider
            extends LabelProvider
            implements ITableLabelProvider
    {
        /**
         * {@inheritDoc}
         */
        public String getColumnText(final Object element,
            final int columnIndex)
        {
            if (element instanceof IProjectCartridgeDescriptor)
            {
                IProjectCartridgeDescriptor descriptor = (IProjectCartridgeDescriptor)element;
                try
                {
                    return descriptor.getType();
                }
                catch (CartridgeParsingException e)
                {
                    AndroidUIPlugin.log(e);
                    return null;
                }
            }
            return element.toString();
        }

        /**
         * {@inheritDoc}
         */
        public Image getColumnImage(final Object element,
            final int columnIndex)
        {
            return null;
        }
    }

    /** The project generator will be configured with this map. */
    private final Map projectProperties;

    /** The selected project cartride descriptor. */
    private IProjectCartridgeDescriptor projectCartridgeDescriptor;

    /** The browser widget that displays the project cartridge description. */
    private FormBrowser descriptionBrowser;

    /**
     * Create the wizard.
     *
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
        tableViewer.addDoubleClickListener(new IDoubleClickListener()
        {
            public void doubleClick(final DoubleClickEvent e)
            {
                advanceToNextPage();
            }

        });
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
        Table table = tableViewer.getTable();
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
     * Set the selected project cartridge selector.
     *
     * @param descriptor The project cartridge descriptor.
     */
    protected void setSelectedProjectCartridge(final IProjectCartridgeDescriptor descriptor)
    {
        this.projectCartridgeDescriptor = descriptor;
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
                        focusAndSelectFirst();
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

    /**
     * Selec the first entry in a list.
     */
    protected void focusAndSelectFirst()
    {
        Table table = tableViewer.getTable();
        table.setFocus();
        TableItem[] items = table.getItems();
        if (items.length > 0)
        {
            TableItem first = items[0];
            Object obj = first.getData();
            tableViewer.setSelection(new StructuredSelection(obj));
        }
    }

    /**
     * Advances to the next wizard page.
     */
    private void advanceToNextPage()
    {
        if (canFlipToNextPage())
        {
            getContainer().showPage(getNextPage());
        }
    }
}
