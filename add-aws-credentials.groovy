#!groovy

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl

def awsCredentialsId = System.getenv("AWS_CREDENTIALS_ID")
def awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
def awsSecretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
if (awsCredentialsId == null || awsAccessKeyId == null || awsSecretAccessKey == null)
    return

Credentials awsCredentials = new AWSCredentialsImpl(CredentialsScope.GLOBAL, awsCredentialsId, awsAccessKeyId, awsSecretAccessKey, null)
SystemCredentialsProvider provider = SystemCredentialsProvider.getInstance()
if (!provider.getCredentials().contains(awsCredentials))
    provider.getStore().addCredentials(Domain.global(), awsCredentials)
