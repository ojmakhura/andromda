package org.andromda.modules.xmilink.dialog;

import com.togethersoft.modules.filechooser.ExtendedFileChooserDialog;

class FileChooser
        extends ExtendedFileChooserDialog
{

    private final IFileHandler a;

    public FileChooser(IFileHandler m1)
    {
        a = m1;
    }

    protected void doCommit()
    {
        if (a == null)
        {
            return;
        }
        else
        {
            a.handleFile(getSelectedFiles());
            return;
        }
    }

    public String getTitle()
    {
        return "Select XMI Output File";
    }
}