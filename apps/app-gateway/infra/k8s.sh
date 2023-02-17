
# create namespace
#minikube kubectl -- create -f ./namespace.yml
minikube kubectl -- apply -f ./deployment.yml
#minikube kubectl -- apply -f ./service.yml


#minikube kubectl -- delete -n default service apppuc-service

#minikube kubectl -- expose deployment apigateway --type=NodePort --port=4000 --target-port=3000

#minikube kubectl -- port-forward service/apigateway 8080:4000


#minikube kubectl -- port-forward service/apicustomer 3000:3000
#minikube kubectl -- port-forward service/apiaccount 3001:3001
#minikube kubectl -- port-forward service/apifraud 3002:3002