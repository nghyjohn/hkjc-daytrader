pipeline {
    agent any
    tools {
        maven 'mvn'
    }
    environment {
        MAVEN_HOME = '/opt/maven'
    }
    parameters {
        string(name: 'DOCKER_IMAGE_TAG', defaultValue: 'daytrader-ee6')
        string(name: 'JFROG_DOCKER', defaultValue: '192.168.136.160')
        string(name: 'JFROG_DOCKER_REPO', defaultValue: '192.168.136.160/docker-local')
        string(name: 'DOCKER_IMAGE_VERSION', defaultValue: '0.1')
    }
    stages {
        stage ('Cleanup old images'){
            steps {
                sh'''
                if [[ "$(sudo docker images -q 192.168.136.160/docker-local/daytrader-ee6:$DOCKER_IMAGE_VERSION 2> /dev/null)" != "" ]]; then
                    docker rmi 192.168.136.160/docker-local/daytrader-ee6:$DOCKER_IMAGE_VERSION
                fi

                if [[ "$(sudo docker images -q dhvines/daytrader-ee6:1.0-SNAPSHOT 2> /dev/null)" != "" ]]; then
                    docker rmi dhvines/daytrader-ee6:1.0-SNAPSHOT
                fi
                '''
            }
        }
        
        stage ('Clone') {
            steps {
                git branch: 'master', url: "https://github.com/nghyjohn/daytrader-ee6.git"
                
                gitCommitMessage = sh(returnStdout: true, script: 'git log -1|tail -1|xargs').trim()
                sh'''
                echo "msg"
                echo $gitCommitMessage
                '''
            }
        }
    
        stage ('Artifactory configuration') {
            steps {
                rtServer (
                    id: "JFrog",
                    url: "http://192.168.136.160/artifactory",
                    username: "admin",
                    password: "P@ssw0rd"
                )

                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "JFrog",
                    releaseRepo: "libs-release-local",
                    snapshotRepo: "libs-snapshot-local"
                )

                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: "JFrog",
                    releaseRepo: "libs-release",
                    snapshotRepo: "libs-snapshot"
                )
            }
        }
        
        stage ('Exec Maven') {
            steps {

                rtMavenRun (
                    pom: 'dt-ejb/pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
                

                rtMavenRun (
                    pom: 'Rest/pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
                
                rtMavenRun (
                    pom: 'web/pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
                
                rtMavenRun (
                    pom: 'daytrader-ee6/pom.xml',
                    goals: 'clean verify -Pci-docker',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
                
            }
        }
        
        stage('Push image to JFrog Docker Repo') {
            steps {
                sh '''
                    echo "docker login to JFrog Docker Repo"
                    docker login -u admin -p P@ssw0rd $JFROG_DOCKER
                    echo "docker tag"
                    docker tag dhvines/daytrader-ee6:1.0-SNAPSHOT $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG:$DOCKER_IMAGE_VERSION
                    echo "docker push"
                    docker push $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG
                    
                '''
            }
        }
    }
}
