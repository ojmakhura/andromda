#! /bin/sh

basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
   cd $dirname
fi
# Get absolute directory
dirname=`pwd`

source set-env.sh

if [ -f pid ];then
  echo "CC already running, well the pid file still exsists."
  exit
fi
                                                                                
if [ -z $CCDIR ]; then
  echo CCDIR not defined, using /usr/local/cruisecontrol
  CCDIR=/usr/local/cruisecontrol/main
  export CCDIR
fi
                                                                                                                                                                
me=`whoami`
if [ $me == root ]; then
  su -l  $CC_USER -c runcc.sh
else
  if [ $me == $CC_USER ]; then
  ./runcc.sh
  else
    echo "Must be root or $CC_USER to run this"
  fi
fi
                                                                                
echo $0 Done.

