#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy(){
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"
  # 엔진엑스가 변경할 프록시 주소를 생성하고, 앞에서 넘겨준 문장을 service-url.inc에 덮어쓴다.
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  # reload는 restart와 다르게 끊기는 현상 없이 설정을 다시 불러온다. 중요한 설정들은 반영되지 않으므로 restart를 써야겠지만, 여기선 외부 설정 파일인 service-url만 다시 불러오기 때문에 reload로도 가능하다
  echo "> 엔진엑스 Reload"
  sudo service nginx reload
}