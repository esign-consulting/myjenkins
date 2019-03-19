#!groovy

import jenkins.model.Jenkins
import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation

def sonarName = "SonarQube"
def sonarUrl = System.getenv("SONARQUBE_URL")
if (sonarUrl == null)
    return

Jenkins jenkins = Jenkins.getInstance()
SonarGlobalConfiguration sonarConfig = jenkins.getDescriptor(SonarGlobalConfiguration.class)

SonarInstallation[] sonarInstalls = sonarConfig.getInstallations()
if (sonarInstalls.find { sonarInstall -> return sonarName.equals(sonarInstall.getName()) })
    return

sonarInstalls += new SonarInstallation(sonarName, sonarUrl, null, null, null, null, null)
sonarConfig.setInstallations(sonarInstalls)
sonarConfig.save()
