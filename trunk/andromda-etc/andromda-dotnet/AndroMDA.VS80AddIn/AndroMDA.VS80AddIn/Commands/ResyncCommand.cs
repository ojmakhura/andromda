
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;
using EnvDTE;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class ResyncCommand : AddInCommandBase
    {
        public ResyncCommand()
        {
            Init("ResyncFiles", "Resync", "Resync Generated Files", 37, VisibilityType.EnabledWhenMdaActiveAndMavenNotRunning);
        }

        public override bool IsToolbarButtonVisible
        {
            get
            {
                return base.IsToolbarButtonVisible && m_addInSettings.ShowResyncButton;
            }
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            m_solutionManager.RefreshGeneratedFiles();
        }

        public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
        {
            base.QueryStatus(commandName, neededText, ref status, ref commandText);
            if (!m_addInSettings.ShowResyncButton)
            {
                status = status | vsCommandStatus.vsCommandStatusInvisible;
            }
        }

    }
}
