NAME=$1

INDEX=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[0].key' ./config.json)
VERSION=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[0].value.version' ./config.json)
INNERDIR=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[0].value.innerdir' ./config.json)
NEWVERSION=$(($VERSION + 1))
echo "changing from $VERSION to $NEWVERSION on $NAME [$INDEX] - $INNERDIR"
yq -iP '.config['$INDEX'].version |= '$NEWVERSION'' ./config.json -o json

INDEX=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[1].key' ./config.json)
VERSION=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[1].value.version' ./config.json)
INNERDIR=$(jq '.config | to_entries | map(select(.value.label == "'$NAME'")) | .[1].value.innerdir' ./config.json)
NEWVERSION=$(($VERSION + 1))
echo "changing from $VERSION to $NEWVERSION on $NAME [$INDEX] - $INNERDIR"
yq -iP '.config['$INDEX'].version |= '$NEWVERSION'' ./config.json -o json

sh manager-cluster.sh $NAME
