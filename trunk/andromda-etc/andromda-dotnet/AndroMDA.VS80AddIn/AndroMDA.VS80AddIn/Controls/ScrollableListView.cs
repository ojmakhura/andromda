using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows.Forms;

namespace AndroMDA.VS80AddIn.Dialogs
{
    public class ScrollableListView : System.Windows.Forms.ListView
    {
        #region Own EventArgs

        /// <summary>
        /// Class that contains the data for
        /// own event args. derives from System.EventArgs.
        /// no args are needed in this example but you can pass every args you want.        
        /// </summary>

        public class ListViewScrollEventArgs : EventArgs
        {
            /// <summary>
            /// Constructor
            /// </summary>            
            public ListViewScrollEventArgs()
            {
            }
        }

        #endregion

        /// <summary>
        /// Delegate the event handler.
        /// </summary>
        public delegate void MyEventHandler(object sender, ListViewScrollEventArgs e);

        /// <summary>
        /// The Event
        /// </summary>
        public event MyEventHandler OnScroll;

        /// <summary>
        /// The protected OnVScroll method raises the event by invoking
        /// the delegates. The sender is always this, the current instance
        /// of the class.
        /// </summary>        
        protected virtual void OnVScroll(ListViewScrollEventArgs e)
        {
            if (OnScroll != null)
            {
                OnScroll(this, e);
            }
        }

        private System.ComponentModel.Container components = null;

        public ScrollableListView()
        {
            InitializeComponent();
            // Turn on double buffering to reduce flicker on resize
            this.DoubleBuffered = true;
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (components != null)
                    components.Dispose();
            }
            base.Dispose(disposing);
        }

        protected override void WndProc(ref Message m)
        {
            base.WndProc(ref m);
            if (m.Msg == 0x115)//fire event on wm_vscroll
            {
                ListViewScrollEventArgs e = new ListViewScrollEventArgs();
                this.OnVScroll(e);
            }
        }

        #region Component Designer generated code

        private void InitializeComponent()
        {
            components = new System.ComponentModel.Container();
        }

        #endregion
    }
}

