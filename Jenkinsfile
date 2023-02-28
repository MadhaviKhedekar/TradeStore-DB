pipeline{
    agent any
    tools{
        jdk "JAVA_HOME"
        maven "MAVEN_HOME"
    }
    environment{
        DEPLOY_TO_IT="true"
    }
    stages {
        stage('Checkout'){
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '1c925c38-0764-4d2c-a31e-44bc37561191', url: 'https://github.com/MadhaviKhedekar/TradeStore-DB']])
            }
        }
        stage ('Build'){
            steps{
                echo "about to build the ppipline"
                bat 'mvn clean install -DskipTests'
                echo 'code build successful'
            }
        }
        stage('Test'){
            steps{
                echo "about to execute tests"
                bat 'mvn test'
                echo 'test successful'

            }
        }
        stage('Deploy to DEV'){
            steps{
                echo 'about to deploy the code'
                bat 'mvn spring-boot:run -Dserver.port=8089'
                echo 'code deployed successfully to DEV'
            }
        }
        /*stage('confirm to IT'){
            when{
                expression {DEPLOY_TO_IT=="true" }
            }
            options{
                timeout(time:2, unit:'MINUTES')
            }
            steps{
                echo 'deployemenr to IT confirmed by user'
            }
        }
        stage('Deploy to IT'){
            when{
                    expression {DEPLOY_TO_IT=="true"}
            }
             steps{
                echo 'about to deploy the code in IT'
                bat 'start /b mvn spring-boot:run -Dserver.port=8087 &'
                echo 'code deployed successfullly to IT'
            }
        }*/
    }

}