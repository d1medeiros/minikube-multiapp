
# create namespace
minikube kubectl -- create -f ./namespace.yml
minikube kubectl -- apply -f ./deployment.yml
minikube kubectl -- apply -f ./service.yml

minikube kubectl -- delete -n default service apppuc-service