
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
    public class ReloadMDAConfigCommand : AddInCommandBase
    {
		public ReloadMDAConfigCommand()
        {
			Init("ReloadMDAConfig", "Reload MDA Config", "Reload MDA Config", 1020, VisibilityType.EnabledWhenMdaActiveButDisabled);
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            m_solutionManager.InitializeSolution();
        }

    }
}
