
# create namespace
#minikube kubectl -- create -f ./namespace.yml
#minikube kubectl -- apply -f ./service.yaml
minikube kubectl -- apply -f ./deployment.yml
#minikube kubectl -- expose deployment apicustomer --type=NodePort --port=3000 --target-port=3000

#minikube kubectl -- delete -n default service apppuc-service