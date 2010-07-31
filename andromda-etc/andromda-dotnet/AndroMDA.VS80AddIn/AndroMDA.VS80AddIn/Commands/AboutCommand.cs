
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;

using Microsoft.VisualStudio.CommandBars;

using EnvDTE;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class AboutCommand : AddInCommandBase
    {
        public AboutCommand()
        {
            Init("About", "About", "About Android/VS", 487, VisibilityType.AlwaysEnabled);
            m_isStartOfGroup = true;
        }

		public override bool IsToolbarButtonVisible
		{
			get
			{
				return base.IsToolbarButtonVisible && m_addInSettings.ShowAboutButton;
			}
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            Dialogs.AboutDialog a = new Dialogs.AboutDialog();
            a.MDASolutionManager = m_solutionManager;
            a.ShowDialog();
        }

		public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
		{
			base.QueryStatus(commandName, neededText, ref status, ref commandText);
			if (!m_addInSettings.ShowAboutButton)
			{
				status = status | vsCommandStatus.vsCommandStatusInvisible;
			}
		}

    }
}
