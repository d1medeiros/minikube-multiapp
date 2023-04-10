
kubectl create configmap kubernetes-conf --from-file=kubernetes.conf --namespace=monitoring --dry-run=client -o yaml \
  | kubectl apply -f -


kubectl create configmap fluentd-conf --from-file=fluent.conf --namespace=monitoring --dry-run=client -o yaml \
  | kubectl apply -f -


kubectl create configmap ftail-conf --from-file=tail_container_parse.conf --namespace=monitoring --dry-run=client -o yaml \
  | kubectl apply -f -