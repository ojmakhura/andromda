#! /bin/sh

basename=`basename $0`
if [ $basename != $0 ]; then
  dirname=`dirname $0`
  cd $dirname
fi

if [ -f pid ];then
  pstree -p `cat pid`
  # have to parse the output of pstree ...
  # cruisecontrol.s(20706)───java(20707)
  ccpid=`cat pid`
  jpid=`pstree -p $ccpid | awk -F\( '{print $3 }' | awk -F\) '{print $1}'`
  echo "Killing $jpid"
  kill -9 $jpid
  rm -f pid
#  logs_dir=/var/cc_work/logs
#  echo '<span class="link">Current Build Stopped At:<br>' > ${logs_dir}/obrien/buildstatus.txt
#  date +%m/%d/%y-%H:%M:%S >> ${logs_dir}/obrien/buildstatus.txt
#  echo '</span>' >> ${logs_dir}/obrien/buildstatus.txt
  
else
  echo "CC not running, well pid file not found."
fi

