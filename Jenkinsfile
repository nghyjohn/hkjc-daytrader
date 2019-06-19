pipeline {
    agent any
    tools {
    maven 'mvn'
  }
    parameters {
        string(name: 'DOCKER_IMAGE_TAG', defaultValue: 'daytrader-ee6')
        string(name: 'JFROG_DOCKER', defaultValue: '192.168.136.160')
        string(name: 'JFROG_DOCKER_REPO', defaultValue: '192.168.136.160/docker-local')
    }
    stages {
        stage('Continuous Integration') {
            steps {
                sh '''
                   cd dt-ejb
                   mvn clean install
                   cd ../Rest
                   mvn clean install
                   cd ../web
                   mvn clean install
                   cd ../daytrader-ee6
                   mvn clean verify -Pci-docker
                   cd ..
                   '''
             }
        }
        stage('Push image to JFrog Docker Repo') {
            steps {
                sh '''
                    echo "docker login to JFrog Docker Repo"
                    docker login -u admin -p P@ssw0rd $JFROG_DOCKER
                    echo "docker tag"
                    docker tag dhvines/daytrader-ee6:1.0-SNAPSHOT $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG
                    echo "docker push"
                    docker push $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG
                    
                '''
            }
        }
    }
}
