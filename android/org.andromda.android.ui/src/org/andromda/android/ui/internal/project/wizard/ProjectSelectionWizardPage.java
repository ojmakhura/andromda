package org.andromda.android.ui.internal.project.wizard;

import java.util.ArrayList;

import org.andromda.android.core.project.AndroidNature;
import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * This page displays all projects that are not Android projects.
 *
 * @author Peter Friese
 * @since 03.11.2005
 */
public class ProjectSelectionWizardPage
        extends WizardPage
{

    /** The list viewer displaying the projects. */
    private CheckboxTableViewer projectsViewer;

    /**
     * Creates a wizard page for selecting non-Android projects.
     */
    public ProjectSelectionWizardPage()
    {
        super("wizardPage");
        setTitle("Convert Java Projects to Android Projects");
        setDescription("Select one or more Java Project that will be converted to an Android Project");
    }

    /**
     * {@inheritDoc}
     */
    public void createControl(final Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);

        projectsViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER);
        projectsViewer.setLabelProvider(new WorkbenchLabelProvider());
        projectsViewer.setContentProvider(new ArrayContentProvider());
        final Table table = projectsViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        final TableColumn projectNameColumn = new TableColumn(table, SWT.NONE);
        projectNameColumn.setWidth(300);
        projectNameColumn.setText("Project name");
        projectsViewer.setInput(new Object());

        setupData();
    }

    /**
     * Read the list of non-Android projects and display them in the project viewer.
     */
    private void setupData()
    {
        final IProject[] nonAndroidProjects = getNonAndroidProjects();
        if (nonAndroidProjects.length == 0)
        {
            setMessage("All projects in the workspace already are Android projects.", IMessageProvider.INFORMATION);
        }
        else
        {
            setMessage(null);
        }
        projectsViewer.setInput(nonAndroidProjects);
    }

    /**
     * Finds all Java projects that do not have the Android nature assigned.
     *
     * @return an array of {@link IProject}s
     */
    private IProject[] getNonAndroidProjects()
    {
        ArrayList list = new ArrayList();
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (int i = 0; i < projects.length; i++)
        {
            try
            {
                IProject project = projects[i];
                if (project.isAccessible() && !project.hasNature(AndroidNature.ID))
                {
                    list.add(project);
                }
            }
            catch (CoreException e)
            {
                AndroidUIPlugin.log(e);
            }
        }
        return (IProject[])list.toArray(new IProject[list.size()]);
    }

    /**
     * Gets all projects selected by the user.
     *
     * @return an array of {@link IProject}s
     */
    public IProject[] getSelectedProjects()
    {
        Object[] checkedElements = projectsViewer.getCheckedElements();
        final IProject[] checkedProjects = new IProject[checkedElements.length];
        System.arraycopy(checkedElements, 0, checkedProjects, 0, checkedElements.length);
        return checkedProjects;
    }
}
