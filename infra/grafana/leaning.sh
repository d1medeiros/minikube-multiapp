

#minikube image load grafana/grafana-oss


minikube kubectl -- port-forward service/grafana 5000:5000