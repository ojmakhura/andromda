This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.

1) Install the latest version of Maven 2.0.x (http://maven.apache.org) and add it to your $PATH.
2) Set the MAVEN_OPTS: "export MAVEN_OPTS=-XX:MaxPermSize=128m\ -Xmx512m"
3) cd to andromda-all, type "mvn -N antrun:run", this will install the required archive maven plugins
   (NOTE: this step only needs to be performed the first time you build with a fresh repository).
4) Next type "mvn", this will proceed to build the entire distribution, which can be found in the
   directory: distribution/target


Good luck!

The AndroMDA Team
