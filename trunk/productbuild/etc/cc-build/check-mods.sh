#!/bin/sh

basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
else
   dirname=`pwd`
fi
cd $dirname 
source set-env.sh
# cd to andromda-all
cd ../..

daystart=`date +"%F 00:00:00 %Z"`
now=`date +"%F %R:%S %Z"`
echo cvs -q log -N "-d$daystart<$now" -b
cvs -q log -N "-d$daystart<$now" -b
