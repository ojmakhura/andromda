#!/bin/sh 

if [[ -z $ANT_HOME || ! -d $ANT_HOME ]];
then
    export ANT_HOME=/usr/local/jakarta/apache-ant
fi

if [ ! -d $ANT_HOME ];then
    echo "*** ANT_HOME:$ANT_HOME not found."
    exit
fi

export CC_USER=amartinwest

case "$JDK" in
  "IBMJava142" )
     export MAVEN_OPTS="-Xmx512m"
     export JAVA_HOME=/opt/IBMJava2-142
  ;;
  "SUNJava142" )
     export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
     export JAVA_HOME=/usr/local/j2sdk1.4.2_03
  ;;
  "SUNJava150" )
     export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
     export JAVA_HOME=/usr/local/jdk1.5.0_01
  ;;
  * )
  ;;
esac

export PATH=$JAVA_HOME/bin:$PATH


CVSHOST=cvs.sourceforge.net
CVSROOTDIR=/cvsroot/andromda

CVSROOT=:ext:${USER}@${CVSHOST}:${CVSROOTDIR}
CVS_RSH=ssh
export CVSROOT CVS_RSH


