#!/bin/zsh

minikube kubectl -- expose deployment apiaccount --type=NodePort --port=3001 --targetPort=3000(find how to use it)

minikube kubectl -- port-forward service/apiaccount 7080:3001