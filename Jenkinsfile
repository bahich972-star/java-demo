pipeline {
    agent {
        docker {
            image 'jenkins-agent-java:latest'
            // Монтируем сокет Docker и конфигурацию kubeconfig
            args '-u root --network minikube -v /var/run/docker.sock:/var/run/docker.sock -v /home/marlon/jenkins-kube/config:/root/.kube/config -v /home/marlon/.minikube:/home/marlon/.minikube -e KUBECONFIG=/root/.kube/config'
        }
    }

    environment {
        DOCKER_REPO = 'bahich1/java-demo'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        // ... (стадии Checkout и Build & Test остаются без изменений)
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_REPO:$IMAGE_TAG .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh 'docker push $DOCKER_REPO:$IMAGE_TAG'
			
		    sh 'docker tag $DOCKER_REPO:$IMAGE_TAG $DOCKER_REPO:latest'
		    sh 'docker push $DOCKER_REPO:latest'
                }
            }
        }

        // НОВАЯ СТАДИЯ ДЕПЛОЯ
        stage('Deploy to Kubernetes') {
            steps {
                echo "=== Деплой образа ${IMAGE_TAG} в Minikube ==="
                // 1. Применяем манифесты (создаем ресурсы, если их нет)
                sh 'kubectl apply -f k8s/deployment.yaml --validate=false'
                sh 'kubectl apply -f k8s/service.yaml --validate=false'
                
                // 2. Обновляем образ на только что собранный
                sh "kubectl set image deployment/java-demo java-demo=${DOCKER_REPO}:${IMAGE_TAG}"
                
                // 3. Ждем, пока Kubernetes завершит обновление (rolling update)
                sh 'kubectl rollout status deployment/java-demo --timeout=300s'
            }
        }
    }

    post {
        success {
            echo '✅ Пайплайн CI/CD успешно завершен!'
        }
        failure {
            echo '❌ Ошибка на одном из этапов.'
        }
    }
}
