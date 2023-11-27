#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_CONTAINER_ID=$(docker container ls -q)

if [ -z "$CURRENT_CONTAINER_ID" ]
then
  echo "$TIME_NOW > 현재 구동중인 Docker 컨테이너가 없습니다." >> $DEPLOY_LOG
else
  echo "현재 구동중인 Docker 컨테이너를 종료시키겠습니다."
  echo "$TIME_NOW > docker stop $CURRENT_CONTAINER_ID"
  docker stop "$CURRENT_CONTAINER_ID"
  docker rm "$CURRENT_CONTAINER_ID"
  sleep 5
fi

if docker ps -a --format '{{.Names}}' | grep -q '^lovebird$'; then
    docker rm lovebird
    echo "중지된 Docker 컨테이너를 종료시키겠습니다."
fi
