#! /bin/sh

me=`whoami`
if [ $me != $CC_USER ]; then
  echo "Must be run as $CC_USER"
  exit
fi
                                                                                                                                                                
basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
   cd $dirname
fi
# Get absolute directory
dirname=`pwd`

source set-env.sh
                                                                                
# save previous output
mv --reply=y --backup=t nohup.out nohup.prev

# For some reason cc/maven doesnt create it
# automagically.

mkdir -p logs/andromda-all
                                                                                
nohup maven cruisecontrol:run $* &
pid=$!
echo $pid >pid
                                                                                
pstree -pl `cat pid`

sleep 5                                                                                
tail nohup.out

echo $0 Done.

