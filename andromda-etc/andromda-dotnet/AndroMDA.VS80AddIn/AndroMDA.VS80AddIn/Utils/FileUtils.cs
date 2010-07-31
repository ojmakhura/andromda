
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;
using System.Collections;
using System.IO;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class FileUtils
    {

        public static string GetPathFromFilename(string pathAndFilename)
        {
            string path = pathAndFilename;
            if (path.Length > 0 && path.LastIndexOf('\\') != -1)
            {
                path = path.Substring(0, path.LastIndexOf('\\'));
            }
            return path;
        }

        public static string GetFilename(string pathAndFilename)
        {
            string path = pathAndFilename;
            if (path.Length > 0 && path.LastIndexOf('\\') != -1)
            {
                path = path.Substring(path.LastIndexOf('\\') + 1);
            }
            return path;
        }

        public static bool CompareDirectories(string dir1, string dir2)
        {
            try
            {
                if (dir1 == dir2) { return true; }
                if (dir1 != string.Empty && dir2 == string.Empty) { return false; }
                if (dir1 == string.Empty && dir2 != string.Empty) { return false; }

                // Save the current directory
                string currentDirectory = System.IO.Directory.GetCurrentDirectory();
                System.IO.Directory.SetCurrentDirectory(dir1);
                dir1 = System.IO.Directory.GetCurrentDirectory();
                System.IO.Directory.SetCurrentDirectory(dir2);
                dir2 = System.IO.Directory.GetCurrentDirectory();
                
                // Go back to the previous saved current directory
                System.IO.Directory.SetCurrentDirectory(currentDirectory);
                
                return dir1.Equals(dir2);
            }
            catch
            {
                return false;
            }
        }

        public static string CleanDirectory(string dir)
        {
            string newdir = dir;
            if (newdir.Length > 0)
            {
                // Save the current directory            
                string currentDirectory = System.IO.Directory.GetCurrentDirectory();
                try
                {
                    System.IO.Directory.SetCurrentDirectory(dir);
                    newdir = System.IO.Directory.GetCurrentDirectory();
                    // Go back to the previous saved current directory
                }
                catch
                {
                }
                finally
                {
                    System.IO.Directory.SetCurrentDirectory(currentDirectory);
                }
            }
            return newdir;
        }

        public static string GetUserHome()
        {
            return CleanDirectory(System.Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + "\\..");
        }

        public static bool IsInSearchPath(string executable)
        {
            string[] path = GetPath();
            foreach (string directory in path)
            {
                if (System.IO.File.Exists(directory + "\\" + executable))
                    return true;
            }
            return false;
        }

        public static string[] GetPath()
        {
            IDictionary environmentVariables = Environment.GetEnvironmentVariables();
            foreach (DictionaryEntry de in environmentVariables)
            {
                if (de.Key.ToString().Equals("Path", StringComparison.OrdinalIgnoreCase))
                {
                    return de.Value.ToString().Trim().Split(';');
                }
            }
            return new string[0];
        }

        public static void WriteFile(string filePath, string fileContent)
        {
            StreamWriter sw = new StreamWriter(filePath, false);
            sw.Write(fileContent);
            sw.Close();
        }


    }
}
