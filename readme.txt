This directory is useful only in the context of the SVN module andromda.
If you want to build the entire andromda project then issue a 'svn checkout https://andromda.svn.sourceforge.net/svnroot/andromda/trunk andromda' and it will
contain this directory's contents.

1) Install the latest version of Maven 2.2.1 (http://maven.apache.org) and add it to your $PATH.
2) Set the MAVEN_OPTS: "export MAVEN_OPTS=-XX:MaxPermSize=256m\ -Xmx512m"
3) cd to andromda, type "mvn -P andromda-prepare", this will install the required model-archive maven plugin
   (NOTE: this step only needs to be performed the first time you build with a fresh repository).
4) Next type "mvn", this will proceed to build the entire distribution, which can be found in the
   directory: andromda-distribution/target


Good luck!

The AndroMDA Team
