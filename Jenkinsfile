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
        string(name: 'JFROG_DOCKER', defaultValue: '192.168.136.213')
        string(name: 'JFROG_DOCKER_REPO', defaultValue: '192.168.136.213/docker-local')
        string(name: 'DOCKER_IMAGE_VERSION', defaultValue: '0.1')
    }
    
    stages {
        
        stage ('Clone') {
            
            steps {
                git branch: 'master', url: "https://github.com/nghyjohn/hkjc-daytrader.git"   
            }
        }
    
        stage ('Artifactory configuration') {
            steps {
                rtServer (
                    id: "JFrog",
                    url: "http://192.168.136.213:8081/artifactory",
                    username: "admin",
                    password: "password"
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
            environment {
                gitCommitMessage = sh(returnStdout: true, script: 'git log -1|tail -1|xargs').trim()
            }
            steps {
                sh '''
                    echo "docker login to JFrog Docker Repo"
                    docker login -u admin -p password $JFROG_DOCKER
                    echo "docker tag"
                    docker tag dhvines/daytrader-ee6:1.0-SNAPSHOT $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG:$gitCommitMessage
                    echo "docker push"
                    docker push $JFROG_DOCKER_REPO/$DOCKER_IMAGE_TAG:$gitCommitMessage
                '''
            }
        }
    }
}
