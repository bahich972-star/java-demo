pipeline {
    agent {
        docker {
            image 'jenkins-agent-java:latest'
            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        // Замените на ваш Docker Hub логин
        DOCKER_REPO = 'bahich1/java-demo'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Собираем и тестируем
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "=== Сборка Docker-образа ${DOCKER_REPO}:${IMAGE_TAG} ==="
                sh 'docker build -t $DOCKER_REPO:$IMAGE_TAG .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                echo "=== Отправка образа в Docker Hub ==="
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh 'docker push $DOCKER_REPO:$IMAGE_TAG'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Сборка, тесты и Docker-образ успешно выполнены!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo '❌ Сборка провалилась.'
        }
    }
}
