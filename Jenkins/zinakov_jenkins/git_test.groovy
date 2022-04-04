def string_job = 'zinakov_string'
def string_job_1 = 'yunovv_string'
def my_map = [string_job, string_job_1]


pipeline{
    agent any
    
    parameters {
        //string (name: 'my_string')
        choice (name: 'choices', choices: my_map)
        
        }
    
    stages {
        
        stage ('To start job') {
            steps {
               script {
                
                sh "echo $choices > example.txt"  
                sh "cat example.txt"
                //sh "apt-get install netstat"
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

