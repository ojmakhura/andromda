package org.andromda.modules.xmilink.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class i
        implements ActionListener
{

    private final ExportDialog exportDialog;

    protected i(ExportDialog exportDialog)
    {
        this.exportDialog = exportDialog;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        ExportDialog.openBrowseDialog(new h(this));
    }
}