package org.andromda.modules.xmilink.dialog;

import org.andromda.modules.xmilink.action.XMIExport2;
import org.andromda.modules.xmilink.action.XMIExportWorker;
import org.andromda.modules.xmilink.io.Writer;
import org.andromda.modules.xmilink.io.XMLWriter;

import com.togethersoft.openapi.ide.config.IdeConfig;
import com.togethersoft.openapi.util.file.FileName;
import com.togethersoft.platform.project.ProjectManagerAccess;

public class FileHandler
        implements IFileHandler
{

    private final ExportDialog exportDialog;

    private final IdeConfig ideConfig;

    private final XMIExport2 xmiExport;

    public FileHandler(XMIExport2 xmiExport, ExportDialog exportDialog, IdeConfig ideConfig)
    {
        this.xmiExport = xmiExport;
        this.exportDialog = exportDialog;
        this.ideConfig = ideConfig;
    }

    public void handleFile(Object obj)
    {
        String fileName = exportDialog.getFileName();
        if (fileName == null)
        {
            return;
        }
        int i = fileName.lastIndexOf(FileName.toSystemNeutral("/"));
        if (i > -1)
        {
            String s2 = fileName.substring(0, i);
            s2 = FileName.toSystemNeutral(s2);
            ideConfig.putProperty("xmiExport.remembered_path", s2);
        }
        com.togethersoft.platform.project.Project project = ProjectManagerAccess.getManager()
                .getActiveProject();
        if (project == null)
        {
            return;
        }
        else
        {
            // write!
            Writer writer = new XMLWriter();
            XMIExportWorker.invokeExport(writer, fileName);
            return;
        }
    }
}