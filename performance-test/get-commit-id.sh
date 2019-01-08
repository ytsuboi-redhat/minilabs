#!/bin/sh

IS_TAG=$1

oc -n auth-api-stg describe istag $IS_TAG | grep GIT_COMMIT_ID | awk -F= '{print $2}'
