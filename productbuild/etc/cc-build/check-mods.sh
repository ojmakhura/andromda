#!/bin/sh

source set-env.sh

daystart=`date +"%F 00:00:00 %Z"`
now=`date +"%F %R:%S %Z"`
echo cvs -q log -N "-d$daystart<$now" -b
cvs -q log -N "-d$daystart<$now" -b
