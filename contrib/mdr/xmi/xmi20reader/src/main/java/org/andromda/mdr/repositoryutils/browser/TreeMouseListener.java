package org.andromda.mdr.repositoryutils.browser;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;


final class TreeMouseListener
    extends MouseAdapter
{
    final Updatable mPanel;
    private final JTree mTree;
    private final MDRepository mRepository;

    TreeMouseListener(
        Updatable panel,
        MDRepository repository,
        JTree tree)
    {
        mPanel = panel;
        mTree = tree;
        mRepository = repository;
    }

    void update()
    {
        mPanel.update();
    }

    public void mousePressed(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            TreePath selectionPath = mTree.getSelectionPath();
            if (selectionPath != null)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
                final Object userObject = node.getUserObject();
                JPopupMenu menu = new JPopupMenu("Actions");
                menu.add(
                    new AbstractAction("Load model")
                    {
                        /**
                         *
                         */
                        private static final long serialVersionUID = 1L;

                        public void actionPerformed(ActionEvent event)
                        {
                            try
                            {
                                loadFile(userObject);
                                update();
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                            }
                            catch (MalformedXMIException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                menu.show(
                    mTree,
                    e.getX(),
                    e.getY());
            }
        }
    }

    /**
     * @param userObject
     * @throws IOException
     * @throws MalformedXMIException
     */
    void loadFile(Object userObject)
        throws IOException, MalformedXMIException
    {
        if (userObject instanceof MofPackage)
        {
            MofPackage mp = (MofPackage)userObject;
            try
            {
                System.out.println("creating extent ...");
                String string = "testInstance";

                while (Arrays.asList(mRepository.getExtentNames()).contains(string))
                {
                    string = string + "1";
                }

                RefPackage package1 = mRepository.createExtent(
                        string,
                        mp);
                System.out.println("creating extent done");
                JFileChooser c = new JFileChooser();
                c.showOpenDialog(mTree);
                File fileName = c.getSelectedFile();
                if (fileName != null)
                {
                    System.out.println("reading user model ...");

                    XMIReader importer = XMIReaderFactory.getDefault().createXMIReader();

                    // JOptionPane.showMessageDialog(null,"start read user
                    // model");
                    long l = System.currentTimeMillis();
                    importer.read(
                        new File(fileName.getAbsolutePath()).toURI().toString(),
                        package1);
                    String msg = "end read user model" + (System.currentTimeMillis() - l) / 1000;
                    System.out.println(msg);

                    // JOptionPane.showMessageDialog(null,msg);
                    System.out.println("done");
                }
            }
            catch (CreationFailedException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}