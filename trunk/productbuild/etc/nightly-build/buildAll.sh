#!/bin/bash

# load the properties from the profile (to find ant on the path)
. ~/.bashrc

# go to the location of execution
cd ~/nightly/build

# execute with the lowest priority and notify using the MailLogger
nice -n 19 ant dist -logger org.apache.tools.ant.listener.MailLogger
nice -n 19 ant docs -logger org.apache.tools.ant.listener.MailLogger

# deploy the samples to the JBoss server
set ONLINE_STORE=~/nightly/build/working/andromda-all/samples/online-store/web/target/online-store-sample-web.war
set ANIMAL_QUIZ=~/nightly/build/working/andromda-all/samples/animal-quiz/app/target/animal-quiz-app-3.0M3-SNAPSHOT.ear

if [ -f $ONLINE_STORE ] && [ -f $ANIMAL_QUIZ ]; then
    ~/stop_jboss.sh
    rm ~/deploy/animal-quiz-*.ear
    rm ~/deploy/online-store-*.war
    cp $ONLINE_STORE ~/deploy
    cp $ANIMAL_QUIZ ~/deploy
    ~/start_jboss.sh
fi
