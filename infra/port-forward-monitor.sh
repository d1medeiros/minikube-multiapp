minikube dashboard --url &

minikube kubectl -- port-forward service/prometheus-service 8080:8080 -n monitoring &

minikube kubectl -- port-forward service/grafana 3000:3000 -n monitoring &

minikube kubectl -- port-forward service/gateway-service 7080:4000 &

