#!/usr/bin/env bash
bin=`dirname "$0"`
LEARNING_SERVER_HOME=`cd "$bin/.."; pwd`

echo $LEARNING_SERVER_HOME

nohup java -jar $LEARNING_SERVER_HOME/lib/learningserver-1.0.0.jar > ../learningserver.log 2>&1 &

PID=`ps aux | grep $LEARNING_SERVER_HOME | grep -v grep | awk '{print $2}'`

echo "start successfully, pid: $PID"
