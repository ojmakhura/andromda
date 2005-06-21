package org.andromda.modules.xmilink.dialog;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class n
        extends JPanel
{

    private final ExportDialog exportDialog;

    public n(ExportDialog exportDialog, LayoutManager layoutmanager)
    {
        super(layoutmanager);
        this.exportDialog = exportDialog;
    }

    public void requestFocus()
    {
        ExportDialog.getFileNameField().requestFocus();
        SwingUtilities.invokeLater(new FocusHelper(this));
    }
}