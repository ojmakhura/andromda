#! /bin/sh

dirname=`dirname $0`
if [ $dirname != "." ]; then
  cd $dirname
fi

if [[ $* = *clean* ]]; then
  rm -rf app/src
  rm -rf core/src/java
  rm -rf mda/src/java 
  rm -rf web/src/java
else
  cp -rup modifications/* .
fi
 
export MAVEN_OPTS="-XX:MaxPermSize=128m -Xmx512m"
maven $*  2>&1 | tee build.log

