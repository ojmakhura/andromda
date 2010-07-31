
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
    public class RunSchemaExportCommand : AddInCommandBase
    {
		public RunSchemaExportCommand()
        {
            Init("ExportSchema", "Export Schema", "Export Database Schema", 4005, VisibilityType.EnabledWhenMdaActiveAndMavenNotRunning);
            m_isStartOfGroup = true;
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
			m_solutionManager.RunSchemaExport();
        }

		public override bool IsToolbarButtonVisible
		{
			get
			{
				return base.IsToolbarButtonVisible && m_addInSettings.ShowSchemaExportButton;
			}
		}

		public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
		{
			base.QueryStatus(commandName, neededText, ref status, ref commandText);
			if (!m_addInSettings.ShowSchemaExportButton || (!m_solutionManager.IsSchemaExportProjectAvailable && m_addInSettings.HideSchemaExportButtonIfProjectUnavailable))
			{
				status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
			}
		}

	}
}
