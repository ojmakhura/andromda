#!/bin/bash

# load the properties from the profile (to find ant on the path)
. ~/.bashrc

# go to the location of execution
cd ~/nightly/build

# execute with the lowest priority and notify using the MailLogger
nice -n 19 ant -logger org.apache.tools.ant.listener.MailLogger


