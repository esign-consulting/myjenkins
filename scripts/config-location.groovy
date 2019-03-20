#!groovy

import jenkins.model.JenkinsLocationConfiguration

def url = System.getenv("JENKINS_URL") ?: "http://localhost:8080"
def email = System.getenv("JENKINS_EMAIL") ?: "jenkins@example.com"

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setUrl(url)
jenkinsLocationConfiguration.setAdminAddress(email)
jenkinsLocationConfiguration.save()
