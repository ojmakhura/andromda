package org.andromda.mdr.repositoryutils.browser;

import java.awt.HeadlessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.jmi.model.Classifier;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;


/**
 * GUI for showing repository.
 */
public class MetaRepositoryBrowser
    extends JFrame
{
    private static final class CMP
        implements Comparator
    {
        public int compare(
            Object o1,
            Object o2)
        {
            RefClass c1 = (RefClass)o1;
            RefClass c2 = (RefClass)o2;
            return c2.refAllOfClass().size() - c1.refAllOfClass().size();
        }
    }

    private JTabbedPane mTabbedPane;
    final MDRepository mRepository;
    private ArrayList mAllInProject;
    private Collection mRoots;

    public MetaRepositoryBrowser(MDRepository repository)
        throws HeadlessException
    {
        this(repository, new ArrayList());
    }

    public MetaRepositoryBrowser(
        MDRepository repository,
        Collection containmentRoots)
        throws HeadlessException
    {
        super("Repository browser");
        mRoots = containmentRoots;
        mTabbedPane = new JTabbedPane();
        getContentPane().add(mTabbedPane);

        mRepository = repository;

        mAllInProject = new ArrayList(MDRReflectionHelper.getAllElements(repository));
        mTabbedPane.add(
            "General",
            createGeneralPanel());
        mTabbedPane.add(
            "Repository",
            createInstancesPanel());
        mTabbedPane.add(
            "Instances",
            createContainmentPanel());

        // mTabbedPane.add( "Features usages",new
        // FeaturesUsagesCollector().createFeaturesUsagesPanel(mAllInProject));
        pack();
    }

    private JComponent createGeneralPanel()
    {
        JTextArea ret = new JTextArea(120, 80);

        ret.append("All Elements in Repository:" + mAllInProject.size() + "\n");
        ArrayList types = new ArrayList();
        for (Iterator iter = mAllInProject.iterator(); iter.hasNext();)
        {
            RefObject ro = (RefObject)iter.next();
            if (!types.contains(ro.refClass()))
            {
                types.add(ro.refClass());
            }
        }

        Collections.sort(
            types,
            new CMP());

        for (int i = 0; i < types.size(); i++)
        {
            RefClass c = (RefClass)types.get(i);
            ret.append(c.refAllOfClass().size() + " " + ((Classifier)c.refMetaObject()).getName() + " \n");
        }
        return new JScrollPane(ret);
    }

    private JPanel createInstancesPanel()
    {
        JPanel instances = new InstancesPanel(this);
        return instances;
    }

    private JPanel createContainmentPanel()
    {
        JPanel instances = new ContainmentPanel(this.mRepository, mRoots);

        // JPanel instances = new JPanel();
        return instances;
    }

    public static boolean isSet(
        RefObject element,
        StructuralFeature feature)
    {
        Object valueGet = element.refGetValue(feature.getName());
        boolean isSet = valueGet != null;
        if (valueGet instanceof Collection)
        {
            Collection collection = (Collection)valueGet;
            isSet = !collection.isEmpty();
        }
        return isSet;
    }

    public static void main(String[] args)
    {
        MDRepository defaultRepository = MDRManager.getDefault().getDefaultRepository();
        try
        {
            defaultRepository.createExtent("TEST");
        }
        catch (CreationFailedException e)
        {
            e.printStackTrace();
        }
        MetaRepositoryBrowser browser = new MetaRepositoryBrowser(defaultRepository);
        browser.setVisible(true);
    }
}