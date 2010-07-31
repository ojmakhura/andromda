
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class VSExternalToolEventArgs : EventArgs
    {

		private VSExternalToolProxy.ToolExitStatus m_exitStatus;
		private string m_errorMessage;
        private int m_exitCode;

		public VSExternalToolProxy.ToolExitStatus ExitStatus
        {
            get { return m_exitStatus; }
            set { m_exitStatus = value; }
        }
	
        public int ExitCode
        {
            get { return m_exitCode; }
            set { m_exitCode = value; }
        }

        public string ErrorMessage
        {
            get { return m_errorMessage; }
            set { m_errorMessage = value; }
        }

        public VSExternalToolEventArgs()
        {
			m_exitStatus = VSExternalToolProxy.ToolExitStatus.DidNotExit;
            m_exitCode = 0;
            m_errorMessage = string.Empty;
        }

		public VSExternalToolEventArgs(VSExternalToolProxy.ToolExitStatus exitStatus, int exitCode, string errorMessage)
        {
            m_exitStatus = exitStatus;
            m_exitCode = exitCode;
            m_errorMessage = errorMessage;
        }
	  
    }
}
