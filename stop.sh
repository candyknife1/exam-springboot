#!/bin/bash

# 项目名称
APP_NAME="exam"
# 项目路径
APP_PATH="/www/wwwroot/exam"
# PID文件
PID_FILE="$APP_PATH/$APP_NAME.pid"

# 检查PID文件是否存在
if [ ! -f $PID_FILE ]; then
    echo "PID文件不存在，应用可能没有运行"
    exit 1
fi

# 读取PID
PID=$(cat $PID_FILE)

# 检查进程是否存在
if ! ps -p $PID > /dev/null 2>&1; then
    echo "进程不存在，删除PID文件"
    rm -f $PID_FILE
    exit 1
fi

# 停止应用
echo "正在停止 $APP_NAME (PID: $PID)..."
kill $PID

# 等待进程结束
sleep 5

# 检查进程是否还在运行
if ps -p $PID > /dev/null 2>&1; then
    echo "进程仍在运行，强制终止..."
    kill -9 $PID
fi

# 删除PID文件
rm -f $PID_FILE
echo "应用已停止" 