This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.
      
Instructions for Maven2:

1) Install the latest version of Maven 2.0.x (http://maven.apache.org) and add it to your $PATH.
2) cd to andromda-all, type "mvn -N antrun:run", this will install the required archive maven plugins
   (NOTE: this step only needs to be performed the first time you build with with a fresh repository).
3) Next type "mvn", this will proceed to build the entire distribution, which can be found in the 
   directory: distribution/target

NOTE: When you build any distribution, all jars (or 'artifacts' as Maven calls them) will be 
      installed into your local repository as well.

Good luck!

The AndroMDA Team
