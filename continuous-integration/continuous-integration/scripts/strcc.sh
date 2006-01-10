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
                                                                                
me=`whoami`
case $me in 
  "root" )
    su -l  $CC_USER -c $dirname/runcc.sh
    ;;
  "$CC_USER" )
    ./runcc.sh
    ;; 
   * )
    echo "Must be root or $CC_USER to run this"
    ;;
esac
                                                                                
echo "use tail -f $dirname/nohup.out to monitor output"

echo $0 Done.

