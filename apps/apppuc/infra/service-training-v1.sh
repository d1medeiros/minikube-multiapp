#!/bin/zsh

minikube kubectl -- expose deployment apppuc --type=NodePort --port=3000

minikube kubectl -- port-forward service/apppuc 7080:3000