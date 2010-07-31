
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
    public class RunSolutionWizardCommand : AddInCommandBase
    {
        public RunSolutionWizardCommand()
        {
            Init("RunSolutionWizard", "Run MDA Solution Wizard", "Run MDA Solution Wizard", 2138, VisibilityType.EnabledWhenMdaInactive);
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
            Dialogs.MDASolutionWizard wizard = new Dialogs.MDASolutionWizard(m_application, m_solutionManager, m_addInSettings);
            wizard.ShowDialog();
        }

    }
}
