This directory is useful only in the context of the SVN module andromda.
If you want to build the entire andromda project then issue a 'svn checkout https://andromda.svn.sourceforge.net/svnroot/andromda/trunk andromda'
and it will contain this directory's contents.

1) Install the latest version of Maven 3.0.4 (http://maven.apache.org) and add two environment variables
2) Set M2_HOME to the install directory and add $M2_HOME/bin to your $PATH.
3) Set MAVEN_OPTS: "export MAVEN_OPTS=-XX:MaxPermSize=256m\ -Xmx512m"
4) cd to andromda-all/maven/maven-config, run 'mvn install'
5) cd to andromda-all/maven/model-archiver, run 'mvn install'
   (NOTE: these steps only need to be performed the first time you build with a fresh release version).
4) cd to andromda-all, type "mvn". This will build the entire distribution, which can be found in the
   directory: andromda-distribution/target
5) Or call "mvn -Pandromda-full" to build and install the entire distribution including all sources and javadocs.
6) Use "mvn deploy -Pandromda-full,server" to build and deploy everything to the sonatype repository
7) Use "mvn site -Pandromda-site" or "mvn site -Pandromda-site-lite" to build site-documentation.
You deploy it with "mvn site:deploy -Pandromda-site,local" or "mvn site:deploy -Pandromda-site,server".

Good luck!

The AndroMDA Team
