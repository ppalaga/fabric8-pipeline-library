#!/usr/bin/groovy

def call(Map parameters = [:], body) {

    def defaultLabel = "clients.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')
    def label = parameters.get('label', defaultLabel)

    def clientsImage = parameters.get('clientsImage', 'fabric8/builder-clients:0.5')
    def inheritFrom = parameters.get('inheritFrom', 'base')
    podTemplate(label: label, serviceAccount: 'jenkins', inheritFrom: "${inheritFrom}",
            containers: [
                    [name: 'clients', image: "${clientsImage}", command: 'cat', ttyEnabled: true]
            ],
            volumes: [
                    secretVolume(secretName: 'gke-service-account', mountPath: '/root/home/.gke')
            ]) {
        body()
    }
}
