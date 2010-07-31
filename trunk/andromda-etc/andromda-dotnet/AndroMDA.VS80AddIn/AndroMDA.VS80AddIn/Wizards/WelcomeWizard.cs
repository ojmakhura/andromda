using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Collections;

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class WelcomeWizard : Form
    {

        #region System Check Base Class

        private class SystemCheck : ListViewItem
        {

            public enum TestStatus { Ok, Warning, Error, Info, NotRun };
            protected TestStatus m_testType;
            protected TestStatus m_testStatus = TestStatus.NotRun;
            protected string m_successMessage;
            protected string m_failureMessage;
            protected string m_failureDetails;
            protected string m_fixDetails;

            public string SuccessMessage
            {
                get { return m_successMessage; }
            }

            public string FailureMessage
            {
                get { return m_failureMessage; }
            }

            public string FailureDetails
            {
                get { return m_failureDetails; }
            }

            public string FixDetails
            {
                get { return m_fixDetails; }
            }

            public TestStatus Status
            {
                get { return m_testStatus; }
            }

            public virtual bool HasFix { get { return false; } }

            public SystemCheck() 
            {
                Init();
            }

            public SystemCheck(string title, ListViewGroup group)
                : base(title, 0, group)
            {
                m_testType = TestStatus.Error; 
                this.SubItems.Add(new ListViewSubItem(this, m_successMessage));
                Init();
            }

            public virtual void Init() { }
            public virtual bool PerformTest() { return false;  }
            public virtual void PerformFix() { }

            public TestStatus RunTest()
            {
                bool result = false;
                try { result = PerformTest(); }
                catch { }
                if (result)
                {
                    this.SubItems[1].Text = m_successMessage;
                    this.ToolTipText = m_successMessage;
                    this.ImageIndex = 1;
                    m_testStatus = TestStatus.Ok;
                }
                else
                {
                    switch (m_testType)
                    {
                        case TestStatus.Info:
                            this.ImageIndex = 0;
                            break;
                        case TestStatus.Warning:
                            this.ImageIndex = 2;
                            break;
                        case TestStatus.Error:
                            this.ImageIndex = 3;
                            break;
                    }
                    this.SubItems[1].Text = m_failureMessage;
                    this.ToolTipText = m_failureMessage;
                    m_testStatus = m_testType;
                }
                return m_testStatus;
            }

            public TestStatus RunFix()
            {
                if (!HasFix) return TestStatus.Error;
                try
                {
                    this.PerformFix();
                    return RunTest();
                }
                catch(Exception e)
                {
                    AddInUtils.ShowError("The fix was not able to complete: " + e.Message);
                }
                return TestStatus.Error;
            }
            public override object Clone()
            {
                SystemCheck item = (SystemCheck)base.Clone();                
                item.SubItems.Add(new ListViewSubItem(this, this.SubItems[1].Text));
                return item;
            }
        }

        #endregion

        #region System Checks

        private class Maven2InstallCheck : SystemCheck
        {
            public Maven2InstallCheck() { }
            public Maven2InstallCheck(ListViewGroup group)
                : base("Installation", group) {}
            public override void Init()
            {
                m_successMessage = "Maven 2 was found";
                m_failureMessage = "The Maven 2 executable (mvn.bat) was not found";
                m_failureDetails = "The Maven 2 executable (mvn.bat) was not found in the system search path.  It appears that Maven 2 is not properly installed.  You can download Maven 2 from http://maven.apache.org/.  Additionally the maven bin directory needs to be added to your system PATH variable.  If you setup a MAVEN_HOME or M2_HOME variable, make sure it is also in PATH variable also.";
                m_testType = TestStatus.Error;
            }
            public override bool PerformTest()
            {
                return FileUtils.IsInSearchPath("mvn.bat");
            }
        }

        private class Maven2SettingsCheck : SystemCheck
        {
            protected AddInSettings m_addInSettings = null;
            public Maven2SettingsCheck(AddInSettings settings)
            {
                m_addInSettings = settings;
            }
            public Maven2SettingsCheck(ListViewGroup group, AddInSettings settings)
                : base("Settings.xml", group)
            {
                m_addInSettings = settings;
            }
            public override bool HasFix { get { return true; } }
            public override void Init()
            {
                m_successMessage = "Settings.xml found";
                m_failureMessage = "Settings.xml not found";
                m_failureDetails = "The Maven 2 settings.xml file was not found.  You need to create this file in " + FileUtils.GetUserHome() + "\\.m2.  This file tells Maven from where it should download the AndroMDA packages.";
                m_fixDetails = "Click this to write the settings.xml file to your maven config directory.";
                m_testType = TestStatus.Error;
            }
            public override bool PerformTest()
            {
                return System.IO.File.Exists(FileUtils.GetUserHome() + "\\.m2\\" + "settings.xml");
            }
            public override void PerformFix()
            {
                string path = FileUtils.GetUserHome() + "\\.m2";
                if (!System.IO.Directory.Exists(path))
                {
                    System.IO.Directory.CreateDirectory(path);
                }
                System.IO.File.Copy(m_addInSettings.WelcomeWizardResourcesLocation + "\\settings.xml", path + "\\settings.xml");
            }
        }

        private class JavaHomeCheck : SystemCheck
        {
            public override bool HasFix { get { return true; } }
            public JavaHomeCheck() { }
            public JavaHomeCheck(ListViewGroup group)
                : base("JAVA_HOME Variable", group) { }
            public override void Init()
            {
                m_successMessage = "The JAVA_HOME environment variable is set";
                m_failureMessage = "The JAVA_HOME environment variable is not set or invalid";
                m_failureDetails = "The JAVA_HOME environment variable has not been set to the location of the java runtime.  Make sure you have the JRE installed, which can be downloaded from http://java.sun.com.  Also be sure this environment variable is set in the Advanced | Environment Variables page of the System control panel.";
                m_fixDetails = "Click this to locate and set the JAVA_HOME variable.";
                m_testType = TestStatus.Error;
            }
            public override bool PerformTest()
            {
                string javaHome = System.Environment.GetEnvironmentVariable("JAVA_HOME");
                return (
                        javaHome != null && 
                        javaHome != string.Empty && 
                        System.IO.Directory.Exists(javaHome) &&
                        System.IO.File.Exists(javaHome + "\\bin\\java.exe")
                        );
            }
            public override void PerformFix()
            {
                OpenFileDialog ofd = new OpenFileDialog();
                ofd.Multiselect = false;
                ofd.CheckFileExists = false;
                ofd.CheckPathExists = true;
                ofd.FileName = "SELECTED";
                ofd.Filter = "JRE Home Directory|";
                ofd.Title = "Please select the the JRE home directory";
                ofd.InitialDirectory = "C:\\Program Files\\Java";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string javaHome = FileUtils.GetPathFromFilename(ofd.FileName);
                    System.Environment.SetEnvironmentVariable("JAVA_HOME", javaHome, EnvironmentVariableTarget.Process);
                    System.Environment.SetEnvironmentVariable("JAVA_HOME", javaHome, EnvironmentVariableTarget.User);
                }
            }
        }

        private class JRECheck : SystemCheck
        {
            public JRECheck() { }
            public JRECheck(ListViewGroup group)
                : base("Java Runtime", group) { }
            public override void Init()
            {
                m_successMessage = "Java.exe was found";
                m_failureMessage = "Java.exe was not found";
                m_failureDetails = "The JRE executable Java.exe could not be found.  Make sure you have the JRE installed, which can be downloaded from http://java.sun.com.";
                m_testType = TestStatus.Error;
            }
            public override bool PerformTest()
            {
                string javaHome = System.Environment.GetEnvironmentVariable("JAVA_HOME");
                return System.IO.File.Exists(javaHome + "\\bin\\java.exe");
            }
        }

        private class UmlModelerCheck : SystemCheck
        {
            AddInSettings m_addInSettings = null;
            public UmlModelerCheck() { }
            public UmlModelerCheck(ListViewGroup group, AddInSettings settings)
                : base("UML Modeler", group) { m_addInSettings = settings; }
           public override void Init()
            {
                m_successMessage = "A UML modeler was found and is configured correctly.";
                m_failureMessage = "A UML modeler was not found (Configure this in Tools | Options)";
                m_testType = TestStatus.Warning;
            }
            public override bool PerformTest()
            {
                if (TestPath(m_addInSettings.UMLModellerPath)) return true;
                string path = "C:\\Program Files\\MagicDraw UML Community Edition\\bin\\mduml.exe";
                if (TestPath(path)) { m_addInSettings.UMLModellerPath = path; return true; }
                path = "C:\\Program Files\\MagicDraw UML Personal Edition\\bin\\mduml.exe";
                if (TestPath(path)) { m_addInSettings.UMLModellerPath = path; return true; }
                path = "C:\\Program Files\\MagicDraw UML Standard Edition\\bin\\mduml.exe";
                if (TestPath(path)) { m_addInSettings.UMLModellerPath = path; return true; }
                path = "C:\\Program Files\\MagicDraw UML Professional Edition\\bin\\mduml.exe";
                if (TestPath(path)) { m_addInSettings.UMLModellerPath = path; return true; }
                path = "C:\\Program Files\\MagicDraw UML Enterprise Edition\\bin\\mduml.exe";
                if (TestPath(path)) { m_addInSettings.UMLModellerPath = path; return true; }
                return false;
            }

            private bool TestPath(string path)
            {
                if (System.IO.File.Exists(path))
                {
                    if ((path.Contains("mduml") || path.Contains("MagicDraw")))
                    {
                        m_successMessage = "MagicDraw was found and is configured correctly.";
                    }
                    return true;
                }
                return false;
            }
        }

        private class MavenHomeCheck : SystemCheck
        {
            public MavenHomeCheck() { }
            public MavenHomeCheck(ListViewGroup group)
                : base("MAVEN_HOME Variable", group) { }
            public override bool HasFix { get { return true; } }
            public override void Init()
            {
                m_successMessage = "The MAVEN_HOME environment variable is set";
                m_failureMessage = "The MAVEN_HOME environment variable is not set or invalid";
                m_failureDetails = "The MAVEN_HOME environment variable has not been set to the location of your Maven 1 installation.  Make sure you have the installed Maven 1 and you have set the MAVEN_HOME variable in the Advanced | Environment section of the System control panel.";
                m_fixDetails = "Click this to locate and set the MAVEN_HOME variable.";
                m_testType = TestStatus.Warning;
            }
            public override bool PerformTest()
            {
                string mavenHome = System.Environment.GetEnvironmentVariable("MAVEN_HOME");
                return (
                        mavenHome != null && 
                        mavenHome != string.Empty && 
                        System.IO.Directory.Exists(mavenHome) &&
                        System.IO.File.Exists(mavenHome + "\\lib\\maven.jar")
                        );
            }
            public override void PerformFix()
            {                
                OpenFileDialog ofd = new OpenFileDialog();
                ofd.Multiselect = false;                
                ofd.CheckFileExists = false;
                ofd.CheckPathExists = true;
                ofd.FileName = "SELECTED";
                ofd.Filter = "Maven Home Directory|";
                ofd.Title = "Please select the the Maven 1 home directory";
                ofd.InitialDirectory = "C:\\Program Files\\Apache Software Foundation\\Maven 1.0.2";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string mavenHome = FileUtils.GetPathFromFilename(ofd.FileName);
                    System.Environment.SetEnvironmentVariable("MAVEN_HOME", mavenHome, EnvironmentVariableTarget.Process);
                    System.Environment.SetEnvironmentVariable("MAVEN_HOME", mavenHome, EnvironmentVariableTarget.User);
                }
            }
        }

        private class Maven1InstallCheck : SystemCheck
        {
            public Maven1InstallCheck() { }
            public Maven1InstallCheck(ListViewGroup group)
                : base("Installation", group) { }
            public override void Init()
            {
                m_successMessage = "Maven 1 was found";
                m_failureMessage = "The Maven 1 executable (maven.bat) was not found";
                m_failureDetails = "The Maven 1 executable (maven.bat) was not found in the system search path.  It appears that Maven 1 is not properly installed.  You can download Maven 1 from http://www.apache.org/dyn/closer.cgi/maven/binaries/.  Additionally the maven bin directory needs to be added to your system PATH variable.";
                m_testType = TestStatus.Warning;
            }
            public override bool PerformTest()
            {
                string mavenHome = System.Environment.GetEnvironmentVariable("MAVEN_HOME");
                return System.IO.File.Exists(mavenHome + "\\bin\\maven.bat");
            }
        }

        private class Maven1SettingsCheck : SystemCheck
        {
            private AddInSettings m_settings = null;
            public override bool HasFix { get { return true; } }
            public Maven1SettingsCheck(AddInSettings settings) 
            {
                m_settings = settings;
            }
            public Maven1SettingsCheck(ListViewGroup group, AddInSettings settings)
                : base("Configuration", group)
            {
                m_settings = settings;
            }
            public override void Init()
            {
                m_successMessage = "Build.properties found";
                m_failureMessage = "Build.properties not found";
                m_failureDetails = "The Maven 1 build.properties file was not found.  You need to create this file in " + FileUtils.GetUserHome() + ".  This file tells Maven from where it should download the AndroMDA packages.";
                m_fixDetails = "Click this to write the build.properties to your user home directory.";
                m_testType = TestStatus.Warning;
            }
            public override bool PerformTest()
            {
                return System.IO.File.Exists(FileUtils.GetUserHome() + "\\" + "build.properties");
            }
            public override void PerformFix()
            {
                string path = FileUtils.GetUserHome();
                System.IO.File.Copy(m_settings.WelcomeWizardResourcesLocation + "\\build.properties", path + "\\build.properties");
                //FileUtils.WriteFile(path + "\\build.properties", Resource1.build_properties);
            }
        }

        #endregion

        #region Member Variables

        private Maven2InstallCheck itemMaven2Install;
        private Maven2SettingsCheck itemMaven2SettingsXml;
        private JavaHomeCheck itemJavaHome;
        private JRECheck itemJRE;
        private UmlModelerCheck itemMagicDraw;
        private MavenHomeCheck itemMavenHomeCheck;
        private Maven1InstallCheck itemMaven1Install;
        private Maven1SettingsCheck itemMaven1BuildProperties;

        private ListViewGroup envGroupMaven2;
        private ListViewGroup envGroupJava;
        private ListViewGroup envGroupUmlTools;
        private ListViewGroup envGroupMaven1;

        private AddInSettings m_settings = null;

        private int m_errors;
        private int m_warnings;

        #endregion

        #region Properties

        public bool ShowWizardAgain
        {
            get { return !cbDontShowWizard.Checked; }
        }

        #endregion

        public WelcomeWizard(AddInSettings settings)
        {
            InitializeComponent();
            m_settings = settings;
            wizard1.CancelEnabled = false;

            envGroupMaven2 = lstEnvironment.Groups.Add("Maven 2", "Maven 2");
            envGroupJava = lstEnvironment.Groups.Add("Java 5", "Java 5");
            envGroupUmlTools = lstEnvironment.Groups.Add("UML Tools", "UML Tools");
            //envGroupMaven1 = lstEnvironment.Groups.Add("Maven 1 (optional)", "Maven 1 (optional)");

            itemMaven2Install = new Maven2InstallCheck(envGroupMaven2);
            itemMaven2SettingsXml = new Maven2SettingsCheck(envGroupMaven2, m_settings);
            itemJavaHome = new JavaHomeCheck(envGroupJava);
            itemJRE = new JRECheck(envGroupJava);
            itemMagicDraw = new UmlModelerCheck(envGroupUmlTools, m_settings);
            //itemMaven1Install = new Maven1InstallCheck(envGroupMaven1);
            //itemMavenHomeCheck = new MavenHomeCheck(envGroupMaven1);
            //itemMaven1BuildProperties = new Maven1SettingsCheck(envGroupMaven1, m_settings);

            lstEnvironment.Items.AddRange(new ListViewItem[] {
                itemMaven2Install,
                itemMaven2SettingsXml,
                itemJavaHome,
                itemJRE,
                itemMagicDraw
                //itemMavenHomeCheck,
                //itemMaven1Install,
                //itemMaven1BuildProperties,
            });

        }

        private void pageEnvironment_ShowFromNext(object sender, EventArgs e)
        {

            m_errors = 0;
            m_warnings = 0;
            lstFailureDetails.Items.Clear();
            foreach (SystemCheck check in lstEnvironment.Items)
            {
                switch (check.RunTest())
                {
                    case SystemCheck.TestStatus.Error:
                        {
                            m_errors++;
                            SystemCheck newItem = (SystemCheck)check.Clone();
                            lstFailureDetails.Items.Add(newItem);
                            break;
                        }
                    case SystemCheck.TestStatus.Warning:
                        m_warnings++;
                        break;
                }
            }
            if (m_errors > 0)
            {
                header6.Description = m_errors.ToString() + " errors were encountered.  Please click Next to see detailed instructions on how to correct them.";
            }
            else
            {
                header6.Description = "Your system is properly configured to use AndroMDA.";
            }
        }


        private void linkLabel_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            LinkLabel label = (LinkLabel)sender;
            System.Diagnostics.Process.Start(label.Text);
        }

        private void cbDontShowWizard_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void pageIssues_ShowFromBack(object sender, EventArgs e)
        {
            if (m_errors == 0)
            {
                wizard1.Back();
            }
            else
            {
            }
        }

        private void pageIssues_ShowFromNext(object sender, EventArgs e)
        {
            if (m_errors == 0)
            {
                wizard1.Next();
            }
            else
            {
                lstFailureDetails.SelectedIndices.Clear();
                if (lstFailureDetails.Items.Count > 0)
                {
                    lstFailureDetails.Items[0].Selected = true;
                }
                lstFailureDetails.Focus();
            }
        }

        private void listView1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (lstFailureDetails.SelectedItems.Count > 0)
            {
                SystemCheck check = (SystemCheck)lstFailureDetails.SelectedItems[0];
                lblIssueDescription.Text = check.FailureDetails;
                if (check.Status == SystemCheck.TestStatus.Ok)
                {
                    lblIssueDescription.Text = "This issue has been successfully resolved.";
                    lblFixDescription.Text = string.Empty;
                    btnRetest.Visible = btnFix.Visible = false;
                    btnRetest.Tag = btnFix.Tag = null;
                }
                else
                {
                    btnFix.Visible = check.HasFix;
                    btnRetest.Visible = true;
                    btnRetest.Tag = btnFix.Tag = check;
                    lblFixDescription.Text = check.FixDetails;
                }
            }
            else
            {
                lblIssueDescription.Text = string.Empty;
                lblFixDescription.Text = string.Empty;
                btnRetest.Visible = btnFix.Visible = false;
                btnRetest.Tag = btnFix.Tag = null;                
            }
        }

        private void btnFix_Click(object sender, EventArgs e)
        {
            if (btnFix.Tag != null)
            {
                SystemCheck item = (SystemCheck)btnFix.Tag;
                item.RunFix();
                Retest();
                item.Selected = false;
                item.Selected = true;
                lstFailureDetails.Focus();
            }
        }

        private void btnRetest_Click(object sender, EventArgs e)
        {
            if (btnRetest.Tag != null)
            {
                SystemCheck item = (SystemCheck)btnFix.Tag;
                Retest();
                item.Selected = false;
                item.Selected = true;
                lstFailureDetails.Focus();
            }
        }

        private void Retest()
        {
            int errors = 0;
            foreach (SystemCheck check in lstFailureDetails.Items)
            {
                if (check.RunTest() == SystemCheck.TestStatus.Error)
                {
                    errors++;
                }
            }
            cbDontShowWizard.Checked = errors == 0;
        }

        private void lblIssueDescription_LinkClicked(object sender, LinkClickedEventArgs e)
        {
            System.Diagnostics.Process.Start(e.LinkText);
        }


    }
}