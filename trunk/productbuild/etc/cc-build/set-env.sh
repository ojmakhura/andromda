#!/bin/sh 

if [[ -z $ANT_HOME || ! -d $ANT_HOME ]];
then
    export ANT_HOME=/opt/apache-ant
fi

if [ ! -d $ANT_HOME ];then
    echo "*** ANT_HOME:$ANT_HOME not found."
    exit
fi

CC_USER=andromda-build

if [ -z $CCDIR ]; then
  echo CCDIR not defined, using /opt/cruisecontrol
  CCDIR=/opt/cruisecontrol/main
  export CCDIR
fi

# Set the JDK to use
# Valid values are IBMJava142, SUNJava142, SUNJava150
export JDK=IBMJava142

case "$JDK" in
  "IBMJava142" )
     export MAVEN_OPTS="-Xmx512m"
     export JAVA_HOME=/opt/java/IBMJava2-142
  ;;
  "SUNJava142" )
     export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
     export JAVA_HOME=/opt/java/j2sdk1.4.2_07
  ;;
  "SUNJava150" )
     export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
     export JAVA_HOME=/opt/java/jdk1.5.0_01
  ;;
  * )
  ;;
esac

export PATH=$CCDIR/bin:$JAVA_HOME/bin:$PATH


CVSHOST=cvs.sourceforge.net
CVSROOTDIR=/cvsroot/andromda

CVSROOT=:ext:${USER}@${CVSHOST}:${CVSROOTDIR}
CVS_RSH=ssh
export CVSROOT CVS_RSH


