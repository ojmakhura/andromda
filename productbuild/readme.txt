This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.

Instructions:
1) Install the latest version of Maven 1.0.x (http://maven.apache.org) and add it to your $PATH.
2) Set the MAVEN_OPTS: "export MAVEN_OPTS=-XX:MaxPermSize=128m\ -Xmx512m" 
3) cd to andromda-all, type 'maven', this will build the entire distribution
   without documentation.  Check the target/distributions directory from the resulting output.

P.S. If you only want to build the documentation, just type 'maven docs' and
     the docs will be added the the distribution in the target/distributions directory. 
     
NOTE: When you build any distribution, all jars (or 'artifacts' as Maven calls them) will be 
      installed into your local repository as well.

Good luck!

The AndroMDA Team
