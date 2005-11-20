#! /bin/sh

# Runs a normal CC, expects config.xml
basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
   cd $dirname 
fi
# Get absolute directory
dirname=`pwd`

source set-env.sh

if [ -z $CC_CONFIG_FILE ];then
   CC_CONFIG_FILE=cruisecontrol.xml
fi

echo "Using config file:$CC_CONFIG_FILE"

me=`whoami`
if [[ $me != $CC_USER ]]; then
  echo "You ($me) must be '$CC_USER' to run $0"
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

logger "runcc.sh - starting cruisecontrol"
logger "PATH: $PATH"
logger "JAVA_HOME:$JAVA_HOME"

nohup nice cruisecontrol.sh -port 8989 -configfile $dirname/${CC_CONFIG_FILE} &
pid=$!
echo $pid >$dirname/pid
echo Done $0

