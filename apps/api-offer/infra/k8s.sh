
# create namespace
#minikube kubectl -- create -f ./namespace.yml
minikube kubectl -- apply -f ./deployment.yml
minikube kubectl -- expose deployment apioffer --type=NodePort --port=3003 --target-port=3000
#minikube kubectl -- apply -f ./service.yml

#minikube kubectl -- delete -n default service apppuc-service