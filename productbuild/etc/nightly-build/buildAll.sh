#!/bin/bash

# load the properties from the profile (to find ant on the path)
. ~/.bashrc

# go to the location of execution
cd ~/nightly/build

# execute with the lowest priority and notify using the MailLogger
nice -n 19 ant dist -logger org.apache.tools.ant.listener.MailLogger
nice -n 19 ant docs -logger org.apache.tools.ant.listener.MailLogger

# deploy the samples to the JBoss server
~/stop_jboss.sh

rm ~/deploy/animal-quiz-*.ear
rm ~/deploy/online-store-*.ear

cp ~/maven/repository/andromda/samples/ears/online-store-app-3.0M3-SNAPSHOT.ear ~/deploy
cp ~/maven/repository/andromda/samples/ears/animal-quiz-app-3.0M3-SNAPSHOT.ear ~/deploy

~/start_jboss.sh
