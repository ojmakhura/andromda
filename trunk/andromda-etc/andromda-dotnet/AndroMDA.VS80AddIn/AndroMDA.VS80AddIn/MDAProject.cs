 
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
    public class MDAProject
    {

        #region Member variables

        private Project m_project;
        private string m_name;
        private string m_projectPath;
        private string m_generatedPath;
        private string m_manualPath;
        private string m_generatedRelativePath;
        private string m_manualRelativePath;

        #endregion

        #region Properties 

        public string ManualPath
        {
            get { return m_manualPath; }
            set { m_manualPath = value; }
        }
	
        public string GeneratedPath
        {
            get { return m_generatedPath; }
            set { m_generatedPath = value; }
        }
	
        public string ProjectPath
        {
            get { return m_projectPath; }
            set { m_projectPath = value; }
        }

        public string GeneratedRelativePath
        {
            get { return m_generatedRelativePath; }
            set { m_generatedRelativePath = value; }
        }

        public string ManualRelativePath
        {
            get { return m_manualRelativePath; }
            set { m_manualRelativePath = value; }
        }

        public string Name
        {
            get { return m_name; }
            set { m_name = value; }
        }

		public Project Project
        {
            get { return m_project; }
            set { m_project = value; }
        }

        public ProjectItem GeneratedPathProjectItem
        {
            get { return FindProjectFolder(this.GeneratedRelativePath); }
        }

        public ProjectItem ManualPathProjectItem
        {
            get { return FindProjectFolder(this.ManualRelativePath); }
        }

        #endregion

        public MDAProject(Project project, string name, string projectPath, string generatedPath, string manualPath)
        {
            m_name = name;
            m_project = project;
            projectPath = FileUtils.CleanDirectory(projectPath);
            generatedPath = FileUtils.CleanDirectory(generatedPath);
            manualPath = FileUtils.CleanDirectory(manualPath);
            m_projectPath = projectPath;
            m_generatedPath = generatedPath;
            m_manualPath = manualPath;
            m_generatedRelativePath = generatedPath.Replace(projectPath, string.Empty).Trim(new char[] { '\\' });
            m_manualRelativePath = manualPath.Replace(projectPath, string.Empty).Trim(new char[] { '\\' });
        }

        public ProjectItem FindProjectFolder(string folderName)
        {
            return FindProjectItem(folderName, EnvDTE.Constants.vsProjectItemKindPhysicalFolder);
        }

        public ProjectItem FindProjectItem(string itemName)
        {
            return FindProjectItem(itemName, string.Empty);
        }

        public ProjectItem FindProjectItem(string itemName, string itemKind)
        {
			return VSSolutionUtils.FindProjectItem(this.Project, itemName, itemKind);
        }

    }
}
