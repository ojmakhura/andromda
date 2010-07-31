
// AndroMDA Visual Studio 2005 Add-In
// (c)2006 Sapient Corporation

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
            Init("RunMaven", "Generate", "Generate code via AndroMDA", 2138, AddInCommandBase.AddInCommandType.MDAEnabledWhileMavenNotRunning);
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            m_solutionManager.RunMaven();
        }

    }
}
