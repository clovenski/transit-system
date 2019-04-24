#!/usr/bin/env bash

if [[ "$OSTYPE" == "msys" ]]; then
  java -cp '.;bin;res/*' Lab4
elif [[ "$OSTYPE" == "darwin" ]]; then
  java -cp '.:bin:res/*' Lab4
else
  echo "script does not support your os: $OSTYPE"
  echo "edit the script to adjust"
fi