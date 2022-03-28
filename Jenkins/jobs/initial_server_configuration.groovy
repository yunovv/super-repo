#!/usr/bin/env groovy

def git_cred_id = 'yunovv_ssh'
def nginx_cred_id = 'nginx_cred_001'
def scripts_repo = 'git@github.com:yunovv/super-repo.git'

pipeline {
    agent any
    parameters {
        string(name: 'my_string', defaultValue: '', description: 'test String')
    }

    stages {
        stage('Checkout Scripts Repo') {
            steps {
                script {
                    
                    sh 'mkdir my_scripts'
                    dir("my_scripts") {
                        checkout([$class: 'GitSCM', branches: [[name: '*/master']],userRemoteConfigs: [[url: scripts_repo, credentialsId: git_cred_id]]])
                        sh 'ls -l'
                    }
                }
            }
        }


        stage('Initial configuration') {
            steps {
                script {
                    sh "mkdir ansible_workplace"
                    sh "cp -r my_scripts/ansible/yunov/* ./ansible_workplace"
                }
            }
        }


        stage('Initial configuration') {
            steps {
                script {
                    dir("ansible_workplace") {
                        sh "ansible-playbook -i hosts initial_configuration.yml"
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
