This directory contents are useful only in the context of the CVS module andromda-all. 
If you want to build the entire andromda project then 'cvs co andromda-all' and it will
contain this directory's contents.

If you type "ant" in this directory, (in the context of the CVS module andromda-all) the 
whole AndroMDA distribution will be built. The resulting zip files will appear inside a 
subdirectory "dist".  The unzipped andromda distribution will appear in the 'dist/predist'
directory.

Prerequisite:
* copy the build.properties.sample file in this directory to the file build.properties 
* and then edit it to reflect your particular environment.

Good luck!
Matthias Bohlen
