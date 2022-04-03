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
					sh "pwd"
					sh "ls -l"
					dir("ansible/rusnak") {
						sh "echo ${server_ip} > hosts"
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