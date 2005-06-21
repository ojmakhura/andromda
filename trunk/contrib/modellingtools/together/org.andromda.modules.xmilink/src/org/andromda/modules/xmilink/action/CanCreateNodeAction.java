package org.andromda.modules.xmilink.action;

import org.andromda.modules.xmilink.Logger;

import com.togethersoft.modules.project.navigation.NavigationFeature;
import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Model;
import com.togethersoft.openapi.model.elements.Model.ChangeException;
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
public class CanCreateNodeAction
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
        walkEntity(entity);
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
                boolean canCreateDataType;
                boolean canCreateClass;
                try
                {
                    canCreateDataType = entity.canCreateChildNode("DataType");
                    canCreateClass = entity.canCreateChildNode("Class");
                    Logger.info(msg + " : " + name + " : " + metaclass + "; canCreateDataType: "
                            + canCreateDataType + "; canCreateClass: " + canCreateClass);
                }
                catch (ChangeException e)
                {
                    e.printStackTrace();
                }
                walkEntity(child);
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