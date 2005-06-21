package org.andromda.modules.xmilink.dialog;

import com.togethersoft.modules.filechooser.ExtendedFileChooserDialog;
import com.togethersoft.platform.ide.window.IdeWindowManagerAccess;

class FileChooserInvoker
        implements Runnable
{

    private final ExtendedFileChooserDialog fileChooser;

    public FileChooserInvoker(ExtendedFileChooserDialog fileChooser)
    {
        this.fileChooser = fileChooser;
    }

    public void run()
    {
        IdeWindowManagerAccess.getManager().showDialog(fileChooser);
    }
}