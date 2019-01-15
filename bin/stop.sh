#!/usr/bin/env bash

bin=`dirname "$0"`
HUSKY_HOME=`cd "$bin/.."; pwd`

echo $HUSKY_HOME

PID=`ps aux | grep $HUSKY_HOME | grep -v grep | awk '{print $2}'`

if [ "$PID" == "" ]; then
    echo "process not exists"
else
    echo "killing process with pid $PID"
    kill -9 $PID
fi