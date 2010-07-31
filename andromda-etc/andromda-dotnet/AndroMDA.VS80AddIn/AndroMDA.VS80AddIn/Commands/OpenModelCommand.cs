
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;

using EnvDTE;
using Microsoft.VisualStudio.CommandBars;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class OpenModelCommand : AddInCommandBase
    {
        public OpenModelCommand()
        {
            Init("OpenModel", "Open Model", "Open Model", 589, VisibilityType.EnabledWhenMdaActive);
            m_isStartOfGroup = true;
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            string magicDrawPath = m_addInSettings.UMLModellerPath;
            if (magicDrawPath != string.Empty && System.IO.File.Exists(magicDrawPath) && System.IO.File.Exists(m_solutionManager.ModelFilePath) )
            {
                try
                {
                    if (m_addInSettings.AutoMakeModelFileWritable)
                    {
                        System.IO.FileAttributes attr = System.IO.File.GetAttributes(m_solutionManager.ModelFilePath);
                        if ((attr & System.IO.FileAttributes.ReadOnly) != 0)
                        {
                            attr -= System.IO.FileAttributes.ReadOnly;
                            System.IO.File.SetAttributes(m_solutionManager.ModelFilePath, attr);
                        }
                    }
                    //AddInUtils.ShowWarning("Path: " + magicDrawPath + "\nArgs: " + "\"" + m_solutionManager.ModelFilePath + "\"");
                    System.Diagnostics.Process.Start(magicDrawPath, "\"" + m_solutionManager.ModelFilePath + "\"");
                }
                catch (Exception e)
                {
                    AddInUtils.ShowError("An unexpected error occured while trying to launch the external UML modeling tool: " + e.Message);
                }
            }
            else
            {
                AddInUtils.ShowError("The external UML modeling tool could not be found.  Please ensure the path is correct in the Android/VS options page (Tools | Options | AndroMDA | Tools)");
            }
        }

		public override bool IsToolbarButtonVisible
		{
			get
			{
				return base.IsToolbarButtonVisible && m_addInSettings.ShowOpenModelButton;
			}
		}

		public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
		{
			base.QueryStatus(commandName, neededText, ref status, ref commandText);
			if (!m_addInSettings.ShowOpenModelButton)
			{
				status = status | vsCommandStatus.vsCommandStatusInvisible;
			}
		}

    }
}
