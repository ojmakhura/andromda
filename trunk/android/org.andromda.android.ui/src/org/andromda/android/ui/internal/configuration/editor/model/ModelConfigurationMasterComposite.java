package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.ui.internal.editor.AbstractModelComposite;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.ModelDocument.Model;
import org.andromda.core.configuration.ModelsDocument.Models;
import org.andromda.core.configuration.RepositoriesDocument.Repositories;
import org.andromda.core.configuration.RepositoryDocument.Repository;
import org.andromda.core.configuration.TransformationDocument.Transformation;
import org.andromda.core.configuration.TransformationsDocument.Transformations;
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
public class ModelConfigurationMasterComposite
        extends AbstractModelComposite
{

    private TreeViewer modelConfigurationTreeViewer;

    class TreeLabelProvider
            extends LabelProvider
    {
        public String getText(Object element)
        {
            if (element instanceof Repository)
            {
                Repository repository = (Repository)element;
                return "[r] " + repository.getName();
            }
            else if (element instanceof Model)
            {
                Model model = (Model)element;
                String[] uris = model.getUriArray();
                String uri;
                if (uris.length > 0) {
                    uri = model.getUriArray(0);
                }
                else {
                    uri = "(no model file specified)";
                }
                return "[m] " + uri;
            }
            else if (element instanceof Transformation)
            {
                Transformation transformation = (Transformation)element;
                return "[t] " + transformation.getUri();
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
                Repositories repositories = andromdaDocument.getAndromda().getRepositories();
                return repositories.getRepositoryArray();
            }
            return null;
        }

        public Object[] getChildren(Object parentElement)
        {
            if (parentElement instanceof Repository)
            {
                Repository repository = (Repository)parentElement;
                Models models = repository.getModels();
                return models.getModelArray();
            }
            else if (parentElement instanceof Model)
            {
                Model model = (Model)parentElement;
                Transformations transformations = model.getTransformations();
                if (transformations != null)
                {
                    return transformations.getTransformationArray();
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
            return getChildren(element).length > 0;
        }
    }

    private Tree modelConfigurationTree;

    private AndromdaDocument andromdaDocument;

    public ModelConfigurationMasterComposite(final SectionPart parentSection,
        int style)
    {
        super(parentSection, style);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        this.setLayout(gridLayout_1);

        modelConfigurationTreeViewer = new TreeViewer(this, SWT.BORDER);
        modelConfigurationTreeViewer.addSelectionChangedListener(new ISelectionChangedListener()
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
        modelConfigurationTreeViewer.setLabelProvider(new TreeLabelProvider());
        modelConfigurationTreeViewer.setContentProvider(new TreeContentProvider());
        modelConfigurationTree = modelConfigurationTreeViewer.getTree();
        modelConfigurationTree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

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

    public void dispose()
    {
        super.dispose();
    }

    protected void checkSubclass()
    {
    }

    /**
     * @param document
     */
    public void setAndroMDADocument(AndromdaDocument document)
    {
        this.andromdaDocument = document;
        modelConfigurationTreeViewer.setInput(document);
    }

    /**
     * @see org.andromda.android.ui.internal.editor.AbstractModelComposite#modelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public void modelChanged(IModelChangedEvent event)
    {
        // TODO Auto-generated method stub

    }

}
