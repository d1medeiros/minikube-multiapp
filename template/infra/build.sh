
VERSION=$(jq '.version' ./template/finalconfig.json)
NAMESPACE=$(jq '.namespace' ./template/finalconfig.json)
LABEL=$(jq '.label' ./template/finalconfig.json)
CLEAN_LABEL=$(echo $LABEL | sed 's/\"//g')
CPORT=$(jq '.cport' ./template/finalconfig.json)
PORT=$(jq '.port' ./template/finalconfig.json)
IMAGE="$CLEAN_LABEL:$VERSION"


echo $LABEL
echo $IMAGE
cat ./template/deployment.yaml \
  | yq '.metadata.namespace |= '$NAMESPACE''  \
  | yq '.metadata.name |= '$LABEL''  \
  | yq '.spec.selector.matchLabels.run |= '$LABEL''  \
  | yq '.spec.template.metadata.labels.run |= '$LABEL''  \
  | yq '.spec.template.spec.containers[0].name |= '$LABEL''  \
  | yq '.spec.template.spec.containers[0].image |= "'$IMAGE'"'  \
  | yq '.spec.template.spec.containers[0].ports[0].containerPort |= '$CPORT''  \
  | tee -i deployment.yaml

LABEL_SERVICE="$CLEAN_LABEL-svc"
cat ./template/service.yaml \
  | yq '.metadata.namespace |= '$NAMESPACE''  \
  | yq '.metadata.name |= "'$LABEL_SERVICE'"'  \
  | yq '.metadata.labels.run |= "'$LABEL_SERVICE'"'  \
  | yq '.spec.selector.run |= '$LABEL''  \
  | yq '.spec.ports[0].port |= '$PORT''  \
  | yq '.spec.ports[0].targetPort |= '$CPORT''  \
  | tee -i service.yaml


#VERSION=$1
#IMAGE=$(yq '.spec.template.spec.containers[0].image' deployment.yaml)
#NEWIMAGE=$(echo $IMAGE | sed 's/:.*/:'$VERSION'/')
#
#echo "changing image from:$IMAGE to:$NEWIMAGE"
#yq -i '.spec.template.spec.containers[0].image |= "'$NEWIMAGE'"' deployment.yaml
#
minikube kubectl -- apply -f .


#minikube kubectl -- delete -n default service apppuc-service
#minikube kubectl -- expose deployment apigateway --type=NodePort --port=4000 --target-port=3000
#minikube kubectl -- port-forward service/apigateway 8080:4000
#minikube kubectl -- port-forward service/apicustomer 3000:3000
#minikube kubectl -- port-forward service/apiaccount 3001:3001
#minikube kubectl -- port-forward service/apifraud 3002:3002