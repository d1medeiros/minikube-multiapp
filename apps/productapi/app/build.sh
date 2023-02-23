
#OLDVERSION=$(cat appversion)
#OLDVERSION=$(($OLDVERSION+1))
#echo $OLDVERSION
#sed -i 's//d/'$OLDVERSION'/' appversion
APPNAME="api-product"
VERSION=4
DOCKER_IMAGE="${APPNAME}:${VERSION}"
echo $DOCKER_IMAGE
./gradlew build
export DOCKER_IMAGE=$DOCKER_IMAGE
echo ${DOCKER_IMAGE}
docker build -t ${DOCKER_IMAGE} .
#docker push ${DOCKER_IMAGE}
minikube image load ${DOCKER_IMAGE}