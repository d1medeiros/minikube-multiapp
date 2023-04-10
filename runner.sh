#!/bin/zsh

kubectl create namespace monitoring

# apps
sh manager-cluster.sh "productapi"
#  && sh manager-cluster.sh "apigateway" \
#  && sh manager-cluster.sh "apicustomer" \
#  && sh manager-cluster.sh "apiaccount" \
#  && sh manager-cluster.sh "apifraud" \
#  && sh manager-cluster.sh "apioffer"


# infra
#sh manager-cluster.sh "infra-create"
#sh manager-cluster.sh "infra-delete"

# commands
## portforward
#kubectl port-forward service/prometheus-service 8080:8080 -n monitoring
#kubectl port-forward service/grafana 3000:3000 -n monitoring
#kubectl port-forward service/apigateway-svc 4000:4000 -n monitoring
#kubectl port-forward service/productapi-svc 4001:4001 -n monitoring
#kubectl port-forward es-cluster-0 9200:9200 --namespace=monitoring
#kubectl port-forward service/kibana 5601:5601 --namespace=monitoring