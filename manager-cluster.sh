## https://minikube.sigs.k8s.io/docs/start/


#minikube start
#minikube stop
#minikube delete -all
#minikube dashboard --url
#minikube image load app-puc:1

#NAME="apigateway"
#NAME="productapi"
#NAME="apicustomer"
#NAME="apiaccount"
#NAME="apifraud"
#NAME="apioffer"
#NAME="infra-create"
#NAME="infra-delete"

NAME=$1

WORKPLACE=$(pwd)

SIZE=$(jq '.config  | map(select(.label == "'$NAME'")) | length' ./config.json)

for (( c=1; c<=$SIZE; c++ ))
do
  echo "Welcome $c times"
  cd $WORKPLACE
  jq '.config  | map(select(.label == "'$NAME'")) | .['$(($c-1))']' ./config.json | tee -i finalconfig.json
  NAMEMODULE=$(cat ./finalconfig.json | jq -r '.name')
  INNER_DIR=$(cat ./finalconfig.json | jq -r '.innerdir')
  VERSION=$(cat ./finalconfig.json | jq '.version')
  TYPE=$(cat ./finalconfig.json | jq -r '.type')
  echo "running $NAMEMODULE version:$VERSION"

  if [ $TYPE == "apps" ]
  then
    echo "work type apps"
    cp ./template/$INNER_DIR/build.sh ./$TYPE/$NAMEMODULE
    mv finalconfig.json ./$TYPE/$NAMEMODULE/template
    cd ./$TYPE/$NAMEMODULE
    sh build.sh
  else
    echo "work type infra"
    ACTION=$(cat ./finalconfig.json | jq -r '.action')
    cd ./$TYPE/$NAMEMODULE
    eval $ACTION
  fi
done
