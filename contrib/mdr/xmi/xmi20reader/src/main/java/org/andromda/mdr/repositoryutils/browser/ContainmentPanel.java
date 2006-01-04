package org.andromda.mdr.repositoryutils.browser;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.Classifier;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefObject;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.netbeans.api.mdr.MDRepository;


class ContainmentPanel
    extends JPanel
    implements Updatable
{
    private static final long serialVersionUID = 1L;
    private JCheckBox mShowWithValues;
    DefaultMutableTreeNode mRootNode;
    private DefaultTreeModel mTreeModel;
    private JTree mTree;

    private final static class CMP
        implements Comparator
    {
        private static CMP cmp;

        public int compare(
            Object o1,
            Object o2)
        {
            StructuralFeature f1 = (StructuralFeature)o1;
            StructuralFeature f2 = (StructuralFeature)o2;
            return f1.getName().compareTo(f2.getName());
        }

        public static CMP instance()
        {
            if (cmp == null)
            {
                cmp = new CMP();
            }
            return cmp;
        }
    }

    boolean init_;
    private final Collection mRoots;
    private final MDRepository mRepository;

    public ContainmentPanel(
        MDRepository repository,
        Collection roots)
    {
        mRepository = repository;

        // init();
        mRoots = roots;
    }

    public void setVisible(boolean aFlag)
    {
        if (!init_ && aFlag)
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
        init_ = true;
        mShowWithValues = createShowOnlyWithValuesCheckBox();
        JPanel configurationPanele = new JPanel();
        configurationPanele.add(mShowWithValues);

        mTreeModel = new DefaultTreeModel(getRootNode());
        mTree = new JTree(mTreeModel);
        mTree.addMouseListener(new SelectInTreeListnener(mTree));
        mTree.addMouseListener(new TreeMouseListener(
                this,
                mRepository,
                mTree));
        update();
        mTree.setCellRenderer(new Renderer());
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(
            configurationPanele,
            BorderLayout.NORTH);
        this.add(
            new JScrollPane(mTree),
            BorderLayout.CENTER);
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

    public void update()
    {
        mTree.setModel(new DefaultTreeModel(getRootNode()));
    }

    private DefaultMutableTreeNode getRootNode()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(mRepository.getClass().toString());
        for (Iterator iter = mRoots.iterator(); iter.hasNext();)
        {
            Object rooti = iter.next();
            root.add(createNode(rooti));
        }

        // ArrayList l = new ArrayList();
        // l.add( extent );
        // DefaultMutableTreeNode node = createNode(extent);
        // node.setUserObject(new ExtentProxy(extent,name));
        // root.add( node);
        // for (int j = 0; j < l.size(); j++)
        // {
        // RefPackage rp = (RefPackage) l.get(j);
        // l.addAll( rp.refAllPackages());
        // Collection cl = rp.refAllClasses();
        // for (Iterator iter = cl.iterator(); iter.hasNext();)
        // {
        // RefClass rc = (RefClass) iter.next();
        // Collection allofClass = rc.refAllOfClass();
        // for (Iterator iterator = allofClass.iterator(); iterator.hasNext();)
        // {
        // RefObject ro = (RefObject) iterator.next();
        // RefFeatured owner = ro.refImmediateComposite();
        // if( owner == null )
        // {
        // node.add( createNode(ro) );
        // }
        // }
        // }
        // }
        // }
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

        // if (userObject instanceof ModelPackage)
        // {
        // ModelPackage pack = (ModelPackage) userObject;
        //                
        // // for (Iterator iter =
        // pack.getContains().getContainedElement(null).iterator();
        // iter.hasNext();)
        // // {
        // // ModelElement value2 = (ModelElement) iter.next();
        // // root.add(createNode(value2));
        // // }
        // }
        if (userObject instanceof RefObject)
        {
            RefObject oo = addChildren(
                    root,
                    userObject);
            RefObject element = oo;
            List features =
                new ArrayList(
                    MDRReflectionHelper.getAttributesAndReferences((Classifier)element.refClass().refMetaObject()));
            Collections.sort(
                features,
                CMP.instance());
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
                            value = element.refGetValue(feature.getName());
                        }
                        Value value2 = new Value(feature, value);
                        root.add(createNode(value2));
                    }
                }
            }
        }
    }

    private RefObject addChildren(
        DefaultMutableTreeNode root,
        Object userObject)
    {
        RefObject oo = (RefObject)userObject;
        Classifier meta = (Classifier)(oo).refClass().refMetaObject();
        List compositeReferences = MDRReflectionHelper.getCompositeReferences(meta);
        Collection inner = new HashSet();
        for (Iterator iter = compositeReferences.iterator(); iter.hasNext();)
        {
            StructuralFeature feature = (StructuralFeature)iter.next();
            String name2 = feature.getName();

            // if( oo.isSet(name2))
            if (true)
            {
                Object object = oo.refGetValue(name2);
                if (object instanceof Collection)
                {
                    Collection col = (Collection)object;
                    for (Iterator iterator = col.iterator(); iterator.hasNext();)
                    {
                        Object ob2 = iterator.next();
                        inner.add(ob2);
                    }
                }
                else
                {
                    if (object != null)
                    {
                        inner.add(object);
                    }
                }
            }
        }
        for (Iterator iter = inner.iterator(); iter.hasNext();)
        {
            Object o = iter.next();
            root.add(createNode(o));
        }
        return oo;
    }

    private boolean isShowOnlyWithSetValues()
    {
        return mShowWithValues.isSelected();
    }
}