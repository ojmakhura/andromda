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
export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"

CVSHOST=cvs.sourceforge.net
CVSROOTDIR=/cvsroot/andromda

CVSROOT=:ext:${USER}@${CVSHOST}:${CVSROOTDIR}
CVS_RSH=ssh
export CVSROOT CVS_RSH


