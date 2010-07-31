
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;

using Microsoft.VisualStudio.CommandBars;

using EnvDTE;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class AndroMDALogoCommand : AddInCommandBase
    {
		private bool m_visible = true;

		public bool Visible
		{
			get { return m_visible; }
			set { m_visible = value; }
		}

		public override bool IsToolbarButtonVisible
		{
			get
			{
				return false;
			}
		}

		public AndroMDALogoCommand()
        {
            Init("AndroidLogo", "Android/VS", "Android/VS", -1, VisibilityType.AlwaysDisabled);
            m_isStartOfGroup = true;
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
        }

		public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
		{
			base.QueryStatus(commandName, neededText, ref status, ref commandText);
			if (!Visible)
			{
				status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
			}
		}

    }
}
