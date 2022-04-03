#!/usr/bin/env groovy

def git_cred_id = 'rusnak_git'
def server_cred_id = 'servers_auth'
//def server_ip = '192.168.2.21'
def server_user = 'admin'
def git_repo = 'git@github.com:yunovv/super-repo.git'


pipeline {
    agent any
    parameters {
        //booleanParam(name: 'show_debug_info', defaultValue: false)
        //booleanParam(name: 'deploy_nginx', defaultValue: false)
        text(name: 'server_ip', description: 'Input Server IP-address.' , defaultValue: '192.168.2.21')
    }
	
	stages {
		stage('Change file hosts') {
            steps {
                script {
					dir("ansible/rusnak") {
						sh "echo [nginx] > hosts"
						sh "echo ${server_ip} >> hosts"
					}
                }
            }
        }
		
		stage('Run ansible configuration') {
			steps {
				script {
					dir("ansible/rusnak") {
						sh "ls -l"
						sh "cat hosts"
						withCredentials([sshUserPrivateKey(credentialsId: server_cred_id, keyFileVariable: 'MY_SSH')]) {
                            sh 'ansible-playbook -i hosts my_playbook.yml --private-key $MY_SSH --ssh-common-args="-o StrictHostKeyChecking=No"'
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