#!/bin/bash
trap "exit" INT

i="0"

while [ $i -lt 360 ]
do
  frame=$[$i+1000]
  angle=$[$i]
  nangle=$[$i+1]
  echo $frame $angle $nangle
  sed  -e "s/FRAME/$frame/g" FluteMotion.rib | sed -e "s/STRTANGLE/$angle/g" | sed -e "s/STOPANGLE/$nangle/g" | prman
  i=$[$i+1]
done