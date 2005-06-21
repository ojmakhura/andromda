package org.andromda.modules.xmilink.dialog;

class FocusHelper
        implements Runnable
{

    private final n a;

    public FocusHelper(n n1)
    {
        a = n1;
    }

    public void run()
    {
        String fileName = ExportDialog.getFileNameField().getText();
        if (fileName != null)
        {
            ExportDialog.getFileNameField().setCaretPosition(fileName.length());
        }
    }
}