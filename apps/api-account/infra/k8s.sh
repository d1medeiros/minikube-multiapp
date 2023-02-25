
# create namespace
#minikube kubectl -- create -f ./namespace.yml
minikube kubectl -- apply -f ./deployment.yml
#minikube kubectl -- expose deployment apiaccount --type=NodePort --port=3001 --target-port=3000

#minikube kubectl -- apply -f ./service.yaml
#minikube kubectl -- delete -n default service apppuc-service