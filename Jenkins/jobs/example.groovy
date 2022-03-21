#!/usr/bin/env groovy

pipeline {
    //agent { label "all" }
    agent any
    parameters {
        string(name: 'send_file_path', defaultValue: '/sowa/profile_storage/custom/curcontrol_web/temp_file', description: '')
        text(name: 'send_file_contents', description: 'Содержимое файла')
        booleanParam(name: 'send_file', defaultValue: false)

    }

    stages {
        stage('Send file to server') {
            when { expression { params.send_file } }
            steps {
                script {

                    sh "touch send_file"
                    writeFile(file: "send_file", text: params.send_file_contents)
                    sh "cat send_file"

                    echo 'sending file using scp:'
                    sshagent(credentials : [sowa_cred]) {
                        sh " scp -o StrictHostKeyChecking=no send_file ${sowa_user}@${sowa_ip}:$send_file_path"
                        sh " ssh -o StrictHostKeyChecking=no ${sowa_user}@${sowa_ip} cat $send_file_path"
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
