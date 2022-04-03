#!/usr/bin/env groovy

def git_cred_id = 'rusnak_git'
def server_cred_id = 'servers_auth'
//def server_ip = '192.168.2.21'
def server_user = 'admin'
def git_repo = 'git@github.com:yunovv/super-repo.git'


pipeline {
    agent any
    parameters {
        booleanParam(name: 'show_debug_info', defaultValue: false)
        booleanParam(name: 'deploy_nginx', defaultValue: false)
        text(name: 'server_ip', description: 'Input Server IP-address.' , defaultValue: '192.168.2.21')
    }
	
	stages {
		stage('Change file hosts.') {
			when { expression { params.deploy_nginx } }
            steps {
                script {
					dir("ansible/rusnak") {
						sh "echo [nginx] > hosts"
						sh "echo ${server_ip} >> hosts"
					}
                }
            }
        }
		
		stage('Run ansible configuration.') {
			when { expression { params.deploy_nginx } }
			steps {
				script {
					dir("ansible/rusnak") {
						withCredentials([sshUserPrivateKey(credentialsId: server_cred_id, keyFileVariable: 'SSH_KEY')]) {
                            sh 'ansible-playbook -i hosts my_playbook.yml --private-key $SSH_KEY --ssh-common-args="-o StrictHostKeyChecking=No"'
						}
					}
				}
			}
		}
		
		stage('Check files and Nginx configuration.') {
            when { expression { params.show_debug_info } }
            steps {
                script {
                    sshagent(credentials : [server_cred_id]) {
                        sh "ssh -o StrictHostKeyChecking=no ${server_user}@${server_ip} netstat -tulnp"
                        sh "ssh -o StrictHostKeyChecking=no ${server_user}@${server_ip} /sbin/ip addr"
                        sh "ssh -o StrictHostKeyChecking=no ${server_user}@${server_ip} cat /etc/nginx/nginx.conf"
                        sh "ssh -o StrictHostKeyChecking=no ${server_user}@${server_ip} ls /etc/nginx/conf.d"
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