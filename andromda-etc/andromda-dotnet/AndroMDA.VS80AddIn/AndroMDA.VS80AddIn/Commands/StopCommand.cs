
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
    class StopCommand : AddInCommandBase
    {

        public StopCommand()
        {
            Init("StopGeneration", "Stop", "Stop Code Generation", 1670, VisibilityType.EnabledWhenMdaActiveAndMavenRunning);
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            m_solutionManager.StopMaven();
        }

    }
}
