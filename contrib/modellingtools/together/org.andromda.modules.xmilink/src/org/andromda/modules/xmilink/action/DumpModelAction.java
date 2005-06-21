package org.andromda.modules.xmilink.action;

import org.andromda.modules.xmilink.Logger;

import com.togethersoft.modules.project.navigation.NavigationFeature;
import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Model;
import com.togethersoft.openapi.model.enum.EntityEnumeration;
import com.togethersoft.platform.ide.action.service.IdeActionDelegate;
import com.togethersoft.platform.project.Project;
import com.togethersoft.platform.project.ProjectManagerAccess;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 02.11.2004
 */
public class DumpModelAction
        implements IdeActionDelegate
{

    /*
     * (non-Javadoc)
     * 
     * @see com.togethersoft.platform.ide.action.IdeActionBase#isEnabled()
     */
    public boolean isEnabled()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.togethersoft.platform.ide.action.IdeActionBase#getValue(java.lang.String)
     */
    public Object getValue(String arg0)
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.togethersoft.platform.ide.action.IdeActionBase#isVisible()
     */
    public boolean isVisible()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        Entity entity = getModel();
        boolean wasLoggingEnabled = Logger.isLogEnabled();
        Logger.setLogEnabled(true);
        walkEntity(entity);
        Logger.setLogEnabled(wasLoggingEnabled);
    }

    /**
     * @param entity
     */
    private void walkEntity(Entity entity)
    {
        EntityEnumeration children = entity.childNodes();
        while (children.hasMoreElements())
        {
            Entity child = (Entity)children.nextElement();
            if (child != null)
            {
                String msg = child.toString();
                String name = child.getPropertyValue("$name");
                String metaclass = child.getPropertyValue("$metaclass");
                Logger.info(msg + " : " + name + " : " + metaclass);
                showReferencedElement(child);
                walkEntity(child);
            }
        }
    }

    private void showReferencedElement(Entity child)
    {
        String s4 = child.getPropertyValue("$typeReferencedElement");
        Entity entity = null;
        if (s4 != null)
        {
            entity = (Entity)com.togethersoft.modules.project.language.model.UniqueNameService.Util
                    .getService(child.getModel()).findElementByPersistentName(s4);
            if (entity != null)
            {
                String msg = entity.toString();
                String name = entity.getPropertyValue("$name");
                String metaclass = entity.getPropertyValue("$metaclass");
                Logger.info("Reference " + msg + " : " + name + " : " + metaclass);
            }
        }
    }

    /**
     * @return
     */
    private static Model getModel()
    {
        Project project = ProjectManagerAccess.getManager().getActiveProject();
        Model model = ((NavigationFeature)project
                .getFeature(com.togethersoft.modules.project.navigation.NavigationFeature.class))
                .getModel();
        return model;
    }

}