
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;
using System.Management;

using Microsoft.VisualStudio.VCCodeModel;

using EnvDTE;
using EnvDTE80;
using System.Collections;

#endregion

namespace AndroMDA.VS80AddIn
{

    public sealed class CodeElement2Comparator : System.Collections.IComparer
    {
        public int Compare(object first, object second)
        {
            CodeElement2 f = (CodeElement2)first;
            CodeElement2 s = (CodeElement2)second;
            return f.Name.CompareTo(s.Name);
        }
    }
  
	public class CodeModelUtils
	{


        public static ArrayList GetPropertiesFromType(CodeType type)
        {
            return GetMembersFromType(type, new vsCMElement[] { vsCMElement.vsCMElementProperty });
        }

        public static ArrayList GetPropertiesFromType(CodeType type, bool sort)
        {
            return GetMembersFromType(type, new vsCMElement[] { vsCMElement.vsCMElementProperty }, sort);
        }

        private static ArrayList GetMembersFromType(CodeType type, vsCMElement[] searchScopes)
        {
            return GetMembersFromType(type, searchScopes, true);
        }

        private static ArrayList GetMembersFromType(CodeType type, vsCMElement[] searchScopes, bool sort)
        {
            ArrayList results = GetMembersFromType(type, searchScopes, new ArrayList());
            if (sort)
            {
                results.Sort(new CodeElement2Comparator());
            }
            return results;
        }

        private static ArrayList GetMembersFromType(CodeType type, vsCMElement[] searchScopes, ArrayList properties)
        {
            foreach (CodeElement2 prop in type.Members)
            {
                foreach(vsCMElement elemType in searchScopes)
                {
                    if (prop.Kind == elemType)
                    {
                        properties.Add(prop);
                        break;
                    }
                }
            }
            for (int i = 1; i <= type.Bases.Count; i++)
            {
                CodeType baseclass = type.Bases.Item(i) as CodeType;
                GetMembersFromType(baseclass, searchScopes, properties);
            }
            return properties;
        }


		public static CodeFunction2 GetCurrentMethod(DTE dte)
		{			
			vsCMElement[] searchScopes = { 
							 vsCMElement.vsCMElementFunction,
							 };
			return GetCodeElement(dte, searchScopes) as CodeFunction2;
		}

		public static CodeElement GetCodeElement(DTE dte)
		{
			vsCMElement[] searchScopes = { 
							 vsCMElement.vsCMElementFunction,
                             vsCMElement.vsCMElementProperty,
                             vsCMElement.vsCMElementVariable,
                             vsCMElement.vsCMElementEvent,
                             vsCMElement.vsCMElementClass,
                             vsCMElement.vsCMElementInterface,
                             vsCMElement.vsCMElementStruct,
                             vsCMElement.vsCMElementEnum  
							 };
			return GetCodeElement(dte, searchScopes);
		}

		public static CodeElement GetCodeElement(DTE dte, vsCMElement[] searchScopes)
		{
			if (dte.ActiveDocument == null) return null;
			if (dte.ActiveDocument.ProjectItem == null) return null;
			if (dte.ActiveDocument.ProjectItem.FileCodeModel == null) return null;
			TextSelection selection = (TextSelection)dte.ActiveWindow.Selection;
			if (selection == null || selection.ActivePoint == null) return null;
			EditPoint selPoint = selection.ActivePoint.CreateEditPoint();
			CodeLanguage currentLang = CodeLanguage.CSharp;
			selPoint.StartOfLine();
			while (true)
			{
				string BlockText = selPoint.GetText(selPoint.LineLength).Trim();
				// *** Skip over any XML Doc comments and Attributes
				if (currentLang == CodeLanguage.CSharp && BlockText.StartsWith("/// ") ||
					  currentLang == CodeLanguage.CSharp && BlockText.StartsWith("[") ||
					  currentLang == CodeLanguage.VB && BlockText.StartsWith("''' ") ||
					  currentLang == CodeLanguage.VB && BlockText.StartsWith("<"))
				{
					selPoint.LineDown(1);
					selPoint.StartOfLine();
				}
				else
					break;
			}

			// *** Make sure the cursor is placed inside of the definition always
			// *** Especially required for single line methods/fields/events etc.
			selPoint.EndOfLine();
			selPoint.CharLeft(1);  // Force into the text

			string xBlockText = selPoint.GetText(selPoint.LineLength).Trim();

			// get the element under the cursor
			CodeElement element = null;
			FileCodeModel2 CodeModel = dte.ActiveDocument.ProjectItem.FileCodeModel as FileCodeModel2;

			// *** Supported scopes - set up here in the right parsing order
			// *** from lowest level to highest level
			// *** NOTE: Must be adjusted to match any CodeElements supported

			foreach (vsCMElement scope in searchScopes)
			{
				try
				{
					element = CodeModel.CodeElementFromPoint(selPoint, scope);
					if (element != null)
						break; // if no exception - break
				}
				catch { ; }
			}
			if (element == null)
				return null;
			return element;
		}


        public static bool IsEntityClass(CodeType type)
        {
            vsCMElement[] searchScopes = { vsCMElement.vsCMElementClass };

            // If the type has a Factory class, then it is an entity
            ArrayList classes = GetMembersFromType(type, searchScopes, false);
            if (classes.Count == 0) return false;
            foreach (CodeElement2 elem in classes)
            {
                if (elem.Name == "Factory") return true;
            }
            return false;
        }


	}
}
