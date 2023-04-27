


VERSION=$(jq '.version' ./template/finalconfig.json)
NAMESPACE=$(jq '.namespace' ./template/finalconfig.json)
LABEL=$(jq '.label' ./template/finalconfig.json)
CLEAN_LABEL=$(echo $LABEL | sed 's/\"//g')
CPORT=$(jq '.cport' ./template/finalconfig.json)
PORT=$(jq '.port' ./template/finalconfig.json)
IMAGE="$CLEAN_LABEL:$VERSION"



APPNAME=$CLEAN_LABEL
echo "iniciando build $APPNAME com versão $VERSION"

DOCKER_IMAGE=$IMAGE
echo $DOCKER_IMAGE
GOOS=linux GOARCH=amd64 go build -ldflags "-extldflags '-static'" -o appbin ./app.go
export DOCKER_IMAGE=$DOCKER_IMAGE
echo ${DOCKER_IMAGE}
docker build -t ${DOCKER_IMAGE} .
#docker push ${DOCKER_IMAGE}
minikube image load ${DOCKER_IMAGE}