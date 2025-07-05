#!/bin/bash

# 项目名称
APP_NAME="exam"
# JAR包名称
JAR_NAME="exam-0.0.1-SNAPSHOT.jar"
# 项目路径
APP_PATH="/www/wwwroot/exam"
# 日志路径
LOG_PATH="$APP_PATH/logs"
# PID文件
PID_FILE="$APP_PATH/$APP_NAME.pid"

# 创建日志目录
mkdir -p $LOG_PATH

# 检查应用是否已经运行
if [ -f $PID_FILE ]; then
    PID=$(cat $PID_FILE)
    if ps -p $PID > /dev/null 2>&1; then
        echo "应用已经在运行中，PID: $PID"
        exit 1
    else
        echo "PID文件存在但进程不存在，删除PID文件"
        rm -f $PID_FILE
    fi
fi

# 启动应用
echo "正在启动 $APP_NAME..."
nohup java -Xms512m -Xmx1024m -jar $APP_PATH/$JAR_NAME --spring.profiles.active=prod > $LOG_PATH/startup.log 2>&1 &

# 保存PID
echo $! > $PID_FILE
echo "应用启动成功，PID: $!"
echo "日志文件: $LOG_PATH/startup.log" 