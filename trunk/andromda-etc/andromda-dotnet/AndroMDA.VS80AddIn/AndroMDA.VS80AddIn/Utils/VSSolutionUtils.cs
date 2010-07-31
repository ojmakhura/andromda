 
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;

using EnvDTE;
using EnvDTE80;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class VSSolutionUtils
    {
        public static string ProjectKindSolutionFolder = "{66A26720-8FB5-11D2-AA7E-00C04F688DDE}";
        public static string ProjectKindVisualBasic = "{F184B08F-C81C-45F6-A57F-5ABD9991F28F}";
        public static string ProjectKindVisualCS = "{FAE04EC0-301F-11D3-BF4B-00C04F79EFBC}";
        public static string ProjectKindVisualCPP = "{8BC9CEB8-8B4A-11D0-8D11-00A0C91BC942}";
        public static string ProjectKindVisualJS = "{E6FDF86B-F3D1-11D4-8576-0002A516ECE8}";
        public static string ProjectKindWeb = "{E24C65DC-7377-472b-9ABA-BC803B73C61A}";

        public static string GetSolutionName(Solution currentSolution)
        {
            if (!currentSolution.IsOpen) return string.Empty;
            return FileUtils.GetFilename(currentSolution.FileName).Replace(".sln", string.Empty);
        }

        public static string GetSolutionPath(Solution currentSolution)
        {
            if (!currentSolution.IsOpen) return string.Empty;
            return FileUtils.GetPathFromFilename(currentSolution.FileName);
        }

		public static Project AddProjectToSolution(string projectName, string projectTemplateFile, string projectTemplateLanguage, string[] filesToRemove, Solution2 currentSolution)
		{
			if (!currentSolution.IsOpen || projectName == string.Empty) return null;
			string destinationPath = GetSolutionPath((Solution)currentSolution);
			destinationPath += "\\" + projectName;
			string csTemplatePath = currentSolution.GetProjectTemplate(projectTemplateFile, projectTemplateLanguage);
			Project newProject = currentSolution.AddFromTemplate(csTemplatePath, destinationPath, projectName, false);
			if (newProject == null)
			{
				if (projectTemplateLanguage == "Web" || projectTemplateLanguage == "Web\\CSharp")
				{
					projectName = destinationPath + "\\";
				}
				foreach (Project p in currentSolution.Projects)
				{
					if (p.Name == projectName)
					{
						newProject = p;
					}
				}
			}
			if (newProject != null)
			{
				foreach (ProjectItem item in newProject.ProjectItems)
				{
					foreach (string filename in filesToRemove)
					{
						if (item.Name == filename)
						{
							item.Delete();
							break;
						}
					}
				}
			}
			else
			{
				throw new Exception("Unable to locate project in solution after it was created (" + projectName + ")");
			}
			return newProject;
		}

        public static Project AddClassLibraryProjectToSolution(string projectName, Solution2 currentSolution)
        {
			return AddProjectToSolution(projectName, "ClassLibrary.zip", "CSharp", new string[] { "Class1.cs" }, currentSolution);
        }

        public static Project AddConsoleAppProjectToSolution(string projectName, Solution2 currentSolution)
        {
			return AddProjectToSolution(projectName, "ConsoleApplication.zip", "CSharp", new string[] { "Program.cs" }, currentSolution);
        }

		public static Project AddWebProjectToSolution(string projectName, Solution2 currentSolution)
		{
			return AddProjectToSolution(projectName, "WebApplication.zip", "Web\\CSharp", new string[] { "Default.aspx" }, currentSolution);
		}

        public static Project FindProjectByName(string projectName, Solution currentSolution)
        {
            if (!currentSolution.IsOpen || projectName == string.Empty) return null;
            foreach (Project p in currentSolution.Projects)
            {
                if (p.Name == projectName)
                {
                    return p;
                }
            }
            return null;
        }

		public static Project FindProjectByDirectory(string projectPath, Solution currentSolution)
		{
			//string path = FileUtils.GetPathFromFilename(projectPath);
			if (!currentSolution.IsOpen || projectPath == string.Empty) return null;
			foreach (Project p in currentSolution.Projects)
			{
				if (FileUtils.GetPathFromFilename(p.FileName) == projectPath)
				{
					return p;
				}
			}
			return null;
		}

		public static ProjectItem FindProjectFolder(Project proj, string folderName)
		{
			return FindProjectItem(proj, folderName, EnvDTE.Constants.vsProjectItemKindPhysicalFolder);
		}

		public static ProjectItem FindProjectItem(Project proj, string itemName)
		{
			return FindProjectItem(proj, itemName, string.Empty);
		}

		public static ProjectItem FindProjectItem(Project proj, string itemName, string itemKind)
		{
			return FindProjectItem(proj, itemName, itemKind, true);
		}

		public static ProjectItem FindProjectItem(Project proj, string itemName, string itemKind, bool caseSensative)
		{
			if (itemName.Length > 0 && proj != null)
			{
				itemName = itemName.Trim();
				foreach (ProjectItem item in proj.ProjectItems)
				{
					string currentItemName = item.Name;
					string currentItemKind = item.Kind;
					if (itemName == currentItemName || (!caseSensative && itemName.ToLower() == currentItemName.ToLower()))
					{
						if (itemKind == string.Empty || itemKind == currentItemKind)
						{
							return item;
						}
					}
				}
			}
			return null;
		}


    }
}
