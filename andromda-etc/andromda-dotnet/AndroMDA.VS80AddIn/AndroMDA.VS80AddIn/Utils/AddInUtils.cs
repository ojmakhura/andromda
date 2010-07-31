
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using EnvDTE80;
using EnvDTE;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class AddInUtils
    {

        public static void ShowError(string errorMessage)
        {
            System.Windows.Forms.MessageBox.Show(errorMessage, "Android/VS Error", System.Windows.Forms.MessageBoxButtons.OK, System.Windows.Forms.MessageBoxIcon.Error);
        }

		public static void ShowWarning(string errorMessage)
		{
            System.Windows.Forms.MessageBox.Show(errorMessage, "Android/VS Warning", System.Windows.Forms.MessageBoxButtons.OK, System.Windows.Forms.MessageBoxIcon.Warning);
		}

		public static void InsertListViewItem(ListView listView, string name, string value, string groupName)
		{
			ListViewItem lvi = new ListViewItem();
			lvi.Group = null;
			foreach (ListViewGroup group in listView.Groups)
			{
				if (group.Header == groupName)
				{
					lvi.Group = group;
				}
			}
			if (lvi.Group == null)
			{
				lvi.Group = new ListViewGroup(groupName);
				listView.Groups.Add(lvi.Group);
			}
			lvi.Text = name;
			lvi.SubItems.Add(new ListViewItem.ListViewSubItem(lvi, value));
			listView.Items.Add(lvi);
		}

        public static void InsertCodeInMethod(CodeFunction2 currentMethod, string codeToInsert)
        {
            TextPoint startPoint = currentMethod.GetStartPoint(vsCMPart.vsCMPartBody);
            // Batch insert the new code so it can be undone in 1 call to undo
            EditPoint pnt = startPoint.CreateEditPoint();
            pnt.Insert(codeToInsert);
            // Format the code (indent it properly)
            pnt.SmartFormat(startPoint);
        }

    }
}
