using System;
using System.Text;
using Microsoft.Build.Utilities;
using System.Collections;

namespace AndroMDA.MSBuild.Tasks
{
    public class AndroMDA : ToolTask
    {

        public const string VERSION = "1.0";

        #region Member Variables

        private bool m_synchronizeProjectTree = false;
        private bool m_offline = false;

        private string m_executablePath;
        private string m_commandLine;
        #endregion

        #region Properties

        public bool SynchronizeProjectTree
        {
            get { return m_synchronizeProjectTree; }
            set { m_synchronizeProjectTree = value; }
        }

        public bool Offline
        {
            get { return m_offline; }
            set { m_offline = value; }
        }

        protected override string ToolName
        {
            get { return m_executablePath; }
        }

        #endregion

        protected override void LogEventsFromTextOutput(string singleLine, Microsoft.Build.Framework.MessageImportance messageImportance)
        {
            base.LogEventsFromTextOutput(singleLine, messageImportance);
        }
        public AndroMDA()
        {
            string java_home = System.Environment.GetEnvironmentVariable("JAVA_HOME");
            string maven_home = System.Environment.GetEnvironmentVariable("M2_HOME");
            string maven_java_exe = java_home + "\\bin\\java.exe";
            string maven_opts = System.Environment.GetEnvironmentVariable("MAVEN_OPTS");
            string maven_cmd_line_args = "install ";
            string classworlds_jar = string.Empty;

            if (Offline) maven_cmd_line_args += "-o ";

            if (java_home == null || java_home == string.Empty)
            {
                throw new Exception("Error: Maven cannot run because the JAVA_HOME environment variable is not set.\nError: Please check your java installation.");
            }

            if (maven_home == null || maven_home == string.Empty)
            {
                throw new Exception("Error: Maven cannot run because the M2_HOME environment variable is not set.\nError: Please check that maven 2.x has been correctly installed.");
            }

            if (maven_opts == null || maven_opts == string.Empty)
            {
                maven_opts = "-Xmx256m";
            }

            string[] classworldJars = System.IO.Directory.GetFiles(maven_home + "\\core\\boot", "classworlds-*.jar", System.IO.SearchOption.TopDirectoryOnly);
            classworlds_jar = classworldJars[0];
            m_executablePath = maven_java_exe;
            m_commandLine = maven_opts + 
                            " -classpath \"" + 
                            classworlds_jar + "\" \"-Dclassworlds.conf=" + 
                            maven_home + "\\bin\\m2.conf\" " +
                            "\"-Dmaven.home=" + maven_home + 
                            "\" org.codehaus.classworlds.Launcher "
                            + maven_cmd_line_args;
        }

        public override bool Execute()
        {
            Log.LogMessage("AndroMDA MSBuild Task v" + VERSION);
            if (SynchronizeProjectTree)
            {
                DoSynchronizeProjectTree();
            }
            return base.Execute();
        }

        protected override string GenerateFullPathToTool()
        {
            return m_executablePath;
        }

        protected override string GenerateCommandLineCommands()
        {
            return m_commandLine;
        }

        private void DoSynchronizeProjectTree()
        {
            // TODO: Sync project tree
        }
    }
}
