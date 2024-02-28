#!/usr/local/bin/bash
DIR=$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
#cd "$DIR"
echo `pwd` > /root/start.log
cd /root/visitkeeper/
echo `pwd` > /root/start.log
#java -jar ./target/retroframe.jar
#chmod 0777 /media/logs/*
#ls /media > /root/retroframe/media.log
echo "cdfinish" > /root/start.log
/usr/local/bin/java -Xms512m -Xmx512m -Dspring.config.location=/media/config/application.properties -jar /root/visitkeeper/target/showvisitskeeper.jar 2>&1 | /usr/local/bin/rotatelogs -f -p '/root/visitkeeper/chmodlogs.sh' /media/logs/visitkeeper-backend.%Y%m%d-%H%M%S-%Z.log 5M  &
echo "java" > /root/start.log
#/usr/local/bin/multitail -J -Q 1 '/media/logs/*' | /usr/local/bin/pcregrep '(WARN|ERROR|^\tat |xception|^Caused by: |\t... \d+ more)'  | /usr/local/bin/rotatelogs -f -p '/root/retroframe/chmodlogs.sh' /media/logs/retroframe.stacktrace.%Y%m%d-%H%M%S-%Z.log 5M &

# ~/visitkeeper/target