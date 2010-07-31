
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
    public class SettingsManager
    {

        #region Enumerations

        public enum SettingsMode { UserSettings, SolutionSettings, ProjectSettings }

        #endregion

        #region Member variables

        private DTE m_applicationObject = null;
        private Globals m_globals = null;
        private SettingsMode m_mode = SettingsMode.UserSettings;

        #endregion

        #region Properties

        public string this[string key]
        {
            get { return GetSetting(key); }
            set { SetSetting(key, value); }
        }

        #endregion

        public SettingsManager(DTE applicationObject)
        {
            Init(applicationObject, SettingsMode.UserSettings, null, null);
        }

        public SettingsManager(DTE applicationObject, SettingsMode mode, Solution sol)
        {
            Init(applicationObject, mode, sol, null);
        }

        public SettingsManager(DTE applicationObject, SettingsMode mode, Solution sol, Project p)
        {
            Init(applicationObject, mode, sol, p);
        }

        private void Init(DTE applicationObject, SettingsMode mode, Solution sol, Project p)
        {
            m_applicationObject = applicationObject;
            m_mode = mode;
            switch (mode)
            {
                case SettingsMode.UserSettings:
                    m_globals = m_applicationObject.Globals;
                    break;
                case SettingsMode.SolutionSettings:
                    m_globals = sol.Globals;
                    break;
                case SettingsMode.ProjectSettings:
                    m_globals = p.Globals;
                    break;
            }
        }

        public bool VariableExists(string key)
        {
            return m_globals.get_VariableExists(key);
        }

        public string GetSetting(string key)
        {
            if (m_globals.get_VariableExists(key))
            {
                return (string)m_globals[key];
            }
            return string.Empty;
        }

        public void SetSetting(string key, string value)
        {
//            if (!m_globals.get_VariableExists(key))
            {
            m_globals[key] = value;
            m_globals.set_VariablePersists(key, true);
            }
//            else
//            {
//                m_globals[key] = value;
//            }
        }

        public bool GetBool(string key)
        {
            return bool.Parse(GetSetting(key));
        }

        public bool GetBool(string key, bool defaultValue)
        {
            string val = GetSetting(key);
            if (val == string.Empty)
            {
                return defaultValue;
            }
            return bool.Parse(val);
        }

        public void SetBool(string key, bool value)
        {
            SetSetting(key, value.ToString());
        }

        public DateTime? GetDateTime(string key)
        {
            string val = GetSetting(key);
            if (val == string.Empty)
            {
                return null;
            }
            return DateTime.Parse(val);
        }

        public void SetDateTime(string key, DateTime? value)
        {
            SetSetting(key, value.ToString());
        }

        public void SetInt(string key, int? value)
        {
            SetSetting(key, value.ToString());
        }

        public int? GetInt(string key)
        {
            string val = GetSetting(key);
            if (val == string.Empty)
            {
                return null;
            }
            return int.Parse(val);
        }

    }
}
