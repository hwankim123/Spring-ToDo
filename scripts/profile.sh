#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1이 사용 주이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음

function find_idle_profile()
{
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면(즉, 40x/50x 에러 모두 포함)

  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ "${CURRENT_PROFILE}" == "real1" ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
  # bash 스크립트는 값을 반환하는 기능이 없기 때문에,
  # 스크립트의 제일 마지막에 echo를 쏘고, 클라이언트가 그 값을 받아간다
}

function find_idle_port()
{
  IDLE_PROFILE=$(find_idle_profile)

  if [ "${IDLE_PROFILE}" == "real1" ]
  then
    echo "8001"
  else
    echo "8002"
  fi
}