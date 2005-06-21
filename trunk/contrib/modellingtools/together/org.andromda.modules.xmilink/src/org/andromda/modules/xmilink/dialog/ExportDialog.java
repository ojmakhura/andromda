package org.andromda.modules.xmilink.dialog;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.togethersoft.modules.filechooser.FileFilterConstant;
import com.togethersoft.openapi.util.file.FileName;
import com.togethersoft.openapi.util.ui.ExtendedControl;
import com.togethersoft.platform.command.CommandProcessor;
import com.togethersoft.platform.ide.window.dialog.IdeDialogAdapter;
import com.togethersoft.platform.project.ProjectManagerAccess;

public class ExportDialog
        extends IdeDialogAdapter
{

    private static JTextField txtFileName;

    private static String filePath;

    private static String fileName;

    private IFileHandler fileHandler;

    public ExportDialog()
    {
        fileName = ProjectManagerAccess.getManager().getActiveProject().getName() + ".xmi";
        setTitle("Export to XMI");
        setButtons(new String[] { "ok", "cancel" });
    }

    public IFileHandler getFileHandler()
    {
        return fileHandler;
    }

    public void setFileHandler(IFileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    public static void setFileName(String newFileName)
    {
        if (newFileName != null)
        {
            if (newFileName.endsWith(".xml") || newFileName.endsWith(".xmi"))
            {
                int j = newFileName.lastIndexOf(FileName.toSystemNeutral("/"));
                if (j > -1)
                {
                    filePath = newFileName.substring(0, j);
                    fileName = newFileName.substring(j + 1);
                }
                else
                {
                    filePath = "";
                    fileName = newFileName;
                }
            }
            else
            {
                filePath = newFileName;
            }
            if (txtFileName != null)
            {
                txtFileName.setText(filePath + FileName.toSystemNeutral("/") + fileName);
            }
        }
    }

    public String getFileName()
    {
        String s = txtFileName.getText();
        int j = s.lastIndexOf("/");
        if (j > -1)
        {
            filePath = s.substring(0, j);
            fileName = s.substring(j + 1);
        }
        else
        {
            fileName = s;
        }
        return txtFileName.getText();
    }

    public JComponent getContent()
    {
        n n1 = new n(this, new BorderLayout());
        n1.add(createContent(), "North");
        return n1;
    }

    protected JComponent createContent()
    {
        JPanel jpanel = new JPanel(new GridBagLayout());

        // Label "XMI file path"
        JLabel jlabel = new JLabel("XMI file path:");
        jlabel.setDisplayedMnemonic('X');
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        gridbagconstraints.anchor = 17;
        gridbagconstraints.fill = 0;
        gridbagconstraints.weightx = 0.0D;
        gridbagconstraints.insets = new Insets(6, 0, 0, 0);
        gridbagconstraints.gridwidth = 1;
        jpanel.add(jlabel, gridbagconstraints);

        // Text field for XMI file name
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.fill = 2;
        gridbagconstraints.gridwidth = 0;
        txtFileName = new JTextField(filePath + FileName.toSystemNeutral("/") + fileName, 35);
        ExtendedControl extendedcontrol = new ExtendedControl(1, txtFileName, new i(this));
        jpanel.add(extendedcontrol, gridbagconstraints);
        jlabel.setLabelFor(txtFileName);

        return jpanel;
    }

    public void dialogClosed(String s)
    {
        if ("ok".equals(s))
        {
            IFileHandler fileHandler = getFileHandler();
            if (fileHandler != null)
            {
                fileHandler.handleFile(null);
            }
        }
    }

    protected String e()
    {
        return "XMIImportExport";
    }

    static void openBrowseDialog(IFileHandler m1)
    {
        FileChooser fileChooser = new FileChooser(m1);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogType(1);
        fileChooser.setFileSelectionMode(2);
        String s = txtFileName.getText();
        if (s != null && s.length() > 0)
        {
            File file = new File(s);
            fileChooser.setSelectedFiles(new File[] { file });
        }
        fileChooser.addChoosableFileFilter(FileFilterConstant.XMI_FILES_FILTER);
        CommandProcessor.getInstance().addCommandToQueue(new FileChooserInvoker(fileChooser));
    }

    static JTextField getFileNameField()
    {
        return txtFileName;
    }
}