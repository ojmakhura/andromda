#! /bin/sh

# Runs a normal CC, expects config.xml
basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
else
   dirname=`pwd`
fi
cd $dirname 

source set-env.sh

cruisecontrol.sh $*
