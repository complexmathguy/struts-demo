pipeline {
    agent { docker { image 'Jenkinsfile_Docker_App_Image()' } }
    stages {
        stage('install') {
        agent {
            docker { image 'openjdk8' }
        }
            steps {

                sh export DEFAULT_HOST_ADDRESS=127.0.0.1
                sh export DEFAULT_HOST_PORT=27017
                sh export DATABASE_USERNAME=root
                sh export DATABASE_PASSWORD=letmein2
                sh export DATABASE_DIALECT=org.hibernate.dialect.MySQL8Dialect
                sh export DATABASE_DRIVER=com.mysql.cj.jdbc.Driver
                sh export DATABASE_URL=jdbc:mysql://localhost:3306/strutsdemo?createDatabaseIfNotExist=true
            }
            
        stage('build') {
            steps {
                sh mvn package
            }
        }
    }
}
