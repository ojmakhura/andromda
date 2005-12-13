package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.internal.editor.BaseMasterDetailsBlock;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 11.12.2005
 */
public class ModelConfigurationMasterDetailsBlock
        extends BaseMasterDetailsBlock
{

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
                String firstUri = model.getUriArray(0);
                return "[m] " + firstUri;
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
                return transformations.getTransformationArray();
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

    private Tree tree;

    private ModelDetailsPage modelDetailsPage;

    public ModelConfigurationMasterDetailsBlock(FormPage parentPage)
    {
        setParentPage(parentPage);
        modelDetailsPage = new ModelDetailsPage();
    }

    protected void createMasterPart(IManagedForm managedForm,
        Composite parent)
    {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 5;
        parent.setLayout(gridLayout);
        FormToolkit toolkit = managedForm.getToolkit();

        final Section modelsSection = toolkit.createSection(parent, Section.DESCRIPTION | Section.EXPANDED
                | Section.TITLE_BAR);
        modelsSection.setDescription("Configure the model(s) to be processed by AndroMDA.");
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        modelsSection.setLayoutData(gridData);
        modelsSection.setText("Models");
        modelsSection.marginWidth = 5;
        modelsSection.marginHeight = 5;

        final Composite composite = toolkit.createComposite(modelsSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        composite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(composite);
        modelsSection.setClient(composite);

        final TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            public void selectionChanged(SelectionChangedEvent e)
            {
                doSelectionChanged(e);
            }

            private void doSelectionChanged(SelectionChangedEvent e)
            {
                IStructuredSelection selection = (IStructuredSelection)e.getSelection();
                detailsPart.selectionChanged(null, selection);

            }
        });
        treeViewer.setLabelProvider(new TreeLabelProvider());
        treeViewer.setContentProvider(new TreeContentProvider());
        tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        treeViewer.setInput(getAndromdaDocument());

        final Composite composite_1 = toolkit.createComposite(composite, SWT.NONE);
        composite_1.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginHeight = 0;
        composite_1.setLayout(gridLayout_2);
        toolkit.paintBordersFor(composite_1);

        final Button button = toolkit.createButton(composite_1, "Add...", SWT.NONE);
        button.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_1 = toolkit.createButton(composite_1, "Remove", SWT.NONE);
        button_1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_2 = toolkit.createButton(composite_1, "Up", SWT.NONE);
        button_2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button button_3 = toolkit.createButton(composite_1, "Down", SWT.NONE);
        button_3.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    protected void registerPages(DetailsPart detailsPart)
    {
        detailsPart.setPageProvider(new IDetailsPageProvider()
        {

            public Object getPageKey(Object object)
            {
                return object;
            }

            public IDetailsPage getPage(Object key)
            {
                if (key instanceof Model)
                {
                    Model model = (Model)key;
                    return modelDetailsPage;
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
