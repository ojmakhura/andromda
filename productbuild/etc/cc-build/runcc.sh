#! /bin/sh

# Runs a normal CC, expects config.xml
basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
   cd $dirname 
fi
# Get absolute directory
dirname=`pwd`

# Set the JDK to use
# Valid values are IBMJava142, SUNJava142, SUNJava150
export JDK=IBMJava142
source set-env.sh

me=`whoami`
if [[ $me != $CC_USER ]]; then
  echo "Must be run as $CC_USER"
  exit
fi


# cd to andromda-all
cd ../..

# save previous output
if [ -f nohup.out ]; then
  mv --reply=y --backup=t nohup.out nohup.prev
fi

# For some reason cc/maven doesnt create it
# automagically.

mkdir -p ../logs/andromda-all                                                                    

nohup cruisecontrol.sh -configfile $dirname/cruisecontrol.xml &
pid=$!
echo $pid >$dirname/pid
echo Done $0

