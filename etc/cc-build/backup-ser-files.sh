#!/bin/sh

basename=`basename $0`
if [ $basename != $0 ]; then
   dirname=`dirname $0`
else
   dirname=`pwd`
fi
cd $dirname/../..

cp --reply=yes *.ser ..
