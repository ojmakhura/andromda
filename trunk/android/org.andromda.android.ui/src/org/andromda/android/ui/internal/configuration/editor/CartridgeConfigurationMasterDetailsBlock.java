package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.AndroidModel;
import org.andromda.android.core.internal.AndroidModelManager;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.configuration.NamespacesDocument.Namespaces;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * @author Peter Friese
 * @since 08.11.2005
 */
public class CartridgeConfigurationMasterDetailsBlock
        extends MasterDetailsBlock
{

    private Section cartridgesSection;

    class TreeLabelProvider
            extends LabelProvider
    {
        public String getText(Object element)
        {
            if (element instanceof Namespace)
            {
                Namespace namespace = (Namespace)element;
                return namespace.getName();
            }
            else if (element instanceof NamespacePropertyContainer)
            {
                NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)element;
                PropertyGroup propertyGroup = namespacePropertyContainer.getPropertyGroup();
                return propertyGroup.getName();
            }
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

        /**
         * Empty array is used by the content provider for empty nodes.
         */
        private Object[] EMPTY_ARRAY = new Object[] {};

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
            if (inputElement instanceof AndromdaDocument)
            {
                AndromdaDocument andromdaDocument = (AndromdaDocument)inputElement;
                Namespaces namespaces = andromdaDocument.getAndromda().getNamespaces();
                return namespaces.getNamespaceArray();
            }
            return null;
        }

        public Object[] getChildren(Object parentElement)
        {
            if (parentElement instanceof Namespace)
            {
                Namespace namespace = (Namespace)parentElement;
                IAndroidProject androidProject = getAndroidProject();
                PropertyGroup[] cartridgePropertyGroups = AndroidCore.getCartridgePropertyGroups(namespace, androidProject);
                if (cartridgePropertyGroups != null)
                {
                    NamespacePropertyContainer[] namespacePropertyContainers = new NamespacePropertyContainer[cartridgePropertyGroups.length];
                    for (int i = 0; i < cartridgePropertyGroups.length; i++)
                    {
                        PropertyGroup propertyGroup = cartridgePropertyGroups[i];
                        NamespacePropertyContainer namespacePropertyContainer = new NamespacePropertyContainer(
                                propertyGroup, namespace);
                        namespacePropertyContainers[i] = namespacePropertyContainer;
                    }
                    return namespacePropertyContainers;
                }
                else
                {
                    return EMPTY_ARRAY;
                }
            }
            return EMPTY_ARRAY;
        }

        public Object getParent(Object element)
        {
            return null;
        }

        public boolean hasChildren(Object element)
        {
            Object[] children = getChildren(element);
            if (children != null)
            {
                return children.length > 0;
            }
            return false;
        }
    }

    private TreeViewer treeViewer;

    private Tree tree;

    private GenericAndroidDetailsPage androidDetailsPage;

    private final CartridgeConfigurationPage parentPage;

    public CartridgeConfigurationMasterDetailsBlock(CartridgeConfigurationPage parentPage)
    {
        this.parentPage = parentPage;
        androidDetailsPage = new GenericAndroidDetailsPage();
    }
    
    /**
     * @see org.eclipse.ui.forms.MasterDetailsBlock#createContent(org.eclipse.ui.forms.IManagedForm)
     */
    public void createContent(IManagedForm managedForm)
    {
        super.createContent(managedForm);
        sashForm.setWeights(new int[] {33, 67});
    }

    protected void createMasterPart(final IManagedForm managedForm,
        Composite parent)
    {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 5;
        parent.setLayout(gridLayout);
        FormToolkit toolkit = managedForm.getToolkit();

        cartridgesSection = toolkit.createSection(parent, Section.DESCRIPTION | Section.EXPANDED | Section.TITLE_BAR);
        cartridgesSection.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        cartridgesSection.setDescription("Select a cartridge to configure.");
        cartridgesSection.setText("Cartridges");
        cartridgesSection.marginWidth = 5;
        cartridgesSection.marginHeight = 5;

        final Composite composite = toolkit.createComposite(cartridgesSection, SWT.NONE);
        composite.setLayout(new GridLayout());
        toolkit.paintBordersFor(composite);
        cartridgesSection.setClient(composite);

        treeViewer = new TreeViewer(composite, SWT.BORDER);
        treeViewer.setLabelProvider(new TreeLabelProvider());
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                doSelectionChanged(e);
            }

            private void doSelectionChanged(SelectionChangedEvent e)
            {
                IStructuredSelection selection = (IStructuredSelection)e.getSelection();
                Object element = selection.getFirstElement();
                StructuredSelection structuredSelection = null;
                if (element instanceof NamespacePropertyContainer)
                {
                    NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)element;
                    structuredSelection = new StructuredSelection(namespacePropertyContainer);
                }
                else if (element instanceof Namespace)
                {
                    Namespace namespace = (Namespace)element;
                    structuredSelection = new StructuredSelection(namespace);
                }
                // managedForm.fireSelectionChanged(this, structuredSelection);
                detailsPart.selectionChanged(null, structuredSelection);
            }
        });
        treeViewer.setContentProvider(new TreeContentProvider());
        tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        treeViewer.setInput(getAndromdaDocument());
    }

    /**
     * @return
     */
    private AndromdaDocument getAndromdaDocument()
    {
        ConfigurationEditor editor = (ConfigurationEditor)this.parentPage.getEditor();
        return editor.getDocument();
    }

    private IAndroidProject getAndroidProject()
    {
        ConfigurationEditor editor = (ConfigurationEditor)this.parentPage.getEditor();
        IResource adapter = (IResource)editor.getEditorInput().getAdapter(IResource.class);
        return AndroidModelManager.getInstance().getAndroidModel().getAndroidProject(adapter);
    }

    protected void registerPages(DetailsPart detailsPart)
    {
        detailsPart.setPageProvider(new IDetailsPageProvider()
        {
            /**
             * @see org.eclipse.ui.forms.IDetailsPageProvider#getPageKey(java.lang.Object)
             */
            public Object getPageKey(Object object)
            {
                System.out.println("getPageKey(): " + object.getClass());
                return object;
            }

            /**
             * @see org.eclipse.ui.forms.IDetailsPageProvider#getPage(java.lang.Object)
             */
            public IDetailsPage getPage(Object key)
            {
                System.out.println("getPage(): " + key.getClass());
                if (key instanceof Namespace)
                {
                    return null;
                }
                if (key instanceof NamespacePropertyContainer)
                {
                    return androidDetailsPage;
                }
                else
                {
                    return null;
                }
            }
        });
    }

    protected void createToolBarActions(IManagedForm managedForm)
    {
    }

}
