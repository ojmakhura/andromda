This directory is useful only in the context of the CVS module andromda-all.
If you want to build the entire andromda project then issue a 'cvs co andromda-all' and it will
contain this directory's contents.

Instructions:
1) Install the latest version of Maven (http://maven.apache.org) and add it to your $PATH
   and place in your %USERPROFILE% (windows), or $HOME (linux/unix) directory.
3) cd to andromda-all, type 'maven dist', this will build the entire distribution
   without documentation.  Check the target/distributions directory from the resulting output.

P.S. If you only want to build the documentation, just type 'maven docs' and
     the docs will be added the the distribution in the target/distributions directory. 
     
NOTE: When you build any distribution, all jars (or 'artifacts' as Maven calls them) will be 
      installed into your local repository as well.

Good luck!

The AndroMDA Team