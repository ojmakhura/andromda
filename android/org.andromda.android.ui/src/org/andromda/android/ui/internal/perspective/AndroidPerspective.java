package org.andromda.android.ui.internal.perspective;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 *
 * @author Peter Friese
 * @since 25.03.2006
 */
public class AndroidPerspective
        implements IPerspectiveFactory
{

    /**
     * Creates the initial layout for a page.
     */
    public void createInitialLayout(IPageLayout layout)
    {
        String editorArea = layout.getEditorArea();
        addFastViews(layout);
        addViewShortcuts(layout);
        addPerspectiveShortcuts(layout);
        layout.addView("org.eclipse.ui.navigator.ProjectExplorer", IPageLayout.LEFT, 0.25f, editorArea);
        layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM, 0.75f, "org.eclipse.ui.navigator.ProjectExplorer");
        IFolderLayout folderLayout_1 = layout.createFolder("folder0", IPageLayout.RIGHT, 0.68f, editorArea);
        folderLayout_1.addView(IPageLayout.ID_OUTLINE);
        folderLayout_1.addView(JavaUI.ID_TYPE_HIERARCHY);
        IFolderLayout folderLayout = layout.createFolder("folder", IPageLayout.BOTTOM, 0.75f, editorArea);
        folderLayout.addView(IPageLayout.ID_PROBLEM_VIEW);
        folderLayout.addView(IPageLayout.ID_TASK_LIST);
        folderLayout.addView("org.eclipse.ui.console.ConsoleView");
    }

    /**
     * Add fast views to the perspective.
     */
    private void addFastViews(IPageLayout layout)
    {
    }

    /**
     * Add view shortcuts to the perspective.
     */
    private void addViewShortcuts(IPageLayout layout)
    {
    }

    /**
     * Add perspective shortcuts to the perspective.
     */
    private void addPerspectiveShortcuts(IPageLayout layout)
    {
        layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
        layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective");
        layout.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
        layout.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective");
        layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaHierarchyPerspective");
    }

}
