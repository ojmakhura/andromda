This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.

If you type "maven" in this directory (in the context of the CVS module andromda-all), the
whole AndroMDA distribution will be built. 

Prerequisite:
* install the latest version of Maven and add it to your $PATH
* create build.properties and add:
  'maven.repo.remote=http://www.ibibilio.org/maven,http://team.andromda.org/maven' 
   and place in your %USERPROFILE% (windows), or $HOME (linux/unix) directory.

Good luck!
Matthias Bohlen
