#!groovy
properties([pipelineTriggers([pollSCM('H/2 * * * *')])])
pipeline {
    agent any
    tools {
        // maven 'maven3.6.0'
        maven 'maven3.5.3'
    }
    stages {
        // stage('チェックアウト') {
        //     steps {
        //         checkout scm
        //     }
        // }
        // stage('MySQLイメージビルド') {
        //     steps {
        //         script {
        //             docker.build('todo-mysql', './todo-mysql')
        //         }
        //     }
        // }
        // stage('frontend単体テスト') {
        //     steps {
        //         dir('todo-frontend') {
        //             nodejs(nodeJSInstallationName: 'Node 10.14.x') {
        //                 // prepare
        //                 sh 'npm install --slient'
        //                 // Run Frontend UT
        //                 sh 'npm run test:unit'
        //             }
        //         }
        //     }
        // }
        // stage('backend単体テスト') {
        //     steps {
        //         script {
        //             docker.image('todo-mysql').withRun('--net=ci_default --name=todo-mysql -e "MYSQL_ROOT_PASSWORD=P@ssw0rd" -e "MYSQL_USER=todo" -e "MYSQL_PASSWORD=P@ssw0rd" -e "MYSQL_DATABASE=todo" -p 3306:3306') { c ->
        //                 // Wait until mysql service is up
        //                 sh './todo-backend/wait-for-it.sh -t 30 todo-mysql:3306'
        //                 // Run Backend UT
        //                 sh 'mvn clean jacoco:prepare-agent test jacoco:report -f todo-backend'
        //             }
        //         }
        //     }
        // }
        // stage('静的解析') {
        //     steps {
        //         withSonarQubeEnv('default') {
        //             sh """
        //               ${tool 'sonarqube-scanner'}/bin/sonar-scanner \
        //                 -Dsonar.projectKey=todo:todo-frontend \
        //                 -Dsonar.projectName=todo-frontend \
        //                 -Dsonar.projectVersion=1 \
        //                 -Dsonar.javascript.lcov.reportPaths=todo-frontend/tests/unit/coverage/lcov.info \
        //                 -Dsonar.sources=todo-frontend/src 
        //             """
        //         }
        //         sh "mvn sonar:sonar -f todo-backend -Dsonar.host.url=http://workshop-sonar:9000"
        //     }
        // }
        // stage('frontendイメージビルド') {
        //     steps {
        //         dir('todo-frontend') {
        //             nodejs(nodeJSInstallationName: 'Node 10.14.x') {
        //                 sh 'npm run build'
        //             }   
        //         }
        //         sh 'docker build todo-frontend -t todo-frontend'
        //     }
        // }
        // stage('backendイメージビルド') {
        //     steps {
        //         sh 'mvn clean package -Dmaven.test.skip=true -f todo-backend'
        //         sh 'docker build todo-backend -t todo-backend'
        //     }
        // }
        // stage('frontend結合テスト') {
        //     steps {
        //         script {
        //             docker.image('todo-frontend').withRun('--net=ci_default --name=todo-frontend -p 80:80 -v $PWD/todo-frontend/dist:/usr/share/nginx/html/') { frontend ->
        //                 // Run AT
        //                 echo 'TODO:Frontend-IT'
        //                 // sh 'mvn clean jacoco:prepare-agent test jacoco:report -f todo-backend'
        //             }
        //         }
        //     }
        // }
        // stage('APIテスト') {
        //     steps {
        //         script {
        //             docker.image('todo-mysql').withRun('--net=ci_default --name=todo-mysql -e "MYSQL_ROOT_PASSWORD=P@ssw0rd" -e "MYSQL_USER=todo" -e "MYSQL_PASSWORD=P@ssw0rd" -e "MYSQL_DATABASE=todo" -p 3306:3306') { db ->
        //                 // Wait until mysql service is up
        //                 sh './todo-backend/wait-for-it.sh -t 30 todo-mysql:3306'
        //                 // Run IT
        //                 sh 'mvn clean verify -P it -f todo-backend -Dspring.profiles.active=container'
        //             }
        //         }
        //     }
        // }
        // stage('受け入れテスト') {
        //     steps {
        //         script {
        //             docker.image('todo-mysql').withRun('--net=ci_default --name=todo-mysql -e "MYSQL_ROOT_PASSWORD=P@ssw0rd" -e "MYSQL_USER=todo" -e "MYSQL_PASSWORD=P@ssw0rd" -e "MYSQL_DATABASE=todo" -p 3306:3306') { db ->
        //                 // Wait until mysql service is up
        //                 sh './todo-backend/wait-for-it.sh -t 30 todo-mysql:3306'

        //                 docker.image('todo-backend').withRun('--net=ci_default --name=todo-backend -p 8080:8080') { backend ->
        //                     // Wait until mysql service is up
        //                     sh './todo-backend/wait-for-it.sh -t 30 todo-backend:8080'

        //                     docker.image('todo-frontend').withRun('--net=ci_default --name=todo-frontend -p 80:80') { frontend ->
        //                         // Run AT
        //                         sh '_JAVA_OPTIONS=-Dfile.encoding=UTF-8 xvfb-run mvn clean test -f todo-at -Dselenide.baseUrl=http://todo-frontend -Dselenide.browser=firefox -Dwdm.gitHubTokenName=ytsuboi-redhat -Dwdm.gitHubTokenSecret=9d3e1bbbb62942a37c665a96609f7ba1acced0ff'
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }
        stage('パフォーマンステスト') {
            steps {
                script {
                    // docker.image('todo-mysql').withRun('--net=ci_default --name=todo-mysql -e "MYSQL_ROOT_PASSWORD=P@ssw0rd" -e "MYSQL_USER=todo" -e "MYSQL_PASSWORD=P@ssw0rd" -e "MYSQL_DATABASE=todo" -p 3306:3306') { db ->
                    //     // Wait until mysql service is up
                    //     sh './todo-backend/wait-for-it.sh -t 30 todo-mysql:3306'

                    //     docker.image('todo-backend').withRun('--net=ci_default --name=todo-backend -p 8080:8080') { backend ->
                    //         // Wait until mysql service is up
                    //         sh './todo-backend/wait-for-it.sh -t 30 todo-backend:8080'
                            // Run PT
                            sh '_JAVA_OPTIONS=-Dfile.encoding=UTF-8 mvn clean gatling:test -f todo-pt -Dgatling.baseUrl=http://todo-backend:8080'
                    //     }
                    // }
                    // echo "TODO"
                }
            }
        }
    }
    post {
        always {
            // save test JUnit format reports
            junit allowEmptyResults: true, testResults: 'test/**/*.xml'
        }
    }
}
