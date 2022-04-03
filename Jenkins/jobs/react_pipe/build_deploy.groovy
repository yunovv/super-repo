def sources_repo = 'git@github.com:yunovv/react-simple.git'
def sources_repo_branch = 'master'
def sources_repo_credid = 'react_git_ssh'
def nginx_cred_id = 'nginx_cred_001'


pipeline {
    agent any
    stages{
        stage('Checkout sorces repo'){
            steps{
                script{
                    sh 'ls -l'
                    sh 'mkdir react_sources'
                    dir("react_sources") {
                        checkout([$class: 'GitSCM', branches: [[name: sources_repo_branch]],userRemoteConfigs: [[url: sources_repo, credentialsId: sources_repo_credid]]])
                        sh 'ls -l'
                    }
                }
            }
        }

        stage('Build react app'){
            steps{
                script{
                    dir("react_sources") {
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }


        stage('Deploy static to Nginx'){
            steps{
                script{
                    dir("ansible/yunov") {
                        withCredentials([sshUserPrivateKey(credentialsId: nginx_cred_id, keyFileVariable: 'MYSSH')]) {
                            sh 'ansible-playbook -i hosts deploy_react_static.yml --private-key $MYSSH --ssh-common-args="-o StrictHostKeyChecking=No"'
                        }
                    }
                }
            }
        }


    }

    post {
        cleanup {
            cleanWs()
        }
    }

}