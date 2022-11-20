
def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t sofienechihi/my-repo:spring-app-1.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push sofienechihi/my-repo:spring-app-1.0'
    }
}

def sonarScan(String serverIp, String serverUser) {
    echo "Running sonarQube scan..."
    def runSonar = '"bash runSonarQube.sh"'
    sshagent (credentials: ['sonar-server']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${runSonar}"
    }}

def deployApp(String serverIp, String serverUser) {
    echo 'deploying the application...'
    def composeRun = '"docker-compose up -d"'
    sshagent (credentials: ['deployment-server']) {
        sh "ssh -o StrictHostKeyChecking=no ${serverUser}@${serverIp} ${composeRun}"
    }
}

def cleanUntaggedImages(){
    def cleanImages = 'docker image prune --force --filter "dangling=true"'
    sshagent (credentials: ['deployment-server']) {
        sh "ssh -o StrictHostKeyChecking=no sofiene@192.168.122.101 ${cleanImages}"
    }
}

return this
