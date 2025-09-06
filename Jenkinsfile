pipeline {
    agent any
    
    stages {
     
        stage('Checkout') {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/gayathriTGit/full-stack-deployment-project.git'
                }
            }
        }
      
        stage('Build') {
            steps {
                sh '''
                docker version
                docker rmi deployment-ec2:1.0 -f || true 
                docker build -t deployment-ec2:1.0 -f Dockerfile .
                '''
            }
        }
    
        stage('Run') {
            steps {
                sh '''                
                docker rm -f deployment-ec2-container || true
                docker run -d --name deployment-ec2-container -p 9001:9001 deployment-ec2:1.0
                '''
            }
        }
    }
}
