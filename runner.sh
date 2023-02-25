#!/bin/zsh

PLACE=$1
MODULE=$2
VERSION=$3
echo "./$PLACE/$MODULE"

cd "./$PLACE/$MODULE"

pwd

sh ./build.sh $VERSION