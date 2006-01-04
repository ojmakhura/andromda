package org.andromda.mdr.repositoryutils.browser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Collection;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


final class SelectInTreeListnener
    extends MouseAdapter
{
    /**
     *
     */
    final JTree mTree;

    public SelectInTreeListnener(JTree tree)
    {
        mTree = tree;
    }

    public void mousePressed(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            TreePath selectionPath = mTree.getSelectionPath();
            if (selectionPath == null)
            {
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
            Object userObject = node.getUserObject();
            if (userObject instanceof Value)
            {
                Value value = (Value)userObject;
                final Object valueToFind;
                if (value.getValue() instanceof Collection)
                {
                    Collection c1 = (Collection)value.getValue();
                    if (c1.size() == 1)
                    {
                        valueToFind = c1.iterator().next();
                    }
                    else
                    {
                        valueToFind = null;
                    }
                }
                else
                {
                    valueToFind = value.getValue();
                }
                if (valueToFind == null)
                {
                    return;
                }
                Runnable runnable =
                    new Runnable()
                    {
                        public void run()
                        {
                            findAndSelect(
                                mTree,
                                (DefaultMutableTreeNode)SelectInTreeListnener.this.mTree.getModel().getRoot(),
                                valueToFind);
                        }
                    };
                try
                {
                    SwingUtilities.invokeLater(runnable);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    boolean findAndSelect(
        JTree tree1,
        DefaultMutableTreeNode root,
        Object valueToFind)
    {
        int childCount = root.getChildCount();
        for (int i = childCount - 1; i >= 0; i--)
        {
            DefaultMutableTreeNode childAt = (DefaultMutableTreeNode)root.getChildAt(i);
            if (childAt.getUserObject() == valueToFind) // &&
                                                        // root.getUserObject()                                            // instanceof RefClass)
            {
                Object[] p = childAt.getPath();
                TreePath path = new TreePath(p);
                if (tree1.getModel().isLeaf(path.getLastPathComponent()))
                {
                    tree1.expandPath(path.getParentPath());
                }
                else
                {
                    tree1.expandPath(path);
                }
                tree1.setSelectionPath(path);
                tree1.setExpandsSelectedPaths(true);
                tree1.scrollPathToVisible(path);
                return true;
            }
            boolean found = findAndSelect(
                    tree1,
                    childAt,
                    valueToFind);
            if (found)
            {
                return true;
            }
        }
        return false;
    }
}