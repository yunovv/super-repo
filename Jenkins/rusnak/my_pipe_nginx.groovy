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
		stage('Change file hosts') {
            steps {
                script {
					sh "pwd"
					sh "ls -l"
					dir("ansible/rusnak") {
						sh "cat hosts"
						sh "echo ${server_ip} > hosts"
						sh "cat hosts"
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