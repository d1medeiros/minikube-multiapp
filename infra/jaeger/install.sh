
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.6.3/cert-manager.yaml

echo "waiting cert-manager install..."
sleep 80

kubectl create namespace observability # <1>
kubectl create -f https://github.com/jaegertracing/jaeger-operator/releases/download/v1.44.0/jaeger-operator.yaml -n observability # <2>


#kubectl port-forward $(kubectl get pods -l=app="jaeger" -o name -n monitoring) 16686:16686 -n monitoring

echo "waiting jaeger cr install..."
sleep 80

kubectl apply -f .