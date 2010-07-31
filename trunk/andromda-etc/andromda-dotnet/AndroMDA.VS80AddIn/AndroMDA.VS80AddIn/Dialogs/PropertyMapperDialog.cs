using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using EnvDTE80;
using EnvDTE;
using System.Collections;

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class PropertyMapperDialog : Form
    {

        CodeFunction2 m_currentMethod = null;
        AddInSettings m_addInSettings = null;
        private string m_codeToInsert;

        public string GeneratedCode
        {
            get { return m_codeToInsert; }
        }

        public PropertyMapperDialog(CodeFunction2 currentMethod, AddInSettings settings)
        {
            InitializeComponent();
            m_currentMethod = currentMethod;
            this.KeyPreview = true;
            m_codeToInsert = string.Empty;
            m_addInSettings = settings;
        }

        private void btnOk_Click(object sender, EventArgs e)
        {
            GenerateCode();
            this.DialogResult = DialogResult.OK;
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
        }

        private void PropertyMapperDialog_Shown(object sender, EventArgs e)
        {
            
            CodeParameter2 fromParameter = m_currentMethod.Parameters.Item(1) as CodeParameter2;
            string returnType = m_currentMethod.Type.AsFullName;
            string fromType = fromParameter.Type.AsFullName;
            lblFromClass.Text = fromType;
            lblToClass.Text = returnType;
            try
            {
                InitListView(lstFromProperties, CodeModelUtils.GetPropertiesFromType(fromParameter.Type.CodeType));
            }
            catch (NotImplementedException)
            {
                AddInUtils.ShowError("The method parameter '" + fromParameter.Type.AsString + "' could not be found in the code model.  Please be sure the type exists and the project has been successfully built.");
                this.DialogResult = DialogResult.Cancel;
            }
            try
            {
                InitListView(lstToProperties, CodeModelUtils.GetPropertiesFromType(m_currentMethod.Type.CodeType));
            }
            catch (NotImplementedException)
            {
                AddInUtils.ShowError("The method return type '" + m_currentMethod.Type.AsString + "' could not be found in the code model.  Please be sure the type exists and the project has been successfully built.");
                this.DialogResult = DialogResult.Cancel;
            }

            //InitTreeView(treeView1, CodeModelUtils.GetPropertiesFromType(fromParameter.Type.CodeType));
            RefreshView();
            if (m_addInSettings.PropertyMapperAutoMapOnOpen)
            {
                AutoMap();
            }
        }

        private void InitListView(ListView lstView, ArrayList properties)
        {
            lstView.BeginUpdate();
            ArrayList list = new ArrayList();

            foreach (CodeProperty prop in properties)
            {
                ListViewItem lvi = CreateListViewItem(prop);
                list.Insert(0, lvi);
            }
            ListViewItem[] items = new ListViewItem[list.Count];
            list.CopyTo(items);
            lstView.Items.AddRange(items);
            lstView.EndUpdate();
        }

        private ListViewItem CreateListViewItem(CodeProperty prop)
        {
            ListViewItem lvi = new ListViewItem();
            lvi.Name = prop.Name;
            lvi.Text = prop.Name;
            lvi.ToolTipText = prop.Name;
            lvi.IndentCount = 0;
            ListViewItem.ListViewSubItem subItem = new ListViewItem.ListViewSubItem(lvi, prop.Type.AsString);
            subItem.Tag = prop;
            lvi.SubItems.Add(subItem);
            lvi.ImageIndex = 2;
            if (!IsItemExpandable(lvi))
                lvi.ImageIndex = 0;
            return lvi;
        }

        private void InitTreeView(TreeView treeView, ArrayList properties)
        {
            treeView.BeginUpdate();
            foreach (CodeProperty prop in properties)
            {
                TreeNode node = new TreeNode();
                node.Text = prop.Name;
                node.ImageIndex = 0;
                treeView.Nodes.Add(node);
            }
            treeView.EndUpdate();
        }

        private void RefreshView()
        {
            lstToProperties.BeginUpdate();
            lstFromProperties.BeginUpdate();
            foreach (ListViewItem i in lstFromProperties.Items)
            {
                if (i.Font.Bold == true)
                {
                    i.Font = new Font(this.Font.FontFamily, this.Font.Size, FontStyle.Regular);
                }            
            }

            foreach (ListViewItem fromItem in lstFromProperties.Items)
            {
                if (fromItem.Tag == null)
                {
                    if (fromItem.ImageIndex == 1) { fromItem.ImageIndex = 0; }
                }
                else
                {
                    if (fromItem.ImageIndex == 0) { fromItem.ImageIndex = 1; }
                }
            }

            foreach (ListViewItem toItem in lstToProperties.Items)
            {
                if (toItem.Tag == null)
                {
                    if (toItem.ImageIndex == 1) { toItem.ImageIndex = 0; }
                }
                else
                {
                    if (toItem.ImageIndex == 0) { toItem.ImageIndex = 1; }
                }
            }
            
            if (lstToProperties.SelectedItems.Count > 0)
            {
                ListViewItem item = lstToProperties.SelectedItems[0];
                if (item.Tag == null)
                {
                    SetMappedIndicator(false);
                }
                else
                {
                    SetMappedIndicator(true);
                    ListViewItem r = (ListViewItem)item.Tag;
                    r.Selected = true;
                    r.Font = new Font(this.Font.FontFamily, this.Font.Size, FontStyle.Bold);
                }
                lblMapped.Visible = true;
            }
            else
            {
                lblMapped.Visible = false;
            }
            pnlConnections.Invalidate();
            lstFromProperties.EndUpdate();
            lstToProperties.EndUpdate();
        }

        private void MapSelectedItems()
        {
            if (lstToProperties.SelectedItems.Count > 0 && lstFromProperties.SelectedItems.Count > 0)
            {
                ListViewItem fromItem = lstFromProperties.SelectedItems[0];
                ListViewItem toItem = lstToProperties.SelectedItems[0];

                if (fromItem.Tag == toItem && toItem.Tag == fromItem)
                {
                    // The selected items are mapped to each other, we are unmapping
                    fromItem.Tag = null;
                    toItem.Tag = null;
                }
                else
                {
                    // If the from item is already mapped
                    if (fromItem.Tag != null)
                    {
                        // Remove the back reference
                        ((ListViewItem)fromItem.Tag).Tag = null;
                        // Unmap it
                        fromItem.Tag = null;
                    }                    
                    if (toItem.Tag != null)
                    {
                        ((ListViewItem)toItem.Tag).Tag = null;
                        toItem.Tag = null;
                    }
                    // Map the items
                    fromItem.Tag = toItem;
                    toItem.Tag = fromItem;
                }
                RefreshView();
            }
        }

        private void SetMappedIndicator(bool isMapped)
        {
            if (isMapped)
            {
                lblMapped.Text = "     Mapped";
                lblMapped.ImageIndex = 3;
                lblMapped.Padding = new Padding(12, 4, 4, 4);
            }
            else
            {
                lblMapped.Text = "     UnMapped";
                lblMapped.ImageIndex = 4;
                lblMapped.Padding = new Padding(8, 4, 4, 4);
            }
        }

        private void btnAutoMap_Click(object sender, EventArgs e)
        {
            AutoMap();
        }

        private void pnlConnections_Paint(object sender, PaintEventArgs e)
        {
            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
            ListViewItem selectedItem = null;
            if (lstToProperties.SelectedItems.Count > 0)
            {
                selectedItem = lstToProperties.SelectedItems[0];
            }
            bool drawSelectedLine = false;
            Point selA = new Point();
            Point selB = new Point();            
            foreach (ListViewItem toItem in lstToProperties.Items)
            {
                if (toItem.Tag != null)
                {
                    ListViewItem fromItem = (ListViewItem)toItem.Tag;
                    Rectangle fromRect = fromItem.Bounds;
                    Rectangle toRect = toItem.Bounds;
                    int fromYOffset = (fromRect.Height / 2) + 2;
                    int toYOffset = (toRect.Height / 2) + 2;
                    Point a = new Point(pnlConnections.Width, fromRect.Location.Y + fromYOffset);
                    Point b = new Point(0, toRect.Location.Y + toYOffset);

                    if (e.ClipRectangle.Contains(a.X-1, a.Y-1) && e.ClipRectangle.Contains(b))
                    {
                        if (selectedItem == toItem)
                        {
                            selA = a;
                            selB = b;
                            drawSelectedLine = true;
                        }
                        else
                        {

                            //e.Graphics.DrawLine(new Pen(Brushes.LightYellow, 2), a, b);
                            Pen p = new Pen(Brushes.DarkGray, 2);
                            p.EndCap = System.Drawing.Drawing2D.LineCap.Custom;
                            p.CustomEndCap = new System.Drawing.Drawing2D.AdjustableArrowCap(4, 4, true);
                            e.Graphics.DrawLine(p, a, b);
                        }
                    }
                }
            }
            if (drawSelectedLine)
            {
                //e.Graphics.DrawLine(new Pen(Brushes.Gray, 2), selA, selB);
                Pen p = new Pen(Brushes.Black, 1);
                p.EndCap = System.Drawing.Drawing2D.LineCap.Custom;
                p.CustomEndCap = new System.Drawing.Drawing2D.AdjustableArrowCap(4, 4, true);
                e.Graphics.DrawLine(p, selA, selB);

            }
        }


        private void lstFromProperties_OnScroll(object sender, ScrollableListView.ListViewScrollEventArgs e)
        {
            RefreshView();
        }

        private void contextMenuStrip1_Opening(object sender, CancelEventArgs e)
        {
            ContextMenuStrip c = (ContextMenuStrip)sender;
            m_whichListView = (ListView)c.SourceControl;
            expandToolStripMenuItem.Enabled = IsItemExpandable(m_whichListView);
            mapToolStripMenuItem.Enabled = (lstToProperties.SelectedItems.Count > 0) && (lstFromProperties.SelectedItems.Count > 0);
            mapToolStripMenuItem.Visible = m_whichListView == lstFromProperties;
            if (mapToolStripMenuItem.Enabled && lstToProperties.SelectedItems[0].Tag != null)
            {
                mapToolStripMenuItem.Text = "&Unmap";
                mapToolStripMenuItem.Image = AndroMDA.VS80AddIn.Resource1.link_break;
            }
            else
            {
                mapToolStripMenuItem.Text = "&Map";
                mapToolStripMenuItem.Image = AndroMDA.VS80AddIn.Resource1.link_add;
            }
        }

        private ListView m_whichListView = null;

        private void AutoMap()
        {
            bool mapped = false;
            foreach (ListViewItem fromItem in lstFromProperties.Items)
            {
                foreach (ListViewItem toItem in lstToProperties.Items)
                {
                    string fromName = fromItem.Name;
                    string toName = toItem.Name;
                    string fromNameNoDots = fromName.Replace(".", string.Empty);
                    string toNameNoDots = toName.Replace(".", string.Empty);
                    if (fromName == toName || fromName == toNameNoDots || fromNameNoDots == toNameNoDots)
                    {
                        string fromType = fromItem.SubItems[1].Text.Replace("?", string.Empty);
                        string toType = toItem.SubItems[1].Text.Replace("?", string.Empty);
                        if (fromType == toType)
                        {
                            // If the from item is already mapped
                            if (fromItem.Tag != null)
                            {
                                // Remove the back reference
                                ((ListViewItem)fromItem.Tag).Tag = null;
                                // Unmap it
                                fromItem.Tag = null;
                            }
                            if (toItem.Tag != null)
                            {
                                ((ListViewItem)toItem.Tag).Tag = null;
                                toItem.Tag = null;
                            }
                            fromItem.Tag = toItem;
                            toItem.Tag = fromItem;
                            mapped = true;
                        }
                    }
                }
            }
            if (mapped)
            {
                RefreshView();
            }
        }

        private void ExpandSelectedItem(ListView lst)
        {
            if (lst.SelectedItems.Count == 0) return;
            ListViewItem item = lst.SelectedItems[0];
            CodeProperty prop = (CodeProperty)item.SubItems[1].Tag;
            ArrayList properties = CodeModelUtils.GetPropertiesFromType(prop.Type.CodeType);
            int index = item.Index + 1;
            foreach (CodeProperty p in properties)
            {
                ListViewItem lvi = CreateListViewItem(p);
                lvi.Name = item.Name + "." + lvi.Text;
                lvi.ToolTipText = lvi.Name;
                lvi.IndentCount = item.IndentCount + 1;
                lst.Items.Insert(index, lvi);
                index = lst.Items.IndexOf(lvi);
            }
            item.ImageIndex = 0;
            RefreshView();
        }

        private bool IsItemExpandable(ListView lst)
        {
            if (lst.SelectedItems.Count == 0) return false;
            return IsItemExpandable(lst.SelectedItems[0]);
            
        }

        private bool IsItemExpandable(ListViewItem item)
        {            
            int index = item.Index;
            if (item.ImageIndex != 2) return false;
            CodeProperty prop = (CodeProperty)item.SubItems[1].Tag;
            if (prop.Type == null) return false;
            if (prop.Type.TypeKind != vsCMTypeRef.vsCMTypeRefCodeType) return false;
            if (prop.Type.CodeType == null) return false;
            if (prop.Type.CodeType.Namespace.FullName == "System") return false;
            //if (prop.Type.CodeType.Namespace.FullName == "Iesi.Collections") return false;
            if (prop.Type.CodeType.Kind == vsCMElement.vsCMElementEnum) return false;
            return true;
        }

        private void lstFromProperties_DoubleClick(object sender, EventArgs e)
        {
            ListView lst = (ListView)sender;
            if (IsItemExpandable(lst))
            {
                m_whichListView = lst;
                expandToolStripMenuItem_Click(sender, e);
            }
        }
        
        private void expandToolStripMenuItem_Click(object sender, EventArgs e)
        {
            ExpandSelectedItem(m_whichListView);
        }

        private void mapToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MapSelectedItems();
        }

        private void PropertyMapperDialog_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyValue == 27)
            {
                this.DialogResult = DialogResult.Cancel;
            }
        }

        private void expandAllToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }


        private void lstFromProperties_SelectedIndexChanged(object sender, EventArgs e)
        {
            //RefreshView();
        }

        private void lstToProperties_SelectedIndexChanged(object sender, EventArgs e)
        {
            RefreshView();
        }

        private void lstToProperties_ItemActivate(object sender, EventArgs e)
        {
        }

        private void lstFromProperties_ItemActivate(object sender, EventArgs e)
        {
            MapSelectedItems();
        }

        private void GenerateCode()
        {
            ConversionCodeGenerator codeGenerator = new ConversionCodeGenerator();
            for(int i = lstToProperties.Items.Count - 1; i>= 0; i--)
            {
                ListViewItem toItem = lstToProperties.Items[i];
                ListViewItem fromItem = (ListViewItem)toItem.Tag;
                if (fromItem != null)
                {
                    codeGenerator.AddProperty(toItem.Name, toItem.SubItems[1].Text, fromItem.Name, fromItem.SubItems[1].Text);
                }
                else
                {
                    codeGenerator.AddProperty(toItem.Name, toItem.SubItems[1].Text);
                }
            }
            m_codeToInsert = codeGenerator.GenerateCode(m_currentMethod);
        }

    }
}