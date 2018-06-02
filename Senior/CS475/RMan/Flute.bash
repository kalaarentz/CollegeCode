#!/bin/bash
trap "exit" INT

i="0"

while [ $i -lt 360 ]
do
  frame=$[$i+1000]
  angle=$[$i]
  echo $frame
  sed  -e "s/FRAME/$frame/g" Flute.rib | sed -e "s/ANGLE/$angle/g" | prman
  i=$[$i+1]
done