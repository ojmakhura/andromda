This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.

Instructions:
1) Install the latest version of Maven 2.0.x (http://maven.apache.org) and add it to your $PATH.
2) cd to andromda-all, type "mvn -N antrun:run", this will install the required archive maven plugins
3) Next type "mvn", this will proceed to build the entire distribution, which can be found in the 
   directory: distribution/target

Good luck!

The AndroMDA Team
