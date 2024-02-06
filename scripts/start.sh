#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app/lovebird-api"
#API_DOCS_ROOT="/home/ubuntu/app/src/main/resources/static/docs/index.html"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

IMAGE_NAME="lovebird"
CONTAINER_NAME="lovebird"

TIME_NOW=$(date +%c)

#chmod -R 755 $API_DOCS_ROOT

cd $PROJECT_ROOT

echo "$TIME_NOW > docker build --platform linux/amd64 --build-arg DEPENDENCY=build/dependency -t $IMAGE_NAME ." >> $DEPLOY_LOG
docker build --platform linux/amd64 --build-arg DEPENDENCY=build/dependency -t $IMAGE_NAME .
echo "$TIME_NOW > docker run --restart always -e TZ=Asia/Seoul --name $CONTAINER_NAME -d -p 8080:8080 $IMAGE_NAME" >> $DEPLOY_LOG
docker run --restart always -e TZ=Asia/Seoul --name $CONTAINER_NAME -d -p 8080:8080 $IMAGE_NAME

CURRENT_CONTAINER_ID=$(docker container ls -q)
echo "$TIME_NOW > 실행된 컨테이너 아이디 $CURRENT_CONTAINER_ID 입니다." >> $DEPLOY_LOG
nohup docker logs "$CURRENT_CONTAINER_ID" > $APP_LOG 2> $ERROR_LOG &
