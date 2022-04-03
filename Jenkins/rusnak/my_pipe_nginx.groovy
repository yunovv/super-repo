#!/usr/bin/env groovy

def git_cred_id = 'rusnak_git'
def server_cred_id = 'servers_auth'
def server_ip = '192.168.2.21'
def server_user = 'admin'
def git_repo = 'git@github.com:yunovv/super-repo.git'


pipeline {
    agent any
    parameters {
        booleanParam(name: 'show_debug_info', defaultValue: false)
        booleanParam(name: 'deploy_nginx', defaultValue: false)
        //text(name: 'input_text_field', description: 'input text field')
    }
	
    stages {
        stage('Checkout Scripts Repo') {
            steps {
                script {
                    // работаем с git при помощи плагина jenkins
                    sh 'mkdir super_repo'
                    dir("super_repo") {
                        checkout([$class: 'GitSCM', branches: [[name: '*/rusnak_jenkins']],userRemoteConfigs: [[url: git_repo, credentialsId: git_cred_id]]])
						sh 'ls -l'
                    }
                }
            }
        }	
		
		stage('Prepare Ansible env') {
            steps {
                script {
					sh "pwd"
					sh "ls -l"
                    sh "mkdir ansible"
                    sh "cp -r my_scripts/ansible/rusnak/* ./ansible"
                }
            }
        }
		
		stage('Change file hosts') {
            steps {
                script {
					dir("ansible") {
						cat hosts
						echo ${server_ip} > hosts
						cat hosts
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