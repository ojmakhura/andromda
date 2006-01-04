package org.andromda.mdr.repositoryutils.browser;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.Classifier;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.netbeans.api.mdr.MDRepository;


class InstancesPanel
    extends JPanel
    implements Updatable
{
    private static final class CMP
        implements Comparator
    {
        public int compare(
            Object o1,
            Object o2)
        {
            StructuralFeature f1 = (StructuralFeature)o1;
            StructuralFeature f2 = (StructuralFeature)o2;
            return f1.getName().compareTo(f2.getName());
        }
    }

    /**
     *
     */
    final MetaRepositoryBrowser mBrowser;
    boolean initialized;

    public InstancesPanel(MetaRepositoryBrowser browser)
    {
        mBrowser = browser;

        // init();
    }

    public void setVisible(boolean aFlag)
    {
        if (!initialized && aFlag)
        {
            init();
        }
        super.setVisible(aFlag);
    }

    /**
     *
     */
    private void init()
    {
        initialized = true;
        mShowWithInstances = createShowOnlyWithInstancesCheckBox();
        mShowWithValues = createShowOnlyWithValuesCheckBox();
        mConfigurationPanele = new JPanel();
        mConfigurationPanele.add(mShowWithInstances);
        mConfigurationPanele.add(mShowWithValues);

        update();
    }

    public void update()
    {
        mRootNode = getRootNode();
        mTree = createTree();
        mTree.addMouseListener(new TreeMouseListener(
                this,
                mBrowser.mRepository,
                mTree));

        this.removeAll();

        // updateTree();
        mTree.setCellRenderer(new Renderer());
        this.setLayout(new BorderLayout());
        this.add(
            mConfigurationPanele,
            BorderLayout.NORTH);
        this.add(
            new JScrollPane(mTree),
            BorderLayout.CENTER);
    }

    private JTree createTree()
    {
        mTreeModel = new DefaultTreeModel(mRootNode);
        final JTree tree = new JTree(mTreeModel);

        tree.addMouseListener(new SelectInTreeListnener(tree));
        return tree;
    }

    private JCheckBox mShowWithInstances;
    private JCheckBox mShowWithValues;
    DefaultMutableTreeNode mRootNode;
    DefaultTreeModel mTreeModel;
    private JPanel mConfigurationPanele;
    JTree mTree;

    private JCheckBox createShowOnlyWithInstancesCheckBox()
    {
        JCheckBox checkBox = new JCheckBox("Show only with instances", true);
        checkBox.addItemListener(
            new ItemListener()
            {
                public void itemStateChanged(ItemEvent e)
                {
                    update();
                }
            });
        return checkBox;
    }

    private JCheckBox createShowOnlyWithValuesCheckBox()
    {
        JCheckBox checkBox = new JCheckBox("Show only with set Values", true);
        checkBox.addItemListener(
            new ItemListener()
            {
                public void itemStateChanged(ItemEvent e)
                {
                    update();
                }
            });
        return checkBox;
    }

    private DefaultMutableTreeNode getRootNode()
    {
        MDRepository repository = mBrowser.mRepository;
        DefaultMutableTreeNode root = createNode(repository);
        String[] extentNames = repository.getExtentNames();
        for (int i = 0; i < extentNames.length; i++)
        {
            String name = extentNames[i];
            RefPackage extent = repository.getExtent(name);
            DefaultMutableTreeNode createNode = createNode(extent);
            createNode.setUserObject(new ExtentProxy(
                    extent,
                    name));
            root.add(createNode);
        }
        return root;
    }

    private DefaultMutableTreeNode createNode(Object userObject)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(userObject);

        addInner(root);
        return root;
    }

    /**
     * @param root
     */
    private void addInner(DefaultMutableTreeNode root)
    {
        Object userObject = root.getUserObject();
        if (userObject instanceof RefPackage)
        {
            RefPackage refPack = (RefPackage)userObject;
            for (Iterator iter = refPack.refAllPackages().iterator(); iter.hasNext();)
            {
                RefPackage pack = (RefPackage)iter.next();
                DefaultMutableTreeNode createdNode = createNode(pack);
                if (!isShowOnlyWithInstances() || createdNode.getChildCount() > 0)
                {
                    root.add(createdNode);
                }
            }
            for (Iterator it = refPack.refAllClasses().iterator(); it.hasNext();)
            {
                RefClass cl = (RefClass)it.next();
                if (!isShowOnlyWithInstances() || cl.refAllOfClass().size() > 0)
                {
                    root.add(createNode(cl));
                }
            }
        }

        if (userObject instanceof RefClass)
        {
            RefClass rc = (RefClass)userObject;
            for (Iterator it = rc.refAllOfClass().iterator(); it.hasNext();)
            {
                RefObject ro = (RefObject)it.next();
                root.add(createNode(ro));
            }
        }

        if (userObject instanceof RefObject)
        {
            RefObject element = (RefObject)userObject;
            List features =
                new ArrayList(
                    MDRReflectionHelper.getAttributesAndReferences((Classifier)element.refClass().refMetaObject()));
            Collections.sort(
                features,
                new CMP());
            for (Iterator iter = features.iterator(); iter.hasNext();)
            {
                StructuralFeature feature = (StructuralFeature)iter.next();
                if (feature.isChangeable())
                {
                    // boolean isSet = element.isSet(feature.getName());
                    boolean isSet = MetaRepositoryBrowser.isSet(
                            element,
                            feature);
                    if (!isShowOnlyWithSetValues() || isSet)
                    {
                        Object value = null;
                        if (isSet)
                        {
                            try
                            {
                                String name2 = feature.getName();
                                value = element.refGetValue(name2);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        Value value2 = new Value(feature, value);
                        root.add(createNode(value2));
                    }
                }
            }
        }

        if (userObject instanceof Value)
        {
            Value value = (Value)userObject;
            if (value.getValue() instanceof Collection)
            {
                Collection collection = (Collection)value.getValue();
                for (Iterator iter = collection.iterator(); iter.hasNext();)
                {
                    Object o = iter.next();
                    Value value2 = new Value(
                            value.getFeature(),
                            o);
                    root.add(createNode(value2));
                }
            }
        }
    }

    private boolean isShowOnlyWithInstances()
    {
        return mShowWithInstances.isSelected();
    }

    private boolean isShowOnlyWithSetValues()
    {
        return mShowWithValues.isSelected();
    }
}