The samples illustrate the use of AndroMDA with Maven.  
Both the Animal Quiz and Car Rental System show typical
AndroMDA project structures.  The Animal Quiz is the most
current sample: you should be able to easily use that as 
a template for your AndroMDA project.  

It's also possible to build an Ant based project with AndroMDA by 
using the <andromda> ant task (although its currently not shown in the
samples).

To build the samples, perform the following steps:

1) Install the latest version of Maven (http://maven.apache.org) and add it to your $PATH or %PATH%
2) Create a build.properties file and add the following line:
  'maven.repo.remote=http://www.ibibilio.org/maven,http://team.andromda.org/maven'. 
   Place this build.properties file within your %USERPROFILE% (windows), or $HOME (
   linux/unix) directory.
3) From this directory, type 'maven', this will build all samples contained within
   this directory.  Check the target directory(s) of each 'app' sub directory for
   the deployable J2EE ear.

Good Luck!

The AndroMDA Team