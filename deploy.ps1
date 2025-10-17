# deploy.ps1
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🚀 Déploiement sur Minikube Local" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Vérifier si Minikube tourne
Write-Host "`n1️⃣ Vérification de Minikube..." -ForegroundColor Yellow
$minikubeStatus = minikube status 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Minikube n'est pas démarré !" -ForegroundColor Red
    Write-Host "🔄 Démarrage de Minikube..." -ForegroundColor Yellow
    minikube start
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Échec du démarrage de Minikube" -ForegroundColor Red
        exit 1
    }
}
Write-Host "✅ Minikube est actif" -ForegroundColor Green

# Appliquer la configuration Kubernetes
Write-Host "`n2️⃣ Application de la configuration Kubernetes..." -ForegroundColor Yellow
kubectl apply -f authenmedaf/k8s-deployment.yaml
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Échec de l'application de la configuration" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Configuration appliquée" -ForegroundColor Green

# Mettre à jour l'image Docker
Write-Host "`n3️⃣ Mise à jour de l'image Docker..." -ForegroundColor Yellow
kubectl set image deployment/authenmedaf authenmedaf=hamzadev2025/authenmedaf:3.3.3
Write-Host "✅ Image mise à jour" -ForegroundColor Green

# Attendre que le déploiement soit prêt
Write-Host "`n4️⃣ Attente du déploiement..." -ForegroundColor Yellow
kubectl rollout status deployment/authenmedaf --timeout=120s
if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️ Le déploiement prend plus de temps que prévu" -ForegroundColor Yellow
}

# Afficher l'état
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "📊 État du déploiement" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
kubectl get pods -l app=authenmedaf
kubectl get svc authenmedaf-service

# Obtenir l'URL
Write-Host "`n========================================" -ForegroundColor Green
Write-Host "✅ Déploiement terminé !" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "`n🌐 Pour accéder à l'application :" -ForegroundColor Cyan
Write-Host "   minikube service authenmedaf-service" -ForegroundColor White
Write-Host "`nOu exécutez cette commande pour obtenir l'URL :" -ForegroundColor Yellow
Write-Host "   minikube service authenmedaf-service --url" -ForegroundColor White