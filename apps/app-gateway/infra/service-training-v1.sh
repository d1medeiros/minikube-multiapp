#!/bin/zsh

minikube kubectl -- expose deployment apigateway --type=NodePort --port=3002 --target-port=3000

minikube kubectl -- port-forward service/apigateway 7080:4000