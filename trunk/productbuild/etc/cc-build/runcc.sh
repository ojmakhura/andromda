#! /bin/sh

me=`whoami`
if [ $me != $CC_USER ]; then
  echo "Must be run as $CC_USER"
  exit
fi

# Runs a normal CC, expects config.xml
basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
   cd $dirname 
fi
# Get absolute directory
dirname=`pwd`
echo dirname $dirname

source set-env.sh

# cd to andromda-all
cd ../..

# save previous output
mv --reply=y --backup=t nohup.out nohup.prev

# For some reason cc/maven doesnt create it
# automagically.

mkdir -p ../logs/andromda-all                                                                    

nohup cruisecontrol.sh -configfile $dirname/cruisecontrol.xml &
pid=$!
echo $pid >$dirname/pid
echo "---------------------------------------"
echo "Following log, Enter ctl-c to terminate"
echo "---------------------------------------"
tail -f nohup.out
echo Done $0

