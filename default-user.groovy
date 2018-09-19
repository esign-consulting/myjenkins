#!groovy

import jenkins.model.Jenkins
import hudson.security.*

def jenkins = Jenkins.getInstance()
jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false))
jenkins.setAuthorizationStrategy(new GlobalMatrixAuthorizationStrategy())

def userName = System.getenv("JENKINS_USER") ?: "admin"
def password = System.getenv("JENKINS_PASS") ?: "jenkins"
def user = jenkins.getSecurityRealm().createAccount(userName, password)
user.save()

jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, userName)
jenkins.save()
