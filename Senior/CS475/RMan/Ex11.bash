#!/bin/bash
trap "exit" INT

sed  -e 's/FRAME/01/g' Ex11.rib | sed -e 's/POS/0 0 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/02/g' Ex11.rib | sed -e 's/POS/0.1 0.1 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/03/g' Ex11.rib | sed -e 's/POS/0.2 0.2 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/04/g' Ex11.rib | sed -e 's/POS/0.3 0.3 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/05/g' Ex11.rib | sed -e 's/POS/0.4 0.4 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/06/g' Ex11.rib | sed -e 's/POS/0.5 0.5 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/07/g' Ex11.rib | sed -e 's/POS/0.6 0.6 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/08/g' Ex11.rib | sed -e 's/POS/0.7 0.7 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/09/g' Ex11.rib | sed -e 's/POS/0.8 0.8 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/10/g' Ex11.rib | sed -e 's/POS/0.9 0.9 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/11/g' Ex11.rib | sed -e 's/POS/1.0 1.0 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/12/g' Ex11.rib | sed -e 's/POS/1.1 1.1 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/13/g' Ex11.rib | sed -e 's/POS/1.2 1.2 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/14/g' Ex11.rib | sed -e 's/POS/1.3 1.3 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/15/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/0/g' | prman
sed  -e 's/FRAME/16/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-10/g' | prman
sed  -e 's/FRAME/17/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-20/g' | prman
sed  -e 's/FRAME/18/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-30/g' | prman
sed  -e 's/FRAME/19/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-40/g' | prman
sed  -e 's/FRAME/20/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-50/g' | prman
sed  -e 's/FRAME/21/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-60/g' | prman
sed  -e 's/FRAME/22/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-70/g' | prman
sed  -e 's/FRAME/23/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-80/g' | prman
sed  -e 's/FRAME/24/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-90/g' | prman
sed  -e 's/FRAME/25/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-100/g' | prman
sed  -e 's/FRAME/26/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-110/g' | prman
sed  -e 's/FRAME/27/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-120/g' | prman
sed  -e 's/FRAME/28/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-130/g' | prman
sed  -e 's/FRAME/29/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-140/g' | prman
sed  -e 's/FRAME/30/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-150/g' | prman
sed  -e 's/FRAME/31/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-160/g' | prman
sed  -e 's/FRAME/32/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-180/g' | prman
sed  -e 's/FRAME/33/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-190/g' | prman
sed  -e 's/FRAME/34/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-200/g' | prman
sed  -e 's/FRAME/35/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-210/g' | prman
sed  -e 's/FRAME/36/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-220/g' | prman
sed  -e 's/FRAME/37/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-230/g' | prman
sed  -e 's/FRAME/38/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-240/g' | prman
sed  -e 's/FRAME/39/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-250/g' | prman
sed  -e 's/FRAME/40/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-260/g' | prman
sed  -e 's/FRAME/41/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-270/g' | prman
sed  -e 's/FRAME/42/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-280/g' | prman
sed  -e 's/FRAME/43/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-290/g' | prman
sed  -e 's/FRAME/44/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-300/g' | prman
sed  -e 's/FRAME/45/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-310/g' | prman
sed  -e 's/FRAME/46/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-320/g' | prman
sed  -e 's/FRAME/47/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-330/g' | prman
sed  -e 's/FRAME/48/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-340/g' | prman
sed  -e 's/FRAME/49/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-350/g' | prman
sed  -e 's/FRAME/50/g' Ex11.rib | sed -e 's/POS/1.4 1.4 0/g' | sed -e 's/ANGLE/-360/g' | prman
