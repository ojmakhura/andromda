#!/bin/sh
# dummy change

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

if [ ! -z $1 ]; then
  options=-r $1
else
  options=-b
fi

daystart=`date +"%F 00:00:00 %Z"`
now=`date +"%F %R:%S %Z"`
echo cvs -q log -N "-d$daystart<$now" $options
cvs -q log -N "-d$daystart<$now" $options
