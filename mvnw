#!/bin/sh
# Maven wrapper script for deployment platforms like Render/Railway
# This downloads mvnw if not present

if [ ! -f "mvnw" ]; then
  curl -s https://raw.githubusercontent.com/nickvdyck/spring-boot-starter/main/mvnw -o mvnw
  chmod +x mvnw
fi

mvn "$@"
