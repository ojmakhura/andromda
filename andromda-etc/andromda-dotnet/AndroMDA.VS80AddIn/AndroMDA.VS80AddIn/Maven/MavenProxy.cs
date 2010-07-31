
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Text;
using System.Threading;
using System.ComponentModel;
using System.Diagnostics;

using EnvDTE;
using EnvDTE80;

using Microsoft.VisualStudio.CommandBars;

#endregion

namespace AndroMDA.VS80AddIn
{

	public class MavenProxyKnownException : Exception
	{
		public MavenProxyKnownException(string message)
			: base(message) { }
		public MavenProxyKnownException(string message, Exception innerException) 
			: base(message, innerException) { }
	}

    public class MavenProxy : VSExternalToolProxy
    {

		public enum MavenStatus { None, UnsatisfiedDependency }

        protected AddInSettings m_addInSettings = null;
        protected string m_mavenOptions;
        protected MavenStatus m_mavenStatus;
		protected bool m_forceOnlineMode;

		public MavenStatus Status
		{
			get { return m_mavenStatus; }
		}

		public bool ForceOnlineMode
		{
			get { return m_forceOnlineMode; }
			set { m_forceOnlineMode = value; }
		}

		public MavenProxy(EnvDTE.DTE applicationObject, AddInSettings addInSettings)
			: base(applicationObject)
		{
			Initialize(applicationObject, addInSettings);
					
		}

		public MavenProxy(ISynchronizeInvoke target, EnvDTE.DTE applicationObject, AddInSettings addInSettings) : base(target, applicationObject)
		{
			Initialize(applicationObject, addInSettings);
		}

		protected virtual void Initialize(EnvDTE.DTE applicationObject, AddInSettings addInSettings)
		{
			m_addInSettings = addInSettings;
			this.Failed += new ThreadExceptionEventHandler(m_mavenProxy_Failed);
			this.Cancelled += new EventHandler(m_mavenProxy_Canceled);
		}

        protected virtual void m_mavenProxy_Canceled(object sender, EventArgs e)
		{
			this.OutputWindowPane.OutputString("\n");
			this.OutputWindowPane.OutputString("Generation canceled\n");
			ApplicationObject.StatusBar.Text = "AndroMDA: Generation canceled";
		}

        protected virtual void m_mavenProxy_Failed(object sender, ThreadExceptionEventArgs e)
		{
			if (e.Exception is MavenProxyKnownException)
			{
				this.OutputWindowPane.OutputString(e.Exception.Message);
			}
			else
			{
				this.OutputWindowPane.OutputString("Error: An unhandled error occurred while attempting to run maven\n");
				this.OutputWindowPane.OutputString("Error: Exception details: " + e.Exception.Message + "\n");
			}
			ApplicationObject.StatusBar.Text = "AndroMDA: Generation failed";
			ApplicationObject.StatusBar.Highlight(true);
		}


        protected override void BeforeExecution()
		{
			ApplicationObject.StatusBar.Text = "AndroMDA: Generation started...";

			this.OutputWindow.Activate();
			this.OutputWindowPane.Clear();
			this.OutputWindowPane.Activate();

            if (m_addInSettings.MavenUseCustomCommandLine)
            {
                m_mavenOptions = m_addInSettings.MavenCustomCommandLine;
            }
            else
            {
                if (m_addInSettings.MavenCleanFirst)
                {
                    m_mavenOptions = "clean " + m_mavenOptions;
                }
                if (m_addInSettings.MavenUseOfflineMode)
                {
                    m_mavenOptions = "-o " + m_mavenOptions;
                }
            }

            if (m_forceOnlineMode)
            {
                m_mavenOptions = m_mavenOptions.Replace("-o", string.Empty);
                m_forceOnlineMode = false;
            }

			base.BeforeExecution();
		}

		protected override void DoWork()
		{
			this.WorkingDirectory = VSSolutionUtils.GetSolutionPath(this.ApplicationObject.Solution);
			ApplicationObject.StatusBar.Text = "AndroMDA: Generation Progress";
			try
			{
				base.DoWork();
			}
			catch (System.ComponentModel.Win32Exception we)
			{
				if (we.Message == "The system cannot find the file specified")
				{				
					throw new MavenProxyKnownException("Error: The java executable could not be found to run maven.\nError: Please check your java installation and ensure that the JAVA_HOME variable is set correctly\n");
				}
				else { throw; }
			}

			VSExternalToolEventArgs args = this.EventArguments as VSExternalToolEventArgs;
			if (args.ExitCode != 0)
			{
				args.ExitStatus = ToolExitStatus.Error;
			}

		}

    }
	
}
