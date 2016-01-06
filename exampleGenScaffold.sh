#!/bin/bash

#选择
#先配置build.gradle中的数据库链接 
#
gradle scaffoldOfLeopaud  -PartifactId=example -PgroupId=com.duowan -PentityName=ExampleData  -Poverwrite=true -ProotProjectDir=/Users/wuwenyi/workspace/yy-workspace/game-workspace/example \
 -Pjdbcurl="jdbc:mysql://127.0.0.1:3306/example?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE" \
 -Pdbusername=root  \
 -Pdbpasswd= \
 -Pdbname=example  --stacktrace
