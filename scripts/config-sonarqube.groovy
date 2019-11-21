#!groovy

import jenkins.model.Jenkins
import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation
import hudson.util.Secret
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

def sonarName = "SonarQube"
def sonarUrl = System.getenv("SONARQUBE_URL")
if (sonarUrl == null)
    return

Jenkins jenkins = Jenkins.getInstance()
SonarGlobalConfiguration sonarConfig = jenkins.getDescriptor(SonarGlobalConfiguration.class)
SonarInstallation[] sonarInstalls = sonarConfig.getInstallations()
if (sonarInstalls.find { sonarInstall -> return sonarName.equals(sonarInstall.getName()) })
    return

def credentialsId = null
def secret = null

def sonarToken = System.getenv("SONARQUBE_TOKEN")
if (sonarToken != null) {

    credentialsId = "sonarqube-token"
    secret = Secret.fromString(sonarToken)
    Credentials credentials = new StringCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, "SonarQube token", secret)
    SystemCredentialsProvider provider = SystemCredentialsProvider.getInstance()
    if (!provider.getCredentials().contains(credentials))
        provider.getStore().addCredentials(Domain.global(), credentials)

}

sonarInstalls += new SonarInstallation(sonarName, sonarUrl, credentialsId, secret, null, null, null, null, null)
sonarConfig.setInstallations(sonarInstalls)
sonarConfig.save()
