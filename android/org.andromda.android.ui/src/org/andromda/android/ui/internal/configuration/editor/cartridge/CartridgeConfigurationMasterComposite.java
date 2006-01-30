package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelComposite;
import org.andromda.android.ui.internal.util.SWTResourceManager;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.configuration.NamespacesDocument.Namespaces;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 *
 * @author Peter Friese
 * @since 14.12.2005
 */
public class CartridgeConfigurationMasterComposite
        extends AbstractAndromdaModelComposite
{

    private TreeViewer cartridgeConfigurationTreeViewer;

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
            if (element instanceof Namespace)
            {
                return SWTResourceManager.getPluginImage(AndroidUIPlugin.getDefault(), "icons/cartridge.gif");
            }
            else if (element instanceof NamespacePropertyContainer)
            {
                return SWTResourceManager.getPluginImage(AndroidUIPlugin.getDefault(), "icons/section.gif");
            }
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
            AndromdaDocument andromdaDocument = null;
            if (inputElement instanceof IAndromdaDocumentEditorModel)
            {
                IAndromdaDocumentEditorModel andromdaDocumentModel = (IAndromdaDocumentEditorModel)inputElement;
                andromdaDocument = andromdaDocumentModel.getAndromdaDocument();
            }
            else if (inputElement instanceof AndromdaDocument)
            {
                andromdaDocument = (AndromdaDocument)inputElement;
            }

            Namespaces namespaces = andromdaDocument.getAndromda().getNamespaces();
            return namespaces.getNamespaceArray();
        }

        public Object[] getChildren(Object parentElement)
        {
            if (parentElement instanceof Namespace)
            {
                Namespace namespace = (Namespace)parentElement;
                IAndroidProject androidProject = getAndroidProject();
                PropertyGroup[] cartridgePropertyGroups = AndroidCore.getCartridgePropertyGroups(namespace,
                        androidProject);
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

    private Tree cartridgeConfigurationTree;

    private AndromdaDocument andromdaDocument;

    /**
     * Creates a new CartridgeConfigurationMasterComposite.
     *
     * @param parentSection
     * @param style
     */
    public CartridgeConfigurationMasterComposite(final SectionPart parentSection,
        final int style)
    {
        super(parentSection, style);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        this.setLayout(gridLayout_1);

        cartridgeConfigurationTreeViewer = new TreeViewer(this, SWT.BORDER);
        cartridgeConfigurationTreeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                doSelectionChanged(e);
            }

            private void doSelectionChanged(SelectionChangedEvent e)
            {
                IStructuredSelection selection = (IStructuredSelection)e.getSelection();
                getParentSection().getManagedForm().fireSelectionChanged(parentSection, selection);
            }
        });
        cartridgeConfigurationTreeViewer.setLabelProvider(new TreeLabelProvider());
        cartridgeConfigurationTreeViewer.setContentProvider(new TreeContentProvider());
        cartridgeConfigurationTree = cartridgeConfigurationTreeViewer.getTree();
        cartridgeConfigurationTree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        final Composite buttonsComposite = toolkit.createComposite(this, SWT.NONE);
        buttonsComposite.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        buttonsComposite.setLayout(gridLayout_2);
        toolkit.paintBordersFor(buttonsComposite);

        final Button addButton = toolkit.createButton(buttonsComposite, "Add...", SWT.NONE);
        addButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button removeButton = toolkit.createButton(buttonsComposite, "Remove", SWT.NONE);
        removeButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button upButton = toolkit.createButton(buttonsComposite, "Up", SWT.NONE);
        upButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button downButton = toolkit.createButton(buttonsComposite, "Down", SWT.NONE);
        downButton.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        //
    }

    /**
     * {@inheritDoc}
     */
    public void dispose()
    {
        super.dispose();
    }

    /**
     * {@inheritDoc}
     */
    protected void checkSubclass()
    {
    }

    /**
     * @param document
     */
    public void setAndroMDADocument(final AndromdaDocument document)
    {
        this.andromdaDocument = document;
        cartridgeConfigurationTreeViewer.setInput(document);
    }

    /**
     * @param event
     */
    public void modelChanged(final IModelChangedEvent event)
    {
    }

}
