package org.andromda.modules.xmilink.action;

import org.andromda.modules.xmilink.dialog.ExportDialog;
import org.andromda.modules.xmilink.dialog.FileHandler;
import org.andromda.modules.xmilink.dialog.IFileHandler;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Project;
import com.togethersoft.openapi.ide.config.IdeConfig;
import com.togethersoft.openapi.ide.config.IdeConfigManagerAccess;
import com.togethersoft.openapi.util.file.FileName;
import com.togethersoft.platform.ide.action.service.IdeActionDelegate;
import com.togethersoft.platform.ide.window.IdeWindowManagerAccess;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 16.09.2004
 */
public class XMIExport2
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
        IdeConfig ideconfig = IdeConfigManagerAccess.getConfigManager().getConfig("$default");
        ExportDialog exportDialog = new ExportDialog();
        IFileHandler fileHandler = new FileHandler(this, exportDialog, ideconfig);
        exportDialog.setFileHandler(fileHandler);
        String s = ideconfig.getProperty("xmiExport.remembered_path", "");
        if (s == null || s.length() == 0)
        {
            s = a();
        }
        ExportDialog.setFileName(s);
        IdeWindowManagerAccess.getManager().showDialog(exportDialog);
    }

    private static String a()
    {
        Project project = Browser.getActiveBrowser().getActiveProject();
        String s = project.getUrl().getParent().getFileObject().getAbsolutePath();
        return FileName.toSystemNeutral(s);
    }

}