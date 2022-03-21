#!/usr/bin/env groovy

def git_cred_id = 'yunovv_ssh'
def nginx_cred_id = 'nginx_cred_001'
def nginx_ip = '10.10.0.'
def nginx_user = 'admin'
def scripts_repo = 'git@github.com:yunovv/super-repo.git'

pipeline {
    agent any
    parameters {
        booleanParam(name: 'show_debug_info', defaultValue: false)
    }

    stages {
        stage('Checkout Scripts Repo') {
            steps {
                script {
                    
                    // работаем с git из cli 
                    sh 'mkdir scripts_repo_dir_1'
                    dir("scripts_repo_dir_1") {
                        sh "git init"
                        withCredentials([sshUserPrivateKey(credentialsId: git_cred_id, keyFileVariable: 'SSH_KEY')]) {
                            sh "GIT_SSH_COMMAND='ssh -i ${SSH_KEY}' git fetch $scripts_repo master:local_master"
                        }
                        sh "git checkout local_master"
                        sh "ls"
                    }
                    

                    // работаем с git при помощи плагина jenkins
                    sh 'mkdir scripts_repo_dir_2'
                    dir("scripts_repo_dir_2") {
                        checkout([$class: 'GitSCM', branches: [[name: '*/master']],userRemoteConfigs: [[url: scripts_repo, credentialsId: git_cred_id]]])
                        sh 'ls -l'
                    }
                }
            }
        }


        stage('Show Nginx configs') {
            when { expression { params.show_debug_info } }
            steps {
                script {
                    echo 'Show Nginx configs:'
                    sshagent(credentials : [nginx_cred_id]) {
                        sh " ssh -o StrictHostKeyChecking=no ${nginx_user}@${nginx_ip} netstat -tulnp"
                        sh " ssh -o StrictHostKeyChecking=no ${nginx_user}@${nginx_ip} ip a"
                        sh " ssh -o StrictHostKeyChecking=no ${nginx_user}@${nginx_ip} cat /etc/nginx/nginx.conf"
                        sh " ssh -o StrictHostKeyChecking=no ${nginx_user}@${nginx_ip} ls /etc/nginx/conf.d"
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
