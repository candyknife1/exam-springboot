#!/bin/bash

# 项目路径
APP_PATH="/www/wwwroot/exam"

echo "正在重启应用..."

# 停止应用
$APP_PATH/stop.sh

# 等待2秒
sleep 2

# 启动应用
$APP_PATH/start.sh

echo "应用重启完成" 