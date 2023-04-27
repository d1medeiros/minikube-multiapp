


SIZE=$(jq '.config  | length' ./config.json)

for (( c=1; c<=$SIZE; c++ ))
do
 echo $c
 yq -iP '.config['$(($c-1))'].version |= 0' ./config.json -o json
done

