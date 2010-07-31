
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.IO;
using System.Collections;
using System.Text;
using System.Xml;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class ConfigFile
    {

        #region Member Variables

        private Hashtable m_configHash = null;

        private string m_fileName = string.Empty;

        private char m_keyValueDelimiter = '=';
        private char m_commentDelimiter = '#';

        #endregion

        #region Properties

        public string this[string key]
        {
            get
            {
                return GetValueResolved(key);
            }
            set
            {
                m_configHash[key] = value;
            }
        }

        public ICollection Keys
        {
            get
            {
                return m_configHash.Keys;
            }
        }

        public ICollection Values
        {
            get
            {
                return m_configHash.Values;
            }
        }

        public int Count
        {
            get
            {
                return m_configHash.Count;
            }
        }

        #endregion

        public ConfigFile()
        {
            Init();
        }

        public ConfigFile(string filename)
        {
            Init();
            LoadFromFile(filename);
        }

        private void Init()
        {
            m_configHash = new Hashtable();
        }

        public void Clear()
        {
            m_configHash.Clear();
            m_fileName = string.Empty;
        }

        public void Add(string key, string value)
        {
            m_configHash.Add(key, value);
        }

        public void Remove(string key)
        {
            m_configHash.Remove(key);
        }

        public string GetValue(string key)
        {
            if (m_configHash[key] == null)
            {
                return string.Empty;
            }
            else
            {
                return (string)m_configHash[key];
            }
        }

        public string GetPath(string key)
        {
            string path = GetValueResolved(key);
            path = path.Replace('/', '\\');
            path = path.Replace("jar:", string.Empty);
            path = path.Replace("file:", string.Empty);
            int position = path.LastIndexOf('!');
            if (position != -1)
            {
                path = path.Substring(0, position);
            }
            return path;
        }

        public void Contains(string key)
        {
            m_configHash.Contains(key);
        }

        public void LoadFromFile(string filename)
        {
            LoadFromFile(filename, true);
        }

        public void LoadFromFile(string filename, bool clearConfig)
        {
            try
            {
                if (clearConfig)
                {
                    Clear();
                }
                if (System.IO.File.Exists(filename))
                {
                    using (StreamReader sr = new StreamReader(filename))
                    {
                        while (!sr.EndOfStream)
                        {
                            string line = sr.ReadLine().Trim();
                            if (line != string.Empty && line[0] != m_commentDelimiter && line.Contains("="))
                            {
                                string[] pair = line.Split(new char[] { m_keyValueDelimiter }, 2);
                                m_configHash[pair[0]] = pair[1];
                            }
                        }
                        sr.Close();
                    }
                }
                else
                {
                    // Create an empty file
                    StreamWriter sw = new StreamWriter(filename);
                    sw.Close();
                }
                m_fileName = filename;
            }
            catch (Exception e)
            {
                throw new Exception("Error while trying to load " + filename + ": " + e.Message, e);
            }
        }

        public void LoadFromXML(string filename)
        {
            LoadFromXML(filename, string.Empty, true);
        }

        public void LoadFromXML(string filename, string namePrefixToAdd)
        {
            LoadFromXML(filename, namePrefixToAdd, true);
        }

        public void LoadFromXML(string filename, string namePrefixToAdd, bool clearConfig)
        {
            try
            {
                if (clearConfig)
                {
                    Clear();
                }
                using (XmlTextReader r = new XmlTextReader(filename))
                {
                    while (r.Name.ToLower() != "properties" && !r.EOF) { r.Read(); }
                    if (!r.EOF)
                    {
                        r.Read();
                        while (r.NodeType != XmlNodeType.EndElement && !r.EOF)
                        {
                            if (r.NodeType == XmlNodeType.Element)
                            {
                                string value = string.Empty;
                                string name = namePrefixToAdd + r.Name;
                                while (r.NodeType != XmlNodeType.EndElement)
                                {
                                    r.Read();
                                    if (r.NodeType == XmlNodeType.Text) value += r.Value;
                                }
                                m_configHash[name] = value;
                            }
                            r.Read();
                        }
                    }
                    r.Close();
                }
                m_fileName = filename;
            }
            catch (Exception e)
            {
                throw new Exception("Error while trying to load " + filename + ": " + e.Message, e);
            }
        }

        public void Save()
        {
            Save(m_fileName);
        }

        public void Save(string filename)
        {
            StreamWriter sw = new StreamWriter(filename);
            foreach (string key in this.Keys)
            {
                sw.WriteLine(key + m_keyValueDelimiter + this[key]);
            }
            sw.Close();
        }

        public void ResolveVariables()
        {
            Hashtable m_values = (Hashtable)m_configHash.Clone();
            bool changesApplied = false;
            foreach (string key in this.Keys)
            {
                foreach (string variableKey in this.Keys)
                {
                    string value = this[key];
                    if (value.Contains("${" + variableKey + "}"))
                    {
                        value = value.Replace("${" + variableKey + "}", this[variableKey]);
                        m_values[key] = value;
                        changesApplied = true;
                    }
                }
            }
            if (changesApplied)
            {
                m_configHash = m_values;
            }
        }

        public string GetValueResolved(string key)
        {
            string resolvedValue = (string)m_configHash[key];
            if (resolvedValue == null)
            {
                return string.Empty;
            }
            foreach (string variableKey in this.Keys)
            {
                if (resolvedValue.Contains("${" + variableKey + "}"))
                {
                    string replacementValue = (string)m_configHash[variableKey];
                    if (replacementValue.Contains("${"))
                    {
                        replacementValue = GetValueResolved(variableKey);
                    }
                    resolvedValue = resolvedValue.Replace("${" + variableKey + "}", replacementValue);
                }
            }
            return resolvedValue;
        }
    }
}
