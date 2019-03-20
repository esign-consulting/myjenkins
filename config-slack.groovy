#!groovy

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import hudson.util.Secret
import jenkins.model.Jenkins
import jenkins.plugins.slack.SlackNotifier.DescriptorImpl
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

def slackWorkspace = System.getenv("SLACK_WORKSPACE")
def slackToken = System.getenv("SLACK_TOKEN")
if (slackWorkspace == null || slackToken == null)
    return

def credentialsId = "slack-token"
Credentials credentials = new StringCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, null, Secret.fromString(slackToken))
SystemCredentialsProvider provider = SystemCredentialsProvider.getInstance()
if (!provider.getCredentials().contains(credentials))
    provider.getStore().addCredentials(Domain.global(), credentials)

Jenkins jenkins = Jenkins.getInstance()
DescriptorImpl slackDescriptor = jenkins.getDescriptorByType(DescriptorImpl.class)

slackDescriptor.setTeamDomain(slackWorkspace)
slackDescriptor.setTokenCredentialId(credentialsId)
slackDescriptor.save()
