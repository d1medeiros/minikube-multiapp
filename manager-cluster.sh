## https://minikube.sigs.k8s.io/docs/start/


#minikube start
#minikube stop
#minikube delete -all
#minikube dashboard --url
#minikube image load app-puc:1

#NAME="app-gateway/app"
#NAME="app-gateway/infra"
NAME="apigateway"

WORKPLACE=$(pwd)

SIZE=$(jq '.config  | map(select(.label == "'$NAME'")) | length' ./config.json)

for (( c=1; c<=$SIZE; c++ ))
do
  echo "Welcome $c times"
  cd $WORKPLACE
  jq '.config  | map(select(.label == "'$NAME'")) | .['$(($c-1))']' ./config.json | tee -i finalconfig.json
  NAMEMODULE=$(cat ./finalconfig.json | jq -r '.name')
  VERSION=$(cat ./finalconfig.json | jq '.version')
  TYPE=$(cat ./finalconfig.json | jq -r '.type')

  echo "running $NAMEMODULE version:$VERSION"
  mv finalconfig.json ./$TYPE/$NAMEMODULE/template
  cd ./$TYPE/$NAMEMODULE

  sh build.sh
done
