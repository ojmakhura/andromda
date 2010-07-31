
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
    public class RunMavenCommand : AddInCommandBase
    {
        public RunMavenCommand()
        {
            Init("GenerateCode", "Generate", "Generate Code From Model", 2138, VisibilityType.EnabledWhenMdaActiveAndMavenNotRunning);
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            m_solutionManager.RunMaven();
        }

    }
}
