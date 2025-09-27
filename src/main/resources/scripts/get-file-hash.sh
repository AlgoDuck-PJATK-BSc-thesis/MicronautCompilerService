#!/bin/sh

FILE_PATH=$1

if [ -f $FILE_PATH ]; then
  sha256sum $FILE_PATH
else
  exit 1
fi