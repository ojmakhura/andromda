#! /bin/sh

source set-env.sh

me=`whoami`
                                                                                
if [ $me != amartinwest ]; then
  echo "Must be run as amartinwest"
  exit
fi
                                                                                
basename=`basename $0`
if [ $basename != $0 ]; then
  dirname=`dirname $0`
  cd $dirname
fi
                                                                                
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

