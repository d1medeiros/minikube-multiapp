#!/bin/zsh

minikube kubectl -- expose deployment apicustomer --type=NodePort --port=3000

minikube kubectl -- port-forward service/apicustomer 7080:3000