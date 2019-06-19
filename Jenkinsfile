pipeline {
    agent any
    tools {
    maven 'mvn'
  }
    parameters {
        string(name: 'ICP_MASTER_CFC', defaultValue: 'mycluster.icp:8500')
        string(name: 'ICP_MASTER_8001', defaultValue: 'https://9.42.41.72:8001')
        string(name: 'ICP_TOKEN', defaultValue: 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlZmF1bHQtdG9rZW4tcjN3bmwiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVmYXVsdCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImM0ZmYxZGY3LWNhMzctMTFlNy05MDE5LTAwNTA1NmEwOTdlOCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OmRlZmF1bHQifQ.VymOFbAi6AQxSOOINAQDGPk14ahDgHMI5Z10cA0xtyM_o4G0-CPbyiavl7QmeXwlZLg8AbKsWxQ1s4PBvMSqt1dt19_5dTmFD6Pp4gM0u0ISEAc0RH-NJrTvydRvKb8b1iDwoi-DfHKvw6wXAcnd6wOIdlyjqbS6nD1yPfZJSWlppKvEv8S46xo-l2uzj4JSJIEc5m8tGkExLhKXb5LvVyfau14CEXkXiHWp9_jjNLrreMTG98BwVw1SLVAHEPWERMMMr0z5XUIV-auSBDuTpOK3RgrOUZZYZP2zUi91XYSkhbiw9IyWW1SnGlvwUPl8D61kSKwu8HWsPECuDRdfCQ')
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
