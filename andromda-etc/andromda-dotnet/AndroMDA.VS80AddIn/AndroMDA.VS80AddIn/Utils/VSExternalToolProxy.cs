
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Collections.Specialized;
using System.Text;
using System.Threading;
using System.ComponentModel;
using System.Diagnostics;
using System.Windows.Forms;

using System.Runtime.InteropServices;

#endregion

namespace AndroMDA.VS80AddIn
{
	public class VSExternalToolProxy : AsyncOperation
	{

		#region Member variables

		public delegate void ProcessTextDelegate(ref string text, ref bool cancelOutput);

		public event ProcessTextDelegate ProcessStdOut;
		public event ProcessTextDelegate ProcessStdErr;

		public enum ToolExitStatus { DidNotExit, Canceled, Error, Success };

		private string m_executablePath = string.Empty;
		private string m_workingDirectory = string.Empty;
		private string m_commandLine = string.Empty;
		private EnvDTE.DTE m_applicationObject = null;
		private EnvDTE.OutputWindowPane m_outputWindowPane = null;
		private EnvDTE.Window m_outputWindow = null;
		private NameValueCollection m_environmentVariables = new NameValueCollection();		
		public ArrayList m_outputLines;
		public string m_output = string.Empty;
		System.Diagnostics.ProcessStartInfo m_psi = null;

		#endregion

		#region Properties

		public string Output
		{
			get 
			{
				if (m_output == string.Empty)
				{
					lock (m_outputLines.SyncRoot)
					{
						foreach (string s in m_outputLines)
						{
							m_output += s;
						}
					}
				}
				return m_output;
			}
		}

		public ArrayList OutputLines
		{
			get { return ArrayList.Synchronized(m_outputLines); }
		}

		public NameValueCollection EnvironmentVariables
		{
			get { return m_environmentVariables; }
			set { m_environmentVariables = value; }
		}

		public EnvDTE.DTE ApplicationObject
		{
			get { return m_applicationObject; }
		}

		public EnvDTE.Window OutputWindow
		{
			get { return m_outputWindow; }
			set { m_outputWindow = value; }
		}

		public EnvDTE.OutputWindowPane OutputWindowPane
		{
			get { return m_outputWindowPane; }
			set { m_outputWindowPane = value; }
		}

		public string CommandLine
		{
			get { return m_commandLine; }
			set { m_commandLine = value; }
		}
	
		public string WorkingDirectory
		{
			get { return m_workingDirectory; }
			set { m_workingDirectory = value; }
		}
	
		public string ExecutablePath
		{
			get { return m_executablePath; }
			set { m_executablePath = value; }
		}

		#endregion

		public VSExternalToolProxy(EnvDTE.DTE applicationObject)
			: base()
		{
			m_applicationObject = applicationObject;
		}
	
		public VSExternalToolProxy(ISynchronizeInvoke target, EnvDTE.DTE applicationObject) : base(target)
		{
			m_applicationObject = applicationObject;			
		}

		public bool OutputContains(string value)
		{
			foreach (string line in OutputLines)
			{
				if (line.Contains(value))
				{
					return true;
				}
			}
			return false;
		}

		protected override void BeforeExecution()
		{
			m_psi = new System.Diagnostics.ProcessStartInfo();            
			foreach (string key in m_environmentVariables.Keys)
			{
				m_psi.EnvironmentVariables.Add(key, m_environmentVariables[key]);
			}
			m_output = string.Empty;
			m_outputLines = new ArrayList();
		}

		public void Stop()
		{
			if (this.IsRunning)
			{
				this.CancelAndWait();
			}
		}

		protected override void DoWork()
		{
            using (Process toolProcess = new Process())
            {
                // Set up process info.
                System.Diagnostics.ProcessStartInfo psi = new System.Diagnostics.ProcessStartInfo();
                m_psi.FileName = m_executablePath;
                m_psi.Arguments = m_commandLine;
                m_psi.WorkingDirectory = m_workingDirectory;
                m_psi.CreateNoWindow = true;
                m_psi.UseShellExecute = false;
                m_psi.RedirectStandardOutput = true;
                m_psi.RedirectStandardError = true;

                toolProcess.EnableRaisingEvents = true;
                toolProcess.ErrorDataReceived += new DataReceivedEventHandler(toolProcess_ErrorDataReceived);
                toolProcess.OutputDataReceived += new DataReceivedEventHandler(toolProcess_OutputDataReceived);

                // Associate process info with the process.
                toolProcess.StartInfo = m_psi;

                VSExternalToolEventArgs eventArguments = new VSExternalToolEventArgs();
                this.EventArguments = eventArguments;

                if (!toolProcess.Start())
                {
                    throw new Exception("External tool failed to start");
                }

                toolProcess.BeginOutputReadLine();
                toolProcess.BeginErrorReadLine();

                try
                {
                    m_applicationObject.MainWindow.SetFocus();

                    while (!toolProcess.WaitForExit(250) && !CancelRequested)
                    {
                    }


                    if (!CancelRequested)
                    {
                        eventArguments.ExitCode = toolProcess.ExitCode;
                        eventArguments.ExitStatus = ToolExitStatus.Success;
                    }
                    else
                    {
                        toolProcess.Kill();
                        eventArguments.ExitStatus = ToolExitStatus.Canceled;
                        AcknowledgeCancel();
                    }
                }
                catch (Exception e)
                {
                    if (toolProcess != null && !toolProcess.HasExited)
                    {
                        toolProcess.Kill();
                    }
                    eventArguments.ExitStatus = ToolExitStatus.Error;
                    throw (e);
                }
                finally
                {
                    if (toolProcess != null)
                    {
                        toolProcess.Close();
                    }
                }
            }
		}

		void toolProcess_ErrorDataReceived(object sender, DataReceivedEventArgs e)
		{
			ProcessOutput(e.Data, true);
		}

		void toolProcess_OutputDataReceived(object sender, DataReceivedEventArgs e)
		{
			ProcessOutput(e.Data, false);
		}

		void ProcessOutput(string input, bool isStdErr)
		{
			if (input != null)
			{
				bool cancelOutput = false;
				if (isStdErr)
				{
                    if (ProcessStdErr != null)
                    {
                        ProcessStdErr(ref input, ref cancelOutput);
                    }
				}
				else
				{
                    if (ProcessStdOut != null)
                    {
                        ProcessStdOut(ref input, ref cancelOutput);
                    }
				}
				if (!cancelOutput)
				{
					this.OutputLines.Add(input);
					this.OutputWindowPane.OutputString(input + "\n");
				}
			}
		}

	}
}
