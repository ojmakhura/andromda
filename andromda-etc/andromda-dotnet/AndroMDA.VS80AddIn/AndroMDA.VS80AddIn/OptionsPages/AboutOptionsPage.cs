
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

#endregion

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class AboutOptionsPage : UserControl, EnvDTE.IDTToolsOptionsPage
	{

		#region Member variables

		static AboutOptionsPageProperties m_properties = new AboutOptionsPageProperties();
		private MDASolutionManager m_solutionManager = null;
		private StatisticsGenerator m_statsGenerator = null;		
		#endregion

		#region Properties

		public MDASolutionManager SolutionManager
		{
			get { return m_solutionManager; }
			set { m_solutionManager = value;  }
		}

		public string SolutionUsingMDA
        {
            set 
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Solution Is Using MDA", value, "AndroMDA");
			}
        }

		public string ModelFile
		{
			set
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Model File", value, "AndroMDA");
			}
		}

        public string LastGenerated
        {
			set
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Last Generated", value, "AndroMDA");
			}
        }

        public string CoreProject
        {
			set
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Core Project", value, "Projects");
			}
		}

        public string CommonProject
        {
			set
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Common Project", value, "Projects");
			}
		}

		public string SchemaExportProject
		{
			set
			{
				AddInUtils.InsertListViewItem(listViewStatus, "Schema Export Project", value, "Projects");
			}
		}



		private ListViewItem GetListViewItem(string name, string value)
		{
			ListViewItem lvi = new ListViewItem();
			lvi.Text = name;
			lvi.SubItems.Add(new ListViewItem.ListViewSubItem(lvi, value));
			return lvi;
		}

		public void Reset()
		{
			listViewStatus.Items.Clear();
		}

		public ArrayList Statistics
		{
			set
			{
				listViewStatistics.BeginUpdate();
				listViewStatistics.Items.Clear();
				listViewStatistics.Groups.Clear();
				ListViewGroup codeGroup = new ListViewGroup("Code Statistics");
				ListViewGroup fileGroup = new ListViewGroup("File Statistics");
				listViewStatistics.Groups.Add(codeGroup);
				listViewStatistics.Groups.Add(fileGroup);
				ArrayList stats = value;
				foreach (MDASolutionManager.SolutionStatistic stat in stats)
				{
					ListViewItem lvi = GetListViewItem(stat.Name, stat.Value);
					switch(stat.StatGroup)
					{
						case MDASolutionManager.SolutionStatistic.Group.CodeStatistics:
							lvi.Group = codeGroup;
							break;
						case MDASolutionManager.SolutionStatistic.Group.FileStatistics:
							lvi.Group = fileGroup;
							break;
					}
					listViewStatistics.Items.Add(lvi);
				}
				listViewStatistics.EndUpdate();
			}
		}

		#endregion

        public AboutOptionsPage()
        {
            InitializeComponent();
            //linkLabel1.Links.Add(0, linkLabel1.Text.Length);
			lblVersion.Text = "v" + VS80AddIn.ADD_IN_VERSION;
			m_statsGenerator = new StatisticsGenerator(this);
			m_statsGenerator.Completed += new EventHandler(m_statsGenerator_Completed);
            
            richTextBox1.Rtf = @"{\rtf1\ansi\ansicpg1252\deff0\deflang1033{\fonttbl{\f0\fswiss\fcharset0 Arial;}}
{\*\generator Msftedit 5.41.15.1507;}\viewkind4\uc1\pard\qc\f0\par\fs36 Android/VS\par
\fs20 Visual Studio 2005 integration for AndroMDA \par
\par
(c)2007 AndroMDA - http://www.andromda.org/\par
\par
\b Primary Development\par
\b0  Chris Micali <chris@andromda.org>\par
\par
\b SchemaExport Code\par
\b0 Naresh Bhatia\par
Eric Crutchfield\par
\par
\b Testing and Feedback\b0\par
Kapil Viren Ahuja\par
Manish Agrawal\par
Vaneet Kaur\par
Karthick Pachiappan\par
\b\par
}";

        
        }

        #region IDTToolsOptionsPage Members

        public void OnAfterCreated(EnvDTE.DTE DTEObject)
        {

        }
		
		public void OnOK()
		{
		}

        public void OnCancel()
        {
        }

        public void OnHelp()
        {
        }

		public void GetProperties(ref object PropertiesObject)
		{
			PropertiesObject = m_properties;
		}

        #endregion

		private void linkLabel_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
		{
			LinkLabel label = (LinkLabel)sender;
			System.Diagnostics.Process.Start(label.Text);
		}

		private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
		{
			if (tabControl1.SelectedIndex == 1)
			{
				if (listViewStatistics.Items.Count == 0 && m_solutionManager.IsEnabled)
				{
					listViewStatistics.Visible = false;
					pnlGenerating.Visible = true;
					m_statsGenerator.SolutionManager = m_solutionManager;
					m_statsGenerator.Start();
				}
				else
				{
					pnlGenerating.Visible = false;
				}
			}
		}

		void m_statsGenerator_Completed(object sender, EventArgs e)
		{
			this.Statistics = m_statsGenerator.Statistics;
			listViewStatistics.Visible = true;
			pnlGenerating.Visible = false;
		}


		private class StatisticsGenerator : AsyncOperation
		{
			private ArrayList m_statistics;
			private MDASolutionManager m_solutionManager;

			public MDASolutionManager SolutionManager
			{
				get { return m_solutionManager; }
				set { m_solutionManager = value; }
			}

			public ArrayList Statistics
			{
				get { return m_statistics; }
				set { m_statistics = value; }
			}

			public StatisticsGenerator(ISynchronizeInvoke target)
				: base(target)
			{
				m_statistics = null;
			}

			public StatisticsGenerator(ISynchronizeInvoke target, MDASolutionManager solutionManager)
				: base(target)
			{
				m_statistics = null;
				m_solutionManager = solutionManager;
			}

			protected override void DoWork()
			{
				m_statistics = m_solutionManager.GetSolutionStatistics();
			}
		}

        private void richTextBox1_LinkClicked(object sender, LinkClickedEventArgs e)
        {
            System.Diagnostics.Process.Start(e.LinkText);
        }

	}



    [System.Runtime.InteropServices.ComVisible(true)]
    [System.Runtime.InteropServices.ClassInterface(System.Runtime.InteropServices.ClassInterfaceType.AutoDual)]
    public class AboutOptionsPageProperties
    {
    }
}
