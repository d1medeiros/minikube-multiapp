
#OLDVERSION=$(cat appversion)
#OLDVERSION=$(($OLDVERSION+1))
#echo $OLDVERSION
#sed -i 's//d/'$OLDVERSION'/' appversion
APPNAME="app-puc"
VERSION=2
DOCKER_IMAGE="${APPNAME}:${VERSION}"
echo $DOCKER_IMAGE
GOOS=linux GOARCH=amd64 go build -ldflags "-extldflags '-static'" -o app ./app.go
export DOCKER_IMAGE=$DOCKER_IMAGE
echo ${DOCKER_IMAGE}
docker build -t ${DOCKER_IMAGE} .
#docker push ${DOCKER_IMAGE}
minikube image load ${DOCKER_IMAGE}