#!/bin/sh 

if [[ -z $ANT_HOME || ! -d $ANT_HOME ]];
then
    export ANT_HOME=/opt/apache-ant
fi

if [ ! -d $ANT_HOME ];then
    echo "*** ANT_HOME:$ANT_HOME not found."
    exit
fi

if [ -z $CCDIR ]; then
  echo CCDIR not defined, using /opt/cruisecontrol
  CCDIR=/opt/cruisecontrol/main
  export CCDIR
fi

# Set the JDK to use
# Valid values are IBMJava142, SUNJava142, SUNJava150
# externalised
# export JDK=IBMJava142
# NB j2sdk1.4 and jdk1.5 are symlinks to the actual installed 
# version.

case "$JDK" in
  "Stockholm" )
     export MAVEN_OPTS="-XX:MaxPermSize=512m -Xmx1024m"
     export JAVA_HOME=/pkg/jdk/1.4.2_06
  ;;
  "IBMJava142" )
     export MAVEN_OPTS="-Xmx1024m"
     export JAVA_HOME=/opt/java/IBMJava2-142
  ;;
  "SUNJava142" )
     export MAVEN_OPTS="-XX:MaxPermSize=512m -Xmx1024m"
     export JAVA_HOME=/opt/java/j2sdk1.4
  ;;
  "SUNJava150" )
     export MAVEN_OPTS="-XX:MaxPermSize=512m -Xmx1024m"
     export JAVA_HOME=/opt/java/jdk1.5
  ;;
  * )
     echo "**** ERROR JDK($JDK) environment variable incorrect"
     echo "**** Should be one of IBMJava142, SUNJava142, SUNJava150"
     echo "**** Defaulting to SunJava42"
     logger "**** ERROR JDK($JDK) environment variable incorrect"
     logger "**** Should be one of IBMJava142, SUNJava142, SUNJava150"
     export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
     export JAVA_HOME=/opt/java/j2sdk1.4
  ;;
esac

export PATH=$CCDIR/bin:$JAVA_HOME/bin:$PATH

echo "**** Java Version "
java -fullversion
echo "***"

CVSHOST=cvs.sourceforge.net
CVSROOTDIR=/cvsroot/andromda

CVSROOT=:ext:${USER}@${CVSHOST}:${CVSROOTDIR}
CVS_RSH=ssh
export CVSROOT CVS_RSH


