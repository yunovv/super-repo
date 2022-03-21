#!/usr/bin/env groovy

def git_cred_id = 'yunovv_ssh'
def nginx_cred_id = 'nginx_cred_001'
def nginx_ip = '10.10.0.'
def nginx_user = 'admin'
def scripts_repo = 'git@github.com:yunovv/super-repo.git'

pipeline {
    agent { label "all" }
    parameters {
        booleanParam(name: 'show_debug_info', defaultValue: false)
    }

    stages {
        stage('Checkout Scripts Repo') {
            when { expression { params.send_file } }
            steps {
                script {

                    // работаем с git из cli 
                    sshagent(credentials : [git_cred_id]) {
                        sh "git ls-remote -h -- ${scripts_repo} HEAD"
                    }

                    // работаем с git при помощи плагина jenkins
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']],userRemoteConfigs: [[url: scripts_repo]], credentialsId: git_cred_id])

                    sh 'ls -l'
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
