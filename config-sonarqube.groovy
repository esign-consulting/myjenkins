#!groovy

import jenkins.model.Jenkins
import hudson.plugins.sonar.*

def jenkins = Jenkins.getInstance()
def sonar_conf = jenkins.getDescriptor(SonarGlobalConfiguration.class)

def env = System.getenv()
def sonar_inst = new SonarInstallation("SonarQube", env.SONARQUBE_URL, null, null, null, null, null)

def sonar_installations = sonar_conf.getInstallations() + sonar_inst
sonar_conf.setInstallations((SonarInstallation[]) sonar_installations)
sonar_conf.save()
