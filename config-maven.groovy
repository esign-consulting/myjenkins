#!groovy

import jenkins.model.Jenkins
import hudson.tasks.*
import hudson.tools.*
import hudson.util.DescribableList

Jenkins jenkins = Jenkins.getInstance()
def mavenDesc = jenkins.getExtensionList(Maven.DescriptorImpl.class)[0]

def isp = new InstallSourceProperty()
def autoInstaller = new Maven.MavenInstaller("3.5.4")
isp.installers.add(autoInstaller)

def proplist = new DescribableList<ToolProperty<?>, ToolPropertyDescriptor>()
proplist.add(isp)

def installation = new Maven.MavenInstallation("M3", "", proplist)
mavenDesc.setInstallations(installation)
mavenDesc.save()
