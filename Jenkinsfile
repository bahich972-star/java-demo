pipeline {
    agent {
        docker {
            image 'jenkins-agent-java:latest'
            args '-u root' // используем root, чтобы избежать проблем с правами на запись в workspace
        }
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Клонируем репозиторий ==='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '=== Сборка проекта (Maven) ==='
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '=== Запуск тестов ==='
                sh 'mvn -B test'
            }
        }

        stage('Package') {
            steps {
                echo '=== Упаковка артефакта (JAR) ==='
                sh 'mvn -B package -DskipTests'
            }
        }
    }

    post {
        success {
            echo '✅ Сборка и тесты прошли успешно!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo '❌ Сборка провалилась. Проверьте логи.'
        }
    }
}
