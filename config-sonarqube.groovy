#!groovy

import jenkins.model.Jenkins
import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation

def sonarUrl = System.getenv("SONARQUBE_URL")
if (sonarUrl == null)
    return

def jenkins = Jenkins.getInstance()
def sonarConfig = jenkins.getDescriptor(SonarGlobalConfiguration.class)

def sonarInstall = new SonarInstallation("SonarQube", sonarUrl, null, null, null, null, null)
def sonarInstalls = sonarConfig.getInstallations() + sonarInstall

sonarConfig.setInstallations((SonarInstallation[]) sonarInstalls)
sonarConfig.save()
