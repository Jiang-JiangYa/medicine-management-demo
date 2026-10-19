#!/bin/sh
set -eu
: "${PORT:=10000}"
export PORT
envsubst '${PORT}' < /app/nginx.conf.template > /etc/nginx/nginx.conf
mkdir -p "$UPLOAD_DIR"
java -jar /app/app.jar &
JAVA_PID=$!
nginx -g 'daemon off;' &
NGINX_PID=$!
trap 'kill -TERM "$JAVA_PID" "$NGINX_PID" 2>/dev/null || true; wait || true' TERM INT EXIT
while kill -0 "$JAVA_PID" 2>/dev/null && kill -0 "$NGINX_PID" 2>/dev/null; do sleep 2; done
exit 1
