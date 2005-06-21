package org.andromda.modules.xmilink.dialog;

import java.io.File;

import com.togethersoft.openapi.util.file.FileName;

class h
        implements IFileHandler
{

    private final i a;

    public h(i j)
    {
        a = j;
    }

    public void handleFile(Object obj)
    {
        File file = ((File[])obj)[0];
        ExportDialog.setFileName(FileName.toSystemNeutral(file.getPath()));
    }
}