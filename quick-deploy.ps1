# quick-deploy.ps1
kubectl apply -f authenmedaf/k8s-deployment.yaml
kubectl set image deployment/authenmedaf authenmedaf=hamzadev2025/authenmedaf:2.2.2
kubectl rollout status deployment/authenmedaf
minikube service authenmedaf-service